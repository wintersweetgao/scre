/**
 * 
 */
(function ($) {
	$.fn.uploadMaterial= function(options){
    	var defaults = {
    			datagrid_id:'',
    			materialType:'',
    			screNameTitle:'',
    			screId:'',
    			materials:'materials',
    			fileCount:'fileCount'
    			
        }
    	var options = $.extend(defaults, options);
    	if (options.datagrid_id==""){
    		alert("缺少参数datagrid_id！");
    		return false;
    	}
    	if (options.materialType==""){
    		alert("缺少参数materialType(材料类型)！");
    		return false;
    	}
    	if (options.screNameTitle==""){
    		alert("缺少参数screNameTitle(上传材料的主题)！");
    		return false;
    	}
    	if (options.screId==""){
    		alert("缺少参数screId(要上传材料的主键)！");
    		return false;
    	}
    	if($("#dlg_commonmaterial").length>0){
    		$("#txt_materialType").val(options.materialType);
        	$("#txt_screNameTitle").val(options.screNameTitle);
        	$("#sp_filetitle").text(options.screNameTitle);
        	$("#txt_screId").val(options.screId);
    		
    		$("#dlg_commonmaterial").dialog('open');
    		//初始化 内容
    		initMaterial();
    	}else{
    		var path=getRootPath();
        	var url= path+'/review/initUploadMaterial';
        	var postData={};
        	var idObj="#"+$(this).attr("id");
        	$.post(url,postData,function(data){
        		$(idObj).html(data);
        		$.parser.parse(idObj); // 解析某个具体节点
        		$("#txt_materialType").val(options.materialType);
            	$("#txt_screNameTitle").val(options.screNameTitle);
            	$("#sp_filetitle").text(options.screNameTitle);
            	$("#txt_screId").val(options.screId);
        		
        		$("#dlg_commonmaterial").dialog('open');
        		//初始化 内容
        		initMaterial();
        		//绑定
            	$("#btn_materialSave").click(function(){
            		upLoadfile();
            	});
            	$("#btn_materialCancel").click(function(){
            		$("#dlg_commonmaterial").dialog('close');
            	});
            	
        	});
    	}
    	function initMaterial(){
    		//初始化
    		$("#material_fileForm .easyui-filebox").each(function(index){
    			$(this).textbox('setValue','');
    		});
    		$("#material_fileForm .easyui-textbox").each(function(index){
    			if (index==0){
    				$(this).textbox('setValue','封面');
    			}else if (index==1){
    				$(this).textbox('setValue','目录');
    			}else if (index==2){
    				$(this).textbox('setValue','正文');
    			}else{
    				$(this).textbox('setValue','');
    			}
    		});
    	}
    	
    	function upLoadfile(){
    		var isContinue=true;
    		$("#dlg_commonmaterial #material_fileForm input[name='materialTitle']").each(function(index){
    			var fileid="#file_material"+index;
	   			if ($(this).val()!=""){
	   				var fileName=$(fileid).textbox('getValue');
	   				if (fileName==""){
	   					alert("请选择'"+$(this).val()+"'对应的jpg文件!");
	   					isContinue=false;
	   					return false;
	   				}else{
	   					var fileExtension = fileName.split('.').pop();
	   					if (fileExtension!="jpg"){
	   						alert("请选择'"+$(this).val()+"'对应的jpg文件!");
	   						isContinue=false;
	   						return false;
	   					}
	   				}
	   				var curTileVal=$(this).val();
	   				//判断文件标题是否重复
	   				$(idObj+" #material_fileForm input[name='materialTitle']").each(function(item){
	   					if (($(this).val()==curTileVal)&&(item>index)){
	   						alert("重复的文件标题:"+curTileVal);
	   						isContinue=false;
	   						return false;
	   					}
	   				});
	   			}else{
	   				if ($(fileid).textbox('getValue')!=""){
	   					alert("请填写第"+(index+1)+"文件标题");
	   					isContinue=false;
	   					return false;
	   				}
	   			}
	   			
	   			if(!isContinue){//不继续
	   				return false;
	   			}
    		});
    		if(isContinue){
    			var url = path+"/review/materialFileUp";
    			var formData = new FormData($("#material_fileForm")[0]);
    			$.ajax({
    				type: "post",
    				url: url,
    				data : formData,
    				cache: false,			 //不需要缓存
    				processData : false,  	//必须false才会避开jQuery对 formdata 的默认处理
    				contentType : false, 	 //必须false才会自动加上正确的Content-Type
    				success:function(data){
    					var mess=eval(data).mess;
    					if (mess=="succ"){
    						alert("上传成功");
    						//需要刷新相关记录
    						var row = $('#'+options.datagrid_id).datagrid('getSelected');
    						var rowIndex=$('#'+options.datagrid_id).datagrid('getRowIndex',row);
    						//方法1--更新动态key对应的值
    						eval("row."+options.materials+"=(data).materials");
    						//方法2--更新动态key对应的值
    						row[options.fileCount] = (data).materials.length; //首先要找到该行，然后为目标字段赋值
    						$('#'+options.datagrid_id).datagrid('refreshRow', rowIndex); //然后刷新该行即可
	   						$("#dlg_commonmaterial").dialog('close');
    					}else{
    						alert(mess);
    					}
    				},
    				error:function(){
	   					 alert("请求出错！");
    				}
    			});
    		}
    	}
	};
 })(jQuery);