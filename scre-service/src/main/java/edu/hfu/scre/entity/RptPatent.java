package edu.hfu.scre.entity;

import edu.hfu.scre.util.FormatUtil;

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
 * 专利成果实体类
 * @author ASUS
 *
 */
@SuppressWarnings("serial")
@Entity //实体类
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="patentName")})//唯一约束，bookName不能重复，如果重复会引发异常报错
public class RptPatent extends RptBase{
	private Integer patentId;//专利ID
	private String patentName;//专利名称
	private String patentNumber;//专利证号
	
	private String patentCompany;//专利鉴定单位
	private String patentPerson;//完成人
	private List<RptPatentMaterial> materials;
	private Integer fileCount;

	@Id  //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //主键生成策略
	public Integer getPatentId() {
		return patentId;
	}
	public void setPatentId(Integer patentId) {
		this.patentId = patentId;
	}
	
	@Column(length=200,nullable=false)
	public String getPatentName() {
		return patentName;
	}
	public void setPatentName(String patentName) {
		this.patentName = patentName;
	}
	@Column(length=200,nullable=false)
	public String getPatentNumber() {
		return patentNumber;
	}
	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}
	@Column(length=200,nullable=false)
	public String getPatentCompany() {
		return patentCompany;
	}
	public void setPatentCompany(String patentCompany) {
		this.patentCompany = patentCompany;
	}
	@Column(length=200,nullable=false)
	public String getPatentPerson() {
		return patentPerson;
	}
	public void setPatentPerson(String patentPerson) {
		this.patentPerson = patentPerson;
	}

	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="patent",fetch=FetchType.LAZY) 
	@OrderBy("ordernum ASC")
	public List<RptPatentMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptPatentMaterial> materials) {
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
