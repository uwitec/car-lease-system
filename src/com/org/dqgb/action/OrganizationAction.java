/**  
 * @Filename:    OrganizationAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-17 下午02:48:50  
 *
 */  

package com.org.dqgb.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Organization;
import com.org.dqgb.service.OrganizationService;

@Controller("OrganizationAction") @Scope("prototype")
@SuppressWarnings("serial")
public class OrganizationAction extends PrimaryAction{

	@Resource @Qualifier("OrganizationServiceImpl")
	public OrganizationService organizationService;
	
	/**
	 *
	 * @TODO	获取系统组织数据
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getOrganization(){
		try{
			List<Organization> list = organizationService.getSystemOrganization(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, list);
			output.put(Constant.TOTAL_SIZE, organizationService.getOrganizationCount());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到修改组织界面
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyOrganizationPage(){
		try{
			int orgId = Integer.parseInt(getHttpServletRequest().getParameter("id"));
			Organization org = this.organizationService.getOrganizationById(orgId);
			getHttpSession().setAttribute("Organization", org);
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	增加一个组织
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addOrganization(){
		try{
			String departmentName = getHttpServletRequest().getParameter("departmentName");
			Organization org = new Organization();
			org.setDepartmentName(departmentName);
			this.organizationService.addOrganization(org);
		}catch(Exception e){
			output.put(Constant.REASON, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	/**
	 *
	 * @TODO	修改组织数据
	 * @date	2011-3-17
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyOrganization(){
		try{
			String departmentName = getHttpServletRequest().getParameter("departmentName");
			String id = getHttpServletRequest().getParameter("id");
			Organization org = organizationService.getOrganizationById(Integer.parseInt(id));
			org.setDepartmentName(departmentName);
			this.organizationService.modifyOrganization(org);
		}catch(Exception e){
			output.put(Constant.REASON, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	
}
