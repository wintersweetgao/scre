/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initScientifit_detail();
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
			var url=path+"/review/passScientifitCollege";
		    var postData={
		    		"memo":row.scieName+"审核通过",
		    		"scieId":row.scieId
		    };
		    $.post(url,postData,function(data){
		    	var mess=data;
		    	if ("succ"==mess){
		    		alert(row.scieName+"审核通过");
		    		$("#btn_passScientific").linkbutton('enable');
		    		$('#tbl_scientific_detail').datagrid('load');
		    	}else{
		    		alert(mess);
		    	}
		    });
		}
		
	});
	
	//拒绝
	$("#btn_noPassScientific").click(function(){
		$("#btn_noPassScientific").linkbutton('disable');
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{		
			$("#dlg_nopassScientific").dialog('open');
		}
		$("#btn_noPassScientific").linkbutton('enable');
	});
	$("#btn_confirmNoPass").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		$("#btn_confirmNoPass").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
	    var url=path+"/review/noPassScientifitCollege";
	    var postData={
	    		"scieId":row.scieId,
	    		"memo":reasons
	    };
	    $.post(url,postData,function(data){
	    	var mess=data;
	    	if ("succ"==mess){
	    		alert("审核完成！");
	    		$("#btn_confirmNoPass").linkbutton('enable');
	    		$('#tbl_scientific_detail').datagrid('load');
				$('#dlg_nopassScientific').dialog('close');
	    	}else{
	    		alert(mess);
	    	}
	    });
	});
	$("#btn_cancelNoPass").click(function(){
		$('#dlg_nopassScientific').dialog('close');
	});
		
	function initScientifit_detail(){
		var url=path+"/statistics/queryStatisticsScientific";
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
				{field:'staffTitle',title:'职称',width:80},
				{field:'staffParentDepart',title:'系部处',width:100},
				{field:'staffDepart',title:'科室',width:100},
				{field:'screType',title:'项目类别',width:100},
				{field:'scieName',title:'项目名称',width:120},
				{field:'scieDepart',title:'项目审批部门',width:100},
				{field:'scieLeader',title:'负责人',width:80},
				{field:'scieStartDate',title:'立项时间',width:100},
				{field:'scieCloseDate',title:'结束时间',width:100},
				{field:'scieOk',title:'是否结题',width:60},
				{field:'fileCount',title:'附件数',width:60,styler: function(value,row,index){
                    return 'color:blue;';
                }},
     
		    ]],
		    onBeforeLoad:function(param){  //这个param就是queryString 
		    	var pageNo = param.page; //保存下值
				delete param.page; //删掉
				param.pageNo = pageNo; //这里就是重新命名了！！！ ,j将page 改为了 pages
				var maxResults = param.rows;
				delete param.rows; //删掉
				param.maxResults = maxResults; //这里就是重新命名了！！！ ,j将page 改为了 pages
		    },
    onLoadSuccess:function(data){
    	$(this).datagrid("selectRow",0);
    	var row = $(this).datagrid('getSelected');
    	setBtnStatus(row);
    },
    
    onClickCell:function(index, field, value){
    	if (field=="fileCount"){
    		$(this).datagrid('selectRow',index);
    		var row = $(this).datagrid('getSelected');
    		if(value==0){
    			alert("没有附件！");
    			return false;
    		}else{
    			$("#scientific_viewfile").previewImg({
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
	 $('#btn_openexl_scientific').click(function () {
			var url=path + "/statistics/scientificExcel";
			var queryParams=$('#scientific_queryForm').serializeJSON();
			postExcelFile(queryParams,url);
		});
	 /*******************是否推荐展示 开始*****************/
	 function setBtnStatus(row){
			var recommend=row.recommend;
			if (recommend=="1"){
				$("#btn_recommend_scientific").linkbutton({"text":"取消推荐"});	
			}else{
				$("#btn_recommend_scientific").linkbutton({"text":"推荐作品"});	
			}
			
		}
		$('#btn_recommend_scientific').click(function(){
			var row = $('#tbl_scientific_detail').datagrid('getSelected');
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
				var url=path+"/statistics/updscientificRend ";
				  var postData={
				    		"scieId":row.scieId,
				    		"recommend":recommend
				    };
			$.post(url,postData,function(data){
				if (data=="succ"){
					//需要刷新相关记录
					var rowIndex=$('#tbl_scientific_detail').datagrid('getRowIndex',row);
					row.recommend=recommend;
					$('#tbl_scientific_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setBtnStatus(row);
					alert(mess);
				}else{
					alert(mess);
					
				}
				
			});
			}
		});
	});