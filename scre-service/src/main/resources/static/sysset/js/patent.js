/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initPatent_detail();
	$("#patent_btn_del").linkbutton('disable');
	$("#patent_btn_edit").linkbutton('disable');
	$("#patent_btn_sub").linkbutton('disable');
	$("#patent_btn_file").linkbutton('disable');
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
		$('#patent_save').dialog('open');
	});
	/**
	 * 增加
	 */
	//专利证号数字限制
	$("#add_patentNumber").textbox('textbox').bind('keyup', function(e){
		  $("#add_patentNumber").textbox('setValue', $(this).val().replace(/\D/g,''));
	});
	// 添加的类型下拉列表
	$("#add_patentType").combobox({
		valueField: 'label',
		textField: 'value',
		data: [{label: '发明专利',value: '发明专利'},
				{label: '外观设计专利',value: '外观设计专利'},
				{label: '实用新型专利',value: '实用新型专利'}]
	});
	
	$("#patent_btn_save").click(function(){
		
		var patentName=$("#add_patentName").textbox('getValue');
		var patentNumber=$("#add_patentNumber").textbox('getValue');
		var patentType=$("#add_patentType").combobox('getValue');
		var patentCompany=$("#add_patentCompany").textbox('getValue');
		var patentPerson=$("#add_patentPerson").textbox('getValue');
		var npatentName= patentName.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentNumber= patentNumber.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentCompany= patentCompany.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentPerson= patentPerson.replace(/^\s+/,'').replace(/\s+$/,'');
		if(npatentName.length<=0){
			 alert("专利名称不能为空，请重新输入!");
			 return false;
		 }else if(npatentNumber.length<=0){
			 alert("专利证号不能为空，请重新输入!");
			 return false;
		 }else if(patentType == ""){
			 alert("专利类型不能为空，请重新输入!");
			 return false;
		 }else if(npatentCompany.length<=0){
			 alert("专利鉴定单位不能为空，请重新输入!");
			 return false;
		 }else if(npatentPerson.length<=0){
			 alert("完成人不能为空，请重新输入!");
			 return false;
		 }else if(npatentName.length > 200){
			 alert("专利名称输入过长，请重新输入!");
			 return false;
		 }else if(npatentNumber.length > 50){
			 alert("专利证号输入过长，请重新输入!");
			 return false;
		 }else if(npatentCompany.length > 200){
			 alert("专利鉴定单位输入过长，请重新输入!");
			 return false;
		 }else if(npatentPerson.length > 200){
			 alert("完成人名称输入过长，请重新输入!");
			 return false;
		 }else{
			 $("#patent_btn_save").linkbutton('disable');
			 var url=path+"/review/savePatent";
			 var postData=$("#patent_saveForm").serializeJSON();
			 $.post(url,postData,function(data){
				 $("#patent_btn_save").linkbutton('enable');
				 var mess=eval(data).mess;
				 var total=eval(data).total;
				 if(total==-1){
						alert("专利名称已存在！");
				 }else if(total==-2){
						alert("专利证号已存在！");
				 }else if ("succ"==mess){
					 alert("保存成功！");
					 $("#patent_saveForm").form('reset');
					 $('#tbl_patent_detail').datagrid('load');
					 $('#patent_save').dialog('close');
				 }else{
					 alert(mess);
				 }
			 });
		 }
	});
	
	/**
	 * 修改
	 */
	//专利证号数字限制
	$("#add_patentNumber").textbox('textbox').bind('keyup', function(e){
		$("#add_patentNumber").textbox('setValue', $(this).val().replace(/\D/g,''));
	});
	// 添加的类型下拉列表
		$("#upd_patentType").combobox({
			valueField: 'label',
			textField: 'value',
			data: [{label: '发明专利',value: '发明专利'},
					{label: '外观设计专利',value: '外观设计专利'},
					{label: '实用新型专利',value: '实用新型专利'}]
	});
	 $("#patent_btn_edit").click(function(){
		    var row = $('#tbl_patent_detail').datagrid('getSelected');
		    $("#patent_btn_edit").linkbutton('disable');  //如果点击了修改，修改应变为不可点
		    if (row==null){
	    		alert("尚未选中任何记录！");
	    		return false;
	    	}else if(row.status=="已提交"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（系）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（院）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else{
	    		$("#upd_patentId").val(row.patentId);
	    		$("#upd_patentName").textbox("setValue",row.patentName);
	    		$("#upd_patentNumber").textbox("setValue",row.patentNumber);
	    		$("#upd_patentType").textbox("setValue",row.patentType);
				$("#upd_patentCompany").textbox("setValue",row.patentCompany);
				$("#upd_patentPerson").textbox("setValue",row.patentPerson);
				$("#patent_btn_edit").linkbutton('enable');  //设置修改按钮重新可点
				$("#patent_update").dialog('open');
	    	}
	    	$("#patent_btn_edit").linkbutton('enable');
	 });
	 
	 $("#patent_btn_update").click(function(){
		var patentName=$("#upd_patentName").textbox('getValue');
		var patentNumber=$("#upd_patentNumber").textbox('getValue');
		var patentType=$("#upd_patentType").combobox('getValue');
		var patentCompany=$("#upd_patentCompany").textbox('getValue');
		var patentPerson=$("#upd_patentPerson").textbox('getValue');
		var npatentName= patentName.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentNumber= patentNumber.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentCompany= patentCompany.replace(/^\s+/,'').replace(/\s+$/,'');
		var npatentPerson= patentPerson.replace(/^\s+/,'').replace(/\s+$/,'');
		if(npatentName.length<=0){
			alert("专利名称不能为空，请重新输入!");
			return false;
		}else if(npatentNumber.length<=0){
			alert("专利证号不能为空，请重新输入!");
			return false;
		}else if(patentType == ""){
			alert("专利类型不能为空，请重新输入!");
			return false;
		}else if(npatentCompany.length<=0){
			alert("专利鉴定单位不能为空，请重新输入!");
			return false;
		}else if(npatentPerson.length<=0){
			alert("完成人不能为空，请重新输入!");
			return false;
		}else if(npatentName.length > 200){
			alert("专利名称输入过长，请重新输入!");
			return false;
		}else if(npatentNumber.length > 50){
			alert("专利证号输入过长，请重新输入!");
			return false;
		}else if(npatentCompany.length > 200){
			alert("专利鉴定单位输入过长，请重新输入!");
			return false;
		}else if(npatentPerson.length > 200){
			alert("完成人名称输入过长，请重新输入!");
			return false;
		}else{
			 $("#patent_btn_save").linkbutton('disable');
			 var url=path+"/review/updatePatent";
			 var postData=$("#patent_updateForm").serializeJSON();
			 $.post(url,postData,function(data){
				 var mess=eval(data).mess;
				 var total=eval(data).total;
				 if(total==-1){
						alert("专利名称已存在！");
				 }else if(total==-2){
						alert("专利证号已存在！");
				 }else if ("succ"==mess){
					 alert("修改成功！");
					 $("#patent_updateForm").form('reset');
					 $('#tbl_patent_detail').datagrid('load');
					 $("#patent_btn_update").linkbutton('enable');
					 $('#patent_update').dialog('close');
				 }else{
					 alert(mess);
				 }
			 });
		}
	 });
	 /**
	  * 提交
	  */
	 $("#patent_btn_sub").click(function(){
		    var row = $('#tbl_patent_detail').datagrid('getSelected');
		    $("#patent_btn_sub").linkbutton('disable');  //如果点击了修改，修改应变为不可点
		    if (row==null){
	    		alert("尚未选中任何记录！");
	    		return false;
	    	}else if(row.status=="已提交" ){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（系）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（院）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else{
				$("#patent_btn_sub").linkbutton('enable');  //设置修改按钮重新可点
				$("#patent_sub").dialog('open');
	    	}
	    	$("#patent_btn_sub").linkbutton('enable');
	 });
	 
	 $("#patent_btn_subuss").click(function(){
			var row = $('#tbl_patent_detail').datagrid('getSelected');
			$("#patent_btn_sub").linkbutton('disable');
			$("#upd_patentId").val(row.patentId);
    		$("#upd_patentName").textbox("setValue",row.patentName);
    		$("#upd_patentNumber").textbox("setValue",row.patentNumber);
    		$("#upd_patentType").textbox("setValue",row.patentType);
			$("#upd_patentCompany").textbox("setValue",row.patentCompany);
			$("#upd_patentPerson").textbox("setValue",row.patentPerson);
			var url=path+"/review/subPatent";
			var postData=$("#patent_updateForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=eval(data).mess;
				var total=eval(data).total;
				if ("succ"==mess){
					alert("提交成功！");
					$("#patent_subForm").form('reset');
					$('#tbl_patent_detail').datagrid('load');
					$("#patent_btn_sub").linkbutton('enable');
					$('#patent_sub').dialog('close');
				}else{
					alert(mess);
				}
			});
	});
	 
	 /**
	  * 点击取消关闭页面
	  */
	$("#patent_btn_cancel").click(function(){
		$('#patent_save').dialog('close');
	});
	$("#patent_btn_cancelUpd").click(function(){
		$('#patent_update').dialog('close');
	});
	$("#patent_btn_cancelDel").click(function(){
		$('#patent_delect').dialog('close');
	});
	$("#patent_btn_cancelSub").click(function(){
		$('#patent_sub').dialog('close');
	});
	/**
	 * 删除
	 */
	$("#patent_btn_del").click(function(){
		    var row = $('#tbl_patent_detail').datagrid('getSelected');
		    $("#patent_btn_del").linkbutton('disable');  //如果点击了修改，修改应变为不可点
		    var url=path+"/review/delPatent";  
		    var postData=$("#patent_delectForm").serializeJSON();
		    $.post(url,postData,function(data){
		    	if (row==null){
		    		alert("尚未选中任何记录！");
		    	}else if(row.status=="已提交"){
		    		alert("已提交，无法修改！");
		    		return false;
		    	}else if(row.status=="通过（系）"){
		    		alert("已提交，无法修改！");
		    		return false;
		    	}else if(row.status=="通过（院）"){
		    		alert("已提交，无法修改！");
		    		return false;
		    	}else{
		    		$("#del_patentId").val(row.patentId);
					$("#patent_btn_del").linkbutton('enable');  //设置修改按钮重新可点
					$("#patent_delect").dialog('open');
		    	}
		    	$("#patent_btn_del").linkbutton('enable');
		   });
	});
	$("#patent_btn_delect").click(function(){
		 var url=path+"/review/delPatent";
			var postData=$("#patent_delectForm").serializeJSON();
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
		});
	});
	
	function initPatent_detail(){
		var url=path+"/review/queryPatent";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
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
				{field:'patentName',title:'专利名称',width:100},
				{field:'patentNumber',title:'专利证号',width:100},
				{field:'patentType',title:'专利类型',width:100},
				{field:'patentCompany',title:'专利鉴定单位',width:100},
				{field:'patentPerson',title:'完成人',width:100},
				{field:'fileCount',title:'附件数',width:100,styler: function(value,row,index){
						return 'color:blue;';
					}},
				{field:'status',title:'状态',width:100}
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
		    },
		    onClickRow: function(index,row){
		    	if (row.status!="未提交"){
		    		$("#patent_btn_del").linkbutton('disable');
		    		$("#patent_btn_edit").linkbutton('disable');
		    		$("#patent_btn_sub").linkbutton('disable');
		    		$("#patent_btn_file").linkbutton('disable');
		    	}else{
		    		$("#patent_btn_del").linkbutton('enable');
		    		$("#patent_btn_edit").linkbutton('enable');
		    		$("#patent_btn_sub").linkbutton('enable');
		    		$("#patent_btn_file").linkbutton('enable');
		    	}

		    },
		    onClickCell:function(index, field, value){
		    	if (field=="fileCount"){
		    		var row = $(this).datagrid('getSelected');
		    		if(value==0){
		    			alert("没有附件！");
		    			return false;
		    		}else{
		    			$("#patent_viewfile").previewImg({
		    				width:600,
		            		height:510,
		            		title:row.patentName,
		            		data:row.materials
		    			});
		    		}
		    	}
		    }
		});
	}
	
	
	
	$("#patent_btn_cancelview").click(function(){
		$("#patent_viewfile").dialog('close');
	});
	
	 $("#patent_btn_file").click(function(){
		 var row = $('#tbl_patent_detail').datagrid('getSelected');
		 if (row==null){
	    		alert("尚未选中任何记录！");
	    		return false;
	    	}else if(row.status=="已提交"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（系）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else if(row.status=="通过（院）"){
	    		alert("已提交，无法修改！");
	    		return false;
	    	}else{
	    		//初始化
	    		$("#patent_fileForm .easyui-filebox").each(function(index){
    				$(this).textbox('setValue','');
    			});
    			$("#patent_fileForm .easyui-textbox").each(function(index){
    				if (index==0){
    					$(this).textbox('setValue','封面');
    				}else if (index==1){
    					$(this).textbox('setValue','目录');
    				}else if (index==2){
    					$(this).textbox('setValue','正文');
    				}else{
    					$(this).textbox('setValue','');
    				}
    			});
	    		$("#sp_filetitle").text(row.patentName);
	    		$("#txt_screNameTitle").val(row.patentName);
	    		$("#txt_screId").val(row.patentId);
	   		 	$("#patent_material").dialog('open');
	    	}
		 
	 });
	 $("#patent_btn_macancel").click(function(){
		 $("#patent_material").dialog('close');
	 });
	
	 $("#patent_btn_masave").click(function(){
		 var isContinue=true;
		 $("#patent_fileForm input[name='materialTitle']").each(function(index){
			var fileid="#file_material"+index;
			if ($(this).val()!=""){
				var fileName=$(fileid).textbox('getValue');
				if (fileName==""){
					alert("请选择'"+$(this).val()+"'对应的jpg文件!");
					isContinue=false;
					return false;
				}else{
					var fileExtension = fileName.split('.').pop();
					if (fileExtension!="jpg"){
						alert("请选择'"+$(this).val()+"'对应的jpg文件!");
						isContinue=false;
						return false;
					}
				}
				var curTileVal=$(this).val();
				//判断文件标题是否重复
				$("#patent_fileForm input[name='materialTitle']").each(function(item){
					if (($(this).val()==curTileVal)&&(item>index)){
						alert("重复的文件标题:"+curTileVal);
						isContinue=false;
						return false;
					}
				});
			}else{
				if ($(fileid).textbox('getValue')!=""){
					alert("请填写第"+(index+1)+"文件标题");
					isContinue=false;
					return false;
				}
			}
			
			if(!isContinue){//不继续
				return false;
			}
		 });
		 if(isContinue){
			 var url = path+"/review/materialFileUp";
			 var formData = new FormData($("#patent_fileForm")[0]);
			 $.ajax({
				 type: "post",
				 url: url,
				 data : formData,
				 cache: false,			 //不需要缓存
				 processData : false,  	//必须false才会避开jQuery对 formdata 的默认处理
				 contentType : false, 	 //必须false才会自动加上正确的Content-Type
				 success:function(data){
					 var mess=eval(data).mess;
					 if (mess=="succ"){
						 alert("上传成功")
						 //需要刷新相关记录
						 var row = $('#tbl_patent_detail').datagrid('getSelected');
						 var rowIndex=$('#tbl_patent_detail').datagrid('getRowIndex',row);
						 row.materials=(data).materials;
						 row.fileCount = (data).materials.length; //首先要找到该行，然后为目标字段赋值
						 $('#tbl_patent_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
						 $("#patent_material").dialog('close');
					 }else{
						 alert(mess);
					 }
				 },
				 error:function(){
					 alert("请求出错！");
				 }
			 });
		 }
	 });
});




