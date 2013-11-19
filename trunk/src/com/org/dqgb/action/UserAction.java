/**  
 * @Filename:    UserAction.java  
 * @TODO:		   获取有关用户的一些请求
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-13 下午04:54:53  
 *
 */  

package com.org.dqgb.action;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.UserService;

@Controller("UserAction") @Scope("prototype")
@SuppressWarnings("serial")
public class UserAction extends PrimaryAction {
	
	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	
	/**
	 *
	 * @TODO	获取系统用户
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getSystemUser(){
		String pageIndex = this.getHttpServletRequest().getParameter("pageIndex");
		String pageSize = this.getHttpServletRequest().getParameter("pageSize");
		try{
			JSONArray arr = this.userService.getSystemUser(Integer.parseInt(pageIndex), 
					Integer.parseInt(pageSize));
			int count = this.userService.getUserCount();
			output.put(Constant.DATA, arr);
			output.put(Constant.TOTAL_SIZE, count);
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	从数据库中假删除用户数据
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String invalidUser(){
		try{
			String us = this.getHttpServletRequest().getParameter("user");
			JSONObject jo = JSONObject.fromObject(us);
			User user = (User) JSONObject.toBean(jo, User.class);
			this.userService.invalidateUser(user);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改数据库中的用户数据信息
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyUserInformation(){
		try{
			User user = new User();
			user.setAge(Integer.parseInt(("".equals(this.getHttpServletRequest().getParameter("age"))) ?
					"0" : this.getHttpServletRequest().getParameter("age")));
			user.setUserName(this.getHttpServletRequest().getParameter("userName"));
			user.setRealName(this.getHttpServletRequest().getParameter("realName"));
			user.setIdCardNumber(this.getHttpServletRequest().getParameter("idCardNumber"));
			user.setGender(this.getHttpServletRequest().getParameter("gender"));
			user.setQq(this.getHttpServletRequest().getParameter("qq"));
			user.setDepartmentId(Integer.parseInt(getHttpServletRequest().getParameter("departmentId")));
			this.userService.modifyUserInformation(user);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	跳转到用户修改界面
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyUserPage(){
		try{
			String userName = this.getHttpServletRequest().getParameter("userName");
			getHttpSession().setAttribute("SpecifiedUser", this.userService.getUserByName(userName));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	添加新用户
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addUser(){
		User user = new User();
		try{
			user.setAge(Integer.parseInt(("".equals(this.getHttpServletRequest().getParameter("age"))) ?
				"0" : this.getHttpServletRequest().getParameter("age")));
			user.setUserName(this.getHttpServletRequest().getParameter("userName"));
			user.setRealName(this.getHttpServletRequest().getParameter("realName"));
			user.setIdCardNumber(this.getHttpServletRequest().getParameter("idCardNumber"));
			user.setGender(this.getHttpServletRequest().getParameter("gender"));
			user.setQq(this.getHttpServletRequest().getParameter("qq"));
			user.setDepartmentId(Integer.parseInt(getHttpServletRequest().getParameter("departmentId")));
			
			this.userService.addUser(user);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
}
