/**  
 * @Filename:    Relet.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-14 下午02:13:05  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="T_RELET")
public class Relet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@Column(name="ORDERFORM_SERIAL_NUMBER",length=50,nullable=true)
	private String orderFormId;
	
	@Column(name="DATE_RELET_BEGIN",nullable=true)					//续租起始日期
	private Date dateBegin;
	
	@Column(name="DATE_RELET_END",nullable=true)					//续租截止日期
	private Date dateEnd;
	
	@Column(name="TRANSACTOR",length=50,nullable=true)
	private String transactor;

	@Column(name="BUSSINESS_DATE",nullable=true)					//业务办理日期
	private Date bussinessDate;
	
	
	
	public Date getBussinessDate() {
		return bussinessDate;
	}

	public void setBussinessDate(Date bussinessDate) {
		this.bussinessDate = bussinessDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderFormId() {
		return orderFormId;
	}

	public void setOrderFormId(String orderFormId) {
		this.orderFormId = orderFormId;
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

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
}
