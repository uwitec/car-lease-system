/**  
 * @Filename:    FinanceService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午10:39:25  
 *
 */  

package com.org.dqgb.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface FinanceService {

	/**
	 *
	 * @TODO	添加一笔财务信息
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	finance
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public void addFinance(Finance finance) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据订单序列号获取财务信息。
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	orderFormId 	订单序列号
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public JSONObject getFinanceByOrderFormId(String orderFormId) throws ServiceException;

	/**
	 * 获取财务数据
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public JSONObject getFinance(int pageIndex, int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	删除订单关联的财务信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	orderFormId
	 * @throws  ServiceException
	 *
	 */
	public void deleteFinanceByOrderFormId(String orderFormId) throws ServiceException;
	
	/**
	 * 
	 * @TODO	根据订单号获取车务人员填写的费用信息
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	orderFormId
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public List<Finance> getCarManagerFinances(String orderFormId, User operator) throws ServiceException;
	
	/**
	 * 获取订单财务
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	Map<Integer,Integer>
	 */
	public Map<Integer,Integer> getFinanceDetails(String orderId)throws ServiceException;
	
	/**
	 * 获取预付租金
	 * @author 	肖乾斌
	 * @date 	2011-12-15
	 * @param orderId
	 * @throws ServiceException
	 * @return 	int
	 */
	public int getPrePayRent(String orderId)throws ServiceException;
}
