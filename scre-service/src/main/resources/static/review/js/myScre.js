/**
 * 
 */
$(document).ready(function(){
	var flag=true;
	var path=getRootPath();
	//初始化
	getGrantBySysCode();
	initDeparts();
	function getGrantBySysCode(){
		var url=path+"/review/getScreCountByCode";
		$.post(url,"",function(data){
			if (data==""){
				alert("未能获取个人科研成果！");
				return false;
			}else{
				var expectedMarks=data.expectedMarks;
				var totalCount=0;
				var totalMark=0;
				var approveMarkCount=data.approveMarkCount;//通过审批的积分
				var screType=new Array();
				var screCount=new Array();
				var screMark=new Array();
				var flag=true;
				for(var i=0;i<expectedMarks.length;i++){
					$("#tr_scremess").find("td[id='td_"+expectedMarks[i].className+"']").text(expectedMarks[i].rptCount);
					totalCount+=Number(expectedMarks[i].rptCount);
					totalMark+=Number(expectedMarks[i].expectedMark);
					screType[i]=expectedMarks[i].screType;
					screCount[i]=expectedMarks[i].rptCount;
					
					var tmp={};
					tmp.name=expectedMarks[i].screType;
					tmp.y=expectedMarks[i].expectedMark;
					if (expectedMarks[i].expectedMark>0&&flag){
						flag=false;
						tmp.sliced=true; //饼图分离设置
						tmp.selected=true;
					}
					screMark[i]=tmp;
				}
				$("#sp_totalCount").text(totalCount);
				$("#sp_totalMark").text(totalMark);
				$("#sp_approMark").text(approveMarkCount);
				
				var markStand=data.markStand;
				if (markStand==""){
					$("#td_overMess").html("未获取到您的科研积分标准");
				}else if (markStand=="-1"){
					$("#td_overMess").html("您入职未满一年，无需参与本轮达标");
				}else{
					if (approveMarkCount>=markStand){
						$("#td_overMess").html("恭喜您，您已经完成本轮达标！");
					}else{
						$("#td_overMess").html("您还差<span style='font-size: 18px;color: red'>"+(markStand-approveMarkCount)+"</span>科研积分完成达标，加油！");
					}
				}
				statics(screType,screCount);
				devote(screMark);
			}
		})
	}
	
	function statics(screType,screCount){
		Highcharts.chart('div_statics', {
		    chart: {
		        type: 'column'
		    },
		    title: {
		        text: '个人成果数量'
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
		        text: '个人科研积分占比'
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
	/**
	 * 点击按钮
	 */
	$("#myscre_btn_information").click(function(){
		flag=true;
		$("#myscre_information").dialog("setTitle","修改个人信息");
		$("#txt_postIds").combobox("setValues",$("#td_postId").val());
		$("#txt_parentDepartId").combobox("setValue",$("#td_parentDepartId").val());
		
		$('#myscre_information').dialog('open');
	});
	$("#myscre_btn_password").click(function(){
		$("#myscre_password").dialog("setTitle","修改个人密码");
		$('#myscre_password').dialog('open');
	});
	$("#myscre_btn_cancelinf").click(function(){
		$('#myscre_information').dialog('close');
	});
	$("#myscre_btn_cancelpas").click(function(){
		$('#myscre_password').dialog('close');
	});
	//修改密码
	$("#myscre_btn_savepas").click(function(){
		if ($('#txt_myscre_password_old').passwordbox("getValue")==""){
			alert("请输入系统当前密码！");
			$("#txt_myscre_password_old").next('span').find('input').focus();
			return false; 
		}	
		if ($('#txt_myscre_password_new').passwordbox("getValue")==""){
			alert("请输入新密码！");
			$("#txt_myscre_password_new").next('span').find('input').focus();
			return false; 
		}
		if ($('#txt_myscre_password_again').passwordbox("getValue")!=$('#txt_myscre_password_new').passwordbox("getValue")){
			alert("两次输入的新密码不匹配！");
			$("#txt_myscre_password_again").next('span').find('input').focus();
			return false; 
		}
		var url=path+"/review/updMyPass";
		var postData={"myscre_password_old":$('#txt_myscre_password_old').passwordbox("getValue"),
				"myscre_password_new":$('#txt_myscre_password_new').passwordbox("getValue")}
		$.post(url,postData,function(data){
			if (data=="success"){
				alert("密码修改成功，请重新登录");
				$('#myscre_password').dialog('close');
				top.location.href = getRootPath();
			}else{
				alert(data);
			}
		})
	});
	
	//修改个人信息
	$("#myscre_btn_saveinf").click(function(){
		var userCode=$("#txt_userCode").textbox('getValue');
		var staffName=$("#txt_staffName").textbox('getValue');
		var staffPhone=$("#txt_staffPhone").textbox('getValue');	
		var staffAddr=$("#txt_staffAddr").textbox('getValue');
		//去空格
		var updUserCode= userCode.replace(/^\s+/,'').replace(/\s+$/,'');
		var updStaffName= staffName.replace(/^\s+/,'').replace(/\s+$/,'');
		var updStaffPhone= staffPhone.replace(/^\s+/,'').replace(/\s+$/,'');	
		var updStaffAddr= staffAddr.replace(/^\s+/,'').replace(/\s+$/,'');
		if (updUserCode.length<=0){
			alert("工号不能为空");
			$("#txt_userCode").next('span').find('input').focus();
			return false;
		}else if (updStaffName.length<=0){
			alert("姓名不能为空");
			$("#txt_staffName").next('span').find('input').focus();
			return false;
		}else if (updStaffPhone.length<=0){
			alert("联系方式不能为空");
			$("#txt_staffPhone").next('span').find('input').focus();
			return false;
		}else if ($("#txt_staffTitle").textbox('getValue')==""){
			alert("职称不能为空");
			$("#txt_staffTitle").next('span').find('input').focus();
			return false;
		}else if ($("#txt_staffEducation").textbox('getValue')==""){
			alert("教育程度不能为空");
			$("#txt_staffEducation").next('span').find('input').focus();
			return false;
		}else if ($("#txt_parentDepartId").textbox('getValue')==""){
			alert("所属系部处不能为空");
			$("#txt_parentDepartId").next('span').find('input').focus();
			return false;
		}else if ($("#txt_postIds").combobox('getValues')==""){
			alert("职务不能为空");
			$("#txt_postIds").next('span').find('input').focus();
			return false;
		}else if (updUserCode.length>15){
			alert("工号不能过长");
			$("#txt_userCode").next('span').find('input').focus();
			return false;
		}else if (updStaffName.length>15){
			alert("姓名不能过长");
			$("#txt_staffName").next('span').find('input').focus();
			return false;	
		}else if(!((/0\d{2,3}-\d{7,8}/.test(staffPhone)) || updStaffPhone.length == 7 || updStaffPhone.length == 8 || (/^1(3|4|5|6|7|8|9)\d{9}$/.test(staffPhone)))){
			alert("联系方式输入有误，请重新输入!");
			$("#txt_staffPhone").next('span').find('input').focus();
			return false;
		}else if (updStaffAddr.length>50){
			alert("地址不能过长");
			$("#txt_staffAddr").next('span').find('input').focus();
			return false;
		}else{
		    var url=path+"/review/updUserByCode";
		    var postData=$("#myscre_userForm").serialize();
		    $.post(url,postData,function(data){
			    var mess=eval(data);
			    if ("succ"==mess){
				    alert("修改成功,请重新登录！");
				    $('#myscre_information').dialog('close');
					top.location.href = getRootPath();
			    }else{
			    	alert(mess);
			    }
		    });
		}
	});
	//获取部门以及复选框
	function initDeparts(){
		url=path + "/review/getDepartOne";
		$.post(url,"",function(data){	
			var lsDepart=eval(data);
			$("#txt_parentDepartId").combobox({
			    data:lsDepart,
			    valueField:'departId',
			    textField:'departName',
			    onSelect: function(record){
			    	var departId=Number(record.departId);
			    	initGetDepart(departId);
				}
			});
		
		});
	}	
	
	function initGetDepart(departId){
		url = path + "/review/getDepartTwo";
		var postData={"parentDepartId":departId};
		$.post(url, postData, function(data) {
			var depart = eval(data);
			$("#txt_departId").combobox({
			    data:depart,
			    valueField:'departId',
			    textField:'departName'
			});
			if (flag){
				$("#txt_departId").combobox("setValue",$("#td_departId").val());
				flag=false;
			}
			
		});
	}
	
})