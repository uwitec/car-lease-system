/**  
 * @Filename:    CustomerAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-7 上午11:23:55  
 *
 */  

package com.org.dqgb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Customer;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.util.DateUtil;

@Controller("CustomerAction") @Scope("prototype")
@SuppressWarnings("serial")
public class CustomerAction extends PrimaryAction{

	@Resource @Qualifier("CustomerServiceImpl")
	public CustomerService customerService;
	
	public Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 *
	 * @TODO	获取用户信息
	 * @date	2011-4-18
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCustomers(){
		try{
			JSONObject json = this.customerService.getAllCustomer(Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到客户修改界面
	 * @date	2011-4-18
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyCustomerPage(){
		try{
			Customer customer = this.customerService.getCustomerById(Integer.parseInt(
				this.getHttpServletRequest().getParameter("id")));
			this.getHttpSession().setAttribute("CUSTOMER", customer);
			this.getHttpSession().setAttribute("r",customer.getRegisterDate() == null ? "" : DateUtil.formatDate(customer.getRegisterDate(), "yyyy-MM-dd"));
			this.getHttpSession().setAttribute("e", customer.getEndDate() == null ? "" : DateUtil.formatDate(customer.getEndDate(), "yyyy-MM-dd"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	新增客户
	 * @date	2011-4-18
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addCustomer(){
		try{
			customer.setVipLevel(1);						//默认是一级会员
			this.customerService.addCustomer(customer);
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	修改客户
	 * @date	2011-4-18
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyCustomer(){
		try{
			Customer cus = new Customer();
			cus.setCompany(this.getHttpServletRequest().getParameter("company"));
			cus.setCustomerName(this.getHttpServletRequest().getParameter("customerName"));
			cus.setId(Integer.parseInt(this.getHttpServletRequest().getParameter("id")));
			cus.setGender(this.getHttpServletRequest().getParameter("gender"));
			cus.setIdCardNumber(this.getHttpServletRequest().getParameter("idCardNumber"));
			cus.setMobilePhone(this.getHttpServletRequest().getParameter("mobilePhone"));
			cus.setCompanyAddress(this.getHttpServletRequest().getParameter("companyAddress"));
			cus.setAddress(this.getHttpServletRequest().getParameter("address"));
			cus.setCompanyPhone(this.getHttpServletRequest().getParameter("companyPhone"));
			cus.setWarrantor(this.getHttpServletRequest().getParameter("warrantor"));
			cus.setIntroducer(this.getHttpServletRequest().getParameter("introducer"));
			
			cus.setBlacklist(NumberUtils.toInt(this.getHttpServletRequest().getParameter("blacklist")));
			cus.setCustomerLevel(this.getHttpServletRequest().getParameter("customerLevel"));
			cus.setRegisterDate(DateUtil.formatString(this.getHttpServletRequest().getParameter("registerDate"), "yyyy-MM-dd"));
			cus.setEndDate(DateUtil.formatString(this.getHttpServletRequest().getParameter("endDate"), "yyyy-MM-dd"));
			
			this.customerService.modifyCustomer(cus);
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	删除客户
	 * @date	2011-4-18
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deleteCustomer(){
		try{
			String ids = this.getHttpServletRequest().getParameter("ids");
			String[] id = ids.split(",");
			for(int i = 0; i < id.length; i++){
				this.customerService.deleteCustomerByID(Integer.parseInt(id[i]));
			}
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	根据查询条件模糊查询用户信息
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String queryCustomer(){
		try{
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("idCardNumber", this.getHttpServletRequest().getParameter("idCardNumber"));		//jquery组件中keyField参数
			JSONObject json = this.customerService.getAllCustomerByCondition(Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize")),condition);
			output.put(Constant.DATA, json);
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 按名字查找
	 * @author 	肖乾斌
	 * @date 	2011-12-3
	 * @return 	String
	 */
	public String queryCustomerByName(){
		try{
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("customerName", this.getHttpServletRequest().getParameter("customerName"));		//jquery组件中keyField参数
			JSONObject json = this.customerService.getAllCustomerByCondition(Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize")),condition);
			output.put(Constant.DATA, json);
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 上传客户证件界面
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @return 	String
	 */
	public String gotoAddImagePathPage(){
		try{
			this.getHttpServletRequest().setAttribute("customerId", this.getHttpServletRequest().getParameter("customerId"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 * 添加客户身份证
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @return 	String
	 */
	public String addIdCarImage(){
		try{
			Customer cus = this.customerService.getCustomer(this.getHttpServletRequest().getParameter("customerId"));
			cus.setIdImgPath(this.getHttpServletRequest().getParameter("image"));
			this.customerService.updateCustomer(cus);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 添加客户驾驶证
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @return 	String
	 */
	public String addDriverLicenseImage(){
		try{
			Customer cus = this.customerService.getCustomer(this.getHttpServletRequest().getParameter("customerId"));
			cus.setDriverLicensePath(this.getHttpServletRequest().getParameter("image"));
			this.customerService.updateCustomer(cus);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取用户增量统计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	@SuppressWarnings("deprecation")
	public String getCustomerAddition(){
		try{
			int year = NumberUtils.toInt(this.getHttpServletRequest().getParameter("year"));
			if(year == 0){
				Date date = new Date();
				year = date.getYear() + 1900;
			}
			List<Map<String,String>> yearmonth = new ArrayList<Map<String,String>>();
			for(int i = 1; i <= 12; i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("customerName", year + "/" + i);
				map.put("leaf", "false");
				yearmonth.add(map);
			}
			output.put(Constant.DATA, yearmonth);
			output.put(Constant.TOTAL_SIZE, 12);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取用户增量统计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	public String getCustomerByDate(){
		try{
			output.put(Constant.DATA, this.customerService.getCustomerByDate(this.getHttpServletRequest().getParameter("date")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
