/**  
 * @Filename:    RunRecordServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-3 下午02:29:21  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.RunRecord;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.RunRecordService;
import com.org.dqgb.util.DateUtil;

@Service("RunRecordServiceImpl")
public class RunRecordServiceImpl implements RunRecordService {

	@Resource @Qualifier("hibernateDao")					
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	根据订单号获取车辆出行记录信息
	 * @date	2011-5-3
	 * @author	肖乾斌
	 * @param	orderFormId
	 * @throws  ServiceException
	 * @return  RunRecord
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public RunRecord getRunRecordByOrderFormID(String orderFormId)
			throws ServiceException {
		try{
			String sql = "select o from " + RunRecord.class.getName() + " o where o.orderFormSerialNumber = '" + 
								orderFormId + "'";
			List<RunRecord> list = (List<RunRecord>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e); 
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 根据日期、车牌号查询出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-3
	 * @param 	carId
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	RunRecord
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public RunRecord getRunRecord(String carId, Date date)
			throws ServiceException {
		try{
			String dt = DateUtil.formatDate(date, "yyyy-M-d HH:mm:ss");
			List<RunRecord> list = (List<RunRecord>) this.hibernateDao.getObjectsByHsql("select o from " + RunRecord.class.getName() + " o " +
					"where o.carId = '" + carId + "' and o.dateBegin <= '" + dt + "' and o.dateEnd >= '" + dt + "'");
			return list.size() != 1 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
