package com.shuishou.sysmgr.ui.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberInputDialog;

public class MenuMgmtPanel extends JPanel implements TreeSelectionListener, ActionListener{
	private final Logger logger = Logger.getLogger(MenuMgmtPanel.class.getName());
	private JTabbedPane tabPane;
	private Category1Panel pCategory1;
	private Category2Panel pCategory2;
	private DishPanel pDish;
	private JTree menuTree;
	private JPopupMenu popupmenuRoot = new JPopupMenu();
	private JMenuItem menuitemAddC1 = new JMenuItem(Messages.getString("MenuMgmtPanel.AddCategory1"));
	private JMenuItem menuitemRefreshTree = new JMenuItem(Messages.getString("MenuMgmtPanel.RefreshTree"));
	private JPopupMenu popupmenuC1 = new JPopupMenu();
	private JMenuItem menuitemModifyC1 = new JMenuItem(Messages.getString("MenuMgmtPanel.Modify"));
	private JMenuItem menuitemAddC2 = new JMenuItem(Messages.getString("MenuMgmtPanel.AddCategory2"));
	private JMenuItem menuitemDeleteC1 = new JMenuItem(Messages.getString("MenuMgmtPanel.Delete"));
	private JPopupMenu popupmenuC2 = new JPopupMenu();
	private JMenuItem menuitemModifyC2 = new JMenuItem(Messages.getString("MenuMgmtPanel.Modify"));
	private JMenuItem menuitemAddDish = new JMenuItem(Messages.getString("MenuMgmtPanel.AddDish"));
	private JMenuItem menuitemDeleteC2 = new JMenuItem(Messages.getString("MenuMgmtPanel.Delete"));
	private JPopupMenu popupmenuDish = new JPopupMenu();
	private JMenuItem menuitemModifyDish = new JMenuItem(Messages.getString("MenuMgmtPanel.Modify"));
	private JMenuItem menuitemDeleteDish = new JMenuItem(Messages.getString("MenuMgmtPanel.Delete"));
//	private JMenuItem menuitemSpecial = new JMenuItem("Special");
//	private JMenuItem menuitemNewDish = new JMenuItem("New");
	private JMenuItem menuitemChangePic= new JMenuItem(Messages.getString("MenuMgmtPanel.ChangePicture"));
	private JMenuItem menuitemChangePrice = new JMenuItem("Change Price");
	private JMenuItem menuitemChangePromotion = new JMenuItem();
	private JMenuItem menuitemSoldout = new JMenuItem("Soldout");
	
	private ArrayList<Category1> category1s ;
	private MainFrame mainFrame;
	
	public MenuMgmtPanel(MainFrame mainFrame, ArrayList<Category1> category1s){
		this.mainFrame = mainFrame;
		this.category1s = category1s;
		initUI();
	}
	
