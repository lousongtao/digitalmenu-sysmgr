package com.shuishou.sysmgr.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;

public class MenuTreeNode extends DefaultMutableTreeNode {
	public MenuTreeNode(Object o){
		super(o);
	}
	
	public String toString(){
		Object o = this.getUserObject();
		if (o instanceof Category1){
			return ((Category1)o).getChineseName();
		}else if (o instanceof Category2){
			return ((Category2)o).getChineseName();
		}else if (o instanceof Dish){
			return ((Dish)o).getChineseName();
		}
		return super.toString();
	}
}
