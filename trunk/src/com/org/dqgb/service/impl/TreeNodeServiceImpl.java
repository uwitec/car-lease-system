/**  
 * @Filename:    TreeNodeServiceImpl.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午09:39:15  
 *
 */  

package com.org.dqgb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.org.dqgb.dao.HibernateDao;
import com.org.dqgb.entity.TreeNode;
import com.org.dqgb.exception.ServiceException;
import com.org.dqgb.service.TreeNodeService;

@Service("TreeNodeServiceImpl")
public class TreeNodeServiceImpl implements TreeNodeService{

	@Resource @Qualifier("hibernateDao")					//按名称进行装配
	public HibernateDao hibernateDao;
	
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 *
	 * @TODO	根据父节点的id获取子节点,子节点个数不能超过9999个
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	@param id
	 * @throws  ServiceException
	 * @return  List<TreeNode>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> getTreeNodeByParentId(int id) throws ServiceException{
		List<TreeNode> list = null;
		try{
			list = (List<TreeNode>) this.hibernateDao.getObjectsList("select o from " + TreeNode.class.getName() + 
					" o where o.parentId = '" + id + "'", 0, 9999, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return (list == null || list.size() == 0) ? null : list;
	}
	
	/**
	 *
	 * @TODO	根据父节点的id获取子节点(筛选出属于自己角色的节点),子节点个数不能超过9999个
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	id	父节点id
	 * @param	roleId	角色id集合
	 * @throws  ServiceException
	 * @return  List<TreeNode>
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeNode> getTreeNodeByParentId(int id, List<Integer> roleId) throws ServiceException{
		List<TreeNode> list = null;
		try{
			String sql = "select o from " + TreeNode.class.getName() + 
							" o where o.parentId = '" + id + "' and (";
			for(int i = 0; i < roleId.size(); i++){
				sql += " o.roleId = " + roleId.get(i);
				if(i < roleId.size() - 1){
					sql += " or ";
				}
			}
			sql += ")";
			list = (List<TreeNode>) this.hibernateDao.getObjectsList(sql, 0, 9999, true);
		}catch(Exception e){
			log.error(e.getMessage(), e); throw new ServiceException(e.getMessage());
		}
		return (list == null || list.size() == 0) ? null : list;
	}

}
