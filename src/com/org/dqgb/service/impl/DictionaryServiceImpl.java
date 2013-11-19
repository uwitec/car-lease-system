/**  
 * @Filename:    DictionaryServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-14 下午03:32:26  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.List;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.common.Constant;
import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.Dictionary;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.DictionaryService;

@Service("DictionaryServiceImpl")
public class DictionaryServiceImpl implements DictionaryService {

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	获取所有字典信息
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getDictionaryInformation(int pageIndex, int pageSize)
			throws ServiceException {
		List<Dictionary> list = null;
		try{
			list = (List<Dictionary>) this.hibernateDao.getObjectsList("select o from " + 
					Dictionary.class.getName() + " o order by o.groupId,o.id asc", 
					pageIndex, pageSize, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}

	/**
	 *
	 * @TODO	根据字典id号码获取字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	id
	 * @throws  ServiceException
	 * @return  Dictionary
	 *
	 */
	@Override
	public Dictionary getDictionaryById(int id) throws ServiceException {
		Dictionary dic = null;
		try{
			dic = (Dictionary) this.hibernateDao.getObjectByID(Dictionary.class, id);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}

		return dic;
	}

	/**
	 *
	 * @TODO	获取字典数据数量
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  int
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int getDictionaryCount() throws ServiceException {
		int count = 0;
		try{
			count = this.hibernateDao.getCount("select o from " + Dictionary.class.getName() + 
					" o ", true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return count;
	}

	/**
	 *
	 * @TODO	修改字典数据
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean modifyDictionaryInformation(Dictionary dic)
			throws ServiceException {
		if(dic == null || 0 == dic.getId()){
			throw new ServiceException("请求修改的字典信息是无效信息!");
		}
		if("".equals(dic.getRemark().trim()) || null == dic.getRemark()){
			throw new ServiceException("字典名字不能为空!");
		}
		try{
			Dictionary dictionary = this.getDictionaryById(dic.getId());
			dictionary.setRemark(dic.getRemark());
			dictionary.setGroupRemark(dic.getGroupRemark());
			dictionary.setGroupId(dic.getGroupId());
			this.hibernateDao.update(dictionary);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	根据分组id获取字典数据(角色信息的分组id为1)
	 * @date	2011-3-14
	 * @author	肖乾斌
	 * @param	groupId	分组id
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getDictionaryByGroupId(int groupId,int pageIndex,int pageSize)
			throws ServiceException {
		List<Dictionary> list = null;
		try{
			list = (List<Dictionary>) this.hibernateDao.getObjectsList("select o from " + Dictionary.class.getName() + 
					" o where o.groupId = '" + groupId + "'", pageIndex, pageSize, true);
			if(null == list || list.size() == 0)
				return null;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return list;
	}

	/**
	 *
	 * @TODO	在字典表的角色组中添加一个字典数据
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	roleName	字典的remark信息
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean createPositionRole(String roleName) throws ServiceException {
		if(null == roleName || "".equals(roleName)){
			throw new ServiceException("角色信息不能为空!");
		}
		if(roleName.length() > 50){
			throw new ServiceException("角色名长度不能超过50字节!");
		}
		try{
			Dictionary dic = new Dictionary();
			dic.setGroupRemark("职位角色");
			dic.setGroupId(Constant.GROUPID_POSITION);
			dic.setRemark(roleName);
			this.hibernateDao.save(dic);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	删除字典表中的职位字典数据，级联删除角色表中对应的角色信息
	 * @date	2011-3-15
	 * @author	肖乾斌
	 * @param	roleId
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean deletePositionRole(int roleId) throws ServiceException {
		Dictionary dic = this.getDictionaryById(roleId);
		if(dic.getGroupId() != Constant.GROUPID_POSITION){
			throw new ServiceException("不能删除非职位组字典数据");
		}
		try{
			this.hibernateDao.deleteObjectByKey(Dictionary.class, roleId);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	获取品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getBrandInformation(int pageIndex, int pageSize)
			throws ServiceException {
		JSONObject json = new JSONObject();
		try{
			JSONArray arr = null;
			String sql = "select o from " + Dictionary.class.getName() + " o where o.groupId = '" + Constant.GROUPID_CAR_BRAND + "'";
			int count = 0;
			count = this.hibernateDao.getCount(sql, true);
			List<Dictionary> list = (List<Dictionary>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			arr = JSONArray.fromObject(list);
			json.put(Constant.DATA, arr);
			json.put(Constant.TOTAL_SIZE, count);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return json;
	}

	/**
	 *
	 * @TODO	添加品牌数据信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public boolean addBrand(Dictionary dic) throws ServiceException {
		if(null == dic || "".equals(dic.getRemark()) || null == dic.getRemark()){
			throw new ServiceException("品牌信息不能为空!");
		}
		List<Dictionary> list = (List<Dictionary>) this.hibernateDao.getObjectsByNativeSql(
				"SELECT * FROM T_DICTIONARY WHERE GROUP_ID = '" + Constant.GROUPID_CAR_BRAND +
				"' AND REMARK = '" + dic.getRemark() + "'");
		if(null != list && list.size() != 0){
			throw new ServiceException("已经存在一个相同的品牌信息!");
		}
		try{
			dic.setGroupId(Constant.GROUPID_CAR_BRAND);
			dic.setGroupRemark("车品牌");
			this.hibernateDao.saveOrUpdate(dic);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	修改品牌信息
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public boolean modifyBrand(Dictionary dic) throws ServiceException {
		if("".equals(dic.getRemark())){
			throw new ServiceException("品牌信息不能为空!");
		}
		if(dic.getGroupId() == Constant.GROUP_MONEY_TYPE){
			throw new ServiceException("财务类型字典信息无法被篡改!");
		}
		List<Dictionary> list = (List<Dictionary>) this.hibernateDao.getObjectsByNativeSql(
				"SELECT * FROM T_DICTIONARY WHERE GROUP_ID = '" + Constant.GROUPID_CAR_BRAND +
				"' AND REMARK = '" + dic.getRemark() + "'");
		if(null != list && list.size() != 0){
			throw new ServiceException("已经存在一个相同的品牌信息!");
		}
		Dictionary dictionary = this.getDictionaryById(dic.getId());
		if(Constant.OHTER_BRAND.equals(dictionary.getRemark())){
			throw new ServiceException("该条数据(其它品牌)不允许修改!");
		}
		try{
			dictionary.setRemark(dic.getRemark());
			this.hibernateDao.saveOrUpdate(dictionary);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	删除品牌数据
	 * @date	2011-3-20
	 * @author	肖乾斌
	 * @param	dic
	 * @throws  ServiceException
	 * @return  boolean
	 *
	 */
	@Override
	public boolean deleteBrand(Dictionary dic) throws ServiceException {
		if(dic.getGroupId() != Constant.GROUPID_CAR_BRAND)
			throw new ServiceException("不能删除非品牌信息数据!");
		if(dic.getRemark() == Constant.OHTER_BRAND){
			throw new ServiceException("不能删除'其它品牌'数据!");
		}
		try{
			this.hibernateDao.delete(dic);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return true;
	}

	/**
	 *
	 * @TODO	获取租赁信息
	 * @date	2011-4-28
	 * @author	肖乾斌
	 * @throws  ServiceException
	 * @return  List<Dictionary>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionary> getLeaseWay() throws ServiceException {
		try{
			String sql = "select o from " + Dictionary.class.getName() + " o where o.groupId = '" + Constant.GROUP_LEASE_WAY + "'";
			List<Dictionary> list = (List<Dictionary>) this.hibernateDao.getObjectsList(sql, 0, 100, true);
			return list;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

	/**
	 *
	 * @TODO	根据分组id获取字典数据 json版本
	 * @date	2011-5-6
	 * @author	肖乾斌
	 * @param	groupId
	 * @param	pageIndex
	 * @param	pageSize
	 * @throws  ServiceException
	 * @return  JSONObject
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getDicByGroupId(int groupId, int pageIndex, int pageSize)
			throws ServiceException {
		try{
			String sql = "select o from " + Dictionary.class.getName() + " o where o.groupId = '" + groupId + "'";
			List<Dictionary> list = (List<Dictionary>) this.hibernateDao.getObjectsList(sql, pageIndex, pageSize, true);
			JSONObject json = new JSONObject();
			json.put(Constant.DATA, JSONArray.fromObject(list));
			json.put(Constant.TOTAL_SIZE, this.hibernateDao.getCount(sql, true));
			return json;
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
	}

}
