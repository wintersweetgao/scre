package edu.hfu.scre.dao.sysset;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysReviewLog;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.security.MyUserDetails;

@Repository
public class ReviewLogDao {
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 
	 * @param recordType 相当于菜单
	 * @param recordId 
	 * @param action
	 * @param memo
	 * @throws Exception
	 */
	public void addReviewLog(String recordType,Integer recordId,String action,String oldStatus,String newStatus,String memo) throws Exception {
		SysReviewLog lg=new SysReviewLog();
		lg.setReviewDateTime(new Date());
		lg.setRecordType(recordType);
		lg.setRecordId(recordId);
		lg.setAction(action);
		lg.setOldStatus(oldStatus);
		lg.setNewStatus(newStatus);
		lg.setMemo(memo);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth!=null) {
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				lg.setStaffName(user.getStaffName());
				lg.setUserCode(user.getUserCode());
			} 
		}else {
			lg.setStaffName("test");
			lg.setUserCode("test");
		}
		dao.save(lg);
	}
	/**
	 * 查询 某个科研项目的 审批记录
	 * @param recordType
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public List<SysReviewLog> findLog(String recordType,Integer recordId)throws Exception{
		String hql="from SysReviewLog where recordType=?0 and recordId=?1";
		return dao.find(hql, recordType,recordId);
	}
}
