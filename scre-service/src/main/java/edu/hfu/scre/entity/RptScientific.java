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
import javax.persistence.Transient;import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 科研课题
 */
@SuppressWarnings("serial")
@Entity //实体类
@Table(uniqueConstraints = {@UniqueConstraint(columnNames= {"scieName","userCode","inOrder"})})//唯一约束，scieName不能重复，如果重复会引发异常报错
public class RptScientific extends RptBase{
	private Integer scieId;//项目Id
	private String scieName;//项目名称
	private String scieDepart;//项目审批部门
	private String scieLeader;//项目负责人
	private Date scieStartDate;//立项时间
	private Date scieCloseDate;//结题时间
	private String scieOk; //是否结题	
	private List<RptScientificMaterial> materials;
	private Integer fileCount;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	public Integer getScieId() {
		return scieId;
	}
	
	public void setScieId(Integer scieId) {
		this.scieId = scieId;
	}
	
	@Column(length = 100)
	public String getScieName() {
		return scieName;
	}
	public void setScieName(String scieName) {
		this.scieName = scieName;
	}
	@Column(length = 100)
	public String getScieDepart() {
		return scieDepart;
	}
	public void setScieDepart(String scieDepart) {
		this.scieDepart = scieDepart;
	}
	@Column(length = 100)
	public String getScieLeader() {
		return scieLeader;
	}
	public void setScieLeader(String scieLeader) {
		this.scieLeader = scieLeader;
	}

	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	public Date getScieStartDate() {
		return scieStartDate;
	}
	public void setScieStartDate(Date scieStartDate) {
		this.scieStartDate = scieStartDate;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	public Date getScieCloseDate() {
		return scieCloseDate;
	}
	public void setScieCloseDate(Date scieCloseDate) {
		this.scieCloseDate = scieCloseDate;
	}
	@Column(length = 100)
	public String getScieOk() {
		return scieOk;
	}
	public void setScieOk(String scieOk) {
		this.scieOk = scieOk;
	}
	
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="scientific",fetch=FetchType.LAZY) 
	@OrderBy("ordernum ASC")
	public List<RptScientificMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptScientificMaterial> materials) {
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
