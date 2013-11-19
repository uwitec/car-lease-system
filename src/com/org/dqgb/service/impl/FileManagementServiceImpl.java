/**  
 * @Filename:    FileManagementServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-7 下午04:49:03  
 *
 */  

package com.org.dqgb.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.FileManagement;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.AlarmService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.FileManagementService;

@Service("FileManagementServiceImpl")
public class FileManagementServiceImpl implements FileManagementService{

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("AlarmServiceImpl")
	public AlarmService alarmService;

	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	添加文件管理信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	fm
	 * @throws  ServiceException
	 *
	 */
	@Override
	public void addFileInformation(FileManagement fm) throws ServiceException {
		if(null == fm.getUsefulLife()){
			throw new ServiceException("保存文件信息失败，文件有效日期不能为空!");
		}
		if(null == fm.getCarId() || "".equals(fm.getCarId())){
			throw new ServiceException("保存文件信息失败，车牌号不能为空!");
		}
		try{
			this.hibernateDao.save(fm);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getFileInformation(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			String sql = "select o from " + FileManagement.class.getName() + " o";
			List<FileManagement> list = (List<FileManagement>) this.hibernateDao.getObjectsList(sql,
					pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("fileTypeRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getFileType()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Override
	public void modifyFileInformation(FileManagement fm)throws ServiceException {
		if(null == fm.getUsefulLife()){
			throw new ServiceException("保存文件信息失败，文件有效日期不能为空!");
		}
		if(null == fm.getCarId() || "".equals(fm.getCarId())){
			throw new ServiceException("保存文件信息失败，车牌号不能为空!");
		}
		try{
			this.hibernateDao.update(fm);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Override
	public void deleteFileInformation(FileManagement fm)
			throws ServiceException {
		try{
			this.hibernateDao.delete(fm);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Override
	public FileManagement getFileInformationById(int id)
			throws ServiceException {
		try{
			return (FileManagement) this.hibernateDao.getObjectByID(FileManagement.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getFileInformationByCarId(String carId, int pageIndex, int pageSize)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			String sql = "select o from " + FileManagement.class.getName() + " o where o.carId = '" + carId + "'";
			List<FileManagement> list = (List<FileManagement>) this.hibernateDao.getObjectsList(sql,
					pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("fileTypeRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getFileType()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Override
	public JSONObject getFileInformationByCondition(
			Map<String, Object> condition, int pageIndex, int pageSize)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 *
	 * @TODO	获取从当前时间开始推迟或提前days天所指的日期
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	isPre ? '提前':'置后'
	 * @param	days 	天数
	 * @return  String
	 *
	 */
	public String getPreOrNextDate(boolean isPre, long days){
		Date date = new Date();
		if(isPre){
			date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
		}else{
			date.setTime(date.getTime() - days * 24 * 60 * 60 * 1000);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d hh:mm:ss");
		return sdf.format(date);
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getFileInformation(int pageIndex, int pageSize,
			String carId) throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			String sql = "select o from " + FileManagement.class.getName() + " o where o.carId = '" + carId + "'";
			@SuppressWarnings("unchecked")
			List<FileManagement> list = (List<FileManagement>) this.hibernateDao.getObjectsList(sql,
					pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("fileTypeRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getFileType()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
}
