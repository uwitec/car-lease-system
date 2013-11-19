/**  
 * @Filename:    DictionaryService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午03:30:07  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.exception.ServiceException;

public interface DictionaryService {
	/**
	 *
	 * @TODO	获取所有字典信息
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	public abstract List<Dictionary> getDictionaryInformation(int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取字典数据数量
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	public abstract int getDictionaryCount() throws ServiceException;

	/**
	 *
	 * @TODO	根据字典id号码获取字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  Dictionary
	 *
	 */
	public abstract Dictionary getDictionaryById(int id) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean modifyDictionaryInformation(Dictionary dic) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据分组id获取字典数据(角色信息的分组id为1)
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	groupId	分组id
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	public abstract List<Dictionary> getDictionaryByGroupId(int groupId,int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据分组id获取字典数据 json版本
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	groupId
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getDicByGroupId(int groupId,int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	在字典表的职位角色组中添加一个字典数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	roleName	字典的remark信息
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean createPositionRole(String roleName)throws ServiceException;
	
	/**
	 *
	 * @TODO	删除字典表中的职位字典数据，级联删除角色表中对应的角色信息
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	roleId
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean deletePositionRole(int roleId)throws ServiceException;
	
	
	/**
	 *
	 * @TODO	获取品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getBrandInformation(int pageIndex, int pageSize)throws ServiceException;
	
	/**
	 *
	 * @TODO	添加品牌数据信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean addBrand(Dictionary dic)throws ServiceException;
	
	/**
	 *
	 * @TODO	修改品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean modifyBrand(Dictionary dic)throws ServiceException;
	
	/**
	 *
	 * @TODO	删除品牌数据
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean deleteBrand(Dictionary dic)throws ServiceException;
	
	/**
	 *
	 * @TODO	获取租赁信息
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	public abstract List<Dictionary> getLeaseWay()throws ServiceException;
}
