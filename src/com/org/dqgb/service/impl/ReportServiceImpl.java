/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-27</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-27	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service.impl;

import java.sql.ResultSet;
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
import com.org.dqgb.common.FinanceConstant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Finance;
import com.org.dqgb.entity.LeaseRecord;
import com.org.dqgb.entity.RunRecord;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.DictionaryService;
import com.org.dqgb.service.FinanceService;
import com.org.dqgb.service.LeaseRecordService;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.ReportService;
import com.org.dqgb.service.RunRecordService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.util.DateUtil;

@Service("ReportServiceImpl")
@SuppressWarnings({"deprecation","unchecked"})
public class ReportServiceImpl implements ReportService {

	@Resource(name = "hibernateDao")
	public HibernateDao hibernateDao;

	@Resource(name = "DictionaryServiceImpl")
	public DictionaryService dictionaryService;

	@Resource(name = "RunRecordServiceImpl")
	public RunRecordService runRecordService;

	@Resource(name = "FinanceServiceImpl")
	public FinanceService financeService;

	@Resource(name = "OrganizationServiceImpl")
	public OrganizationService orgService;

	@Resource(name = "UserServiceImpl")
	public UserService userService;
	
	@Resource @Qualifier("CustomerServiceImpl")
	public  CustomerService customerService;
	
	@Resource(name="LeaseRecordServiceImpl")
	public LeaseRecordService lrs;

