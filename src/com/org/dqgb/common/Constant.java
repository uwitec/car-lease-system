/**  
 * @Filename:    Constant.java  
 * @TODO:		  常量类
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-16 下午10:09:42  
 *
 */  

package com.org.dqgb.common;

public class Constant {
	
	public final static String USER_LIST = "UserList";				//application 对象中存储用户列表的 key值
	public final static String CURRENT_USER = "CurrentUser";		//session 对象中存储用户信息的 key值
	public final static String RANDOM_CODE = "randomCode";			//随机码
	
	//===========预警配置================================
	public final static String CONFIGURATION = "configuration";		//配置根节点
	public final static String TYPE = "type";						//配置 类型
	public final static String APL = "apl";							//参数列表
	public final static String PARAMETER = "parameter";				//AlarmParamList
	public final static String AM_URL = "url";						//url
	public final static int AM_AFTER = 0;							//预警方式置后
	public final static int AM_BEFORE = 1;							//预警方式提前

	public final static int AM_TYPE_FILE = 1;						//预警类型---文件预警
	public final static int AM_TYPE_ORDER_FORM = 2;					//预警类型---订单预警
	public final static int AM_TYPE_VS = 3;							//预警类型---违章保证金预警
	
	//================用户帐号常量===================
	
	public final static String USERNAME	= "userName";					
	public final static String SYSTEM_ADMIN	= "systemAdmin";			//系统超级管理员，不能被删除的账户
	public final static String MALE	= "男";
	public final static String FEMALE	= "女";
	public final static String DEFAULT_PASSWORD	= "123456";				//新建用户默认密码
	public final static String VALID	= "1";							//用户有效
	public final static String INVALID	= "0";							//用户无效
	
	
	//===============ajax数据请求的结果===============
	public final static String FAILED	= "FAILED";						//失败
	public final static String SUCCESS	= "SUCCESS";					//成功
	public final static String RESULT	= "RESULT";						//结果
	public final static String REASON	= "REASON";						//原因
	
	//===============js请求中的常量===================
	public final static String ROOT	= "root";							//根节点id
	public final static String DATA	= "data";							//返回给grid控件的json数据中存放data的名字
	public final static String TOTAL_SIZE = "totalSize";				//返回给grid控件的json数据中存放数据size的名字
	
	//===============用户角色常量=====================
	public final static int USER = 1;									//普通用户
	public final static int SYSTEM_ADMINISTRATOR = 2;					//系统管理员
	public final static String ROLE = "role";							//角色变量，角色管理页面中使用
	public final static String ROLE_ID = "roleId";						//角色id变量，角色管理页面中使用
	public final static String DEPARTMENT_NAME = "departmentName";		//部门名字变量，角色管理页面中使用
	
	//===============字典组ID常量====================
	public final static int GROUPID_ROLE = 1;							//用户角色组ID号		
	public final static int GROUPID_POSITION = 2;						//职位组ID号
	public final static int GROUPID_DRIVER_STATUS = 3;					//司机状态组ID号
	public final static int GROUPID_CAR_STATUS = 4;						//汽车状态组ID号
	public final static int GROUPID_CAR_BRAND = 5;						//汽车品牌(BMW,Mercedes-Benz)
	public final static int GROUP_LEASE_WAY = 6;						//租赁方式
	public final static int GROUP_ORDER_FORM_STATUS = 7;				//订单状态
	public final static int GROUP_MONEY_TYPE = 8;						//金额类别
	public final static int GROUP_FINANCE_TYPE = 9;						//财务支付状况
	public final static int GROUP_FILE_TYPE = 10;						//文件类别
	public final static int GROUP_CAR_TYPE = 11;						//车辆类别
	public final static int GROUP_MONEY_TYPE_CM = 12;					//车务补填款项类别
	public final static int GROUP_LEASE_MODEL = 13;						//租赁模式
	
	//===============车辆类别==============================
	public final static int CAR_TYPE_GK = 51;							//挂靠
	public final static int CAR_TYPE_CG = 50;							//自购
	
