/**  
 * @Filename:    CustomerServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-6 下午08:00:49  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Customer;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CustomerService;

@Service("CustomerServiceImpl")
public class CustomerServiceImpl implements CustomerService {

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAllCustomer(int pageIndex, int pageSize)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			JSONArray arr = null;
			String sql = "select o from " + Customer.class.getName() + " o";
			int count = 0;
			count = this.hibernateDao.getCount(sql, true);
			List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			arr = JSONArray.fromObject(list);
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, count);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean addCustomer(Customer customer) throws ServiceException {
		if(customer == null){
			throw new ServiceException("客户信息不能为空！");
		}
		if(null == customer.getIdCardNumber() || "".equals(customer.getIdCardNumber())){
			throw new ServiceException("客户身份证信息不能为空！");
		}
		String sql = "select o from " + Customer.class.getName() + 
		" o where o.idCardNumber = '" + customer.getIdCardNumber() + "'";
		List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsByHsql(sql);
		if(list.size() != 0){
			throw new ServiceException("客户信息已经存在,不能重复添加!");
		}
		if(null == customer.getCustomerName() || "".equals(customer.getCustomerName())){
			throw new ServiceException("客户名字信息不能为空！");
		}
		try{
//			customer.setRegisterDate(new Date());
			this.hibernateDao.save(customer);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Override
	public boolean deleteCustomerByID(int id) throws ServiceException {
		try{
			Customer customer = null;
			customer = (Customer) this.hibernateDao.getObjectByID(Customer.class, id);
			if(null != customer)
				this.hibernateDao.delete(customer);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getCustomerByIdCardNumber(String idCardNumber)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + Customer.class.getName() + 
							" o where o.idCardNumber = '" + idCardNumber + "'";
			List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsByHsql(sql);
			JSONArray arr = JSONArray.fromObject(list);
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

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
	@SuppressWarnings("unchecked")
	@Override
	public boolean modifyCustomer(Customer customer) throws ServiceException {
		if("".equals(customer.getIdCardNumber()) || null == customer.getIdCardNumber()){
			throw new ServiceException("客户姓名身份证号码不能为空!");
		}
		String sql = "select o from " + Customer.class.getName() + 
		" o where o.id = '" + customer.getId() + "'";
		List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsByHsql(sql);
		if(list.size() == 0){
			throw new ServiceException("客户信息不存在，无法修改!");
		}
		if("".equals(customer.getCustomerName()) || null == customer.getCustomerName()){
			throw new ServiceException("客户姓名不能为空!");
		}
		if("".equals(customer.getMobilePhone()) || null == customer.getMobilePhone()){
			throw new ServiceException("客户联系方式不能为空!");
		}
		try{
			Customer cus = list.get(0);
			cus.setGender(customer.getGender());
			cus.setCompany(customer.getCompany());
			cus.setCustomerName(customer.getCustomerName());
			cus.setMobilePhone(customer.getMobilePhone());
			cus.setIdCardNumber(customer.getIdCardNumber());
			cus.setAddress(customer.getAddress());
			cus.setCompanyAddress(customer.getCompanyAddress());
			cus.setIntroducer(customer.getIntroducer());
			cus.setWarrantor(customer.getWarrantor());
			cus.setRegisterDate(customer.getRegisterDate());
			cus.setEndDate(customer.getEndDate());
			cus.setCustomerLevel(customer.getCustomerLevel());
			cus.setBlacklist(customer.getBlacklist());
			this.hibernateDao.saveOrUpdate(cus);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

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
	@Override
	public Customer getCustomerById(int id) throws ServiceException {
		Customer customer = null;
		try{
			customer = (Customer) this.hibernateDao.getObjectByID(Customer.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return customer;
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAllCustomerByCondition(int pageIndex, int pageSize,
			Map<String, Object> con) throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map =  this.hibernateDao.getListByCondition(Customer.class, con, null, pageIndex, pageSize);
			List<Customer> list = (List<Customer>)map.get(Constant.DATA);
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, map.get(Constant.TOTAL_SIZE));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	/**
	 * 查找客户
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @param idCarNumber
	 * @throws ServiceException
	 * @return 	Customer
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public Customer getCustomer(String idCarNumber) throws ServiceException {
		try{
			List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsList("select o from " + Customer.class.getName() + " o where o.idCardNumber = '" + idCarNumber + "'", 
					0,1, true);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 直接更新用户信息
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	cus
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateCustomer(Customer cus) throws ServiceException {
		try{
			this.hibernateDao.update(cus);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 按日期查询客户
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param date
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getCustomerByDate(String date) throws ServiceException {
		try{
			String[] d = date.split("/");
			String db = (d[0] + "-" + d[1] + "-1 0:00:00");
			String de = (d[0] + "-" + (NumberUtils.toInt(d[1]) + 1) + "-1 0:00:00");
			if(d[1].equals("12")){
				d[0] = NumberUtils.toInt(d[0]) + 1 + "";
				de = (d[0] + "-1-1 0:00:00");
			}
			List<Customer> list = (List<Customer>) this.hibernateDao.getObjectsByHsql("select o from " + 
					Customer.class.getName() + " o where o.registerDate >= '" + db + 
					"' and o.registerDate < '" + de + "'");
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("leaf", "false");
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
