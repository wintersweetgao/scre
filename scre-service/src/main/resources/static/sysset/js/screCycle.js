/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initCycle_detail();
	
	$("#sycle_btn_add").click(function(){
		$("#txt_cycleName").textbox("setValue","");
		$("#scrdCycle_edit").dialog("open");
	});	
	$("#scrdCycle_btn_save").click(function(){
		var url=path+"/sysset/addNextCycle";
		var postData=$("#screCycleForm").serializeJSON();
		$.post(url,postData,function(data){
			if (data=="succ"){
				alert("新增科研周期成功！");
				$('#tbl_cycle_detail').datagrid('load',{});
				$("#scrdCycle_edit").dialog("close");
			}else{
				alert(data);
			}
		});
	});
	
	$("#scrdCycle_btn_cancel").click(function(){
		$("#scrdCycle_edit").dialog("close");
	});
	
	function initCycle_detail(){
		var url=path+"/sysset/queryAllCycle";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
		$('#tbl_cycle_detail').datagrid({
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
		    queryParams: $('#screCycleForm').serializeJSON(),
		    columns:[[
				{field:'cycleName',title:'周期名称',width:100},
				{field:'beginDate',title:'开始日期',width:100},
				{field:'endDate',title:'结束日期',width:100},
				{field:'isOver',title:'状态',width:100}
		    ]],
		    onLoadSuccess:function(data){
		    	if (data.mess=="succ"){
		    		var rows=data.rows;
		    		if (null!=rows&&rows.length>0){
		    			$('#tbl_cycle_detail').datagrid("selectRow",0);
		    			$("#span_cycleName").text(rows[0].cycleName);
		    			$("#span_endDate").text(rows[0].endDate);
		    		}else{
		    			$("#span_cycleName").text("未定义");
		    			$("#span_endDate").text("未定义");
		    		}
		    	}
		    }
		});
	}
	
});
