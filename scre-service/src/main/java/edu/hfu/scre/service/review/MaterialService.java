package edu.hfu.scre.service.review;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import edu.hfu.scre.dao.review.MaterialDao;
import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.RptAcademicMaterial;
import edu.hfu.scre.entity.RptAchievement;
import edu.hfu.scre.entity.RptAchievementMaterial;
import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.RptBookMaterial;
import edu.hfu.scre.entity.RptPatent;
import edu.hfu.scre.entity.RptPatentMaterial;
import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.RptScientificMaterial;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.entity.RptPaperMaterial;

@Service
@Transactional
public class MaterialService {
	@Resource(name = "materialDao")
	MaterialDao materialDao;
	/**
	 * 
	 * @param fileMap
	 * @param materialType
	 * @param materialTitle
	 * @param ordernum
	 * @param screId
	 * @param materialId 材料id 新增的时候可能为null
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public   <T> List<T> saveMaterialByType(Map<String,String> fileMap,String tableName,String[] materialTitle,
			Integer[] ordernum,Integer id) throws Exception {
		
		switch (tableName) {
		case "RptScientific"://科研
		{
			RptScientific scie=materialDao.get(RptScientific.class, id);
			if (null!=scie.getMaterials()&&scie.getMaterials().size()>0) {
				for(RptScientificMaterial material:scie.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptScientificMaterial> scieMaterials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptScientificMaterial material=new RptScientificMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setScientific(scie);
					scieMaterials.add(material);
				}
			}
			scie.setMaterials(scieMaterials);
			materialDao.merge(scie);
			return (List<T>) scieMaterials;
		}
		case "RptBook": //著作
		{
			RptBook book=materialDao.get(RptBook.class, id);
			if (null!=book.getMaterials()&&book.getMaterials().size()>0) {
				for(RptBookMaterial material:book.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptBookMaterial> achMaterials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptBookMaterial material=new RptBookMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setBook(book);
					achMaterials.add(material);
				}
			}
			book.setMaterials(achMaterials);
			materialDao.merge(book);
			return (List<T>) achMaterials;
		}
		case "RptPaper"://论文
		{
			RptPaper paper=materialDao.get(RptPaper.class, id);
			if (null!=paper.getMaterials()&&paper.getMaterials().size()>0) {
				for(RptPaperMaterial material:paper.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptPaperMaterial> materials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptPaperMaterial material=new RptPaperMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setPaper(paper);
					materials.add(material);
				}
			}
			paper.setMaterials(materials);
			materialDao.merge(paper);
			return (List<T>) materials;	
		}
		case "RptPatent"://专利
		{
			RptPatent patent=materialDao.get(RptPatent.class, id);
			if (null!=patent.getMaterials()&&patent.getMaterials().size()>0) {
				for(RptPatentMaterial material:patent.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptPatentMaterial> patentMaterials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptPatentMaterial material=new RptPatentMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setPatent(patent);
					patentMaterials.add(material);
				}
			}
			patent.setMaterials(patentMaterials);
			materialDao.merge(patent);
			return (List<T>) patentMaterials;
		}
		case "RptAcademic"://讲座
		{
			RptAcademic academic=materialDao.get(RptAcademic.class, id);
			if (null!=academic.getMaterials()&&academic.getMaterials().size()>0) {
				for(RptAcademicMaterial material:academic.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptAcademicMaterial> acaMaterials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptAcademicMaterial material=new RptAcademicMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setAcademic(academic);
					acaMaterials.add(material);
				}
			}
			academic.setMaterials(acaMaterials);
			materialDao.merge(academic);
			return (List<T>) acaMaterials;
		}
		case "RptAchievement"://成果
		{
			RptAchievement achievement=materialDao.get(RptAchievement.class, id);
			if (null!=achievement.getMaterials()&&achievement.getMaterials().size()>0) {
				for(RptAchievementMaterial material:achievement.getMaterials()) {
					materialDao.delete(material);
				}
			}
			List<RptAchievementMaterial> achMaterials=new ArrayList<>();
			for (int i=0;i<materialTitle.length;i++) {
				if (null!=materialTitle[i]&&!"".equals(materialTitle[i])) {
					RptAchievementMaterial material=new RptAchievementMaterial();
					material.setCreateDate(new Date());
					material.setMaterialTitle(materialTitle[i]);
					material.setMaterialPath(fileMap.get(materialTitle[i]));
					material.setOrdernum(ordernum[i]);
					material.setAchievement(achievement);
					achMaterials.add(material);
				}
			}
			achievement.setMaterials(achMaterials);
			materialDao.merge(achievement);
			return (List<T>) achMaterials;
		}
		default:
			throw new java.lang.RuntimeException("不识别的类型："+tableName);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getMaterialById(String className,String rptId)throws Exception{
		
		switch (className) {
		case "RptAcademic":
		{
			Integer academicId=Integer.valueOf(rptId.replace("RptAcademic", "")) ;
			return (List<T>) materialDao.getAcademicMaterialById(academicId);
			
		}
		case "RptAchievement":
		{
			Integer achievementId=Integer.valueOf(rptId.replace("RptAchievement", "")) ;
			return (List<T>) materialDao.getAchievementMaterialById(achievementId);
		}
		case "RptBook":
		{
			Integer bookID=Integer.valueOf(rptId.replace("RptBook", "")) ;
			return (List<T>) materialDao.getBookMaterialById(bookID);
		}
		case "RptPaper":
		{
			Integer paperId=Integer.valueOf(rptId.replace("RptPaper", "")) ;
			return (List<T>) materialDao.getPaperMaterialById(paperId);
		}
		case "RptPatent":
		{
			Integer patentId=Integer.valueOf(rptId.replace("RptPatent", "")) ;
			return (List<T>) materialDao.getPatentMaterialById(patentId);
		}
		case "RptScientific":
		{
			Integer scientificId=Integer.valueOf(rptId.replace("RptScientific", "")) ;
			return (List<T>) materialDao.getScientificMaterialById(scientificId);
		}
	default:
		throw new java.lang.RuntimeException("不识别的类型："+className);
		}
	}
}
