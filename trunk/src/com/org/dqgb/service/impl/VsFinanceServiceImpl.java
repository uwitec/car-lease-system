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
import com.org.dqgb.entity.VsFinanceRecord;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.OrganizationService;
import com.org.dqgb.service.UserService;
import com.org.dqgb.service.VsFinanceService;

@Service("VsFinanceServiceImpl")
public class VsFinanceServiceImpl implements VsFinanceService {

	@Resource(name="hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	

	@Resource(name="UserServiceImpl")
	public UserService userService;
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
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getVsFinance(int id, int pageIndex, int pageSize)
			throws ServiceException {
		try {
			List<VsFinanceRecord> list = (List<VsFinanceRecord>) this.hibernateDao.getObjectsList("select o from " + 
					VsFinanceRecord.class.getName() + " o where o.vs.id = '" + id + "'", pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			JSONArray arr = JSONArray.fromObject(list);
			for(int i = 0; i < arr.size(); i++){
				arr.getJSONObject(i).put("deptName", this.organizationService.getOrganizationById(list.get(i).getOrgId()).getDepartmentName());
				arr.getJSONObject(i).put("realName", this.userService.getUserByName(list.get(i).getTransactor()).getRealName());
			}
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount("select o from " + 
					VsFinanceRecord.class.getName() + " o where o.vs.id = '" + id + "'", true));
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

}
