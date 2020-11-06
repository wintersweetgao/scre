/**
 * 
 */
/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initStaffTopic();
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_staffMarkStandard_detail').datagrid('load',queryParams);
	});
	//新增
	$("#staffTopicStand_btn_add").click(function(){
		$("#staffTopicStand_edit").dialog("setTitle","参与达标教师的科研达标积分标准 ");
		$("#staffTopicStand_edit").dialog("open");
	});

//	 * 修改页面
	$("#staffTopicStand_btn_edit").click(function(){
		$("#staffTopicStand_edit").dialog("setTitle","修改积分标准");	
		var row = $('#tbl_staffMarkStandard_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			$("#txt_standardId").val(row.standardId);
			$("#txt_markStandard").textbox("setValue",row.markStandard);
			$("#txt_belongCycle").textbox("setValue",row.belongCycle);
			$("#txt_staffTitle").textbox("setValue",row.staffTitle);	
			$('#staffTopicStand_edit').dialog('open');
	    }
	 });
//	 确定 增加或修改
	$("#staffTopicStand_btnsave").click(function(){
		var markStandard=$("#txt_markStandard").textbox('getValue');//标准分值
		if(markStandard==""){
			$("#txt_markStandard").next('span').find('input').focus();
			return false;
		}
		if($("#txt_standardId").val()!=""){//修改
			var url=path+"/sysset/updstaffTopicStand";
			var postData=$("#staffTopicStand_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
//					$("#btn_search").click();
//					$("#staffTopicStand_btn_cancel").click();
					$("#staffTopicStand_saveForm").form('reset');
					$('#tbl_staffMarkStandard_detail').datagrid('load');
					$('#staffTopicStand_edit').dialog('close');
				}else{
					alert(mess);
				}
			});		
		}else{//增加
			var url=path+"/sysset/savestaffTopicStand";
			var postData=$("#staffTopicStand_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#staffTopicStand_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#staffTopicStand_saveForm").form('reset');
					$('#tbl_staffMarkStandard_detail').datagrid('load');
					$('#staffTopicStand_edit').dialog('close');
				}else{
					alert(mess);
				}			
			});	
		}

	});
//	   删除提示

	 $("#staffTopicStand_btn_del").click(function(){
	     var row = $('#tbl_staffMarkStandard_detail').datagrid('getSelected');
		 $("#staffTopicStand_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.staffTitle)
			 $("#staffTopicStand_delect").dialog('open');
		 }
		 $("#staffTopicStand_btn_del").linkbutton('enable');
	 });
	 $("#staffTopicStand_btn_delect").click(function(){
		 $("#staffTopicStand_btn_delect").linkbutton('disable');
		 var url=path+"/sysset/delstaffTopicStand";
		 var row = $('#tbl_staffMarkStandard_detail').datagrid('getSelected');
		 var memo="科研达标积分标准 ："+row.staffTitle;
		 var postData={"standardId":row.standardId,"memo":memo}; 
		 $.post(url,postData,function(data){
				 alert("删除成功！");
				 $('#tbl_staffMarkStandard_detail').datagrid('load');
				 $("#staffTopicStand_btn_delect").linkbutton('enable');
				 $('#staffTopicStand_delect').dialog('close');
			     $("#staffTopicStand_btn_delect").linkbutton('enable');
		 });
	 });
	
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#staffTopicStand_btn_cancel").click(function(){
		$("#txt_standardId").val("");
		$("#txt_staffTitle").textbox("setValue","");
		$("#txt_markStandard").textbox("setValue","");
		//academicMemo 的清空
		$('#staffTopicStand_edit').dialog('close');
	});
	$("#staffTopicStand_btn_cancelDel").click(function(){
		$('#staffTopicStand_delect').dialog('close');
	});
	 
	 

	function initStaffTopic(){
		var url=path+"/sysset/queryStaffTopicStand";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
		$('#tbl_staffMarkStandard_detail').datagrid({
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
			remoteSort:true,
			pagination:true,
		    pageSize:10,
		    url:url,
		    queryParams: $('#queryForm').serializeJSON(),
		    columns:[[
		    	{field:'staffTitle',title:'职  称',width:50,sortable:true},
				{field:'markStandard',title:'积分标准',width:50,sortable:true},
				{field:'belongCycle',title:'科研周期',width:50,sortable:true}
		    ]],
		    onBeforeLoad:function(param){  //这个param就是queryString 
		    	var pageNo = param.page; //保存下值
				delete param.page; //删掉
				param.pageNo = pageNo; //这里就是重新命名了！！！ ,j将page 改为了 pages
				var maxResults = param.rows;
				delete param.rows; //删掉
				param.maxResults = maxResults; //这里就是重新命名了！！！ ,j将page 改为了 pages
//				param["pageNo"] = param.page;
//              param["maxResults"] = param.rows;
		    },
		});
	}
	
});