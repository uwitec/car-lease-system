/**  
 * @Filename:    FileManagement.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-5-6 上午09:37:18  
 *
 */  

package com.org.dqgb.entity;

import java.util.Date;

public class FileManagement {
	private int id;					
	private int fileType;						//文件类型		
	private String carId;						//车牌号
	private String fileIdentifyNumber;			//证件号
	private Date dateCheckIn;					//办理日期
	private Date usefulLife;					//证件到期时限
	private String filePath;					//证件图片
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getFileIdentifyNumber() {
		return fileIdentifyNumber;
	}
	public void setFileIdentifyNumber(String fileIdentifyNumber) {
		this.fileIdentifyNumber = fileIdentifyNumber;
	}
	public Date getDateCheckIn() {
		return dateCheckIn;
	}
	public void setDateCheckIn(Date dateCheckIn) {
		this.dateCheckIn = dateCheckIn;
	}
	public Date getUsefulLife() {
		return usefulLife;
	}
	public void setUsefulLife(Date usefulLife) {
		this.usefulLife = usefulLife;
	}
}
