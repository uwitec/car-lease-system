/**  
 * @Filename:    FinanceServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午10:44:24  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.HashMap;
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
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.FinanceService;
import com.org.dqgb.service.OrganizationService;

@Service("FinanceServiceImpl")
public class FinanceServiceImpl implements FinanceService {

	@Resource @Qualifier("hibernateDao")					
	public HibernateDao hibernateDao;
	
	@Resource(name="DictionaryServiceImpl")
	public DictionaryService dicService;
	
	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	public Logger log = Logger.getLogger(this.getClass());
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
	@Override
	public void addFinance(Finance finance) throws ServiceException {
		if(finance.getSerialNumber() == null || "".equals(finance.getSerialNumber())){
			throw new ServiceException("添加财务信息失败,订单号不能为空!");
		}
		if(finance.getMoney() == 0){
			throw new ServiceException("添加财务信息失败,金额不能为0!");
		}
		try{
			this.hibernateDao.saveOrUpdate(finance);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getFinanceByOrderFormId(String orderFormId)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + orderFormId + "'";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("financeTypeRemark", dicService.getDictionaryById(list.get(i).getFinanceType()).getRemark());
				arr.getJSONObject(i).put("statusRemark", dicService.getDictionaryById(list.get(i).getPayDirection()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	删除订单关联的财务信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	orderFormId
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void deleteFinanceByOrderFormId(String orderFormId)
			throws ServiceException {
		try{
			String sql = "delete from T_FINANCE where SERIAL_NUMBER = '" + orderFormId + "'";
			this.hibernateDao.executeNativeSql(sql);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}
	
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Finance> getCarManagerFinances(String orderFormId, User operator)
			throws ServiceException {
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + orderFormId + "' and " +
					"o.transactor = '" + operator.getUserName() + "' and o.financeType in (select a.id from " + 
					Dictionary.class.getName() + " a where a.groupId = '" + Constant.GROUP_MONEY_TYPE_CM + "')";
			return (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * 获取订单财务
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	Map<Integer,Integer>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Map<Integer, Integer> getFinanceDetails(String orderId)
			throws ServiceException {
		try{
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + orderId + "'";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			if(list.size() == 0){
				return null;
			}else{
				int rent = 0;
				int in = 0;
				for(int i = 0; i < list.size(); i++){
					Finance f = list.get(i);
					if(f.getFinanceType() == Constant.GROUP_F_PRICE){
						if(f.getPayDirection() == Constant.FINANCE_IN){
							rent += f.getMoney();
						}else{
							rent -= f.getMoney();
						}
					}
					if(f.getPayDirection() == Constant.FINANCE_IN){
						in += f.getMoney();
					}else{
						in -= f.getMoney();
					}
				}
				map.put(Constant.GROUP_F_PRICE, rent);
				map.put(Constant.FINANCE_IN, in);
				return map;
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取财务数据
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getFinance(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.money != '0' and (o.payDirection = '30' or o.payDirection = '31') order by o.payDate desc";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("remark", this.dicService.getDictionaryById(list.get(i).getFinanceType()).getRemark());
				arr.getJSONObject(i).put("deptName", this.organizationService.getOrganizationById(list.get(i).getOrganizationId()).getDepartmentName());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取预付租金
	 * @author 	肖乾斌
	 * @date 	2011-12-15
	 * @param orderId
	 * @throws ServiceException
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getPrePayRent(String orderId) throws ServiceException {
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + orderId + "' and o.payType = '"
						+ Constant.PREPAY + "' and o.financeType = '" + Constant.GROUP_F_PRICE + "'";
			@SuppressWarnings("unchecked")
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 10000, true);
			if(list.size() == 0)
				return 0;
			else{
				return list.get(0).getMoney();
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
}
