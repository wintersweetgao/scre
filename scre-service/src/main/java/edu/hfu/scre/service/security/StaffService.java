package edu.hfu.scre.service.security;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import edu.hfu.scre.dao.security.UserGrantDao;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.util.CacheData;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.MD5Util;

@Service
public class StaffService {
	@Resource
	UserGrantDao userGrantDao;
	
	/**
	 * 修改密码
	 * @param userCode
	 * @param newPass
	 * @return
	 */
	public Integer updUserPassword(String userCode,String newPass) {
		newPass=MD5Util.string2MD5(newPass);
		return userGrantDao.updUserPassword(CacheData.getAccessCode(),userCode, newPass);
	}
	
	public String updUserByCode(SysStaff staff,String postIds) {
		String staffBirthDay=FormatUtil.formatDateToStr(staff.getStaffBirthDay(), "yyyy-MM-dd");
		String entryDate=FormatUtil.formatDateToStr(staff.getEntryDate(), "yyyy-MM-dd");
		System.out.println("staffBirthDay:"+staffBirthDay);
		String mess= userGrantDao.updUserByCode(CacheData.getAccessCode(),staff.getStaffId(), staff.getUserCode(),
				staff.getStaffName(), staffBirthDay, staff.getStaffNational(), 
				staff.getStaffEducation(), entryDate, staff.getStaffPhone(), 
				staff.getStaffAddr(), staff.getStaffSex(), staff.getStaffTitle(), 
				staff.getDepartId(),staff.getParentDepartId(), postIds);
		return mess;
	}
}
