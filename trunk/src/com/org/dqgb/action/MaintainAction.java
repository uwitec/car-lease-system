/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-31</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-31	    肖乾斌	    Created</li>
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
import com.org.dqgb.entity.MaintainRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.MaintainService;
import com.org.dqgb.util.DateUtil;

@Controller("MaintainAction")	@Scope("prototype")
@SuppressWarnings("serial")
public class MaintainAction extends PrimaryAction {

	@Resource(name="MaintainServiceImpl")
	public MaintainService ms;
	
	public MaintainRecord mr;
	
	public void setMr(MaintainRecord mr) {
		this.mr = mr;
	}


	/**
	 * 
	 * @TODO	获取所有养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String getMaintainRecord(){
		try{
			this.output.put(Constant.DATA, this.ms.getMaintainRecord(
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	根据carId获取养护记录信息
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String getMaintainRecordByCar(){
		try{
			this.output.put(Constant.DATA, this.ms.getMaintainRecord(
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize")),
					this.getHttpServletRequest().getParameter("carId")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	添加养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String addMaintainRecord(){
		try{
			User user = this.getCurrentUser();
//			this.mr.setMaintainDate(new Date());
			this.mr.setTransactor(user.getUserName());
			this.mr.setTransactorName(user.getRealName());
			this.ms.addMaintainRecord(mr);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	修改养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String modifyMaintainRecord(){
		try{
			MaintainRecord mrd = this.ms.getMaintainRecord(mr.getId());
			mrd.setMoney(mr.getMoney());
			mrd.setRemark(mr.getRemark());
			mrd.setMaintainDate(mr.getMaintainDate());
			this.ms.modifyMaintainRecord(mrd,this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	删除养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String deleteMaintainRecord(){
		try{
			this.ms.deleteMaintainRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到新增养护记录页面
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String gotoAddMaintainRecordPage(){
		try{
			this.getHttpServletRequest().setAttribute("carId", 
					new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	跳转到修改养护记录页面
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String gotoModifyMaintainRecordPage(){
		try{
			this.getHttpServletRequest().setAttribute("MaintainRecord", 
					this.ms.getMaintainRecord(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id"))));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	统计养护费用
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @return 	String
	 */
	public String getRepairFee(){
		try{
			this.output.put(Constant.DATA, this.ms.getRepairFee(DateUtil.formatString(
					this.getHttpServletRequest().getParameter("date"), "yyyy-M-d")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到养护记录界面
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @return 	String
	 */
	public String gotoMaintainRecordPage(){
		try{
			Date dt = new Date();
			this.getHttpServletRequest().setAttribute("DATE", DateUtil.formatDate(dt, "yyyy-M-d"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute(Constant.ERROR, e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
}
