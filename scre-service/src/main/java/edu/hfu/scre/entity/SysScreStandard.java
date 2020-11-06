package edu.hfu.scre.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *    科研达标项目的评分标准
 * @author iceheartboy
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"screType","screTopic","belongCycle"})})
public class SysScreStandard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	private Integer standardId;
	@Column(length = 50)
	private String screType;//科研达标类型（科研课题、科研论文等）
	@Column(length = 100)
	private String screTopic;//国家级项目、核心期刊等
	private Integer validNum;//有效人数
	private Integer screMark;//科研项目类型标准分值
	@Column(precision = 3)
	private Float weight1; //第一人权重系数 
	@Column(precision = 3)
	private Float weight2; //第二人权重系数
	@Column(precision = 3)
	private Float weight3; //第三一人权重系数
	@Column(precision = 3)
	private Float weight4; //第四一人权重系数
	@Column(precision = 3)
	private Float weight5; //第五一人权重系数
	
	@Column(length = 50) 
	protected String belongCycle;
}
