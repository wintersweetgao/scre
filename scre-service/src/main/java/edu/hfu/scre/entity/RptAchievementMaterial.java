package edu.hfu.scre.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 科研获奖
 * @author asus
 *
 */
@Entity
public class RptAchievementMaterial {
	private Integer materialId;
	private String materialTitle;
	private String materialPath;
	private Integer ordernum;
	private Date createDate;
	private RptAchievement achievement;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	@Column(length=100,nullable = false)
	public String getMaterialTitle() {
		return materialTitle;
	}
	public void setMaterialTitle(String materialTitle) {
		this.materialTitle = materialTitle;
	}
	@Column(length=100,nullable = false)
	public String getMaterialPath() {
		return materialPath;
	}
	public void setMaterialPath(String materialPath) {
		this.materialPath = materialPath;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	@ManyToOne(cascade = {CascadeType.REFRESH }, optional = true,fetch=FetchType.LAZY)    
	@JoinColumn(name="achievementId")//这里设置JoinColum设置了外键的名字，并且orderItem是关系维护端
	@JSONField(serialize = false)
	public RptAchievement getAchievement() {
		return achievement;
	}
	public void setAchievement(RptAchievement achievement) {
		this.achievement = achievement;
	}
	
}
