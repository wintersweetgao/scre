/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initAcademic_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#academic_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_academic_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#academic_btn_add").click(function(){
		$("#academic_save").dialog("setTitle","新增学术报告");
		$('#academic_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#academic_btn_save").click(function(){
		var academicTopic=$("#txt_academicTopic").textbox('getValue');//报告主题
		if(academicTopic==""){
			alert("报告主题为空,请重新输入！");
			$("#txt_academicTopic").next('span').find('input').focus();
			return false;
		}
		var academicDate=$("#txt_academicDate").datebox('getValue');//主讲日期
		if(academicDate==""){
			alert("主讲日期为空,请重新输入！");
			$("#txt_academicDate").next('span').find('input').focus();
			return false;
		} 
		
		if($("#txt_academicId").val()!=""){//修改
			var url=path+"/review/updAcademic";
			var postData=$("#academic_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#academic_saveForm").form('reset');
					$('#tbl_academic_detail').datagrid('load');
					$('#academic_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/saveAcademic";
			var postData=$("#academic_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#academic_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#academic_saveForm").form('reset');
					$('#tbl_academic_detail').datagrid('load');
					$('#academic_save').dialog('close');
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
	$("#academic_btn_edit").click(function(){
		$("#academic_save").dialog("setTitle","修改学术报告");
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$("#txt_academicId").val(row.academicId);
			$("#txt_academicTopic").textbox("setValue",row.academicTopic);
			$("#txt_academicDate").datebox("setValue",row.academicDate);
			$("#txt_academicMemo").textbox("setValue",row.academicMemo);
			$('#academic_save').dialog('open');
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#academic_btn_cancel").click(function(){
		$("#txt_academicId").val("");
		$("#txt_academicTopic").textbox("setValue","");
		$("#txt_academicDate").datebox("setValue","");
		$("#txt_academicMemo").textbox("setValue","");
		$('#academic_save').dialog('close');
	});
	 
	$("#academic_btn_cancelDel").click(function(){
		$('#academic_delect').dialog('close');
	});
	$("#academic_btn_cancelSub").click(function(){
		$('#academic_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#academic_btn_del").click(function(){
		 var row = $('#tbl_academic_detail').datagrid('getSelected');
		 $("#academic_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.academicTopic)
			 $("#academic_delect").dialog('open');
		 }
		 $("#academic_btn_del").linkbutton('enable');
	 });
	 $("#academic_btn_delect").click(function(){
		 $("#academic_btn_delect").linkbutton('disable');
		 var url=path+"/review/delAcademic";
		 var row = $('#tbl_academic_detail').datagrid('getSelected');
		 var memo="删除学术报告："+row.academicTopic;
		 var postData={"academicId":row.academicId,"memo":memo}; 
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_academic_detail').datagrid('load');
				 $("#academic_btn_delect").linkbutton('enable');
				 $('#academic_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
			 $("#academic_btn_delect").linkbutton('enable');
		 }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
	 });
	 //提交界面
	 $("#academic_btn_sub").click(function(){
		 var row = $('#tbl_academic_detail').datagrid('getSelected');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 if(row.status=="CApprove"){
				 alert("该信息已提交，请勿重复提交");
			 }else{
				 $("#td_submitMess").text("您确认提交记录："+row.academicTopic+",提交之后记录将不能修改或删除！")
				 $("#academic_submit").dialog('open');
			 }
		 }
	 });
	 $("#academic_btn_submit").click(function(){
		 var row = $('#tbl_academic_detail').datagrid('getSelected');
		 if(row.fileCount==0){
			 alert("附件资料不全，不能提交！");
			 return false;
		 }
		 $("#academic_btn_submit").linkbutton('disable');
		 var url=path+"/review/submitacademic";
		 var memo="提交学术报告："+row.academicTopic
		 var postData={"academicId":row.academicId,"memo":memo};  
		 $.post(url,postData,function(data){
		    	var mess=eval(data).mess;
		    	if ("succ"==mess){
		    		alert("提交成功！");
		    		$("#academic_btn_submit").linkbutton('enable');
		    		//需要刷新相关记录
					var rowIndex=$('#tbl_academic_detail').datagrid('getRowIndex',row);
					row.status="CApprove";
					row.statusValue=getStatusValByKey(row.status);//更新状态值
					$('#tbl_academic_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setButtonStatus(row);
					$('#academic_submit').dialog('close');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		});
	function initAcademic_detail(){
		var url=path+"/review/queryAcademic";
		$('#tbl_academic_detail').datagrid({
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
		    queryParams: $('#academic_queryForm').serializeJSON(),
		    columns:[[
				{field:'academicTopic',title:'报告主题',width:150},
				{field:'academicDate',title:'主讲日期',width:100},
				{field:'screTopic',title:'学术类型',width:100},
				{field:'academicMemo',title:'报告简介',width:100},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }},
				{field:'statusValue',title:'状态',width:80,formatter:function(value,row,index){
					return row.statusValue;
				}}
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
	    		$("#academic_btn_edit").linkbutton('disable');
	    		$("#academic_btn_sub").linkbutton('disable');
	    		$("#academic_btn_del").linkbutton('disable');
	    		$("#academic_btn_file").linkbutton('disable');
	    	}else{
	    		$("#academic_btn_edit").linkbutton('enable');
	    		$("#academic_btn_sub").linkbutton('enable');
	    		$("#academic_btn_del").linkbutton('enable');
	    		$("#academic_btn_file").linkbutton('enable');
	    	}
		}
	}
	//上传文件
	$("#academic_btn_file").click(function(){
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#academic_material").uploadMaterial({
				datagrid_id:'tbl_academic_detail',
    			materialType:'RptAcademic',
    			screNameTitle:row.academicTopic,
    			screId:row.academicId
			});
	    }
	 });
	$("#academic_btn_log").click(function(){
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#academic_log").getReviewLog({
				tblName:'RptAcademic',
    			screId:row.academicId
			});
		}
	});
	
});