	public final static String OHTER_BRAND = "其它品牌";					//汽车品牌
	public final static String BRAND_NAME = "brandName";				//汽车品牌名
	public final static String CATEGORY = "category";					//汽车系列名
	
	public final static int CAR_STATUS_IDLE = 3;						//车子处于无人使用状态(可调度)
	public final static int CAR_STATUS_BUSY = 4;						//车子处于使用中(不可调度)
	public final static int CAR_INNER_USE = 4000;						//内部用车(不可调度)
	public final static int CAR_STATUS_REPAIR = 5;						//车子处于修理状态(不可调度)
	public final static int CAR_STATUS_ACCIDENT = 6;					//车子处于事故处理(不可调度)
	
	public final static int ORDER_STATUS_BOOKING = 17;					//订单状态---预定，打印单据以后如果状态是 ORDER_STATUS_PRE_BOOK 则修改为该状态
	public final static int ORDER_STATUS_PROCESSING = 18;				//订单状态---已发车,从车务领走车以后修改该状态
	public final static int ORDER_STATUS_OVER_NORMALLY = 19;			//订单状态---已结单
	public final static int ORDER_STATUS_OVER_BEYOND_DL = 20;			//订单状态---超期结束
	public final static int ORDER_STATUS_GIVEBACK_CAR = 22;				//订单状态---已还车
	public final static int ORDER_STATUS_SUPPLIMENT = 44;				//订单状态---费用补填中
	public final static int ORDER_STATUS_FREEZED = 34;					//订单状态---冻结
	public final static int ORDER_STATUS_PRE_BOOK = 23;					//订单状态---预填(该状态的数据可以被修改或者删除)
	
	
	public final static String CAR_STATUS_REMARK = "statusRemark";		//车子状态的中文描述
	
	public final static String PRICE = "price";							//租金单价	
	public final static String FOREGIFT = "foregift";					//押金
	public final static String PRICE_PER_HOUR = "pricePerHour";			//超时费
	public final static String PRICE_PER_KM = "pricePerKm";				//超驶费
	
	//------------还车情况------------
	public final static String NORMAL_BACK = "0";						//正常归还
	public final static String BEYOND_DEADLINE = "1";					//超期归还
	
	//------------财务支付状态--------
	public final static int PREPAY	= 1;								//预付
	public final static int SETTLEMENT = 2;								//结算
	
	//------------财务金额状态--------
	public final static int GROUP_F_PRICE = 24;							//租金	
	public final static int GROUP_F_FOREGIFT = 25;						//押金
	public final static int GROUP_F_BOOKINGFEE = 26;					//预约金
	public final static int GROUP_F_LATE_FEE = 27;						//超时费
	public final static int GROUP_F_FAITH_FEE = 28;						//违约金
	public final static int GROUP_F_OTHER_FEE = 29;						//其它费用
	public final static int GROUP_F_RELET_FEE = 49;						//续租费
	public final static int GROUP_F_OVER_DRIVE_FEE = 48;				//超驾费
	public final static int GROUP_F_OIL_FEE = 47;						//油费
	public final static int GROUP_F_FORFEIT_FEE = 46;					//违章罚款费
	public final static int GROUP_F_MISTAKE_WORK_FEE = 45;				//误工费
	public final static int GROUP_F_ACCIDENT = 99;						//事故处理费
	public final static int GROUP_F_DISCOUNT = 100;						//优惠
	
	public final static int FINANCE_WAITING_OUT = 33;					//待还
	public final static int FINANCE_WAITING = 32;						//待收
	public final static int FINANCE_IN = 31;							//已收
	public final static int FINANCE_OUT = 30;							//已还
	
	//===========租赁模式==================
	public final static int LEASE_MODEL_RESTRICT_MILE = 53;				//限里程计费
	public final static int LEASE_MODEL_RESTRICT_TIME = 52;				//限时计费
	
	
	public final static String ERROR = "ERROR";							//错误
}
