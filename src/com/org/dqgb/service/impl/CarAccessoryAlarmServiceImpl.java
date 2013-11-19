/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-10-1</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-10-1	    肖乾斌	    Created</li>
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
import com.org.dqgb.entity.CarAccessoryAlarm;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CarAccessoryAlarmService;
import com.org.dqgb.service.CarService;
import com.org.dqgb.util.DateUtil;

@Service("CarAccessoryAlarmServiceImpl")
public class CarAccessoryAlarmServiceImpl implements CarAccessoryAlarmService{

	public Logger log = Logger.getLogger(this.getClass());

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("CarServiceImpl")
	public CarService carService;
	/**
	 * 添加一条预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	caa
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void add(CarAccessoryAlarm caa) throws ServiceException {
		try{
			this.hibernateDao.save(caa);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 添加一辆车的预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	lcaa
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void add(List<CarAccessoryAlarm> lcaa) throws ServiceException {
		try{
			for(int i = 0; i < lcaa.size(); i++){
				CarAccessoryAlarm caa = lcaa.get(i);
				if("0".equals(caa.getRemindWay().toString())){
					if(null == caa.getKmInterval() || "0".equals(caa.getKmInterval())){
						continue;
					}
					Car car = caa.getCar();//this.carService.getCarByID(caa.getCarId());
					caa.setNextKm(caa.getKmInterval() + car.getKm());
				}else{
					if(null == caa.getDateInterval() || "0".equals(caa.getDateInterval())){
						continue;
					}
					Date dt = new Date();
					dt.setMonth(dt.getMonth() + caa.getDateInterval());
					caa.setNextDate(dt);
				}
				List<CarAccessoryAlarm> l =  (List<CarAccessoryAlarm>) this.hibernateDao.getObjectsByHsql("select o from " + CarAccessoryAlarm.class.getName() + " o where " +
						"o.car.id = '" + caa.getCar().getId() + "' and o.item = '" + caa.getItem() + "'");
				if(l.size() == 0){
					this.hibernateDao.save(caa);
				}else{
					CarAccessoryAlarm ca = l.get(0);
					ca.setRemindWay(caa.getRemindWay());
					if("0".equals(caa.getRemindWay().toString())){
						if(!ca.getKmInterval().equals(caa.getKmInterval())){
							ca.setKmInterval(caa.getKmInterval());
							ca.setNextKm(caa.getKmInterval() + ca.getCar().getKm());
						}
					}else{
						if(!ca.getDateInterval().equals(caa.getDateInterval())){
							ca.setDateInterval(caa.getDateInterval());
							ca.setNextDate(caa.getNextDate());
						}
					}
					this.hibernateDao.update(ca);
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取车辆配置
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	carId
	 * @throws ServiceException
	 * @return 	List<CarAccessoryAlarm>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public List<CarAccessoryAlarm> getConfig(int carId) throws ServiceException {
		try{
			List<CarAccessoryAlarm> list = (List<CarAccessoryAlarm>) this.hibernateDao.getObjectsByHsql("select o from " + CarAccessoryAlarm.class.getName() + " o where " +
					"o.car.id = '" + carId + "'");
			return list;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 获取预警信息的条数
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @throws 	ServiceException
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getAccessoryAICount() throws ServiceException {
		try{
			String sql = "select o from " + CarAccessoryAlarm.class.getName() + " o where (o.remindWay = '0' " +
					"and o.nextKm <= o.car.km) or (o.remindWay = '1' and o.nextDate <= '" +
					DateUtil.formatDate(new Date(), "yyyy-M-d H:mm:ss") + "')";
			return this.hibernateDao.getCount(sql, true);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @throws 	ServiceException
	 * @return 	int
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getCarAlarmInformation(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select o from " + CarAccessoryAlarm.class.getName() + " o where (o.remindWay = '0' " +
						"and o.nextKm <= o.car.km) or (o.remindWay = '1' and o.nextDate <= '" +
						DateUtil.formatDate(new Date(), "yyyy-M-d H:mm:ss") + "')";
			JSONObject json = new JSONObject();
			List<CarAccessoryAlarm> list = (List<CarAccessoryAlarm>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 处理预警
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@SuppressWarnings("deprecation")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void handleAlarm(int id) throws ServiceException {
		try{
			CarAccessoryAlarm caa = (CarAccessoryAlarm) this.hibernateDao.getObjectByID(CarAccessoryAlarm.class, id);
			if("0".equals(caa.getRemindWay().toString())){
				caa.setNextKm(caa.getKmInterval() + caa.getCar().getKm());
			}else{
				Date dt = new Date();
				dt.setMonth(caa.getNextDate().getMonth() + caa.getDateInterval());
				caa.setNextDate(dt);
			}
			this.hibernateDao.update(caa);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

}
