package edu.hfu.scre.service.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.sysset.StaffTopicStandDao;
import edu.hfu.scre.entity.SysStaffMarkStandard;
@Service
@Transactional
public class StaffTopicStandService {

	@Resource
	StaffTopicStandDao staffTopicStandDao;
	
	public List<SysStaffMarkStandard> queryStaffTopicStand(SysStaffMarkStandard stand,Integer pageNo,Integer maxResults) throws Exception{
		return staffTopicStandDao.queryStaffTopicStand(stand,pageNo,maxResults);
	}
	public Integer queryStaffTopicStandCount(SysStaffMarkStandard stand) throws Exception{
		return staffTopicStandDao.queryStaffTopicStandCount(stand);
	}
	/**
	 * 获取职称对应的积分标准
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> queryStaffTopicStand(String belongCycle)throws Exception{
		Map<String,Integer> map=new HashMap<>();
		SysStaffMarkStandard std=new SysStaffMarkStandard();
		std.setBelongCycle(belongCycle);
		List<SysStaffMarkStandard> ls=staffTopicStandDao.queryStaffTopicStand(std,1,100);
		for(SysStaffMarkStandard stand:ls) {
			map.put(stand.getStaffTitle(), stand.getMarkStandard());
		}
		return map;
	}
	
	public void saveStaffTopicStand(SysStaffMarkStandard staffTitle) throws Exception {
		staffTopicStandDao.saveStaffTopicStand(staffTitle);
	}
	public void delStaffTopicStand(Integer standardId) throws Exception {
		staffTopicStandDao.delStaffTopicStand(standardId);
	}
	public void updStaffTopicStand(SysStaffMarkStandard staffTitle) throws Exception {
		staffTopicStandDao.updStaffTopicStand(staffTitle);
	}
	/**
	 * 根据职称获取 达标标准
	 * @param staffTitle
	 * @return
	 * @throws Exception
	 */
	public SysStaffMarkStandard queryStaffTopicStand(String staffTitle,String belongCycle)throws Exception{
		SysStaffMarkStandard stand=new SysStaffMarkStandard();
		stand.setStaffTitle(staffTitle);
		stand.setBelongCycle(belongCycle);
		List<SysStaffMarkStandard> ls=this.queryStaffTopicStand(stand,1,100);
		if (null!=ls&&ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}
	
}