	public Logger log = Logger.getLogger(this.getClass());
	/**
	 * 
	 * @TODO 获取销售日报数据信息,冻结的订单不会被统计
	 * @author 肖乾斌
	 * @date 2011-5-27
	 * @param date
	 * @param pageIndex
	 * @param pageSize
	 * @throws ServiceException
	 * @return JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getDailyReport(Date date, int pageIndex, int pageSize)
			throws ServiceException {
		try {
			String today = DateUtil.formatDate(date, "yyyy-M-d 00:00:00");
			Date td = new Date();
			td.setTime(date.getTime());
			date.setTime(date.getTime() + 24 * 60 * 60 * 1000);
			String tomorrow = DateUtil.formatDate(date, "yyyy-M-d 00:00:00");

			// 只看今天发生财务信息的订单
			String sql = "select distinct o from "
					+ LeaseRecord.class.getName() + " o," + Finance.class.getName()
					+ " f where o.leaseRecordStatus !='" + Constant.ORDER_STATUS_FREEZED + "' and o.leaseRecordStatus !='" 
					+ Constant.ORDER_STATUS_PRE_BOOK 
					+ "' and o.serialNumber = f.serialNumber and f.payDate >= '"
					+ today + "' and f.payDate < '" + tomorrow + "'";

			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao
					.getObjectsList(sql, pageIndex, pageSize, true);
			JSONArray arr = JSONArray.fromObject(list);
			for (int i = 0; i < arr.size(); i++) {
				// 获取车辆订单状态
				arr.getJSONObject(i).put("leaseStatusRemark",this.dictionaryService.getDictionaryById(list.get(i).getLeaseRecordStatus()).getRemark());

				// 取车、还车日期
				RunRecord rr = this.runRecordService
						.getRunRecordByOrderFormID(list.get(i)
								.getSerialNumber());
				if (null != rr) {
					Date giveBackDate = rr.getDateEnd();
					arr.getJSONObject(i).put("giveBackCarDate",giveBackDate == null ? "" : giveBackDate);
					Date fetchCarDate = rr.getDateBegin();
					arr.getJSONObject(i).put("fetchCarDate",fetchCarDate == null ? "" : fetchCarDate);
				}

				// 租赁模式
				arr.getJSONObject(i).put("leaseModelRemark",this.dictionaryService.getDictionaryById(list.get(i).getLeaseModel()).getRemark());

				// 下单驻场信息
				arr.getJSONObject(i).put("organizationName",this.orgService.getOrganizationById(this.userService.getUserByName(list.get(i).getTransactor()).getDepartmentId()).getDepartmentName());

				// 结单驻场信息
				if(list.get(i).getLeaseRecordStatus() == Constant.ORDER_STATUS_OVER_NORMALLY){
					arr.getJSONObject(i).put("organizationNameSettle",this.orgService.getOrganizationById(this.userService.getUserByName(
												list.get(i).getFinisher()).getDepartmentId()).getDepartmentName());
				}else{
					arr.getJSONObject(i).put("organizationNameSettle","");
				}
				
				List<Finance> lf = (List<Finance>) this.hibernateDao.getObjectsList("select o from " + Finance.class.getName()
										+ " o where o.serialNumber = '" + list.get(i).getSerialNumber() + "'",0, 1000, true);

				int rec = 0; // 收
				int back = 0; // 退
				for (Finance f : lf) {
					if (f.getFinanceType() == Constant.GROUP_F_BOOKINGFEE) {
						arr.getJSONObject(i).put("bookingFee", f.getMoney());
					}
					if (f.getFinanceType() == Constant.GROUP_F_PRICE) {
						arr.getJSONObject(i).put("rentFee", f.getMoney());
					}
					if (f.getFinanceType() == Constant.GROUP_F_FOREGIFT) {
						arr.getJSONObject(i).put("foregiftFee", f.getMoney());
					}
					if (f.getPayDirection() == Constant.FINANCE_IN) {
						if (f.getPayDate().getYear() == td.getYear()&& f.getPayDate().getMonth() == td.getMonth()&& f.getPayDate().getDate() == td.getDate()) {
							rec += f.getMoney();
						}
					} else if (f.getPayDirection() == Constant.FINANCE_OUT) {
						if (f.getPayDate().getYear() == td.getYear()&& f.getPayDate().getMonth() == td.getMonth()&& f.getPayDate().getDate() == td.getDate()) {
							back += f.getMoney();
						}
					}
				}
				arr.getJSONObject(i).put("income", rec);
				arr.getJSONObject(i).put("outcome", back);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO 获取销售日报的统计信息
	 * @author 肖乾斌
	 * @date 2011-5-28
	 * @param date
	 * @throws ServiceException
	 * @return Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Map<String, Object> getDailyReportSum(Date date)
			throws ServiceException {
		try {
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			ResultSet rs = (ResultSet) this.hibernateDao.getListFromProcedure(
					"{CALL P_DAILYREPORT(?)}", var, false).get(Constant.DATA);
			Map<String, Object> out = new HashMap<String, Object>();
			while (rs.next()) {
				out.put("IN", rs.getInt("_IN"));
				out.put("OUT", rs.getInt("_OUT"));
			}
			return out;
		} catch (Exception e) {
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO 获取销售日报统计信息
	 * @author 肖乾斌
	 * @date 2011-5-29
	 * @param date
	 * @throws ServiceException
	 * @return JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getDailyReportSumDetails(Date date)
			throws ServiceException {
		try {
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			ResultSet rs = (ResultSet) this.hibernateDao.getListFromProcedure(
					"{CALL P_DAILYREPORT_SUM_DETAILS(?)}", var, false).get(
					Constant.DATA);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("departmentName", rs.getString("DEPARTMENT_NAME"));
				out.put("out", rs.getString("_OUT"));
				out.put("ins", rs.getString("_IN"));
				out.put("income", rs.getString("INCOME"));
				out.put("discount", rs.getString("DISCOUNT"));
				out.put("rentIn", rs.getString("RENT_IN"));
				out.put("rentOut", rs.getString("RENT_OUT"));
				out.put("foregiftIn", rs.getString("FOREGIFT_IN"));
				out.put("foregiftOut", rs.getString("FOREGIFT_OUT"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO 获取月报统计
	 * @author 肖乾斌
	 * @date 2011-6-2
	 * @param date
	 * @param pageIndex
	 * @param pageSize
	 * @throws ServiceException
	 * @return JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getMensalReport(Date date, int pageIndex, int pageSize)
			throws ServiceException {
		try {
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			var.put(1, pageIndex);
			var.put(2, pageSize);
			var.put(3, 0);
			Map<String, Object> map = this.hibernateDao.getListFromProcedure(
					"{CALL P_MENSALREPORT(?,?,?,?)}", var, false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			map = this.hibernateDao.getListFromProcedure(
					"{CALL P_MENSALREPORT(?,?,?,?)}", var, true);
			int totalSize = (Integer) map.get(Constant.TOTAL_SIZE);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("out", rs.getString("OUTCOME"));
				out.put("in", rs.getString("INCOME"));
				out.put("total", rs.getString("TOTAL"));
				out.put("brand", rs.getString("BRAND_ID"));
				out.put("brandName", rs.getString("REMARK"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, totalSize);
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	获取指定品牌的车辆的销售月报
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @param 	date
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	brand
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getMensalReport(Date date, int pageIndex, int pageSize,
			int brand) throws ServiceException {
		try{
			Map<Integer,Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date,"yyyy-M-d"));
			var.put(1, pageIndex);
			var.put(2, pageSize);
			var.put(3, brand);
			var.put(4, 0);
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_MENSALREPORT_BRAND(?,?,?,?,?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			map = this.hibernateDao.getListFromProcedure("{CALL P_MENSALREPORT_BRAND(?,?,?,?,?)}", var,true);
			int totalSize = (Integer) map.get(Constant.TOTAL_SIZE);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("out", rs.getString("OUTCOME"));
				out.put("in", rs.getString("INCOME"));
				out.put("total", rs.getString("TOTAL"));
				out.put("carSource", rs.getString("REMARK"));
				out.put("totalRent", rs.getString("RENTINCOME"));
				out.put("carId", rs.getString("CAR_ID"));
				out.put("category", rs.getString("BRANDCATEGORY"));
				double duration = rs.getInt("DURATION");
				out.put("days", Math.ceil(duration/60/60/24));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, totalSize);
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	获取月报统计信息
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getMensalReportSum(Date date) throws ServiceException {
		try{
			Map<Integer,Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date,"yyyy-M-d"));
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_MENSALREPORT_SUM(?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("out", rs.getString("OUTCOME"));
				out.put("in", rs.getString("INCOME"));
				out.put("total", rs.getString("TOTAL"));
				out.put("remark", rs.getString("REMARK"));
				out.put("carSource", rs.getString("CAR_SOURCE"));
				out.put("ch", rs.getString("CH"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	获取月报统计的各个驻场明细
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @param 	date
	 * @param	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getMensalReportSumDetails(Date date, int pageIndex,
			int pageSize) throws ServiceException {
		try{
			Map<Integer,Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date,"yyyy-M-d"));
			var.put(1, pageIndex);
			var.put(2, pageSize);
			var.put(3, 0);
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_MENSALREPORT_SUM_DETAILS(?,?,?,?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			map = this.hibernateDao.getListFromProcedure("{CALL P_MENSALREPORT_SUM_DETAILS(?,?,?,?)}", var,true);
			int totalSize = (Integer) map.get(Constant.TOTAL_SIZE);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String, Object> out = new HashMap<String, Object>();
				out.put("out", rs.getString("OUTCOME"));
				out.put("ins", rs.getString("INCOME"));
				out.put("income", rs.getString("TOTAL"));
				out.put("departmentName", rs.getString("DEPARTMENT_NAME"));
				out.put("rentIn", rs.getString("RENT_IN"));
				out.put("rentOut", rs.getString("RENT_OUT"));
				out.put("foregiftIn", rs.getString("FOREGIFT_IN"));
				out.put("foregiftOut", rs.getString("FOREGIFT_OUT"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, totalSize);
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	获取每日担保金的收入统计明细
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @param 	date
	 * @throws 	ServiceException
	 * @return  JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getDailyViolateSuretySum(Date date)
			throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_DAILY_VIOLATE_SURETY(?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> out = new HashMap<String, Object>();
				out.put("departmentName", rs.getString("DEPARTMENT_NAME"));
				out.put("income", rs.getString("INCOME"));
				out.put("outcome", rs.getString("OUTCOME"));
				out.put("total", rs.getString("TOTAL"));
				out.put("forfeit", rs.getString("FORFEIT"));
				out.put("poundage", rs.getString("POUNDAGE"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 事故统计报表
	 * @author 	肖乾斌
	 * @date 	2011-9-13
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getAccidentReport(Date date) throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, DateUtil.formatDate(date, "yyyy-M-d"));
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_ACCIDENT_SUM(?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> out = new HashMap<String, Object>();
				out.put("month", rs.getString("_MONTH"));
				out.put("yearmonth", rs.getString("YEARMONTH"));
				out.put("delayFee", rs.getString("DELAY_FEE"));
				out.put("frayFee", rs.getString("CAR_FRAY_FEE"));
				out.put("repairFee", rs.getString("REPAIR_FEE"));
				out.put("insuranceFee", rs.getString("INSURANCE_FEE"));
				out.put("otherFee", rs.getString("OTHER_FEE"));
				out.put("counts", rs.getString("COUNTS"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 获取客户消费总计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param customerId
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getConsumeSummary(String customerId)
			throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, customerId);
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_CUSTOMER_FINANCE_SUM(?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> out = new HashMap<String, Object>();
				out.put("finance", rs.getString("FINANCE"));
				out.put("rent", rs.getString("RENT"));
				out.put("discount", rs.getString("DISCOUNT"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 刷新欠费订单
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws  ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject updateArrearage()throws ServiceException {
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.leaseRecordStatus = '" + 
				Constant.ORDER_STATUS_PROCESSING + "' or o.leaseRecordStatus = '" + 
				Constant.ORDER_STATUS_GIVEBACK_CAR + "' or o.leaseRecordStatus ='"  + 
				Constant.ORDER_STATUS_SUPPLIMENT + "'";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsByHsql(sql);
			for(int i = 0; i < list.size(); i++){
				Map<String,Integer> fMap = this.lrs.rentCalculate(list.get(i).getSerialNumber());
				int rent = fMap.get(FinanceConstant.ACTUAL_RENT_FEE);	//实际发生的租金总额
				int dis = rent - fMap.get(FinanceConstant.RENT_FEE);	//获取差价this.getRentReceived(serialNumber);
				list.get(i).setReletFeeWaiting(0);
				if(dis > 0){											//租金已超出实际支付
					int actualReletFee = rent - fMap.get(FinanceConstant.RENT_FEE);//实际发生的续租费用
					actualReletFee = actualReletFee > fMap.get(FinanceConstant.RELET_FEE) ? fMap.get(FinanceConstant.RELET_FEE) : actualReletFee;
					//续租费的差额
					dis = fMap.get(FinanceConstant.RECIEVED_RELET_FEE) - actualReletFee;
					if(dis < 0){
						list.get(i).setReletFeeWaiting(dis);
					}
				}
				this.hibernateDao.update(list.get(i));
			}
			return null;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 获取欠费订单
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws  ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getArrearage(int pageIndex, int pageSize)
	throws ServiceException {
		try{
			String sql = "select o from " + LeaseRecord.class.getName() + " o where o.reletFeeWaiting != '0' order by o.orderDate desc";
			List<LeaseRecord> list = (List<LeaseRecord>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				arr.getJSONObject(i).put("customerName", this.customerService.getCustomer(list.get(i).getCustomerIdCardNumber()).getCustomerName());
				arr.getJSONObject(i).put("orgName", this.orgService.getOrganizationById(list.get(i).getOrderId()).getDepartmentName());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			sql = "select sum(o.reletFeeWaiting) as total from " + LeaseRecord.class.getName() + " o";
			List<Object> l = (List<Object>) this.hibernateDao.getObjectsByHsql(sql);
			json.put("total", l.get(0));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 欠费统计
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getArrearageSummary() throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_ARREARAGE_SUM()}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> out = new HashMap<String, Object>();
				out.put("finance", rs.getString("FINANCE"));
				out.put("orgName", rs.getString("DEPARTMENT_NAME"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, list.size());
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 获取员工产值
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	year
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public JSONObject getOutputSummary(int year,int pageIndex, int pageSize) throws ServiceException {
		try{
			Map<Integer, Object> var = new HashMap<Integer, Object>();
			var.put(0, year);
			var.put(1, pageIndex);
			var.put(2, pageSize);
			var.put(3, 0);
			Map<String,Object> map = this.hibernateDao.getListFromProcedure("{CALL P_EMPOUT_SUM(?,?,?,?)}", var,false);
			ResultSet rs = (ResultSet) map.get(Constant.DATA);
			map = this.hibernateDao.getListFromProcedure("{CALL P_EMPOUT_SUM(?,?,?,?)}", var,true);
			int totalSize = (Integer) map.get(Constant.TOTAL_SIZE);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> out = new HashMap<String, Object>();
				out.put("realName", rs.getString("REALNAME"));
				out.put("username", rs.getString("USERNAME"));
				out.put("_1", rs.getString("M1"));
				out.put("_2", rs.getString("M2"));
				out.put("_3", rs.getString("M3"));
				out.put("_4", rs.getString("M4"));
				out.put("_5", rs.getString("M5"));
				out.put("_6", rs.getString("M6"));
				out.put("_7", rs.getString("M7"));
				out.put("_8", rs.getString("M8"));
				out.put("_9", rs.getString("M9"));
				out.put("_10", rs.getString("M10"));
				out.put("_11", rs.getString("M11"));
				out.put("_12", rs.getString("M12"));
				out.put("year", rs.getString("_YEAR"));
				list.add(out);
			}
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, totalSize);
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
}
