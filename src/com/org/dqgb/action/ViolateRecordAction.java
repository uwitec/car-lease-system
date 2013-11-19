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
package com.org.dqgb.action;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.ViolateRecord;
import com.org.dqgb.service.ViolateRecordService;

@SuppressWarnings("serial")
@Controller("ViolateRecordAction")	@Scope("prototype")
public class ViolateRecordAction extends PrimaryAction {
	
	@Resource(name="ViolateRecordServiceImpl")
	public ViolateRecordService vs;
	
	public ViolateRecord vr;
	
	public ViolateRecord getVr() {
		return vr;
	}

	public void setVr(ViolateRecord vr) {
		this.vr = vr;
	}

	/**
	 * 获取违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-1
	 * @return 	String
	 */
	public String getViolateRecord(){
		try{
			this.output.put(Constant.DATA, 
					this.vs.getViolateRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取客户违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-1
	 * @return 	String
	 */
	public String getViolateRecordByCustomer(){
		try{
			this.output.put(Constant.DATA, 
					this.vs.getViolateRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
							NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize")),
							this.getHttpServletRequest().getParameter("customerId")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 按车牌查找违章记录
	 * @author 	肖乾斌
	 * @date 	2011-12-15
	 * @return 	String
	 */
	public String getViolateRecordByCar(){
		try{
			this.output.put(Constant.DATA, 
					this.vs.getViolateRecordByCar(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
							NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize")),
							this.getHttpServletRequest().getParameter("carId")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 添加违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-1
	 * @return 	String
	 */
	public String addViolateRecord(){
		try{
			this.vs.addViolateRecord(vr,this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
