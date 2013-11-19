/**  
 * @Filename:    PriceService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-21 下午09:30:26  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.Price;
import com.org.dqgb.exception.ServiceException;

public interface PriceService {

	/**
	 *
	 * @TODO	获取所有车辆单价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getAllPriceInformation(int pageIndex,int pageSize) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据车品牌信息获取车系信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @param	brand
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	public abstract JSONObject getCategoryByBrand(int pageIndex,int pageSize,int brand) throws ServiceException;
	
	/**
	 *
	 * @TODO	添加价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	price
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean addPriceInformation(Price price) throws ServiceException;
	
	/**
	 *
	 * @TODO	修改车辆价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	price
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean modifyPriceInformation(Price price) throws ServiceException;
	
	/**
	 *
	 * @TODO	删除车辆价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	price
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	public abstract boolean deletePriceInformation(Price price) throws ServiceException;
	
	/**
	 *
	 * @TODO	根据车的品牌以及车系唯一定位一条价格数据。
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	brand		品牌id
	 * @param	category	系列名称。
	 * @throws  ServiceException
	 * @return  Price
	 *
	 */
	public abstract Price getPriceByCategory(int brand,String category)throws ServiceException;
	
	/**
	 *
	 * @TODO	根据id获取价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  Price
	 *
	 */
	public abstract Price getPriceById(int id)throws ServiceException;
	
	/**
	 *
	 * @TODO	根据品牌id获取价格信息
	 * @date	2011-3-23
	 * @author	肖乾斌
	 * @param	brandId
	 * @throws  ServiceException
	 * @return  List<Price>
	 *
	 */
	public abstract List<Price> getPriceByBrandId(int brandId) throws ServiceException;
	
}
