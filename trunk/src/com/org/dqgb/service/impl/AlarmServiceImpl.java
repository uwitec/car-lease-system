/**  
 * @Filename:    AlarmServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 下午01:55:09  
 *
 */  

package com.org.dqgb.service.impl;

import java.text.SimpleDateFormat;
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

import com.org.dqgb.bean.AlarmConfiguration;
import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.AlarmParam;
import com.org.dqgb.entity.AlarmSetting;
import com.org.dqgb.entity.AlarmType;
import com.org.dqgb.entity.Customer;
import com.org.dqgb.entity.FileManagement;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.entity.ViolateSurety;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.AlarmService;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.util.DateUtil;

@Service("AlarmServiceImpl")
public class AlarmServiceImpl implements AlarmService{

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	
	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	
	@Resource @Qualifier("CustomerServiceImpl")
	public  CustomerService customerService;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	配置个人预警信息
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @param	acf
	 * @throws  ServiceException
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void setPersonalAPC(User user, List<AlarmConfiguration> acf)
			throws ServiceException {
		try{
			this.deletePersonalAPC(user);			//删除原有配置
			if(acf != null){
				for(int i = 0; i < acf.size(); i++){
					AlarmSetting as = new AlarmSetting();
					as.setAlarmType(acf.get(i).getType());
					as.setUserName(user.getUserName());
					this.hibernateDao.saveOrUpdate(as);
					if(null != acf.get(i).getApl()){
						for(int j = 0; j < acf.get(i).getApl().size(); j++){
							AlarmParam ap = acf.get(i).getApl().get(j);
							if(as.getAlarmType() == Constant.AM_TYPE_FILE){				//文件预警---提前
								ap.setAlarmMethod(Constant.AM_BEFORE);
							}else if(as.getAlarmType() == Constant.AM_TYPE_ORDER_FORM){	//订单预警--置后
								ap.setAlarmMethod(Constant.AM_AFTER);
							}else if(as.getAlarmType() == Constant.AM_TYPE_VS){			//违章保证金预警
								//到期预警
								if(ap.getParamValue().equals("0")){				
									ap.setAlarmMethod(Constant.AM_BEFORE);
								}else if(ap.getParamValue().equals("1")){		//冻结预警
									ap.setAlarmMethod(Constant.AM_AFTER);
								}
							}
							ap.setAlarmSettingId(as.getId());
							this.hibernateDao.saveOrUpdate(ap);
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 *
	 * @TODO	删除个人预警配置
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 *
	 */
	@Override
	public void deletePersonalAPC(User user) throws ServiceException {
		try{
			String sql = "delete from " + AlarmSetting.class.getName() + 
						" o where o.userName = '" + user.getUserName() + "'";
			this.hibernateDao.bulkUpdate(sql);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 *
	 * @TODO	获取个人预警配置信息
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	user
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getPersonalAPC(User user) throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			String sql = "select o from " + AlarmSetting.class.getName() + 
						" o where o.userName = '" + user.getUserName() + "'";
			List<AlarmSetting> list = (List<AlarmSetting>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			JSONArray arr = JSONArray.fromObject(list);
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, list.size());
			for(int i = 0; i < list.size(); i++){
				AlarmType  at = (AlarmType) this.hibernateDao.getObjectByID(AlarmType.class, list.get(i).getAlarmType());
				arr.getJSONObject(i).put(Constant.AM_URL,at.getUrl());
				sql = "select o from " + AlarmParam.class.getName() + 
						" o where o.alarmSettingId = '" + list.get(i).getId() + "'";
				List<AlarmParam> l = (List<AlarmParam>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
				arr.getJSONObject(i).put(Constant.PARAMETER, JSONArray.fromObject(l));
			}
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}
	/**
	 *
	 * @TODO	获取个人预警信息	AI:alarm infromation
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	user
	 * @param	acf
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getPersonalAI(User user, List<AlarmConfiguration> acf)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			for(int i = 0; i < acf.size(); i++){
				AlarmConfiguration ac = acf.get(i);
				if(Constant.AM_TYPE_FILE == ac.getType()){					//文件预警
					String sql = "select o from " + FileManagement.class.getName() + " o where ";
					for(int j = 0; j < ac.getApl().size(); j++){
						sql += " (o.fileType = '" + ac.getApl().get(j).getParamValue() + "' and o.usefulLife <= '" +
								this.getPreOrNextDate(true, ac.getApl().get(j).getDuration()) + "')";
						if(j != ac.getApl().size() - 1){
							sql += " or ";
						}
					}
					int fileCount = this.hibernateDao.getCount(sql, true);
					json.put("FILECOUNT", fileCount);
				}else if(Constant.AM_TYPE_ORDER_FORM == ac.getType()){		//订单预警
					String sql = "select o from " + LeaseRecord.class.getName() + " o where o.leaseRecordStatus = '" +
								Constant.ORDER_STATUS_FREEZED + "' ";
					for(int j = 0; j < ac.getApl().size(); j++){
						sql += " and o.dateSettle >= '" + this.getPreOrNextDate(false, ac.getApl().get(j).getDuration()) + "'"; 
					}
					int fileCount = this.hibernateDao.getCount(sql, true);
					json.put("ORDERCOUNT", fileCount);
				}else if(Constant.AM_TYPE_VS == ac.getType()){				//保证金预警
					for(int j = 0; j < ac.getApl().size(); j++){
						String sql = "select o from " + ViolateSurety.class.getName() + " o where ";
						if(ac.getApl().get(j).getParamValue().equals("0")){	//到期预警
							sql += " o.vsStatus = '0' and ISNULL(o.givebackDate,'') = '' and o.deadlineDate < '" + this.getPreOrNextDate(true, ac.getApl().get(j).getDuration()) + "'";
							json.put("REMIND_VS",this.hibernateDao.getCount(sql, true));
						}else if(ac.getApl().get(j).getParamValue().equals("1")){
							sql += " o.vsStatus = '1' and o.deadlineDate > '" + this.getPreOrNextDate(false, ac.getApl().get(j).getDuration()) + "'";;
							json.put("FREEZONE_VS",this.hibernateDao.getCount(sql, true));
						}
					}
				}
			}
			Date today = new Date();
			
			String _sql = "select o from " + LeaseRecord.class.getName() + " o where ('" + DateUtil.formatDate(today, "yyyy-M-d") + "' >= o.dateEnd and ISNULL(o.dateRelet,'') = '' and " +
					" o.leaseRecordStatus = '" + Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_BOOKING + "')" +
					" or ('" + DateUtil.formatDate(today, "yyyy-M-d") + "' >= o.dateRelet and ISNULL(o.dateRelet,'') != '' and o.leaseRecordStatus = '" + Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_BOOKING + "')";
			
			json.put("DEADLINE_ORDER",  this.hibernateDao.getCount(_sql, true));
			
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 *
	 * @TODO	获取从当前时间开始推迟或提前days天所指的日期
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	isPre ? '提前':'置后'
	 * @param	days 	天数
	 * @return  String
	 *
	 */
	public String getPreOrNextDate(boolean isPre, long days){
		Date date = new Date();
		if(isPre){
			date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
		}else{
			date.setTime(date.getTime() - days * 24 * 60 * 60 * 1000);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d hh:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 *
	 * @TODO	获取订单预警信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @param	operator
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getOrderAlarmInformation(User operator, int pageIndex,
			int pageSize) throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			
			String sql = "select o from " + AlarmSetting.class.getName() + " o where o.userName = '" + 
						operator.getUserName() + "' and o.alarmType = '" + Constant.AM_TYPE_ORDER_FORM + "'";
			List<AlarmSetting> list = (List<AlarmSetting>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			//如果没有找到配置则返回
			if(list.size() == 0){
				return null;
			}
			AlarmSetting as = list.get(0);
			sql = "select o from " + AlarmParam.class.getName() + " o where o.alarmSettingId = '" + 
					as.getId() + "'";
			List<AlarmParam> l = (List<AlarmParam>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			
			sql = "select o from " + LeaseRecord.class.getName() + " o where o.leaseRecordStatus = '" +
					Constant.ORDER_STATUS_FREEZED + "' ";
			
			for(int i = 0; i < l.size(); i++){
				sql += " and o.dateSettle >= '" + this.getPreOrNextDate(false, l.get(0).getDuration()) + "'"; 
			}
			List<LeaseRecord> lf = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(lf);
			for(int i = 0; i < lf.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(lf.get(i).getTransactor()).getRealName());
				
				//添加业务结算员真实的姓名
				if(null != lf.get(i).getFinisher() && !"".equals(lf.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(lf.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(lf.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(lf.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(lf.get(i).getLeaseRecordStatus()).getRemark());
				
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 *
	 * @TODO	获取文件档案预警信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @param	operator
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getFileAlarmInformation(User operator, int pageIndex,
			int pageSize) throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			
			String sql = "select o from " + AlarmSetting.class.getName() + " o where o.userName = '" + 
						operator.getUserName() + "' and o.alarmType = '" + Constant.AM_TYPE_FILE + "'";
			List<AlarmSetting> list = (List<AlarmSetting>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			//如果没有找到配置则返回
			if(list.size() == 0){
				return null;
			}
			AlarmSetting as = list.get(0);
			sql = "select o from " + AlarmParam.class.getName() + " o where o.alarmSettingId = '" + 
					as.getId() + "'";
			List<AlarmParam> l = (List<AlarmParam>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			
			sql = "select o from " + FileManagement.class.getName() + " o where ";
			for(int i = 0; i < l.size(); i++){
				sql += " (o.fileType = '" + l.get(i).getParamValue() + "' and o.usefulLife <= '" +
						this.getPreOrNextDate(true, l.get(i).getDuration()) + "')";
				if(i != l.size() - 1){
					sql += " or ";
				}
			}
			List<FileManagement> lf = (List<FileManagement>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(lf);
			for(int i = 0; i < lf.size(); i++){
				arr.getJSONObject(i).put("fileTypeRemark", 
						this.dictionaryService.getDictionaryById(lf.get(i).getFileType()).getRemark());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	获取保证金预警信息
	 * @author 	肖乾斌
	 * @date 	2011-6-1
	 * @param 	operator
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getVsAlarmInformation(User operator, int pageIndex,
			int pageSize) throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			
			String sql = "select o from " + AlarmSetting.class.getName() + " o where o.userName = '" + 
						operator.getUserName() + "' and o.alarmType = '" + Constant.AM_TYPE_VS + "'";
			List<AlarmSetting> list = (List<AlarmSetting>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			//如果没有找到配置则返回
			if(list.size() == 0){
				return null;
			}
			
			//获取违章保证金预警配置
			AlarmSetting as = list.get(0);
			sql = "select o from " + AlarmParam.class.getName() + " o where o.alarmSettingId = '" + 
					as.getId() + "'";
			List<AlarmParam> l = (List<AlarmParam>) this.hibernateDao.getObjectsList(sql, 0, 1000, true);
			
			sql = "select o from " + ViolateSurety.class.getName() + " o where ";
			for(int i = 0; i < l.size(); i++){
				AlarmParam ap = l.get(i);
				if(ap.getParamValue().equals("0")){						//到期预警
					if(i != 0){
						sql += " or ";
					}
					sql += " (o.vsStatus = '0' and ISNULL(o.givebackDate,'') = '' and o.deadlineDate < '" + 
							this.getPreOrNextDate(true,ap.getDuration()) + "')";	
				}else if(ap.getParamValue().equals("1")){				//冻结预警
					if(i != 0){
						sql += " or ";
					}
					sql += " (o.vsStatus = '1' and o.deadlineDate > '" + 
							this.getPreOrNextDate(false, ap.getDuration()) + "')";
				}
			}
			List<ViolateSurety> lv = (List<ViolateSurety>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(lv);
			json.put(Constant.DATA, arr);
			for(int i = 0; i < lv.size(); i++){
				arr.getJSONObject(i).put("transactorName", this.userService.getUserByName(lv.get(i).getTransactor()).getRealName());
			}
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 获取到期预警订单
	 * @author 	肖乾斌
	 * @date 	2011-12-4
	 * @param pageIndex
	 * @param pageSize
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getOrderAlarmInfor(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			JSONObject json = new JSONObject();
			Date today = new Date();
			
			String _sql = "select o from " + LeaseRecord.class.getName() + " o where ('" + DateUtil.formatDate(today, "yyyy-M-d") + "' >= o.dateEnd and ISNULL(o.dateRelet,'') = '' and " +
					" o.leaseRecordStatus = '" + Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_BOOKING + "')" +
					" or ('" + DateUtil.formatDate(today, "yyyy-M-d") + "' >= o.dateRelet and ISNULL(o.dateRelet,'') != '' and o.leaseRecordStatus = '" + 
					Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + Constant.ORDER_STATUS_BOOKING + "')";
	
			@SuppressWarnings("unchecked")
			List<LeaseRecord> lf = (List<LeaseRecord>) this.hibernateDao.getObjectsList(_sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(lf);
			for(int i = 0; i < lf.size(); i++){
				//添加业务员真实的姓名
				arr.getJSONObject(i).put("transactorName",				 
						this.userService.getUserByName(lf.get(i).getTransactor()).getRealName());
				
				//添加业务结算员真实的姓名
				if(null != lf.get(i).getFinisher() && !"".equals(lf.get(i).getFinisher())){
					arr.getJSONObject(i).put("finisherName",				 
						this.userService.getUserByName(lf.get(i).getFinisher()).getRealName());
				}
				
				JSONObject jsons = this.customerService.getCustomerByIdCardNumber(lf.get(i).getCustomerIdCardNumber());
				
				//添加客户真实的姓名
				if(jsons.getJSONArray(Constant.DATA).size() != 0){
					Customer cus = (Customer) JSONObject.toBean(jsons.getJSONArray(Constant.DATA).getJSONObject(0), Customer.class);
					arr.getJSONObject(i).put("customerName",cus.getCustomerName());
				}
				
				//获取车辆租赁方式
				arr.getJSONObject(i).put("leaseWayRemark",				 
						this.dictionaryService.getDictionaryById(lf.get(i).getLeaseWay()).getRemark());
				
				//获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",				 
						this.dictionaryService.getDictionaryById(lf.get(i).getLeaseRecordStatus()).getRemark());
				
			}
			json.put(Constant.DATA, arr);
			
			json.put(Constant.TOTAL_SIZE,  this.hibernateDao.getCount(_sql, true));
			
			
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

}
