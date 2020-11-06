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

	//通过
	$("#btn_passAcademic").click(function(){
		$("#btn_passAcademic").linkbutton('disable');
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passAcademicDepart";
		    var postData={
		    		"academicId":row.academicId,
		    		"memo":row.academicTopic+"审核通过"
		    };
		    $.post(url,postData,function(data){
		    	var mess=data
		    	if ("succ"==mess){
		    		alert(row.academicTopic+"审核通过");
		    		$('#tbl_academic_detail').datagrid('load');
		    		$("#btn_passAcademic").linkbutton('enable');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}
		$("#btn_passAcademic").linkbutton('enable');
	});
	
	//未通过
	$("#btn_nopassAcademic").click(function(){
		$("#btn_nopassAcademic").linkbutton('disable');
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.academicTopic+"'通过的原因")
			$("#dlg_nopassAcademic").dialog('open');
		}
		$("#btn_nopassAcademic").linkbutton('enable');
	});
	$("#btn_passAcademic_submit").click(function(){
		var row = $('#tbl_academic_detail').datagrid('getSelected');
		$("#btn_passAcademic_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/refuseAcademicDepart";
	    var postData={
	    		"academicId":row.academicId,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$('#tbl_academic_detail').datagrid('load');
	    		$("#btn_passAcademic_submit").linkbutton('enable');
				$('#dlg_nopassAcademic').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		});
	});
	$("#btn_nopassAcademic_submit").click(function(){
		$('#dlg_nopassAcademic').dialog('close');
	});
	
	function initAcademic_detail(){
		var url=path+"/review/queryAcademicDepart";
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
		    	{field:'staffName',title:'姓名',width:80},
		    	{field:'staffTitle',title:'职称',width:70},
		    	{field:'academicTopic',title:'报告主题',width:150},
		    	{field:'academicDate',title:'主讲日期',width:100},
				{field:'screTopic',title:'学术类型',width:100},
				{field:'academicMemo',title:'报告简介',width:100},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }},
				{field:'statusValue',title:'状态',width:80}
				
		    ]],
		    onLoadSuccess:function(data){
		    	$(this).datagrid("selectRow",0);
		    },
		    onBeforeLoad:function(param){
		    	var pageNo = param.page; 
				delete param.page; 
				param.pageNo = pageNo; 
				var maxResults = param.rows;
				delete param.rows; 
				param.maxResults = maxResults; 
		    },
		    onClickCell:function(index, field, value){
		    	if (field=="fileCount"){
		    		$(this).datagrid('selectRow',index);
		    		var row = $(this).datagrid('getSelected');
		    		if(value==0){
		    			alert("没有附件！");
		    			return false;
		    		}else{
		    			$("#scientfit_viewfile").previewImg({
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
});
