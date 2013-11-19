/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-1</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-1	    肖乾斌	    Created</li>
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
import com.org.dqgb.common.VsConstant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.NonBusinessRunRecord;
import com.org.dqgb.entity.RunRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.entity.ViolateRecord;
import com.org.dqgb.entity.ViolateSurety;
import com.org.dqgb.entity.VsFinanceRecord;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.LeaseRecordService;
import com.org.dqgb.service.NBusiRunrecordService;
import com.org.dqgb.service.RunRecordService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.service.ViolateRecordService;
import com.org.dqgb.service.ViolateSuretyService;

@SuppressWarnings("unchecked")
@Service("ViolateRecordServiceImpl")
public class ViolateRecordServiceImpl implements ViolateRecordService {

	@Resource @Qualifier("hibernateDao")
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="ViolateSuretyServiceImpl")
	public ViolateSuretyService vss;

	@Resource(name="LeaseRecordServiceImpl")
	public LeaseRecordService lrs;

	@Resource(name="CustomerServiceImpl")
	public CustomerService cs;
	
	@Resource(name="RunRecordServiceImpl")
	public RunRecordService rrs;
	
	@Resource(name="NBusiRunrecordServiceImpl")
	public NBusiRunrecordService nbrrService;
	
	@Resource @Qualifier("UserServiceImpl")
	public UserService userService;
	/**
	 * 添加违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param vr
	 * @param user
	 * @throws ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addViolateRecord(ViolateRecord vr,User user) throws ServiceException {
		ViolateSurety vs = null;
		RunRecord rr = this.rrs.getRunRecord(vr.getCarId(), vr.getViolateDate());
		if(rr == null){
			NonBusinessRunRecord nbrr = this.nbrrService.getAccidentRunRecord(vr.getCarId(), vr.getViolateDate());
			if(nbrr != null){
				vr.setInnerUsage(1);
				vr.setTransactor(user.getUserName());
				this.hibernateDao.save(vr);
				return;
			}
			throw new ServiceException("没有找到车辆出行记录信息，请您核实您的违章日期和车牌号信息。");
		}
		LeaseRecord lr = this.lrs.getLeaseRecordBySerialNumber(rr.getOrderFormSerialNumber());
		try{
			vs = this.vss.getViolateSurety(lr.getCustomerIdCardNumber());
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		if(null == vs){
			throw new ServiceException("该客户(" + this.cs.getCustomer(lr.getCustomerIdCardNumber()).getCustomerName() + "--" + lr.getCustomerIdCardNumber()+ ")尚未缴纳违章保证金,暂时无法添加违章记录!");
		}
		try{
			Date dt = new Date();
			vs.setBalance(vs.getBalance() - vr.getViolateMoney() - vr.getPoundage());
			this.hibernateDao.update(vs);
			vr.setTransactor(user.getUserName());
			vr.setInnerUsage(0);
			vr.setSerialNumber(rr.getOrderFormSerialNumber());
			vr.setVsId(vs.getId());
			this.hibernateDao.save(vr);
			VsFinanceRecord vfr = new VsFinanceRecord();
			vfr.setDescription("罚款");
			vfr.setFinanceType(VsConstant.MINUS);
			vfr.setMoney(vr.getViolateMoney());
			vfr.setOrgId(user.getDepartmentId());
			vfr.setTransactor(user.getUserName());
			vfr.setVs(vs);
			vfr.setGivebackDate(dt);
			this.hibernateDao.save(vfr);
			VsFinanceRecord vfr1 = new VsFinanceRecord();
			vfr1.setDescription("手续费");
			vfr1.setFinanceType(VsConstant.POUNDAGE);
			vfr1.setMoney(vr.getPoundage());
			vfr1.setOrgId(user.getDepartmentId());
			vfr1.setTransactor(user.getUserName());
			vfr1.setVs(vs);
			vfr1.setGivebackDate(dt);
			this.hibernateDao.save(vfr1);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 修改违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param vr
	 * @throws ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void modifyViolateRecord(ViolateRecord vr) throws ServiceException {
		try{
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getViolateRecord(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			List<ViolateRecord> list = (List<ViolateRecord>) this.hibernateDao.getObjectsList("select o from " + ViolateRecord.class.getName() + " o order by o.violateDate desc", pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			
			for(int i = 0; i < arr.size(); i++){
				if(1 == (list.get(i).getInnerUsage())){
					NonBusinessRunRecord nbrr = this.nbrrService.getAccidentRunRecord(list.get(i).getCarId(), list.get(i).getViolateDate());
					arr.getJSONObject(i).put("customerName", nbrr.getUsername());
				}else{
					LeaseRecord lr = this.lrs.getLeaseRecordBySerialNumber(list.get(i).getSerialNumber());
					arr.getJSONObject(i).put("customerName", lr == null ? "" : this.cs.getCustomer(lr.getCustomerIdCardNumber()).getCustomerName());
				}
				User user = this.userService.getUserByName(list.get(i).getTransactor());
				if(null != user){
					arr.getJSONObject(i).put("transactorName", user.getRealName());
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount("select o from " + ViolateRecord.class.getName() + " o", true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取用户违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	customerId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getViolateRecord(int pageIndex, int pageSize,
			String customerId) throws ServiceException {
		try{
			String sql = "select v from " + ViolateRecord.class.getName() + " v," + LeaseRecord.class.getName() + " l where " + 
						" v.serialNumber = l.serialNumber and l.customerIdCardNumber = '" + customerId + "'  order by v.violateDate desc";
			List<ViolateRecord> list = (List<ViolateRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < arr.size(); i++){
				if(list.get(i).getInnerUsage().equals(1)){
					NonBusinessRunRecord nbrr = this.nbrrService.getAccidentRunRecord(list.get(i).getCarId(), list.get(i).getViolateDate());
					arr.getJSONObject(i).put("customerName", nbrr.getUsername());
				}else{
					arr.getJSONObject(i).put("customerName", this.cs.getCustomer(customerId).getCustomerName());
				}
				
				User user = this.userService.getUserByName(list.get(i).getTransactor());
				if(null != user){
					arr.getJSONObject(i).put("transactorName", user.getRealName());
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
	 * 根据车牌获取违章记录
	 * @author 	肖乾斌
	 * @date 	2011-12-15
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getViolateRecordByCar(int pageIndex, int pageSize,
			String carId) throws ServiceException {
		try {
			String sql = "select v from "
					+ ViolateRecord.class.getName()
					+ " v,"
					+ LeaseRecord.class.getName()
					+ " l where "
					+ " v.serialNumber = l.serialNumber and l.carId = '"
					+ carId + "'  order by v.violateDate desc";
			List<ViolateRecord> list = (List<ViolateRecord>) this.hibernateDao
					.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for (int i = 0; i < arr.size(); i++) {
				if (list.get(i).getInnerUsage().equals(1)) {
					NonBusinessRunRecord nbrr = this.nbrrService.getAccidentRunRecord(list.get(i).getCarId(), list.get(i).getViolateDate());
					arr.getJSONObject(i).put("customerName", nbrr.getUsername());
				} else {
					LeaseRecord lr = this.lrs.getLeaseRecordBySerialNumber(list.get(i).getSerialNumber());
					arr.getJSONObject(i).put("customerName", lr == null ? "" : this.cs.getCustomer(lr.getCustomerIdCardNumber()).getCustomerName());
				}

				User user = this.userService.getUserByName(list.get(i)
						.getTransactor());
				if (null != user) {
					arr.getJSONObject(i).put("transactorName",
							user.getRealName());
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

	
}
