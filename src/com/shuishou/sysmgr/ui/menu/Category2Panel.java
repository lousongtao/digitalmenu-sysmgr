package com.shuishou.sysmgr.ui.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Category2Printer;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class Category2Panel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(Category2Panel.class.getName());
	private MenuMgmtPanel parent;
	private JTextField tfFirstLanguageName= new JTextField(155);
	private JTextField tfSecondLanguageName= new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private JComboBox<Category1> cbCategory1 = new JComboBox();
	private ArrayList<PrinterPanel> printerPanelList = new ArrayList<>();
	private JPanel pPrinter;
	
//	private JList<PrinterChoosed> listPrinter = new JList<>();
//	private DefaultListModel<PrinterChoosed> modelPrinter = new DefaultListModel<>();
	private Category2 c2;
	private Gson gson = new Gson();
	private Category1 parentCategory1;
	public Category2Panel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
		initData();
	}
	
	public Category2Panel(MenuMgmtPanel parent, Category1 parentCategory1){
		this.parent = parent;
		this.parentCategory1 = parentCategory1;
		initUI();
		initData();
	}
	
	private void initUI(){
		JLabel lbFirstLanguageName = new JLabel("First Language Name");
		JLabel lbSecondLanguageName = new JLabel("Second Language Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		JLabel lbCategory1 = new JLabel("Category1");
		pPrinter = new JPanel(new GridLayout(0,1));
		pPrinter.setBorder(BorderFactory.createTitledBorder("Printer"));
		ArrayList<Printer> listPrinter = parent.getMainFrame().getListPrinters();
		if (listPrinter != null){
			for(Printer p : listPrinter){
				PrinterChoosed pc = new PrinterChoosed(p, false, ConstantValue.CATEGORY2_PRINT_TYPE_SEPARATELY);
				PrinterPanel pp = new PrinterPanel(pc);
				pPrinter.add(pp);
				printerPanelList.add(pp);
			}
		}
		this.setLayout(new GridBagLayout());
		add(lbFirstLanguageName, 	new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfFirstLanguageName, 	new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbSecondLanguageName, 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfSecondLanguageName, 	new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, 			new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, 			new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbCategory1, 			new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(cbCategory1, 			new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(pPrinter,				new GridBagConstraints(0, 4, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), 			new GridBagConstraints(0, 5, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		tfFirstLanguageName.setMinimumSize(new Dimension(180,25));
		tfSecondLanguageName.setMinimumSize(new Dimension(180,25));
		cbCategory1.setMinimumSize(new Dimension(180,25));
		
//		tfDisplaySeq.addKeyListener(new KeyAdapter() {
//			public void keyTyped(KeyEvent e) {
//				char c = e.getKeyChar();
//				if (!((c >= '0') && (c <= '9'))) {
//					getToolkit().beep();
//					e.consume();
//				} 
//			}
//		});
	}

	private void initData(){
		ArrayList<Category1> listCategory1 = parent.getMainFrame().getListCategory1s();
		cbCategory1.removeAllItems();
		for(Category1 c1 : listCategory1){
			cbCategory1.addItem(c1);
		}
		if (parentCategory1 != null){
			cbCategory1.setSelectedItem(parentCategory1);
		}
		
		
	}
	
	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("firstLanguageName", tfFirstLanguageName.getText());
		if (tfSecondLanguageName.getText() != null)
			params.put("secondLanguageName", tfSecondLanguageName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("category1Id", ((Category1)cbCategory1.getSelectedItem()).getId() + "");
		JSONArray ja = new JSONArray();
		for (int i = 0; i< printerPanelList.size(); i++) {
			PrinterPanel pp = printerPanelList.get(i);
			if (pp.isSelected()){
				JSONObject jo = new JSONObject();
				jo.put("printerId", pp.getPrinterChoosed().printer.getId());
				jo.put("printStyle", pp.getPrintStyle());
				ja.put(jo);
			}
		}
		params.put("printers", ja.toString());
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
		HttpResult<Category2> result = gson.fromJson(response, new TypeToken<HttpResult<Category2>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update category2. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
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
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input First Language Name");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		boolean hasPrinter = false;
		for (int i = 0; i < printerPanelList.size(); i++) {
			if (printerPanelList.get(i).isSelected()) {
				hasPrinter = true;
				break;
			}
		}
		if (!hasPrinter){
			JOptionPane.showMessageDialog(this, "Please choose Printer");
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
		tfFirstLanguageName.setText(c2.getFirstLanguageName());
		tfSecondLanguageName.setText(c2.getSecondLanguageName());
		tfDisplaySeq.setText(c2.getSequence()+"");
		cbCategory1.setSelectedItem(c2.getCategory1());
		for (int i = 0; i < printerPanelList.size(); i++) {
			printerPanelList.get(i).setSelect(false);
		}
		List<Category2Printer> cps = c2.getCategory2PrinterList();
		if (cps != null && !cps.isEmpty()){
			for(Category2Printer cp : cps){
				for (int i = 0; i < printerPanelList.size(); i++) {
					if (cp.getPrinter().getId() == printerPanelList.get(i).getPrinterChoosed().printer.getId()) {
						printerPanelList.get(i).setSelect(true);
						printerPanelList.get(i).setPrintStyle(cp.getPrintStyle());
					}
				}
			}
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
		printerPanelList.clear();
		pPrinter.removeAll();
		ArrayList<Printer> listPrinter = parent.getMainFrame().getListPrinters();
		if (listPrinter != null){
			for(Printer p : listPrinter){
				PrinterChoosed pc = new PrinterChoosed(p, false, ConstantValue.CATEGORY2_PRINT_TYPE_SEPARATELY);
				PrinterPanel pp = new PrinterPanel(pc);
				pPrinter.add(pp);
				printerPanelList.add(pp);
			}
		}
	}

	public void setEditable(boolean b){
		tfFirstLanguageName.setEditable(b);
		tfSecondLanguageName.setEditable(b);
		tfDisplaySeq.setEditable(b);
		if (printerPanelList != null){
			for(PrinterPanel pp : printerPanelList){
				pp.setEditable(b);
			}
		}
	}
	
	class Category1ListRender extends JLabel implements ListCellRenderer{
		
		public Category1ListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			setText(((Category1)value).getFirstLanguageName());
			return this;
		}
	}
	
	class PrinterPanel extends JPanel{
		private JCheckBox cbPrinterName = new JCheckBox();
		private JRadioButton rbPrintTogether = new JRadioButton(Messages.getString("Category2Panel.PrintTogether"), false);
		private JRadioButton rbPrintSeparately = new JRadioButton(Messages.getString("Category2Panel.PrintSeparately"), false);
		private PrinterChoosed pc;
		public PrinterPanel(PrinterChoosed pc){
			this.pc = pc;
			cbPrinterName.setText(pc.printer.getName());
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			
			cbPrinterName.setPreferredSize(new Dimension(120, 30));
			cbPrinterName.setMinimumSize(new Dimension(120, 30));
			cbPrinterName.setMaximumSize(new Dimension(120, 30));
			ButtonGroup bgPrintStyle = new ButtonGroup();
			bgPrintStyle.add(rbPrintSeparately);
			bgPrintStyle.add(rbPrintTogether);
			this.add(cbPrinterName);
			this.add(rbPrintSeparately);
			this.add(rbPrintTogether);
			rbPrintSeparately.setVisible(false);
			rbPrintTogether.setVisible(false);
			cbPrinterName.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent e) {
					rbPrintSeparately.setVisible(cbPrinterName.isSelected());
					rbPrintTogether.setVisible(cbPrinterName.isSelected());
				}
				
			});
		}
		
		public void setSelect(boolean b){
			cbPrinterName.setSelected(b);
			rbPrintSeparately.setVisible(b);
			rbPrintTogether.setVisible(b);
		}
		
		public boolean isSelected(){
			return cbPrinterName.isSelected();
		}
		
		public int getPrintStyle(){
			if (rbPrintTogether.isSelected())
				return ConstantValue.CATEGORY2_PRINT_TYPE_TOGETHER;
			return ConstantValue.CATEGORY2_PRINT_TYPE_SEPARATELY;
		}
		
		public void setPrintStyle(int style){
			if (style == ConstantValue.CATEGORY2_PRINT_TYPE_SEPARATELY){
				rbPrintSeparately.setSelected(true);
			} else {
				rbPrintTogether.setSelected(true);
			}
		}
		
		public PrinterChoosed getPrinterChoosed(){
			return pc;
		}
		
		public void setEditable(boolean b){
			cbPrinterName.setEnabled(b);
			rbPrintTogether.setEnabled(b);
			rbPrintSeparately.setEnabled(b);
		}
	}
	
	class PrinterChoosed{
		Printer printer;
		boolean isChoosed = false;
		int printStyle;
		public PrinterChoosed(Printer p, boolean c, int ps){
			printer = p;
			isChoosed = c;
			printStyle = ps;
		}
	}
}
