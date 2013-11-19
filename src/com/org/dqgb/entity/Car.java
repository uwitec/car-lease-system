/**  
 * @Filename:    Car.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-16 下午09:42:43  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class Car {
	private int id;							//主键id
	private String carId;					//车牌号
	private Date registerDate;				//登记时间
	private String ownerName;				//车主姓名
	private String mobilePhone;				//车主电话
	private int status;						//车状态(详见constant.java)
	private String imagePath;				//车的照片路径
	private String ownerIdCarNumber;		//车主身份证
	private int organizationId;				//驻场信息
	private String engineNumber;			//发动机编号
	private String bodyNumber;				//车架号
	private int carSource;					//车类别
	
	private int brandId;				//品牌id	
	private String carCategory;			//车系
	private int price; 					//单价
	private int foregift;				//押金
	private int pricePerHour;			//超时单价
	private int pricePerKm;				//超驶单价
	private int priceRM;				//限里程模式下的单价 
	private int pricePerHourRM;			//限里程模式下的超时单价 
	private int pricePerKmRM;			//限里程模式下的超驶单价 
	private int km;						//当前路程数
	
	
	public int getKm() {
		return km;
	}
	public void setKm(int km) {
		this.km = km;
	}
	public int getCarSource() {
		return carSource;
	}
	public void setCarSource(int carSource) {
		this.carSource = carSource;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public String getBodyNumber() {
		return bodyNumber;
	}
	public void setBodyNumber(String bodyNumber) {
		this.bodyNumber = bodyNumber;
	}
	public String getOwnerIdCarNumber() {
		return ownerIdCarNumber;
	}
	public void setOwnerIdCarNumber(String ownerIdCarNumber) {
		this.ownerIdCarNumber = ownerIdCarNumber;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	
	/**
	 * @return the brandId
	 */
	public int getBrandId() {
		return brandId;
	}
	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	/**
	 * @return the carCategory
	 */
	public String getCarCategory() {
		return carCategory;
	}
	/**
	 * @param carCategory the carCategory to set
	 */
	public void setCarCategory(String carCategory) {
		this.carCategory = carCategory;
	}
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return the foregift
	 */
	public int getForegift() {
		return foregift;
	}
	/**
	 * @param foregift the foregift to set
	 */
	public void setForegift(int foregift) {
		this.foregift = foregift;
	}
	/**
	 * @return the pricePerHour
	 */
	public int getPricePerHour() {
		return pricePerHour;
	}
	/**
	 * @param pricePerHour the pricePerHour to set
	 */
	public void setPricePerHour(int pricePerHour) {
		this.pricePerHour = pricePerHour;
	}
	/**
	 * @return the pricePerKm
	 */
	public int getPricePerKm() {
		return pricePerKm;
	}
	/**
	 * @param pricePerKm the pricePerKm to set
	 */
	public void setPricePerKm(int pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	/**
	 * @return the priceRM
	 */
	public int getPriceRM() {
		return priceRM;
	}
	/**
	 * @param priceRM the priceRM to set
	 */
	public void setPriceRM(int priceRM) {
		this.priceRM = priceRM;
	}
	/**
	 * @return the pricePerHourRM
	 */
	public int getPricePerHourRM() {
		return pricePerHourRM;
	}
	/**
	 * @param pricePerHourRM the pricePerHourRM to set
	 */
	public void setPricePerHourRM(int pricePerHourRM) {
		this.pricePerHourRM = pricePerHourRM;
	}
	/**
	 * @return the pricePerKmRM
	 */
	public int getPricePerKmRM() {
		return pricePerKmRM;
	}
	/**
	 * @param pricePerKmRM the pricePerKmRM to set
	 */
	public void setPricePerKmRM(int pricePerKmRM) {
		this.pricePerKmRM = pricePerKmRM;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
