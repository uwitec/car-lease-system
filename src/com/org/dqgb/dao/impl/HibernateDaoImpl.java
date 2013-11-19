/**  
 * @Filename:    HibernateDaoImpl.java  
 * @TODO:		   封装了大部分hibernate的基础操作
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-2-15 下午10:32:20  
 *  
 * @Modification History:  
 * @Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-2-15             肖乾斌        	1.0         1.0 Version  
 */  

package com.org.dqgb.dao.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.exception.DaoException;


public class HibernateDaoImpl extends HibernateDaoSupport implements HibernateDao{
	
	/**
	 *
	 * @TODO	调用存储过程，无返回值的存储过程
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	sql：sql语句,如果{call addUser(?,?)}。
	 * @param	var：存储过程的参数。index从0开始。
	 *
	 */
	public void invokeProcedure(final String sql,final Map<Integer,Object> var)
			throws DaoException {
		this.getHibernateTemplate().execute(
				new HibernateCallback(){
					@SuppressWarnings("deprecation")
					public Object doInHibernate(Session session)
							throws DaoException {
						Connection connection;
						try{
							connection = session.connection();
							CallableStatement cst = connection.prepareCall(sql);
							for(int i = 1; var != null && i <= var.size(); i++){
								cst.setObject(i, var.get(i - 1));
							}
							cst.execute();			//无返回值,需要返回值的时候调用cst.executeQuery()
						}catch(Exception e){
							throw new DaoException(e.getMessage());
						}
						return null;
					}
					
				});
		return;
	}
	
