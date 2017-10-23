package com.shuishou.sysmgr.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.DishChoosePopinfo;
import com.shuishou.sysmgr.beans.DishChooseSubitem;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;

public class DishPanel extends JPanel implements CommonDialogOperatorIFC, ActionListener{
	private final Logger logger = Logger.getLogger(DishPanel.class.getName());
	private final static String CARDLAYOUT_COMMON = "COMMON";
	private final static String CARDLAYOUT_POPUPMESSAGE = "POPUPMESSAGE";
	private final static String CARDLAYOUT_CHOOSESUBITEM = "CHOOSESUBITEM";
	private MenuMgmtPanel parent;
	private JTextField tfChineseName= new JTextField(155);
	private JTextField tfEnglishName= new JTextField(155);
	private JTextField tfDisplaySeq= new JTextField(155);
	private JTextField tfPrice= new JTextField(155);
	private JTextField tfAbbre= new JTextField(155);
	private JComboBox cbCategory2 = new JComboBox();
	private JComboBox cbHotLevel = new JComboBox();
	private JCheckBox cbAutoMergeWhileChoose = new JCheckBox(Messages.getString("DishPanel.AutoMergeWhileChoose"));
	private JLabel lbPicture;
	private JPanel pPicture;
	private JPanel pChooseModeCards;
	private JRadioButton rbChooseModeCommon = new JRadioButton(Messages.getString("DishPanel.Common"));
	private JRadioButton rbChooseModePopmsg = new JRadioButton(Messages.getString("DishPanel.PopupMessage"));
	private JRadioButton rbChooseModeSubitem = new JRadioButton(Messages.getString("DishPanel.ForceChooseSubitem"));
	private JRadioButton rbChooseAfterPopmsg = new JRadioButton(Messages.getString("DishPanel.ChooseDishAfterMessage"));
	private JRadioButton rbNoChooseAfterPopmsg = new JRadioButton(Messages.getString("DishPanel.NotChooseDishOnlyShowMessage"));
	private JRadioButton rbPurchaseTypeUnit = new JRadioButton(Messages.getString("DishPanel.PurchaseTypeUnit"));
	private JRadioButton rbPurchaseTypeWeight = new JRadioButton(Messages.getString("DishPanel.PurchaseTypeWeight"));
	private JTextField tfPopmsg_cn= new JTextField(155);
	private JTextField tfPopmsg_en= new JTextField(155);
	private JTextField tfSubitemAmount = new JTextField(155);
	private JList<DishChooseSubitem> listSubitem = new JList<>();
	private DefaultListModel<DishChooseSubitem> subitemModel = new DefaultListModel<>();
	private JTextField tfSubitem_cn= new JTextField(155);
	private JTextField tfSubitem_en= new JTextField(155);
	private JButton btnAddSubitem = new JButton("add");
	private JButton btnDeleteSubitem = new JButton("delete");
	private Dish dish;
	public DishPanel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
		initData();
	}
	
	private void initData(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory2.removeAllItems();
		for(Category1 c1 : listCategory1){
			for(Category2 c2 : c1.getCategory2s()){
				cbCategory2.addItem(c2);
			}
		}
	}
	
	private void initUI(){
		JLabel lbChineseName = new JLabel("Chinese Name");
		JLabel lbEnglishName = new JLabel("English Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbCategory2 = new JLabel("Category2");
		JLabel lbPrice = new JLabel("Price");
		JLabel lbHotLevel = new JLabel("Hot Level");
		JLabel lbAbbre = new JLabel("Abbreviation");
		cbCategory2.setRenderer(new Category2ListRender());
		listSubitem.setModel(subitemModel);
		listSubitem.setCellRenderer(new SubitemListRenderer());
		listSubitem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		cbAutoMergeWhileChoose.setSelected(true);
		
		ButtonGroup bgPurchaseType = new ButtonGroup();
		bgPurchaseType.add(rbPurchaseTypeUnit);
		bgPurchaseType.add(rbPurchaseTypeWeight);
		JPanel pPurchaseType = new JPanel();
		pPurchaseType.add(rbPurchaseTypeUnit);
		pPurchaseType.add(rbPurchaseTypeWeight);
		rbPurchaseTypeUnit.setSelected(true);
		pPurchaseType.setBorder(BorderFactory.createTitledBorder(Messages.getString("DishPanel.PurchaseType")));
		
		JPanel pBaseProperty = new JPanel(new GridBagLayout());
		pBaseProperty.setBorder(BorderFactory.createTitledBorder("Base Properties"));
		int row = 0;
		pBaseProperty.add(lbChineseName, new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfChineseName, new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbEnglishName, new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfEnglishName, new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbDisplaySeq,  new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfDisplaySeq,  new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbPrice, 		 new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfPrice, 		 new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbHotLevel, 	new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(cbHotLevel, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbAbbre, 		new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(tfAbbre, 		new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(lbCategory2, 	new GridBagConstraints(0, row, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pBaseProperty.add(cbCategory2, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(cbAutoMergeWhileChoose, 	new GridBagConstraints(1, row, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(pPurchaseType, new GridBagConstraints(0, row, 2, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));
		row++;
		pBaseProperty.add(new JPanel(), new GridBagConstraints(0, row, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		
		
		lbPicture = new JLabel();
		Dimension dPicture = lbPicture.getPreferredSize();
		dPicture.width = 120;
		dPicture.height = 120;
		lbPicture.setPreferredSize(dPicture);
		pPicture = new JPanel(new BorderLayout());
		pPicture.add(lbPicture, BorderLayout.CENTER);
		pPicture.setBorder(BorderFactory.createTitledBorder("Picture"));
		
		rbChooseAfterPopmsg.setSelected(true);
		ButtonGroup bgPopmsg = new ButtonGroup();
		bgPopmsg.add(rbChooseAfterPopmsg);
		bgPopmsg.add(rbNoChooseAfterPopmsg);
		JPanel pChooseModePopmsg = new JPanel(new GridBagLayout());
		pChooseModePopmsg.add(rbChooseAfterPopmsg,  new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(rbNoChooseAfterPopmsg,new GridBagConstraints(1, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(new JLabel("Chinese"),new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(tfPopmsg_cn,			new GridBagConstraints(1, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(new JLabel("English"),new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModePopmsg.add(tfPopmsg_en,			new GridBagConstraints(1, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		
		JPanel pChooseModeSubitem = new JPanel(new GridBagLayout());
		pChooseModeSubitem.add(new JLabel("Amount"), 	new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(tfSubitemAmount, 		new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(listSubitem, 			new GridBagConstraints(2, 0, 1, 5,1,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,10,0,0), 0, 0));
		pChooseModeSubitem.add(new JLabel("Chinese"), 	new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(tfSubitem_cn, 			new GridBagConstraints(1, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(new JLabel("English"), 	new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(tfSubitem_en, 			new GridBagConstraints(1, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(btnAddSubitem, 			new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		pChooseModeSubitem.add(btnDeleteSubitem, 		new GridBagConstraints(1, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
//		JPanel pChooseModeSubitem = new JPanel(new BorderLayout());
//		pChooseModeSubitem.add(listSubitem, BorderLayout.CENTER);
		
		rbChooseModeCommon.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbChooseModeCommon);
		bg.add(rbChooseModePopmsg);
		bg.add(rbChooseModeSubitem);
		JPanel pChooseModeRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pChooseModeRadio.add(rbChooseModeCommon);
		pChooseModeRadio.add(rbChooseModePopmsg);
		pChooseModeRadio.add(rbChooseModeSubitem);
		pChooseModeCards = new JPanel(new CardLayout());
		pChooseModeCards.add(new JPanel(), CARDLAYOUT_COMMON);
		pChooseModeCards.add(pChooseModePopmsg, CARDLAYOUT_POPUPMESSAGE);
		pChooseModeCards.add(pChooseModeSubitem, CARDLAYOUT_CHOOSESUBITEM);
		JPanel pChooseMode = new JPanel(new BorderLayout());
		pChooseMode.add(pChooseModeRadio, BorderLayout.NORTH);
		pChooseMode.add(pChooseModeCards, BorderLayout.CENTER);
		pChooseMode.setBorder(BorderFactory.createTitledBorder(Messages.getString("DishPanel.ChooseMode")));
		
		this.setLayout(new GridBagLayout());
		add(pBaseProperty, 	new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(10,0,0,0), 0, 0));
		add(pPicture, 		new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		add(pChooseMode, 	new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), 	new GridBagConstraints(0, 2, 2, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,0,0,0), 0, 0));
		

		
//		tfChineseName.setEditable(false);
//		tfEnglishName.setEditable(false);
//		tfDisplaySeq.setEditable(false);
//		tfPrice.setEditable(false);
//		cbHotLevel.setEditable(false);
//		tfAbbre.setEditable(false);
//		cbCategory2.setEditable(false);
		tfChineseName.setMinimumSize(new Dimension(180,25));
		tfEnglishName.setMinimumSize(new Dimension(180,25));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfPrice.setMinimumSize(new Dimension(180,25));
		cbHotLevel.setMinimumSize(new Dimension(180,25));
		tfAbbre.setMinimumSize(new Dimension(180,25));
		cbCategory2.setMinimumSize(new Dimension(180,25));
		tfSubitem_cn.setMinimumSize(new Dimension(180,25));
		tfSubitem_en.setMinimumSize(new Dimension(180,25));
		tfPopmsg_cn.setMinimumSize(new Dimension(180,25));
		tfPopmsg_en.setMinimumSize(new Dimension(180,25));
		tfSubitemAmount.setMinimumSize(new Dimension(180,25));
		
		cbHotLevel.addItem(0);
		cbHotLevel.addItem(1);
		cbHotLevel.addItem(2);
		cbHotLevel.addItem(3);
		
		rbChooseModeCommon.addActionListener(this);
		rbChooseModePopmsg.addActionListener(this);
		rbChooseModeSubitem.addActionListener(this);
		btnAddSubitem.addActionListener(this);
		btnDeleteSubitem.addActionListener(this);
		tfDisplaySeq.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9'))) {
					getToolkit().beep();
					e.consume();
				} 
			}
		});
		tfPrice.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == '.'))) {
					getToolkit().beep();
					e.consume();
				} 
				if (c == '.'){
					if (tfPrice.getText() != null && tfPrice.getText().indexOf(".") >= 0){
						getToolkit().beep();
						e.consume();
					}
				}
				
			}
		});
	}

	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		Gson gson = new Gson();
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("chineseName", tfChineseName.getText());
		params.put("englishName", tfEnglishName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("price", tfPrice.getText());
		params.put("hotLevel", cbHotLevel.getSelectedItem().toString());
		params.put("abbreviation", tfAbbre.getText()); 
		params.put("category2Id", ((Category2)cbCategory2.getSelectedItem()).getId()+"");
		params.put("subitemAmount", tfSubitemAmount.getText());
		params.put("autoMerge", cbAutoMergeWhileChoose.isSelected()+"");
		if (this.rbPurchaseTypeUnit.isSelected())
			params.put("purchaseType", ConstantValue.DISH_PURCHASETYPE_UNIT+"");
		else if (this.rbPurchaseTypeWeight.isSelected())
			params.put("purchaseType", ConstantValue.DISH_PURCHASETYPE_WEIGHT+"");
		if (rbChooseModeCommon.isSelected()){
			params.put("chooseMode", ConstantValue.DISH_CHOOSEMODE_DEFAULT+"");
		} else if (rbChooseModePopmsg.isSelected()){
			if (rbChooseAfterPopmsg.isSelected()){
				params.put("chooseMode", ConstantValue.DISH_CHOOSEMODE_POPINFOCHOOSE+"");
			} else if (rbNoChooseAfterPopmsg.isSelected()){
				params.put("chooseMode", ConstantValue.DISH_CHOOSEMODE_POPINFOQUIT+"");
			}
			params.put("dishChoosePopinfo_cn", tfPopmsg_cn.getText());
			params.put("dishChoosePopinfo_en", tfPopmsg_en.getText());
		} else if (rbChooseModeSubitem.isSelected()){
			params.put("chooseMode", ConstantValue.DISH_CHOOSEMODE_SUBITEM+"");
			ArrayList<DishChooseSubitem> subitems = new ArrayList<>();
			for (int i = 0; i < subitemModel.getSize(); i++) {
				subitems.add(subitemModel.get(i));
			}
			params.put("dishChooseSubitem", gson.toJson(subitems));
		}
		
		String url = "menu/add_dish";
		if (dish != null){
			url = "menu/update_dish";
			params.put("id", dish.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update dish. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update dish. URL = " + url);
			return false;
		}
		
		HttpResult<Dish> result = gson.fromJson(response, new TypeToken<HttpResult<Dish>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update dish. URL = " + url);
			JOptionPane.showMessageDialog(this, "return false while add/update dish. URL = " + url);
			return false;
		}
		//the category2 info is null after get from server
		result.data.setCategory2((Category2)cbCategory2.getSelectedItem());
		if (dish == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, dish);
		}
		return true;
	}
	
	private boolean doCheckInput(){
		if (tfChineseName.getText() == null || tfChineseName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Chinese Name");
			return false;
		}
		if (tfEnglishName.getText() == null || tfEnglishName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input English Name");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		if (tfPrice.getText() == null || tfPrice.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Price");
			return false;
		}
		if (cbHotLevel.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Hot Level");
			return false;
		}
		if (tfAbbre.getText() == null || tfAbbre.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Abbreviation");
			return false;
		}
		if (cbCategory2.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Category2");
			return false;
		}
		if (rbChooseModePopmsg.isSelected()){
			if (tfPopmsg_cn.getText() == null || tfPopmsg_cn.getText().length() == 0
					|| tfPopmsg_en.getText() == null || tfPopmsg_en.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "Please input Popup Message in Choose Mode");
				return false;
			}
		}
		if (rbChooseModeSubitem.isSelected()){
			if (tfSubitemAmount.getText() == null || tfSubitemAmount.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "Please input Amount in Choose Mode");
				return false;
			}
			if (subitemModel.getSize() == 0){
				JOptionPane.showMessageDialog(this, "Please input Dish Subitem in Choose Mode");
				return false;
			}
		}
		return true;
	}
	
	public void setObjectValue(Dish dish){
		this.dish = dish;
		tfChineseName.setText(dish.getChineseName());
		tfEnglishName.setText(dish.getEnglishName());
		tfDisplaySeq.setText(dish.getSequence()+"");
		tfPrice.setText(dish.getPrice()+"");
		cbHotLevel.setSelectedIndex(dish.getHotLevel());
		tfAbbre.setText(dish.getAbbreviation());
		cbCategory2.setSelectedItem(dish.getCategory2());
		cbAutoMergeWhileChoose.setSelected(dish.isAutoMergeWhileChoose());
		this.rbPurchaseTypeUnit.setSelected(dish.getPurchaseType() == ConstantValue.DISH_PURCHASETYPE_UNIT);
		this.rbPurchaseTypeWeight.setSelected(dish.getPurchaseType() == ConstantValue.DISH_PURCHASETYPE_WEIGHT);
		if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_DEFAULT){
			rbChooseModeCommon.setSelected(true);
			rbChooseModeCommon.doClick();
		} else if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_POPINFOCHOOSE){
			rbChooseModePopmsg.setSelected(true);
			rbChooseModePopmsg.doClick();
			rbChooseAfterPopmsg.setSelected(true);
			tfPopmsg_cn.setText(dish.getChoosePopInfo().getPopInfoCN());
			tfPopmsg_en.setText(dish.getChoosePopInfo().getPopInfoEN());
		} else if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_POPINFOQUIT){
			rbChooseModePopmsg.setSelected(true);
			rbChooseModePopmsg.doClick();
			rbNoChooseAfterPopmsg.setSelected(true);
			tfPopmsg_cn.setText(dish.getChoosePopInfo().getPopInfoCN());
			tfPopmsg_en.setText(dish.getChoosePopInfo().getPopInfoEN());
		} else if (dish.getChooseMode() == ConstantValue.DISH_CHOOSEMODE_SUBITEM){
			rbChooseModeSubitem.setSelected(true);
			rbChooseModeSubitem.doClick();
			this.tfSubitemAmount.setText(dish.getSubitemAmount()+"");
			if (dish.getChooseSubItems() != null){
				subitemModel.clear();
				for(DishChooseSubitem item : dish.getChooseSubItems()){
					subitemModel.addElement(item);
				}
			}
		}
		//show picture
		if (dish.getPictureName() != null){
			try {
				lbPicture.setIcon(new ImageIcon(ImageIO.read(new URL(MainFrame.SERVER_URL + "../dishimage_big/"+dish.getPictureName()))));
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void refreshCategory2List(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory2.removeAllItems();
		for(Category1 c1 : listCategory1){
			for(Category2 c2 : c1.getCategory2s()){
				cbCategory2.addItem(c2);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rbChooseModeCommon){
			CardLayout cl = (CardLayout)pChooseModeCards.getLayout();
			cl.show(pChooseModeCards, CARDLAYOUT_COMMON);
		} else if (e.getSource() == rbChooseModePopmsg){
			CardLayout cl = (CardLayout)pChooseModeCards.getLayout();
			cl.show(pChooseModeCards, CARDLAYOUT_POPUPMESSAGE);
		} else if (e.getSource() == rbChooseModeSubitem){
			CardLayout cl = (CardLayout)pChooseModeCards.getLayout();
			cl.show(pChooseModeCards, CARDLAYOUT_CHOOSESUBITEM);
		} else if (e.getSource() == btnAddSubitem){
			addSubitemToList();
		} else if (e.getSource() == btnDeleteSubitem){
			if (subitemModel.getSize() == 0)
				return;
			if (listSubitem.getSelectedIndex() < 0)
				return;
			subitemModel.removeElementAt(listSubitem.getSelectedIndex());
		}
	}
	
	private void addSubitemToList(){
		if (tfSubitem_cn.getText() == null || tfSubitem_cn.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input the Chinese");
			return;
		}
		if (tfSubitem_en.getText() == null || tfSubitem_en.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input the English");
			return;
		}
		DishChooseSubitem item = new DishChooseSubitem();
		item.setChineseName(tfSubitem_cn.getText());
		item.setEnglishName(tfSubitem_en.getText());
		subitemModel.addElement(item);
		tfSubitem_cn.setText("");
		tfSubitem_en.setText("");
	}
	
	public void showPicturePanel(boolean b){
		pPicture.setVisible(b);
	}
	
	public void showChooseSubitemButton(boolean b){
		btnAddSubitem.setVisible(b);
		btnDeleteSubitem.setVisible(b);
	}
	
	class Category2ListRender extends JLabel implements ListCellRenderer{
		
		public Category2ListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((Category2)value).getChineseName());
			return this;
		}
		
	}
	
	class SubitemListRenderer extends JPanel implements ListCellRenderer{
		private JLabel lbCN = new JLabel();
		private JLabel lbEN = new JLabel();
		public SubitemListRenderer(){
			this.setLayout(new GridLayout(0,2));
			add(lbCN);
			add(lbEN);
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
			DishChooseSubitem cs = (DishChooseSubitem)value;
			lbCN.setText(cs.getChineseName());
			lbEN.setText(cs.getEnglishName());
			return this;
		}
	}
	
//	class DishPicturePanel extends JPanel{
//		private BufferedImage img;
//		public void showPicture(String url) {
//			try {
//				img = ImageIO.read(new URL(url));
//				
//			} catch (IOException e) {
//				logger.error(e);
//			}
//	    }
//		
//		@Override
//		protected void paintComponent(Graphics g) {
//	        if (img != null){
//	        	g.drawImage(img, 0, 0, null);
//	        }
//	    }
//	}
}
