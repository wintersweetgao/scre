package edu.hfu.scre.dao.review;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptPaper;

@Repository
public class PaperDao {
	private static final Logger LOG = LoggerFactory.getLogger(PaperDao.class);
	@Resource
	private BaseDaoImpl dao;
	
	/**
	 * 保存
	 * @param paper
	 * @return
	 * @throws Exception
	 */
	public Integer savePaper(RptPaper paper)throws Exception {
		Integer id=(Integer) dao.save(paper);
		return id;
	}
	/**
	 * 更新
	 * @param paper
	 * @throws Exception
	 */
	public void updPaper(RptPaper paper)throws Exception {
		dao.update(paper);
	}
}
