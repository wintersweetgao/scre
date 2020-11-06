/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initpatent_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#patent_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_patent_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#patent_btn_add").click(function(){
		$("#patent_save").dialog("setTitle","新增专利成果");
		$('#patent_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#patent_btn_save").click(function(){
		var patentName=$("#txt_patentName").textbox('getValue');//专利名称
		if(patentName==""){
			alert("专利名称为空,请重新输入！");
			$("#txt_patentName").next('span').find('input').focus();
			return false;
		}
		var patentNumber=$("#txt_patentNumber").textbox('getValue');//专利证号
		if(patentNumber==""){
			alert("专利证号为空,请重新输入！");
			$("#txt_patentNumber").next('span').find('input').focus();
			return false;
		} 
		var patentCompany=$("#txt_patentCompany").textbox('getValue');//专利鉴定单位
		if(patentCompany==""){
			alert("专利鉴定单位为空,请重新输入！");
			$("#txt_patentCompany").next('span').find('input').focus();
			return false;
		} 
		
		var patentPerson=$("#txt_patentPerson").combobox('getValue');//完成人
		if(patentPerson==""){
			alert("完成人为空,请重新输入！");
			$("#txt_patentPerson").next('span').find('input').focus();
			return false;
		} 
		
		if($("#txt_patentId").val()!=""){//修改
			var url=path+"/review/updPatent";
			var postData=$("#patent_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#patent_saveForm").form('reset');
					$('#tbl_patent_detail').datagrid('load');
					$('#patent_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/savePatent";
			var postData=$("#patent_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#patent_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#patent_saveForm").form('reset');
					$('#tbl_patent_detail').datagrid('load');
					$('#patent_save').dialog('close');
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
	$("#patent_btn_edit").click(function(){
		$("#patent_save").dialog("setTitle","修改专利成果");
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$('#patent_save').dialog('open');
			$("#txt_patentId").val(row.patentId);
			$("#txt_patentName").textbox("setValue",row.patentName);
			$("#txt_patentNumber").textbox("setValue",row.patentNumber);
			$("#txt_screType").textbox("setValue",row.screType);
			$("#txt_patentType").combobox("setValue",row.screTopic);
			$("#txt_patentCompany").textbox("setValue",row.patentCompany);
			$("#txt_patentPerson").combobox("setValue",row.patentPerson);
			$("#txt_inOrder").textbox("setValue",row.inOrder);
			$('#patent_save').dialog('open');
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#patent_btn_cancel").click(function(){
		$("#txt_patentId").val("");
		$("#txt_patentName").textbox("setValue","");
		$("#txt_patentNumber").textbox("setValue","");
		$("#txt_patentType").combobox("setValue","");
		$("#txt_patentCompany").textbox("setValue","");
		$("#txt_patentPerson").combobox("setValue","");
		$("#txt_inOrder").textbox("setValue",1);
		$('#patent_save').dialog('close');
	});
	 
	$("#patent_btn_cancelDel").click(function(){
		$('#patent_delect').dialog('close');
	});
	$("#patent_btn_cancelSub").click(function(){
		$('#patent_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#patent_btn_del").click(function(){
		 var row = $('#tbl_patent_detail').datagrid('getSelected');
		 $("#patent_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.patentName)
			 $("#patent_delect").dialog('open');
		 }
		 $("#patent_btn_del").linkbutton('enable');
	 });
	 $("#patent_btn_delect").click(function(){
		 $("#patent_btn_delect").linkbutton('disable');
		 var url=path+"/review/delPatent";
		 var row = $('#tbl_patent_detail').datagrid('getSelected');
		 var memo="删除专利名称："+row.patentName;
		 var postData={"patentId":row.patentId,"memo":memo}; 
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_patent_detail').datagrid('load');
				 $("#patent_btn_delect").linkbutton('enable');
				 $('#patent_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
			 $("#patent_btn_delect").linkbutton('enable');
		 }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
	 });
	 //提交界面
	 $("#patent_btn_sub").click(function(){
		 var row = $('#tbl_patent_detail').datagrid('getSelected');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 if(row.status=="CApprove"){
				 alert("该信息已提交，请勿重复提交");
			 }else{
				 $("#td_submitMess").text("您确认提交记录："+row.patentName+",提交之后记录将不能修改或删除！")
				 $("#patent_submit").dialog('open');
			 }
		 }
	 });
	 $("#patent_btn_submit").click(function(){
		 var row = $('#tbl_patent_detail').datagrid('getSelected');
		 if(row.fileCount==0){
			 alert("附件资料不全，不能提交！");
			 return false;
		 }
		 $("#patent_btn_submit").linkbutton('disable');
		 var url=path+"/review/submitPatent";
		 var memo="提交专利："+row.patentName
		 var postData={"patentId":row.patentId,"memo":memo};  
		 $.post(url,postData,function(data){
		    	var mess=eval(data).mess;
		    	if ("succ"==mess){
		    		alert("提交成功！");
		    		$("#patent_btn_submit").linkbutton('enable');
		    		//需要刷新相关记录
					var rowIndex=$('#tbl_patent_detail').datagrid('getRowIndex',row);
					row.status="CApprove";
					row.statusValue=getStatusValByKey(row.status);//更新状态值
					$('#tbl_patent_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setButtonStatus(row);
					$('#patent_submit').dialog('close');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		});
	function initpatent_detail(){
		var url=path+"/review/queryPatent";
		$('#tbl_patent_detail').datagrid({
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
		    queryParams: $('#patent_queryForm').serializeJSON(),
		    columns:[[
				{field:'patentName',title:'专利名称',width:150},
				{field:'patentNumber',title:'专利证号',width:100},
				{field:'screTopic',title:'专利类型',width:100},
				{field:'patentCompany',title:'专利鉴定单位',width:100},
				{field:'patentPerson',title:'完成人',width:100},
				{field:'screType',title:'科研达标类型',width:100},
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
	    		$("#patent_btn_edit").linkbutton('disable');
	    		$("#patent_btn_sub").linkbutton('disable');
	    		$("#patent_btn_del").linkbutton('disable');
	    		$("#patent_btn_file").linkbutton('disable');
	    	}else{
	    		$("#patent_btn_edit").linkbutton('enable');
	    		$("#patent_btn_sub").linkbutton('enable');
	    		$("#patent_btn_del").linkbutton('enable');
	    		$("#patent_btn_file").linkbutton('enable');
	    	}
		}
	}
	//上传文件
	$("#patent_btn_file").click(function(){
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#patent_material").uploadMaterial({
				datagrid_id:'tbl_patent_detail',
    			materialType:'RptPatent',
    			screNameTitle:row.patentName,
    			screId:row.patentId
			});
	    }
	 });
	$("#patent_btn_log").click(function(){
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#patent_log").getReviewLog({
				tblName:'Rptpatent',
    			screId:row.patentId
			});
		}
	});
	
});