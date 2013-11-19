/**  
 * @Filename:    RunRecord.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-3 上午11:36:50  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class RunRecord {
	private int id;
	private int fetchOrgId;						//取车驻场
	private int giveBackOrgId;					//换车驻场
	private String orderFormSerialNumber;		//订单序列号
	private String beginTransactor;				//发车业务人员
	private String endTransactor;				//收车业务人员
	private String carId;						//车牌号
	private Date dateBegin;						//发车时间
	private Date dateEnd;						//收车时间
	private int startKm;						//发车时的行驶距离
	private int endKm;							//收车时行驶距离
	
	
	public int getStartKm() {
		return startKm;
	}
	public void setStartKm(int startKm) {
		this.startKm = startKm;
	}
	public int getEndKm() {
		return endKm;
	}
	public void setEndKm(int endKm) {
		this.endKm = endKm;
	}
	public int getFetchOrgId() {
		return fetchOrgId;
	}
	public void setFetchOrgId(int fetchOrgId) {
		this.fetchOrgId = fetchOrgId;
	}
	public int getGiveBackOrgId() {
		return giveBackOrgId;
	}
	public void setGiveBackOrgId(int giveBackOrgId) {
		this.giveBackOrgId = giveBackOrgId;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderFormSerialNumber() {
		return orderFormSerialNumber;
	}
	public void setOrderFormSerialNumber(String orderFormSerialNumber) {
		this.orderFormSerialNumber = orderFormSerialNumber;
	}
	public String getBeginTransactor() {
		return beginTransactor;
	}
	public void setBeginTransactor(String beginTransactor) {
		this.beginTransactor = beginTransactor;
	}
	public String getEndTransactor() {
		return endTransactor;
	}
	public void setEndTransactor(String endTransactor) {
		this.endTransactor = endTransactor;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
}
