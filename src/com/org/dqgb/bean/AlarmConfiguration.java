/**  
 * @Filename:    Alarm.java  
 * @TODO:		 
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 下午01:15:51  
 *
 */  

package com.org.dqgb.bean;

import java.util.List;

import com.org.dqgb.entity.AlarmParam;

public class AlarmConfiguration {
	public int type;
	public List<AlarmParam> apl;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<AlarmParam> getApl() {
		return apl;
	}
	public void setApl(List<AlarmParam> apl) {
		this.apl = apl;
	}
}
