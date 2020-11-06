package edu.hfu.scre.dao.sysset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysScreCycle;

@Repository
public class ScreCycleDao {
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 科研周期
	 * @param stand
	 * @return
	 * @throws Exception
	 */
	public List<SysScreCycle> queryScreCycle(SysScreCycle cycle) throws Exception{
		String hql="from SysScreCycle ";
		int index=0;
		List<Object> params=new ArrayList<>();
		if (null!=cycle) {
			if (null!=cycle.getCycleName()&&!"".equals(cycle.getCycleName())) {
				hql+=(index==0?"where":"and")+" screType=?"+(index++);
				params.add(cycle.getCycleName());
			}
		}
		hql+=" order by createTime desc";
		return dao.find(hql, params.toArray());
	}
	/**
	 * 获取最后一条记录
	 * @return
	 * @throws Exception
	 */
	public SysScreCycle getLastCycle()throws Exception{
		String hql="from SysScreCycle order by createTime desc";
		List<SysScreCycle> ls=dao.findByPage(hql, 1, 1);
		if (null!=ls&&ls.size()>0) {
			return ls.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 获取最近6条记录
	 * @return
	 * @throws Exception
	 */
	public List<SysScreCycle> getLastFiveCycle()throws Exception{
		String hql="from SysScreCycle order by createTime desc";
		List<SysScreCycle> ls=dao.findByPage(hql, 1, 5);
		return ls;
	}
	public void saveScreCycle(SysScreCycle cycle) throws Exception {
		dao.save(cycle);
	}
	public void updScreCycle(SysScreCycle cycle) throws Exception {
		dao.update(cycle);
	}
}
