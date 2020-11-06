package edu.hfu.scre.entity;

/**
 * 权限 定义表
 * @author iceheartboy
 *
 */
public class AuthGrant{
	private String grantCode;
	private String grantName;
	private String parentGrantCode;
	public String getGrantCode() {
		return grantCode;
	}
	public void setGrantCode(String grantCode) {
		this.grantCode = grantCode;
	}
	public String getGrantName() {
		return grantName;
	}
	public void setGrantName(String grantName) {
		this.grantName = grantName;
	}
	public String getParentGrantCode() {
		return parentGrantCode;
	}
	public void setParentGrantCode(String parentGrantCode) {
		this.parentGrantCode = parentGrantCode;
	}
	
	
}
