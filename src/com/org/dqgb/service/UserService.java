/**  
 * @Filename:    UserService.java  
 * @TODO:		   用户服务类
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-11 下午09:43:31  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface UserService {
	
	/**
	 *
	 * @TODO	判断用户合法与否
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	user
	 * @return  boolean		用户合法为true，非法返回false
	 *
	 */
	public abstract boolean isValidUser(User user) throws ServiceException;
	
	/**
	 *
	 * @TODO	把密码经过MD5算法加密
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	password，返回加密后的密码
	 * @return  String
	 *
	 */
	public abstract String getMD5Password(String password);
	
	/**
	 *
	 * @TODO	通过帐号获取用户信息	,只查找合法用户(flag = 1)	
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	userName帐号
	 * @return  User
	 *
	 */
	public abstract User getUserByName(String userName) throws ServiceException;
	
	/**
	 *
	 * @TODO	添加用户,添加前会将密码进行MD5加密
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean addUser(User user) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改密码
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean modifyPassword(User user) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取系统用户信息
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	countPerPage
	 * @throws  ServiceException
	 * @return  List<User>
	 *
	 */
	public List<User> getUsers(int pageIndex, int countPerPage)throws ServiceException; 
	/**
	 *
	 * @TODO	获取系统用户信息
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	countPerPage
	 * @throws  ServiceException
	 * @return  JSONArray
	 *
	 */
	public abstract JSONArray getSystemUser(int pageIndex,int countPerPage) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取系统用户总数
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	public abstract int getUserCount() throws ServiceException;
	
	/**
	 *
	 * @TODO	根据用户名删除用户
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	userName	用户名
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean deleteUserByName(String userName) throws ServiceException;
	
	/**
	 *
	 * @TODO	假删除用户，设置用户标识为无效
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	user
	 * @param	@throws ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean invalidateUser(User user)throws ServiceException;

	
	/**
	 *
	 * @TODO	修改用户信息
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean modifyUserInformation(User user)throws ServiceException;
	
	
	/**
	 *
	 * @TODO	获取 用户--角色 数据，显示角色id值最小的那个角色的名字，所以实际分配时应对应职位最大的角色
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONArray
	 *
	 */
	public abstract JSONArray getUser_Role(int pageIndex,int pageSize) throws ServiceException;
}
