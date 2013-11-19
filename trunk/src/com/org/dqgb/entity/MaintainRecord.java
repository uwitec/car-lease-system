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

@Entity(name="T_REPAIR_RECORD")
public class MaintainRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@Column(name="CAR_ID",length=20,nullable=true)
	private String carId;
	
	@Column(name="MAINTAIN_DATE")
	private Date maintainDate;
	
	@Column(name="REMARK",length=300,nullable=true)
	private String remark;
	
	@Column(name="MONEY")
	private int money;
	
	@Column(name="TRANSACTOR",length=20,nullable=true)
	private String transactor;
	
	@Column(name="TRANSACTOR_NAME",length=50,nullable=true)
	private String transactorName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public Date getMaintainDate() {
		return maintainDate;
	}

	public void setMaintainDate(Date maintainDate) {
		this.maintainDate = maintainDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
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
}
