package edu.hfu.scre.dao.review;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptAcademic;


@Repository
public class AcademicDao {
	@Resource
	private BaseDaoImpl dao;
	
	public List<RptAcademic> getAcademicByCon(RptAcademic academic,Integer pageNo, Integer maxResults)throws Exception{
		List<RptAcademic> ls=dao.queryBean(academic,pageNo,maxResults);
		return ls;
	}
	public int getAcademicCountByCon(RptAcademic academic)throws Exception{
		int res=dao.queryBeanCount(academic);
		return res;
	}
		/**
	 * 保存
	 * @param paper
	 * @return
	 * @throws Exception
	 */
	public Integer saveAcademic(RptAcademic academic)throws Exception {
		Integer id=(Integer) dao.save(academic);
		return id;
	}
	/**
	 * 更新
	 * @param paper
	 * @throws Exception
	 */
	public void updAcademic(RptAcademic academic)throws Exception {
		dao.update(academic);
	}
}
