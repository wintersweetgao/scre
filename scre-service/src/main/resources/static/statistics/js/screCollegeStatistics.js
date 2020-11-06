/**
 * 
 */
$(document).ready(function(){
	
	var path=getRootPath();
	var cycleName=$("#txt_cycleName").val();
//	//获取科研成果
	getScreStatisByCollegeClassType(); 
//	//达标率
	getScreStatisByCollegePercent();
	//5年内的达标情况
	getScreStatisByCollegeCycle();
	//各系科研作品数量
	getScreStatisByRptCountCollege();
	function getScreStatisByRptCountCollege(){
		var url=path+"/statistics/getScreStatisByRptCountCollege";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取本校各系的科研作品数量！");
				return false;
			}else{
				var screRptCount=new Array();
				var flag=true;
				for(var i=0;i<data.length;i++){
					var tmp={};
					tmp.name=data[i].staffParentDepart;
					tmp.y=data[i].rptCount;
					if (data[i].rptCount>0&&flag){
						flag=false;
						tmp.sliced=true; //饼图分离设置
						tmp.selected=true;
					}
					screRptCount[i]=tmp;
				}
				showScreRptCountPie(screRptCount);
			}
		});
	}
	
	function getScreStatisByCollegeCycle(){
		var url=path+"/statistics/getScreStatisByCollegeCycle";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取科研达标周期等信息");
				return false;
			}else{
				var xAxis_cycles=new Array();//定义x轴数据  （周期）
//				{
//		        name: 'Installation',
//		        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
//		    }
				var yAxis_datas=new Array(); //定义数据项
				var cycles=data.cycles;
				var screDatas=data.screDatas;//统计数据
				for(var i=0;i<cycles.length;i++){
					xAxis_cycles[i]=cycles[cycles.length-1-i].cycleName;
				}
				var j=0;
				for (var key in screDatas){
					var tmp={};
					var tmp_data=new Array();
					var keyVal=screDatas[key];
					for(var k=0;k<keyVal.length;k++){
						tmp_data[k]=keyVal[k].rptCount;
					}
					tmp["name"]=key;
					tmp["data"]=tmp_data;
					yAxis_datas[j]=tmp;
					j++;
				}
				//近 5个周期的 占比
				showScreResultLines(xAxis_cycles,yAxis_datas);
			}
		});
	}
	
	
	function getScreStatisByCollegePercent(){
		var url=path+"/statistics/getScreStatisByCollegePercent";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取参与科研达标的人员信息！");
				return false;
			}else{
				var screCount=Number(data.screCount);
				var reachCount=Number(data.reachCount);
				var screPercent=new Array();
				if (screCount>0){
					screPercent[0]=Number(Number(reachCount*100/screCount).toFixed(2));
				}else{
					screPercent[0]=0.00;
				}
				//显示速度仪器
				showScrePercent(screPercent);
			}
		});
	}
	function getScreStatisByCollegeClassType(){
		var url=path+"/statistics/getScreStatisByCollegeClassType";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取科研成果！");
				return false;
			}else{
				var screTotal=0;//统计总数
				var screType=new Array();
				var screCount=new Array();
				for(var i=0;i<data.length;i++){
					$("#tr_scremess").find("td[id='td_"+data[i].className+"']").text(data[i].rptCount);
					screTotal+=Number(data[i].rptCount);
					screType[i]=data[i].screType;
					screCount[i]=data[i].rptCount;
				}
				$("#td_screTotal").text(screTotal);
				
				showScreBar(screType,screCount);
			}
		});
	}
	
	
	function showScreBar(screType,screCount){
		//隐藏loading
		$("#div_screBar").css("display","block");
		hideLoad('screBar-loading');
		Highcharts.chart('div_screBar', {
		    chart: {
		        type: 'column'
		    },
		    title: {
		        text: cycleName+'科研成果'
		    },		    
		    xAxis: {
		        categories: screType,
		        crosshair: true
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: '成果数量'
		        }
		    },
		    tooltip: {
		        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		            '<td style="padding:0"><b>{point.y:.0f} </b></td></tr>',
		        footerFormat: '</table>',
		        shared: true,
		        useHTML: true
		    },
		    credits:{
		        enabled:false // 禁用版权信息
		    },
		    plotOptions: {
		        column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		        }
		    },
		    series: [{
		        name: '科研成果',
		        data: screCount
		    }]
		});
	}
	
	function showScrePercent(screPercent){
		//隐藏loading
		hideLoad('screPercent-loading');
		$("#div_screPercent").css("display","block");
		Highcharts.chart('div_screPercent', {
		    chart: {
		        type: 'gauge',
		        plotBorderWidth: 1,
		        plotBackgroundColor: {
		            linearGradient: { x1: 0, y1: 0 },
		            stops: [
		                [0, '#FFF4C6'],
		                [0.3, '#FFFFFF'],
		                [1, '#FFF4C6']
		            ]
		        },
		        plotBackgroundImage: null,
		        height: 200
		    },
		    credits:{
		        enabled:false // 禁用版权信息
		    },
		    title: {
		        text: cycleName+'科研达标率'
		    },

		    pane: [{
		        startAngle: -45,
		        endAngle: 45,
		        background: null,
		        center: ['50%', '145%'],
		        size: 300
		    }],
		    tooltip: {
		        enabled: false
		    },

		    yAxis: [{
		        min: 0,
		        max: 100,
		        minorTickPosition: 'outside',
		        tickPosition: 'outside',
		        labels: {
		            rotation: 'auto',
		            distance: 20
		        },
		        plotBands: [{
		            from: 0,
		            to: 50,
		            color: '#C02316',
		            innerRadius: '100%',
		            outerRadius: '105%'
		        }],
		        pane: 0,
		        title: {
		            text: '<br/><span style="font-size:8px">达标率'+screPercent[0]+'%</span>',
		            y: -40
		        }
		    }],

		    plotOptions: {
		        gauge: {
		            dataLabels: {
		                enabled: true
		            },
		            dial: {
		                radius: '100%'
		            }
		        }
		    },

		    series: [{
		        name: '达标率',
		        data: screPercent,
		        yAxis: 0
		    }]

		},

		// Let the music play
		function (chart) {
		    setInterval(function () {
		        if (chart.series) { // the chart may be destroyed
		            var left = chart.series[0].points[0],
		                leftVal,
		                inc = (Math.random() - 0.5) * 1;

		            leftVal = left.y + inc;
		            if (leftVal < 0 || leftVal > 100) {
		                leftVal = left.y + inc;
		            }

		            left.update(leftVal, false);
		            chart.redraw();
		        }
		    }, 500);

		});
		
	}
	
	
	function showScreResultLines(xAxis_cycles,yAxis_datas){
		//隐藏loading
		hideLoad('screLineResults-loading');
		$("#div_screLineResults").css("display","block");
		Highcharts.chart('div_screLineResults', {
		    title: {
		        text: '近5个科研周期科研成果趋势'
		    },

		    yAxis: {
		        title: {
		            text: '科研成果数量'
		        }
		    },
		    legend: {
		        layout: 'vertical',
		        align: 'right',
		        verticalAlign: 'middle'
		    },
		    credits:{
		        enabled:false // 禁用版权信息
		    },
		    plotOptions: {
		        series: {
		            label: {
		                connectorAllowed: false
		            }
		        },
		        line: {
		            dataLabels: {
		                enabled: true
		            },
		            enableMouseTracking: false
		        }
		    },
		    xAxis: {
		        categories: xAxis_cycles
		    },
		    series: yAxis_datas,
		    responsive: {
		        rules: [{
		            condition: {
		                maxWidth: 500
		            },
		            chartOptions: {
		                legend: {
		                    layout: 'horizontal',
		                    align: 'center',
		                    verticalAlign: 'bottom'
		                }
		            }
		        }]
		    }

		});
	}
	//展示各个系科研数量
	function showScreRptCountPie(screRptCount){
		//隐藏loading
		hideLoad('screRptCount-loading');
		$("#div_screRptCount").css("display","block");
		Highcharts.chart('div_screRptCount', {
		    chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false,
		        type: 'pie'
		    },
		    title: {
		        text: cycleName+'科研成果数量占比'
		    },
		    tooltip: {
		        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		    },
		    plotOptions: {
		        pie: {
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
		            }
		        }
		    },
		    credits:{
		        enabled:false // 禁用版权信息
		    },
		    series: [{
		        name: '成果占比',
		        colorByPoint: true,
		        data: screRptCount
		    }]
		    
		});
	}
	
	function hideLoad(loadingId){
		$("#"+loadingId).css("display","none");
	}
	
});