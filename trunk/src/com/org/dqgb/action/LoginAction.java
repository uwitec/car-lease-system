/**  
 * @Filename:    LoginAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-11 下午04:24:50  
 *
 */  

package com.org.dqgb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.common.GlobalUser;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.UserService;

@Controller("LoginAction") @Scope("prototype")
@SuppressWarnings("serial")
public class LoginAction extends PrimaryAction {
	
	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	
	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	public User user;
	/**
	 *
	 * @TODO	登录系统
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String login(){
		try{
			this.userService.isValidUser(user);
			getHttpSession().setAttribute("DATE",getTime());
			
			this.loginSystem(this.userService.getUserByName(user.getUserName()),user.getUserName());
			String url = this.getHttpServletRequest().getHeader("REFERER");
			String pages = url.substring(url.lastIndexOf("/") + 1, url.length());
			if(!"login.jsp".equals(pages) && !"login.action".equals(pages) && !"logout.action".equals(pages)){
				output.put("redirect", url);
			}
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		/*if(!userService.isValidUser(user)){
			return INPUT;
		}
		getHttpSession().setAttribute("DATE",getTime());
		
		this.loginSystem(this.userService.getUserByName(user.getUserName()),user.getUserName());
		String url = this.getHttpServletRequest().getHeader("REFERER");
		String pages = url.substring(url.lastIndexOf("/") + 1, url.length());
		if(!"login.jsp".equals(pages) && !"login.action".equals(pages) && !"logout.action".equals(pages)){
			try {
				ServletActionContext.getResponse().sendRedirect(url);
				return null;
			} catch (IOException e) {
			}
		}*/
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	退出系统
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String logout(){
		this.logoutSystem();
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	获取在线人员信息
	 * @date	2011-3-19
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getOnlineUser(){
		try{
			Map<String,Object> map = this.getUserList();
			Iterator<String> it = map.keySet().iterator();
			int pageIndex = Integer.parseInt(getHttpServletRequest().getParameter("pageIndex"));
			int pageSize  =	Integer.parseInt(getHttpServletRequest().getParameter("pageSize"));
			int i = 0;
			List<Map<String,Object>> usList = new ArrayList<Map<String,Object>>();
			while(it.hasNext()){
				if(i >= (pageIndex * pageSize + pageSize)){
					break;
				}
				if( i >= pageIndex * pageSize && i < (pageIndex * pageSize + pageSize)){
					GlobalUser gUser = (GlobalUser) map.get(it.next());
					User user = (User) gUser.getUser();
					Map<String,Object> um = new HashMap<String, Object>();
					um.put("userName", user.getUserName());
					um.put("loginIp", gUser.getRemoteIP());
					um.put("realName", user.getRealName());
					um.put("qq", user.getQq());
					um.put("gender", user.getGender());
					um.put("departmentName", organizationService.getOrganizationById(user.getDepartmentId()).getDepartmentName());
					usList.add(um);
				}else{
					it.next();
				}
				i++;
			}
			output.put(Constant.DATA, usList);
			output.put(Constant.TOTAL_SIZE, map.size());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	} 
	/**
	 *
	 * @TODO	修改密码
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyPassword(){
		String ops = getHttpServletRequest().getParameter("oldPassword");		//旧密码
		String nps1 = getHttpServletRequest().getParameter("newPassword1");		//新密码1
		String nps2 = getHttpServletRequest().getParameter("newPassword2");		//新密码2
		User user = (User) getHttpSession().getAttribute(Constant.CURRENT_USER);
		if(!user.getPassword().equals(this.userService.getMD5Password(ops))){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, "密码输入错误...");
		}else if(nps1 == null || nps2 == null || !nps1.equals(nps2)){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, "两次输入的密码不一致...");
		}else if(nps1.length() > 16 || nps1.length() < 6){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, "新密码长度应该在6-16位之间...");
		}else{
			user.setPassword(this.userService.getMD5Password(nps1));
			getHttpSession().setAttribute(Constant.CURRENT_USER, user);			//把用户新密码更新到session中
			try{
				this.userService.modifyPassword(user);
				output.put(Constant.RESULT, "密码修改成功...");
			}catch(Exception e){
				output.put(Constant.RESULT, Constant.FAILED);
				output.put(Constant.REASON, e.getMessage());
			}
		}
		return SUCCESS;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
