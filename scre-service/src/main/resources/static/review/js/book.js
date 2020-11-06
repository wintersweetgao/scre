/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initbook_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#book_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_book_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#book_btn_add").click(function(){
		$("#book_save").dialog("setTitle","新增教材、专著");
		$('#book_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#book_btn_save").click(function(){
		var bookName=$("#txt_bookName").textbox('getValue');//论文题目
		if(bookName==""){
			alert("教材、专著名称为空,请重新输入！");
			$("#txt_bookName").next('span').find('input').focus();
			return false;
		}
		var firstEditor=$("#txt_firstEditor").textbox('getValue');//主编或第一主编
		if(firstEditor==""){
			alert("主编或第一主编为空,请重新输入！");
			$("#txt_firstEditor").next('span').find('input').focus();
			return false;
		} 
		var press=$("#txt_press").textbox('getValue');//出版社
		if(press==""){
			alert("主办单位为空,请重新输入！");
			$("#txt_press").next('span').find('input').focus();
			return false;
		} 
		var publishDate=$("#txt_publishDate").datebox('getValue');//发表日期
		if(publishDate==""){
			alert("发表日期为空,请重新输入！");
			$("#txt_publishDate").next('span').find('input').focus();
			return false;
		} 
		
		if($("#txt_bookID").val()!=""){//修改
			var url=path+"/review/updBook";
			var postData=$("#book_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#book_saveForm").form('reset');
					$('#tbl_book_detail').datagrid('load');
					clearForm();
					$('#book_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/saveBook";
			var postData=$("#book_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#book_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#book_saveForm").form('reset');
					$('#tbl_book_detail').datagrid('load');
					clearForm();
					$('#book_save').dialog('close');
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
	$("#book_btn_edit").click(function(){
		$("#book_save").dialog("setTitle","修改教材专著");
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$('#book_save').dialog('open');
			$("#txt_bookID").val(row.bookID);
			$("#txt_bookName").textbox("setValue",row.bookName);
			$("#txt_firstEditor").textbox("setValue",row.firstEditor);
			$("#txt_press").textbox("setValue",row.press);
			$("#txt_publishDate").datebox("setValue",row.publishDate);
			if (row.bookEi=="是"){
				$("#rdb_bookEiYes").radiobutton("check",0);
			}else{
				$("#rdb_bookEiNo").radiobutton("check",1);
			}
			$("#txt_inOrder").textbox("setValue",row.inOrder);
			$('#book_save').dialog('open');
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#book_btn_cancel").click(function(){
		clearForm();
		$('#book_save').dialog('close');
	});
	 
	function clearForm(){
		$("#txt_bookID").val("");
		$("#txt_bookName").textbox("setValue","");
		$("#txt_firstEditor").textbox("setValue","");
		$("#txt_press").textbox("setValue","");
		$("#txt_publishDate").datebox("setValue","");
		$("#rdb_bookEiYes").radiobutton("check",0);
		$("#txt_inOrder").textbox("setValue",1);
		
	}
	 
	$("#book_btn_cancelDel").click(function(){
		$('#book_delect').dialog('close');
	});
	$("#book_btn_cancelSub").click(function(){
		$('#book_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#book_btn_del").click(function(){
		 var row = $('#tbl_book_detail').datagrid('getSelected');
		 $("#book_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.bookName)
			 $("#book_delect").dialog('open');
		 }
		 $("#book_btn_del").linkbutton('enable');
	 });
	 $("#book_btn_delect").click(function(){
		 $("#book_btn_delect").linkbutton('disable');
		 var url=path+"/review/delBook";
		 var row = $('#tbl_book_detail').datagrid('getSelected');
		 var memo="删除教材、专著："+row.bookName;
		 var postData={"bookID":row.bookID,"memo":memo}; 
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_book_detail').datagrid('load');
				 $("#book_btn_delect").linkbutton('enable');
				 $('#book_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
			 $("#book_btn_delect").linkbutton('enable');
		 }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
	 });
	 //提交界面
	 $("#book_btn_sub").click(function(){
		 var row = $('#tbl_book_detail').datagrid('getSelected');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 if(row.status=="CApprove"){
				 alert("该信息已提交，请勿重复提交");
			 }else{
				 $("#td_submitMess").text("您确认提交记录："+row.bookName+",提交之后记录将不能修改或删除！")
				 $("#book_submit").dialog('open');
			 }
		 }
	 });
	 $("#book_btn_submit").click(function(){
		 var row = $('#tbl_book_detail').datagrid('getSelected');
		 if(row.fileCount==0){
			 alert("附件资料不全，不能提交！");
			 return false;
		 }
		 $("#book_btn_submit").linkbutton('disable');
		 var url=path+"/review/submitBook";
		 var memo="提交教材专著："+row.bookName
		 var postData={"bookID":row.bookID,"memo":memo};  
		 $.post(url,postData,function(data){
		    	var mess=eval(data).mess;
		    	if ("succ"==mess){
		    		alert("提交成功！");
		    		$("#book_btn_submit").linkbutton('enable');
		    		//需要刷新相关记录
					var rowIndex=$('#tbl_book_detail').datagrid('getRowIndex',row);
					row.status="CApprove";
					row.statusValue=getStatusValByKey(row.status);//更新状态值
					$('#tbl_book_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setButtonStatus(row);
					$('#book_submit').dialog('close');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		});
	function initbook_detail(){
		var url=path+"/review/queryBook";
		$('#tbl_book_detail').datagrid({
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
		    queryParams: $('#book_queryForm').serializeJSON(),
		    columns:[[
				{field:'bookName',title:'教材，著作名称',width:150},
				{field:'firstEditor',title:'主编或第一主编',width:100},
				{field:'press',title:'出版社',width:100},
				{field:'publishDate',title:'发表时间',width:100,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
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
	    		$("#book_btn_edit").linkbutton('disable');
	    		$("#book_btn_sub").linkbutton('disable');
	    		$("#book_btn_del").linkbutton('disable');
	    		$("#book_btn_file").linkbutton('disable');
	    	}else{
	    		$("#book_btn_edit").linkbutton('enable');
	    		$("#book_btn_sub").linkbutton('enable');
	    		$("#book_btn_del").linkbutton('enable');
	    		$("#book_btn_file").linkbutton('enable');
	    	}
		}
	}
	//上传文件
	$("#book_btn_file").click(function(){
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#book_material").uploadMaterial({
				datagrid_id:'tbl_book_detail',
    			materialType:'RptBook',
    			screNameTitle:row.bookName,
    			screId:row.bookID
			});
	    }
	 });
	$("#book_btn_log").click(function(){
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#book_log").getReviewLog({
				tblName:'RptBook',
    			screId:row.bookID
			});
		}
	});
	
});