package edu.hfu.scre.dao.review;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.RptPaper;

@Repository
public class BookDao {
	private static final Logger LOG = LoggerFactory.getLogger(BookDao.class);
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 保存
	 * @param book
	 * @return tanzhi
	 * @throws Exception
	 */
	public Integer savePaper(RptBook book)throws Exception {
		Integer id=(Integer) dao.save(book);
		return id;
	}
	/**
	 * 更新
	 * @param book
	 * @throws Exception
	 */
	public void updPaper(RptBook book)throws Exception {
		dao.update(book);
	}
}
