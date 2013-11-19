/**  
 * @Filename:    ChartsAction.java  
 * @TODO:		  报表action
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公叄1�7 
 * @author:      肖乾斄1�7  
 * @version:     1.0  
 * @Create at:   2011-3-20 上午10:43:30  
 *
 */  

package com.org.dqgb.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.service.ReportService;
import com.org.dqgb.util.DateUtil;

@Controller("ChartsAction")	@Scope("prototype")
@SuppressWarnings("serial")
public class ChartsAction extends PrimaryAction {

	public Date date;
	
	@Resource(name="ReportServiceImpl")
	public ReportService reportService;
	/**
	 * 
	 * @TODO	获取锄1�7售日报数捄1�7
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-27
	 * @return 	String
	 */
	public String getDailyReport(){
		try{
			output.put(Constant.DATA, this.reportService.getDailyReport(date, 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")), 
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到销售日报界靄1�7
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-27
	 * @return 	String
	 */
	public String gotoDailyChartPage(){
		try{
			Date dt = new Date();
			this.getHttpServletRequest().setAttribute("DATE", DateUtil.formatDate(dt, "yyyy-M-d"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转到销售月报界靄1�7
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-27
	 * @return 	String
	 */
	public String gotoMensalChartPage(){
		try{
			Date dt = new Date();
			this.getHttpServletRequest().setAttribute("DATE", DateUtil.formatDate(dt, "yyyy-M-d"));
		}catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	按日期统计日锄1�7售额
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-28
	 * @return 	String
	 */
	public String getDailyReportSum(){
		try{
			this.output.put(Constant.DATA,  this.reportService.getDailyReportSum(date));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	跳转刄1�7	日销唄1�7/月报统计	 明细界面
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-28
	 * @return 	String
	 */
	public String gotoDailyReportDetails(){
		try{
			this.getHttpServletRequest().setAttribute("date", this.getHttpServletRequest().getParameter("date"));
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute("ERROR", e.getMessage());
			return "failed";
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取日销售额统计明细
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String getDailyReportSumDetails(){
		try{
			output.put(Constant.DATA, this.reportService.getDailyReportSumDetails(date));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取月报
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String getMensalReport(){
		try{
			output.put(Constant.DATA, this.reportService.getMensalReport(date,
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	获取月报
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String getMensalReportSum(){
		try{
			output.put(Constant.DATA, this.reportService.getMensalReportSum(date));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取月报公司明细
	 * @author 	肖乾斄1�7
	 * @date 	2011-6-2
	 * @return 	String
	 */
	public String getMensalReportSumDetails(){
		try{
			output.put(Constant.DATA, this.reportService.getMensalReportSumDetails(date,
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * @TODO	获取月报
	 * @author 	肖乾斄1�7
	 * @date 	2011-5-29
	 * @return 	String
	 */
	public String getMensalReportByBrand(){
		try{
			output.put(Constant.DATA, this.reportService.getMensalReport(date,
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("brand"))));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	获取每日担保金的收入统计明细
	 * @author 	肖乾斄1�7
	 * @date 	2011-6-4
	 * @return 	String
	 */
	public String getDailyViolateSuretySum(){
		try{
			this.output.put(Constant.DATA, this.reportService.getDailyViolateSuretySum(date));
		}catch(Exception e){
			
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @TODO	跳转到担保金统计页面
	 * @author 	肖乾斄1�7
	 * @date 	2011-6-4
	 * @return 	String
	 */
	@SuppressWarnings("deprecation")
	public String gotoDailyViolateSuretySumPage(){
		try{
			Date dt = new Date();
			this.getHttpServletRequest().setAttribute("date",DateUtil.formatDate(dt, "yyyy-M-d"));
			this.getHttpServletRequest().setAttribute("year",dt.getYear() + 1900);
			this.getHttpServletRequest().setAttribute("month",dt.getMonth() + 1);
		}catch(Exception e){
			
			this.getHttpServletRequest().setAttribute("ERROR", e.getMessage());
			return FAILED;
		}
		return SUCCESS;
	}
	/**
	 * 获取事故统计报表
	 * @author 	肖乾斌
	 * @date 	2011-9-13
	 * @return 	String
	 */
	public String getAccidentReport(){
		try{
			this.output.put(Constant.DATA, this.reportService.getAccidentReport(date));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取消费总计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	public String getConsumeSummary(){
		try{
			this.output.put(Constant.DATA, this.reportService.getConsumeSummary(
					this.getHttpServletRequest().getParameter("customerId")));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取欠费总计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	public String getArrearage(){
		try{
			this.reportService.updateArrearage();
			this.output.put(Constant.DATA, this.reportService.getArrearage(
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取欠费统计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	public String getArrearageSummary(){
		try{
			this.output.put(Constant.DATA, this.reportService.getArrearageSummary());
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取员工产值统计
	 * @author 	肖乾斌
	 * @date 	2011-9-30
	 * @return 	String
	 */
	@SuppressWarnings("deprecation")
	public String getOutputSummary(){
		try{
			int year = NumberUtils.toInt(this.getHttpServletRequest().getParameter("year"));
			year = year == 0 ? new Date().getYear() + 1900 : year;
			this.output.put(Constant.DATA, this.reportService.getOutputSummary(
					year,
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageIndex")),
					NumberUtils.toInt(this.getHttpServletRequest().getParameter("pageSize"))					
			));
		}catch(Exception e){
			output.put(Constant.RESULT, Constant.FAILED);
			output.put(Constant.REASON, e.getMessage());
		}
		return SUCCESS;
	}
	
}
