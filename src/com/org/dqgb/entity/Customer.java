/**  
 * @Filename:    Customer.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-11 下午04:26:17  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class Customer {
	public int id;						//主键id
	public int vipLevel;				//会员等级
	public String idCardNumber;			//客户姓名
	public String customerName;			//客户姓名
	public String address;				//家庭住址
	public String gender;				//性别
	public String company;				//客户单位
	public String companyAddress;		//客户单位地址
	public String companyPhone;			//客户单位电话
	public String mobilePhone;			//移动电话
	
	public String warrantor;			//担保人
	public String introducer;			//介绍人
	public String idImgPath;			//身份证图片路径
	public String driverLicensePath;	//驾驶证图片路径
	
	public Date registerDate;			//注册日期
	
	
	//2011-12-3
	public Date endDate;				//合同到期日期
	public Integer blacklist;			//黑名单 1：拉黑
	public String customerLevel;		//客户等级
	
	
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(Integer blacklist) {
		this.blacklist = blacklist;
	}
	public String getCustomerLevel() {
		return customerLevel;
	}
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getIdImgPath() {
		return idImgPath;
	}
	public void setIdImgPath(String idImgPath) {
		this.idImgPath = idImgPath;
	}
	public String getDriverLicensePath() {
		return driverLicensePath;
	}
	public void setDriverLicensePath(String driverLicensePath) {
		this.driverLicensePath = driverLicensePath;
	}
	public String getWarrantor() {
		return warrantor;
	}
	public void setWarrantor(String warrantor) {
		this.warrantor = warrantor;
	}
	public String getIntroducer() {
		return introducer;
	}
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdCardNumber() {
		return idCardNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
}
