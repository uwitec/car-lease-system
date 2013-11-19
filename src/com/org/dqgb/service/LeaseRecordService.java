/**  
 * @Filename:    LeaseRecordService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午08:57:44  
 *
 */  

package com.org.dqgb.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface LeaseRecordService {
	
	/**
	 *
	 * @TODO	获取订单信息，按时间降序排列
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	condition 查询条件
	 * @param	orderBy 排序条件
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public JSONObject getOrderForms(int pageIndex,int pageSize,Map<String,Object> condition,Map<String,String> orderBy) throws ServiceException;
	
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
	public abstract JSONObject getOrderFormBySerialNumber(String serialNumber) throws ServiceException; 
	
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
	public abstract LeaseRecord getLeaseRecordBySerialNumber(String serialNumber) throws ServiceException; 

	
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
	public abstract JSONObject getOrderInfoToPrint(String serialNumber) throws ServiceException; 
	
	
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
	public void addOrderForm(LeaseRecord lr, List<Finance> finances, User operator) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改订单，该操作只有在订单状态为预填状态才有效
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	lr
	 * @param	finances 财务明细
	 * @param	currentOperator
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public void modifyOrderForm(LeaseRecord lr, List<Finance> finances, User currentOperator) throws ServiceException;
	
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
	public void deleteLeaseRecord(String serialNumber,User currentOperator) throws ServiceException;
	
	/**
	 *
	 * @TODO	生成序列号
	 * @date	2011-4-29
	 * @author	肖乾斌
	 * @param 	departmentId
	 * @return  String
	 *
	 */
	public String generateSerialNumber(int departmentId)throws ServiceException;
	
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
	public JSONObject getBookRecord(int pageIndex, int pageSize, String carId) throws ServiceException;
	
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
	public void changeOrderFormToBooking(String serialNumber,User currentOperator) throws ServiceException;
	
	/**
	 *
	 * @TODO	订单结算，只有当订单处于"已还车"状态的时候该操作才有效
	 * @date	2011-5-1
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	operator		业务结算员
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public abstract void settleOrderForm(String serialNumber, User operator)throws ServiceException;
	
	/**
	 *
	 * @TODO	从车务部发车
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	serialNumber	发车单
	 * @param	operator		发车业务操作员
	 * @param	sender			发车人
	 * @param	maker			整备人
	 * @param	leaveTable		发车单path
	 * @throws  ServiceException
	 *
	 */
	public abstract void startOut(String serialNumber,String sender,String maker,User operator,String leaveTable)throws ServiceException;
	
	/**
	 *
	 * @TODO	收车
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	carId
	 * @param	operator
	 * @param	lf
	 * @param	accident
	 * @param	km	当前行驶里程
	 * @param	backTablePath	收车单
	 * @throws  ServiceException
	 *
	 */
	public abstract void giveBackCar(String carId,User operator,List<Finance> lf, int km,String backTablePath,int accident)throws ServiceException;
	
	/**
	 *
	 * @TODO	车务人员追加费用,该操作后订单变为换车状态，前台可结算
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @param	serialNumber
	 * @param	lf
	 * @param	operator
	 * @throws  ServiceException
	 * @return  void
	 *
	 */
	public abstract void supplementFinance(String serialNumber,List<Finance> lf,User operator) throws ServiceException;
	
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
	public abstract LeaseRecord financeSettle(String serialNumber, User operator) throws ServiceException;
	
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
	public abstract void freezeOrder(String serialNumber, User operator) throws ServiceException;
	
	
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
	public abstract JSONObject getHistoryOrderByCustomer(String idCardNumber,int pageIndex, int pageSize) throws ServiceException;
	
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
	public abstract void addReletBusiness(int reletDays,String orderFormId, User operator)throws ServiceException;
	
	/**
	 * 
	 * @TODO	租金计算
	 * @author 	肖乾斌
	 * @date 	2011-5-20
	 * @param 	orderFormID
	 * @throws 	ServiceException
	 * @return 	Map<String,Integer>
	 */
	public abstract Map<String,Integer> rentCalculate(String orderFormID)throws ServiceException;
	
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
	public abstract LeaseRecord getLeaseRecordToSupplement(String carId,User operator)throws ServiceException;
	
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
	public abstract JSONObject getLeaseRecordsToSupplement(String carId,User operator)throws ServiceException;
	/**
	 * 
	 * @TODO	修改订单状态
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	orderFormId
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void modifyLeaseRecordStatus(String orderFormId,int status) throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取已收的租金
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @param 	orderFormId
	 * @throws 	ServiceException
	 * @return 	int
	 */
	public abstract int getRentReceived(String orderFormId)throws ServiceException;
	
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
	public abstract void discountOrder(String orderFormId,int money,String remark,User operator)throws ServiceException;
	
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
	public abstract void removeDiscount(int id,User operator)throws ServiceException;
	
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
	public abstract JSONObject getDiscountedLeaseRecord(int pageIndex,int pageSize)throws ServiceException;
	
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
	public abstract void supplyReletFee(int money,String orderId, User user)throws ServiceException;
	
}
