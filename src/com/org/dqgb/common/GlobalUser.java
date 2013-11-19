/**  
 * @Filename:    GlobalUser.java  
 * @TODO:		   存储在application对象中user对象
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-16 下午09:53:30  
 *
 */  

package com.org.dqgb.common;

import javax.servlet.http.HttpSession;

public class GlobalUser {
	public String uniqueID;						//唯一标识符，用来区分不同的用户
	public String remoteIP;						//用户的ip
	public Object user;							//user对象实体
	public HttpSession session;					//当前用户使用的session
	
	public String getUniqueID() {
		return uniqueID;
	}
	
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}
	
	public HttpSession getSession() {
		return session;
	}
	
	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	
}
