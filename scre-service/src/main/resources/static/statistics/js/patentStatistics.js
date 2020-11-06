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
		var queryParams=$('#patent_queryForm_statistics').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_patent_detail_statistics').datagrid('load',queryParams);
	});

	
	
	
	function initPatent_detail(){
		var url=path+"/statistics/queryPatentStatistics";
		$('#tbl_patent_detail_statistics').datagrid({
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
		    queryParams: $('#patent_queryForm_statistics').serializeJSON(),
		    columns:[[
		    	{field:'staffName',title:'姓名',width:80},
		    	{field:'staffTitle',title:'职称',width:70},
		    	{field:'staffParentDepart',title:'系部处',width:100},
				{field:'staffDepart',title:'科室',width:100},
		    	{field:'patentName',title:'专利名称',width:150},
				{field:'patentNumber',title:'专利证号',width:100},
				{field:'screTopic',title:'专利类型',width:100}, 
				{field:'patentCompany',title:'专利鉴定单位',width:100},
				{field:'patentPerson',title:'完成人',width:100},
				{field:'screType',title:'科研达标类型',width:100},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }}
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
	$('#btn_openexl_patent').click(function () {
		var url=path + "/statistics/patentExcel";
		var queryParams=$('#patent_queryForm_statistics').serializeJSON();
		postExcelFile(queryParams,url);
	});
	
	 /*******************是否推荐展示 开始*****************/
	function setBtnStatus(row){
		var recommend=row.recommend;
		if (recommend=="1"){
			$("#btn_recommend_patent").linkbutton({"text":"取消推荐"});	
		}else{
			$("#btn_recommend_patent").linkbutton({"text":"推荐作品"});	
		}
		
	}
	//推荐
	$('#btn_recommend_patent').click(function(){
		var row = $('#tbl_patent_detail_statistics').datagrid('getSelected');
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
			var url=path+"/statistics/updPatentRend ";
			  var postData={
			    		"patentId":row.patentId,
			    		"recommend":recommend
			    };
		$.post(url,postData,function(data){
			if (data=="succ"){
				//需要刷新相关记录
				var rowIndex=$('#tbl_patent_detail_statistics').datagrid('getRowIndex',row);
				row.recommend=recommend;
				$('#tbl_patent_detail_statistics').datagrid('refreshRow', rowIndex); //然后刷新该行即可
				setBtnStatus(row);
				alert(mess);
			}else{
				alert(mess);
				
			}
			
		});
		}
	});
});
