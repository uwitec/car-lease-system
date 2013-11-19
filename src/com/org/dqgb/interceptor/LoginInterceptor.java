/**  
 * @Filename:    LoginInterceptor.java  
 * @TODO:		   登录拦截器
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-16 下午10:11:55  
 *
 */  

package com.org.dqgb.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.org.dqgb.action.PrimaryAction;
import com.org.dqgb.common.Constant;

@SuppressWarnings("serial")
public class LoginInterceptor extends PrimaryAction implements Interceptor{

	/**
	 *
	 * @TODO	对action进行拦截，如果发现没有登录系统则返回
	 * @date	2011-2-16
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		if(null == session.get(Constant.CURRENT_USER)){
			if(-1 != invocation.getAction().getClass().getName().indexOf("FileUploadAction"))	//使得超时以后跳转到success函数 
				return ERROR;
			return INPUT;
		}else{
			return invocation.invoke();
		}
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
		
	}
}
