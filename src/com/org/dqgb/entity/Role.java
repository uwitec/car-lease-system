/**  
 * @Filename:    Role.java  
 * @TODO:		  角色数据
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午11:24:46  
 *
 */  

package com.org.dqgb.entity;

public class Role {
	public int id;							//主键id
	public int roleId;						//角色id
	public String userName;					//用户帐号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
