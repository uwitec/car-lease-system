/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-29</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-29	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="T_VIOLATE_SURETY")
public class ViolateSurety {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@Column(name="CUSTOMER_ID",length=20,nullable=true)
	private String customerId;
	
	@Column(name="PAY_DATE")					//交付保证金日期
	private Date payDate; 
	
	@Column(name="GIVEBACK_DATE")				//实际退还保证金日期
	private Date givebackDate; 
	
	@Column(name="DEADLINE_DATE")				//理论退还日期
	private Date deadlineDate; 
	
	@Column(name="CARID")						//车辆
	private String carId;
	
	@Column(name="TRANSACTOR")
	private String transactor;
	
//	@Column(name="FINISHER")
//	private String finisher;

	@Column(name="MONEY")
	private int money;
	
	@Column(name="BALANCE")						//余额
	private int balance;
	
//	@Column(name="GIVEBACK_MONEY")
//	private int givebackMoney;					//退还金额
	
	@Column(name="PAY_DEPT_ID")					//缴费驻场
	private int payOrganizationId;
	
//	@Column(name="GIVEBACK_DEPT_ID")
//	private int givebackOrganizationId;			//还钱驻场
	
	/***
	 * 0：有效，1：无效(冻结)
	 */
	@Column(name="STATUS")						
	private int vsStatus;						//保证金状态
	
	

	
	public Date getGivebackDate() {
		return givebackDate;
	}

	public void setGivebackDate(Date givebackDate) {
		this.givebackDate = givebackDate;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getVsStatus() {
		return vsStatus;
	}

	public void setVsStatus(int vsStatus) {
		this.vsStatus = vsStatus;
	}

	public int getPayOrganizationId() {
		return payOrganizationId;
	}

	public void setPayOrganizationId(int payOrganizationId) {
		this.payOrganizationId = payOrganizationId;
	}


	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}


	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	
	
}
