package com.shuishou.sysmgr.ui.config;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;
import com.shuishou.sysmgr.ui.menu.DishPanel;

public class ConfigsDialog extends JDialog implements ActionListener{
	private final Logger logger = Logger.getLogger(ConfigsDialog.class.getName());
	private JButton btnSaveConfirmCode = new JButton("Save");
	private JButton btnSaveOpenCashdrawerCode = new JButton("Save");
	private JButton btnSaveCancelOrderCode = new JButton("Save");
	private JButton btnSaveClearTableCode = new JButton("Save");
	private JTextField tfOldConfirmCode;
	private NumberTextField tfNewConfirmCode;
	private JTextField tfOldOpenCashdrawerCode;
	private NumberTextField tfNewOpenCashdrawerCode;
	private JTextField tfOldCancelOrderCode;
	private NumberTextField tfNewCancelOrderCode;
	private JTextField tfOldClearTableCode;
	private NumberTextField tfNewClearTableCode;
	private JRadioButton rbLanguageAmount1 = new JRadioButton("1");
	private JRadioButton rbLanguageAmount2 = new JRadioButton("2");
	private JTextField tfFirstLanguageName = new JTextField();
	private JTextField tfSecondLanguageName = new JTextField();
	private JButton btnSaveLanguageSet = new JButton("Save");
	private MainFrame mainFrame;
	public ConfigsDialog(MainFrame mainFrame){
		super(mainFrame, Messages.getString("ConfigsDialog.Config"), true);
		this.mainFrame = mainFrame;
		initUI();
		initData();
	}
	
