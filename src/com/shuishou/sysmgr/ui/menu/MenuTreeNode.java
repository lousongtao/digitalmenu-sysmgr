package com.shuishou.sysmgr.ui.menu;

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
			return ((Category1)o).getFirstLanguageName();
		}else if (o instanceof Category2){
			return ((Category2)o).getFirstLanguageName();
		}else if (o instanceof Dish){
			if (((Dish)o).isPromotion()){
				return "<html>"+((Dish)o).getFirstLanguageName()+"<font color='red'>[Promotion]</font></html>";
			} else {
				return ((Dish)o).getFirstLanguageName();
			}
		}
		return super.toString();
	}
}
