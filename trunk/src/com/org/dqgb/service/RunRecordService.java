/**  
 * @Filename:    RunRecordService.java  
 * @TODO:		  车辆出行记录
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-3 下午02:27:15  
 *
 */  

package com.org.dqgb.service;

import java.util.Date;

import com.org.dqgb.entity.RunRecord;
import com.org.dqgb.exception.ServiceException;

public interface RunRecordService {
	
	/**
	 *
	 * @TODO	根据订单号获取车辆出行记录信息
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	orderFormId
	 * @throws  ServiceException
	 * @return  RunRecord
	 *
	 */
	public abstract RunRecord getRunRecordByOrderFormID(String orderFormId)throws ServiceException;
	
	/**
	 * 根据日期、车牌号查询出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-3
	 * @param 	carId
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	RunRecord
	 */
	public abstract RunRecord getRunRecord(String carId,Date date)throws ServiceException;
}
