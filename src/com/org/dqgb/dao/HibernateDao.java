/**  
 * @Filename:    HibernateDao.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-15 下午10:33:44  
 *  
 * @Modification History:  
 * @Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-2-15      肖乾斌        	1.0         1.0 Version  
 */  

package com.org.dqgb.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.org.dqgb.exception.DaoException;


public interface HibernateDao {
	/**
	 *
	 * @TODO	调用存储过程，无返回值的
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	sql：sql语句,如果{call addUser(?,?)}。
	 * @param	var：存储过程的参数。index从0开始。
	 *
	 */
	public void invokeProcedure(final String sql,final Map<Integer,Object> var)throws DaoException;
	
	/**
	 *
	 * @TODO	调用存储过程，有返回值的
	 * @date	2011-3-4
	 * @author	肖乾斌
	 * @param	sql
	 * @param	var
	 * @param	output				//是否需要返回数据长度
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getListFromProcedure(final String sql, final Map<Integer,Object> var,boolean output) throws DaoException;
	/**
	 *
	 * @TODO	执行本地sql,返回list
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public List<?> getObjectsByNativeSql(final String sql) throws DaoException;
	
	/**
	 *
	 * @TODO	通过hsql获取所有对象信息
	 * @date	2011-3-23
	 * @author	肖乾斌
	 * @param	sql
	 * @throws  DaoException
	 * @return  List<?>
	 *
	 */
	public List<?> getObjectsByHsql(final String sql) throws DaoException;
	/**
	 *
	 * @TODO	获取分页数据
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	isHql：true ? "sql代表hql语句" : "sql代表普通sql语句"
	 * @param	pageIndex：页数，从0开始
	 * @param	pageSize：每页显示的数据条数
	 *
	 */
	public List<?> getObjectsList(String sql,int pageIndex,int pageSize,boolean isHql) throws DaoException;
	
	/**
	 *
	 * @TODO	根据限定条件查询数据,模糊查询
	 * @date	2011-3-22
	 * @author	肖乾斌
	 * @param	cls						被查询的hibernate entity的class对象
	 * @param	condition				条件字段
	 * @param	orderBy					排序条件
	 * @param	pageIndex				页码
	 * @param	pageSize				每页显示数据个数
	 * @throws  DaoException
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getListByCondition(Class<?> cls,Map<String,Object> condition,Map<String, String> orderBy,int pageIndex, int pageSize) throws DaoException;
	
	/**
	 *
	 * @TODO	根据限定条件查询数据,精确查找
	 * @date	2011-5-7
	 * @author	肖乾斌
	 * @param	cls
	 * @param	condition
	 * @param	orderBy
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  DaoException
	 * @return  Map<String,Object>
	 *
	 */
	public Map<String,Object> getExactListByCondition(Class<?> cls,Map<String,Object> condition,Map<String, String> orderBy,int pageIndex, int pageSize)throws DaoException;
	/**
	 *
	 * @TODO	获取sql语句条件下的记录总数
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	isHql：true ? "sql代表hql语句" : "sql代表普通sql语句"
	 * 
	 */
	public int getCount(String sql,boolean isHql) throws DaoException;
	/**
	 *
	 * @TODO	执行本地sql语句，可以执行update，delete，insert等操作
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void executeNativeSql(final String sql) throws DaoException;
	
	/**
	 *
	 * @TODO	批量执行本地sql语句
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	queryString：sql语句集
	 *
	 */
	public int[] batchExecuteNativeSql(final List<String> queryString) throws DaoException;
	
	/**
	 *
	 * @TODO	执行hsql
	 * @date	2011-4-30
	 * @author	肖乾斌
	 * @param	sql
	 * @throws  DaoException
	 * @return  int[]
	 *
	 */
	public int bulkUpdate(String sql) throws DaoException;
	/**
	 *
	 * @TODO	保存一个实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public Serializable save(Object entity) throws DaoException;
	
	/**
	 *
	 * @TODO	删除实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public Object delete(Object entity) throws DaoException;

	/**
	 *
	 * @TODO	通过主键删除数据
	 * @date	2011-3-6
	 * @author	肖乾斌
	 * @param	id	主键
	 * @return  void
	 *
	 */
	public void deleteObjectByKey(Class<?> cls,int id) throws DaoException;
	/**
	 *
	 * @TODO	获取所有实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public List<?> getAllObjects(Class<?> c) throws DaoException;
	
	/**
	 *
	 * @TODO	通过id获取实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	cls：实体类名(全路径名)
	 * @param	id：实体类id
	 *
	 */
	public Object getObjectByID(Class<?> cls, int id) throws DaoException;

	/**
	 *
	 * @TODO	update 实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void update(Object entity) throws DaoException;
	
	/**
	 *
	 * @TODO	添加或者保存实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void saveOrUpdate(Object entity) throws DaoException;
}
