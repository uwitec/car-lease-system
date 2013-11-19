/**  
 * @Filename:    AlarmParam.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 上午10:07:30  
 *
 */  

package com.org.dqgb.entity;

public class AlarmParam {
	private int id;
	private int alarmSettingId;				//预警配置id
	private String paramName;
	private String paramValue;
	private int duration;						//预警持续时间。
	private int alarmMethod;					//预警方式，提前或者置后
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAlarmSettingId() {
		return alarmSettingId;
	}
	public void setAlarmSettingId(int alarmSettingId) {
		this.alarmSettingId = alarmSettingId;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAlarmMethod() {
		return alarmMethod;
	}
	public void setAlarmMethod(int alarmMethod) {
		this.alarmMethod = alarmMethod;
	}
}
