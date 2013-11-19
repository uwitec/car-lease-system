/**  
 * @Filename:    AlarmService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 上午09:31:15  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.org.dqgb.bean.AlarmConfiguration;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface AlarmService {
	
	/**
	 *
	 * @TODO	配置个人预警信息(APC alarm parameter configuration)
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @param	acf
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public abstract void setPersonalAPC(User user,List<AlarmConfiguration> acf) throws ServiceException; 
	
	
	/**
	 *
	 * @TODO	删除个人预警配置
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 *
	 */
	public abstract void deletePersonalAPC(User user) throws ServiceException;
	
	
	/**
	 *
	 * @TODO	获取个人预警配置信息
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getPersonalAPC(User user) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取个人预警信息	AI:alarm infromation
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	user
	 * @param	acf
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getPersonalAI(User user,List<AlarmConfiguration> acf) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取文件档案预警信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @param	operator
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getFileAlarmInformation(User operator,int pageIndex, int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	获取订单预警信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @param	operator
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getOrderAlarmInformation(User operator, int pageIndex,int pageSize) throws ServiceException;
	
	
	/**
	 * 获取到期预警订单
	 * @author 	肖乾斌
	 * @date 	2011-12-4
	 * @param pageIndex
	 * @param pageSize
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getOrderAlarmInfor(int pageIndex, int pageSize)throws ServiceException;
	/**
	 * 
	 * @TODO	获取保证金预警信息
	 * @author 	肖乾斌
	 * @date 	2011-6-1
	 * @param 	operator
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getVsAlarmInformation(User operator, int pageIndex,int pageSize) throws ServiceException;
}
