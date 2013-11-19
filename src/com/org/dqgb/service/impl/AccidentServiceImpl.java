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
import com.org.dqgb.entity.Accident;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.AccidentService;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.LeaseRecordService;

@Service("AccidentServiceImpl")
@SuppressWarnings("unchecked")
public class AccidentServiceImpl implements AccidentService {

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="LeaseRecordServiceImpl")
	public LeaseRecordService lrs;
	
	@Resource(name="CustomerServiceImpl")
	public CustomerService cs;
	/**
	 * 添加事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @param 	acc
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void add(Accident acc, User user) throws ServiceException {
		try {
			acc.setTransactor(user.getUserName());
			this.hibernateDao.save(acc);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 修改事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-8
	 * @param 	acc
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void modify(Accident acc, User user) throws ServiceException {
		Accident ac = this.getAccident(acc.getId());
		if(ac.getStatus() == 1){
			throw new ServiceException("单据已封存，无法被修改！");
		}
		try {
			ac.setAccidentDate(acc.getAccidentDate());
			ac.setCarId(acc.getCarId());
			ac.setDalayFee(acc.getDalayFee());
			ac.setFrayFee(acc.getFrayFee());
			ac.setInsuranceFee(acc.getInsuranceFee());
			ac.setOtherFee(acc.getOtherFee());
			ac.setRemark(acc.getRemark());
			ac.setRepairFee(acc.getRepairFee());
			ac.setSerialNumber(acc.getSerialNumber());
			ac.setTransactor(user.getUserName());
			this.hibernateDao.update(ac);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 根据id获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	Accident
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Accident getAccident(int id) throws ServiceException {
		try {
			return (Accident) this.hibernateDao.getObjectByID(Accident.class, id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	/**
	 * 获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getAccident(int pageIndex, int pageSize)
			throws ServiceException {
		try {
			String sql = "select o from " + Accident.class.getName() + " o order by o.status asc, o.accidentDate desc";
			List<Accident> list = (List<Accident>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				if("".equals(list.get(i).getSerialNumber()) || null == list.get(i).getSerialNumber()){
					arr.getJSONObject(i).put("customerName", "");
				}else{
					arr.getJSONObject(i).put("customerName", 
							this.cs.getCustomer(this.lrs.getLeaseRecordBySerialNumber(list.get(i).getSerialNumber()).getCustomerIdCardNumber()).getCustomerName());
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
	 * 根据订单号获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getAccident(String orderId) throws ServiceException {
		try {
			String sql = "select o from " + Accident.class.getName() + " o where o.serialNumber = '" + orderId + "' order by o.accidentDate desc";
			List<Accident> list = (List<Accident>) this.hibernateDao.getObjectsByHsql(sql);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("customerName", 
					this.cs.getCustomer(this.lrs.getLeaseRecordBySerialNumber(list.get(i).getSerialNumber()).getCustomerIdCardNumber()).getCustomerName());
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
	 * 根据订单号获取事故记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	Accident
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Accident getAccidentByOrderId(String orderId)
			throws ServiceException {
		try{
			String sql = "select o from " + Accident.class.getName() + " o where o.serialNumber = '" + orderId + "' order by o.accidentDate desc";
			List<Accident> list = (List<Accident>) this.hibernateDao.getObjectsByHsql(sql);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 锁定记录
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	id
	 * @param 	user
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void lockRecord(int id,User user) throws ServiceException {
		Accident ac = this.getAccident(id);
		if(!ac.getTransactor().equals(user.getUserName())){
			throw new ServiceException("只有业务员本人才能执行封存操作");
		}
		try{
			ac.setStatus(1);
			ac.setFinisher(user.getUserName());
			this.hibernateDao.update(ac);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取事故处理费用总计
	 * @author 	肖乾斌
	 * @date 	2011-9-9
	 * @param 	orderId
	 * @throws 	ServiceException
	 * @return 	int
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getAccidentFinance(String orderId) throws ServiceException {
		try{
			Accident acc = this.getAccidentByOrderId(orderId);
			return acc.getDalayFee() + acc.getFrayFee() + acc.getRepairFee() + acc.getInsuranceFee() + acc.getOtherFee();
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

}
