package edu.hfu.scre.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SysScreCycle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
	private Integer cycleId;
	@Column(length = 20,nullable = false)
	private String cycleName;//周期名称
	private String beginDate;//开始年月日
	private String endDate;//结束年月日
	private String isOver;
	private Date createTime;
}
