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
package com.org.dqgb.service;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.User;
import com.org.dqgb.entity.ViolateSurety;
import com.org.dqgb.exception.ServiceException;

/**
 * @TODO 违章保证金服务
 */
public interface ViolateSuretyService {
	
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
	public abstract void addViolateSurety(ViolateSurety vs,User operator,long duration)throws ServiceException;
	
	
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
	public abstract ViolateSurety getViolateSurety(String customerId,String carId)throws ServiceException;
	
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
	public abstract void addSuretyDuration(int id,long duration,User operator)throws ServiceException;
	
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
	public abstract void givebackViolateSurety(int id,int money,User operator)throws ServiceException;
	
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
	public abstract JSONObject getViolateSurety(int pageIndex,int pageSize)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取违章保证金
	 * @author 	肖乾斌
	 * @date 	2011-6-22
	 * @param	customerId
	 * @throws 	ServiceException
	 * @return 	ViolateSurety
	 */
	public abstract ViolateSurety getViolateSurety(String customerId)throws ServiceException;
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
	public abstract void freezoneViloateSurety(int id,User operator)throws ServiceException;
	
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
	public abstract JSONObject findViolateSurety(int pageIndex,int pageSize,String customerId)throws ServiceException;
	
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
	public abstract void addVsMoney(int id,int money,User operator)throws ServiceException;
	
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
	public abstract JSONObject getVsFinance(int id, int pageIndex, int pageSize)throws ServiceException;
}
