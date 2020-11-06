package edu.hfu.scre.dao.review;

import javax.annotation.Resource;

import edu.hfu.scre.entity.RptAchievement;
import edu.hfu.scre.entity.RptPaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;

@Repository
public class AchievementDao {
	private static final Logger LOG = LoggerFactory.getLogger(AchievementDao.class);
	@Resource
	private BaseDaoImpl dao;
	/**
	 * 保存
	 * @param achievement
	 * @return
	 * @throws Exception
	 */
	public Integer saveAchievement(RptAchievement achievement)throws Exception {
		Integer id=(Integer) dao.save(achievement);
		return id;
	}
	/**
	 * 更新
	 * @param achievement
	 * @throws Exception
	 */
	public void updAchievement(RptAchievement achievement)throws Exception {
		dao.update(achievement);
	}
}
