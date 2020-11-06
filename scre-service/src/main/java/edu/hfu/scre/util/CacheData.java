package edu.hfu.scre.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysPost;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.entity.xmlBean.SysDictionaryList;
import edu.hfu.scre.service.security.MyUserDetails;

public class CacheData {
	
	private static List<SysDictionary> dictList=new ArrayList<>();//数据字典 列表
	private static Map<String,List<SysDictionary>> dictMap=new HashMap<>();//根据字典类型进行分类
	private static List<SysPost> postList=new ArrayList<>();//一级部门
	
	

	private static List<SysDepart> departOneList=new ArrayList<>();//一级部门
	private static Map<Integer,List<SysDepart>> departTwoList=new HashMap<>();//二级部门
	
	public static List<SysDictionary> getDictList() {
		return dictList;
	}

	/**
	 * 根据类型获取 数据字典
	 * @param dictType
	 * @return
	 */
	public static List<SysDictionary> getDictListByType(String dictType){
		return dictMap.get(dictType);
	}
	/**
	 * 根据科研获取value
	 * @param dictKey
	 * @param dictType
	 * @return
	 */
	
	public static String getDictValByKey(String dictKey,String dictType) {
		List<SysDictionary> ls=dictMap.get(dictType);
		String dictVal="";
		for (SysDictionary dict:ls) {
			if (dict.getDictKey().equals(dictKey)) {
				dictVal= dict.getDictValue();
			}
		}
		return dictVal;
	}
	
	public static void seCacheData(Object data) {
		if (data instanceof SysDictionaryList) {
			SysDictionaryList dictls=(SysDictionaryList)data;
			dictList=dictls.getDictList();
			for(SysDictionary dict:dictList) {
				List<SysDictionary> dictTypes=dictMap.get(dict.getDictType());
				if (null==dictTypes) {
					dictTypes=new ArrayList<>();
					dictTypes.add(dict);
				}else {
					dictTypes.add(dict);
				}
				dictMap.put(dict.getDictType(), dictTypes);
			}
		}
	}

	public static List<SysDepart> getDepartOneList() {
		return departOneList;
	}

	public static void setDepartOneList(List<SysDepart> departOneList) {
		CacheData.departOneList = departOneList;
	}

	public static List<SysDepart> getDepartTwoList(Integer departId) {
		return departTwoList.get(departId);
	}

	public static void setDepartTwoList(Integer departId ,List<SysDepart>departTwoList) {
		CacheData.departTwoList.put(departId, departTwoList);
	}
	public static List<SysPost> getPostList() {
		return postList;
	}

	public static void setPostList(List<SysPost> postList) {
		CacheData.postList = postList;
	}
	
	public static String getAccessCode() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal=auth.getPrincipal();
		//用户代码等
		if (principal != null && principal instanceof MyUserDetails) {
			MyUserDetails usd = (MyUserDetails) principal;
			SysStaff user=(SysStaff)usd.getCustomObj();
			return user.getAccessCode();
		}else {
			return null;
		}
		
	}
	
}
