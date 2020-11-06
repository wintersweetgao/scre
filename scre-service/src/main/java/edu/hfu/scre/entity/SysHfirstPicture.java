package edu.hfu.scre.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 首页滚动图
 * */
@Entity
public class SysHfirstPicture {
	
	private Integer hfirstPictureId;// ID
	private String  hfirstPicturePath;//路径
	  
	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略
    public Integer getHfirstPictureId() {
		return hfirstPictureId;
	}
	public void setHfirstPictureId(Integer hfirstPictureId) {
		this.hfirstPictureId = hfirstPictureId;
	}
	@Column(length = 200)
	public String getHfirstPicturePath() {
		return hfirstPicturePath;
	}
	public void setHfirstPicturePath(String hfirstPicturePath) {
		this.hfirstPicturePath = hfirstPicturePath;
	}
}