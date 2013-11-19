/**  
 * @Filename:    DaoException.java  
 * @TODO:
 * @Description: 异常公用类   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-9 上午09:34:26  
 *
 */  

package com.org.dqgb.exception;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException{
	protected String message;
	/**
	 * @TODO	构造函数
	 */
	public DaoException(String msg){
		this.setMessage(msg);
	}
	
	/**
	 * @TODO	构造函数
	 */
	public DaoException(String msg,Throwable t){
		this.setMessage(msg);
		this.initCause(t);
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
