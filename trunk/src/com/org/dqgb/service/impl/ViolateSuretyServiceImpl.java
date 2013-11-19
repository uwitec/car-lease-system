/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-29</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-29	    肖乾斌	    Created</li>
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.common.VsConstant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Customer;
import com.org.dqgb.entity.User;
import com.org.dqgb.entity.ViolateSurety;
import com.org.dqgb.entity.VsFinanceRecord;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.CustomerService;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.service.ViolateSuretyService;
import com.org.dqgb.service.VsFinanceService;

@Service("ViolateSuretyServiceImpl")
@SuppressWarnings("unchecked")
public class ViolateSuretyServiceImpl implements ViolateSuretyService{

	@Resource(name="hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="UserServiceImpl")
	public UserService userService;
	
	@Resource(name="OrganizationServiceImpl")
	public OrganizationService orgService;
	
	@Resource(name="CustomerServiceImpl")
	public CustomerService customerService;
	
	@Resource(name="VsFinanceServiceImpl")
	public VsFinanceService vfs;
	/**
	 * 
	 * @TODO	添加违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	vs
	 * @param 	operator
	 * @param 	duration
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addViolateSurety(ViolateSurety vs,User operator,long duration) throws ServiceException {
		if(this.getViolateSurety(vs.getCustomerId()) != null){
			throw new ServiceException("该客户对该车已经缴付过一次保证金，无需再缴。请<font style='color:fuchsia;'>延长担保期限</font>");
		}
		try{
			vs.setTransactor(operator.getUserName());
			Date date = new Date();
			vs.setPayDate(date);
			Date deadLine = new Date();
			deadLine.setTime(date.getTime() + 24 * 60 * 60 * 1000 * duration);
			vs.setBalance(vs.getMoney());
			vs.setDeadlineDate(deadLine);
			vs.setPayOrganizationId(operator.getDepartmentId());
			this.hibernateDao.save(vs);
			
			VsFinanceRecord vfr = new VsFinanceRecord();
			vfr.setDescription("预付金额");
			vfr.setFinanceType(VsConstant.RECEIVE);
			vfr.setMoney(vs.getMoney());
			vfr.setOrgId(operator.getDepartmentId());
			vfr.setTransactor(operator.getUserName());
			vfr.setVs(vs);
			vfr.setGivebackDate(date);
			this.hibernateDao.save(vfr);
			
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	根据客户身份证和车牌号获取违章保证金信息，只获取未还的信息
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	customerId
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	ViolateSurety
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public ViolateSurety getViolateSurety(String customerId, String carId)
			throws ServiceException {
		try{
			String sql = "select o from " + ViolateSurety.class.getName() + " o where o.carId = '" + carId + 
					"' and o.customerId = '" + customerId + "' and ISNULL(o.givebackDate,'') = '' and o.vsStatus = '0'"; 
			List<ViolateSurety> list =  (List<ViolateSurety>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @TODO	获取违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-6-22
	 * @param	customerId
	 * @throws 	ServiceException
	 * @return 	ViolateSurety
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public ViolateSurety getViolateSurety(String customerId)throws ServiceException {
		try{
			String sql = "select o from " + ViolateSurety.class.getName() + " o where o.customerId = '" + 
					customerId + "' and ISNULL(o.givebackDate,'') = '' and o.vsStatus = '0'"; 
			List<ViolateSurety> list =  (List<ViolateSurety>) this.hibernateDao.getObjectsList(sql, 0, 10, true);
			return list.size() == 0 ? null : list.get(0);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	延长担保期限
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	id
	 * @param 	duration			期限(天)
	 * @param 	operator			操作人员
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Override
	public void addSuretyDuration(int id, long duration,User operator)
			throws ServiceException {
		ViolateSurety vs = (ViolateSurety) this.hibernateDao.getObjectByID(ViolateSurety.class, id);
		
		try{
			Date date = new Date();
			vs.getDeadlineDate().setTime(date.getTime() + 24 * 60 * 60 * 1000 * duration);
			vs.setTransactor(operator.getUserName());
			this.hibernateDao.update(vs);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 
	 * @TODO	退还担保金,修改只能在当天执行
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	id
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void givebackViolateSurety(int id,int money, User operator)
			throws ServiceException {
		ViolateSurety vs = (ViolateSurety) this.hibernateDao.getObjectByID(ViolateSurety.class, id);
		if(vs.getVsStatus() == 1){
			throw new ServiceException("结单失败,订单已冻结!");
		}else if(null != vs.getGivebackDate()){
			throw new ServiceException("该单已结，毋须重复结单!");
		}else if(vs.getBalance() <= 0){
			throw new ServiceException("结单前请补缴相关费用!");
		}
		try{
			Date dst = new Date();
			vs.setGivebackDate(dst);
			vs.setBalance(vs.getBalance() - money);
			this.hibernateDao.update(vs);
			
			VsFinanceRecord vfr = new VsFinanceRecord();
			vfr.setFinanceType(VsConstant.GIVEBACK);
			vfr.setGivebackDate(dst);
			vfr.setDescription("结单");
			vfr.setMoney(money);
			vfr.setTransactor(operator.getUserName());
			vfr.setVs(vs);
			vfr.setOrgId(operator.getDepartmentId());
			this.hibernateDao.save(vfr);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		
	}

	/**
	 * 
	 * @TODO	获取担保金信息
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject getViolateSurety(int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select o from " + ViolateSurety.class.getName() + " o  where o.vsStatus = '0' order by o.deadlineDate asc";
			int count = this.hibernateDao.getCount(sql, true);
			List<ViolateSurety> list =  (List<ViolateSurety>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				
				//收取保证金的业务员
				arr.getJSONObject(i).put("transactorName", 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
				
//				//退还保证金的业务员
//				User finisher = this.userService.getUserByName(list.get(i).getFinisher());
//				arr.getJSONObject(i).put("finisherName", finisher == null ? "" : finisher.getRealName());
//				
				//收取保证金的驻场
				arr.getJSONObject(i).put("payOrgName", 
						this.orgService.getOrganizationById(list.get(i).getPayOrganizationId()).getDepartmentName());
				
				//退还保证金的驻场
//				Organization org = this.orgService.getOrganizationById(list.get(i).getGivebackOrganizationId());
//				arr.getJSONObject(i).put("givebackOrgName", org == null ? "" : org.getDepartmentName());
				
				sql = "select o from " + Customer.class.getName() + 
					" o where o.idCardNumber = '" + list.get(i).getCustomerId() + "'";
				List<Customer> lc = (List<Customer>) this.hibernateDao.getObjectsByHsql(sql);
				if(null != lc && lc.size() != 0)
					arr.getJSONObject(i).put("customerName", lc.get(0).getCustomerName());
				else{
					arr.getJSONObject(i).put("customerName","");
				}
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, count);
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @TODO	冻结保证金
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void freezoneViloateSurety(int id,User operator) throws ServiceException {
		ViolateSurety vs = (ViolateSurety) this.hibernateDao.getObjectByID(ViolateSurety.class, id);
		if(vs == null){
			throw new ServiceException("冻结失败,未能找到相关订单信息!");
		}else if(vs.getBalance() != vs.getMoney()){
			throw new ServiceException("冻结失败,该单已经发生财务信息。");
		}
		if(!vs.getTransactor().equals(operator.getUserName())){
			throw new ServiceException("您没有权限这样做!");
		}
		try{
			vs.setVsStatus(1);
			this.hibernateDao.update(vs);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 
	 * @TODO	根据客户id获取保证金信息，只查询还没有还保证金且没有被冻结的订单
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	customerId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public JSONObject findViolateSurety(int pageIndex, int pageSize,
			String customerId) throws ServiceException {
		try{
			String sql = "select o from " + ViolateSurety.class.getName() + " o where o.customerId = '" + customerId + 
								"' and o.vsStatus = '0'"; 
	
			List<ViolateSurety> list =  (List<ViolateSurety>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < list.size(); i++){
				
				//收取保证金的业务员
				arr.getJSONObject(i).put("transactorName", 
						this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
				
				//退还保证金的业务员
//				User finisher = this.userService.getUserByName(list.get(i).getFinisher());
//				arr.getJSONObject(i).put("finisherName", finisher == null ? "" : finisher.getRealName());
//				
				//收取保证金的驻场
				arr.getJSONObject(i).put("payOrgName", 
						this.orgService.getOrganizationById(list.get(i).getPayOrganizationId()).getDepartmentName());
				
				//退还保证金的驻场
//				Organization org = this.orgService.getOrganizationById(list.get(i).getGivebackOrganizationId());
//				arr.getJSONObject(i).put("givebackOrgName", org == null ? "" : org.getDepartmentName());
				
				sql = "select o from " + Customer.class.getName() + 
					" o where o.idCardNumber = '" + list.get(i).getCustomerId() + "'";
				List<Customer> lc = (List<Customer>) this.hibernateDao.getObjectsByHsql(sql);
				if(null != lc && lc.size() != 0)
					arr.getJSONObject(i).put("customerName", lc.get(0).getCustomerName());
				else{
					arr.getJSONObject(i).put("customerName","");
				}
				
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 续交违章保证金
	 * @author 		肖乾斌
	 * @date 		2011-9-2
	 * @param	 	id
	 * @param	 	money
	 * @param 		operator
	 * @throws 		ServiceException
	 * @return 		void
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addVsMoney(int id, int money, User operator) throws ServiceException {
		ViolateSurety vs = (ViolateSurety) this.hibernateDao.getObjectByID(ViolateSurety.class, id);
		if(vs.getVsStatus() == 1){
			throw new ServiceException("订单已冻结，无法续缴费用！");
		}else if(vs.getGivebackDate() != null){
			throw new ServiceException("订单已结单，无法续缴费用！");
		}else if(money == 0){
			throw new ServiceException("续缴费用不能为0");
		}
		try{
			vs.setMoney(money + vs.getMoney());
			vs.setBalance(vs.getBalance() + money);
			this.hibernateDao.update(vs);
			VsFinanceRecord vfr = new VsFinanceRecord();
			vfr.setDescription("续缴费用");
			vfr.setFinanceType(VsConstant.RECEIVE);
			vfr.setMoney(money);
			vfr.setOrgId(operator.getDepartmentId());
			vfr.setTransactor(operator.getUserName());
			vfr.setVs(vs);
			vfr.setGivebackDate(new Date());
			this.hibernateDao.save(vfr);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	/**
	 * 获取财务明细
	 * @author 	肖乾斌
	 * @date 	2011-9-2
	 * @param 	id
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	@Override
	public JSONObject getVsFinance(int id, int pageIndex, int pageSize)
			throws ServiceException {
		try{
			return this.vfs.getVsFinance(id, pageIndex, pageSize);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
}
