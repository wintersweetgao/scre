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

	//通过
	$("#btn_passbook").click(function(){
		$("#btn_passbook").linkbutton('disable');
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passBookCollege";
		    var postData={
		    		"bookID":row.bookID,
		    		"memo":row.bookName+"审核通过"
		    };
		    $.post(url,postData,function(data){
		    	var mess=data
		    	if ("succ"==mess){
		    		alert(row.bookName+"审核通过");
		    		$('#tbl_book_detail').datagrid('load');
		    		$("#btn_passbook").linkbutton('enable');
		    	}else{
		    		alert(mess);
		    	}
		    }).error(function(error){
		        var mess=eval(error);
		        alert(mess.responseText);
		    });
		}
		$("#btn_passbook").linkbutton('enable');
	});
	
	//未通过
	$("#btn_nopassbook").click(function(){
		$("#btn_nopassbook").linkbutton('disable');
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.bookName+"'通过的原因")
			$("#dlg_nopassbook").dialog('open');
		}
		$("#btn_nopassbook").linkbutton('enable');
	});
	$("#btn_passbook_submit").click(function(){
		var row = $('#tbl_book_detail').datagrid('getSelected');
		$("#btn_passbook_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/refuseBookCollege";
	    var postData={
	    		"bookID":row.bookID,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$('#tbl_book_detail').datagrid('load');
	    		$("#btn_passbook_submit").linkbutton('enable');
				$('#dlg_nopassbook').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		});
	});
	$("#btn_nopassbook_submit").click(function(){
		$('#dlg_nopassbook').dialog('close');
	});
	
	function initbook_detail(){
		var url=path+"/review/queryBookCollege";
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
		    	{field:'staffName',title:'姓名',width:80},
		    	{field:'staffTitle',title:'职称',width:70},
		    	{field:'staffParentDepart',title:'系部处',width:100},
		    	{field:'staffDepart',title:'科室',width:100},
		    	{field:'bookName',title:'教材专著题目',width:150},
				{field:'firstEditor',title:'主编',width:100},
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
