package edu.hfu.scre.entity;

import java.util.Date;
import java.util.List;

import edu.hfu.scre.util.FormatUtil;

/**
 * 员工定义
 */
public class SysStaff  {
	private Integer staffId;//员工ID
	private	String userCode;
	private String userPass;
	private String staffName;//员工姓名	
	private Integer departId;//科室iD
	private Integer parentDepartId;//父级部门科室iD
	private String poststr;//职务
	private String postId;
	private String staffTitle;//职称
	private String staffParentDepart;//系部处
	private String staffDepart;//科室
	private String staffEducation;//员工受教育程度
	private String staffNational;//员工民族
	private String staffSex;//员工性别
	private String staffPhone;//员工联系方式
	private String staffAddr;//员工家庭住
	private Date entryDate;//员工入职日期
	private Date staffBirthDay;//员工出生日期
	private List<AuthGrant> grants;//权限列表

	private String  entryDateStr;//员工入职日期字符串
	private String staffBirthDayStr;//员工出生日期字符串
	
	private String accessCode;
	
	

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Integer getDepartId() {
		return departId;
	}

	public void setDepartId(Integer departId) {
		this.departId = departId;
	}

	public Integer getParentDepartId() {
		return parentDepartId;
	}

	public void setParentDepartId(Integer parentDepartId) {
		this.parentDepartId = parentDepartId;
	}

	public String getPoststr() {
		return poststr;
	}

	public void setPoststr(String poststr) {
		this.poststr = poststr;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getStaffTitle() {
		return staffTitle;
	}

	public void setStaffTitle(String staffTitle) {
		this.staffTitle = staffTitle;
	}

	public String getStaffParentDepart() {
		return staffParentDepart;
	}

	public void setStaffParentDepart(String staffParentDepart) {
		this.staffParentDepart = staffParentDepart;
	}

	public String getStaffDepart() {
		return staffDepart;
	}

	public void setStaffDepart(String staffDepart) {
		this.staffDepart = staffDepart;
	}

	public String getStaffEducation() {
		return staffEducation;
	}

	public void setStaffEducation(String staffEducation) {
		this.staffEducation = staffEducation;
	}

	public String getStaffNational() {
		return staffNational;
	}

	public void setStaffNational(String staffNational) {
		this.staffNational = staffNational;
	}

	public String getStaffSex() {
		return staffSex;
	}

	public void setStaffSex(String staffSex) {
		this.staffSex = staffSex;
	}

	public String getStaffPhone() {
		return staffPhone;
	}

	public void setStaffPhone(String staffPhone) {
		this.staffPhone = staffPhone;
	}

	public String getStaffAddr() {
		return staffAddr;
	}

	public void setStaffAddr(String staffAddr) {
		this.staffAddr = staffAddr;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getStaffBirthDay() {
		return staffBirthDay;
	}

	public void setStaffBirthDay(Date staffBirthDay) {
		this.staffBirthDay = staffBirthDay;
	}

	public List<AuthGrant> getGrants() {
		return grants;
	}

	public void setGrants(List<AuthGrant> grants) {
		this.grants = grants;
	}

	public String getEntryDateStr() {
		if (null!=entryDate) {
			entryDateStr=FormatUtil.formatDateToStr(entryDate,"yyyy-MM-dd");
		}
		
		return entryDateStr;
	}

	public void setEntryDateStr(String entryDateStr) {
		this.entryDateStr = entryDateStr;
	}

	public String getStaffBirthDayStr() {
		if (null!=staffBirthDay) {
			staffBirthDayStr=FormatUtil.formatDateToStr(staffBirthDay,"yyyy-MM-dd");
		}
		
		return staffBirthDayStr;
	}

	public void setStaffBirthDayStr(String staffBirthDayStr) {
		this.staffBirthDayStr = staffBirthDayStr;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
	
}