	/**
	 *
	 * @TODO	调用存储过程，必须有返回值，否则出现异常
	 * @date	2011-3-4
	 * @author	肖乾斌
	 * @param	sql
	 * @param	var
	 * @param	output						//是否需要返回数据长度
	 * @return  Map<String,Object>
	 * @throws  SQLException 
	 *
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getListFromProcedure(final String sql,final Map<Integer, Object> var,final boolean output) 
				throws DaoException {
		Map<String,Object> map = (Map<String,Object>) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					@SuppressWarnings("deprecation")
					public Object doInHibernate(Session session)throws DaoException {
						Connection connection;
						try {
							Map<String,Object> map = new HashMap<String, Object>();
							connection = session.connection();
							CallableStatement cst = connection.prepareCall(sql);
							
							for (int i = 1; var != null && i <= var.size(); i++) {
								cst.setObject(i, var.get(i - 1));
							}
							if(output){
								cst.registerOutParameter(var.size(),Types.INTEGER);
							}
							ResultSet rs = cst.executeQuery(); // 无返回值,需要返回值的时候调用cst.executeQuery()
							map.put(Constant.DATA, rs);
							if(output){
								map.put(Constant.TOTAL_SIZE, cst.getInt(var.size()));
							}
							return map;
						} catch (Exception e) {
							throw new DaoException(e.getMessage());
						}
					}
				});
		return map;
	}
	
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
	@SuppressWarnings("unchecked")
	public List<?> getObjectsList(final String sql,final int pageIndex,final int pageSize,final boolean isHql) 
			throws DaoException{
		List<Object> list = new ArrayList<Object>();
		try{
			Query query = isHql ? this.getSession().createQuery(sql) : this.getSession().createSQLQuery(sql);			//调用本地sql语句，createQuery调用hql语句
			query.setFirstResult(pageIndex * pageSize);
			query.setMaxResults(pageSize);
			list = query.list();
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return list;
	}
	
	/**
	 *
	 * @TODO	获取sql语句条件下的记录总数
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	isHql：true ? "sql代表hql语句" : "sql代表普通sql语句"
	 * 
	 */
	public int getCount(final String sql,final boolean isHql) throws DaoException{
		Query query = isHql ? this.getSession().createQuery(sql) : this.getSession().createSQLQuery(sql);
		return query.list().size();
	}
	/**
	 *
	 * @TODO	执行本地sql,返回list
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<?> getObjectsByNativeSql(final String sql)throws DaoException {
		List<Object> list = this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws DaoException {
				List<Object> list = null;
				try{
					list = session.createSQLQuery(sql).list();
				}catch(Exception e){
					throw new DaoException(e.getMessage());
				}
				return list;
			}
		});
		return list;
	}
	
	/**
	 *
	 * @TODO	执行本地sql语句，可以执行update，delete，insert等操作
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void executeNativeSql(final String sql) throws DaoException {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws DaoException {
				try{
					session.createSQLQuery(sql).executeUpdate();
				}catch(Exception e){
					throw new DaoException(e.getMessage());
				}
				return null;
			}
		});
	}
	
	/**
	 *
	 * @TODO	批量执行本地sql语句
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	queryString：sql语句集
	 *
	 */
	public int[] batchExecuteNativeSql(final List<String> queryString) throws DaoException {
		int[] num = (int[]) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					@SuppressWarnings("deprecation")
					public Object doInHibernate(Session session)
							throws DaoException {
						Connection connect;
						Statement statement;
						int[] ret = null;
						try {
							connect = session.connection();
							statement = connect.createStatement();
							for (int i = 0; i < queryString.size(); i++) {
								statement.addBatch(queryString.get(i));
							}
							ret = statement.executeBatch();
						} catch (SQLException e) {
							throw new DaoException(e.getMessage());
						}
						return ret;
					}
				});
		return num;
	}
	
	/**
	 *
	 * @TODO	保存一个实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public Serializable save(Object entity) throws DaoException{
		Serializable serializable = null;
		try{
			serializable = this.getHibernateTemplate().save(entity);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return serializable;
	}

	/**
	 *
	 * @TODO	删除实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public Object delete(Object entity) throws DaoException{
		try{
			this.getHibernateTemplate().delete(entity);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return entity;
	}

	/**
	 *
	 * @TODO	获取所有实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public List<?> getAllObjects(Class<?> c) throws DaoException{
		List<?> list = new ArrayList<Object>();
		try{
			list = this.getHibernateTemplate().find(
					"select o from " + c.getName() + " o order by o.id desc");
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return list;
	}
	
	/**
	 *
	 * @TODO	通过id获取实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public Object getObjectByID(Class<?> cls, int id) throws DaoException{
		Object obj = null;
		try{
			obj = this.getHibernateTemplate().get(cls, id);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return obj;
	}

	/**
	 *
	 * @TODO	update 实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void update(Object entity) throws DaoException{
		try{
			this.getHibernateTemplate().update(entity);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	添加或者保存实体
	 * @date	2011-2-15
	 * @author	肖乾斌
	 * @param	
	 *
	 */
	public void saveOrUpdate(Object entity) throws DaoException{
		try{
			this.getHibernateTemplate().saveOrUpdate(entity);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
	}
	
	/**
	 *
	 * @TODO	通过主键删除数据
	 * @date	2011-3-6
	 * @author	肖乾斌
	 * @param	id	主键
	 * @return  void
	 *
	 */
	@Override
	public void deleteObjectByKey(Class<?> cls,int id) throws DaoException {
		List<?> list = null;
		try{
			list = this.getHibernateTemplate().find("select o from " + cls.getName() + " o where o.id = '" + id + "'");
			if(list == null || list.size() == 0)
				return;
			this.delete(list.get(0));
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	根据限定条件查询数据
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
	@Override
	public Map<String,Object> getListByCondition(Class<?> cls,Map<String, Object> condition, Map<String, String> orderBy,int pageIndex, int pageSize)
			throws DaoException {
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select obj from " + cls.getName() + " obj where 1=1 ");
			Iterator<?> it = condition.keySet().iterator();
			Object value;
			String key;
			while (it.hasNext()) {
				key = (String) it.next();
				value = condition.get(key);
				if ("".equals(value) || value == null) {
					continue;
				} else if (value instanceof String) {
					sb.append("and obj." + key + " like '%" + value + "%' ");
				} else if (value instanceof Integer || value instanceof Long
						|| value instanceof Double || value instanceof Float) {
					String str = String.valueOf(value);
					if (!str.equals("0")) {
						sb.append("and obj." + key + "=" + value + " ");
					}
				}
			}
			//排序
			if(null != orderBy && 0 != orderBy.size()){
				it = orderBy.keySet().iterator();
				sb.append(" order by ");
				while (it.hasNext()) {
					key = (String) it.next();
					value = orderBy.get(key);
					sb.append(" " + key + " " + value);
					if(it.hasNext()){
						sb.append(",");
					}
				}
			}
			map.put(Constant.DATA, this.getObjectsList(sb.toString(), pageIndex, pageSize, true));
			map.put(Constant.TOTAL_SIZE, this.getCount(sb.toString(), true));
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return map;
	}

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
	@Override
	public List<?> getObjectsByHsql(String sql) throws DaoException {
		List<?> list = null;
		try{
			list = this.getHibernateTemplate().find(sql);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return list;
	}

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
	@Override
	public int bulkUpdate(String sql) throws DaoException {
		try{
			return this.getHibernateTemplate().bulkUpdate(sql);
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
	}

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
	@Override
	public Map<String, Object> getExactListByCondition(Class<?> cls,
			Map<String, Object> condition, Map<String, String> orderBy,
			int pageIndex, int pageSize) throws DaoException {
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select obj from " + cls.getName() + " obj where 1=1 ");
			Iterator<?> it = condition.keySet().iterator();
			Object value;
			String key;
			while (it.hasNext()) {
				key = (String) it.next();
				value = condition.get(key);
				if ("".equals(value) || value == null) {
					continue;
				} else if (value instanceof String) {
					sb.append("and obj." + key + " = '" + value + "' ");
				} else if (value instanceof Integer || value instanceof Long
						|| value instanceof Double || value instanceof Float) {
					String str = String.valueOf(value);
					if (!str.equals("0")) {
						sb.append("and obj." + key + "=" + value + " ");
					}
				}
			}
			//排序
			if(null != orderBy && 0 != orderBy.size()){
				it = orderBy.keySet().iterator();
				sb.append(" order by ");
				while (it.hasNext()) {
					key = (String) it.next();
					value = orderBy.get(key);
					sb.append(" " + key + " " + value);
					if(it.hasNext()){
						sb.append(",");
					}
				}
			}
			map.put(Constant.DATA, this.getObjectsList(sb.toString(), pageIndex, pageSize, true));
			map.put(Constant.TOTAL_SIZE, this.getCount(sb.toString(), true));
		}catch(Exception e){
			throw new DaoException(e.getMessage());
		}
		return map;
	}
}
