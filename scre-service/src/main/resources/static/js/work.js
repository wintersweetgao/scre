/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	initnotice();
     var  showSelectcxx=function(row){
		
		if(row.rptId){
			//window.open(path+"/showRptPageDetail?rptId="+row.rptId+"&className="+row.className);
			window.open(path+"/getexcellence?rptId="+row.rptId+"&className="+row.className);
	
		}
	}
	function initnotice(){
		var url=path+"/queryViewExhall?staffParentDepart=财经信息工程系";
		$('#tbl_cxx_detail').datagrid({
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
		    striped:false,
		    columns:[[
				{field:'rptName',title:'标题名称',width:300},
				{field:'createTime',align: 'right',title:'发表时间',width:50,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
		    ]],
		    onClickRow :function(rowIndex,rowData){

		    	showSelectcxx(rowData);

 				},
 		    onBeforeLoad:function(param){
 				    	var pageNo = param.page; 
 						delete param.page; 
 						param.pageNo = pageNo; 
 						var maxResults = param.rows;
 						delete param.rows; 
 						param.maxResults = maxResults; 
 				    },
		});
	}
	
});