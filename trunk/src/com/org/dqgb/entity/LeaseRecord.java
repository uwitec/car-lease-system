/**  
 * @Filename:    LeaseRecord.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午08:35:18  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class LeaseRecord {
	private int 	id;			
	private String 	serialNumber;				//订单编号
	private String 	customerIdCardNumber;		//顾客身份证号码
	private Date 	orderDate;					//下单日期
	private Date 	dateBegin;					//起租日期
	private Date 	dateEnd;					//截止日期
	private Date    dateRelet;					//续租日期
	private Date 	dateSettle;					//结单日期
	private String 	carId;						//车牌号
	private String 	isBeyondDeadline;			//是否超期
	private int 	leaseRecordStatus;			//订单状态
	private String	transactor;					//办理人
	private String	finisher;					//业务结算人员
	private int 	leaseWay;					//租赁方式
	private String 	driverId;					//驾驶证件号
	private String  driverName;					//驾驶员名字
	private String 	contactor;					//紧急联系人
	private String  contactorNumber;			//紧急联系电话
	private int 	fetchOrgId;					//取车门店
	private int 	givebackOrgId;				//还车门店
	private String 	remark;						//备注信息
	private int 	leaseModel;					//租赁模式
	private int		price;						//单价
	private int 	pricePerHour;				//超时费
	private int 	pricePerKm;					//超驶费
	
	
	private String sender;						//发车人
	private String maker;						//整备人
	private String leaveTable;					//发车交接表
	private String backTable;					//收车交接表
	private String customerNameRemark;			//客户姓名
	private int accident;						//标识是否是事故收车	（0是正常，1是事故）
	private int reletFeeWaiting;				//拖欠的续租费
	private int orderId;						//下单驻场
	
	
	
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getReletFeeWaiting() {
		return reletFeeWaiting;
	}
	public void setReletFeeWaiting(int reletFeeWaiting) {
		this.reletFeeWaiting = reletFeeWaiting;
	}
	
	public int getAccident() {
		return accident;
	}
	public void setAccident(int accident) {
		this.accident = accident;
	}
	public String getCustomerNameRemark() {
		return customerNameRemark;
	}
	public void setCustomerNameRemark(String customerName) {
		this.customerNameRemark = customerName;
	}
	public String getSender() {
		return sender;
	}
	public String getLeaveTable() {
		return leaveTable;
	}
	public void setLeaveTable(String leaveTable) {
		this.leaveTable = leaveTable;
	}
	public String getBackTable() {
		return backTable;
	}
	public void setBackTable(String backTable) {
		this.backTable = backTable;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPricePerHour() {
		return pricePerHour;
	}
	public void setPricePerHour(int pricePerHour) {
		this.pricePerHour = pricePerHour;
	}
	public int getPricePerKm() {
		return pricePerKm;
	}
	public void setPricePerKm(int pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	public int getFetchOrgId() {
		return fetchOrgId;
	}
	public void setFetchOrgId(int fetchOrgId) {
		this.fetchOrgId = fetchOrgId;
	}
	public int getGivebackOrgId() {
		return givebackOrgId;
	}
	public void setGivebackOrgId(int givebackOrgId) {
		this.givebackOrgId = givebackOrgId;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getContactor() {
		return contactor;
	}
	public void setContactor(String contactor) {
		this.contactor = contactor;
	}
	public String getContactorNumber() {
		return contactorNumber;
	}
	public void setContactorNumber(String contactorNumber) {
		this.contactorNumber = contactorNumber;
	}
	public int getLeaseModel() {
		return leaseModel;
	}
	public void setLeaseModel(int leaseModel) {
		this.leaseModel = leaseModel;
	}
	public Date getDateRelet() {
		return dateRelet;
	}
	public void setDateRelet(Date dateRelet) {
		this.dateRelet = dateRelet;
	}
	public String getFinisher() {
		return finisher;
	}
	public void setFinisher(String finisher) {
		this.finisher = finisher;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getLeaseRecordStatus() {
		return leaseRecordStatus;
	}
	public void setLeaseRecordStatus(int leaseRecordStatus) {
		this.leaseRecordStatus = leaseRecordStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerIdCardNumber() {
		return customerIdCardNumber;
	}
	public void setCustomerIdCardNumber(String customerIdCardNumber) {
		this.customerIdCardNumber = customerIdCardNumber;
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
	
	public Date getDateSettle() {
		return dateSettle;
	}
	public void setDateSettle(Date dateSettle) {
		this.dateSettle = dateSettle;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getIsBeyondDeadline() {
		return isBeyondDeadline;
	}
	public void setIsBeyondDeadline(String isBeyondDeadline) {
		this.isBeyondDeadline = isBeyondDeadline;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	public int getLeaseWay() {
		return leaseWay;
	}
	public void setLeaseWay(int leaseWay) {
		this.leaseWay = leaseWay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
