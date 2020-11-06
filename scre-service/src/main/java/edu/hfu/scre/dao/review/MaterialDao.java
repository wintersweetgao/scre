package edu.hfu.scre.dao.review;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptAcademicMaterial;
import edu.hfu.scre.entity.RptAchievementMaterial;
import edu.hfu.scre.entity.RptBookMaterial;
import edu.hfu.scre.entity.RptPaperMaterial;
import edu.hfu.scre.entity.RptPatentMaterial;
import edu.hfu.scre.entity.RptScientificMaterial;

@Repository
public class MaterialDao {
	@Resource
	private BaseDaoImpl dao;
	public <T> T merge(T entity) throws Exception {
		return dao.merge(entity);
	}

	public <T> T get(Class<T> entityClass, Serializable id)
			throws Exception {		
		return dao.get(entityClass, id);
	}
	public <T> void delete(T entity) throws Exception {
		dao.delete(entity);
	}
	
	
	public List<RptAcademicMaterial> getAcademicMaterialById(Integer academicId) throws Exception {
		String hql="from RptAcademicMaterial o where o.academic.academicId=?0 ";
		return dao.find(hql,academicId);
	}
	public List<RptAchievementMaterial> getAchievementMaterialById(Integer achievementId) throws Exception {
		String hql="from RptAchievementMaterial o where o.achievement.achievementId=?0 ";
		return dao.find(hql,achievementId);
	}
	public List<RptBookMaterial> getBookMaterialById(Integer bookID) throws Exception {
		String hql="from RptBookMaterial o where o.book.bookID=?0 ";
		return dao.find(hql,bookID);
	}
	public List<RptPaperMaterial> getPaperMaterialById(Integer paperId) throws Exception {
		String hql="from RptPaperMaterial o where o.paper.paperId=?0 ";
		return dao.find(hql,paperId);
	}
	public List<RptPatentMaterial> getPatentMaterialById(Integer patentId) throws Exception {
		String hql="from RptPatentMaterial o where o.patent.patentId=?0 ";
		return dao.find(hql,patentId);
	}
	public List<RptScientificMaterial> getScientificMaterialById(Integer scientificId) throws Exception {
		String hql="from RptScientificMaterial o where o.scientific.scieId=?0 ";
		System.out.println("hql=="+hql);
		return dao.find(hql,scientificId);
	}
}
