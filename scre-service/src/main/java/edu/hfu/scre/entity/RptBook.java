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
 * 教材，著作成果统计表
 * @author asus
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames= {"bookName"})})//唯一约束，bookName不能重复，如果重复会引发异常报错
public class RptBook extends RptBase{
	
	private Integer bookID;
	private String bookName;//教材，著作名称
	private String firstEditor;//主编或第一主编
	private String press;//出版社
	private Date publishDate;//出版日期
	private List<RptBookMaterial> materials;
	private Integer fileCount;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	public Integer getBookID() {
		return bookID;
	}
	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}
	
	@Column(length = 200,nullable=false)
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	@Column(length = 100,nullable=false)
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	@Column(length = 50,nullable=false)
	public String getFirstEditor() {
		return firstEditor;
	}
	public void setFirstEditor(String firstEditor) {
		this.firstEditor = firstEditor;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="book",fetch=FetchType.LAZY) 
	@OrderBy("ordernum ASC")
	public List<RptBookMaterial> getMaterials() {
		return materials;
	}
	public void setMaterials(List<RptBookMaterial> materials) {
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
