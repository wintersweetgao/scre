package edu.hfu.scre.service.security;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.security.UserGrantDao;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.util.CacheData;
@Service
public class DepartService {
	@Resource
	UserGrantDao userGrantDao;
	/**
	 * 获取所有一级部门
	 * @return
	 */
	public List<SysDepart> getAllDepartOne(){
		List<SysDepart> ls=userGrantDao.getAllDepart(CacheData.getAccessCode());
		return ls;
	}
	/**
	 * 获取二级部门
	 * */
	public List<SysDepart> getAllDepartTwo(Integer departId){
		List<SysDepart> ls=userGrantDao.getDepartByLvl2(CacheData.getAccessCode(),departId);
		return ls;
	}
	
	
	/**
	 * 根据名字 获取部门相关信息
	 * @param departName
	 * @param cycleDate
	 * @return
	 */
	public Map<String,Object> getDepartInfo(String departName,String cycleDate){
		return userGrantDao.getDepartInfo(CacheData.getAccessCode(),departName, cycleDate);
	}
	
}
