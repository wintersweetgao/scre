/**
 * 
 */
(function ($) {
	$.fn.getReviewLog= function(options){
    	var defaults = {
    			tblName:'',
    			screId:''
        }
    	var options = $.extend(defaults, options);
    	if (options.tblName==""){
    		alert("缺少参数tblName(表名称)！");
    		return false;
    	}
    	if (options.screId==""){
    		alert("缺少参数screId(要查看记录的主键)！");
    		return false;
    	}
    	var path=getRootPath();
    	var url=path+'/review/initReviewLog?tblName='+options.tblName+"&screId="+options.screId
    	$(this).dialog({
    	    title: '查看审批日志',
    	    width: 650,
    	    height: 330,
    	    iconCls:'icon-search',
    	    resizable:false,
    	    closable:true,
    	    closed: true,
    	    cache: false,
    	    href: url,
    	    modal: true
    	});
    	$(this).dialog("open");
    	
	};
 })(jQuery);