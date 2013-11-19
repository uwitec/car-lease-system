/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-1</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-1	    肖乾斌	    Created</li>
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

@Entity(name="T_VS_FINANCE_RECORD")	//违章保证金财务信息
public class VsFinanceRecord {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PK_ID")
	private Integer id;
	
	//财务发生日期
	@Column(name="GIVEBACK_DATE")
	private Date givebackDate;
	
	//退还金额
	@Column(name="_MONEY")
	private int money;
	
	//业务人员
	@Column(name="TRANSACTOR")
	private String transactor;
	
	//办理驻场id
	@Column(name="ORGID")
	private int orgId;
	
	//备注
	@Column(name="_DESCRIPTION")
	private String description;

	//保证金
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FK_VS_ID")
	private ViolateSurety vs;
	
	//财务类别(1：收（续交）2：还（退还）3：扣（从现有财务数据中扣除费用）)
	@Column(name="FINANCE_TYPE")
	private int financeType;
	
	
	
	public int getFinanceType() {
		return financeType;
	}

	public void setFinanceType(int financeType) {
		this.financeType = financeType;
	}

	public ViolateSurety getVs() {
		return vs;
	}

	public void setVs(ViolateSurety vs) {
		this.vs = vs;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getGivebackDate() {
		return givebackDate;
	}

	public void setGivebackDate(Date givebackDate) {
		this.givebackDate = givebackDate;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
