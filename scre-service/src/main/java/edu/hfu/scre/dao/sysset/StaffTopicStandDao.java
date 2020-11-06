package edu.hfu.scre.dao.sysset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysReviewLog;
import edu.hfu.scre.entity.SysStaffMarkStandard;
@Repository
public class StaffTopicStandDao {
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 参与达标教师的科研达标积分标准
	 * @param stand
	 * @return
	 * @throws Exception
	 */
	//查询
	public List<SysStaffMarkStandard> queryStaffTopicStand(SysStaffMarkStandard stand,Integer pageNo,Integer maxResults) throws Exception{
		String hql="from SysStaffMarkStandard ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=stand) {
			if (null!=stand.getStaffTitle()&&!"".equals(stand.getStaffTitle())) {
				hql+=(index==0?"where":"and")+" staffTitle=?"+(index++);
				params.add(stand.getStaffTitle());
		   }
		   if (null!=stand.getMarkStandard()) {
			   hql+=(index==0?"where":"and")+" markStandard=?"+(index++);
			   params.add(stand.getMarkStandard());
		   }
		   if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
			   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
			   params.add(stand.getBelongCycle());
		   }
		}
		  
		hql+=" order by belongCycle desc";
		return dao.findByPage(hql, params.toArray(), pageNo, maxResults);
	}
	
	public Integer queryStaffTopicStandCount(SysStaffMarkStandard stand) throws Exception{
		String hql="from SysStaffMarkStandard ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=stand) {
			if (null!=stand.getStaffTitle()&&!"".equals(stand.getStaffTitle())) {
				hql+=(index==0?"where":"and")+" staffTitle=?"+(index++);
				params.add(stand.getStaffTitle());
		   }
		   if (null!=stand.getMarkStandard()) {
			   hql+=(index==0?"where":"and")+" markStandard=?"+(index++);
			   params.add(stand.getMarkStandard());
		   }
		   if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
			   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
			   params.add(stand.getBelongCycle());
		   }
		}
		  
		return dao.queryBeanCountByHql(hql, params.toArray());
	}
	
	
	public List<SysStaffMarkStandard> queryStaffTopicStand(SysStaffMarkStandard stand) throws Exception{
		String hql="from SysStaffMarkStandard ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=stand) {
			if (null!=stand.getStaffTitle()&&!"".equals(stand.getStaffTitle())) {
				hql+=(index==0?"where":"and")+" staffTitle=?"+(index++);
				params.add(stand.getStaffTitle());
		   }
		   if (null!=stand.getMarkStandard()) {
			   hql+=(index==0?"where":"and")+" markStandard=?"+(index++);
			   params.add(stand.getMarkStandard());
		   }
		   if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
			   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
			   params.add(stand.getBelongCycle());
		   }
		}
		return dao.find(hql, params.toArray());
	}
	//增加
	public void saveStaffTopicStand(SysStaffMarkStandard staffTitle) throws Exception {
		dao.save(staffTitle);
	}
	//删除
	public void delStaffTopicStand(Integer standardId) throws Exception {
		String hql="delete from SysStaffMarkStandard where standardId=?0";
		dao.bulkUpdate(hql, standardId);
	}
	//修改
	public void updStaffTopicStand(SysStaffMarkStandard staffTitle) throws Exception {
		dao.update(staffTitle);
	}
}
