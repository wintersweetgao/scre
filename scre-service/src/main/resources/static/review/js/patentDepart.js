/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initPatent_detail();
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

	//通过
	$("#btn_passPatent").click(function(){
		$("#btn_passPatent").linkbutton('disable');
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passPatentDepart";
		    var postData={
		    		"patentId":row.patentId,
		    		"memo":row.patentName+"审核通过"
		    };
		    $.post(url,postData,function(data){
		    	var mess=data
		    	if ("succ"==mess){
		    		alert(row.patentName+"审核通过");
		    		$('#tbl_patent_detail').datagrid('load');
		    		$("#btn_passPatent").linkbutton('enable');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}
		$("#btn_passPatent").linkbutton('enable');
	});
	
	//未通过
	$("#btn_nopassPatent").click(function(){
		$("#btn_nopassPatent").linkbutton('disable');
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.patentName+"'通过的原因")
			$("#dlg_nopassPatent").dialog('open');
		}
		$("#btn_nopassPatent").linkbutton('enable');
	});
	$("#btn_passPatent_submit").click(function(){
		var row = $('#tbl_patent_detail').datagrid('getSelected');
		$("#btn_passPatent_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/refusePatentDepart";
	    var postData={
	    		"patentId":row.patentId,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$('#tbl_patent_detail').datagrid('load');
	    		$("#btn_passPatent_submit").linkbutton('enable');
				$('#dlg_nopassPatent').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		});
	});
	$("#btn_nopassPatent_submit").click(function(){
		$('#dlg_nopassPatent').dialog('close');
	});
	
	function initPatent_detail(){
		var url=path+"/review/queryPatentDepart";
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
		    	{field:'staffName',title:'姓名',width:80},
		    	{field:'staffTitle',title:'职称',width:70},
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
