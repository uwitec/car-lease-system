/**  
 * @Filename:    ReletServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-14 下午02:22:55  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Relet;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.ReletService;

@Service("ReletServiceImpl")
public class ReletServiceImpl implements ReletService {

	@Resource(name="hibernateDao")					
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	添加续租信息
	 * @date	2011-5-14
	 * @author	肖乾斌
	 * @param	relet
	 * @throws  ServiceException
	 *
	 */
	@Override
	public void addRelet(Relet relet) throws ServiceException {
		if("".equals(relet.getOrderFormId())){
			throw new ServiceException("续租失败,续租单据号不能为空!");
		}
		try{
			this.hibernateDao.save(relet);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Relet> getReletByOrderFormId(String orderFormId) throws ServiceException {
		try{
			String sql = "select o from " + Relet.class.getName() + " o where o.orderFormId = '" + 
							orderFormId + "'";
			return (List<Relet>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}


}
