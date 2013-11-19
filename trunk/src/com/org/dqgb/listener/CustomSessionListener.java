/**  
 * @Filename:    CustomSessionListener.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-16 下午11:53:09  
 *
 */  

package com.org.dqgb.listener;

import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.org.dqgb.action.PrimaryAction;
import com.org.dqgb.common.Constant;

/**
 * 
 * 1.ServletContextAttributeListener    
 * 					监听对ServletContext属性的操作，比如增加/删除/修改
 *       
 * 2.ServletContextListener     		
 * 					监听ServletContext,当创建ServletContext时，
 * 					激发 contextInitialized(ServletContextEvent sce)方法；
 * 					当销毁ServletContext时，
 * 					激发contextDestroyed(ServletContextEvent sce)方法。      
 * 3.HttpSessionListener      			
 * 					监听HttpSession的操作。当创建一个 Session时，
 * 					激发session Created(SessionEvent se)方法；
 * 					当销毁一个Session时，激发sessionDestroyed (HttpSessionEvent se)方法。      
 * 4.HttpSessionAttributeListener       
 * 					监听HttpSession中的属性的操作。当在Session增加一个属性时，
 * 					激发 attributeAdded(HttpSessionBindingEvent se) 方法；
 * 					当在Session删除一个属性时，激发attributeRemoved(HttpSessionBindingEvent se)方法；
 * 					当在Session属性被重新设置时，激发attributeReplaced(HttpSessionBindingEvent se) 方法。
 * 
 */
@SuppressWarnings("serial")
public class CustomSessionListener extends PrimaryAction implements HttpSessionListener{
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("~~~new user~~~" + event.getSession().getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		Map<String,Object> userList = (Map<String, Object>) event.getSession().getServletContext().getAttribute(Constant.USER_LIST);//获取用户列表
		if(userList != null)
			userList.remove(event.getSession().getId());
		System.out.println("~~~session time out~~~" + event.getSession().getId());
	}

}
