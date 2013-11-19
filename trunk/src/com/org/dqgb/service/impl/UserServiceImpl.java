/**  
 * @Filename:    UserServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-11 下午09:57:47  
 *
 */  

package com.org.dqgb.service.impl;

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
import com.org.dqgb.entity.Role;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.UserService;

import fi.iki.santtu.md5.MD5;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
	
	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;

	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	判断用户合法与否
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	@param user
	 * @return  boolean		用户合法为true，非法返回false
	 *
	 */
	@Override
	public boolean isValidUser(User user) throws ServiceException{
		if(null == user || user.getUserName() == null || "".equals(user.getUserName()) 	//验证帐号密码是否为空
				|| user.getPassword() == null || "".equals(user.getPassword())){
			throw new ServiceException("帐号信息不能为空，请确认");
		}
		User us = this.getUserByName(user.getUserName());
		if(us == null){
			throw new ServiceException("帐号信息有误，请确认");
		}
		if(Constant.INVALID.equals(us.getFlag())){
			throw new ServiceException("帐号已删除，无法再使用");
		}
		if(!us.getPassword().equals(getMD5Password(user.getPassword()))){	//非系统用户
			throw new ServiceException("密码信息有误，请确认");
		}
		return true;
	}

	/**
	 *
	 * @TODO	把密码经过MD5算法加密
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	password，返回加密后的密码
	 * @return  String
	 *
	 */
	@Override
	public String getMD5Password(String password) {
		MD5 md5 = new MD5();
		md5.Init();
		md5.Update(password);
		return md5.asHex();
	}

	/**
	 *
	 * @TODO	通过帐号获取用户信息,只查找合法用户(flag = 1)	
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	userName帐号
	 * @return  User
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public User getUserByName(String userName) throws ServiceException{
		List<User> list = null;
		try{
			list = (List<User>) this.hibernateDao.getObjectsList("select o from " + User.class.getName() + 
					" o where o.userName = '" + userName + "'", 0, 10, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	/**
	 *
	 * @TODO	添加用户,添加前会将密码进行MD5加密
	 * @date	2011-3-11
	 * @author	肖乾斌
	 * @param	user
	 * @param	@throws ServiceException
	 * @return  boolean
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addUser(User user) throws ServiceException {
		if(user == null || user.getUserName() == null || "".equals(user.getUserName())){
			throw new ServiceException("用户信息无效!");
		}
		if(null != this.getUserByName(user.getUserName())){
			throw new ServiceException("用户已存在!");
		}
		try{
			user.setPassword(getMD5Password(Constant.DEFAULT_PASSWORD));		//增加默认密码
			user.setFlag(Constant.VALID);										//设置用户状态为有效
			this.hibernateDao.save(user);
			//==给用户创建默认角色===
			Role role = new Role();
			role.setRoleId(Constant.USER);
			role.setUserName(user.getUserName());
			this.hibernateDao.save(role);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	修改密码
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	@param user
	 * @param	@throws ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean modifyPassword(User user) throws ServiceException {
		try{
			this.hibernateDao.saveOrUpdate(user);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public List<User> getUsers(int pageIndex, int countPerPage)throws ServiceException {
		List<User> list = null;
		try{
			list = (List<User>) this.hibernateDao.getObjectsList("select o from " + User.class.getName() + 
					" o where o.flag = '" + Constant.VALID + "'", pageIndex, countPerPage, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}
	
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getSystemUser(int pageIndex, int countPerPage)
			throws ServiceException {
		List<User> list = null;
		JSONArray arr = null;
		try{
			list = (List<User>) this.hibernateDao.getObjectsList("select o from " + User.class.getName() + 
					" o where o.flag = '" + Constant.VALID + "'", pageIndex, countPerPage, true);
			arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put(Constant.DEPARTMENT_NAME,
						organizationService.getOrganizationById(list.get(i).getDepartmentId()).getDepartmentName());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return arr;
	}

	/**
	 *
	 * @TODO	获取系统用户总数
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getUserCount() throws ServiceException {
		int count = 0;
		try{
			count = this.hibernateDao.getCount("select o from " + User.class.getName() + 
					" o where o.flag = '" + Constant.VALID + "'", true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return count;
	}
	
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
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteUserByName(String userName) throws ServiceException{
		try{
			this.hibernateDao.executeNativeSql("DELETE FROM T_USER WHERE USERNAME = '" + userName + "'");
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}
	
	/**
	 *
	 * @TODO	假删除用户，设置用户标识为无效
	 * @date	2011-3-13
	 * @author	肖乾斌
	 * @param	@param user
	 * @param	@throws ServiceException
	 * @return  boolean
	 *
	 */
	public boolean invalidateUser(User user)throws ServiceException{
		if(user == null || user.getUserName() == null || "".equals(user.getUserName())){
			throw new ServiceException("用户信息无效!");
		}
		if(Constant.SYSTEM_ADMIN.equals(user.getUserName())){
			throw new ServiceException("不能删除系统超级用户!");
		}
		try{
			User us = this.getUserByName(user.getUserName());
			if(null != us){
				user.setFlag(Constant.INVALID);
				this.hibernateDao.saveOrUpdate(user);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Override
	public boolean modifyUserInformation(User user) throws ServiceException {
		if(user == null || user.getUserName() == null || "".equals(user.getUserName())){
			throw new ServiceException("用户信息无效!");
		}
		try{
			User us = this.getUserByName(user.getUserName());
			us.setAge(user.getAge());
			us.setGender(user.getGender());
			us.setIdCardNumber(user.getIdCardNumber());
			us.setRealName(user.getRealName());
			us.setDepartmentId(user.getDepartmentId());
			us.setQq(user.getQq());
			this.hibernateDao.saveOrUpdate(us);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getUser_Role(int pageIndex, int pageSize)
			throws ServiceException {
		JSONArray arr = null;
		try{
			List<User> list = this.getUsers(pageIndex, pageSize);
			arr = this.getSystemUser(pageIndex, pageSize);
			for(int i = 0; i < list.size(); i++){
				User user = list.get(i);
				List<Object> l = (List<Object>)this.hibernateDao.getObjectsByNativeSql(
						"SELECT MAX(ROLE_ID) FROM T_ROLE WHERE USERNAME = '" + user.getUserName() + "'");
				if(l == null || l.size() == 0 || l.get(0) == null){								//没有找到任何角色
					arr.getJSONObject(i).put(Constant.ROLE, "尚未分配角色");
					arr.getJSONObject(i).put(Constant.ROLE_ID, 0);
				}else{
					Object o = l.get(0);
					arr.getJSONObject(i).put(Constant.ROLE, dictionaryService.getDictionaryById((Integer)o).getRemark());
					arr.getJSONObject(i).put(Constant.ROLE_ID, dictionaryService.getDictionaryById((Integer)o).getId());
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return arr;
	}
}
