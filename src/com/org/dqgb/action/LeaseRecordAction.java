/**  
 * @Filename:    LeaseRecordAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午09:06:33  
 *
 */  

package com.org.dqgb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.Relet;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.FinanceService;
import com.org.dqgb.service.LeaseRecordService;
import com.org.dqgb.service.ReletService;
import com.org.dqgb.util.DateUtil;

@Controller("LeaseRecordAction") @Scope("prototype")
@SuppressWarnings("serial")
public class LeaseRecordAction extends PrimaryAction {
	
	@Resource @Qualifier("LeaseRecordServiceImpl")
	public LeaseRecordService leaseRecordService;

	@Resource @Qualifier("FinanceServiceImpl")
	public FinanceService financeService;

	@Resource @Qualifier("CarServiceImpl")
	public CarService carService;
	
	@Resource(name="ReletServiceImpl")
	public ReletService reletService;
	
	@Resource(name="DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	public LeaseRecord leaseRecords;
	
	public void setLeaseRecords(LeaseRecord leaseRecords) {
		this.leaseRecords = leaseRecords;
	}
	
	/**
	 *
	 * @TODO	获取订单记录信息
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getLeaseRecord(){
		try{
			Map<String,Object> condition = new HashMap<String, Object>();
			Map<String,String> orderBy = new HashMap<String, String>();
			String order = this.getHttpServletRequest().getParameter("order");
			String direction = this.getHttpServletRequest().getParameter("direction");
			if(!"".equals(order) && null != order){
				orderBy.put(order, direction);
			}
			if(orderBy.size() == 0)
				orderBy.put("orderDate", "desc");				//默认按下单时间 降序排列
			String carId = this.getHttpServletRequest().getParameter("carId");
			if(!"".equals(carId) && null != carId){
				condition.put("carId", carId);
			}
			String serialNumber = this.getHttpServletRequest().getParameter("serialNumber");
			if(!"".equals(serialNumber) && null != serialNumber){
				condition.put("serialNumber", serialNumber);
			}
			JSONObject json = this.leaseRecordService.getOrderForms(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")),condition,orderBy);
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 获取客户消费历史
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	public String getCustomerOrder(){
		try{
			Map<String,Object> condition = new HashMap<String, Object>();
			Map<String,String> orderBy = new HashMap<String, String>();
			if(orderBy.size() == 0)
				orderBy.put("orderDate", "desc");				//默认按下单时间 降序排列
			String idCardNumber = this.getHttpServletRequest().getParameter("idCardNumber");
			if(!"".equals(idCardNumber) && null != idCardNumber){
				condition.put("customerIdCardNumber", idCardNumber);
			}
			condition.put("leaseRecordStatus", Constant.ORDER_STATUS_OVER_NORMALLY);
			JSONObject json = this.leaseRecordService.getOrderForms(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")),condition,orderBy);
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	冻结订单
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String freezeOrder(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.freezeOrder(getHttpServletRequest().getParameter("serialNumber"),user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}	
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	跳转到财务结算页面
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoFinanceSettlePage(){
		try{
			this.getHttpSession().setAttribute("LeaseReocordSerialNumber", 
					this.getHttpServletRequest().getParameter("serialNumber"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	财务结算向数据库中添加财务信息
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String financeSettle(){
		try{
			this.leaseRecordService.financeSettle(this.getHttpServletRequest().getParameter("serialNumber"), 
					this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	下订单
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String AddOrderForm(){
		try{
			this.leaseRecords.setOrderDate(new Date());
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecords.setTransactor(user.getUserName());
			String price = this.getHttpServletRequest().getParameter("price");
			String priceRemark = this.getHttpServletRequest().getParameter("priceRemark");
			String bookingFee = this.getHttpServletRequest().getParameter("bookingFee");
			String bookingFeeRemark = this.getHttpServletRequest().getParameter("bookingFeeRemark");
			String foregift = this.getHttpServletRequest().getParameter("foregift");
			String foregiftRemark = this.getHttpServletRequest().getParameter("foregiftRemark");
			List<Finance> list = new ArrayList<Finance>();
			Finance fp = new Finance();
			fp.setMoney(Integer.parseInt(price));
			fp.setFinanceType(Constant.GROUP_F_PRICE);
			fp.setRemark(priceRemark);
			fp.setTransactor(user.getUserName());
			fp.setTransactorName(user.getRealName());
			fp.setTransactorOver(user.getUserName());
			fp.setTransactorOverName(user.getRealName());
			list.add(fp);
			
			Finance fp1 = new Finance();
			fp1.setMoney(Integer.parseInt(bookingFee));
			fp1.setRemark(bookingFeeRemark);
			fp1.setFinanceType(Constant.GROUP_F_BOOKINGFEE);
			fp1.setTransactor(user.getUserName());
			fp1.setTransactorName(user.getRealName());
			fp1.setTransactorOver(user.getUserName());
			fp1.setTransactorOverName(user.getRealName());
			list.add(fp1);
			
			Finance fp2 = new Finance();
			fp2.setMoney(Integer.parseInt(foregift));
			fp2.setFinanceType(Constant.GROUP_F_FOREGIFT);
			fp2.setRemark(foregiftRemark);
			fp2.setTransactor(user.getUserName());
			fp2.setTransactorName(user.getRealName());
			fp2.setTransactorOver(user.getUserName());
			fp2.setTransactorOverName(user.getRealName());
			list.add(fp2);
			this.leaseRecordService.addOrderForm(this.leaseRecords,list,user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改订单信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyOrderForm(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			String price = this.getHttpServletRequest().getParameter("price");
			String priceRemark = this.getHttpServletRequest().getParameter("priceRemark");
			String bookingFee = this.getHttpServletRequest().getParameter("bookingFee");
			String bookingFeeRemark = this.getHttpServletRequest().getParameter("bookingFeeRemark");
			String foregift = this.getHttpServletRequest().getParameter("foregift");
			String foregiftRemark = this.getHttpServletRequest().getParameter("foregiftRemark");
			List<Finance> list = new ArrayList<Finance>();
			
			//租金
			Finance fp = new Finance();
			fp.setMoney(Integer.parseInt(price));
			fp.setFinanceType(Constant.GROUP_F_PRICE);
			fp.setRemark(priceRemark);
			fp.setTransactor(user.getUserName());
			fp.setTransactorName(user.getRealName());
			fp.setTransactorOver(user.getUserName());
			fp.setTransactorOverName(user.getRealName());
			list.add(fp);
			
			//预约金
			Finance fp1 = new Finance();
			fp1.setMoney(Integer.parseInt(bookingFee));
			fp1.setRemark(bookingFeeRemark);
			fp1.setFinanceType(Constant.GROUP_F_BOOKINGFEE);
			fp1.setTransactor(user.getUserName());
			fp1.setTransactorName(user.getRealName());
			fp1.setTransactorOver(user.getUserName());
			fp1.setTransactorOverName(user.getRealName());
			list.add(fp1);
			
			//押金
			Finance fp2 = new Finance();
			fp2.setMoney(Integer.parseInt(foregift));
			fp2.setFinanceType(Constant.GROUP_F_FOREGIFT);
			fp2.setRemark(foregiftRemark);
			fp2.setTransactor(user.getUserName());
			fp2.setTransactorName(user.getRealName());
			fp2.setTransactorOver(user.getUserName());
			fp2.setTransactorOverName(user.getRealName());
			list.add(fp2);
			this.leaseRecordService.modifyOrderForm(this.leaseRecords,list,user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	订单结算
	 * @date	2011-5-1
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String settleOrderForm(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.settleOrderForm(this.getHttpServletRequest().getParameter("serialNumber"), user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	车务管理人员追加费用 
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String supplementFinance(){
		try{
			JSONArray arr = JSONArray.fromObject(this.getHttpServletRequest().getParameter("input"));
			List<Finance> list = new ArrayList<Finance>();
			for(int i = 0; i < arr.size(); i++){
				Finance f = (Finance) JSONObject.toBean(arr.getJSONObject(i), Finance.class);
				list.add(f);
			}
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.supplementFinance(this.getHttpServletRequest().getParameter("serialNumber"),
					list,user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	收车
	 * @date	2011-5-1
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String giveBackCar(){
		try{
			JSONArray arr = JSONArray.fromObject(this.getHttpServletRequest().getParameter("input"));
			List<Finance> list = new ArrayList<Finance>();
			for(int i = 0; i < arr.size(); i++){
				Finance f = (Finance) JSONObject.toBean(arr.getJSONObject(i), Finance.class);
				list.add(f);
			}
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.giveBackCar(getHttpServletRequest().getParameter("carId"), user,list,
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("km")),
					this.getHttpServletRequest().getParameter("backTable"),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("accident")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	把订单状态从预填状态修改为预定状态 
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String changeOrderFormToBooking(){
		try{
			User currentOperator = (User) getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.changeOrderFormToBooking(
					this.getHttpServletRequest().getParameter("serialNumber"), currentOperator);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	取消预约单
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deleteLeaseRecord(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.deleteLeaseRecord(this.getHttpServletRequest().getParameter("serialNumber"),user);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取指定车辆的预约单信息
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getBookRecord(){
		try{
			JSONObject json = this.leaseRecordService.getBookRecord(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")),
					getHttpServletRequest().getParameter("carId"));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	跳转到查看订单预约记录界面
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoBrowseOrderFormsPages(){
		try {
			this.getHttpSession().setAttribute("carId",  new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		} catch (Exception e) {
			log.error("跳转到查看订单预约记录界面时发生错误：" + e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	跳转到费用补填界面
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @return 	String
	 */
	public String gotoSupplementFinance(){
		try {
//			String carId = new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8");
//			LeaseRecord lr = this.leaseRecordService.getLeaseRecordToSupplement(carId, this.getCurrentUser());
			String serialNumber = this.getHttpServletRequest().getParameter("serialNumber");
			this.leaseRecordService.modifyLeaseRecordStatus(serialNumber, Constant.ORDER_STATUS_SUPPLIMENT);
			this.getHttpServletRequest().setAttribute("orderFormId",  serialNumber);
		} catch (Exception e) {
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return "failed";
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取补填费用的订单
	 * @author 	肖乾斌
	 * @date 	2011-6-23
	 * @return 	String
	 */
	public String getLeaseRecordsToSupplement(){
		try{
			String carId = this.getHttpServletRequest().getParameter("carId");//new String(.getBytes("8859_1"), "UTF-8");
			this.output.put(Constant.DATA, this.leaseRecordService.getLeaseRecordsToSupplement(
					carId,this.getCurrentUser()));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	根据订单号获取订单信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getOrderFormBySerialNumber(){
		try{
			output.put(Constant.DATA, this.leaseRecordService.getOrderFormBySerialNumber(
				this.getHttpServletRequest().getParameter("serialNumber")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}

	/**
	 *
	 * @TODO	跳转到添加订单预约记录界面
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoAddOrderFormPage(){
		try {
			this.getHttpSession().setAttribute("carId",  new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		} catch (Exception e) {
			log.error("跳转到添加订单预约记录界面时发生错误：" + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到打印订单界面
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoPrintOrderForm(){
		try {
			this.getHttpSession().setAttribute("serialNumber",  new String(this.getHttpServletRequest().getParameter("serialNumber").getBytes("8859_1"), "UTF-8"));
		} catch (Exception e) {
			log.error("跳转到打印预约订单界面时发生错误：" + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取单据的所有详细信息供打印使用
	 * @date	2011-5-1
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getOrderPrintInfor(){
		try{
			output.put(Constant.DATA, this.leaseRecordService.getOrderInfoToPrint(
				this.getHttpServletRequest().getParameter("serialNumber")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	跳转到添加订单预约记录界面
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyOrderFormPage(){
		try {
			String s = new String(this.getHttpServletRequest().getParameter("serialNumber").getBytes("8859_1"), "UTF-8");
			LeaseRecord lr = this.leaseRecordService.getLeaseRecordBySerialNumber(s);
			this.getHttpSession().setAttribute("serialNumber",  s);
			this.getHttpSession().setAttribute("carId",  lr.getCarId());
		} catch (Exception e) {
			log.error("跳转到修改订单预约记录界面时发生错误：" + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	发车
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String startOut(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.startOut(getHttpServletRequest().getParameter("serialNumber"),
					this.getHttpServletRequest().getParameter("sender"),
					this.getHttpServletRequest().getParameter("maker"),	
					user,
					this.getHttpServletRequest().getParameter("leaveTable"));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取顾客的历史订单信息
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getHistoryOrderByCustomer(){
		try{
			output.put(Constant.DATA, this.leaseRecordService.getHistoryOrderByCustomer(
					this.getHttpServletRequest().getParameter("idCardNumber"),
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到历史订单界面
	 * @date	2011-5-4
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoHistoryOrderPage(){
		try {
			this.getHttpSession().setAttribute("idCardNumber",  
					this.getHttpServletRequest().getParameter("idCardNumber"));
		} catch (Exception e) {
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	办理续租
	 * @date	2011-5-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addReletBusiness(){
		try {
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.leaseRecordService.addReletBusiness(Integer.parseInt(this.getHttpServletRequest().getParameter("reletDays")), 
					this.getHttpServletRequest().getParameter("orderFormId"), user);
		} catch (Exception e) {
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到续租明细界面
	 * @date	2011-5-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoReletDetailsPage(){
		try{
			this.getHttpSession().setAttribute("ORDERFORMID", 
					this.getHttpServletRequest().getParameter("serialNumber"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取续租明细信息
	 * @date	2011-5-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getReletDetails(){
		try{
			List<Relet> list =  this.reletService.getReletByOrderFormId(
					this.getHttpServletRequest().getParameter("serialNumber"));
			output.put(Constant.DATA,list);
			output.put(Constant.TOTAL_SIZE,list.size());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取车务财务费用类型
	 * @date	2011-5-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCMFeeType(){
		try{
			List<Dictionary> list =  this.dictionaryService.getDictionaryByGroupId(Constant.GROUP_MONEY_TYPE_CM, 
					0, 1000);
			output.put(Constant.DATA,list);
			output.put(Constant.TOTAL_SIZE,list.size());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	获取车务填写的车务费用信息
	 * @author 	肖乾斌
	 * @date 	2011-5-21
	 * @return 	String
	 */
	public String getCMFee(){
		try{
			List<Finance> list = this.financeService.getCarManagerFinances(this.getHttpServletRequest().getParameter("serialNumber"), this.getCurrentUser());
			output.put(Constant.DATA,list);
			output.put(Constant.TOTAL_SIZE,list.size());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到财务结算单打印界面	
	 * @date	2011-5-16
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoFinanceSettlePrintPage(){
		try{
			this.getHttpSession().setAttribute("serialNumber",
					this.getHttpServletRequest().getParameter("serialNumber"));
			LeaseRecord lr = this.leaseRecordService.getLeaseRecordBySerialNumber(this.getHttpServletRequest().getParameter("serialNumber"));
			if(lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_OVER_NORMALLY && lr.getLeaseRecordStatus() != Constant.ORDER_STATUS_OVER_BEYOND_DL){
				this.getHttpServletRequest().setAttribute(Constant.ERROR, "不能给没有结算的订单出示结算单！");
				return "failed";
			}
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	打折
	 * @author 	肖乾斌
	 * @date 	2011-5-27
	 * @return 	String
	 */
	public String discountOrder(){
		try{
			this.leaseRecordService.discountOrder(this.getHttpServletRequest().getParameter("serialNumber"),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("money")), 
					this.getHttpServletRequest().getParameter("remark"), this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	删除一条优惠数据
	 * @author 	肖乾斌
	 * @date 	2011-5-27
	 * @return 	String
	 * 
	 */
	public String removeDiscount(){
		try{
			this.leaseRecordService.removeDiscount(NumberUtils.toInt(
					this.getHttpServletRequest().getParameter("id")),this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到顶单管理界面
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @return 	String
	 */
	public String gotoLeaseRecordPage(){
		try{
			this.getHttpServletRequest().setAttribute("date", DateUtil.formatDate(new Date(),"yyyy-M-d"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute("ERROR", e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取优惠过的订单
	 * @author 	肖乾斌
	 * @date 	2011-6-5
	 * @return 	String
	 */
	public String getDiscountedLeaseRecord(){
		try{
			this.output.put(Constant.DATA, this.leaseRecordService.getDiscountedLeaseRecord(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	根据订单号查询财务信息
	 * @author 	肖乾斌
	 * @date 	2011-6-5
	 * @return 	String
	 */
	public String getFinanceByLeaseRecord(){
		try{
			this.output.put(Constant.DATA, 
					this.financeService.getFinanceByOrderFormId(
							this.getHttpServletRequest().getParameter("serialNumber")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	跳转到订单财务信息界面
	 * @author 	肖乾斌
	 * @date 	2011-6-5
	 * @return 	String
	 */
	public String gotoOrderFinancePage(){
		try{
			this.getHttpServletRequest().setAttribute("serialNumber", 
					this.getHttpServletRequest().getParameter("serialNumber"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute("ERROR", e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	
	/**
	 * 补缴续租费
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @return 	String
	 */
	public String supplyReletFee(){
		try{
			this.leaseRecordService.supplyReletFee(NumberUtils.toInt(this.getHttpServletRequest().getParameter("money")),
					this.getHttpServletRequest().getParameter("orderId"), this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 获取财务信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @return 	String
	 */
	public String getFinance(){
		try{
			this.output.put(Constant.DATA, this.financeService.getFinance(
					NumberUtils.toInt(getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	
}
