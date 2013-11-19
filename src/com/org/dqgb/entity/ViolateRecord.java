/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-8-30</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-8-30	    肖乾斌	    Created</li>
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

//违章记录表实体
@Entity(name="T_VIOLATE_RECORD")
public class ViolateRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@Column(name="SERIAL_NUMBER",length=50)	//订单号
	private String serialNumber;
	
	@Column(name="VIOLATE_DATE")		//违章日期
	private Date violateDate;
	
	@Column(name="REMARK",length=200)	//违章记录
	private String remark;
	
	@Column(name="VIOLATE_MONEY")		//违章金额
	private int violateMoney;
	
	@Column(name="TRANSACTOR",length=20)
	private String transactor;			//业务办理员
	
	@Column(name="PAY_DATE")
	private Date payDate;				//罚款缴纳日期

	@Column(name="CAR_ID")
	private String carId;				//违章车辆
	
	@Column(name="VS_ID",nullable=true)
	private Integer vsId;					//违章保证金id（标识该次违章扣费的保证金）
	
	@Column(name="POUNDAGE")
	private int poundage;				//手续费
	
	@Column(name="INNER_USAGE")
	private Integer innerUsage;			//内部用车(1:内部，0：业务用车)
	
	
	public Integer getInnerUsage() {
		return innerUsage;
	}

	public void setInnerUsage(Integer innerUsage) {
		this.innerUsage = innerUsage;
	}

	public int getPoundage() {
		return poundage;
	}

	public void setPoundage(int poundage) {
		this.poundage = poundage;
	}
	public Integer getVsId() {
		return vsId;
	}

	public void setVsId(Integer vsId) {
		this.vsId = vsId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getViolateDate() {
		return violateDate;
	}

	public void setViolateDate(Date violateDate) {
		this.violateDate = violateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getViolateMoney() {
		return violateMoney;
	}

	public void setViolateMoney(int violateMoney) {
		this.violateMoney = violateMoney;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}
