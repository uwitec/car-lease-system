/**  
 * @Filename:    Finance.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午10:31:23  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class Finance {
	private int 	id;				
	private String 	serialNumber;			//订单编号
	private int		payDirection;			//财务支付方向	0支出,1收入
	private	int		financeType;			//财务类型
	private	int		money;					//金额
	private int 	payType;				//财务支付方式，1为预付，2为结算以后所付费用
	private Date	payDate;				//支付日期
	private String	remark;					//财务描述
	private String  transactor;				//业务员编号
	private String  transactorOver;			//业务结算员编号
	private String  transactorName;			//业务员姓名
	private String  transactorOverName;		//业务结算员姓名
	private int		organizationId;			//财务发生的驻场信息
	
	
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getTransactorOver() {
		return transactorOver;
	}
	public void setTransactorOver(String transactorOver) {
		this.transactorOver = transactorOver;
	}
	public String getTransactorOverName() {
		return transactorOverName;
	}
	public void setTransactorOverName(String transactorOverName) {
		this.transactorOverName = transactorOverName;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	public String getTransactorName() {
		return transactorName;
	}
	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}
	public int getPayDirection() {
		return payDirection;
	}
	public void setPayDirection(int payDirection) {
		this.payDirection = payDirection;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getFinanceType() {
		return financeType;
	}
	public void setFinanceType(int financeType) {
		this.financeType = financeType;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
