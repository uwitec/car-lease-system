package com.org.dqgb.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.bean.AlarmConfiguration;
import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.AlarmService;

@Controller("AlarmSettingAction") @Scope("prototype")
@SuppressWarnings("serial")
public class AlarmSettingAction extends PrimaryAction {
	
	@Resource @Qualifier("AlarmServiceImpl")
	public AlarmService alarmService;
	
	public List<AlarmConfiguration> alarmSetting;
	
	public void setAlarmSetting(List<AlarmConfiguration> alarmSetting) {
		this.alarmSetting = alarmSetting;
	}

	/**
	 *
	 * @TODO	保存预警配置
	 * @date	2011-5-6
	 * @author	肖乾斌
	 *
	 */
	public String saveAlarmSetting(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.alarmService.setPersonalAPC(user, this.alarmSetting);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}

	/**
	 *
	 * @TODO	获取个人预警配置信息
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getPersonalAPC(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.output.put(Constant.DATA, this.alarmService.getPersonalAPC(user));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取个人预警信息
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getPersonalAlarmInfor(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			this.output.put(Constant.DATA, this.alarmService.getPersonalAI(user,this.alarmSetting));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取预警文件档案信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getFileAlarmInformation(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			output.put(Constant.DATA, this.alarmService.getFileAlarmInformation(user,
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取预警订单信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getOrderAlarmInformation(){
		try{
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			output.put(Constant.DATA, this.alarmService.getOrderAlarmInformation(user,
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 到期订单
	 * @author 	肖乾斌
	 * @date 	2011-12-4
	 * @return 	String
	 */
	public String getDeadlineOrder(){
		try{
			output.put(Constant.DATA, this.alarmService.getOrderAlarmInfor(
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	获取预警担保金信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getVsAlarmInformation(){
		try{
			output.put(Constant.DATA, this.alarmService.getVsAlarmInformation(this.getCurrentUser(),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
}
