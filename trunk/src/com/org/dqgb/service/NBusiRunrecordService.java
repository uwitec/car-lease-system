/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-6</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-6	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import java.util.Date;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.NonBusinessRunRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface NBusiRunrecordService {

	/**
	 * 添加出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	nbrr
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public void addRunRecord(NonBusinessRunRecord nbrr,User user)throws ServiceException;
	
	/**
	 * 内部用车收车
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @param 	carId
	 * @param 	user
	 * @param 	kmEnd
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public void giveBackCar(int rrId, int kmEnd,User user)throws ServiceException;
	
	
	/**
	 * 根据车牌号获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param carId
	 * @throws ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	public NonBusinessRunRecord getRunRecord(String carId)throws ServiceException;
	
	/**
	 * 查找车辆内部用车记录
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @param 	carId
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	public JSONObject getRunRecord(String carId, int pageIndex, int pageSize)throws ServiceException;
	/**
	 * 根据id获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param id
	 * @throws ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	public NonBusinessRunRecord getRunRecord(int id)throws ServiceException;
	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public JSONObject getRunRecord(int pageIndex, int pageSize)throws ServiceException;
	
	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public JSONObject getRunRecord(String carId,Date dt)throws ServiceException;

	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-10
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	public NonBusinessRunRecord getAccidentRunRecord(String carId,Date dt)throws ServiceException;
	/**
	 * 验证是否存在违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-10
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	boolean
	 */
	public boolean isRunRecord(String carId,Date dt)throws ServiceException;
}
