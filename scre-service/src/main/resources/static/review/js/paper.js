/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initPaper_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#paper_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_paper_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#paper_btn_add").click(function(){
		$("#paper_save").dialog("setTitle","新增科研论文");
		$('#paper_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#paper_btn_save").click(function(){
		var paperThesis=$("#txt_paperThesis").textbox('getValue');//论文题目
		if(paperThesis==""){
			alert("论文题目为空,请重新输入！");
			$("#txt_paperThesis").next('span').find('input').focus();
			return false;
		}
		var paperJournal=$("#txt_paperJournal").textbox('getValue');//期刊名称
		if(paperJournal==""){
			alert("期刊名称为空,请重新输入！");
			$("#txt_paperJournal").next('span').find('input').focus();
			return false;
		} 
		var paperSponsor=$("#txt_paperSponsor").textbox('getValue');//主办单位
		if(paperSponsor==""){
			alert("主办单位为空,请重新输入！");
			$("#txt_paperSponsor").next('span').find('input').focus();
			return false;
		} 
		var paperDepart=$("#txt_paperDepart").textbox('getValue');//主管部门
		if(paperDepart==""){
			alert("主管部门为空,请重新输入！");
			$("#txt_paperDepart").next('span').find('input').focus();
			return false;
		} 
		var publishDate=$("#txt_publishDate").datebox('getValue');//发表日期
		if(publishDate==""){
			alert("发表日期为空,请重新输入！");
			$("#txt_publishDate").next('span').find('input').focus();
			return false;
		} 
		
		if($("#txt_paperId").val()!=""){//修改
			var url=path+"/review/updPaper";
			var postData=$("#paper_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#paper_saveForm").form('reset');
					$('#tbl_paper_detail').datagrid('load');
					clearForm();
					$('#paper_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/savePaper";
			var postData=$("#paper_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#paper_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#paper_saveForm").form('reset');
					$('#tbl_paper_detail').datagrid('load');
					clearForm();
					$('#paper_save').dialog('close');
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
	$("#paper_btn_edit").click(function(){
		$("#paper_save").dialog("setTitle","修改科研论文");
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$('#paper_save').dialog('open');
			$("#txt_paperId").val(row.paperId);
			$("#txt_paperThesis").textbox("setValue",row.paperThesis);
			$("#txt_paperJournal").textbox("setValue",row.paperJournal);
			$("#txt_screTopic").combobox("setValue",row.screTopic);
			$("#txt_paperSponsor").textbox("setValue",row.paperSponsor);
			$("#txt_paperDepart").textbox("setValue",row.paperDepart);
			$("#txt_publishDate").datebox("setValue",row.publishDate);
			if (row.paperEi=="是"){
				$("#rdb_paperEiYes").radiobutton("check",0);
			}else{
				$("#rdb_paperEiNo").radiobutton("check",1);
			}
			$("#txt_inOrder").combobox("setValue",row.inOrder);
			$('#paper_save').dialog('open');
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#paper_btn_cancel").click(function(){
		clearForm();
		$('#paper_save').dialog('close');
	});
	 
	function clearForm(){
		$("#txt_paperId").val("");
		$("#txt_paperThesis").textbox("setValue","");
		$("#txt_paperJournal").textbox("setValue","");
		$("#txt_paperSponsor").textbox("setValue","");
		$("#txt_paperDepart").textbox("setValue","");
		$("#txt_publishDate").datebox("setValue","");
		$("#rdb_paperEiYes").radiobutton("check",0);
		$("#txt_inOrder").combobox("setValue",1);
	}
	
	$("#paper_btn_cancelDel").click(function(){
		$('#paper_delect').dialog('close');
	});
	$("#paper_btn_cancelSub").click(function(){
		$('#paper_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#paper_btn_del").click(function(){
		 var row = $('#tbl_paper_detail').datagrid('getSelected');
		 $("#paper_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.paperThesis)
			 $("#paper_delect").dialog('open');
		 }
		 $("#paper_btn_del").linkbutton('enable');
	 });
	 $("#paper_btn_delect").click(function(){
		 $("#paper_btn_delect").linkbutton('disable');
		 var url=path+"/review/delPaper";
		 var row = $('#tbl_paper_detail').datagrid('getSelected');
		 var memo="删除科研论文："+row.paperThesis;
		 var postData={"paperId":row.paperId,"memo":memo}; 
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_paper_detail').datagrid('load');
				 $("#paper_btn_delect").linkbutton('enable');
				 $('#paper_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
			 $("#paper_btn_delect").linkbutton('enable');
		 }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
	 });
	 //提交界面
	 $("#paper_btn_sub").click(function(){
		 var row = $('#tbl_paper_detail').datagrid('getSelected');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 if(row.status=="CApprove"){
				 alert("该信息已提交，请勿重复提交");
			 }else{
				 $("#td_submitMess").text("您确认提交记录："+row.paperThesis+",提交之后记录将不能修改或删除！")
				 $("#paper_submit").dialog('open');
			 }
		 }
	 });
	 $("#paper_btn_submit").click(function(){
		 var row = $('#tbl_paper_detail').datagrid('getSelected');
		 if(row.fileCount==0){
			 alert("附件资料不全，不能提交！");
			 return false;
		 }
		 $("#paper_btn_submit").linkbutton('disable');
		 var url=path+"/review/submitPaper";
		 var memo="提交论文："+row.paperThesis
		 var postData={"paperId":row.paperId,"memo":memo};  
		 $.post(url,postData,function(data){
		    	var mess=eval(data).mess;
		    	if ("succ"==mess){
		    		alert("提交成功！");
		    		$("#paper_btn_submit").linkbutton('enable');
		    		//需要刷新相关记录
					var rowIndex=$('#tbl_paper_detail').datagrid('getRowIndex',row);
					row.status="CApprove";
					row.statusValue=getStatusValByKey(row.status);//更新状态值
					$('#tbl_paper_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setButtonStatus(row);
					$('#paper_submit').dialog('close');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		});
	function initPaper_detail(){
		var url=path+"/review/queryPaper";
		$('#tbl_paper_detail').datagrid({
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
		    queryParams: $('#paper_queryForm').serializeJSON(),
		    columns:[[
				{field:'paperThesis',title:'论文题目',width:150},
				{field:'paperJournal',title:'期刊名称',width:100},
				{field:'screTopic',title:'期刊类型',width:100},
				{field:'paperSponsor',title:'主办单位',width:100},
				{field:'paperDepart',title:'主管部门',width:100},
				{field:'publishDate',title:'发表时间',width:100,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
				{field:'paperEi',title:'EI..检索',width:60},
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
	    		$("#paper_btn_edit").linkbutton('disable');
	    		$("#paper_btn_sub").linkbutton('disable');
	    		$("#paper_btn_del").linkbutton('disable');
	    		$("#paper_btn_file").linkbutton('disable');
	    	}else{
	    		$("#paper_btn_edit").linkbutton('enable');
	    		$("#paper_btn_sub").linkbutton('enable');
	    		$("#paper_btn_del").linkbutton('enable');
	    		$("#paper_btn_file").linkbutton('enable');
	    	}
		}
	}
	//上传文件
	$("#paper_btn_file").click(function(){
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#paper_material").uploadMaterial({
				datagrid_id:'tbl_paper_detail',
    			materialType:'RptPaper',
    			screNameTitle:row.paperThesis,
    			screId:row.paperId
			});
	    }
	 });
	$("#paper_btn_log").click(function(){
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#paper_log").getReviewLog({
				tblName:'RptPaper',
    			screId:row.paperId
			});
		}
	});
	
});