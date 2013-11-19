/**  
 * @Filename:    RoleAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午03:37:56  
 *
 */  

package com.org.dqgb.action;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.service.RoleService;
import com.org.dqgb.service.UserService;

@Controller("RoleAction") @Scope("prototype")
@SuppressWarnings("serial")
public class RoleAction extends PrimaryAction {
	
	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	
	@Resource @Qualifier("RoleServiceImpl")
	public RoleService roleService;
	/**
	 *
	 * @TODO	获取用户---角色对应关系表
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getUser_Role(){
		try{
			JSONArray arr = this.userService.getUser_Role(Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")), 
						Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, arr);
			output.put(Constant.TOTAL_SIZE, userService.getUserCount());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到分配角色界面 
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoAddRolePage(){
		String userName = getHttpServletRequest().getParameter("userName");
		getHttpSession().setAttribute("modifiedUserName", userName);
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取系统现有角色数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getSystemRole(){
		try{
			List<Dictionary> list = this.roleService.getSystemRoles(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, list);
			output.put(Constant.TOTAL_SIZE, list.size());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取个人角色数据 
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getPersonalRole(){
		try{
			String userName = getHttpServletRequest().getParameter("userName");
			JSONArray arr =  this.roleService.getPersonalRoles(userName);
			output.put(Constant.DATA, arr);
			output.put(Constant.TOTAL_SIZE, arr == null ? 0 : arr.size());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	给用户分配角色
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addRole(){
		try{
			String id = getHttpServletRequest().getParameter("id");
			String userName = getHttpServletRequest().getParameter("userName");
			if("".equals(id) || null == id){
				this.roleService.addRole(userName,null);
			}else{
				this.roleService.addRole(userName, id.split(","));
			}
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
}
