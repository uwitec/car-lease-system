/**  
 * @Filename:    CarServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-17 下午09:10:56  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.MaintainRecord;
import com.org.dqgb.entity.Organization;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.MaintainService;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.PriceService;

@Service("CarServiceImpl")
public class CarServiceImpl implements CarService {

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	@Resource @Qualifier("PriceServiceImpl")
	public PriceService priceService;
	
	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	@Resource(name="MaintainServiceImpl")
	public MaintainService ms;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	获取当前系统中的车
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject	车辆信息以及总数信息，以及json格式存储
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getAllCars(int pageIndex, int pageSize)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			JSONArray arr = null;
			String sql = "select o from " + Car.class.getName() + " o order by o.brandId,o.price desc";
			int count = 0;
			count = this.hibernateDao.getCount(sql, true);
			List<Car> list = (List<Car>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put(Constant.CAR_STATUS_REMARK, dictionaryService.getDictionaryById(
						list.get(i).getStatus()).getRemark());
				String brandName = dictionaryService.getDictionaryById(list.get(i).getBrandId()).getRemark();
				arr.getJSONObject(i).put(Constant.BRAND_NAME, brandName);
				arr.getJSONObject(i).put(Constant.CATEGORY, list.get(i).getCarCategory());
				Organization org = this.organizationService.getOrganizationById(list.get(i).getOrganizationId());	//增加组织名
				arr.getJSONObject(i).put(Constant.DEPARTMENT_NAME,org.getDepartmentName());
				arr.getJSONObject(i).put("carSourceRemark", this.dictionaryService.getDictionaryById(list.get(i).getCarSource()).getRemark());
				sql = "select o from " + LeaseRecord.class.getName() + " o where  o.carId = '" + list.get(i).getCarId() + "' and o.leaseRecordStatus = '" + 
					Constant.ORDER_STATUS_BOOKING +  "'";
				arr.getJSONObject(i).put("bookCount", this.hibernateDao.getCount(sql, true));
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, count);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	
	/**
	 *
	 * @TODO	通过 id 获取车辆的信息
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  Car
	 *
	 */
	@Override
	public Car getCarByID(int id) throws ServiceException {
		Car car = null;
		try{
			car = (Car) this.hibernateDao.getObjectByID(Car.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return car;
	}

	/**
	 *
	 * @TODO	添加车辆信息
	 * @date	2011-3-18
	 * @author	肖乾斌
	 * @param	car
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean addCar(Car car) throws ServiceException {
		if(car == null){
			throw new ServiceException("车辆信息不能为空！");
		}
		if("".equals(car.getCarId()) || null == car.getCarId()){
			throw new ServiceException("车牌号不能为空！");
		}
		if("".equals(car.getOwnerName()) || null == car.getOwnerName()){
			throw new ServiceException("车主信息不能为空！");
		}
		if("".equals(car.getMobilePhone()) || null == car.getMobilePhone()){
			throw new ServiceException("车主联系方式不能为空！");
		}
		if(this.getCarByCarId(car.getCarId()) != null){
			throw new ServiceException("添加失败,该车牌号的车已经存在！");
		}
		if(car.getOwnerIdCarNumber() == null || "".equals(car.getOwnerIdCarNumber())){
			throw new ServiceException("车主证件号不能为空！");
		}
		if(car.getBodyNumber() == null || "".equals(car.getBodyNumber())){
			throw new ServiceException("车架件号不能为空！");
		}
		if(car.getEngineNumber() == null || "".equals(car.getEngineNumber())){
			throw new ServiceException("发动机编号不能为空！");
		}
		try{
			this.hibernateDao.save(car);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	通过 车牌号  获取车辆的信息
	 * @date	2011-3-18
	 * @author	肖乾斌
	 * @param	carId 			车牌号
	 * @throws  ServiceException
	 * @return  Car
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public Car getCarByCarId(String carId) throws ServiceException {
		Car car = null;
		try{
			List<Car> list = (List<Car>) this.hibernateDao.getObjectsList("select o from " + 
					Car.class.getName() + " o where o.carId = '" + carId + "'", 0, 999, true);
			if(list == null || list.size() == 0){
				return null;
			}
			car = list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return car;
	}

	/**
	 *
	 * @TODO	修改车辆信息，不能修改车牌号
	 * @date	2011-3-19
	 * @author	肖乾斌
	 * @param	car
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean modifyCarInformation(Car car) throws ServiceException {
		if(car == null){
			throw new ServiceException("车辆信息不能为空！");
		}
		if("".equals(car.getCarId()) || null == car.getCarId()){
			throw new ServiceException("车牌号不能为空！");
		}
		Car orgCar = this.getCarByCarId(car.getCarId());
		if(orgCar == null){
			throw new ServiceException("修改信息失败,没有车牌号为 '" + car.getCarId() + "' 的车辆！");
		}
		if("".equals(car.getOwnerName()) || null == car.getOwnerName()){
			throw new ServiceException("车主信息不能为空！");
		}else{
			orgCar.setOwnerName(car.getOwnerName());
		}
		if("".equals(car.getMobilePhone()) || null == car.getMobilePhone()){
			throw new ServiceException("车主联系方式不能为空！");
		}else{
			orgCar.setMobilePhone(car.getMobilePhone());
		}
		if(car.getBodyNumber() == null || "".equals(car.getBodyNumber())){
			throw new ServiceException("车架件号不能为空！");
		}
		if(car.getEngineNumber() == null || "".equals(car.getEngineNumber())){
			throw new ServiceException("发动机编号不能为空！");
		}
		if(car.getOwnerIdCarNumber() == null || "".equals(car.getOwnerIdCarNumber())){
			throw new ServiceException("车主证件号不能为空！");
		}else{
			orgCar.setOwnerIdCarNumber(car.getOwnerIdCarNumber());
		}
		try{
			orgCar.setBodyNumber(car.getBodyNumber());
			orgCar.setEngineNumber(car.getEngineNumber());
			orgCar.setCarSource(car.getCarSource());
			orgCar.setCarCategory(car.getCarCategory());
			orgCar.setImagePath(car.getImagePath());
			if(car.getOrganizationId() != 0)
				orgCar.setOrganizationId(car.getOrganizationId());
			this.hibernateDao.saveOrUpdate(orgCar);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 * 修改车的价格信息
	 * @author 	肖乾斌
	 * @date 	2011-8-27
	 * @param 	car
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void modifyCarPrice(Car car)throws ServiceException{
		try{
			Car oc = this.getCarByCarId(car.getCarId());
			oc.setPrice(car.getPrice());
			oc.setForegift(car.getForegift());
			oc.setPricePerHour(car.getPricePerHour());
			oc.setPricePerHourRM(car.getPricePerHourRM());
			oc.setPricePerKm(car.getPricePerKm());
			oc.setPriceRM(car.getPriceRM());
			this.hibernateDao.saveOrUpdate(oc);
		}catch(Exception e){
			log.error(e.getMessage(), e); 
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 *
	 * @TODO	按条件查询车辆信息
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	con					条件
	 * @param	orderBy				排序
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getCarsByCondition(int pageIndex, int pageSize,
			Map<String, Object> con,Map<String, String> orderBy) throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			if(con.size() == 0){
				return this.getAllCars(pageIndex, pageSize);
			}
			List<Car> list = new ArrayList<Car>();
			Map<String, Object> var = new HashMap<String, Object>();
			
			if(null != con.get("carId") && !"".equals(con.get("carId"))){					//按牌照查找
				var.put("carId", con.get("carId"));
				if(null != con.get("status") && 0 != (Integer)con.get("status")){
					var.put("status", con.get("status"));
				}
				if(null != con.get("organizationId") && 0 != (Integer)con.get("organizationId")){
					var.put("organizationId", con.get("organizationId"));
				}
				Map<String,Object> map = this.hibernateDao.getListByCondition(Car.class, var,null, pageIndex, pageSize);
				list = (List<Car>) map.get(Constant.DATA);
				json.put(Constant.TOTAL_SIZE, map.get(Constant.TOTAL_SIZE));
			}else if(null != con.get("brandId") && 0 != (Integer)con.get("brandId")){		//按品牌查找
				String sql = "select o from " + Car.class.getName() + " o  where o.brandId = '" + con.get("brandId").toString() + "'";
				if(null != con.get("status") && 0 != (Integer)con.get("status")){
					sql += " and o.status = '" + con.get("status") + "' ";
				}
				if(null != con.get("organizationId") && 0 != (Integer)con.get("organizationId")){
					sql += " and o.organizationId = '" + con.get("organizationId") + "' ";
				}
				List<?> obj = this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
				for(int i = 0; i < obj.size(); i++){
					Object o = (Object) obj.get(i);
					Car _car = (Car) o;
					list.add(_car);
				}
				json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			}else if(null != con.get("priceId") && 0 != (Integer)con.get("priceId")){		//按车系查找
				String sql = "select o from " + Car.class.getName() + " o where o.priceId = '" + con.get("priceId").toString() + "' and o.id=o.id ";
				if(null != con.get("status") && 0 != (Integer)con.get("status")){
					sql += " and o.status = '" + con.get("status") + "' ";
				}
				if(null != con.get("organizationId") && 0 != (Integer)con.get("organizationId")){
					sql += " and o.organizationId = '" + con.get("organizationId") + "' ";
				}
				list = (List<Car>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
				json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			}else if(null != con.get("status") && 0 != (Integer)con.get("status")){			//按状态查找
				String sql = "select o  from " + Car.class.getName() + " o  where o.status = '" + con.get("status") + "' " ;
				if(null != con.get("organizationId") && 0 != (Integer)con.get("organizationId")){
					sql += " and o.organizationId = '" + con.get("organizationId") + "' ";
				}
				sql += " order by o.brandId";
				List<?> obj = this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
				for(int i = 0; i < obj.size(); i++){
					Object o = (Object) obj.get(i);
					Car _car = (Car) o;
					list.add(_car);
				}
				json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			}else if(null != con.get("organizationId") && 0 != (Integer)con.get("organizationId")){
				String sql = "select o  from " + Car.class.getName() + " o  where o.organizationId = '" + con.get("organizationId") + "' ";
				if(null != con.get("status") && 0 != (Integer)con.get("status")){
					sql += " and o.status = '" + con.get("status") + "' ";
				}
				sql += " order by o.brandId";
				List<?> obj = this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
				for(int i = 0; i < obj.size(); i++){
					Object o = obj.get(i);
					Car _car = (Car) o;
					list.add(_car);
				}
				json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			}
			
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put(Constant.CAR_STATUS_REMARK, dictionaryService.getDictionaryById(
						list.get(i).getStatus()).getRemark());
				String brandName = dictionaryService.getDictionaryById(list.get(i).getBrandId()).getRemark();
				arr.getJSONObject(i).put(Constant.BRAND_NAME, brandName);
				arr.getJSONObject(i).put(Constant.CATEGORY, list.get(i).getCarCategory());
				
				arr.getJSONObject(i).put(Constant.PRICE,list.get(i).getPrice());
				arr.getJSONObject(i).put(Constant.FOREGIFT,list.get(i).getForegift());
				arr.getJSONObject(i).put(Constant.PRICE_PER_HOUR,list.get(i).getPricePerHour());
				
				Organization org = this.organizationService.getOrganizationById(list.get(i).getOrganizationId());//增加组织名字
				arr.getJSONObject(i).put(Constant.DEPARTMENT_NAME,org.getDepartmentName());
				arr.getJSONObject(i).put("carSourceRemark", this.dictionaryService.getDictionaryById(list.get(i).getCarSource()).getRemark());
				String sql = "select o from " + LeaseRecord.class.getName() + " o where  o.carId = '" + list.get(i).getCarId() + "' and o.leaseRecordStatus = '" + 
					Constant.ORDER_STATUS_BOOKING +  "'";
				arr.getJSONObject(i).put("bookCount", this.hibernateDao.getCount(sql, true));
			}
			json.put(Constant.DATA, arr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 * 
	 * @TODO	维修
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void repair(int id,User operator) throws ServiceException {
		Car car = this.getCarByID(id);
		if(car.getStatus() != Constant.CAR_STATUS_IDLE){
			throw new ServiceException("只有处于空闲状态的车辆才能报修!");
		}
		try{
			car.setStatus(Constant.CAR_STATUS_REPAIR);
			this.hibernateDao.update(car);
			MaintainRecord mr = new MaintainRecord();
			mr.setCarId(car.getCarId());
			mr.setMaintainDate(new Date());
			mr.setRemark("送修");
			mr.setTransactor(operator.getUserName());
			mr.setTransactorName(operator.getRealName());
			this.hibernateDao.save(mr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 
	 * @TODO	送修完毕后把车送回车库
	 * @author 	肖乾斌
	 * @date 	2011-6-1
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void giveBackCar(int id) throws ServiceException {
		Car car = this.getCarByID(id);
		if(car.getStatus() != Constant.CAR_STATUS_REPAIR){
			throw new ServiceException("只有处于报修状态的车辆才能执行该操作!");
		}
		try{
			car.setStatus(Constant.CAR_STATUS_IDLE);
			this.hibernateDao.update(car);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

}
