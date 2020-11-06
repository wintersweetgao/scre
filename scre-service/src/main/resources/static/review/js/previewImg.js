/**
 * 
 */
var wcviewfilelistSplider=null;
(function ($) {
    $.fn.previewImg= function(options){
        var defaults = {
        		width:600,
        		height:510,
        		title:'',
        		data:null
        }
        var options = $.extend(defaults, options);
        var path=getRootPath();
        //$(this).empty();
        //创建对话框
        var dlgId="previewfile"+$(this).attr("id");
        var isFirst=false;
        if ($("#"+dlgId).html()==undefined){
        	isFirst=true;
        	var html='<div id="'+dlgId+'" class="easyui-dialog" title="'+options.title+'附件预览"  '+
 			'style="width:'+options.width+'px;height:'+options.height+'px;overflow: hidden" '+
	            	'data-options="resizable:false,modal:true,closable:true,closed:true,onClose:function(){closePreviewfileDlg();}"> '+
			        	'<div style="width: 100%; height:auto;margin: 0 auto;">'+
			    			'<ul class="bxslider" id="wcviewfilelist">'+
			    			 
			    			'</ul>'+
			    		'</div>'+
			     '</div>';
		 	$(this).append(html);
		 	$.parser.parse("#"+$(this).attr("id")); // 解析某个具体节点
        }
        $("#wcviewfilelist").empty();
		var materials =options.data;
		if (materials==null){
			alert("没有要预览的图片数据");
		}
		var liStr="";
		var img404Path=path+"/static/images/img404.png";
		for (var i=0;i<materials.length;i++){
			
			liStr+=" <li style='width:584px;height:430px'><img src='"+path+materials[i].materialPath+"' " +
					"style='width:100%;height:100%'  title='"+materials[i].materialTitle+"' " +
							"onerror=\"javascript:this.src='"+img404Path+"'\" /></li>";
		}
		$("#wcviewfilelist").append(liStr);
		
		
		
		$("#"+dlgId).dialog('open');
		if (isFirst){
			wcviewfilelistSplider=$('#wcviewfilelist').bxSlider({
				mode:'horizontal', //默认的是水平 
				displaySlideQty:1,//显示li的个数 
				moveSlideQty: 1,//移动li的个数  
				captions: true,//标题
				auto: true, 
				controls: true
			});
		}else{
			wcviewfilelistSplider.reloadSlider();
		}
    };
   
})(jQuery);

function closePreviewfileDlg(){
	$("#wcviewfilelist").empty();
	if (null!=wcviewfilelistSplider){
//		wcviewfilelistSplider.destroySlider();
//		wcviewfilelistSplider.reloadSlider();
	}
}