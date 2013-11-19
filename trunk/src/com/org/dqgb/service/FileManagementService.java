/**  
 * @Filename:    FileManagementService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-7 下午04:42:03  
 *
 */  

package com.org.dqgb.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.FileManagement;
import com.org.dqgb.exception.ServiceException;

public interface FileManagementService {
	
	/**
	 *
	 * @TODO	添加文件管理信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	fm
	 * @throws  ServiceException
	 *
	 */
	public abstract void addFileInformation(FileManagement fm) throws ServiceException;
	
	
	/**
	 *
	 * @TODO	获取文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getFileInformation(int pageIndex, int pageSize) throws ServiceException;
	
	/**
	 * 根据车牌获取文件
	 * @author 	肖乾斌
	 * @date 	2011-12-25
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getFileInformation(int pageIndex, int pageSize,String carId) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	fm
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public abstract void modifyFileInformation(FileManagement fm) throws ServiceException;
	
	/**
	 *
	 * @TODO	删除文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	fm
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public abstract void deleteFileInformation(FileManagement fm) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据id获取文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  FileManagement
	 *
	 */
	public abstract FileManagement getFileInformationById(int id) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据车牌获取相关文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	carId
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getFileInformationByCarId(String carId,int pageIndex, int pageSize) throws ServiceException;
	
	
	/**
	 *
	 * @TODO	根据条件获取文件信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	condition
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getFileInformationByCondition(Map<String,Object> condition, 
			int pageIndex, int pageSize) throws ServiceException;
}
