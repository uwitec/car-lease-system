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
package com.org.dqgb.action;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.ViolateSurety;
import com.org.dqgb.service.ViolateSuretyService;

@Controller("ViolateSuretyAction") @Scope("prototype")
@SuppressWarnings("serial")
public class ViolateSuretyAction extends PrimaryAction{				//保证金action
	
	public ViolateSurety vs;
	
	public void setVs(ViolateSurety vs) {
		this.vs = vs;
	}
	@Resource(name="ViolateSuretyServiceImpl")
	public ViolateSuretyService vsService;
	
	/**
	 * 
	 * @TODO	获取违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String getViolateSurety(){
		try{
			this.output.put(Constant.DATA, 
					this.vsService.getViolateSurety(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
							NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	添加违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String addViolateSurety(){
		try{
			this.vsService.addViolateSurety(vs,this.getCurrentUser(),NumberUtils.toLong(this.getHttpServletRequest().getParameter("duration")));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 	
	 * @TODO	延期违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String addSuretyDuration(){
		try{
			this.vsService.addSuretyDuration(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")),
					NumberUtils.toLong(this.getHttpServletRequest().getParameter("duration")),this.getCurrentUser());
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 	
	 * @TODO	退还违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String givebackViolateSurety(){
		try{
			this.vsService.givebackViolateSurety(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("money")),
					this.getCurrentUser());
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 	
	 * @TODO	跳转到增加违章保证金界面
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String gotoAddViolateSuretyPage(){
		try{
//			this.getHttpServletRequest().setAttribute("carId", 
//					new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		}catch(Exception e){
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	/**
	 * 	
	 * @TODO	跳转到修改违章保证金界面
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String gotoModifyViolateSuretyPage(){
		try{
			
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	/**
	 * 	
	 * @TODO	冻结违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String freezoneViolateSurety(){
		try{
			this.vsService.freezoneViloateSurety(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")),this.getCurrentUser());
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	查找保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String findViolateSurety(){
		try{
			this.output.put(Constant.DATA, 
					this.vsService.findViolateSurety(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
							NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize")),
							this.getHttpServletRequest().getParameter("customerId")));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 补缴担保金
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @return 	String
	 */
	public String addVsMoney(){
		try{
			this.vsService.addVsMoney(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("money")), this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 跳转到保证金财务明细界面
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @return 	String
	 */
	public String gotoVsFinancePage(){
		try{
			this.getHttpServletRequest().setAttribute("id",this.getHttpServletRequest().getParameter("id"));
		}catch(Exception e){
		}
		return SUCCESS;
	}
	
	
	/**
	 * 获取保证金财务明细
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @return 	String
	 */
	public String getVsFinance(){
		try{
			this.output.put(Constant.DATA, this.vsService.getVsFinance(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
