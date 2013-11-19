/**  
 * @Filename:    PriceAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-21 下午10:07:41  
 *
 */  

package com.org.dqgb.action;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Car;
import com.org.dqgb.entity.Price;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.PriceService;

@Controller("PriceAction") @Scope("prototype")
@SuppressWarnings("serial")
public class PriceAction extends PrimaryAction {

	@Resource @Qualifier("PriceServiceImpl")
	public PriceService priceService;
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	@Resource(name="CarServiceImpl")
	public CarService cs;
	
	public Car car;
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	/**
	 *
	 * @TODO	获取所有车辆价格信息
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getPriceInformation(){
		try{
			JSONObject json = this.priceService.getAllPriceInformation(
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);	
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转修改价格界面
	 * @date	2011-3-21
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyPricePage(){
		try{
			String id = this.getHttpServletRequest().getParameter("id");
			Price price = this.priceService.getPriceById(Integer.parseInt(id));
			getHttpSession().setAttribute("PRICE", price);
			getHttpSession().setAttribute(Constant.BRAND_NAME, 
					dictionaryService.getDictionaryById(price.getBrandId()).getRemark());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改价格信息
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyPrice(){
		try{
			this.cs.modifyCarPrice(car);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);	
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	新增车辆价格信息
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addPrice(){
		try{
			String brandId = this.getHttpServletRequest().getParameter("brandId");
			String price = this.getHttpServletRequest().getParameter("price");
			String carCategory = this.getHttpServletRequest().getParameter("carCategory");		//车系列
			String foregift = this.getHttpServletRequest().getParameter("foregift");		//车系列
			Price p = new Price();
			p.setBrandId(Integer.parseInt(brandId));
			p.setPrice(Integer.parseInt(price));
			p.setForegift(Integer.parseInt(foregift));
			p.setCarCategory(carCategory);
			p.setPricePerHour(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pricePerHour")));
			p.setPricePerKm(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pricePerKm")));
			p.setPriceRM(NumberUtils.toInt(this.getHttpServletRequest().getParameter("priceRM")));
			p.setPricePerHourRM(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pricePerHourRM")));
			p.setPricePerKmRM(NumberUtils.toInt(this.getHttpServletRequest().getParameter("pricePerKmRM")));
			this.priceService.addPriceInformation(p);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);	
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	删除车辆价格信息
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deletePrice(){
		try{
			String id = this.getHttpServletRequest().getParameter("id");
			this.priceService.deletePriceInformation(this.priceService.getPriceById(Integer.parseInt(id)));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);	
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	根据车的品牌信息获取车的系列信息
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCarCategoryByBrand(){
		try{
			String brandId = this.getHttpServletRequest().getParameter("brandId");
			String pageIndex = this.getHttpServletRequest().getParameter("pageIndex");
			String pageSize = this.getHttpServletRequest().getParameter("pageSize");
			JSONObject json = this.priceService.getCategoryByBrand(
					Integer.parseInt(pageIndex), 
					Integer.parseInt(pageSize), Integer.parseInt(brandId));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);	
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}

}
