package edu.hfu.scre.entity;

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
 * 学术讲座
 * @author iceheartboy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames= {"academicTopic"})})//唯一约束，academicTopic不能重复，如果重复会引发异常报错
public class RptAcademic extends RptBase{
	private Integer academicId;
	private String academicTopic;//报告主题
	private String academicDate;//主讲日期
	private String academicMemo;
	private List<RptAcademicMaterial> materials;
	private Integer fileCount;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	public Integer getAcademicId() {
		return academicId;
	}
	public void setAcademicId(Integer academicId) {
		this.academicId = academicId;
	}
	@Column(length = 200,nullable=false)
	public String getAcademicTopic() {
		return academicTopic;
	}
	public void setAcademicTopic(String academicTopic) {
		this.academicTopic = academicTopic;
	}
	public String getAcademicDate() {
		return academicDate;
	}
	public void setAcademicDate(String academicDate) {
		this.academicDate = academicDate;
	}
	
	@Column(length = 500)
	public String getAcademicMemo() {
		return academicMemo;
	}
	public void setAcademicMemo(String academicMemo) {
		this.academicMemo = academicMemo;
	}
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="academic",fetch=FetchType.LAZY) 
	@OrderBy("ordernum ASC")
	public List<RptAcademicMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptAcademicMaterial> materials) {
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
