/**
 *
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initAchievement_detail();
	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#achievement_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_achievement_detail').datagrid('load',queryParams);
	});
	$("#btn_nopassAchievement_submit").click(function(){
		$('#dlg_nopassAchievement').dialog('close');
	});
	function initAchievement_detail(){
		var url=path+"/statistics/queryAchievementStatistics";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
		$('#tbl_achievement_detail').datagrid({
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
			queryParams:$('#achievement_queryForm').serializeJSON(),
			pagination:true,
			pageSize:10,
			pageNumber:1,
			columns:[[
				{field:'staffName',title:'姓名',width:100},
				{field:'staffTitle',title:'职称',width:100},
				{field:'staffParentDepart',title:'系部处',width:80},
				{field:'staffDepart',title:'科室',width:80},
				{field:'achievementName',title:'成果名称',width:150},
				{field:'achievementType',title:'成果类型',width:100},
				{field:'awardName',title:'获奖名称',width:100},
				{field:'screTopic',title:'获奖等级',width:100},
				{field:'awardDepart',title:'颁奖部门',width:100},
				{field:'firstPerson',title:'第一完成人',width:100},
				{field:'publishDate',title:'获奖时间',width:100,formatter:function(val, row, index){
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
	$('#btn_openexl_achievement').click(function () {
		var url=path + "/statistics/achievementExcel";
		var queryParams=$('#achievement_queryForm').serializeJSON();
		postExcelFile(queryParams,url);
	});
	/*******************是否推荐展示 开始*****************/
	function setBtnStatus(row){
		var recommend=row.recommend;
		if (recommend=="1"){
			$("#btn_recommend_achievement").linkbutton({"text":"取消推荐"});	
		}else{
			$("#btn_recommend_achievement").linkbutton({"text":"推荐作品"});	
		}
		
	}
	//推荐
	$('#btn_recommend_achievement').click(function(){
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
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

			var url=path+"/statistics/updAchievementRend ";
			var postData={
				"achievementId":row.achievementId,
				"recommend":recommend
			};
			$.post(url,postData,function(data){
				if (data=="succ"){
					//需要刷新相关记录
					var rowIndex=$('#tbl_achievement_detail').datagrid('getRowIndex',row);
					row.recommend=recommend;
					$('#tbl_achievement_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
					setBtnStatus(row);
					alert(mess);
				}else{
					alert(mess);

				}

			});
		}
	});
});
