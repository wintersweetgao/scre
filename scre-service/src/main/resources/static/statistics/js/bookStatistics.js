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
	$("#btn_nopassbook_submit").click(function(){
		$('#dlg_nopassbook').dialog('close');
	});
	
	function initbook_detail(){
		var url=path+"/statistics/queryBookstatistics";
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
				
		    ]],
		    onLoadSuccess:function(data){
		    	$(this).datagrid("selectRow",0);
		    	var row = $(this).datagrid('getSelected');
		    	setBtnStatus(row);
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
		    },
		    onClickRow:function(index, row){
		    	setBtnStatus(row);
		    }
		});
	}
	 /*******************导出excel 开始*****************/
	$('#btn_openexl_book').click(function () {
		var url=path + "/statistics/bookExcel";
		var queryParams=$('#book_queryForm').serializeJSON();
		postExcelFile(queryParams,url);
	});
	 /*******************是否推荐展示 开始*****************/
	function setBtnStatus(row){
		var recommend=row.recommend;
		if (recommend=="1"){
			$("#btn_recommend_book").linkbutton({"text":"取消推荐"});	
		}else{
			$("#btn_recommend_book").linkbutton({"text":"推荐作品"});	
		}
		
	}
	
	//推荐
	$('#btn_recommend_book').click(function(){
		var row = $('#tbl_book_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var recommend=row.recommend;
			var mess="";
			if (recommend=="1"){
				recommend="0";	
				mess="取消推荐成功";
			}else{
				recommend="1";
				mess="推荐作品成功";
			}
			
			var url=path+"/statistics/updBookRend ";
			var postData={
			    		"bookID":row.bookID,
			    		"recommend":recommend
			    };
			$.post(url,postData,function(data){
				if (data=="succ"){
					//需要刷新相关记录
					var rowIndex=$('#tbl_book_detail').datagrid('getRowIndex',row);
					row.recommend=recommend;
					$('#tbl_book_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setBtnStatus(row);
					alert(mess);
				}else{
					alert(mess);
					
				}
				
			});
		}
	});
});
