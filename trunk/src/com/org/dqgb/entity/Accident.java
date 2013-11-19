/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-8</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-8	    肖乾斌	    Created</li>
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

@Entity(name="T_ACCIDENT")
public class Accident {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	@Column(name="CAR_ID",length=20)
	private String carId;
	
	@Column(name="TRANSACTOR",length=20)
	private String transactor;
	
	@Column(name="SERIAL_NUMBER",length=50)
	private String serialNumber;
	
	@Column(name="ACCIDENT_DATE")
	private Date accidentDate;
	
	@Column(name="DELAY_FEE")//停运费
	private Integer dalayFee;
	
	@Column(name="CAR_FRAY_FEE")//车损费
	private Integer frayFee;
	
	@Column(name="REPAIR_FEE")//维修费
	private Integer repairFee;
	
	@Column(name="INSURANCE_FEE")//保险索赔费
	private Integer insuranceFee;
	
	@Column(name="OTHER_FEE")//其它费用费
	private Integer otherFee;
	
	@Column(name="REMARK",length=200)//事故说明
	private String remark;

	@Column(name="FINISHER",length=20)
	private String finisher;
	
	@Column(name="STATUS")//0未结单，1结单
	private int status;
	
	
	public String getFinisher() {
		return finisher;
	}

	public void setFinisher(String finisher) {
		this.finisher = finisher;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	public Integer getDalayFee() {
		return dalayFee;
	}

	public void setDalayFee(Integer dalayFee) {
		this.dalayFee = dalayFee;
	}

	public Integer getFrayFee() {
		return frayFee;
	}

	public void setFrayFee(Integer frayFee) {
		this.frayFee = frayFee;
	}

	public Integer getRepairFee() {
		return repairFee;
	}

	public void setRepairFee(Integer repairFee) {
		this.repairFee = repairFee;
	}

	public Integer getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(Integer insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public Integer getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(Integer otherFee) {
		this.otherFee = otherFee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
}
