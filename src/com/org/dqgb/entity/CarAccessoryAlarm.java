/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-10-1</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-10-1	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="T_REMIND_SETTING")
public class CarAccessoryAlarm {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CAR_ID")
	private Car car;
	
	//预警栏目
	@Column(name="ITEM",length=50)
	private String item;
	
	//预警里程间隔
	@Column(name="KM_INTERVAL")
	private Integer kmInterval;
	
	//预警时间间隔
	@Column(name="DATE_INTERVAL")
	private Integer dateInterval;

	//预警方式 0 ：时间，1：里程
	@Column(name="REMIND_WAY")
	private Integer remindWay;
	
	//下次维护里程
	@Column(name="NEXT_KM")
	private Integer nextKm;

	//下次维护的日期
	@Column(name="NEXT_DATE")
	private Date nextDate;

	@Column(name="FIELD_NAME")	//js字段id
	private String fieldIndex; 
	
	

	public String getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(String fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getKmInterval() {
		return kmInterval;
	}

	public void setKmInterval(Integer kmInterval) {
		this.kmInterval = kmInterval;
	}

	public Integer getDateInterval() {
		return dateInterval;
	}

	public void setDateInterval(Integer dateInterval) {
		this.dateInterval = dateInterval;
	}

	public Integer getRemindWay() {
		return remindWay;
	}

	public void setRemindWay(Integer remindWay) {
		this.remindWay = remindWay;
	}

	public Integer getNextKm() {
		return nextKm;
	}

	public void setNextKm(Integer nextKm) {
		this.nextKm = nextKm;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}
}
