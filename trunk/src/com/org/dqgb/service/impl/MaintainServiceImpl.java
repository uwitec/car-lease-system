/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-31</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-31	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.MaintainRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.MaintainService;
import com.org.dqgb.util.DateUtil;

@Service("MaintainServiceImpl")
public class MaintainServiceImpl implements MaintainService {

	@Resource(name="hibernateDao")					
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 * 
	 * @TODO	增添养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	mr
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void addMaintainRecord(MaintainRecord mr) throws ServiceException {
		try{
			this.hibernateDao.save(mr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 
	 * @TODO	修改养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	mr
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void modifyMaintainRecord(MaintainRecord mr,User operator) throws ServiceException {
		if(!mr.getTransactor().equals(operator.getUserName())){
			throw new ServiceException("你没有权限修改别人登记的养护记录");
		}
		try{
			this.hibernateDao.update(mr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	根据carId查询养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMaintainRecord(int pageIndex, int pageSize,
			String carId) throws ServiceException {
		try{
			String sql = "select o from " + MaintainRecord.class.getName() + " o where o.carId = '" + carId + "' order by o.maintainDate desc";
			List<MaintainRecord> list = (List<MaintainRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	查询所有的养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMaintainRecord(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select o from " + MaintainRecord.class.getName() + " o order by o.maintainDate desc";
			List<MaintainRecord> list = (List<MaintainRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	删除养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void deleteMaintainRecord(int id) throws ServiceException {
		try{
			MaintainRecord mr = (MaintainRecord) this.hibernateDao.getObjectByID(MaintainRecord.class, id);
			if(null != mr)
				this.hibernateDao.delete(mr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 
	 * @TODO	获取养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	MaintainRecord
	 */
	@Override
	public MaintainRecord getMaintainRecord(int id) throws ServiceException {
		try{
			return (MaintainRecord) this.hibernateDao.getObjectByID(MaintainRecord.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 	
	 * @TODO	统计每月的养护费用
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @param 	date
	 * @throws ServiceException
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getRepairFee(Date date) throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_MAINTAIN_FEE_SUM(?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			while(rs.next()){
				return rs.getInt("TOTAL");
			}
			return 0;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}
}
