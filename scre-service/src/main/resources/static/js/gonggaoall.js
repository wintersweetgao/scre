/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	initnotice();
	var showSelectedSurveryDataOnMap=function(row){

		if(row.noticeId){
			window.open(path+"/idxNotice?noticeId="+row.noticeId);
       
		}

	}
	function initnotice(){
		var url=path+"/getNoticeshou";
		$('#tbl_notice_detail').datagrid({
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
		    queryParams: $('#notice_queryForm').serializeJSON(),
		    columns:[[
				{field:'noticeTitle',title:'标题名称',width:300},
				{field:'createTime',align: 'right',title:'发表时间',width:50,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
		    ]],
		    onClickRow :function(rowIndex,rowData){

 				 showSelectedSurveryDataOnMap(rowData);

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