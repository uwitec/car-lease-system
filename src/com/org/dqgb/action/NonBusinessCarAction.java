/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-7</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-7	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.NonBusinessRunRecord;
import com.org.dqgb.service.NBusiRunrecordService;

@Controller("NonBusinessCarAction") @Scope("prototype")
@SuppressWarnings("serial")
public class NonBusinessCarAction extends PrimaryAction {

	@Resource(name="NBusiRunrecordServiceImpl")
	public NBusiRunrecordService nbrrs;
	
	public NonBusinessRunRecord nbrr;
	
	public Date violateDate;
	
	
	public Date getViolateDate() {
		return violateDate;
	}


	public void setViolateDate(Date violateDate) {
		this.violateDate = violateDate;
	}
	public NonBusinessRunRecord getNbrr() {
		return nbrr;
	}


	public void setNbrr(NonBusinessRunRecord nbrr) {
		this.nbrr = nbrr;
	}


	/**
	 * 内部用车发车
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @return 	String
	 */
	public String setOut(){
		try{
			this.nbrrs.addRunRecord(nbrr, this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 内部用车收车
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @return 	String
	 */
	public String backCompany(){
		try{
			this.nbrrs.giveBackCar(NumberUtils.toInt(this.getHttpServletRequest().getParameter("rrId")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("km")), 
					this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @return 	String
	 */
	public String getRunRecord(){
		try{
			this.output.put(Constant.DATA, this.nbrrs.getRunRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 查询车辆出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @return 	String
	 */
	public String getRunRecorByCar(){
		try{
			this.output.put(Constant.DATA, this.nbrrs.getRunRecord(this.getHttpServletRequest().getParameter("carId"),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 获取违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @return 	String
	 */
	public String getViolateRunRecord(){
		try{
			this.output.put(Constant.DATA, this.nbrrs.getRunRecord(this.getHttpServletRequest().getParameter("carId"),
					this.violateDate));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
