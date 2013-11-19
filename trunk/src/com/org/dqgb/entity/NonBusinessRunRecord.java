/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-6</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-6	    肖乾斌	    Created</li>
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


@Entity(name="T_NONBUSINESS_RUN_RECORD")	//内部用车
public class NonBusinessRunRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	//发车日期
	@Column(name="DATE_BEGIN")
	private Date dateBegin;
	
	//收车日期
	@Column(name="DATE_END")
	private Date dateEnd;
	
	//发车里程
	@Column(name="KM_BEGIN")
	private int kmBegin;
	
	//收车里程
	@Column(name="KM_END")
	private int kmEnd;
	
	//用车人名字
	@Column(name="USER_NAME")
	private String username;
	
	//用车原因
	@Column(name="REMARK",length=200)
	private String remark;
	
	//车牌号
	@Column(name="CAR_ID",length=20)
	private String carId;

	//业务员
	@Column(name="TRANSACTOR")
	private String transactor;
	
	//收车人
	@Column(name="FINISHER")
	private String finisher;
	
	
	public String getFinisher() {
		return finisher;
	}

	public void setFinisher(String finisher) {
		this.finisher = finisher;
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

	public int getKmBegin() {
		return kmBegin;
	}

	public void setKmBegin(int kmBegin) {
		this.kmBegin = kmBegin;
	}

	public int getKmEnd() {
		return kmEnd;
	}

	public void setKmEnd(int kmEnd) {
		this.kmEnd = kmEnd;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	
}
