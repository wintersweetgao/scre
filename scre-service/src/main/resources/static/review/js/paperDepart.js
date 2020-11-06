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

	//通过
	$("#btn_passPaper").click(function(){
		$("#btn_passPaper").linkbutton('disable');
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passPaperDepart";
		    var postData={
		    		"paperId":row.paperId,
		    		"memo":row.paperThesis+"审核通过"
		    };
		    $.post(url,postData,function(data){
		    	var mess=data
		    	if ("succ"==mess){
		    		alert(row.paperThesis+"审核通过");
		    		$('#tbl_paper_detail').datagrid('load');
		    		$("#btn_passPaper").linkbutton('enable');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}
		$("#btn_passPaper").linkbutton('enable');
	});
	
	//未通过
	$("#btn_nopassPaper").click(function(){
		$("#btn_nopassPaper").linkbutton('disable');
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.paperThesis+"'通过的原因")
			$("#dlg_nopassPaper").dialog('open');
		}
		$("#btn_nopassPaper").linkbutton('enable');
	});
	$("#btn_passPaper_submit").click(function(){
		var row = $('#tbl_paper_detail').datagrid('getSelected');
		$("#btn_passPaper_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/refusePaperDepart";
	    var postData={
	    		"paperId":row.paperId,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$('#tbl_paper_detail').datagrid('load');
	    		$("#btn_passPaper_submit").linkbutton('enable');
				$('#dlg_nopassPaper').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		});
	});
	$("#btn_nopassPaper_submit").click(function(){
		$('#dlg_nopassPaper').dialog('close');
	});
	
	function initPaper_detail(){
		var url=path+"/review/queryPaperDepart";
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
		    	{field:'staffName',title:'姓名',width:80},
		    	{field:'staffTitle',title:'职称',width:70},
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
