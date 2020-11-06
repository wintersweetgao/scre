package edu.hfu.scre.dao.sysset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysScreStandard;

@Repository
public class ScreStandardDao {
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 获取评分标准
	 * @param stand
	 * @return
	 * @throws Exception
	 */
	public Integer getPatentByNameCount(SysScreStandard stand) throws Exception {
		String hql="from SysScreStandard  ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if (null!=stand) {
			if (null!=stand.getScreType()&&!"".equals(stand.getScreType())) {
				hql+=(index==0?"where":"and")+" screType=?"+(index++);
				params.add(stand.getScreType());
			}
			
			if (null!=stand.getScreTopic()&&!"".equals(stand.getScreTopic())) {
				hql+=(index==0?"where":"and")+" screTopic=?"+(index++);
				params.add(stand.getScreTopic());
			}
			if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
				   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
				   params.add(stand.getBelongCycle());
			   }
		}
		System.out.println("hql:"+hql);
		return dao.queryBeanCountByHql(hql, params.toArray());
	}
	public List<SysScreStandard> queryScreStandard(SysScreStandard stand) throws Exception{
		String hql="from SysScreStandard ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=stand) {
			if (null!=stand.getScreType()&&!"".equals(stand.getScreType())) {
				hql+=(index==0?"where":"and")+" screType=?"+(index++);
				params.add(stand.getScreType());
			}
			
			if (null!=stand.getScreTopic()&&!"".equals(stand.getScreTopic())) {
				hql+=(index==0?"where":"and")+" screTopic=?"+(index++);
				params.add(stand.getScreTopic());
			}
			if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
				   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
				   params.add(stand.getBelongCycle());
			   }
		}
		hql+=" order by screType";
		System.out.println("hql:"+hql);
		return dao.find(hql, params.toArray());
	}
	public List<SysScreStandard> queryAllScreStandard(SysScreStandard stand,Integer pageNo,Integer maxResults) throws Exception{
		String hql="from SysScreStandard  ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=stand) {
			if (null!=stand.getScreType()&&!"".equals(stand.getScreType())) {
				hql+=(index==0?"where":"and")+" screType=?"+(index++);
				params.add(stand.getScreType());
			}
			
			if (null!=stand.getScreTopic()&&!"".equals(stand.getScreTopic())) {
				hql+=(index==0?"where":"and")+" screTopic=?"+(index++);
				params.add(stand.getScreTopic());
			}
			if (null!=stand.getBelongCycle()&&!"".equals(stand.getBelongCycle())) {
				   hql+=(index==0?"where":"and")+" belongCycle=?"+(index++);
				   params.add(stand.getBelongCycle());
			   }
		}
		
		hql+=" order by screType";
		System.out.println("hql:"+hql);
		return dao.findByPage(hql, params.toArray(),pageNo,maxResults);
	}
	public void saveScreStandard(SysScreStandard screStand) throws Exception {
		dao.save(screStand);
	}
	public void delScreStandard(Integer standardId) throws Exception {
		String hql="delete from SysScreStandard where standardId=?0";
		dao.bulkUpdate(hql, standardId);
	}
	public void updScreStandard(SysScreStandard screStand) throws Exception {
		dao.update(screStand);
	}
}
