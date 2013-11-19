/**  
 * @Filename:    RoleService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午11:26:30  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.exception.ServiceException;

public interface RoleService {
	
	/**
	 *
	 * @TODO	根据用户帐号获取用户的所有角色ID
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	userName
	 * @throws  ServiceException
	 * @return  List<Integer>
	 *
	 */
	public abstract List<Integer> getPersonalRolesID(String userName) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据用户帐号获取用户的所有角色
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	userName
	 * @throws  ServiceException
	 * @return  List<Role>
	 *
	 */
	public abstract JSONArray getPersonalRoles(String userName) throws ServiceException;
	/**
	 *
	 * @TODO	判断当前用户是否拥有角色id
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	roleId		角色id
	 * @param	userName	用户
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean hasRole(String userName,int roleId) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取所有的系统角色和职位角色信息
	 * @date	2011-3-14
	 * @param	pageIndex			页码
	 * @param	pageSize			每页显示的数据个数
	 * @throws  ServiceException
	 * @return  List<Object>
	 *
	 */
	public abstract List<Dictionary> getSystemRoles(int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	给用户添加角色，不会重复添加
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	userName		需要添加角色的用户
	 * @param	roleId			角色id数组
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean addRole(String userName,String[] roleId) throws ServiceException;
}
