/**  
 * @Filename:    FileUploadAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-17 下午10:46:59  
 *
 */  

package com.org.dqgb.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("FileUploadAction") @Scope("prototype")
@SuppressWarnings("serial")
public class FileUploadAction extends PrimaryAction{
	
	public File image;						//文件名
	public String imageFileName;			//文件的名字
	public String imageContentType;			//文件类型

	/**
	 *
	 * @TODO	
	 * @date	2011-3-16
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String uploadFile(){
		String path = ServletActionContext.getServletContext().getRealPath("/upload/car");
		System.out.println(path);
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = "";
		String suffix = "";
		if(null == imageFileName || "".equals(imageFileName)){
			
		}else{
			fileName = imageFileName.substring(0,imageFileName.indexOf("."));
			suffix = imageFileName.substring(imageFileName.indexOf(".") + 1, imageFileName.length());
			if("jpg".equalsIgnoreCase(suffix) && "jpeg".equalsIgnoreCase(suffix) && "bmp".equalsIgnoreCase(suffix)
					&& "gif".equalsIgnoreCase(suffix) && "png".equalsIgnoreCase(suffix)){
				//格式验证
				return null;
			}
		}
		try {
			if(image != null){
				Long time = new Date().getTime();
				FileUtil.copyFile(image,new File(path,fileName + time + "." + suffix));
				output.put("fileName", fileName + time + "." + suffix);
				
			}else{
				output.put("msg", "请指定要上传的文件");
			}
		} catch (IOException e) {
			return null;
		}
		return SUCCESS;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}
	public Map<String, Object> getOutput() {
		return output;
	}
	public void setOutput(Map<String, Object> output) {
		this.output = output;
	}
}
