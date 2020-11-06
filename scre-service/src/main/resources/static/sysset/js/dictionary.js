/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initDictionary_detail();
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_dictionary_detail').datagrid('load',queryParams);
	});
	
	function initDictionary_detail(){
		var url=path+"/sysset/queryDictionary";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
		$('#tbl_dictionary_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:true,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
			sortName:"dictType",
			remoteSort:false,
		    url:url,
		    queryParams: $('#queryForm').serializeJSON(),
		    columns:[[
				{field:'dictKey',title:'Key',width:100},
				{field:'dictValue',title:'Value',width:100},
				{field:'dictValue1',title:'Value1',width:100},
				{field:'dictType',title:'字典分类',width:100}
		    ]],
		    onLoadSuccess:function(data){
		    	//alert(data);
		    }
		});
	}
	
});
