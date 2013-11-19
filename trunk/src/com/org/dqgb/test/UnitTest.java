/**  
 * @Filename:    UnitTest.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午09:24:01  
 *
 */  

package com.org.dqgb.test;

import junit.framework.TestCase;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Car;

public class UnitTest  extends TestCase {
	
	public void testHibernate(){
		AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		HibernateDao ps = (HibernateDao)context.getBean("hibernateDao");
		Car c = (Car) ps.getObjectByID(Car.class, 10);
		System.out.println(c.getOwnerIdCarNumber());
	}
}
