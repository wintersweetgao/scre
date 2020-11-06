/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initScientific_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#scientific_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_scientific_detail').datagrid('load',queryParams);
	});

	//通过
	$("#btn_passScientific").click(function(){
		$("#btn_passScientific").linkbutton('disable');
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passScientificDepart";
		    var postData={
		    		"scieId":row.scieId,
		    		"memo":row.scieName+"审核通过"
		    };
		    $.post(url,postData,function(data){
		    	var mess=data
		    	if ("succ"==mess){
		    		alert(row.scieName+"审核通过");
		    		$('#tbl_scientific_detail').datagrid('load');
		    		$("#btn_passScientific").linkbutton('enable');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			
		    });
		}
		$("#btn_passScientific").linkbutton('enable');
	});
	
	//未通过
	$("#btn_nopassScientific").click(function(){
		$("#btn_nopassScientific").linkbutton('disable');
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.scieName+"'通过的原因")
			$("#dlg_nopassScientific").dialog('open');
		}
		$("#btn_nopassScientific").linkbutton('enable');
	});
	$("#btn_passScientific_submit").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		$("#btn_passScientific_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/noPassScientificDepart";
	    var postData={
	    		"scieId":row.scieId,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$('#tbl_scientific_detail').datagrid('load');
	    		$("#btn_passScientific_submit").linkbutton('enable');
				$('#dlg_nopassScientific').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		
	    });
	});
	$("#btn_nopassScientific_submit").click(function(){
		$('#dlg_nopassScientific').dialog('close');
	});
	
	function initScientific_detail(){
		var url=path+"/review/queryScientificDepart";
		$('#tbl_scientific_detail').datagrid({
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
		    queryParams: $('#scientific_queryForm').serializeJSON(),
		    columns:[[
		    	{field:'staffName',title:'姓名',width:80},
				{field:'staffTitle',title:'职称',width:70},
				{field:'screType',title:'项目类别',width:100},
				{field:'screTopic',title:'项目内容',width:100},
				{field:'scieName',title:'项目名称',width:150},
				{field:'scieDepart',title:'项目审批部门',width:100},
				{field:'scieLeader',title:'负责人',width:80},
				{field:'scieStartDate',title:'立项时间',width:100},
				{field:'scieCloseDate',title:'结束时间',width:100},
				{field:'scieOk',title:'是否结题',width:60},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }},
				{field:'statusValue',title:'状态',width:60}
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
