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

	//通过
	$("#btn_passAchievement").click(function(){
		$("#btn_passAchievement").linkbutton('disable');
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			var url=path+"/review/passAchievementCollege";
			var postData={
				"achievementId":row.achievementId,
				"memo":row.achievementName+"审核通过"
			};
			$.post(url,postData,function(data){
				var mess=data
				if ("succ"==mess){
					alert(row.achievementName+"审核通过");
					$('#tbl_achievement_detail').datagrid('load');
					$("#btn_passAchievement").linkbutton('enable');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}
		$("#btn_passAchievement").linkbutton('enable');
	});

	//未通过
	$("#btn_nopassAchievement").click(function(){
		$("#btn_nopassAchievement").linkbutton('disable');
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#td_refuseMess").text("请输入拒绝'"+row.achievementName+"'通过的原因")
			$("#dlg_nopassAchievement").dialog('open');
		}
		$("#btn_nopassAchievement").linkbutton('enable');
	});
	$("#btn_passAchievement_submit").click(function(){
		var row = $('#tbl_achievement_detail').datagrid('getSelected');
		$("#btn_passAchievement_submit").linkbutton('disable');
		var reasons=$("#noPass_reason").textbox('getValue');
		var url=path+"/review/refuseAchievementCollege";
		var postData={
			"achievementId":row.achievementId,
			"memo":reasons
		};
		$.post(url,postData,function(data){
			var mess=data;
			if ("succ"==mess){
				alert("审核完成！");
				$('#tbl_achievement_detail').datagrid('load');
				$("#btn_passAchievement_submit").linkbutton('enable');
				$('#dlg_nopassAchievement').dialog('close');
			}else{
				alert(mess);
			}
		}).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
		});
	});
	$("#btn_nopassAchievement_submit").click(function(){
		$('#dlg_nopassAchievement').dialog('close');
	});

	function initAchievement_detail(){
		var url=path+"/review/queryAchievementCollege";
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
			pagination:true,
			pageSize:10,
			queryParams: $('#achievement_queryForm').serializeJSON(),
			columns:[[
				{field:'staffName',title:'姓名',width:80},
				{field:'staffTitle',title:'职称',width:70},
				{field:'staffParentDepart',title:'系部处',width:100},
				{field:'staffDepart',title:'科室',width:100},
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
