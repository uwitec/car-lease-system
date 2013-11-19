/**  
 * @Filename:    TreeNodeService.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午09:34:42  
 *
 */  

package com.org.dqgb.service;

import java.util.List;

import com.org.dqgb.entity.TreeNode;
import com.org.dqgb.exception.ServiceException;

public interface TreeNodeService {
	/**
	 *
	 * @TODO	根据父节点的id获取子节点,子节点个数不能超过9999个
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	id 父节点id
	 * @throws  ServiceException
	 * @return  List<TreeNode>
	 *
	 */
	public List<TreeNode> getTreeNodeByParentId(int id) throws ServiceException;

	/**
	 *
	 * @TODO	根据父节点的id获取子节点(筛选出属于自己角色的节点),子节点个数不能超过9999个
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @param	id	父节点id
	 * @param	roleId	角色id集合
	 * @param	@throws ServiceException
	 * @return  List<TreeNode>
	 *
	 */
	public List<TreeNode> getTreeNodeByParentId(int id,List<Integer> roleId) throws ServiceException;
}
