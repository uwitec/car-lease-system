/**  
 * @Filename:    Price.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-21 下午09:26:01  
 *
 */  

package com.org.dqgb.entity;

public class Price {
	private int id;						//主键
	private int brandId;				//品牌id	
	private String carCategory;			//车系
	private int price; 					//单价
	private int foregift;				//押金
	private int pricePerHour;			//超时单价
	private int pricePerKm;				//超驶单价
	private int priceRM;				//限里程模式下的单价 
	private int pricePerHourRM;			//限里程模式下的超时单价 
	private int pricePerKmRM;			//限里程模式下的超驶单价 
	
	
	public int getPriceRM() {
		return priceRM;
	}
	public void setPriceRM(int priceRM) {
		this.priceRM = priceRM;
	}
	public int getPricePerHourRM() {
		return pricePerHourRM;
	}
	public void setPricePerHourRM(int pricePerHourRM) {
		this.pricePerHourRM = pricePerHourRM;
	}
	public int getPricePerKmRM() {
		return pricePerKmRM;
	}
	public void setPricePerKmRM(int pricePerKmRM) {
		this.pricePerKmRM = pricePerKmRM;
	}
	public int getPricePerKm() {
		return pricePerKm;
	}
	public void setPricePerKm(int pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	public int getPricePerHour() {
		return pricePerHour;
	}
	public void setPricePerHour(int pricePerHour) {
		this.pricePerHour = pricePerHour;
	}
	public int getForegift() {
		return foregift;
	}
	public void setForegift(int foregift) {
		this.foregift = foregift;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getCarCategory() {
		return carCategory;
	}
	public void setCarCategory(String carCategory) {
		this.carCategory = carCategory;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
