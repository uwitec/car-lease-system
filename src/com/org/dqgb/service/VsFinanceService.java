/**
 * 
 * 中国石油大庆金桥信息技术工程有限公司成都分公司
 * <ul>
 * <li>Author: 		 肖乾斌</li>
 * <li>E-Mail:		jiaoyl-ds@petrochina.com.cn</li>
 * <li>T-Phone:		13880430860</li>
 * <li>Date: 		2011-9-2</li>
 * <li>Description:	 </li>
 * <li>+-History-------------------------------------+</li>
 * <li>| Date		Author		Description	</li>
 * <li>|2011-9-2	    肖乾斌	    Created</li>
 * <li>+------------------------------------------------</li>
 * </ul>
 */
package com.org.dqgb.service;

import net.sf.json.JSONObject;

import com.org.dqgb.exception.ServiceException;

public interface VsFinanceService {

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
	public abstract JSONObject getVsFinance(int id,int pageIndex, int pageSize)throws ServiceException;
}
