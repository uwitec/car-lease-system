/**  
 * @Filename:    DictionaryAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午03:36:07  
 *
 */  

package com.org.dqgb.action;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.service.DictionaryService;

@Controller("DictionaryAction") @Scope("prototype")
@SuppressWarnings("serial")
public class DictionaryAction extends PrimaryAction {
	
	@Resource @Qualifier("DictionaryServiceImpl")
	public DictionaryService dictionaryService;
	/**
	 *
	 * @TODO	获取字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getDictionaryInformation(){
		try{
			List<Dictionary> list = this.dictionaryService.getDictionaryInformation(
					Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, list);
			output.put(Constant.TOTAL_SIZE, this.dictionaryService.getDictionaryCount());
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到修改字典数据页面
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyDictionaryPage(){
		try{
			String id = this.getHttpServletRequest().getParameter("id");
			getHttpSession().setAttribute("SpecifiedDictionary", 
					this.dictionaryService.getDictionaryById(Integer.parseInt(id)));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyDictionaryInformation(){
		try{
			Dictionary dic = new Dictionary();
			dic.setId(Integer.parseInt(getHttpServletRequest().getParameter("id")));
			dic.setRemark(getHttpServletRequest().getParameter("remark"));
			dic.setGroupRemark(getHttpServletRequest().getParameter("groupRemark"));
			dic.setGroupId(Integer.parseInt(getHttpServletRequest().getParameter("groupId")));
			this.dictionaryService.modifyDictionaryInformation(dic);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	创建一个职位角色
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String createPositionRole(){
		try{
			this.dictionaryService.createPositionRole(getHttpServletRequest().getParameter("roleName"));
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	删除字典表中的职位数据，级联删除会连带清除角色表中的相关数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deletePosition(){
		try{
			this.dictionaryService.deletePositionRole(
					Integer.parseInt(this.getHttpServletRequest().getParameter("id")));
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取车品牌信息数据
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getBrandInformation(){
		try{
			JSONObject json = this.dictionaryService.getBrandInformation(
				Integer.parseInt(getHttpServletRequest().getParameter("pageIndex")),
				Integer.parseInt(getHttpServletRequest().getParameter("pageSize")));
			output.put(Constant.DATA, json);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	添加品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addBrand(){
		try{
			String brandName = this.getHttpServletRequest().getParameter("brandName");
			Dictionary dic = new Dictionary();
			dic.setRemark(brandName);
			dictionaryService.addBrand(dic);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyBrand(){
		try{
			String brandName = this.getHttpServletRequest().getParameter("brandName");
			int id = Integer.parseInt(this.getHttpServletRequest().getParameter("id"));
			Dictionary dic = new Dictionary();
			dic.setId(id);
			dic.setRemark(brandName);
			dictionaryService.modifyBrand(dic);
			output.put(Constant.RESULT, Constant.SUCCESS);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到修改品牌信息界面
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyBrandPage(){
		try{
			int id = Integer.parseInt(this.getHttpServletRequest().getParameter("id"));
			Dictionary brand = dictionaryService.getDictionaryById(id);
			this.getHttpSession().setAttribute("BRAND", brand);
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	删除品牌数据
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deleteBrand(){
		try{
			int id = Integer.parseInt(this.getHttpServletRequest().getParameter("id"));
			this.dictionaryService.deleteBrand(this.dictionaryService.getDictionaryById(id));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取租赁方式
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getLeaseWay(){
		try{
 			output.put(Constant.DATA, this.dictionaryService.getLeaseWay());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	获取文件类型
	 * @date	2011-5-6
	 * @author	肖乾斌
	 *
	 */
	public String getFileTypes(){
		try{
 			output.put(Constant.DATA, this.dictionaryService.getDictionaryByGroupId(Constant.GROUP_FILE_TYPE, 0, 1000));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取租赁模式信息
	 * @author 	肖乾斌
	 * @date 	2011-5-25
	 * @return 	String
	 */
	public String getLeaseModel(){
		try{
 			output.put(Constant.DATA, this.dictionaryService.getDictionaryByGroupId(Constant.GROUP_LEASE_MODEL, 0, 1000));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
}
