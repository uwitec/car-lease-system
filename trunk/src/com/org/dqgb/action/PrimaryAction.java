/**  
 * @Filename:    PrimaryAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-16 下午09:34:58  
 *
 */  

package com.org.dqgb.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.dqgb.common.Constant;
import com.org.dqgb.common.GlobalUser;
import com.org.dqgb.entity.User;

@SuppressWarnings("serial")
public class PrimaryAction extends ActionSupport{
	
	public Map<String,Object> output = new HashMap<String, Object>();			//jquery ajax返回值
	
	public Logger log = Logger.getLogger(this.getClass());
	
	public static String FAILED = "failed";
	/**
	 *
	 * @TODO	获取重复登录的帐号对应的session信息。
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @param	uniqueId
	 * @return  userList对象中的key值,""表示没有找到
	 *
	 */
	private String getRepeatedUser(String uniqueId){
		Map<String,Object> userList = this.getUserList();
		Iterator<String> it = userList.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			GlobalUser globalUser = (GlobalUser) userList.get(key);
			if(uniqueId.equals(globalUser.getUniqueID())){
				return key;
			}
		}
		return "";
	}
	
	/**
	 *
	 * @TODO	向application对象中写入当前用户信息
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @param	userEntity		user对象
	 * @param	uniqueId		用来比较两个对象是否相等的依据
	 * @return  boolean
	 *
	 */
	@SuppressWarnings("unchecked")
	public boolean saveUser(Object userEntity,String uniqueId){
		Map<String,Object> userList = (Map<String, Object>) this.getGlobalApplication()
										.get(Constant.USER_LIST);
		//初始化用户列表
		if(null == userList){							
			userList = new HashMap<String, Object>();
			this.getGlobalApplication().put(Constant.USER_LIST, userList);
		}
		String key = this.getRepeatedUser(uniqueId);
		if(!"".equals(key)){//重复登录
			GlobalUser globalUser = (GlobalUser) userList.get(key);
			if(!getHttpSession().getId().equals(globalUser.getSession().getId())){
				globalUser.getSession().invalidate();				//注销原来的session,session监听器会删除userList对象中的数据
			}
		}
		GlobalUser globalUser = new GlobalUser();
		globalUser.setSession(getHttpSession());
		globalUser.setUniqueID(uniqueId);
		globalUser.setRemoteIP(getCurrentUserIP());				//保存用户ip信息
		globalUser.setUser(userEntity);
		userList.put(getHttpSession().getId(), globalUser);
		return true;
	}
	
	/**
	 *
	 * @TODO	获取当前登录系统的用户列表
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @return  Map<String,Object>
	 *
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getUserList(){
		return this.getGlobalApplication().get(Constant.USER_LIST) == null ? 
				null : (Map<String, Object>) this.getGlobalApplication().get(Constant.USER_LIST);
	}
	
	/**
	 *
	 * @TODO	获取当前系统在线人数
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @return  int
	 *
	 */
	@SuppressWarnings("unchecked")
	public int getCurrentUserCount(){
		Map<String, Object> map = (Map<String, Object>) this.getGlobalApplication().get(Constant.USER_LIST);
		return map == null ? 0 : map.size();
	}
	/**
	 *
	 * @TODO	从application对象中删除指定用户信息
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @param	@param uniqueId
	 * @return  boolean
	 *
	 */
	@SuppressWarnings("unchecked")
	public boolean removeUser(String uniqueId){
		if(null == uniqueId || "".equals(uniqueId))
			return true;
		Map<String,Object> userList = (Map<String, Object>) this.getGlobalApplication()
										.get(Constant.USER_LIST);
		if(null == userList)
			return true;
		userList.remove(uniqueId);
		return true;
	}
	
	/**
	 *
	 * @TODO	退出系统
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @param	@param uniqueId
	 * @return  boolean
	 *
	 */
	public boolean logoutSystem(){
		getHttpSession().invalidate();
		return true;				//this.removeUser(uniqueId);	session监听器会删除userList对象中的数据
	}

	/**
	 *
	 * @TODO	登录系统
	 * @date	2011-2-18
	 * @author	肖乾斌
	 * @param	uniqueId
	 * @param	userEntity
	 * @return  boolean
	 *
	 */
	public boolean loginSystem(Object userEntity,String uniqueId){
		if(null == userEntity || "".equals(userEntity))
			return false;								//登录失败
		boolean result = saveUser(userEntity,uniqueId);
		this.getHttpSession().setAttribute(Constant.CURRENT_USER, userEntity);
		return result;
	}
	/**
	 *
	 * @TODO	获取全局的application对象
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getGlobalApplication(){
		return ServletActionContext.getContext().getApplication();
	}
	
	/**
	 *
	 * @TODO	获取时间
	 * @date	2011-3-7
	 * @author	肖乾斌
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getTime(){
		Calendar cal = Calendar.getInstance();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("YEAR", cal.get(Calendar.YEAR));
		map.put("MONTH", cal.get(Calendar.MONTH) + 1);
		map.put("DATE", cal.get(Calendar.DAY_OF_MONTH));
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
		String weekDay = "";
		switch(day){
			case 0:
				weekDay = "日";
				break;
			case 1:
				weekDay = "一";
				break;
			case 2:
				weekDay = "二";
				break;
			case 3:
				weekDay = "三";
				break;
			case 4:
				weekDay = "四";
				break;
			case 5:
				weekDay = "五";
				break;
			case 6:
				weekDay = "六";
				break;
			default:
				break;
		}
		map.put("DAY", weekDay);
		return map;
	}
	/**
	 *
	 * @TODO	获取http请求对象
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  HttpServletRequest
	 *
	 */
	public HttpServletRequest getHttpServletRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 *
	 * @TODO	获取http响应对象
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  HttpServletResponse
	 *
	 */
	public HttpServletResponse getHttpServletResponse(){
		return ServletActionContext.getResponse();
	}
	
	/**
	 *
	 * @TODO	获取http session
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  HttpSession
	 *
	 */
	public HttpSession getHttpSession() {
		return this.getHttpServletRequest().getSession();
	}
	
	/**
	 *
	 * @TODO	获取ActionContext
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  ActionContext
	 *
	 */
	public ActionContext getActionContext() {
		return ActionContext.getContext();
	}
	
	/**
	 *
	 * @TODO	获取session的Map对象
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getSession() {
		return this.getActionContext().getSession();
	}
	
	/**
	 *
	 * @TODO	获取当前用户的IP地址
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCurrentUserIP(){
		return this.getHttpServletRequest().getRemoteAddr();
	}
	
	/**
	 *
	 * @TODO	获取随机验证码
	 * @date	2011-3-10
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getValidationCode() {

		// 在内存中创建图象
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		// 设定背景色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);

		// 画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生的认证码(4位数字)
		String rand = "" + (Math.random() * 10000);
		rand = rand.substring(0, rand.indexOf("."));
		switch (rand.length()) {
		case 1:
			rand = "000" + rand;
			break;
		case 2:
			rand = "00" + rand;
			break;
		case 3:
			rand = "0" + rand;
			break;
		default:
			rand = rand.substring(0, 4);
			break;
		}

		HttpSession session = getHttpSession();

		// 将认证码存入SESSION
		session.setAttribute(Constant.RANDOM_CODE, rand);

		// 将认证码显示到图象中
		g.setColor(Color.black);
		Integer tempNumber = new Integer(rand);
		String numberStr = tempNumber.toString();

		g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
		String Str = numberStr.substring(0, 1);
		g.drawString(Str, 8, 17);

		Str = numberStr.substring(1, 2);
		g.drawString(Str, 20, 15);
		Str = numberStr.substring(2, 3);
		g.drawString(Str, 35, 18);

		Str = numberStr.substring(3, 4);
		g.drawString(Str, 45, 15);

		// 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}

		// 图象生效
		g.dispose();

		// 输出图象到页面,在页面上调用 <img src="/yourPath/xxx.action" />
		try {
			ImageIO.write(image, "JPEG", getHttpServletResponse().getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 
	 * @TODO	获取当前登录用户
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @return 	User
	 */
	public User getCurrentUser(){
		return (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
	}
}
