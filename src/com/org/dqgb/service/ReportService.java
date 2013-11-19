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
package com.org.dqgb.service;

import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;

import com.org.dqgb.exception.ServiceException;

public interface ReportService {
	
	/**
	 * 
	 * @TODO	获取销售日报数据信息,冻结的订单不会被统计
	 * @author 	肖乾斌
	 * @date 	2011-5-27
	 * @param 	date
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getDailyReport(Date date,int pageIndex,int pageSize)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取date指定的日期的财务收入统计
	 * @author 	肖乾斌
	 * @date 	2011-5-28
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	Map<String,Object>
	 */
	public abstract Map<String,Object> getDailyReportSum(Date date)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取销售日报统计信息
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getDailyReportSumDetails(Date date)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取月报统计
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @param 	date
	 * @param 	pageIndex
	 * @param 	pageSize
	 * @throws  ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getMensalReport(Date date,int pageIndex,int pageSize)throws ServiceException;
	
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
	public abstract JSONObject getMensalReport(Date date, int pageIndex, int pageSize,int brand) throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取月报统计信息
	 * @author 	肖乾斌
	 * @date 	2011-6-2
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getMensalReportSum(Date date)throws ServiceException;
	
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
	public abstract JSONObject getMensalReportSumDetails(Date date, int pageIndex, int pageSize)throws ServiceException;
	
	/**
	 * 
	 * @TODO	获取每日担保金的收入统计明细
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @param 	date
	 * @throws 	ServiceException
	 * @return JSONObject
	 */
	public abstract JSONObject getDailyViolateSuretySum(Date date)throws ServiceException;
	
	/**
	 * 事故统计报表
	 * @author 	肖乾斌
	 * @date 	2011-9-13
	 * @param 	date
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getAccidentReport(Date date)throws ServiceException;
	
	
	/**
	 * 获取客户消费总计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @param customerId
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getConsumeSummary(String customerId)throws ServiceException;
	
	/**
	 * 刷新欠费订单
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws  ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject updateArrearage()throws ServiceException;
	
	/**
	 * 获取欠费订单
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws  ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getArrearage(int pageIndex, int pageSize)throws ServiceException;
	
	/**
	 * 欠费统计
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @throws ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getArrearageSummary()throws ServiceException;
	
	/**
	 * 获取员工产值
	 * @author 	肖乾斌
	 * @date 	2011-10-1
	 * @param 	year
	 * @throws 	ServiceException
	 * @return 	JSONObject
	 */
	public abstract JSONObject getOutputSummary(int year,int pageIndex, int pageSize)throws ServiceException;
	
}
