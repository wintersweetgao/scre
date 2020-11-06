/**
 *
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initScientific_detail();
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
	$("#txt_screType").combobox({
		onSelect:function(rec){
			var url =path+ '/sysset/queryDictByType';
			var postData={"dictType":rec.value};
			$.post(url,postData,function(data){
				$('#txt_screTopic').combobox('loadData', data);
				var data = $("#txt_screTopic").combobox('getData');//获取所有下拉框数据
				if (data.length>0){
					$("#txt_screTopic").combobox('select',data[0].dictKey);
				}
			});
		}
	});
	/**
	 * 点击增加按钮
	 */
	$("#scientific_btn_add").click(function(){
		$("#scientific_save").dialog("setTitle","新增科研课题");
		$('#scientific_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#scientific_btn_save").click(function(){
		var scieName=$("#txt_scieName").textbox('getValue');//项目名称
		if(scieName==""){
			alert("项目名称为空,请重新输入！");
			$("#txt_scieName").next('span').find('input').focus();
			return false;
		}
		var screTopic=$("#txt_screTopic").textbox('getValue');//项目名称
		if(screTopic==""){
			alert("科研达标内容为空,请重新输入！");
			$("#txt_screTopic").next('span').find('input').focus();
			return false;
		}
		var scieDepart=$("#txt_scieDepart").textbox('getValue');//项目部门
		if(scieDepart==""){
			alert("项目审批部门为空,请重新输入！");
			$("#txt_scieDepart").next('span').find('input').focus();
			return false;
		}
		var scieLeader=$("#txt_scieLeader").textbox('getValue');//项目负责人
		if(scieLeader==""){
			alert("项目负责人为空,请重新输入！");
			$("#txt_scieLeader").next('span').find('input').focus();
			return false;
		}
		var scieStartDate=$("#txt_scieStartDate").datebox('getValue');//项目立项时间
		if(scieStartDate==""){
			alert("立项时间为空,请重新输入！");
			$("#txt_scieStartDate").next('span').find('input').focus();
			return false;
		}
		var scieCloseDate=$("#txt_scieCloseDate").datebox('getValue');//获取结题时间
		if(scieCloseDate){
			var scieOk=$('input:radio[name="scieOk"]:checked').val();//是否结题
			//string转换为->date
			var sClosedate = scieCloseDate.split('-');
			var sdate = new Date(sClosedate[0], sClosedate[1]-1, sClosedate[2]);
			if  (sdate>new Date() && scieOk=='是'){
				alert('当前您还未结题，是否结题，请选择“否”')
				return false;
			}
		}

		if($("#txt_scieId").val()!=""){//修改
			var url=path+"/review/updScientific";
			var postData=$("#scientific_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#scientific_saveForm").form('reset');
					$('#tbl_scientific_detail').datagrid('load');
					$('#scientific_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/review/saveScientific";
			var postData=$("#scientific_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#scientific_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#scientific_saveForm").form('reset');
					$('#tbl_scientific_detail').datagrid('load');
					$('#scientific_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}

	});
	/**
	 * 修改页面
	 */
	$("#scientific_btn_edit").click(function(){
		$("#scientific_save").dialog("setTitle","修改科研课题");
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else if(row.status=="CApprove"){
			alert("CApprove,无法修改");
		}else{
			$('#scientific_save').dialog('open');
			$("#txt_scieId").val(row.scieId);
			$("#txt_screType").combobox("setValue",row.screType);
			$("#txt_screTopic").combobox("setValue",row.screTopic);
			$("#txt_scieName").textbox("setValue",row.scieName);
			$("#txt_scieLeader").textbox("setValue",row.scieLeader);
			$("#txt_scieDepart").textbox("setValue",row.scieDepart);
			$("#txt_scieStartDate").textbox("setValue",row.scieStartDate);
			$("#txt_scieCloseDate").textbox("setValue",row.scieCloseDate);
			if (row.scieOk=="是"){
				$("#rdb_scieOkYes").radiobutton("check",0);
			}else{
				$("#rdb_scieOkNo").radiobutton("check",1);
			}
			$("#txt_inOrder").combobox("setValue",row.inOrder);
			$('#scientific_save').dialog('open');

		}
	});

	/**
	 * 点击取消关闭页面
	 */
	$("#scientific_btn_cancel").click(function(){
		$("#txt_scieId").val("");

		$("#txt_screType").combobox("setValue","纵向科研");
		$("#txt_screTopic").combobox("setValue","国家级项目");
		$("#txt_scieName").textbox("setValue","");
		$("#txt_scieLeader").textbox("setValue","");
		$("#txt_scieDepart").textbox("setValue","");
		$("#txt_scieStartDate").textbox("setValue","");
		$("#txt_scieCloseDate").textbox("setValue","");
		$("#rdb_scieOkYes").radiobutton("check",0);
		$("#txt_inOrder").combobox("setValue",1);
		$('#scientific_save').dialog('close');
	});

	$("#scientific_btn_cancelDel").click(function(){
		$('#scientific_delect').dialog('close');
	});
	$("#scientific_btn_cancelSub").click(function(){
		$('#scientific_submit').dialog('close');
	});
	/**
	 * 删除提示
	 */
	$("#scientific_btn_del").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		$("#scientific_btn_del").linkbutton('disable');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			$("#del_scieId").val(row.scieId);
			$("#td_dismess").text("您确认删除记录："+row.scieName)
			$("#scientific_delect").dialog('open');
		}
		$("#scientific_btn_del").linkbutton('enable');
	});
	$("#scientific_btn_delect").click(function(){
		$("#scientific_btn_delect").linkbutton('disable');
		var url=path+"/review/delScientific";
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		var postData={"scieId":row.scieId,"scieName":row.scieName};
		$.post(url,postData,function(data){
			var mess=eval(data).mess;
			if ("succ"==mess){
				alert("删除成功！");
				$('#tbl_scientific_detail').datagrid('load');
				$("#scientific_btn_delect").linkbutton('enable');
				$('#scientific_delect').dialog('close');
			}else{
				alert(mess);
			}
			$("#scientific_btn_delect").linkbutton('enable');
		}).error(function(error){
			var mess=eval(error);
//				for(let key  in mess){
//			        console.log(key + '---' + mess[key])
//				}
			alert(mess.responseText);

		});
	});
	//提交界面
	$("#scientific_btn_sub").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
		}else{
			if(row.status=="CApprove"){
				alert("该信息已提交，请勿重复提交");
			}else{
				$("#td_submitMess").text("您确认提交记录："+row.scieName+",提交之后记录将不能修改或删除！")
				$("#scientific_submit").dialog('open');
			}
		}
	});
	$("#scientific_btn_submit").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if(row.fileCount==0){
			alert("附件资料不全，不能提交！");
			return false;
		}
		$("#scientific_btn_submit").linkbutton('disable');
		var url=path+"/review/subScientific";
		var postData={"scieId":row.scieId,"scieName":row.scieName};
		$.post(url,postData,function(data){
			var mess=eval(data).mess;
			if ("succ"==mess){
				alert("提交成功！");
				$("#scientific_btn_submit").linkbutton('enable');
				//需要刷新相关记录
				var rowIndex=$('#tbl_scientific_detail').datagrid('getRowIndex',row);
				row.status="CApprove";
				row.statusValue=getStatusValByKey(row.status);//更新状态值
				$('#tbl_scientific_detail').datagrid('refreshRow', rowIndex); //然后刷新该行即可
				setButtonStatus(row);
				$('#scientific_submit').dialog('close');
			}else{
				alert(mess);
			}
		});
	});
	function initScientific_detail(){
		var url=path+"/review/queryScientific";
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
				{field:'scieName',title:'项目名称',width:100},
				{field:'screType',title:'项目类别',width:100},
				{field:'scieDepart',title:'项目审批部门',width:100},
				{field:'scieLeader',title:'负责人',width:100},
				{field:'scieStartDate',title:'立项时间',width:100,formatter:function(val, row, index){
						if (val != null) {
							val=val.substr(0,10);
							return val;
						}
					}},
				{field:'scieCloseDate',title:'结束时间',width:100,formatter:function(val, row, index){
						if (val != null) {
							val=val.substr(0,10);
							return val;
						}
					}},
				{field:'scieOk',title:'是否结题',width:60},
				{field:'expectedMark',title:'可得积分',width:60},
				{field:'fileCount',title:'附件数',width:50,styler: function(value,row,index){
						return 'color:blue;';
					}},
				{field:'statusValue',title:'状态',width:50}

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
			onLoadSuccess:function(data){
				$(this).datagrid("selectRow",0);
				var row = $(this).datagrid('getSelected');
				setButtonStatus(row);
			},
			onClickRow: function(index,row){
				setButtonStatus(row);

			},
			onClickCell:function(index, field, value){
				if (field=="fileCount"){
					$(this).datagrid('selectRow',index);
					var row = $(this).datagrid('getSelected');
					if(value==0){
						alert("没有附件！");
						return false;
					}else{
						$("#scie_viewfile").previewImg({
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
	$("#scie_btn_cancelview").click(function(){
		$("#scie_viewfile").dialog('close');
	});

	function setButtonStatus(rowData){
		if (null!=rowData){
			if (rowData.status!="BApprove"){
				$("#scientific_btn_edit").linkbutton('disable');
				$("#scientific_btn_sub").linkbutton('disable');
				$("#scientific_btn_del").linkbutton('disable');
				$("#scientific_btn_file").linkbutton('disable');
			}else{
				$("#scientific_btn_edit").linkbutton('enable');
				$("#scientific_btn_sub").linkbutton('enable');
				$("#scientific_btn_del").linkbutton('enable');
				$("#scientific_btn_file").linkbutton('enable');
			}
		}
	}
	//上传文件
	$("#scientific_btn_file").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//弹出上传页面
			$("#scientific_material").uploadMaterial({
				datagrid_id:'tbl_scientific_detail',
				materialType:'RptScientific',
				screNameTitle:row.scieName,
				screId:row.scieId
			});
		}
	});
	$("#scientific_btn_log").click(function(){
		var row = $('#tbl_scientific_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			//查看日志
			$("#scientific_log").getReviewLog({
				tblName:'RptScientific',
				screId:row.scieId
			});
		}
	});


});