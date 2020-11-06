package edu.hfu.scre.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * 科研获奖
 * @author iceheartboy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames= {"achievementName"})})//唯一约束，achievementName不能重复，如果重复会引发异常报错
public class RptAchievement  extends RptBase{
	private Integer achievementId;
	private String achievementName;//成果名称
	private String achievementType;//成果类型（论文、课题、作品、其它）
	private String awardName;//获奖名称
	private String awardDepart;//颁奖部门
	private String firstPerson;//第一完成人
	private Date publishDate;//获奖时间
	private List<RptAchievementMaterial>  materials;
	private Integer fileCount;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	public Integer getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(Integer achievementId) {
		this.achievementId = achievementId;
	}
	@Column(length = 200,nullable=false)
	public String getAchievementName() {
		return achievementName;
	}
	public void setAchievementName(String achievementName) {
		this.achievementName = achievementName;
	}
	@Column(length = 50,nullable=false)
	public String getAchievementType() {
		return achievementType;
	}
	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
	}
	@Column(length = 200,nullable=false)
	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	@Column(length = 100)
	public String getAwardDepart() {
		return awardDepart;
	}
	public void setAwardDepart(String awardDepart) {
		this.awardDepart = awardDepart;
	}
	@Column(length = 50)
	public String getFirstPerson() {
		return firstPerson;
	}
	public void setFirstPerson(String firstPerson) {
		this.firstPerson = firstPerson;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="achievement",fetch=FetchType.LAZY)
	@OrderBy("ordernum ASC")
	public List<RptAchievementMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptAchievementMaterial> materials) {
		this.materials = materials;
	}
	@Transient
	public Integer getFileCount() {
		if (null!=this.getMaterials()&this.getMaterials().size()>0) {
			fileCount= getMaterials().size();
		}else {
			fileCount= 0;
		}
		return fileCount;
	}
	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}
	
	
}
