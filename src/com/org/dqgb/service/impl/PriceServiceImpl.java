/**  
 * @Filename:    PriceServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-21 下午09:36:49  
 *
 */  

package com.org.dqgb.service.impl;

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
import com.org.dqgb.entity.Price;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.PriceService;

@Service("PriceServiceImpl")
public class PriceServiceImpl implements PriceService{

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	public Logger log = Logger.getLogger(this.getClass());
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public JSONObject getAllPriceInformation(int pageIndex,int pageSize) throws ServiceException{
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + Price.class.getName() + " o order by o.brandId,o.price desc";
			List<Price> list = (List<Price>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < arr.size(); i++){
				arr.getJSONObject(i).put(Constant.BRAND_NAME, dictionaryService.getDictionaryById(
						list.get(i).getBrandId()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public JSONObject getCategoryByBrand(int pageIndex,int pageSize,int brand) throws ServiceException{
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + Price.class.getName() + " o where o.brandId = '" + brand + "'";
			List<Price> list = (List<Price>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < arr.size(); i++){
				arr.getJSONObject(i).put(Constant.BRAND_NAME, dictionaryService.getDictionaryById(
						list.get(i).getBrandId()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	
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
	public boolean addPriceInformation(Price price) throws ServiceException{
		if(null == price || 0 == price.getBrandId()){
			throw new ServiceException("价格信息添加失败!");
		}
		if(this.getPriceByCategory(price.getBrandId(), price.getCarCategory()) != null){
			throw new ServiceException("添加失败,不能重复添加价格信息!");
		}
		try{
			this.hibernateDao.save(price);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}
	
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
	public boolean modifyPriceInformation(Price price) throws ServiceException{
		if(null == price || 0 == price.getBrandId()){
			throw new ServiceException("价格信息修改失败!");
		}
		if(this.hibernateDao.getObjectByID(Price.class, price.getId()) == null){
			throw new ServiceException("价格信息修改失败!,没有找到id(" + price.getId() + ")对应的价格信息。");
		}
		Price p = this.getPriceByCategory(price.getBrandId(), price.getCarCategory());
		if(p != null && p.getId() != price.getId()){
			throw new ServiceException("修改失败,该价格信息已经存在!");
		}
		try{
			this.hibernateDao.saveOrUpdate(price);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}
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
	public boolean deletePriceInformation(Price price) throws ServiceException{
		if(null == price || 0 == price.getId()){
			throw new ServiceException("价格信息删除失败,目标对象不存在！");
		}
		try{
			this.hibernateDao.delete(price);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}
	
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public Price getPriceByCategory(int brand,String category)throws ServiceException{
		Price price = null;
		try{
			List<Price> list = (List<Price>) this.hibernateDao.getObjectsList("select o from " + Price.class.getName() + " o where " +
					"o.brandId = '" + brand + "' and o.carCategory = '" + category + "'" ,0, 10, true);
			if(null == list || list.size() == 0){
				return null;
			}else{
				price = list.get(0);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return price;
	}

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
	@Override
	public Price getPriceById(int id) throws ServiceException {
		Price price = null;
		try{
			price = (Price) this.hibernateDao.getObjectByID(Price.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return price;
	}

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
	@SuppressWarnings("unchecked")
	@Override
	public List<Price> getPriceByBrandId(int brandId) throws ServiceException {
		List<Price> list = null;
		try{
			list = (List<Price>) this.hibernateDao.getObjectsByHsql("select o from " + Price.class.getName() + "o where " +
					"o.brandId = '" + brandId + "'");
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}
}
