/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-8-30</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-8-30	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.User;
import com.org.dqgb.entity.ViolateRecord;
import com.org.dqgb.exception.ServiceException;

public interface ViolateRecordService {

	/**
	 * 添加违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param vr
	 * @param user
	 * @throws ServiceException
	 * @return 	void
	 */
	public abstract void addViolateRecord(ViolateRecord vr,User user)throws ServiceException;
	
	/**
	 * 修改违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param vr
	 * @throws ServiceException
	 * @return 	void
	 */
	public abstract void modifyViolateRecord(ViolateRecord vr)throws ServiceException;
	
	
	/**
	 * 获取违章记录
	 * @author 	肖乾斌
	 * @date 	2011-8-30
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getViolateRecord(int pageIndex, int pageSize)throws ServiceException;
	
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
	public abstract JSONObject getViolateRecord(int pageIndex, int pageSize,String customerId)throws ServiceException;
	
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
	public abstract JSONObject getViolateRecordByCar(int pageIndex, int pageSize,String carId)throws ServiceException; 
}
