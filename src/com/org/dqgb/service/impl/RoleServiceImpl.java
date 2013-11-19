/**  
 * @Filename:    RoleServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午11:28:54  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.entity.Role;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.RoleService;
import com.org.dqgb.service.UserService;

@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService {

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("DictionaryServiceImpl")					//按名称进行装配
	public DictionaryService dictionaryService;
	
	@Resource @Qualifier("UserServiceImpl")	
	public UserService userService;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	根据用户帐号获取用户的所有角色
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	userName
	 * @throws  ServiceException
	 * @return  List<Role>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getPersonalRolesID(String userName) throws  ServiceException{
		List<Integer> list = new ArrayList<Integer>();
		try{
			List<Role> rList = (List<Role>) this.hibernateDao.getObjectsList("select o from " + 
								Role.class.getName() + " o where o.userName = '" + userName + "'" , 0, 9999, true);
			for(int i = 0; rList != null && i < rList.size(); i++){
				list.add(rList.get(i).getRoleId());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}


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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasRole(String userName, int roleId) throws ServiceException {
		try{
			List<Role> list = (List<Role>) this.hibernateDao.getObjectsList("select o from " + 
								Role.class.getName() + " o where o.userName = '" + userName + "' " +
								"and o.roleId = '" + roleId + "'" , 0, 9999, true);
			if(list == null || list.size() == 0)
				return false;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}


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
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public List<Dictionary> getSystemRoles(int pageIndex,int pageSize) throws ServiceException {
		List<Dictionary> list = null;
		try{
			list = this.dictionaryService.getDictionaryByGroupId(Constant.GROUPID_ROLE, pageIndex, pageSize);
			List<Dictionary> list1 = this.dictionaryService.getDictionaryByGroupId(Constant.GROUPID_POSITION, pageIndex, pageSize);
			for(int i = 0; null != list1 && i < list1.size(); i++){
				if(list == null)
					list = new ArrayList<Dictionary>();
				list.add(list1.get(i));
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}


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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getPersonalRoles(String userName) throws ServiceException {
		JSONArray arr = null;
		List<Role> list = null;
		if("".equals(userName) || null == userName){
			throw new ServiceException("用户名不能为空!");
		}
		try{
			list = (List<Role>) this.hibernateDao.getObjectsList("select o1 from " + 
								Role.class.getName() + " o1," + Dictionary.class.getName() + 
								" o2 where o1.userName = '" + userName + "' and o1.roleId = o2.id" ,
								0, 9999, true);
			arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put(Constant.ROLE, dictionaryService.getDictionaryById(
						list.get(i).getRoleId()).getRemark());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return arr;
	}


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
	@Transactional (propagation=Propagation.REQUIRED)
	@Override
	public boolean addRole(String userName, String[] roleId)
			throws ServiceException {
		if(null == userName || "".equals(userName)){
			throw new ServiceException("用户名不能为空!");
		}
		if(userService.getUserByName(userName) == null){
			throw new ServiceException("用户不是合法用户!");
		}
		this.hibernateDao.executeNativeSql("delete from T_ROLE where USERNAME = '" + userName + "'");
		for(int i = 0; roleId != null && i < roleId.length; i++){
			if(null == dictionaryService.getDictionaryById(Integer.parseInt(roleId[i]))){
				throw new ServiceException("角色不存在!");				//字典表中没有相应的数据信息
			}
			try{
				Role role = new Role();
				role.setRoleId(Integer.parseInt(roleId[i]));
				role.setUserName(userName);
				this.hibernateDao.save(role);
			}catch(Exception e){
				log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
			}
		}
		return true;
	}
}
