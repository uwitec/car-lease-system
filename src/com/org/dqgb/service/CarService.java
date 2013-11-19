/**  
 * @Filename:    CarService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-16 下午09:41:09  
 *
 */  

package com.org.dqgb.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Car;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface CarService {

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
	public abstract JSONObject getAllCars(int pageIndex, int pageSize) throws ServiceException;
	
	/**
	 * 修改车的价格信息
	 * @author 	肖乾斌
	 * @date 	2011-8-27
	 * @param 	car
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void modifyCarPrice(Car car)throws ServiceException;
	/**
	 *
	 * @TODO	按条件查询车辆信息,如果条件为空(con.size() == 0)则等同调用getAllCars
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
	public abstract JSONObject getCarsByCondition(int pageIndex, int pageSize,Map<String,Object> con,Map<String,String> orderBy) throws ServiceException;
	
	
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
	public abstract Car getCarByID(int id) throws ServiceException;
	
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
	public abstract Car getCarByCarId(String carId)throws ServiceException;
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
	public abstract boolean addCar(Car car) throws ServiceException;
	
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
	public abstract boolean modifyCarInformation(Car car) throws ServiceException;
	
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
	public abstract void repair(int id,User operator)throws ServiceException;
	
	/**
	 * 
	 * @TODO	送修完毕后把车送回车库
	 * @author 	肖乾斌
	 * @date 	2011-6-1
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void giveBackCar(int id)throws ServiceException;
}
