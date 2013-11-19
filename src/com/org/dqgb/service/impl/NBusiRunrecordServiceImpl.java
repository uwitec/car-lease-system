/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-6</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-6	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Car;
import com.org.dqgb.entity.NonBusinessRunRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.NBusiRunrecordService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.util.DateUtil;

@Service("NBusiRunrecordServiceImpl")
@SuppressWarnings("unchecked")
public class NBusiRunrecordServiceImpl implements NBusiRunrecordService {

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="CarServiceImpl")
	public CarService cs;
	
	@Resource(name="UserServiceImpl")
	public UserService us;
	/**
	 * 添加出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	nbrr
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addRunRecord(NonBusinessRunRecord nbrr, User user)
			throws ServiceException {
		Car car = this.cs.getCarByCarId(nbrr.getCarId());
		if(null == car){
			throw new ServiceException("车牌号有误,请核实!");
		}
		if(car.getStatus() != Constant.CAR_STATUS_IDLE){
			throw new ServiceException("发车失败,该车正忙!");
		}
		try {
			nbrr.setDateBegin(new Date());
			nbrr.setKmBegin(car.getKm());
			nbrr.setTransactor(user.getUserName());
			this.hibernateDao.save(nbrr);
			car.setStatus(Constant.CAR_INNER_USE);
			this.hibernateDao.update(car);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	
	/**
	 * 根据车牌号获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param carId
	 * @throws ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public NonBusinessRunRecord getRunRecord(String carId)
			throws ServiceException {
		try {
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o where o.carId = '" + carId + "' and " +
					" ISNULL(o.dateEnd,'') = ''";
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsByHsql(sql);
			return list.size() == 0 ? null : list.get(0);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 根据id获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param id
	 * @throws ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public NonBusinessRunRecord getRunRecord(int id) throws ServiceException {
		try {
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-6
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getRunRecord(int pageIndex, int pageSize)
			throws ServiceException {
		try {
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o order by o.dateBegin desc";
			JSONObject json = new JSONObject();
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("transactorName", this.us.getUserByName(list.get(i).getTransactor()).getRealName());
				if(list.get(i).getFinisher() != null){
					arr.getJSONObject(i).put("finisherName", this.us.getUserByName(list.get(i).getFinisher()).getRealName());
				}else{
					arr.getJSONObject(i).put("finisherName", "");
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 内部用车收车
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @param 	carId
	 * @param 	user
	 * @param 	kmEnd
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void giveBackCar(int rrId, int kmEnd,User user) throws ServiceException {
		NonBusinessRunRecord nbrr = (NonBusinessRunRecord) this.hibernateDao.getObjectByID(NonBusinessRunRecord.class, rrId);
		if(nbrr == null){
			throw new ServiceException("收车失败,没有找到相关发车记录");
		}
		Car car = this.cs.getCarByCarId(nbrr.getCarId());
		if(car.getStatus() != Constant.CAR_INNER_USE){
			throw new ServiceException("收车失败,该车没有处于发车状态");
		}
		if(nbrr.getKmEnd() != 0){
			throw new ServiceException("收车失败,该单已过期！");
		}
		if(kmEnd < car.getKm()){
			throw new ServiceException("当前里程有误,请核实！");
		}
		try{
			car.setStatus(Constant.CAR_STATUS_IDLE);
			car.setKm(kmEnd);
			this.hibernateDao.update(car);
			nbrr.setDateEnd(new Date());
			nbrr.setFinisher(user.getUserName());
			nbrr.setKmEnd(kmEnd);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 查找车辆内部用车记录
	 * @author 	肖乾斌
	 * @date 	2011-9-7
	 * @param 	carId
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getRunRecord(String carId, int pageIndex, int pageSize)
			throws ServiceException {
		try {
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o where o.carId = '" + carId + "' order by o.dateBegin desc";
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsByHsql(sql);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("transactorName", this.us.getUserByName(list.get(i).getTransactor()).getRealName());
				if(list.get(i).getFinisher() != null){
					arr.getJSONObject(i).put("finisherName", this.us.getUserByName(list.get(i).getFinisher()).getRealName());
				}else{
					arr.getJSONObject(i).put("finisherName", "");
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getRunRecord(String carId, Date dt)
			throws ServiceException {
		try{
			String date = DateUtil.formatDate(dt, "yyyy-M-d HH:mm:ss");
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o where o.carId = '" + 
			carId + "' and o.dateBegin <= '" + date + "'and o.dateEnd >= '" + date + "'";
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsByHsql(sql);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("transactorName", this.us.getUserByName(list.get(i).getTransactor()).getRealName());
				if(list.get(i).getFinisher() != null){
					arr.getJSONObject(i).put("finisherName", this.us.getUserByName(list.get(i).getFinisher()).getRealName());
				}else{
					arr.getJSONObject(i).put("finisherName", "");
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 验证是否存在违章记录
	 * @author 	肖乾斌
	 * @date 	2011-9-10
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	boolean
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean isRunRecord(String carId, Date dt) throws ServiceException {
		try{
			String date = DateUtil.formatDate(dt, "yyyy-M-d HH:mm:ss");
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o where o.carId = '" + 
			carId + "' and o.dateBegin <= '" + date + "'and o.dateEnd >= '" + date + "'";
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsByHsql(sql);
			return list.size() == 0 ? false : true ;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取出行记录
	 * @author 	肖乾斌
	 * @date 	2011-9-10
	 * @param 	carId
	 * @param 	dt
	 * @throws 	ServiceException
	 * @return 	NonBusinessRunRecord
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public NonBusinessRunRecord getAccidentRunRecord(String carId, Date dt)
			throws ServiceException {
		try{
			String date = DateUtil.formatDate(dt, "yyyy-M-d HH:mm:ss");
			String sql = "select o from " + NonBusinessRunRecord.class.getName() + " o where o.carId = '" + 
			carId + "' and o.dateBegin <= '" + date + "'and o.dateEnd >= '" + date + "'";
			List<NonBusinessRunRecord> list = (List<NonBusinessRunRecord>) this.hibernateDao.getObjectsByHsql(sql);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

}
