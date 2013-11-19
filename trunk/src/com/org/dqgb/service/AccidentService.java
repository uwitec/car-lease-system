/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-8</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-8	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Accident;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface AccidentService {

	/**
	 * 添加事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @param 	acc
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void add(Accident acc,User user)throws ServiceException;
	
	/**
	 * 修改事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @param 	acc
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void modify(Accident acc,User user)throws ServiceException;
	
	/**
	 * 根据id获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	Accident
	 */
	public abstract Accident getAccident(int id)throws ServiceException;
	
	/**
	 * 获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getAccident(int pageIndex, int pageSize)throws ServiceException;
	
	
	/**
	 * 根据订单号获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getAccident(String orderId)throws ServiceException;
	
	/**
	 * 根据订单号获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	Accident
	 */
	public abstract Accident getAccidentByOrderId(String orderId)throws ServiceException;
	
	/**
	 * 锁定记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	id
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void lockRecord(int id,User user)throws ServiceException;
	
	/**
	 * 获取事故处理费用总计
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	int
	 */
	public abstract int getAccidentFinance(String orderId)throws ServiceException;
}
