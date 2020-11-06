package edu.hfu.scre.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import edu.hfu.scre.util.CacheData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class RptBase implements java.io.Serializable{
	@Column(length = 100)
	protected String staffName;//姓名
	@Column(length = 100)
	protected String staffTitle;//职称
	@Column(length = 100)
	protected String staffParentDepart;//系部处
	@Column(length = 100)
	protected String staffDepart;//科室
	@Column(length = 10)
	protected String userCode;
	@Column(length = 20)
	protected String  status;//BApprove CApprove DApprove EApprove 
	protected String  statusValue;//BApprove CApprove DApprove EApprove 
	protected Date createTime;//创建时间
	protected Integer inOrder;//参与次序
	protected Integer expectedMark;//预计得分
	@Column(length = 50)
	protected String belongCycle;
	@Column(length = 50)
	protected String screType;//科研达标类型（科研课题、科研论文等）
	@Column(length = 100)
	protected String screTopic;//国家级项目、核心期刊等
	@Column(length = 10)
	protected String recommend;//是否推荐
	@Transient
	public String getStatusValue() {
		statusValue=CacheData.getDictValByKey(status, "审批状态");
		if (statusValue=="") {
			statusValue=status;
		}
		return statusValue;
	}
}
