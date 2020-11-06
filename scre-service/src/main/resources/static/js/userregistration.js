/**
 * 
 */

$(document).ready(function(){
	var path=getRootPath();
	initDeparts();
	initPosts();
	
	$("#txt_addStaffPhone").textbox('textbox').bind('keyup', function(e){
		  $("#txt_addStaffPhone").textbox('setValue', $(this).val().replace(/\D/g,''));

		});
	$("#txt_idCard").textbox('textbox').bind('keyup', function(e){
		  $("#txt_idCard").textbox('setValue', $(this).val().replace(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,''));
		});
	//点击确定，进行保存
	$("#btn_save").click(function(){	
	var staffPhone=$("#txt_addStaffPhone").textbox('getValue');
	var idCard=$("#txt_idCard").textbox('getValue');
		if ($("#txt_userCode").val()==""){
			alert("账号不能为空");
			return false;
		}else if ($("#txt_userPass").val()==""){
			alert("密码不能为空");
			return false;

		}else if ($("#txt_addStaffName").val()==""){
			alert("姓名不能为空");
			return false;
		}else if ($("#txt_admin").val()==""){


			alert("用户类型不能为空");
			return false;
		}else if ($("#txt_departIds").val()==""){
			alert("所属部门不能为空");
			return false;
		}else if ($("#txt_postIds").val()==""){
			alert("职务不能为空");
			return false;
		}else if ($("#txt_addStaffPhone").val()==""){
			alert("联系方式不能为空");
			return false;
		}else if(staffPhone.length>11){
			alert("联系方式小于12位");	
			$('#txt_addStaffPhone').textbox().next('span').find('input').focus();
		}
//		else if(idCard.length<18||idCard.length>18){
//			alert("身份证号为18位");	
//			$('#txt_idCard').textbox().next('span').find('input').focus();
//		}
		else{
			$("#btn_save").linkbutton('disable');
		    var urls=path+"/sysset/registerStaff";
		    var postDatas=$("#saveStaff").serialize();
		    $.post(urls,postDatas,function(data){
			    $("#btn_save").linkbutton('enable');
			    var mess=eval(data).mess;
			    if ("succ"==mess){
				    alert("保存成功！");
				    $("#saveStaff")[0].reset();
				    $('#tbl_staff_detail').datagrid('load');
				    $('#dlg_saveStaff').dialog('close');
			    }else if("error"==mess){
			    	alert("账号不能重复");
			    }else{
				    alert(mess);
				    }
			    });
		    }
		});
//	});
//	//点击取消，关闭增加窗口
//	$("#btn_savecancel").click(function(){
//		$('#dlg_saveStaff').dialog('close');
//	});
	
	//获取职务以及复选框
	function initPosts(){
		url = path + "/postgrant/getPost"
		$.post(url, "", function(data) {
			var lsPost = eval(data).posts;
			$("#txt_postIds").combobox({
			    data:lsPost,
			    valueField:'postId',
			    textField:'postName'
			});
		});
	}
	
	//获取部门以及复选框
	function initDeparts(){
		url=path + "/sysset/getDepart";
		$.post(url,"",function(data){	
			var lsDepart=eval(data).departs;
			$("#txt_departIds").combobox({
			    data:lsDepart,
			    valueField:'departId',
			    textField:'departName'
			});
		
		
		});
	}
	
});