/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-10-1</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-10-1	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.org.dqgb.entity.CarAccessoryAlarm;
import com.org.dqgb.exception.ServiceException;

public interface CarAccessoryAlarmService {

	/**
	 * 添加一条预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	caa
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void add(CarAccessoryAlarm caa)throws ServiceException;
	
	/**
	 * 添加一辆车的预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	lcaa
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void add(List<CarAccessoryAlarm> lcaa)throws ServiceException;
	
	/**
	 * 获取车辆配置
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	carId
	 * @throws 	ServiceException
	 * @return 	List<CarAccessoryAlarm>
	 */
	public abstract List<CarAccessoryAlarm> getConfig(int carId)throws ServiceException;
	
	/**
	 * 获取预警信息的条数
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @throws 	ServiceException
	 * @return 	int
	 */
	public abstract int getAccessoryAICount()throws ServiceException;
	
	/**
	 * 获取预警信息
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @throws 	ServiceException
	 * @return 	int
	 */
	public abstract JSONObject getCarAlarmInformation(int pageIndex, int pageSize)throws ServiceException;
	
	/**
	 * 处理预警
	 * @author 	肖乾斌
	 * @date 	2011-10-2
	 * @param 	id
	 * @throws 	ServiceException
	 * @return 	void
	 */
	public abstract void handleAlarm(int id)throws ServiceException;
}
