/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	//初始化
	initnotice_detail();
	  /*******************文本编辑器 逻辑 开始*****************/
    var editor;
    //详情编辑器，编辑和新增的逻辑分开，在不同的ID上创建
    KindEditor.ready(function (K) {
        editor = K.create('#textarea_editor_edit',{
            items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
				'link', 'unlink', '|', 'about'
            ],
            resizeType: 1,   //2时可以拖动改变宽度和高度，1时只能改变高度，0时不能拖动。
            allowPreviewEmoticons : true,//true时鼠标放在表情上可以预览表情。
            allowImageUpload : true,
            allowFileManager : false, //插入文件-》文件空间，设置为无
            allowImageRemote: false, // true时显示网络图片标签，false时不显示
            imageTabIndex:1,
            uploadJson : path+"/review/uploadFile",
            filterMode : false,
        });
    });

	/**
	 * 查询
	 */
	$("#btn_search").click(function(){
		$("#btn_search").linkbutton('disable');
		var queryParams=$('#notice_queryForm').serializeJSON();
		queryParams.onLoadSuccess=function(data){
			$("#btn_search").linkbutton('enable');
		};
		$('#tbl_notice_detail').datagrid('load',queryParams);
	});
	/**
	 * 点击增加按钮
	 */
	$("#notice_btn_add").click(function(){
		$("#notice_save").dialog("setTitle","发布通知公告");
		$('#notice_save').dialog('open');
	});
	/**
	 * 确定 增加或修改
	 */
	$("#notice_btn_save").click(function(){
		// 同步数据后可以直接取得textarea的value
		 editor.sync();
		var noticeTitle=$("#txt_noticeTitle").textbox('getValue');//公告标题
		if(noticeTitle==""){
			alert("公告标题为空,请重新输入！");
			$("#txt_noticeTitle").next('span').find('input').focus();
			return false;
		}
		var noticeContent=$("#textarea_editor_edit").val();//公告内容
		if(noticeContent==""){
			alert("公告内容为空,请重新输入！");
			$("#textarea_editor_edit").next('span').find('input').focus();
			return false;
		} 
		if($("#txt_noticeId").val()!=""){//修改
			var url=path+"/sysset/updNotice";
			var postData=$("#notice_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				var mess=data;
				if ("succ"==mess){
					alert("修改成功！");
					$("#notice_saveForm").form('reset');
					$('#tbl_notice_detail').datagrid('load');
					clearForm();
					$('#notice_save').dialog('close');
				}else{
					alert(mess);
				}
			}).error(function(error){
				var mess=eval(error);
				alert(mess.responseText);
			});
		}else{//增加
			var url=path+"/sysset/addNotice";
			var postData=$("#notice_saveForm").serializeJSON();
			$.post(url,postData,function(data){
				$("#notice_btn_save").linkbutton('enable');
				var mess=data;
				if ("succ"==mess){
					alert("保存成功！");
					$("#notice_saveForm").form('reset');
					$('#tbl_notice_detail').datagrid('load');
					clearForm();
					$('#notice_save').dialog('close');
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
	$("#notice_btn_edit").click(function(){
		$("#notice_save").dialog("setTitle","修改通知公告");
		var row = $('#tbl_notice_detail').datagrid('getSelected');
		if (row==null){
			alert("尚未选中任何记录！");
			return false;
		}else{
			var htmlContent = row.noticeContent;
			$('#notice_save').dialog('open');
		    editor.html(htmlContent);
			$("#txt_noticeId").val(row.noticeId);
			
			$("#txt_noticeTitle").textbox("setValue",row.noticeTitle);
			$("#txt_noticeReadvolume").textbox("setValue",row.noticeReadvolume);
			$('#notice_save').dialog('open');
			editor.sync();
            //这个隐藏的也复制，更新时作为主键
	    }
	 });
	
	 /**
	  * 点击取消关闭页面
	  */
	$("#notice_btn_cancel").click(function(){
		clearForm();
		$('#notice_save').dialog('close');
	});
	 
	function clearForm(){
		editor.html('');
		$("#txt_noticeId").val("");
		$("#txt_noticeTitle").textbox("setValue","");
		//$("#textarea_editor_edit").textbox("setValue","");
		$("#txt_noticeReadvolume").textbox("setValue","");
		
	}
	 
	$("#notice_btn_cancelDel").click(function(){
		$('#notice_delect').dialog('close');
	});
	$("#notice_btn_cancelSub").click(function(){
		$('#notice_submit').dialog('close');
	});
	 /**
	  * 删除提示
		 */
	 $("#notice_btn_del").click(function(){
		 var row = $('#tbl_notice_detail').datagrid('getSelected');
		 $("#notice_btn_del").linkbutton('disable');
		 if (row==null){
			 alert("尚未选中任何记录！");
		 }else{
			 $("#td_dismess").text("您确认删除记录："+row.noticeName)
			 $("#notice_delect").dialog('open');
		 }
		 $("#notice_btn_del").linkbutton('enable');
	 });
	 $("#notice_btn_delect").click(function(){
		 $("#notice_btn_delect").linkbutton('disable');
		 var url=path+"/sysset/delNoticeById";
		 var row = $('#tbl_notice_detail').datagrid('getSelected');
		 var memo="删除通知公告："+row.noticeName;
		 var postData={"noticeId":row.noticeId,"memo":memo}; 
		 $.post(url,postData,function(data){
			 var mess=eval(data).mess;
			 if ("succ"==mess){
				 alert("删除成功！");
				 $('#tbl_notice_detail').datagrid('load');
				 $("#notice_btn_delect").linkbutton('enable');
				 $('#notice_delect').dialog('close');
			 }else{
				 alert(mess);
			 }
			 $("#notice_btn_delect").linkbutton('enable');
		 }).error(function(error){
			var mess=eval(error);
			alert(mess.responseText);
			});
	 });
	
	function initnotice_detail(){
		var url=path+"/sysset/getAllNotice";
		$('#tbl_notice_detail').datagrid({
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
		    queryParams: $('#notice_queryForm').serializeJSON(),
		    columns:[[
				{field:'noticeTitle',title:'公告标题',width:150},
				{field:'noticeName',title:'发布者',width:100},
				{field:'noticeReadvolume',title:'阅读量',width:100},
				{field:'createTime',title:'创建公告时间',width:100,formatter:function(val, row, index){
				    if (val != null) {
				    	val=val.substr(0,10);
				    	return val;
			        }
				}},
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

		});
	}
	$("#scie_btn_cancelview").click(function(){
		$("#scie_viewfile").dialog('close');
	});

	
});