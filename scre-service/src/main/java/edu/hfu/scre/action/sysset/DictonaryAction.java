package edu.hfu.scre.action.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.sysset.DictionaryService;
import edu.hfu.scre.util.CacheData;

@RestController
@RequestMapping(path = "/sysset")
public class DictonaryAction {

	private static final Logger LOG = LoggerFactory.getLogger(DictonaryAction.class);
	
	@Resource
	private DictionaryService dictionaryService;
	
	@RequestMapping(value="/initDict", method= {RequestMethod.GET,RequestMethod.POST})
	//返回路径和页面用ModelAndView
	@PreAuthorize("hasPermission('/initDict','000503')")
	public ModelAndView initDict(){
		LOG.debug("进入/sysset/dictionary.btl");
		ModelAndView mod= new ModelAndView("/sysset/dictionary.btl");
		return mod;
	}
	@RequestMapping(value="/queryDictionary", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryDictionary','000503000')")
	public  Map<String,Object> queryDictionary(String dictType){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			List<SysDictionary> rows=null;
			if (null==dictType||dictType.equals("")) {
				rows=dictionaryService.getAllDictonary();
			}else {
				rows=dictionaryService.getDictonaryByType(dictType);
			}
			Integer total=rows.size();
			LOG.debug("获取全部字典数据："+total);
			String mess="succ";
			rtnMap.put("total", total);
			rtnMap.put("rows", rows);
			rtnMap.put("mess", mess);
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	@RequestMapping(value="/queryDictByType", method= {RequestMethod.GET,RequestMethod.POST})
	public  List<SysDictionary> queryDictByType(String dictType){
		try {
			LOG.debug("查询字典类型："+dictType);
			if(null!=dictType) {
				List<SysDictionary> ls=dictionaryService.getDictonaryByType(dictType);
				return ls;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value="/getDictValByKey", method= {RequestMethod.POST})
	public String getDictValByKey(String dictType,String dictKey) {
		return CacheData.getDictValByKey(dictKey, dictType);
	}
}
