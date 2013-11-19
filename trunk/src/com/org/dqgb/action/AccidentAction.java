/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-9</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-9	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.action;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Accident;
import com.org.dqgb.service.AccidentService;

@SuppressWarnings("serial")

@Controller("AccidentAction") @Scope("prototype")
public class AccidentAction extends PrimaryAction {

	@Resource(name="AccidentServiceImpl")
	public AccidentService accService;
	
	public Accident acc;
	
	public Accident getAcc() {
		return acc;
	}

	public void setAcc(Accident acc) {
		this.acc = acc;
	}

	/**
	 * 获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @return 	String
	 */
	public String getAccident(){
		try{
			this.output.put(Constant.DATA, this.accService.getAccident(
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 修改单据
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @return 	String
	 */
	public String modifyAccident(){
		try{
			this.accService.modify(acc, this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 封存操作
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @return 	String
	 */
	public String lock(){
		try{
			this.accService.lockRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")), 
					this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
