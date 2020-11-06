/**
 * 
 */
/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initScreStand_detail();
	var tmpScreTopic=null;
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_screStand_detail').datagrid('load',queryParams);
	});
	//选择科研类型
	$("#txt_screType").combobox({
	    onSelect: function(rec){
	    	var url =path+ '/sysset/queryDictByType';
	    	var postData={"dictType":rec.value};
	    	$.post(url,postData,function(data){
	    		$('#txt_screTopic').combobox('loadData', data);
	    		var data = $("#txt_screTopic").combobox('getData');//获取所有下拉框数据
		    	if (data.length>0){
		    		if (tmpScreTopic!=null){
		    			$("#txt_screTopic").combobox('setValue',tmpScreTopic);
		    		}else{
		    			$("#txt_screTopic").combobox('select',data[0].dictKey);
		    		}
		    	}
	    	});
	    }	    
	});
	//根据人数确定有效的权重系数
	$("#sel_validNum").combobox({
		onSelect: function(rec){
	    	$(".weight").each(function(index){
	    		var personNum=rec.value;
	    		var tmpPerson=$(this).attr("data-person");
	    		if (tmpPerson<=personNum){
	    			$(this).numberbox("enable");
	    		}else{
	    			$(this).numberbox("disable");
	    		}
	    	});
	    },
	});
	$("#screStand_btn_edit").click(function(){
		var row=$('#tbl_screStand_detail').datagrid("getSelected");
		if (row==null){
			alert("请选中一行!");
			return false;
		}else{
			$("#scrdStand_edit").dialog("setTitle","修改评分标准");
			$("#txt_standardId").val(row.standardId);
			tmpScreTopic=row.screTopic;
			
			//控件赋值
			$("#screStandForm .setval").each(function(){
				//获取控件名称
				var tmpName=$(this).next('span').find('input[type="hidden"]').attr("name");
				var tmpValue=eval("row."+tmpName);
				if ($(this).hasClass("easyui-combobox")){
					$(this).combobox("setValue",tmpValue);
				}else if ($(this).hasClass("easyui-numberbox")){
					if (tmpValue==0){
						tmpValue="";
					}
					$(this).numberbox("setValue",tmpValue);
				}
			});
			$("#scrdStand_edit").dialog("open");
		}
	});
	
	//新增
	$("#screStand_btn_add").click(function(){
		$("#scrdStand_edit").dialog("setTitle","新增评分标准");
		$("#scrdStand_edit").dialog("open");
	});
	//保存，修改
	$("#scrdStand_btn_save").click(function(){
		if ($("#txt_screMark").val()==""){
			alert("请填写标准分值！");
			$('#txt_screMark').next('span').find('input').focus();
			return false;
		}
		if($("#txt_belongCycle").combobox("getValue")==""){
			alert("请选择科研周期！");
			return false;
			
		}		
		
		var personNum=$("#sel_validNum").combobox("getValue");
		var  execFlag=true;
		$(".weight").each(function(index){
    		var tmpPerson=$(this).attr("data-person");
    		if (tmpPerson<=personNum){
    			if($(this).numberbox("getValue")==""){
    				alert("请填写权重系数"+tmpPerson+"！");
    				$(this).next('span').find('input').focus();
    				execFlag=false;
    				return false;
    			}
    		}
    	});
		
		if (execFlag){
			if($("#txt_standardId").val()==""){//增加
				var url=path+"/sysset/saveScreStandard";
				var postData=$("#screStandForm").serializeJSON();
				$.post(url,postData,function(data){
					if (data=="succ"){
						alert("保存成功！");
						$("#btn_search").click();
						$("#scrdStand_btn_cancel").click();
					}else{
						alert(data);
					}
				});
			}else{//修改
				var url=path+"/sysset/updScreStandard";
				var postData=$("#screStandForm").serializeJSON();
				$.post(url,postData,function(data){
					if (data=="succ"){
						tmpScreTopic=null;
						alert("修改保存成功！");
						$("#btn_search").click();
						$("#scrdStand_btn_cancel").click();
					}else{
						alert(data);
					}
				});
			}
		}
	});
	//删除
	$("#screStand_btn_del").click(function(){
		var row=$('#tbl_screStand_detail').datagrid("getSelected");
		if (row==null){
			alert("请选中一行!");
			return false;
		}else{
			var screTopic=row.screTopic;
			$.messager.confirm('确认','您确认要删除记录:'+screTopic+'么？',function(r){
				if (r){
					var standardId=row.standardId;
					var url=path+"/sysset/delScreStandard";
					var postData={"standardId":standardId};
					$.post(url,postData,function(data){
						if (data=="succ"){
							alert("修改保存成功！");
							$("#btn_search").click();
							$("#scrdStand_btn_cancel").click();
						}else{
							alert(data);
						}
					});
				}
			});

		}
	});
	
	//取消
	$("#scrdStand_btn_cancel").click(function(){
		resetForm();
		$("#scrdStand_edit").dialog("close");
	});
	function resetForm(){
		$("#txt_standardId").val("");
		$("#screStandForm .setval").each(function(){
			if ($(this).hasClass("easyui-combobox")){
				$(this).combobox("reset");
			}else if ($(this).hasClass("easyui-numberbox")){
				$(this).numberbox("setValue","");
			}
		});
		//科研达标类型 第一个有效
    	var data = $("#txt_screType").combobox('getData');//获取所有下拉框数据
    	if (data.length>0){
    		$("#txt_screType").combobox('select',data[0].value);
    	}
	}
	
	
	function initScreStand_detail(){
		var url=path+"/sysset/queryScreStand";
		//data-options="border:false,singleSelect:true,fit:true,fitColumns:true,rownumbers:true"
		$('#tbl_screStand_detail').datagrid({
		    pagination:true,
			border:false,
			singleSelect:true,
			fit:true,
			fitColumns:true,
			rownumbers:true,
			autoRowHeight:false,
			nowrap:true,
			loadMsg:"正在加载，请稍后...",
			striped:true,
			sortName:"screType",
			remoteSort:false,
		    url:url,
		    multiSort:true,
		    pageSize:10,
		    pageNumber:1,
		    queryParams: $('#queryForm').serializeJSON(),
		    columns:[[
				{field:'screType',title:'科研达标类型',width:100,sortable:true},
				{field:'screTopic',title:'科研达标内容',width:140,sortable:true},
				{field:'validNum',title:'有效人数',width:80},
				{field:'screMark',title:'标准分值',width:80,sortable:true},
				{field:'weight1',title:'权重系数1',width:80},
				{field:'weight2',title:'权重系数2',width:80,formatter:formatterWeight,styler: stylerWeight},
				{field:'weight3',title:'权重系数3',width:80,formatter:formatterWeight,styler: stylerWeight},
				{field:'weight4',title:'权重系数4',width:80,formatter:formatterWeight,styler: stylerWeight},
				{field:'weight5',title:'权重系数5',width:80,formatter:formatterWeight,styler: stylerWeight},
				{field:'belongCycle',title:'科研周期',width:50,sortable:true}
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
		    	//alert(data);
		    }
		});
	}
	
	function formatterWeight(value,row,index){
		if (value==0){
			return "";
		}else{
			return value;
		}
	}
	function stylerWeight(value,row,index){
		if (value==0){
			return 'background-color:#ffeedd;';
		}
	}
});
