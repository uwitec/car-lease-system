/**  
 * @Filename:    OrganizationService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-15 下午10:39:04  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import com.org.dqgb.entity.Organization;
import com.org.dqgb.exception.ServiceException;

public interface OrganizationService {
	
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
	public abstract List<Organization> getSystemOrganization(int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取系统组织数量
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	public abstract int getOrganizationCount() throws ServiceException;
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
	public abstract Organization getOrganizationById(int id) throws ServiceException;
	
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
	public abstract boolean addOrganization(Organization org) throws ServiceException;
	
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
	public abstract boolean modifyOrganization(Organization org) throws ServiceException;
	
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
	public abstract boolean deleteOrganization(int id) throws ServiceException;
}
