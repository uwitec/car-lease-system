/**  
 * @Filename:    AjaxInterceptor.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午04:20:31  
 *
 */  

package com.org.dqgb.interceptor;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.org.dqgb.action.PrimaryAction;
import com.org.dqgb.common.Constant;

@SuppressWarnings("serial")
public class AjaxInterceptor extends PrimaryAction implements Interceptor {

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

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
			Map<String,Object> map = new HashMap<String, Object>();
			map.put(Constant.RESULT, Constant.FAILED);
			map.put(Constant.REASON, "连接超时,请重新登录...");
			invocation.getStack().setValue("output", map);
			return SUCCESS;
		}else{
			return invocation.invoke();
		}
	}

}
