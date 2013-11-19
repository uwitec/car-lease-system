/**  
 * @Filename:    FileManagementAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-8 下午07:32:05  
 *
 */  

package com.org.dqgb.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.FileManagement;
import com.org.dqgb.service.FileManagementService;

@Controller("FileManagementAction")	@Scope("prototype")
@SuppressWarnings("serial")
public class FileManagementAction extends PrimaryAction {
	
	@Resource	@Qualifier("FileManagementServiceImpl")
	public FileManagementService fileManagementService;
	
	
	public FileManagement file;
	
	public void setFile(FileManagement file) {
		this.file = file;
	}



	/**
	 *
	 * @TODO	获取文件信息
	 * @date	2011-5-8
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getFileInformation(){
		try{
			output.put(Constant.DATA, this.fileManagementService.getFileInformation(
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize"))
					));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 * 根据车牌查找文件
	 * @author 	肖乾斌
	 * @date 	2011-12-25
	 * @return 	String
	 */
	public String getFileByCar(){
		try{
			output.put(Constant.DATA, this.fileManagementService.getFileInformation(
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					Integer.parseInt(this.getHttpServletRequest().getParameter("pageSize")),this.getHttpServletRequest().getParameter("carId")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	通过ID获取文件信息
	 * @date	2011-5-8
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getFileInformationByID(){
		try{
			output.put(Constant.DATA, this.fileManagementService.getFileInformationById(
					Integer.parseInt(this.getHttpServletRequest().getParameter("id"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	新增文件管理信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String addFile(){
		try{
			this.fileManagementService.addFileInformation(file);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}

	/**
	 *
	 * @TODO	删除文件管理信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String deleteFile(){
		try{
			FileManagement fm = this.fileManagementService.getFileInformationById(
					Integer.parseInt(this.getHttpServletRequest().getParameter("id")));
			this.fileManagementService.deleteFileInformation(fm);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	修改文件管理信息
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String modifyFile(){
		try{
			this.fileManagementService.modifyFileInformation(this.file);
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
			
		}
		return SUCCESS;
	}
	
	/**
	 *
	 * @TODO	跳转到档案修改页面
	 * @date	2011-5-9
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String gotoModifyFilePage(){
		try{
			this.getHttpSession().setAttribute("fileManagementID", this.getHttpServletRequest().getParameter("id"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到档案增加页面
	 * @author 	肖乾斌
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String gotoAddFilePage(){
		try{
			this.getHttpServletRequest().setAttribute("carId", new String(this.getHttpServletRequest().getParameter("carId").getBytes("8859_1"), "UTF-8"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute(Constant.ERROR,e.getMessage());
			return "failed";
		}
		return SUCCESS;
	}
}
