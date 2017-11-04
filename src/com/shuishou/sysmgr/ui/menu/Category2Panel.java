package com.shuishou.sysmgr.ui.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;

public class Category2Panel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(Category2Panel.class.getName());
	private MenuMgmtPanel parent;
	private JTextField tfChineseName= new JTextField(155);
	private JTextField tfEnglishName= new JTextField(155);
	private JTextField tfDisplaySeq= new JTextField(155);
	private JComboBox<Category1> cbCategory1 = new JComboBox();
	private JComboBox<Printer> cbPrinter = new JComboBox();
	
	private Category2 c2;
	public Category2Panel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
		initData();
	}
	
	private void initUI(){
		JLabel lbChineseName = new JLabel("Chinese Name");
		JLabel lbEnglishName = new JLabel("English Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbCategory1 = new JLabel("Category1");
		JLabel lbPrinter = new JLabel("Printer");
		this.setLayout(new GridBagLayout());
		add(lbChineseName, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfChineseName, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbEnglishName, new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfEnglishName, new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, 	new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, 	new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbCategory1, 	new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(cbCategory1, 	new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbPrinter, 		new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(cbPrinter, 		new GridBagConstraints(1, 4, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), new GridBagConstraints(0, 5, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
//		tfChineseName.setEditable(false);
//		tfEnglishName.setEditable(false);
//		cbCategory1.setEditable(false);
//		cbPrinter.setEditable(false);
//		tfDisplaySeq.setEditable(false);
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfChineseName.setMinimumSize(new Dimension(180,25));
		tfEnglishName.setMinimumSize(new Dimension(180,25));
		cbCategory1.setMinimumSize(new Dimension(180,25));
		cbPrinter.setMinimumSize(new Dimension(180,25));
		
//		cbCategory1.setRenderer(new Category1ListRender());
//		cbPrinter.setRenderer(new PrinterListRender());
		
		tfDisplaySeq.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9'))) {
					getToolkit().beep();
					e.consume();
				} 
			}
		});
	}

	private void initData(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		ArrayList<Printer> listPrinter = parent.getMainFrame().getListPrinters();
		cbPrinter.removeAllItems();
		cbCategory1.removeAllItems();
		if (listPrinter != null){
			for(Printer p : listPrinter){
				cbPrinter.addItem(p);
			}
		}
		for(Category1 c1 : listCategory1){
			cbCategory1.addItem(c1);
		}
		cbPrinter.setSelectedIndex(-1);
	}
	
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("chineseName", tfChineseName.getText());
		params.put("englishName", tfEnglishName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("printerId", ((Printer)cbPrinter.getSelectedItem()).getId()+"");
		params.put("category1Id", ((Category1)cbCategory1.getSelectedItem()).getId() + "");
		String url = "menu/add_category2";
		if (c2 != null){
			url = "menu/update_category2";
			params.put("id", c2.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update category2. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update category2. URL = " + url);
			return false;
		}
		Gson gson = new Gson();
		HttpResult<Category2> result = gson.fromJson(response, new TypeToken<HttpResult<Category2>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update category2. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while add/update category2. URL = " + url + ", response = "+response);
			return false;
		}
		result.data.setCategory1((Category1)cbCategory1.getSelectedItem());
		if (c2 == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, c2);
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
		if (cbPrinter.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Printer");
			return false;
		}
		if (cbCategory1.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this, "Please input Cagetory1");
			return false;
		}
		return true;
	}
	
	public void setObjectValue(Category2 c2){
		this.c2 = c2;
		tfChineseName.setText(c2.getChineseName());
		tfEnglishName.setText(c2.getEnglishName());
		tfDisplaySeq.setText(c2.getSequence()+"");
		cbCategory1.setSelectedItem(c2.getCategory1());
		if (c2.getPrinter() != null){
			cbPrinter.setSelectedItem(c2.getPrinter());
		} else {
			cbPrinter.setSelectedIndex(-1);
		}
	}
	
	public void refreshCategory1List(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory1.removeAllItems();
		for(Category1 c1 : listCategory1){
			cbCategory1.addItem(c1);
		}
	}
	
	public void refreshPrinterList(){
		ArrayList<Printer> listPrinter = parent.getMainFrame().getListPrinters();
		cbPrinter.removeAllItems();
		if (listPrinter != null){
			for(Printer p : listPrinter){
				cbPrinter.addItem(p);
			}
		}
		cbPrinter.setSelectedIndex(-1);
	}

	class PrinterListRender extends JLabel implements ListCellRenderer{
		
		public PrinterListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((Printer)value).getName());
			return this;
		}
		
	}
	
	class Category1ListRender extends JLabel implements ListCellRenderer{
		
		public Category1ListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			setText(((Category1)value).getChineseName());
			return this;
		}
		
	}
}
