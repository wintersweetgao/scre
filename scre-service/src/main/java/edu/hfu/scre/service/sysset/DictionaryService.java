package edu.hfu.scre.service.sysset;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.util.CacheData;

@Service
public class DictionaryService {
	/**
	 * 获取所有的数据字典项
	 * @return
	 */
	public List<SysDictionary> getAllDictonary(){
		return CacheData.getDictList();
	}
	public List<SysDictionary> getDictonaryByType(String dictType){
		return CacheData.getDictListByType(dictType);
	}
}
