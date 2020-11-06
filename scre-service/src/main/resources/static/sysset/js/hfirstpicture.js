/**
 * 
 */
$(document).ready(function(){
	var path=getRootPath();
	$("#tbl_hfirstPicture_detail .easyui-linkbutton").click(function(){
		var trObj=$(this).parent().parent();
		var materialfile=$(trObj).find(".easyui-filebox");
		var fileVal=$(materialfile).textbox('getValue');
		if(fileVal==""){
			alert("上传内容为空,请重新输入！");
			return false;
		}
		var formId=$(trObj).find("form");
		upAdFile(formId);
	});

	
	function  upAdFile(formId){
		var url=path+"/sysset/hfirstPictureFileUp";
		var postData = new FormData($(formId)[0]);
		$.ajax({
			type: "post",
			url: url,
			data : postData,
			cache: false,			 //不需要缓存
			processData : false,  	//必须false才会避开jQuery对 formdata 的默认处理
			contentType : false, 	 //必须false才会自动加上正确的Content-Type
			success:function(data){
				if (data.indexOf("succ")>-1){
					alert("上传成功");
					var filepath=data.split(":")[1];
					$(formId).parent().parent().find("td:eq(1)").text(filepath);					
				}else{
					alert(data);
				}
			},
			error:function(){
				alert("请求出错！");
			}
		});
		
	}
});