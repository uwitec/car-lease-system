/**  
 * @Filename:    CustomerService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午08:11:43  
 *
 */  

package com.org.dqgb.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Customer;
import com.org.dqgb.exception.ServiceException;

public interface CustomerService {
	
	/**
	 *
	 * @TODO	获取所有的客户信息，带分页效果
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  List<Customer>
	 *
	 */
	public JSONObject getAllCustomer(int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据条件模糊查询用户信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	con
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public JSONObject getAllCustomerByCondition(int pageIndex,int pageSize,Map<String,Object> con) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据身份证号查询客户信息
	 * @date	2011-4-6
	 * @author	肖乾斌
	 * @param	idCardNumber	身份证号码
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public JSONObject getCustomerByIdCardNumber(String idCardNumber) throws ServiceException;
	
	/**
	 * 按日期查询客户
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param date
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	public JSONObject getCustomerByDate(String date) throws ServiceException;
	
	/**
	 * 查找客户
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @param idCarNumber
	 * @throws ServiceException
	 * @return 	Customer
	 */
	public Customer getCustomer(String idCarNumber) throws ServiceException;
	/**
	 *
	 * @TODO	根据id信息获取客户信息
	 * @date	2011-4-6
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  Customer
	 *
	 */
	public Customer getCustomerById(int id) throws ServiceException;
	
	/**
	 *
	 * @TODO	添加客户信息
	 * @date	2011-4-6
	 * @author	肖乾斌
	 * @param	customer	客户信息
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public boolean addCustomer(Customer customer) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改客户信息
	 * @date	2011-4-6
	 * @author	肖乾斌
	 * @param	customer
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public boolean modifyCustomer(Customer customer) throws ServiceException;
	
	/**
	 * 直接更新用户信息
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	cus
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public void updateCustomer(Customer cus)throws ServiceException;
	/**
	 *
	 * @TODO	根据id删除客户信息，永久性删除
	 * @date	2011-4-6
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public boolean deleteCustomerByID(int id) throws ServiceException;
}