	private void initUI(){
		//build tree
		MenuTreeNode root = new MenuTreeNode("root");
		buildTree(root);
		menuTree = new JTree(root);
		menuTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		menuTree.addTreeSelectionListener(this);
		JScrollPane jspTree = new JScrollPane(menuTree);
		Dimension d = jspTree.getPreferredSize();
		d.width = 300;
		jspTree.setPreferredSize(d);
		
		//build Tab
		pCategory1 = new Category1Panel(this);
		pCategory2 = new Category2Panel(this);
		pDish = new DishPanel(this);
		tabPane = new JTabbedPane();
		tabPane.add("Category1", pCategory1);
		tabPane.add("Category2", pCategory2);
		tabPane.add("Dish", pDish);
		this.setLayout(new BorderLayout());
		add(jspTree, BorderLayout.WEST);
		add(tabPane, BorderLayout.CENTER);
		
		pDish.showChooseSubitemButton(false);
		//build popup menu
		popupmenuRoot.add(menuitemAddC1);
		popupmenuRoot.add(menuitemRefreshTree);
		popupmenuC1.add(menuitemModifyC1);
		popupmenuC1.add(menuitemAddC2);
		popupmenuC1.add(menuitemDeleteC1);
		popupmenuC2.add(menuitemModifyC2);
		popupmenuC2.add(menuitemAddDish);
		popupmenuC2.add(menuitemDeleteC2);
		popupmenuDish.add(menuitemModifyDish);
//		popupmenuDish.add(menuitemSpecial);
//		popupmenuDish.add(menuitemNewDish);
		popupmenuDish.add(menuitemChangePic);
		popupmenuDish.add(menuitemChangePrice);
		popupmenuDish.add(menuitemSoldout);
		popupmenuDish.add(menuitemChangePromotion);
		popupmenuDish.add(menuitemDeleteDish);
		menuitemAddC1.addActionListener(this);
		menuitemRefreshTree.addActionListener(this);
		menuitemModifyC1.addActionListener(this);
		menuitemAddC2.addActionListener(this);
		menuitemModifyC2.addActionListener(this);
		menuitemAddDish.addActionListener(this);
		menuitemModifyDish.addActionListener(this);
//		menuitemSpecial.addActionListener(this);
//		menuitemNewDish.addActionListener(this);
		menuitemChangePic.addActionListener(this);
		menuitemChangePrice.addActionListener(this);
		menuitemChangePromotion.addActionListener(this);
		menuitemSoldout.addActionListener(this);
		menuitemDeleteC1.addActionListener(this);
		menuitemDeleteC2.addActionListener(this);
		menuitemDeleteDish.addActionListener(this);
		
		menuTree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				//show node info is done via valueChanged function, not here
				if (SwingUtilities.isRightMouseButton(e)){
					int row = menuTree.getClosestRowForLocation(e.getX(), e.getY());
			        menuTree.setSelectionRow(row);
			        MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
					if (node == null)
						return;
					if (node.toString().equals("root")){
						popupmenuRoot.show(e.getComponent(), e.getX(), e.getY());
					} else if (node.getUserObject() instanceof Category1){
						popupmenuC1.show(e.getComponent(), e.getX(), e.getY());
					} else if (node.getUserObject() instanceof Category2){
						popupmenuC2.show(e.getComponent(), e.getX(), e.getY());
					} else if (node.getUserObject() instanceof Dish){
						if (((Dish)node.getUserObject()).isPromotion()){
							menuitemChangePromotion.setText("Cancel Promotion");
						} else {
							menuitemChangePromotion.setText("Set Promotion");
						}
						popupmenuDish.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
	}
	
	private void buildTree(MenuTreeNode root) {
		if (category1s != null){
			for (int i = 0; i < category1s.size(); i++) {
				Category1 c1 = category1s.get(i);
				MenuTreeNode c1node = new MenuTreeNode(c1);
				root.add(c1node);
				
				if (c1.getCategory2s() != null){
					for (int j = 0; j < c1.getCategory2s().size(); j++) {
						Category2 c2 = c1.getCategory2s().get(j);
						MenuTreeNode c2node = new MenuTreeNode(c2);
						c1node.add(c2node);
						if (c2.getDishes() != null){
							for (int k = 0; k < c2.getDishes().size(); k++) {
								Dish dish = c2.getDishes().get(k);
								MenuTreeNode dishnode = new MenuTreeNode(dish);
								c2node.add(dishnode);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.getUserObject() instanceof Category1){
			tabPane.setSelectedIndex(0);
			pCategory1.setObjectValue((Category1)node.getUserObject());
		} else if (node.getUserObject() instanceof Category2){
			tabPane.setSelectedIndex(1);
			pCategory2.setObjectValue((Category2)node.getUserObject());
		} else if (node.getUserObject() instanceof Dish){
			tabPane.setSelectedIndex(2);
			pDish.setObjectValue((Dish)node.getUserObject());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuitemAddC1){
			Category1Panel p = new Category1Panel(this);
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MenuMgmtPanel.AddCategory1"), 300, 300);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemRefreshTree){
			mainFrame.reloadListCategory1s();
			this.category1s = mainFrame.getListCategory1s();
			MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
			root.removeAllChildren();
			buildTree(root);
			menuTree.updateUI();
		} else if (e.getSource() == menuitemModifyC1){
			Category1Panel p = new Category1Panel(this);
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			p.setObjectValue((Category1)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MenuMgmtPanel.ModifyCategory1"), 300, 300);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemAddC2){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			Category2Panel p = new Category2Panel(this, (Category1)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MenuMgmtPanel.AddCategory2"), 400, 500);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemModifyC2){
			Category2Panel p = new Category2Panel(this);
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			p.setObjectValue((Category2)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MenuMgmtPanel.ModifyCategory2"), 400, 500);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemAddDish){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			DishPanel p = new DishPanel(this, (Category2)node.getUserObject());
			p.showPicturePanel(false);
			CommonDialog dlg = new CommonDialog(mainFrame, p, Messages.getString("MenuMgmtPanel.AddDish"), 1000, 720);
			dlg.setVisible(true);
		} else if (e.getSource() == menuitemModifyDish){
			DishPanel p = new DishPanel(this);
			p.showPicturePanel(false);
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			p.setObjectValue((Dish)node.getUserObject());
			CommonDialog dlg = new CommonDialog(mainFrame, p, "Modify Dish", 1000, 720);
			dlg.setVisible(true);
		} 
//		else if (e.getSource() == menuitemSpecial){
//			
//		} else if (e.getSource() == menuitemNewDish){
//			
//		} 
		else if (e.getSource() == menuitemChangePic){
			updateDishPicture((MenuTreeNode) menuTree.getLastSelectedPathComponent());
		} else if (e.getSource() == menuitemChangePrice){
			
		} else if (e.getSource() == menuitemSoldout){
			
		} else if (e.getSource() == menuitemChangePromotion){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			doChangePromotion(node);
		} else if (e.getSource() == menuitemDeleteC1){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			onDeleteC1(node);
		} else if (e.getSource() == menuitemDeleteC2){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			onDeleteC2(node);
		} else if (e.getSource() == menuitemDeleteDish){
			MenuTreeNode node = (MenuTreeNode) menuTree.getLastSelectedPathComponent();
			onDeleteDish(node);
		} 
	}
	
	private void doChangePromotion(MenuTreeNode node){
		String operation = "cancel";
		Dish dish = (Dish)node.getUserObject();
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("dishId", dish.getId()+"");
		String url = "menu/changedishpromotion";
		if (!dish.isPromotion()){
			operation = "set";
			NumberInputDialog numdlg = new NumberInputDialog(mainFrame, "Input", Messages.getString("MenuMgmtPanel.PromotionPrice"), true);
			numdlg.setVisible(true);
			if (!numdlg.isConfirm)
				return;
			params.put("promotionPrice", numdlg.inputDouble+"");
			url = "menu/changedishpromotion";
		} else {
			url = "menu/canceldishpromotion";
			if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "Do you want to cancel the promotion?", "Confirm", JOptionPane.YES_NO_OPTION)){
				return;
			}
		}
		
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for "+operation+" dish promotion. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for "+operation+" dish promotion. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while "+operation+" dish promotion. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while "+operation+" dish promotion. URL = " + url + ", response = "+response);
			return;
		}
		JOptionPane.showMessageDialog(mainFrame, operation + " dish promotion "+dish.getFirstLanguageName()+" successfully");
		//修改node的显示样式, 替换mainframe中存储的对象
		node.setUserObject(result.data);
		menuTree.updateUI();
		mainFrame.reloadListCategory1s();
		this.valueChanged(null);//refresh the property panel value
	}
	
	private void updateDishPicture(MenuTreeNode node){
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg, *.jpeg, *.png, *.bmp", "jpg", "jpeg", "png", "bmp");
		JFileChooser fd = new JFileChooser(); 
		fd.setFileFilter(filter);
		fd.showDialog(this, "upload");
		File file = fd.getSelectedFile();
		if (file == null){
			return;
		}
		Dish dish = (Dish)node.getUserObject();
		String url = MainFrame.SERVER_URL + "menu/changedishpicture";
		FileBody bin = new FileBody(file);
        StringBody dishid = new StringBody(dish.getId()+"", ContentType.TEXT_PLAIN);
        StringBody userid = new StringBody(mainFrame.getLoginUser().getId()+"", ContentType.TEXT_PLAIN);
        HashMap<String, ContentBody> params = new HashMap<>();
        params.put("picture", bin);
        params.put("id", dishid);
        params.put("userId", userid);
        String response = HttpUtil.getJSONObjectByUploadFile(url, params);
        if (response == null){
			logger.error("get null from server for change dish picture. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for change dish picture. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while change dish picture. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while change dish picture. URL = " + url + ", response = "+response);
			return;
		}
		JOptionPane.showMessageDialog(mainFrame, "Update dish picture of " + dish.getFirstLanguageName() + " successfully");
		result.data.setCategory2(dish.getCategory2());
		node.setUserObject(result.data);
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * if root node is null, then insert position = 0
	 * if cannot find any node which sequence more than this one, then insert into the last position
	 * @param c1
	 */
	public void insertNode(Category1 c1){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		int count = root.getChildCount();
		MenuTreeNode newnode = new MenuTreeNode(c1);
		if(count == 0){
			root.insert(newnode, 0);
		} else {
			for (int i = 0; i < count; i++) {
				MenuTreeNode nodei = (MenuTreeNode)root.getChildAt(i);
				if (((Category1)nodei.getUserObject()).getSequence() > c1.getSequence()){
					root.insert(newnode, i);
					break;//must break, otherwise it will be inserted again for (i == count - 1)
				}
				if (i == count - 1){
					root.insert(newnode, count);
				}
			}
		}
		//refresh UI
		menuTree.updateUI();
		
		//refresh local data
		mainFrame.reloadListCategory1s();
		pCategory2.refreshCategory1List();
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * find the parent category1 node fist,
	 * if parent node is null children, then insert position = 0
	 * if cannot find any node which sequence more than this one, then insert into the last position
	 * @param c2
	 */
	public void insertNode(Category2 c2){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		TreePath path = this.findPath(root, c2.getCategory1());
		MenuTreeNode parentnode = (MenuTreeNode)path.getLastPathComponent();
		int count = parentnode.getChildCount();
		MenuTreeNode newnode = new MenuTreeNode(c2);
		if (count == 0){
			parentnode.insert(newnode, 0);
		} else {
			for (int i = 0; i < count; i++) {
				MenuTreeNode nodei = (MenuTreeNode)parentnode.getChildAt(i);
				if(((Category2)nodei.getUserObject()).getSequence() > c2.getSequence()){
					parentnode.insert(newnode, i);
					break;//must break, otherwise it will be inserted again for (i == count - 1)
				}
				if (i == count - 1){
					parentnode.insert(newnode, count);
				}
			}
		}
		
		//refresh UI
		menuTree.updateUI();
		
		//refresh local data
		mainFrame.reloadListCategory1s();
		pDish.refreshCategory2List();
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * find the parent category2 node fist,
	 * if parent node is null children, then insert position = 0
	 * if cannot find any node which sequence more than this one, then insert into the last position
	 * @param dish
	 */
	public void insertNode(Dish dish){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		TreePath path = findPath(root, dish.getCategory2());
		MenuTreeNode parentnode = (MenuTreeNode)path.getLastPathComponent();
		int count = parentnode.getChildCount();
		MenuTreeNode newnode = new MenuTreeNode(dish);
		if (count == 0){
			parentnode.insert(newnode, 0);
		} else {
			for (int i = 0; i < count; i++) {
				MenuTreeNode nodei = (MenuTreeNode)parentnode.getChildAt(i);
				if (((Dish)nodei.getUserObject()).getSequence() > dish.getSequence()){
					parentnode.insert(newnode, i);
					break;//must break, otherwise it will be inserted again for (i == count - 1)
				}
				if (i == count -1){
					parentnode.insert(newnode, count);
				}
			}
		}
		
		//refresh UI
		menuTree.updateUI();
		//refresh local data
		mainFrame.reloadListCategory1s();
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * find the node then reset its userobject
	 * if sequence changed, then delete it from parent, and insert again
	 * @param c1
	 * @param origin
	 */
	public void updateNode(Category1 c1, Category1 origin){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		TreePath path = findPath(root, c1);
		MenuTreeNode node = (MenuTreeNode)path.getLastPathComponent();
		node.setUserObject(c1);
		((DefaultTreeModel)menuTree.getModel()).reload(node);//without reload, JTree can truncate the name as (...) if it is longer than before
		if (c1.getSequence() != origin.getSequence()){
			root.remove(node);
			int count = root.getChildCount();
			if (count == 0){
				root.insert(node, 0);
			} else {
				for (int i = 0; i < count; i++) {
					MenuTreeNode nodei = (MenuTreeNode)root.getChildAt(i);
					if (((Category1)nodei.getUserObject()).getSequence() > c1.getSequence()){
						root.insert(node, i);
						break;//must break, otherwise it will be inserted again for (i == count - 1)
					}
					if (i == count - 1){
						root.insert(node, count);
					}
				}
			}
		}
		
		// refresh UI
		menuTree.updateUI();
		
		// refresh local data
		mainFrame.reloadListCategory1s();
		pCategory2.refreshCategory1List();
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * find the node and reset the userobject;
	 * if sequence/parent changed, then delete it from parent node and then insert again
	 * @param c2
	 * @param origin
	 */
	public void updateNode(Category2 c2, Category2 origin){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		TreePath path = findPath(root, c2);
		MenuTreeNode node = (MenuTreeNode)path.getLastPathComponent();
		node.setUserObject(c2);
		((DefaultTreeModel)menuTree.getModel()).reload(node);//without reload, JTree can truncate the name as (...) if it is longer than before
		if (c2.getSequence() != origin.getSequence() || c2.getCategory1().getId() != origin.getCategory1().getId()){
			((MenuTreeNode)node.getParent()).remove(node);
			path = findPath(root, c2.getCategory1());
			MenuTreeNode parentnode = (MenuTreeNode)path.getLastPathComponent();
			int count = parentnode.getChildCount();
			if (count == 0){
				parentnode.insert(node,  0);
			} else {
				for (int i = 0; i < count; i++) {
					MenuTreeNode nodei = (MenuTreeNode)parentnode.getChildAt(i);
					if (((Category2)nodei.getUserObject()).getSequence() > c2.getSequence()){
						parentnode.insert(node, i);
						break;//must break, otherwise it will be inserted again for (i == count - 1)
					}
					if (i == count - 1){
						parentnode.insert(node, count);
					}
				}
			}
		}
		
		// refresh UI
		menuTree.updateUI();
		// refresh local data
		mainFrame.reloadListCategory1s();
		pDish.refreshCategory2List();
		this.valueChanged(null);//refresh the property panel value
	}
	
	/**
	 * find the node and reset the userobject;
	 * if sequence/parent changed, then delete it from parent node and then insert again
	 * @param dish
	 * @param origin
	 */
	public void updateNode(Dish dish, Dish origin){
		MenuTreeNode root = (MenuTreeNode)menuTree.getModel().getRoot();
		TreePath path = findPath(root, dish);
		MenuTreeNode node = (MenuTreeNode)path.getLastPathComponent();
		node.setUserObject(dish);
		((DefaultTreeModel)menuTree.getModel()).reload(node);//without reload, JTree can truncate the name as (...) if it is longer than before
		if (dish.getSequence() != origin.getSequence() || dish.getCategory2().getId() != origin.getCategory2().getId()){
			((MenuTreeNode)node.getParent()).remove(node);
			path = findPath(root, dish.getCategory2());
			MenuTreeNode parentnode = (MenuTreeNode)path.getLastPathComponent();
			int count = parentnode.getChildCount();
			if (count == 0){
				parentnode.insert(node, 0);
			} else {
				for (int i = 0; i < count; i++) {
					MenuTreeNode nodei = (MenuTreeNode)parentnode.getChildAt(i);
					if (((Dish)nodei.getUserObject()).getSequence() > dish.getSequence()){
						parentnode.insert(node, i);
						break;//must break, otherwise it will be inserted again for (i == count - 1)
					}
					if (i == count - 1){
						parentnode.insert(node, count);
					}
				}
			}
		}
		this.valueChanged(null);
		// refresh UI
		menuTree.updateUI();
		// refresh local data
		mainFrame.reloadListCategory1s();
		this.valueChanged(null);//refresh the property panel value
	}
	
	private void onDeleteC1(MenuTreeNode node){
		if (menuTree.getModel().getChildCount(node) > 0){
			JOptionPane.showMessageDialog(this, "There are sub menu in this node, please delete them first.");
			return;
		}
		Category1 c1 = (Category1)node.getUserObject();
		if (JOptionPane.showConfirmDialog(this, "Do you want to delete this node : "+ c1.getFirstLanguageName(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", c1.getId()+"");
		String url = "menu/delete_category1";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete category1. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete category1. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<HashMap<String, String>> result = gson.fromJson(response, new TypeToken<HttpResult<HashMap<String, String>>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete category1. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while delete category1. URL = " + url + ", response = "+response);
			return;
		}
		JOptionPane.showMessageDialog(mainFrame, "Delete category"+c1.getFirstLanguageName()+" successfully");
		((DefaultTreeModel)menuTree.getModel()).removeNodeFromParent(node);
		// refresh local data
		mainFrame.reloadListCategory1s();
		pCategory2.refreshCategory1List();
	}

	private void onDeleteC2(MenuTreeNode node){
		if (menuTree.getModel().getChildCount(node) > 0){
			JOptionPane.showMessageDialog(this, "There are sub menu in this node, please delete them first.");
			return;
		}
		Category2 c2 = (Category2)node.getUserObject();
		if (JOptionPane.showConfirmDialog(this, "Do you want to delete this node : "+ c2.getFirstLanguageName(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", c2.getId()+"");
		String url = "menu/delete_category2";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete category2. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete category2. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<HashMap<String, String>> result = gson.fromJson(response, new TypeToken<HttpResult<HashMap<String, String>>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete category2. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while delete category2. URL = " + url + ", response = "+response);
			return;
		}
		JOptionPane.showMessageDialog(mainFrame, "Delete category " + c2.getFirstLanguageName() + " successfully");
		((DefaultTreeModel)menuTree.getModel()).removeNodeFromParent(node);
		// refresh local data
		mainFrame.reloadListCategory1s();
		pDish.refreshCategory2List();
	}

	private void onDeleteDish(MenuTreeNode node){
		Dish dish = (Dish)node.getUserObject();
		if (JOptionPane.showConfirmDialog(this, "Do you want to delete this node : "+ dish.getFirstLanguageName(), "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
			return;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("id", dish.getId()+"");
		String url = "menu/delete_dish";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for delete dish. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for delete dish. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<HashMap<String, String>> result = gson.fromJson(response, new TypeToken<HttpResult<HashMap<String, String>>>(){}.getType());
		if (!result.success){
			logger.error("return false while delete dish. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while delete dish. URL = " + url + ", response = "+response);
			return;
		}
		JOptionPane.showMessageDialog(mainFrame, "Delete dish " + dish.getFirstLanguageName() + " successfully");
		((DefaultTreeModel)menuTree.getModel()).removeNodeFromParent(node);
		// refresh local data
		mainFrame.reloadListCategory1s();
	}
	
	public ArrayList<Category1> getCategory1s() {
		return category1s;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	private TreePath findPath(MenuTreeNode root, Object o) {
	    @SuppressWarnings("unchecked")
	    Enumeration<MenuTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			MenuTreeNode node = e.nextElement();
			if (node.getUserObject().getClass().getName().equals(o.getClass().getName())) {
				// since Category1, Category2, Dish use id to do equal, so here can equal them directly
				if (o.equals(node.getUserObject()))
					return new TreePath(node.getPath());
			}
		}
	    return null;
	}
}
