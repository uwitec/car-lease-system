/**  
 * @Filename:    LeaseRecordServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午09:02:20  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.common.FinanceConstant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Accident;
import com.org.dqgb.entity.Car;
import com.org.dqgb.entity.Customer;
import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.Organization;
import com.org.dqgb.entity.Relet;
import com.org.dqgb.entity.RunRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.AccidentService;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.FinanceService;
import com.org.dqgb.service.LeaseRecordService;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.PriceService;
import com.org.dqgb.service.ReletService;
import com.org.dqgb.service.RunRecordService;
import com.org.dqgb.service.UserService;

@Service("LeaseRecordServiceImpl")
public class LeaseRecordServiceImpl implements LeaseRecordService {

	@Resource @Qualifier("hibernateDao")					
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("CustomerServiceImpl")
	public  CustomerService customerService;

	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;

	@Resource @Qualifier("FinanceServiceImpl")
	public FinanceService financeService;
	
	@Resource @Qualifier("CarServiceImpl")
	public CarService carService;
	
	@Resource @Qualifier("PriceServiceImpl")
	public PriceService priceService;

	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	@Resource @Qualifier("RunRecordServiceImpl")
	public RunRecordService runRecordService;
	
	@Resource(name="ReletServiceImpl")
	public ReletService reletService;
	
	@Resource(name="AccidentServiceImpl")
	public AccidentService accService;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	获取订单信息，按时间降序排列
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	condition 查询条件
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getOrderForms(int pageIndex, int pageSize,
			Map<String, Object> condition,Map<String,String> orderBy) throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map =  this.hibernateDao.getListByCondition(LeaseRecord.class, condition,orderBy, pageIndex, pageSize);
			List<LeaseRecord> list = (List<LeaseRecord>)map.get(Constant.DATA);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
				
				//添加业务结算员真实的姓名
				if(null != list.get(i).getFinisher() && !"".equals(list.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(list.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(list.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
				
				//取车、还车日期
				RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(list.get(i).getSerialNumber());
				if(null != rr){
					Date giveBackDate = rr.getDateEnd();
					arr.getJSONObject(i).put("giveBackCarDate", giveBackDate == null ? "" : giveBackDate);
					Date fetchCarDate = rr.getDateBegin();
					arr.getJSONObject(i).put("fetchCarDate", fetchCarDate == null ? "" : fetchCarDate);
				}
				//财务状况
				if(list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_OVER_NORMALLY){
					Map<Integer,Integer> fmap = this.financeService.getFinanceDetails(list.get(i).getSerialNumber());
					arr.getJSONObject(i).put("totalIn",fmap.get(Constant.FINANCE_IN));
					arr.getJSONObject(i).put("rent",fmap.get(Constant.GROUP_F_PRICE));
				}else{
					arr.getJSONObject(i).put("totalIn",0);
					arr.getJSONObject(i).put("rent",0);
				}
				//租赁模式
				arr.getJSONObject(i).put("leaseModelRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());
				
				if(list.get(i).getFetchOrgId() != 0){
					arr.getJSONObject(i).put("fetchDeptName", this.organizationService.getOrganizationById(list.get(i).getFetchOrgId()).getDepartmentName());
				}
				if(list.get(i).getGivebackOrgId() != 0){
					arr.getJSONObject(i).put("givebackDeptName", this.organizationService.getOrganizationById(list.get(i).getGivebackOrgId()).getDepartmentName());
				}
				
				if(list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_PROCESSING || 
						list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_GIVEBACK_CAR ||
						list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_SUPPLIMENT){
					Map<String,Integer> fMap = this.rentCalculate(list.get(i).getSerialNumber());
//					int rent = fMap.get(FinanceConstant.THEORY_FEE);													//实际发生的租金总额
					int dis = fMap.get(FinanceConstant.ACTUAL_RELET_FEE) - fMap.get(FinanceConstant.RECIEVED_RELET_FEE);	//获取差价
					list.get(i).setReletFeeWaiting(0);
					if(dis > 0){											//租金已超出实际支付
						arr.getJSONObject(i).put("reletFeeWaiting", dis);
						list.get(i).setReletFeeWaiting(dis);
						
//						int actualReletFee = rent - fMap.get(FinanceConstant.RENT_FEE);//实际发生的续租费用
//						actualReletFee = actualReletFee > fMap.get(FinanceConstant.RELET_FEE) ? fMap.get(FinanceConstant.RELET_FEE) : actualReletFee;
//						//续租费的差额
//						dis = fMap.get(FinanceConstant.RECIEVED_RELET_FEE) - actualReletFee;
//						if(dis < 0){
//							arr.getJSONObject(i).put("reletFeeWaiting", dis);
//							list.get(i).setReletFeeWaiting(dis);
//						}
					}
					this.hibernateDao.update(list.get(i));
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, map.get(Constant.TOTAL_SIZE));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	下订单
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	lr
	 * @param	finances 财务明细
	 * @param	operator 操作人员
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@Transactional (propagation=Propagation.REQUIRED)
	@Override
	public void addOrderForm(LeaseRecord lr, List<Finance> finances, User operator) throws ServiceException {
		lr.setLeaseRecordStatus(Constant.ORDER_STATUS_PRE_BOOK);
		/*if(null == lr.getCustomerIdCardNumber() || "".equals(lr.getCustomerIdCardNumber())){
			throw new ServiceException("请输入客户身份证号码!");
		}*/
		if(0 == lr.getLeaseWay()){
			throw new ServiceException("请选择租赁方式!");
		}
		if(0 == lr.getLeaseModel()){
			throw new ServiceException("请选择租赁模式!");
		}
		if(null == this.customerService.getCustomer(lr.getCustomerIdCardNumber())){
			throw new ServiceException("客户信息不存在，请核对!");
		}
		try{
			String sn = this.generateSerialNumber(operator.getDepartmentId());
			lr.setSerialNumber(sn);
			lr.setOrderId(operator.getDepartmentId());
			this.hibernateDao.saveOrUpdate(lr);
			for(int i = 0; i < finances.size(); i++){
				Finance f = finances.get(i);
				f.setSerialNumber(sn);
				f.setPayDate(lr.getOrderDate());
				f.setPayDirection(Constant.FINANCE_IN);
				f.setPayType(Constant.PREPAY);
				f.setOrganizationId(operator.getDepartmentId());
				this.hibernateDao.saveOrUpdate(f);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	获取指定车辆的近期预约单信息,包括正在执行中的
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	carId
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getBookRecord(int pageIndex, int pageSize, String carId)
			throws ServiceException {
		JSONObject json = new JSONObject();
		if(null == carId || "".equals(carId)){
			throw new ServiceException("获取预约记录失败,车辆id不能为空！");
		}
		String sql = "select o from " + LeaseRecord.class.getName() + " o where  o.carId = '" + carId + "' and (o.leaseRecordStatus = '" + 
					Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_BOOKING +  
					"'  or o.leaseRecordStatus = '" +  Constant.ORDER_STATUS_PRE_BOOK + "') order by o.dateBegin,o.carId asc";
		try{	
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());

				//添加业务结算员真实的姓名
				if(null != list.get(i).getFinisher() && !"".equals(list.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(list.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(list.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
				
				//取车、还车日期
				RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(list.get(i).getSerialNumber());
				if(null != rr){
					Date giveBackDate = rr.getDateEnd();
					arr.getJSONObject(i).put("giveBackCarDate", giveBackDate == null ? "" : giveBackDate);
					Date fetchCarDate = rr.getDateBegin();
					arr.getJSONObject(i).put("fetchCarDate", fetchCarDate == null ? "" : fetchCarDate);
				}
				//租赁模式
				arr.getJSONObject(i).put("leaseModelRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());
				if(list.get(i).getFetchOrgId() != 0){
					arr.getJSONObject(i).put("fetchDeptName", this.organizationService.getOrganizationById(list.get(i).getFetchOrgId()).getDepartmentName());
				}
				if(list.get(i).getGivebackOrgId() != 0){
					arr.getJSONObject(i).put("givebackDeptName", this.organizationService.getOrganizationById(list.get(i).getGivebackOrgId()).getDepartmentName());
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	生成序列号
	 * @date	2011-4-29
	 * @author	肖乾斌
	 * @param 	departmentId
	 * @return  String
	 *
	 */
	@Override
	public String generateSerialNumber(int departmentId) throws ServiceException {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		return departmentId + "-" + cal.get(Calendar.YEAR) + (month >= 10 ? month : ("0" + month)) + 
				(day >= 10 ? day : ("0" + day)) + "-" + (hour >= 10 ? hour : ("0" + hour)) + 
				(minute >= 10 ? minute : ("0" + minute)) + (sec >= 10 ? sec : ("0" + sec));
//		return UUID.randomUUID().toString();	
	}

	/**
	 *
	 * @TODO	根据订单号获取订单信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getOrderFormBySerialNumber(String serialNumber)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 100, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_PROCESSING || 
						list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_GIVEBACK_CAR ||
						list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_SUPPLIMENT){
					Map<String,Integer> fMap = this.rentCalculate(list.get(i).getSerialNumber());
//					int rent = fMap.get(FinanceConstant.THEORY_FEE);													//实际发生的租金总额
					int dis = fMap.get(FinanceConstant.ACTUAL_RELET_FEE) - fMap.get(FinanceConstant.RECIEVED_RELET_FEE);	//获取差价
					list.get(i).setReletFeeWaiting(0);
					if(dis > 0){											//租金已超出实际支付
						arr.getJSONObject(i).put("reletFeeWaiting", dis);
						list.get(i).setReletFeeWaiting(dis);
					}
					this.hibernateDao.update(list.get(i));
				}
			}
			
			JSONObject fn = this.financeService.getFinanceByOrderFormId(serialNumber);
			arr.getJSONObject(0).put("finance", fn);
			
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, list.size());
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	修改订单,该操作只有在订单状态为预填状态才有效
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	lr
	 * @param	finances 财务明细
	 * @param	currentOperator
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void modifyOrderForm(LeaseRecord lr, List<Finance> finances, User currentOperator)
			throws ServiceException {
		String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + lr.getSerialNumber() + "'";
		List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
		if(list.size() == 0){
			throw new ServiceException("没有找到指定订单信息!");
		}
		LeaseRecord lrOrg = list.get(0);
		if(lrOrg.getLeaseRecordStatus() != Constant.ORDER_STATUS_PRE_BOOK){
			throw new ServiceException("不能修改非预填状态的订单!");
		}
		if(!currentOperator.getUserName().equals(lrOrg.getTransactor())){
			throw new ServiceException("该操作必须由填报者亲自完成!");
		}
		try{
			lrOrg.setCustomerIdCardNumber(lr.getCustomerIdCardNumber());
			lrOrg.setDateBegin(lr.getDateBegin());
			lrOrg.setDateEnd(lr.getDateEnd());
			lrOrg.setLeaseWay(lr.getLeaseWay());
			lrOrg.setLeaseModel(lr.getLeaseModel());
			lrOrg.setPrice(lr.getPrice());
			lrOrg.setPricePerHour(lr.getPricePerHour());
			lrOrg.setPricePerKm(lr.getPricePerKm());
			lrOrg.setCustomerNameRemark(lr.getCustomerNameRemark());
			this.hibernateDao.saveOrUpdate(lrOrg);
			this.financeService.deleteFinanceByOrderFormId(lr.getSerialNumber());
			for(int i = 0; i < finances.size(); i++){
				Finance f = finances.get(i);
				f.setSerialNumber(lrOrg.getSerialNumber());
				f.setPayDate(lrOrg.getOrderDate());
				f.setPayDirection(Constant.FINANCE_IN);
				f.setPayType(Constant.PREPAY);
				f.setOrganizationId(currentOperator.getDepartmentId());
				this.hibernateDao.saveOrUpdate(f);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}


	/**
	 *
	 * @TODO	删除预约单，连带删除财务信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	currentOperator
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void deleteLeaseRecord(String serialNumber,User currentOperator) throws ServiceException {
		String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
		List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
		if(list.size() == 0){
			throw new ServiceException("没有找到指定订单信息!");
		}
		if(!currentOperator.getUserName().equals(list.get(0).getTransactor())){
			throw new ServiceException("该操作必须由填报者亲自完成!");
		}
		try{
			this.hibernateDao.delete(list.get(0));
			this.financeService.deleteFinanceByOrderFormId(serialNumber);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 *
	 * @TODO	把订单状态从预填状态修改为预定状态 ,该操作必须满足两个前提，一是本人才能修改，二是该定单处于预填状态
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	currentOperator			当前业务操作人员
	 * @throws 	ServiceException
	 * @return  void
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public void changeOrderFormToBooking(String serialNumber, User currentOperator)
			throws ServiceException {
		String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
		List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
		if(list.size() == 0){
			throw new ServiceException("没有找到指定订单信息!");
		}
		LeaseRecord lr = list.get(0);
		if(!currentOperator.getUserName().equals(lr.getTransactor())){
			throw new ServiceException("该操作必须由填报者亲自完成!");
		}
		if(StringUtils.isBlank(lr.getCustomerIdCardNumber())){
			throw new ServiceException("需要先填写客户身份证号码才能出示单据！");
		}
		try{
			//如果订单状态为预填状态，则修改为预定状态
			if(lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_PRE_BOOK){
				lr.setLeaseRecordStatus(Constant.ORDER_STATUS_BOOKING);
				this.hibernateDao.saveOrUpdate(lr);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 *
	 * @TODO	获取订单打印信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	serialNumber			订单号
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getOrderInfoToPrint(String serialNumber)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			JSONArray alr = JSONArray.fromObject(list);
			for(int i = 0; i < alr.size(); i++){									//获取租赁方式、模式以及订单状态的描述
				alr.getJSONObject(i).put("leaseWayRemark",
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseWay()).getRemark());
				alr.getJSONObject(i).put("leaseModelRemark",
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());
				alr.getJSONObject(i).put("leaseRecordStatusRemark",
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
			}
			
			//获取财务信息
			sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<Finance> lf = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			JSONArray af = JSONArray.fromObject(lf);
			for(int i = 0; i < lf.size(); i++){				//增添财务描述
				af.getJSONObject(i).put("financeRemark",
						this.dictionaryService.getDictionaryById(lf.get(i).getFinanceType()).getRemark());
				af.getJSONObject(i).put("payDirectionRemark",
						this.dictionaryService.getDictionaryById(lf.get(i).getPayDirection()).getRemark());
			}
			json.put("customerInformation", 
					this.customerService.getCustomerByIdCardNumber(list.get(0).getCustomerIdCardNumber()));
			json.put(Constant.DATA, alr);
			json.put("finance", af);
			json.put(Constant.TOTAL_SIZE, list.size());
			
			//获取车辆信息
			Car car = this.carService.getCarByCarId(list.get(0).getCarId());
			JSONArray carArr = JSONArray.fromObject(car);
			String category = car.getCarCategory();
			String brandName = dictionaryService.getDictionaryById(car.getBrandId()).getRemark();
			carArr.getJSONObject(0).put(Constant.BRAND_NAME, brandName);
			carArr.getJSONObject(0).put(Constant.CATEGORY, category);
			carArr.getJSONObject(0).put(Constant.PRICE,car.getPrice());
			carArr.getJSONObject(0).put(Constant.FOREGIFT,car.getForegift());
			carArr.getJSONObject(0).put(Constant.PRICE_PER_HOUR,car.getPricePerHour());
			carArr.getJSONObject(0).put(Constant.PRICE_PER_KM,car.getPricePerKm());
			Organization org = this.organizationService.getOrganizationById(car.getOrganizationId());//增加组织名字
			carArr.getJSONObject(0).put(Constant.DEPARTMENT_NAME,org.getDepartmentName());
			json.put("carInformation", carArr);
			
			//增添当前日期
			json.put("currentDate", JSONArray.fromObject(new Date()));
			
			//增添出行记录
			RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(serialNumber);
			if(null != rr){
				JSONObject jrr = JSONObject.fromObject(rr);
				jrr.put("FetchOrgName", this.organizationService.getOrganizationById(rr.getFetchOrgId()).getDepartmentName());
				if(rr.getGiveBackOrgId() != 0)
					jrr.put("GivebackOrgName", this.organizationService.getOrganizationById(rr.getGiveBackOrgId()).getDepartmentName());
				json.put("RunRecord", jrr);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	订单结算，只有当订单处于"已还车"状态的时候该操作才有效
	 * @date	2011-5-1
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	operator	业务结算员
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void settleOrderForm(String serialNumber, User operator) throws ServiceException {
		try{
			LeaseRecord lr = this.financeSettle(serialNumber,operator);				//确保用户不是从URL地址栏进入
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			Date dt = new Date();
			for(int i = 0; i < list.size(); i++){
				Finance f = list.get(i);
				//该收的收，该还的还
				if(f.getPayDirection() == Constant.FINANCE_WAITING){
					f.setPayDirection(Constant.FINANCE_IN);
					f.setTransactorOver(operator.getUserName());
					f.setTransactorOverName(operator.getRealName());
					f.setPayType(Constant.SETTLEMENT);
					f.setOrganizationId(operator.getDepartmentId());
					f.setPayDate(dt);
					if(f.getFinanceType() == Constant.GROUP_F_RELET_FEE){
						f.setRemark("已补续租费");
					}
					this.hibernateDao.update(f);
				}else if(f.getPayDirection() == Constant.FINANCE_WAITING_OUT){
					f.setPayDirection(Constant.FINANCE_OUT);
					f.setTransactorOver(operator.getUserName());
					f.setTransactorOverName(operator.getRealName());
					f.setOrganizationId(operator.getDepartmentId());
					f.setPayDate(dt);
					f.setPayType(Constant.SETTLEMENT);
					if(f.getFinanceType() == Constant.GROUP_F_RELET_FEE){
						f.setRemark("已退续租费");
					}
					this.hibernateDao.update(f);
				}
			}
			lr.setDateSettle(dt);
			lr.setFinisher(operator.getUserName());
			lr.setReletFeeWaiting(0);
			lr.setLeaseRecordStatus(Constant.ORDER_STATUS_OVER_NORMALLY);
			this.hibernateDao.saveOrUpdate(lr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	从车务部发车
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	serialNumber	发车单
	 * @param	operator		发车业务操作员
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void startOut(String serialNumber,String sender,String maker, User operator,String leaveTable)
			throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(serialNumber);
		if(null == lr){
			throw new ServiceException("发车异常！异常原因：该订单不存在,请仔细确认.");
		}
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_BOOKING){
			throw new ServiceException("非预定状态的订单无法进行发车操作!");
		}else{
			Car car = this.carService.getCarByCarId(lr.getCarId());
			if(car.getStatus() != Constant.CAR_STATUS_IDLE){
				String remark = this.dictionaryService.getDictionaryById(car.getStatus()).getRemark();
				throw new ServiceException("车辆(" + car.getCarId() + ")正处于&nbsp;<font style='color:fuchsia;'>" + remark + "</font>&nbsp;状态,无法发车!");
			}
			try{
				lr.setLeaseRecordStatus(Constant.ORDER_STATUS_PROCESSING);		//发车
				lr.setFetchOrgId(operator.getDepartmentId());
				lr.setSender(sender);
				lr.setMaker(maker);
				lr.setLeaveTable(leaveTable);
				this.hibernateDao.saveOrUpdate(lr);
				//添加车辆出行记录
				RunRecord rr = new RunRecord();
				
				Date dt = new Date();
		//		dt.setTime(dt.getTime() - 2 * 24 * 60 * 60 * 1000);
				rr.setDateBegin(dt);
				rr.setCarId(lr.getCarId());
				rr.setFetchOrgId(operator.getDepartmentId());
				rr.setOrderFormSerialNumber(serialNumber);
				rr.setBeginTransactor(operator.getUserName());
				rr.setStartKm(car.getKm());
				this.hibernateDao.saveOrUpdate(rr);
				
				//修改车辆状态
				car.setStatus(Constant.CAR_STATUS_BUSY);
				this.hibernateDao.saveOrUpdate(car);
			}catch(Exception e){
				log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
			}
		}
	}

	/**
	 *
	 * @TODO	根据订单号获取订单信息
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param   serialNumber
	 * @throws  ServiceException
	 * @return  LeaseRecord
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public LeaseRecord getLeaseRecordBySerialNumber(String serialNumber)
			throws ServiceException {
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	收车
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	carId
	 * @param	operator
	 * @param	lf
	 * @param	accident
	 * @param	km
	 * @param	backTablePath
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public void giveBackCar(String carId,User operator,List<Finance> lf, int km,String backTablePath,int accident)
			throws ServiceException{
		String sql = "select o from " + LeaseRecord.class.getName() + " o where o.carId = '" + 
					carId + "' and o.leaseRecordStatus = '" + Constant.ORDER_STATUS_PROCESSING + "'";
		List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
		if(list.size() == 0){
			throw new ServiceException("没有找到相应的发车单!请确认后再操作!");
		}
		LeaseRecord lr = list.get(0);
		Car car = this.carService.getCarByCarId(lr.getCarId());
		if(km < car.getKm()){
			throw new ServiceException("里程数不能小于当前里程(" + car.getKm() + ")!");
		}
		try{
			//修改订单状态以及车的归还日期
			Date dt = new Date();
			if(dt.after(lr.getDateEnd())){
				lr.setIsBeyondDeadline(Constant.BEYOND_DEADLINE);
			}else{
				lr.setIsBeyondDeadline(Constant.NORMAL_BACK);
			}
			lr.setLeaseRecordStatus(Constant.ORDER_STATUS_GIVEBACK_CAR);
			lr.setGivebackOrgId(operator.getDepartmentId());
			lr.setBackTable(backTablePath);
			
			if(accident != 0){	//事故收车添加事故记录
				lr.setAccident(1);
				Accident ac = new Accident();
				ac.setSerialNumber(lr.getSerialNumber());
				ac.setCarId(carId);
				ac.setTransactor(operator.getUserName());
				this.hibernateDao.save(ac);
			}
			this.hibernateDao.saveOrUpdate(lr);
			//修改车的状态。
			car.setStatus(Constant.CAR_STATUS_IDLE);
			car.setOrganizationId(operator.getDepartmentId());				//修改车辆驻场信息
			car.setKm(km);
			this.hibernateDao.saveOrUpdate(car);
			//修改车辆出行记录
			RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(lr.getSerialNumber());
			rr.setDateEnd(dt);
			rr.setGiveBackOrgId(operator.getDepartmentId());
			rr.setEndTransactor(operator.getUserName());
			rr.setEndKm(km);
			this.hibernateDao.saveOrUpdate(rr);
			
			for(int i = 0; i < lf.size(); i++){
				Finance f = lf.get(i);
				if(f.getMoney() == 0){
					continue;
				}
				f.setPayDate(dt);
				f.setPayDirection(Constant.FINANCE_WAITING);				//待缴费用
				f.setSerialNumber(lr.getSerialNumber());
				f.setTransactor(operator.getUserName());
				f.setTransactorName(operator.getRealName());
				this.hibernateDao.saveOrUpdate(f);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	财务结算操作，该操作执行完毕以后数据库中会出现该退还、该收取的财务信息。
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	operator	业务员
	 * @throws  ServiceException
	 * @return  LeaseRecord
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public LeaseRecord financeSettle(String serialNumber,User operator) throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(serialNumber);
		if(lr.getAccident() == 1){
			Accident acc = this.accService.getAccidentByOrderId(serialNumber);
			if(acc.getStatus() == 0){
				throw new ServiceException("当前订单的车辆事故尚未被处理，暂时不能结单！");
			}
		}
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_GIVEBACK_CAR){
			if(lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_SUPPLIMENT){
				throw new ServiceException("车务人员正在补填费用信息,请您稍后片刻再结单.");
			}
			throw new ServiceException("该操作只能对&nbsp;<font style='color:fuchsia;'>" + 
					this.dictionaryService.getDictionaryById(Constant.ORDER_STATUS_GIVEBACK_CAR).getRemark() +  
					"</font>&nbsp; 状态的订单执行");
		}
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + serialNumber + "'";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			
			Date dt = new Date();
			for(int i = 0; i < list.size(); i++){
				Finance f = list.get(i);
				//如果该订单中有预约费或者是押金则添加退还信息
				if((f.getFinanceType() == Constant.GROUP_F_BOOKINGFEE && f.getPayDirection() == Constant.FINANCE_IN)
						|| (f.getFinanceType() == Constant.GROUP_F_FOREGIFT  && f.getPayDirection() == Constant.FINANCE_IN)){
					if(f.getMoney() == 0){
						continue;
					}
					//先判断是否重复财务结算，避免重复添加待还财务数据
					String dSql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + 
							serialNumber + "' and o.financeType = '" + 
							f.getFinanceType() + "' and o.payDirection = '" + Constant.FINANCE_WAITING_OUT + "'";
					List<Finance> l = (List<Finance>) this.hibernateDao.getObjectsList(dSql,0, 10, true);
					if(l.size() != 0)
						continue;
					
					//添加待还财务信息
					Finance fi = new Finance();
					fi.setFinanceType(f.getFinanceType());
					if(f.getFinanceType() == Constant.GROUP_F_FOREGIFT){
						fi.setRemark("待还押金");
					}
					fi.setMoney(f.getMoney());
					fi.setPayDate(dt);
					fi.setPayDirection(Constant.FINANCE_WAITING_OUT);
					fi.setSerialNumber(serialNumber);
					fi.setTransactor(operator.getUserName());
					fi.setTransactorName(operator.getRealName());
					this.hibernateDao.saveOrUpdate(fi);
				}
				//删除待还或者是待缴的租金、续租费、超时费信息 重新计算
				if(f.getFinanceType() == Constant.GROUP_F_PRICE || f.getFinanceType() == Constant.GROUP_F_ACCIDENT ||
						f.getFinanceType() == Constant.GROUP_F_RELET_FEE || f.getFinanceType() == Constant.GROUP_F_LATE_FEE){
					if(f.getPayDirection() == Constant.FINANCE_WAITING || f.getPayDirection() == Constant.FINANCE_WAITING_OUT){
						this.hibernateDao.delete(f);
					}
				}
			}
			//添加事故处理费用
			if(lr.getAccident() == 1){
				Finance fi = new Finance();
				fi.setFinanceType(Constant.GROUP_F_ACCIDENT);
				fi.setMoney(this.accService.getAccidentFinance(serialNumber));
				fi.setPayDate(dt);
				fi.setPayDirection(Constant.FINANCE_WAITING);
				fi.setSerialNumber(serialNumber);
				fi.setTransactor(operator.getUserName());
				fi.setTransactorName(operator.getRealName());
				this.hibernateDao.save(fi);
			}
			
			Map<String,Integer> fMap = this.rentCalculate(serialNumber);
			int rent = fMap.get(FinanceConstant.ACTUAL_RENT_FEE);	//实际发生的租金总额
			
			int dis = rent - fMap.get(FinanceConstant.RENT_FEE);	//获取差价this.getRentReceived(serialNumber);
			if(dis != 0){
				if(dis < 0){											//租金尚未用完
					Finance fr = new Finance();
					fr.setFinanceType(Constant.GROUP_F_PRICE);
					fr.setMoney(Math.abs(dis));
					fr.setPayDate(dt);
					fr.setPayDirection(Constant.FINANCE_WAITING_OUT);
					fr.setRemark("待还租金");
					fr.setSerialNumber(serialNumber);
					fr.setTransactor(operator.getUserName());
					fr.setTransactorName(operator.getRealName());
					this.hibernateDao.saveOrUpdate(fr);
				}else{
					Finance fr = new Finance();
					fr.setFinanceType(Constant.GROUP_F_PRICE);
					fr.setMoney(Math.abs(dis));
					fr.setPayDate(dt);
					fr.setPayDirection(Constant.FINANCE_WAITING);
					fr.setRemark("待补缴租金");
					fr.setSerialNumber(serialNumber);
					fr.setTransactor(operator.getUserName());
					fr.setTransactorName(operator.getRealName());
					this.hibernateDao.saveOrUpdate(fr);
				}
			}
			
//			int actualReletFee = fMap.get(FinanceConstant.ACTUAL_RELET_FEE) - fMap.get(FinanceConstant.RECIEVED_RELET_FEE);//实际发生的续租费用
//			actualReletFee = actualReletFee > fMap.get(FinanceConstant.RELET_FEE) ? fMap.get(FinanceConstant.RELET_FEE) : actualReletFee;
			
			//续租费的差额
			dis = fMap.get(FinanceConstant.RECIEVED_RELET_FEE) - fMap.get(FinanceConstant.ACTUAL_RELET_FEE);
			if(dis != 0){
				Finance fr = new Finance();
				fr.setFinanceType(Constant.GROUP_F_RELET_FEE);
				fr.setMoney(Math.abs(dis));
				fr.setPayDate(dt);
				fr.setPayDirection(dis > 0 ? Constant.FINANCE_WAITING_OUT : Constant.FINANCE_WAITING);
				fr.setRemark(dis > 0 ? "待还续租费" : "待收续租费");
				fr.setSerialNumber(serialNumber);
				fr.setTransactor(operator.getUserName());
				fr.setTransactorName(operator.getRealName());
				this.hibernateDao.saveOrUpdate(fr);
			}
			
			//超时费
			dis = fMap.get(FinanceConstant.THEORY_FEE) - fMap.get(FinanceConstant.ACTUAL_RENT_FEE) - fMap.get(FinanceConstant.ACTUAL_RELET_FEE);
			if(dis > 0){
				Finance fr = new Finance();
				fr.setFinanceType(Constant.GROUP_F_LATE_FEE);
				fr.setMoney(Math.abs(dis));
				fr.setPayDate(dt);
				fr.setPayDirection(Constant.FINANCE_WAITING);
				fr.setRemark("超时费");
				fr.setSerialNumber(serialNumber);
				fr.setTransactor(operator.getUserName());
				fr.setTransactorName(operator.getRealName());
				this.hibernateDao.saveOrUpdate(fr);
			}
			
			
			return lr;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 *
	 * @TODO	车务人员追加费用
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	lf
	 * @param	operator
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void supplementFinance(String serialNumber, List<Finance> lf, User operator) throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(serialNumber);
		if(null == lr || lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_SUPPLIMENT){
			throw new ServiceException("补填财务信息失败,当前订单正处理<font style='color:fuchsia;'>" + 
					this.dictionaryService.getDictionaryById(lr.getLeaseRecordStatus()).getRemark() + "</font>状态");
		}
		try{
			//删除车务填写的费用
			this.hibernateDao.executeNativeSql("DELETE FROM T_FINANCE WHERE SERIAL_NUMBER = '" + serialNumber + "' AND FINANCE_TYPE IN(" +
					"SELECT PK_ID FROM T_DICTIONARY WHERE GROUP_ID = '" + Constant.GROUP_MONEY_TYPE_CM + "')");
			
			Date dt = new Date();
			for(int i = 0; i < lf.size(); i++){
				Finance f = lf.get(i);
				if(f.getMoney() == 0){
					continue;
				}
				f.setPayDate(dt);
				f.setPayDirection(Constant.FINANCE_WAITING);				//待缴费用
				f.setSerialNumber(lr.getSerialNumber());
				f.setTransactor(operator.getUserName());
				f.setTransactorName(operator.getRealName());
				this.hibernateDao.saveOrUpdate(f);
			}
			lr.setLeaseRecordStatus(Constant.ORDER_STATUS_GIVEBACK_CAR);
			this.hibernateDao.update(lr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	执行冻结订单操作，只有下单的操作员才能执行该操作
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	operator
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void freezeOrder(String serialNumber, User operator)
			throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(serialNumber);
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_BOOKING){
			throw new ServiceException("只有被预定的订单才能被冻结.");
		}
		if(!operator.getUserName().equals(lr.getTransactor())){
			throw new ServiceException("你没有冻结该订单的权限!");
		}
		try{
			lr.setLeaseRecordStatus(Constant.ORDER_STATUS_FREEZED);
			lr.setDateSettle(new Date());
			lr.setFinisher(operator.getUserName());
			this.hibernateDao.saveOrUpdate(lr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	根据用户获取历史订单信息
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @param	idCardNumber
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getHistoryOrderByCustomer(String idCardNumber,int pageIndex, int pageSize)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.customerIdCardNumber = '" + 
						idCardNumber + "' and o.leaseRecordStatus !='" + Constant.ORDER_STATUS_FREEZED + "' order by o.isBeyondDeadline desc,o.orderDate desc";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
				
				//添加业务结算员真实的姓名
				if(null != list.get(i).getFinisher() && !"".equals(list.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(list.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(list.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
				
				//取车、还车日期
				RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(list.get(i).getSerialNumber());
				if(null != rr){
					Date giveBackDate = rr.getDateEnd();
					arr.getJSONObject(i).put("giveBackCarDate", giveBackDate == null ? "" : giveBackDate);
					Date fetchCarDate = rr.getDateBegin();
					arr.getJSONObject(i).put("fetchCarDate", fetchCarDate == null ? "" : fetchCarDate);
				}
				
				//租赁模式
				arr.getJSONObject(i).put("leaseModelRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());
				if(list.get(i).getFetchOrgId() != 0){
					arr.getJSONObject(i).put("fetchDeptName", this.organizationService.getOrganizationById(list.get(i).getFetchOrgId()).getDepartmentName());
				}
				if(list.get(i).getGivebackOrgId() != 0){
					arr.getJSONObject(i).put("givebackDeptName", this.organizationService.getOrganizationById(list.get(i).getGivebackOrgId()).getDepartmentName());
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	添加续租单，只有发车状态的订单才能续租
	 * @date	2011-5-14
	 * @author	肖乾斌
	 * @param	reletDays	天数
	 * @param	orderFormId 单据号
	 * @param	operator 	业务人员
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addReletBusiness(int reletDays,String orderFormId,User operator) throws ServiceException {
		
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(orderFormId);
		if(lr == null){
			throw new ServiceException("单据号错误，无法办理续租");
		}
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_PROCESSING){
			throw new ServiceException("该操作只能对&nbsp;<font style='color:fuchsia;'>" + 
					this.dictionaryService.getDictionaryById(Constant.ORDER_STATUS_PROCESSING).getRemark() +  
					"</font>&nbsp; 状态的订单执行");
		}
		try{
			Date dtt = new Date();										//业务办理日期
			Relet relet = new Relet();
			relet.setBussinessDate(dtt);
			relet.setOrderFormId(orderFormId);
			relet.setTransactor(operator.getUserName());
			relet.setDateBegin(lr.getDateRelet() == null ? lr.getDateEnd() : lr.getDateRelet());
			Date dt = new Date();
			dt.setTime(lr.getDateRelet() == null ? (lr.getDateEnd().getTime() + ((long)reletDays) * 1000 * 60 * 60 * 24) : (lr.getDateRelet().getTime() + ((long)reletDays) * 1000 * 60 * 60 * 24));
			relet.setDateEnd(dt);
			this.reletService.addRelet(relet);				//添加续租单
			lr.setDateRelet(dt);
			this.hibernateDao.update(lr);					//修改续租日期
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}
	
	

	/**
	 * 
	 * @TODO	租金计算
	 * @author 	肖乾斌
	 * @date 	2011-5-20
	 * @param 	orderFormID
	 * @throws 	ServiceException
	 * @return 	Map<String,Integer>
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Map<String,Integer> rentCalculate(String orderFormID) throws ServiceException {
		Map<String,Integer> map = new HashMap<String,Integer>();
		RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(orderFormID);
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(orderFormID);
		long b = new Date().getTime();
		if(null != rr && null != rr.getDateEnd()){
			b = rr.getDateEnd().getTime();
		}
		long _day  = (b - rr.getDateBegin().getTime())/1000/60/60/24;
		long minutes = (b - rr.getDateBegin().getTime())/1000/60 + ((b - rr.getDateBegin().getTime()/1000)%60 > 0 ? 1 : 0) - _day * 24 * 60;
		long hours = 0;
		if(minutes >= 60 * 6 && _day != 0){
			_day += 1;
		}else{
			if(_day == 0){
				_day = 1;
			}else{
				hours = minutes / 60 + ((minutes % 60) > 0 ? 1 : 0);	
			}
		}
		
		//已收租金
//		map.put(FinanceConstant.RENT_FEE, (int)(_day) * lr.getPrice());		//由于租金允许客户修改
		map.put(FinanceConstant.RENT_FEE, this.financeService.getPrePayRent(orderFormID));	//预付租金
		//理论费用
		map.put(FinanceConstant.THEORY_FEE, (_day == 0 ? lr.getPrice() : ((int)(_day) * lr.getPrice()  + (int)hours * lr.getPricePerHour())));				
		
		
		//理论续租费
		map.put(FinanceConstant.RELET_FEE, 0);
		//实收续租费
		map.put(FinanceConstant.RECIEVED_RELET_FEE, 0);
		
		//实际发生的租金
		map.put(FinanceConstant.ACTUAL_RENT_FEE, 0);
		
		//实际发生的续租费
		map.put(FinanceConstant.ACTUAL_RELET_FEE, 0);

		
//		Date dtb = rr.getDateBegin();
//		Date dte = rr.getDateEnd();
//		if(null == dte){
//			dte = new Date();
//		}
		try{
			long time = lr.getDateEnd().getTime() - lr.getDateBegin().getTime();
			int day = (int) (time/1000/60/60/24);
			map.put(FinanceConstant.THEORY_RENT_FEE,(day != 0 ? day : 1) * lr.getPrice());
			if(day * lr.getPrice() <= map.get(FinanceConstant.THEORY_FEE)){
				map.put(FinanceConstant.ACTUAL_RENT_FEE,(day != 0 ? day : 1) * lr.getPrice());
			}else{
				map.put(FinanceConstant.ACTUAL_RENT_FEE,map.get(FinanceConstant.THEORY_FEE));
			}
			
//			if(minutes <= (24 * 60)){
//				map.put(FinanceConstant.ACTUAL_RENT_FEE, lr.getPrice());
//			}else{
//				long days = minutes / (60 * 24);
//				hours = 0;
//				minutes -= days * 24 * 60;
//				if(minutes >= 60 * 6){
//					days += 1;
//				}else{
//					hours = minutes / 60 + ((minutes % 60) > 0 ? 1 : 0);
//				}
//				map.put(FinanceConstant.ACTUAL_RENT_FEE,(int)(days * lr.getPrice() + hours * lr.getPricePerHour()));
//			}
			if(null != lr.getDateRelet()){
				_day  = (lr.getDateRelet().getTime() - lr.getDateEnd().getTime())/1000/60/60/24;
				//理论续租费
				map.put(FinanceConstant.RELET_FEE, (int)(_day) * lr.getPrice());
				
				//实收续租费
				map.put(FinanceConstant.RECIEVED_RELET_FEE, this.getRecievedReletFee(orderFormID));

				//理论产生费用  - 理论租金 ==（实际发生的续租费 + 超时费）
				day = (int) ((lr.getDateEnd().getTime() - lr.getDateBegin().getTime())/1000/60/60/24);
				day = day == 0 ? 1 : day;
				int am = map.get(FinanceConstant.THEORY_FEE) - day * lr.getPrice();
				
				//实际该付续租租金
				if(am <= 0){
					map.put(FinanceConstant.ACTUAL_RELET_FEE,0);
				}else{
					if(am > (int)(_day) * lr.getPrice()){
						map.put(FinanceConstant.ACTUAL_RELET_FEE,(int) ((_day) * lr.getPrice()));
					}else{
						map.put(FinanceConstant.ACTUAL_RELET_FEE,am);
					}
				}
			}else{
				//理论续租费
				map.put(FinanceConstant.RELET_FEE, 0);
				//实收续租费
				map.put(FinanceConstant.RECIEVED_RELET_FEE, 0);
			}
			return map;
		}catch(Exception e){
			log.error(e.getMessage(), e); 
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 获取已收续租费
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @param 	orderId
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public int getRecievedReletFee(String orderId) throws ServiceException{
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + 
						orderId + "' and o.financeType = '" + Constant.GROUP_F_RELET_FEE + "' and o.payDirection = '" + Constant.FINANCE_IN + "'" ;
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsByHsql(sql);
			int total = 0;
			for(int i = 0; i < list.size(); i++){
				total += list.get(i).getMoney();
			}
			return total;
		}catch(Exception e){
			log.error(e.getMessage(), e); 
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	根据车牌查找相应的收车单来补填费用信息
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	carId
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	LeaseRecord
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public LeaseRecord getLeaseRecordToSupplement(String carId, User operator) throws ServiceException {
		String sql = "select o from " + LeaseRecord.class.getName() + " o where o.carId = '" + carId + 
						"' and (o.leaseRecordStatus = '" + Constant.ORDER_STATUS_GIVEBACK_CAR + 
						"' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_SUPPLIMENT + "')";
		List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
		if(list.size() == 0){
			throw new ServiceException("没有找到可以补填费用的定单信息，该单可能已经被结算，请您尽快联系相关人员。");
		}
		RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(list.get(0).getSerialNumber());
		if(!rr.getEndTransactor().equals(operator.getUserName())){
			throw new ServiceException("费用补填操作失败，只有收车人本人才能对其订单进行费用补填操作!");
		}
		return list.get(0);
	}
	/**
	 * 
	 * @TODO	根据车牌查找相应的收车单来补填费用信息
	 * @author 	肖乾斌
	 * @date 	2011-6-23
	 * @param 	carId
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getLeaseRecordsToSupplement(String carId, User operator) throws ServiceException {
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o, " + RunRecord.class.getName() +  
				" r where o.carId = '" + carId + "' and o.serialNumber = r.orderFormSerialNumber and " +
				"r.endTransactor = '" + operator.getUserName() + 
				"' and (o.leaseRecordStatus = '" + Constant.ORDER_STATUS_GIVEBACK_CAR + 
				"' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_SUPPLIMENT + "')";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, 0, 100, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("statusRemark", this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
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
	 * 
	 * @TODO	修改订单状态
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	orderFormId
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void modifyLeaseRecordStatus(String orderFormId, int status) throws ServiceException {
		try{
			LeaseRecord lr = this.getLeaseRecordBySerialNumber(orderFormId);
			if(null == lr)
				return;
			lr.setLeaseRecordStatus(status);
			this.hibernateDao.update(lr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 
	 * @TODO	获取已收的租金
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	orderFormId
	 * @throws 	ServiceException
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public int getRentReceived(String orderFormId) throws ServiceException {
		try{
			String sql = "select o from " + Finance.class.getName() + " o where o.serialNumber = '" + orderFormId + "'";
			List<Finance> list = (List<Finance>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			int money = 0;
			for(Finance f : list){
				if(f.getFinanceType() == Constant.GROUP_F_PRICE && f.getPayDirection() == Constant.FINANCE_IN){
					money += f.getMoney();
				}
			}
			return money;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	优惠
	 * @author 	肖乾斌
	 * @date 	2011-5-26
	 * @param 	orderFormId
	 * @param 	money
	 * @param 	remark		优惠的原因
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void discountOrder(String orderFormId, int money,String remark, User operator)
			throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(orderFormId);
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_GIVEBACK_CAR){
			if(lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_SUPPLIMENT){
				throw new ServiceException("车务人员正在补填费用信息,请您稍后片刻再结单.");
			}
			throw new ServiceException("该操作只能对&nbsp;<font style='color:fuchsia;'>" + 
					this.dictionaryService.getDictionaryById(Constant.ORDER_STATUS_GIVEBACK_CAR).getRemark() +  
					"</font>&nbsp; 状态的订单执行");
		}
		try{
			Finance f = new Finance();
			f.setFinanceType(Constant.GROUP_F_DISCOUNT);
			f.setMoney(money);
			f.setPayDate(new Date());
			f.setPayDirection(Constant.FINANCE_WAITING_OUT);
			f.setPayType(Constant.SETTLEMENT);
			f.setRemark(remark);
			f.setSerialNumber(orderFormId);
			f.setTransactor(operator.getUserName());
			f.setTransactorName(operator.getRealName());
			f.setOrganizationId(operator.getDepartmentId());
			this.hibernateDao.save(f);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @TODO	删除优惠
	 * @author 	肖乾斌
	 * @date 	2011-5-27
	 * @param 	id
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void removeDiscount(int id, User operator)throws ServiceException{
		Finance f = (Finance) this.hibernateDao.getObjectByID(Finance.class, id);
		if(f == null){
			return;
		}
		if(null == f.getTransactor() || "".equals(f.getTransactor()) || !operator.getUserName().equals(f.getTransactor())){
			throw new ServiceException("删除优惠失败，只能删除您自己创建的优惠数据!");
		}
		if(f.getFinanceType() != Constant.GROUP_F_DISCOUNT){
			throw new ServiceException("删除优惠失败，不能删除非优惠数据!");
		}
		try{
			this.hibernateDao.delete(f);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	获取被优惠过的订单
	 * @author 	肖乾斌
	 * @date 	2011-6-5
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getDiscountedLeaseRecord(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select distinct o from " + LeaseRecord.class.getName() + " o, " + Finance.class.getName() + " f where "
						+ "o.serialNumber = f.serialNumber and f.financeType = '" + Constant.GROUP_F_DISCOUNT + "'";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
				
				//添加业务结算员真实的姓名
				if(null != list.get(i).getFinisher() && !"".equals(list.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(list.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(list.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());
				
				//取车、还车日期
				RunRecord rr = this.runRecordService.getRunRecordByOrderFormID(list.get(i).getSerialNumber());
				if(null != rr){
					Date giveBackDate = rr.getDateEnd();
					arr.getJSONObject(i).put("giveBackCarDate", giveBackDate == null ? "" : giveBackDate);
					Date fetchCarDate = rr.getDateBegin();
					arr.getJSONObject(i).put("fetchCarDate", fetchCarDate == null ? "" : fetchCarDate);
				}
				 
				//租赁模式
				arr.getJSONObject(i).put("leaseModelRemark", 
						this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());
				if(list.get(i).getFetchOrgId() != 0){
					arr.getJSONObject(i).put("fetchDeptName", this.organizationService.getOrganizationById(list.get(i).getFetchOrgId()).getDepartmentName());
				}
				if(list.get(i).getGivebackOrgId() != 0){
					arr.getJSONObject(i).put("givebackDeptName", this.organizationService.getOrganizationById(list.get(i).getGivebackOrgId()).getDepartmentName());
				}
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); 
			throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 补缴续租费
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @param 	money
	 * @param 	user
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void supplyReletFee(int money, String orderId,User user) throws ServiceException {
		LeaseRecord lr = this.getLeaseRecordBySerialNumber(orderId);
		if(lr.getDateRelet() == null){
			throw new ServiceException("续缴费用失败，该订单没有发生续租业务。");
		}
		if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_GIVEBACK_CAR && 
				lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_SUPPLIMENT &&
				lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_PROCESSING){
			throw new ServiceException("续缴费用失败，当前订单状态无法进行费用续缴。");
		}
		if(lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_GIVEBACK_CAR || lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_SUPPLIMENT){
			//已收车，只补差价
			Map<String,Integer> fMap = this.rentCalculate(orderId);
			int rent = fMap.get(FinanceConstant.ACTUAL_RENT_FEE);	//实际发生的租金总额
			int dis = rent - fMap.get(FinanceConstant.RENT_FEE);	//获取差价this.getRentReceived(serialNumber);
			if(dis > 0){											//租金已超出实际支付
				int actualReletFee = rent - fMap.get(FinanceConstant.RENT_FEE);//实际发生的续租费用
				actualReletFee = actualReletFee > fMap.get(FinanceConstant.RELET_FEE) ? fMap.get(FinanceConstant.RELET_FEE) : actualReletFee;
				//续租费的差额
				dis = fMap.get(FinanceConstant.RECIEVED_RELET_FEE) - actualReletFee;
				if(dis >= 0){
					throw new ServiceException("续租费尚有剩余，毋须缴纳续租费。");
				}
				if(Math.abs(dis) != money){
					throw new ServiceException("补缴金额应该是￥" + Math.abs(dis));
				}else{
					try{
						Finance f = new Finance();
						f.setFinanceType(Constant.GROUP_F_RELET_FEE);
						f.setMoney(money);
						f.setOrganizationId(user.getDepartmentId());
						f.setPayDate(new Date());
						f.setPayDirection(Constant.FINANCE_IN);
						f.setPayType(Constant.PREPAY);
						f.setRemark("续租费");
						f.setSerialNumber(orderId);
						f.setTransactor(user.getUserName());
						f.setTransactorOver(user.getUserName());
						f.setTransactorName(user.getRealName());
						f.setTransactorOverName(user.getRealName());
						this.hibernateDao.save(f);
					}catch(Exception e){
						log.error(e.getMessage(), e);
						throw new ServiceException(e.getMessage());
					}
				}
			}
		}else if(lr.getLeaseRecordStatus() == Constant.ORDER_STATUS_PROCESSING){
			//尚未收车，想缴多少缴多少
			try{
				Finance f = new Finance();
				f.setFinanceType(Constant.GROUP_F_RELET_FEE);
				f.setMoney(money);
				f.setOrganizationId(user.getDepartmentId());
				f.setPayDate(new Date());
				f.setPayDirection(Constant.FINANCE_IN);
				f.setPayType(Constant.PREPAY);
				f.setRemark("续租费");
				f.setSerialNumber(orderId);
				f.setTransactor(user.getUserName());
				f.setTransactorOver(user.getUserName());
				f.setTransactorName(user.getRealName());
				f.setTransactorOverName(user.getRealName());
				this.hibernateDao.save(f);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}

		}
		
	}
}
