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
import edu.hfu.scre.util.FormatUtil;
/**
 * 论文
 * @author iceheartboy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="paperThesis")})//唯一约束，paperThesis不能重复，如果重复会引发异常报错
public class RptPaper extends RptBase{
	private Integer paperId;   //序号
	private String paperThesis;   //论文题目
	private String paperJournal;   //发表期刊名称
	private String paperSponsor;  //主办单位
	private String paperDepart;   //主管部门
	private String paperEi;   //是否EI,SCI,SSCI,CPCI检索
	private Date publishDate;  //发表日期
	private List<RptPaperMaterial> materials;
	private Integer fileCount;
	
	
	@Id  //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //主键生成策略
	public Integer getPaperId() {
		return paperId;
	}
	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}
	@Column(length=100,nullable = false)
	public String getPaperThesis() {
		return paperThesis;
	}
	public void setPaperThesis(String paperThesis) {
		this.paperThesis = paperThesis;
	}
	@Column(length=100,nullable = false)
	public String getPaperJournal() {
		return paperJournal;
	}
	public void setPaperJournal(String paperJournal) {
		this.paperJournal = paperJournal;
	}
	@Column(length=100,nullable = false)
	public String getPaperSponsor() {
		return paperSponsor;
	}
	public void setPaperSponsor(String paperSponsor) {
		this.paperSponsor = paperSponsor;
	}
	@Column(length=100)
	public String getPaperDepart() {
		return paperDepart;
	}
	public void setPaperDepart(String paperDepart) {
		this.paperDepart = paperDepart;
	}
	@Column(length=10)
	public String getPaperEi() {
		return paperEi;
	}
	public void setPaperEi(String paperEi) {
		this.paperEi = paperEi;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="paper",fetch=FetchType.LAZY) 
	@OrderBy("ordernum ASC")
	public List<RptPaperMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptPaperMaterial> materials) {
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
