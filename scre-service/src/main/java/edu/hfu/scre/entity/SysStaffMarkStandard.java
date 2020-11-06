package edu.hfu.scre.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * 职称积分标准表
 * @author iceheartboy
 *
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"staffTitle","belongCycle"})})
public class SysStaffMarkStandard {
	@Id  //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //主键生成策略
	private Integer standardId;
	@Column(length = 50)
	private String staffTitle;//员工职称
	private Integer markStandard; //积分标准
	@Column(length = 50) 
	protected String belongCycle;
	
}