	private void initUI(){
		
		JLabel lbOldConfirmCode = new JLabel("old code");
		JLabel lbNewConfirmCode = new JLabel("new code");
		tfOldConfirmCode = new JTextField();
		tfNewConfirmCode = new NumberTextField(false);
		JPanel pConfirmCode = new JPanel(new GridBagLayout());
		pConfirmCode.add(lbOldConfirmCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));;
		pConfirmCode.add(tfOldConfirmCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(lbNewConfirmCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(tfNewConfirmCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.add(btnSaveConfirmCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pConfirmCode.setBorder(BorderFactory.createTitledBorder("Confirm Code"));
		
		JLabel lbOldOpenCashdrawerCode = new JLabel("old code");
		JLabel lbNewOpenCashdrawerCode = new JLabel("new code");
		tfOldOpenCashdrawerCode = new JTextField();
		tfNewOpenCashdrawerCode = new NumberTextField(false);
		JPanel pOpenCashdrawerCode = new JPanel(new GridBagLayout());
		pOpenCashdrawerCode.add(lbOldOpenCashdrawerCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfOldOpenCashdrawerCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(lbNewOpenCashdrawerCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfNewOpenCashdrawerCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(btnSaveOpenCashdrawerCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.setBorder(BorderFactory.createTitledBorder("Open Cashdrawer Code"));
		
		JLabel lbOldCancelOrderCode = new JLabel("old code");
		JLabel lbNewCancelOrderCode = new JLabel("new code");
		tfOldCancelOrderCode = new JTextField();
		tfNewCancelOrderCode = new NumberTextField(false);
		JPanel pCancelOrderCode = new JPanel(new GridBagLayout());
		pCancelOrderCode.add(lbOldCancelOrderCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(tfNewCancelOrderCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(lbNewCancelOrderCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(tfNewCancelOrderCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.add(btnSaveCancelOrderCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pCancelOrderCode.setBorder(BorderFactory.createTitledBorder("Cancel Order Code"));
		
		JLabel lbOldClearTableCode = new JLabel("old code");
		JLabel lbNewClearTableCode = new JLabel("new code");
		tfOldClearTableCode = new JTextField();
		tfNewClearTableCode = new NumberTextField(false);
		JPanel pClearTableCode = new JPanel(new GridBagLayout());
		pClearTableCode.add(lbOldClearTableCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(tfNewClearTableCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(lbNewClearTableCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(tfNewClearTableCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.add(btnSaveClearTableCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pClearTableCode.setBorder(BorderFactory.createTitledBorder("Clear Table Code"));
		
		ButtonGroup bgLanguageAmount = new ButtonGroup();
		bgLanguageAmount.add(rbLanguageAmount1);
		bgLanguageAmount.add(rbLanguageAmount2);
		rbLanguageAmount2.setSelected(true);
		JPanel pLanguageSet = new JPanel(new GridBagLayout());
		pLanguageSet.setBorder(BorderFactory.createTitledBorder("Language Setting"));
		pLanguageSet.add(new JLabel("Language Amount"), new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(rbLanguageAmount1, 			new GridBagConstraints(1, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(rbLanguageAmount2, 			new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(new JLabel("First Language"), 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(tfFirstLanguageName, 			new GridBagConstraints(1, 1, 2, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(new JLabel("Second Language"), new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pLanguageSet.add(tfSecondLanguageName, 			new GridBagConstraints(1, 2, 2, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,10,0,0), 0, 0));;
		pLanguageSet.add(btnSaveLanguageSet, 			new GridBagConstraints(3, 0, 2, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,10,0,0), 0, 0));;
		
		JPanel pBasic = new JPanel(new GridBagLayout());
		pBasic.add(pConfirmCode, 		new GridBagConstraints(0, 0, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		pBasic.add(pOpenCashdrawerCode, new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		pBasic.add(pLanguageSet, 		new GridBagConstraints(0, 2, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add("Basic Setting", pBasic);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(tabPane,  BorderLayout.CENTER);
		
		btnSaveConfirmCode.addActionListener(this);
		btnSaveOpenCashdrawerCode.addActionListener(this);
		btnSaveLanguageSet.addActionListener(this);
		btnSaveClearTableCode.addActionListener(this);
		btnSaveCancelOrderCode.addActionListener(this);
		
		setSize(500, 400);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}

	private void initData(){
//		tfOldConfirmCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_CONFIRMCODE));
//		tfOldOpenCashdrawerCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_OPENCASHDRAWERCODE));
		if ("1".equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_LANGUAGEAMOUNT))){
			rbLanguageAmount1.setSelected(true);
		} else if ("2".equals(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_LANGUAGEAMOUNT))){
			rbLanguageAmount2.setSelected(true);
		}
		tfFirstLanguageName.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_FIRSTLANGUAGENAME));
		tfSecondLanguageName.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_SECONDLANGUAGENAME));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSaveConfirmCode){
			doSaveConfirmCode();
		} else if (e.getSource() == btnSaveOpenCashdrawerCode){
			doSaveOpenCashdrawerCode();
		} else if (e.getSource() == btnSaveLanguageSet){
			doSaveLanguageSet();
		} else if (e.getSource() == btnSaveCancelOrderCode){
			doSaveCancelOrderCode();
		} else if (e.getSource() == btnSaveClearTableCode){
			doSaveClearTableCode();
		}
	}
	
	private void doSaveLanguageSet(){
		if (tfFirstLanguageName.getText() == null || tfFirstLanguageName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input first language name");
			return;
		}
		if (tfSecondLanguageName.getText() == null || tfSecondLanguageName.getText().length() == 0){
			if (rbLanguageAmount2.isSelected()){
				JOptionPane.showMessageDialog(this, "No input second language name");
				return;
			}
		}
		String url = "common/savelanguageset";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		if (rbLanguageAmount2.isSelected()){
			params.put("amount", "2");
			params.put("secondName", tfSecondLanguageName.getText());
		} else {
			params.put("amount", "1");
		}
		params.put("firstName", tfFirstLanguageName.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save language. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save language. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save language. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save language. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_LANGUAGEAMOUNT, params.get("amount"));
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_FIRSTLANGUAGENAME, tfFirstLanguageName.getText());
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_SECONDLANGUAGENAME, tfSecondLanguageName.getText());
		this.setVisible(false);
	}
	
	private void doSaveCancelOrderCode(){
		if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input new code");
			return;
		}
		String url = "common/savecancelordercode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", tfNewConfirmCode.getText());
		params.put("oldCode", tfOldConfirmCode.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save cancel order code. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save cancel order code. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save cancel order code. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save cancel order code. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CANCELORDERCODE, tfNewConfirmCode.getText());
		this.setVisible(false);
	}
	
	private void doSaveClearTableCode(){
		if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input new code");
			return;
		}
		String url = "common/savecleartablecode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", tfNewConfirmCode.getText());
		params.put("oldCode", tfOldConfirmCode.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save clear table code. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save clear table code. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save clear table code. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save clear table code. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CLEARTABLECODE, tfNewConfirmCode.getText());
		this.setVisible(false);
	}
	
	private void doSaveConfirmCode(){
		if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input new code");
			return;
		}
		String url = "common/saveconfirmcode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", tfNewConfirmCode.getText());
		params.put("oldCode", tfOldConfirmCode.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save confirm code. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save confirm code. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save confirm code. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save confirm code. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_CONFIRMCODE, tfNewConfirmCode.getText());
		this.setVisible(false);
	}
	
	private void doSaveOpenCashdrawerCode(){
//		if (tfOldOpenCashdrawerCode.getText() == null || tfOldOpenCashdrawerCode.getText().length() == 0){
//			JOptionPane.showMessageDialog(this, "No input old code");
//			return;
//		}
		if (tfNewOpenCashdrawerCode.getText() == null || tfNewOpenCashdrawerCode.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input new code");
			return;
		}
		String url = "common/saveopencashdrawercode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", tfNewOpenCashdrawerCode.getText());
		params.put("oldCode", tfOldOpenCashdrawerCode.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save open cashdrawer code. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save open cashdrawer code. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save open cashdrawer code. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save open cashdrawer code. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_OPENCASHDRAWERCODE, tfNewOpenCashdrawerCode.getText());
		this.setVisible(false);
	}
}
