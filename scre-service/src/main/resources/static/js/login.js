/**
 * 登录
 */
$(document).ready(function(){
	var path=getRootPath();
	initnotice();
	querycxxExhibition();
	queryjjxExhibition();
	queryglxExhibition();
	querykjxExhibition();
	queryrwxExhibition();
	queryysxExhibition();
	var  showSelectcxx=function(row){
		
		if(row.rptId){
			//window.open(path+"/showRptPageDetail?rptId="+row.rptId+"&className="+row.className);
			window.open(path+"/getexcellence?rptId="+row.rptId+"&className="+row.className);
	
		}
	}
	//公告
	var showSelectedSurveryDataOnMap=function(row){
		
		if(row.noticeId){
			window.open(path+"/idxNotice?noticeId="+row.noticeId);
			
		}

	}
	//财信系
	function querycxxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_cxx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"财经信息工程系"},
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
 				}
		});
	}
	//经济系
	function queryjjxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_jjx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"经济系"},
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
 				}
		});
	}
	//会计系
	function querykjxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_kjx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"会计系"},
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
 				}
		});
	}
	//人文科学系
	function queryrwxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_rwx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"人文科学系"},
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
 				}
		});
	}
	//管理系
	function queryglxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_glx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"管理系"},
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
 				}
		});
	}
	//艺术系
	function queryysxExhibition(){
		var url=path+"/queryViewExhibition";
		$('#tbl_ysx_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    queryParams:{"staffParentDepart":"艺术系"},
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
 				}
		});
	}
	//公告
	function initnotice(){
		var url=path+"/getNoticee";
		$('#tbl_notice_detail').datagrid({
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:false,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
		    url:url,
		    pagination:false,
		    striped:false,
		    columns:[[
				{field:'noticeTitle',title:'公告标题',width:300},
				{field:'createTime',align: 'right',title:'发表时间',width:50,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
		    ]],
		    onClickRow :function(rowIndex,rowData){

 				 showSelectedSurveryDataOnMap(rowData);

 				}
		});
	}
	$("#btn_login").click(function(){
		
		if ($("#txt_userName").val()==""){
			alert("请输入工号！");
			return false;
		}if ($("#txt_userPass").val()==""){
			alert("请输入密码！");
			return false;
		}else{
			$("#loginForm").submit();
			
			
//			var url=path+"/userLogin";
//			var postData={"userCode":$("#txt_userName").val(),"userPass":$("#txt_userPass").val()};
//			$.post(url,postData,function(data){
//				var mess=eval(data).mess;
//				if (mess=="succ"){
//					window.location.href=path+"/goIndex";
//				}else{
//					alert(mess);
//					return false;
//				}
//				
//			}).error(function(error){
//				var mess=eval(error);
//				mess=eval("("+mess.responseText+")");
////				for(let key  in mess){
////			        console.log(key + '---' + mess[key])
////				}
//				alert("登录失败，请联系管理员："+mess.message);
//				
//			});
			
			
		}
		
	});
	
});