/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//获取科研成果
	getScreStatisByClassType();
	
	function getScreStatisByClassType(){
		var url=path+"/statistics/getScreStatisByClassType";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取本系科研成果！");
				return false;
			}else{
				var screType=new Array();
				var screCount=new Array();
				var screMark=new Array();
				var flag=true;
				
				for(var i=0;i<data.length;i++){
					$("#tr_scremess").find("td[id='td_"+data[i].className+"']").text(data[i].rptCount);
					screType[i]=data[i].screType;
					screCount[i]=data[i].rptCount;
					var tmp={};
					tmp.name=data[i].screType;
					tmp.y=Number(data[i].expectedMark);
					if (data[i].expectedMark>0&&flag){
						flag=false;
						tmp.sliced=true; //饼图分离设置
						tmp.selected=true;
					}
					screMark[i]=tmp;
				}
				
				statics(screType,screCount);
				devote(screMark);
			}
		});
	}
	
	function statics(screType,screCount){
		Highcharts.chart('div_statics', {
		    chart: {
		        type: 'column'
		    },
		    title: {
		        text: '本系成果'
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
		        name: '成果',
		        data: screCount
		    }]
		});
	}
	function devote(screMark){
		Highcharts.chart('div_devote', {
		    chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false,
		        type: 'pie'
		    },
		    title: {
		        text: '本系科研积分占比'
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
		        name: '积分',
		        colorByPoint: true,
		        data: screMark
		    }]
		    
		});
	}
});