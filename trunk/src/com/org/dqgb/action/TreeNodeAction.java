/**  
 * @Filename:    TreeNodeAction.java  
 * @TODO:
 * @Description:   
 * @Copyright:   Copyright (c)2009  
 * @Company:     大庆金桥成都分公司 
 * @author:      肖乾斌  
 * @version:     1.0  
 * @Create at:   2011-3-12 下午09:30:29  
 *
 */  

package com.org.dqgb.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dqgb.common.Constant;
import com.org.dqgb.entity.TreeNode;
import com.org.dqgb.entity.User;
import com.org.dqgb.service.RoleService;
import com.org.dqgb.service.TreeNodeService;

@Controller("TreeNodeAction") @Scope("prototype")
@SuppressWarnings("serial")
public class TreeNodeAction extends PrimaryAction{
	
	@Resource @Qualifier("TreeNodeServiceImpl")
	public TreeNodeService treeNodeService;
	
	@Resource @Qualifier("RoleServiceImpl")
	public RoleService	roleServiceImpl;
	
	/**
	 *
	 * @TODO	获取树节点
	 * @date	2011-3-12
	 * @author	肖乾斌
	 * @return  String
	 *
	 */
	public String getTreeData(){
		String id = getHttpServletRequest().getParameter("ID");
		try{
			List<TreeNode> list = null;
			User user = (User) getHttpSession().getAttribute(Constant.CURRENT_USER);
			List<Integer> rList = this.roleServiceImpl.getPersonalRolesID(user.getUserName());
			if(Constant.ROOT.equals(id)){
				list = this.treeNodeService.getTreeNodeByParentId(0,rList);
			}else{
				list = this.treeNodeService.getTreeNodeByParentId(Integer.parseInt(id),rList);
			}
			output.put("node", list);
		}catch(Exception e){
			output.put(Constant.FAILED, e.getMessage());
		}
		return SUCCESS;
	}
}
