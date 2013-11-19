/**  
 * @Filename:    DateUtil.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-4-27 下午11:09:38  
 *
 */  

package com.org.dqgb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	/**
	 *
	 * @TODO	把字符串形式日期转换成date类型
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	date		"2010-2-1 00:00:00"
	 * @param	separator
	 * @return  Date
	 * @throws  ParseException 
	 *
	 */
	public static Date formatStringToDate(String date,String separator) throws  ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd hh:mm:ss");
		return sdf.parse(date);
	}
	
	/**
	 *
	 * @TODO	把date类型的日期转换成string类型		长字符类型 2011-04-27
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	date
	 * @param	separator
	 * @throws  ParseException
	 * @return  String
	 *
	 */
	public static String formatDateToString(Date date,String separator) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd hh:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 *
	 * @TODO	把date类型的日期转换成string类型		短字符类型 2011-4-27 11:2:2
	 * @date	2011-4-27
	 * @author	肖乾斌
	 * @param	date
	 * @param	separator
	 * @throws  ParseException
	 * @return  String
	 *
	 */
	public static String formatDateToShortString(Date date,String separator) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + separator + "M" + separator + "d h:m:s");
		return sdf.format(date);
	}
	
	/**
	 * 
	 * @TODO	按照格式转换日期
	 * @author 	肖乾斌
	 * @date 	2011-5-27
	 * @param 	date
	 * @param 	format
	 * @return 	String
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 
	 * @TODO	字符转日期
	 * @author 	肖乾斌
	 * @date 	2011-6-4
	 * @param 	date
	 * @param 	format
	 * @throws 	ParseException
	 * @return 	Date
	 */
	public static Date formatString(String date,String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
}
