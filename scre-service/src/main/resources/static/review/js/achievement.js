/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initAchievement_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#achievement_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_achievement_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#achievement_btn_add").click(function(){
		$("#achievement_save").dialog("setTitle","新增科研获奖");
		$('#achievement_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#achievement_btn_save").click(function(){
		var achievementName=$("#txt_achievementName").textbox('getValue');//成果名称
		if(achievementName==""){
			alert("成果名称为空,请重新输入！");
			$("#txt_achievementName").next('span').find('input').focus();
			return false;
		}
		var achievementType=$("#txt_achievementType").textbox('getValue');//成果类型
		if(achievementType==""){
			alert("成果类型为空,请重新输入！");
			$("#txt_achievementType").next('span').find('input').focus();
			return false;
		}
		var awardName=$("#txt_awardName").textbox('getValue');//成果类型
		if(awardName==""){
			alert("获奖名称为空,请重新输入！");
			$("#txt_awardName").next('span').find('input').focus();
			return false;
		}
		var screTopic=$("#txt_screTopic").textbox('getValue');//获奖等级
		if(screTopic==""){
			alert("获奖等级为空,请重新输入！");
			$("#txt_screTopic").next('span').find('input').focus();
			return false;
		} 
		var awardDepart=$("#txt_awardDepart").textbox('getValue');//颁奖部门
		if(awardDepart==""){
			alert("颁奖部门为空,请重新输入！");
			$("#txt_awardDepart").next('span').find('input').focus();
			return false;
		} 
		var publishDate=$("#txt_publishDate").datebox('getValue');//发表日期
		if(publishDate==""){
			alert("获奖日期为空,请重新输入！");
			$("#txt_publishDate").next('span').find('input').focus();
			return false;
		} 
		
		if($("#txt_achievementId").val()!=""){//修改
			var url=path+"/review/updAchievement";
			var postData=$("#achievement_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#achievement_saveForm").form('reset');
					$('#tbl_achievement_detail').datagrid('load');
					$('#achievement_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/saveAchievement";
			var postData=$("#achievement_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#achievement_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#achievement_saveForm").form('reset');
					$('#tbl_achievement_detail').datagrid('load');
					$('#achievement_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}
			
	});
	/**
	 * 修改页面
	 */
	$("#achievement_btn_edit").click(function(){
		$("#achievement_save").dialog("setTitle","修改获奖成果");
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$('#achievement_save').dialog('open');
			$("#txt_achievementId").val(row.achievementId);
			$("#txt_achievementName").textbox("setValue",row.achievementName);
			$("#txt_achievementType").textbox("setValue",row.achievementType);
			$("#txt_awardName").textbox("setValue",row.awardName);
			$("#txt_screTopic").combobox("setValue",row.screTopic);
			$("#txt_awardDepart").textbox("setValue",row.awardDepart);
			$("#txt_firstPerson").textbox("setValue",row.firstPerson);
			$("#txt_publishDate").datebox("setValue",row.publishDate);
			// if (row.achievementEi=="是"){
			// 	$("#rdb_achievementEiYes").radiobutton("check",0);
			// }else{
			// 	$("#rdb_achievementEiNo").radiobutton("check",1);
			// }
			$("#txt_inOrder").textbox("setValue",row.inOrder);
			$('#achievement_save').dialog('open');
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#achievement_btn_cancel").click(function(){
		$("#txt_achievementId").val("");
		$("#txt_achievementName").textbox("setValue","");
		$("#txt_achievementType").textbox("setValue","");
		$("#txt_awardName").textbox("setValue","");
		$("#txt_screTopic").textbox("setValue","");
		$("#txt_ awardDepart").textbox("setValue","");
		$("#txt_firstPerson").textbox("setValue","");
		// $("#rdb_achievementEiYes").radiobutton("check",0);
		$("#txt_inOrder").textbox("setValue",1);
		$('#achievement_save').dialog('close');
	});
	 
	$("#achievement_btn_cancelDel").click(function(){
		$('#achievement_delect').dialog('close');
	});
	$("#achievement_btn_cancelSub").click(function(){
		$('#achievement_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#achievement_btn_del").click(function(){
		 var row = $('#tbl_achievement_detail').datagrid('getSelected');
		 $("#achievement_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.achievementName)
			 $("#achievement_delect").dialog('open');
		 }
		 $("#achievement_btn_del").linkbutton('enable');
	 });
	 $("#achievement_btn_delect").click(function(){
		 $("#achievement_btn_delect").linkbutton('disable');
		 var url=path+"/review/delAchievement";
		 var row = $('#tbl_achievement_detail').datagrid('getSelected');
		 var memo="删除科研获奖："+row.achievementName;
		 var postData={"achievementId":row.achievementId,"memo":memo};
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_achievement_detail').datagrid('load');
				 $("#achievement_btn_delect").linkbutton('enable');
				 $('#achievement_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
		 }).error(function(error){
			 var mess=eval(error);
			 alert(mess.responseText);
			 $("#achievement_btn_delect").linkbutton('enable');
		 });
	 });
	 //提交界面
	 $("#achievement_btn_sub").click(function(){
		 var row = $('#tbl_achievement_detail').datagrid('getSelected');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 if(row.status=="CApprove"){
				 alert("该信息已提交，请勿重复提交");
			 }else{
				 $("#td_submitMess").text("您确认提交记录："+row.achievementName+",提交之后记录将不能修改或删除！")
				 $("#achievement_submit").dialog('open');
			 }
		 }
	 });
	 $("#achievement_btn_submit").click(function(){
		 var row = $('#tbl_achievement_detail').datagrid('getSelected');
		 if(row.fileCount==0){
			 alert("附件资料不全，不能提交！");
			 return false;
		 }
		 $("#achievement_btn_submit").linkbutton('disable');
		 var url=path+"/review/submitAchievement";
		 var memo="提交成果："+row.achievementName
		 var postData={"achievementId":row.achievementId,"memo":memo};
		 $.post(url,postData,function(data){
		    	var mess=eval(data).mess;
		    	if ("succ"==mess){
		    		alert("提交成功！");
		    		$("#achievement_btn_submit").linkbutton('enable');
		    		//需要刷新相关记录
					var rowIndex=$('#tbl_achievement_detail').datagrid('getRowIndex',row);
					row.status="CApprove";
					row.statusValue=getStatusValByKey(row.status);//更新状态值
					$('#tbl_achievement_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setButtonStatus(row);
					$('#achievement_submit').dialog('close');
		    	}else{
		    		alert(mess);
		    	}
		 }).error(function(error){
			 var mess=eval(error);
			 alert(mess.responseText);
		    });
		});
	function initAchievement_detail(){
		var url=path+"/review/queryAchievement";
		$('#tbl_achievement_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:true,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:true,
		    pageSize:10,
		    queryParams: $('#achievement_queryForm').serializeJSON(),
		    columns:[[
				{field:'achievementName',title:'成果名称',width:150},
				{field:'achievementType',title:'成果类型',width:100},
				{field:'awardName',title:'获奖名称',width:100},
				{field:'screTopic',title:'获奖等级',width:100},
				{field:'awardDepart',title:'颁奖部门',width:100},
				{field:'firstPerson',title:'第一完成人',width:100},
				{field:'publishDate',title:'获奖时间',width:100,formatter:function(val, row, index){
						if (val != null) {
							val=val.substr(0,10);
							return val;
						}
					}},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }},
				{field:'statusValue',title:'状态',width:80}
		    ]],
		    onBeforeLoad:function(param){  //这个param就是queryString 
		    	var pageNo = param.page; //保存下值
				delete param.page; //删掉
				param.pageNo = pageNo; //这里就是重新命名了！！！ ,j将page 改为了 pages
				var maxResults = param.rows;
				delete param.rows; //删掉
				param.maxResults = maxResults; //这里就是重新命名了！！！ ,j将page 改为了 pages
//				param["pageNo"] = param.page;
//              param["maxResults"] = param.rows;
		    },
		    onLoadSuccess:function(data){
		    	$(this).datagrid("selectRow",0);
		    	var row = $(this).datagrid('getSelected');
		    	setButtonStatus(row);
		    },
		    onClickRow: function(index,row){
		    	setButtonStatus(row);

		    },
		    onClickCell:function(index, field, value){
		    	if (field=="fileCount"){
		    		$(this).datagrid('selectRow',index);
		    		var row = $(this).datagrid('getSelected');
		    		if(value==0){
		    			alert("没有附件！");
		    			return false;
		    		}else{
		    			$("#scie_viewfile").previewImg({
		    				width:600,
		            		height:510,
		            		title:row.scieName,
		            		data:row.materials
		    			});
		    		}
		    	}
		    }
		});
	}
	$("#scie_btn_cancelview").click(function(){
		$("#scie_viewfile").dialog('close');
	});
	//修改按钮状态
	function setButtonStatus(rowData){
		if (null!=rowData){
			if (rowData.status!="BApprove"){
	    		$("#achievement_btn_edit").linkbutton('disable');
	    		$("#achievement_btn_sub").linkbutton('disable');
	    		$("#achievement_btn_del").linkbutton('disable');
	    		$("#achievement_btn_file").linkbutton('disable');
	    	}else{
	    		$("#achievement_btn_edit").linkbutton('enable');
	    		$("#achievement_btn_sub").linkbutton('enable');
	    		$("#achievement_btn_del").linkbutton('enable');
	    		$("#achievement_btn_file").linkbutton('enable');
	    	}
		}
	}
	//上传文件
	$("#achievement_btn_file").click(function(){
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#achievement_material").uploadMaterial({
				datagrid_id:'tbl_achievement_detail',
    			materialType:'RptAchievement',
    			screNameTitle:row.achievementName,
    			screId:row.achievementId
			});
	    }
	 });
	$("#achievement_btn_log").click(function(){
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#achievement_log").getReviewLog({
				tblName:'RptAchievement',
    			screId:row.achievementId
			});
		}
	});
	
});