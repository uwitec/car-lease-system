/**  
 * @Filename:    Dictionary.java  
 * @TODO:		  系统字典
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午03:17:51  
 *
 */  

package com.org.dqgb.entity;

public class Dictionary {
	public int id;					//主键id	非自增长的
	public int groupId;				//主键id的属组id(例如"角色组")
	public String remark;			//id的注释
	public String groupRemark;		//id的注释
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupRemark() {
		return groupRemark;
	}
	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}
}
