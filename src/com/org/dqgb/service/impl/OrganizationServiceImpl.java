/**  
 * @Filename:    OrganizationServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-15 下午10:45:49  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Organization;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.OrganizationService;

@Service("OrganizationServiceImpl")
public class OrganizationServiceImpl implements OrganizationService {

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	获取系统组织
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @return  List<Organization>
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public List<Organization> getSystemOrganization(int pageIndex,int pageSize) throws ServiceException{
		List<Organization> list = null;
		try{
			list = (List<Organization>) this.hibernateDao.getObjectsList("select o from " + Organization.class.getName() + " o", pageIndex, pageSize, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}
	
	/**
	 *
	 * @TODO	获取系统组织数量
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int getOrganizationCount() throws ServiceException{
		int count = 0;
		try{
			count = this.hibernateDao.getCount("select o from " + 
					Organization.class.getName() + " o", true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return count;
	}
	/**
	 *
	 * @TODO	根据组织id获取组织信息
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	id
	 * @return  Organization
	 * @throws  ServiceException
	 * 
	 */
	public Organization getOrganizationById(int id) throws ServiceException{
		Organization org = null;
		try{
			org = (Organization) this.hibernateDao.getObjectByID(Organization.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return org;
	}
	
	/**
	 *
	 * @TODO	添加组织
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	org
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public boolean addOrganization(Organization org) throws ServiceException{
		if(org == null || org.getDepartmentName() == null || "".equals(org.getDepartmentName())){
			throw new ServiceException("添加组织数据失败！");
		}
		try{
			this.hibernateDao.save(org);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}
	
	/**
	 *
	 * @TODO	根据组织id删除组织数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	id				
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public boolean deleteOrganization(int id) throws ServiceException{
		try{
			this.hibernateDao.deleteObjectByKey(Organization.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	修改组织数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	org
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean modifyOrganization(Organization org) throws ServiceException {
		if(org == null){
			throw new ServiceException("组织数据不能为空！");
		}
		if(org.getDepartmentName() == null || "".equals(org.getDepartmentName())){
			throw new ServiceException("组织名不能为空！");
		}
		try{
			this.hibernateDao.saveOrUpdate(org);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

}
