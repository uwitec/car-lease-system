/**  
 * @Filename:    CarAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-16 下午09:04:43  
 *
 */  

package com.org.dqgb.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Car;
import com.org.dqgb.entity.CarAccessoryAlarm;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.CarAccessoryAlarmService;
import com.org.dqgb.service.CarService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.PriceService;

@Controller("CarAction") @Scope("prototype")
@SuppressWarnings("serial")
public class CarAction extends PrimaryAction {
	
	@Resource @Qualifier("CarServiceImpl")
	public CarService carService;

	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	@Resource @Qualifier("CarAccessoryAlarmServiceImpl")
	public CarAccessoryAlarmService caaService;
	
	@Resource @Qualifier("PriceServiceImpl")
	public PriceService priceService;
	
	
	/**
	 *
	 * @TODO	获取车辆信息
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCar(){
		try{
			JSONObject json = this.carService.getAllCars(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	预览车辆照片
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String browseCar(){
		try{
			String id = this.getHttpServletRequest().getParameter("id");
			Car car = this.carService.getCarByID(Integer.parseInt(id));
			getHttpSession().setAttribute("CAR", car);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	增添车辆信息
	 * @date	2011-3-18
	 * @author	肖乾斌
	 * @param	@return
	 * @return  String
	 *
	 */
	public String addCar(){
		try{
			Car car = new Car();
			car.setCarId(this.getHttpServletRequest().getParameter("carId"));
			car.setImagePath(this.getHttpServletRequest().getParameter("imagePath"));
			car.setMobilePhone(this.getHttpServletRequest().getParameter("mobilePhone"));
			car.setOwnerName(this.getHttpServletRequest().getParameter("ownerName"));
			car.setOwnerIdCarNumber(this.getHttpServletRequest().getParameter("ownerIdCarNumber"));
			car.setEngineNumber(this.getHttpServletRequest().getParameter("engineNumber"));
			car.setBodyNumber(this.getHttpServletRequest().getParameter("bodyNumber"));
			car.setBrandId(NumberUtils.toInt(this.getHttpServletRequest().getParameter("brandId")));
			car.setCarCategory(this.getHttpServletRequest().getParameter("carCategory"));
			car.setCarSource(Integer.parseInt(this.getHttpServletRequest().getParameter("carSource")));
			car.setKm(NumberUtils.toInt(this.getHttpServletRequest().getParameter("km")));
			User user = (User) this.getHttpSession().getAttribute(Constant.CURRENT_USER);
			car.setOrganizationId(user.getDepartmentId());
			car.setRegisterDate(new Date());
			car.setStatus(Constant.CAR_STATUS_IDLE);
			this.carService.addCar(car);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到车辆资料维护界面
	 * @date	2011-3-19
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoCarModifyPage(){
		try{
			String id = getHttpServletRequest().getParameter("id");
			Car car = this.carService.getCarByID(Integer.parseInt(id));
			
			this.getHttpSession().setAttribute("CAR", car);
			this.getHttpSession().setAttribute("CATEGORY", car.getCarCategory());
			this.getHttpSession().setAttribute("BRAND_NAME", 
					this.dictionaryService.getDictionaryById(car.getBrandId()).getRemark());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改车辆信息
	 * @date	2011-3-19
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyCarInformation(){
		try{
			Car car = new Car();
			car.setCarId(this.getHttpServletRequest().getParameter("carId"));
			car.setImagePath(this.getHttpServletRequest().getParameter("imagePath"));
			car.setMobilePhone(this.getHttpServletRequest().getParameter("mobilePhone"));
			car.setOwnerName(this.getHttpServletRequest().getParameter("ownerName"));
			car.setBrandId(NumberUtils.toInt(this.getHttpServletRequest().getParameter("brandId")));
			car.setCarCategory(this.getHttpServletRequest().getParameter("carCategory"));
			car.setOwnerIdCarNumber(this.getHttpServletRequest().getParameter("ownerIdCarNumber"));
			car.setEngineNumber(this.getHttpServletRequest().getParameter("engineNumber"));
			car.setBodyNumber(this.getHttpServletRequest().getParameter("bodyNumber"));
			car.setCarSource(Integer.parseInt(this.getHttpServletRequest().getParameter("carSource")));
			car.setStatus(Constant.CAR_STATUS_IDLE);
			this.carService.modifyCarInformation(car);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	按条件查询车辆
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCarByCondition(){
		try{
			Map<String,Object> map = new HashMap<String, Object>();
			String carId = this.getHttpServletRequest().getParameter("carId");
			String brandId = this.getHttpServletRequest().getParameter("brandId");
			String pageIndex = this.getHttpServletRequest().getParameter("pageIndex");
			String pageSize = this.getHttpServletRequest().getParameter("pageSize");
			String priceId = this.getHttpServletRequest().getParameter("priceId");
			String status = this.getHttpServletRequest().getParameter("status");
			String order = this.getHttpServletRequest().getParameter("order");
			String direction = this.getHttpServletRequest().getParameter("direction");
			String organizationId = this.getHttpServletRequest().getParameter("organizationId");
			
			Map<String,String> orderBy = new HashMap<String, String>();
			if(null != order && !"".equals(order)){
				orderBy.put(order, direction);
			}
			
			if(null != carId && !"".equals(carId))
				map.put("carId", carId);								//车牌号
			if(null != brandId && !"".equals(brandId))
				map.put("brandId", Integer.parseInt(brandId));			//品牌
			if(null != priceId && !"".equals(priceId))
				map.put("priceId", Integer.parseInt(priceId));			//系列
			if(null != status && !"".equals(status))
				map.put("status", Integer.parseInt(status));			//系列
			if(null != organizationId && !"".equals(organizationId))
				map.put("organizationId", Integer.parseInt(organizationId));			//驻场信息
			
			JSONObject json = this.carService.getCarsByCondition(Integer.parseInt(pageIndex), 
					Integer.parseInt(pageSize), map, orderBy);
			
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 跳转到发车界面
	 * @author 	肖乾斌
	 * @date 	2011-8-27
	 * @return 	String
	 */
	public String gotoSetOutCarPage(){
		try{
			this.getHttpServletRequest().setAttribute("setOutCarId", new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		}catch (Exception e) {
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取车源种类
	 * @date	2011-5-12
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getCarSource(){
		try{
			output.put(Constant.DATA, this.dictionaryService.getDictionaryByGroupId(Constant.GROUP_CAR_TYPE, 
					0, 1000));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到收车界面
	 * @date	2011-5-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoGivebackCarPage(){
		try{
			this.getHttpSession().setAttribute("carId",  new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		}catch(Exception e){
			log.error("跳转到收车界面时发生错误，错误原因：" + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	维修
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String repair(){
		try{
			this.carService.repair(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")),
					this.getCurrentUser());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	入库
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @return 	String
	 */
	public String giveBack(){
		try{
			this.carService.giveBackCar(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	修改驻场
	 * @author 	肖乾斌
	 * @date 	2011-6-21
	 * @return 	String
	 */
	public String modifyCarOrg(){
		try{
			Car car = this.carService.getCarByID(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")));
			car.setOrganizationId(NumberUtils.toInt(this.getHttpServletRequest().getParameter("organizationId")));
			this.carService.modifyCarInformation(car);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 添加车辆零部件预警
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @return 	String
	 */
	public String addCarAccessoryAlarm(){
		try{
			JSONArray arr = JSONArray.fromObject(this.getHttpServletRequest().getParameter("data"));
			List<CarAccessoryAlarm>  list = new ArrayList<CarAccessoryAlarm>();
			for(int i = 0; i < arr.size(); i++){
				CarAccessoryAlarm caa = (CarAccessoryAlarm) JSONObject.toBean(arr.getJSONObject(i), CarAccessoryAlarm.class);
				Car car = new Car();
				car.setId(NumberUtils.toInt(this.getHttpServletRequest().getParameter("carId")));
				caa.setCar(car);
				list.add(caa);
			}
			this.caaService.add(list);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取车辆零部件预警
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @return 	String
	 */
	public String getConfig(){
		try{
			List<CarAccessoryAlarm>  list = this.caaService.getConfig(NumberUtils.toInt(this.getHttpServletRequest().getParameter("carId")));
			output.put(Constant.DATA,list);
			output.put(Constant.TOTAL_SIZE,list.size());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取车辆零部件预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @return 	String
	 */
	public String getAccessoryAI(){
		try{
			output.put(Constant.TOTAL_SIZE,this.caaService.getAccessoryAICount());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取车辆零部件预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @return 	String
	 */
	public String getCarAlarmInformation(){
		try{
			output.put(Constant.DATA,this.caaService.getCarAlarmInformation(NumberUtils.toInt(
					this.getHttpServletRequest().getParameter("pageIndex")), NumberUtils.toInt(this.
							getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 处理预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @return 	String
	 */
	public String handleAlarm(){
		try{
			this.caaService.handleAlarm(NumberUtils.toInt(this.getHttpServletRequest().getParameter("id")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
}
