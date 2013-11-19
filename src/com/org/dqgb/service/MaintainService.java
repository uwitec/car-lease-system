/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-5-31</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-5-31	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import java.util.Date;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.MaintainRecord;
import com.org.dqgb.entity.User;
import com.org.dqgb.exception.ServiceException;

public interface MaintainService {

	/**
	 * 
	 * @TODO	增添养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	mr
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void addMaintainRecord(MaintainRecord mr)throws ServiceException;
	
	/**
	 * 
	 * @TODO	修改养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	mr
	 * @param 	operator
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void modifyMaintainRecord(MaintainRecord mr,User operator)throws ServiceException;
	
	/**
	 * 
	 * @TODO	根据carId查询养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getMaintainRecord(int pageIndex,int pageSize,String carId)throws ServiceException;
	
	/**
	 * 
	 * @TODO	查询所有的养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getMaintainRecord(int pageIndex,int pageSize)throws ServiceException;
	
	/**
	 * 
	 * @TODO	删除养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void deleteMaintainRecord(int id)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取养护记录
	 * @author 	肖乾斌
	 * @date 	2011-5-31
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	MaintainRecord
	 */
	public abstract MaintainRecord getMaintainRecord(int id)throws ServiceException;
	
	/**
	 * 	
	 * @TODO	统计每月的养护费用
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @param 	date
	 * @throws ServiceException
	 * @return 	int
	 */
	public abstract int getRepairFee(Date date) throws ServiceException;
}
