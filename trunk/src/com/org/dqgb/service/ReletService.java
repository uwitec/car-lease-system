/**  
 * @Filename:    ReletService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-14 下午02:19:34  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import com.org.dqgb.entity.Relet;
import com.org.dqgb.exception.ServiceException;

public interface ReletService {

	/**
	 *
	 * @TODO	
	 * @date	2011-5-14
	 * @author	肖乾斌
	 * @param	relet
	 * @throws  ServiceException
	 *
	 */
	public abstract void addRelet(Relet relet)throws ServiceException;
	
	
	/**
	 *
	 * @TODO	根据订单号获取续租信息
	 * @date	2011-5-14
	 * @author	肖乾斌
	 * @param	orderFormId
	 * @throws  ServiceException
	 * @return  List<Relet>
	 *
	 */
	public abstract List<Relet> getReletByOrderFormId(String orderFormId)throws ServiceException;
}
