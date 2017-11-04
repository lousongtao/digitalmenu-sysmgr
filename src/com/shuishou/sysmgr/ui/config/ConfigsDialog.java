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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private JTextField tfOldConfirmCode;
	private NumberTextField tfNewConfirmCode;
	private JTextField tfOldOpenCashdrawerCode;
	private NumberTextField tfNewOpenCashdrawerCode;
	
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
		tfOldConfirmCode.setEditable(false);
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
		tfOldOpenCashdrawerCode.setEditable(false);
		tfNewOpenCashdrawerCode = new NumberTextField(false);
		JPanel pOpenCashdrawerCode = new JPanel(new GridBagLayout());
		pOpenCashdrawerCode.add(lbOldOpenCashdrawerCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfOldOpenCashdrawerCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(lbNewOpenCashdrawerCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfNewOpenCashdrawerCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(btnSaveOpenCashdrawerCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.setBorder(BorderFactory.createTitledBorder("Open Cashdrawer Code"));
		
		JPanel pBasic = new JPanel(new GridBagLayout());
		pBasic.add(pConfirmCode, 		new GridBagConstraints(0, 0, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		pBasic.add(pOpenCashdrawerCode, new GridBagConstraints(0, 1, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add("Basic Setting", pBasic);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(tabPane,  BorderLayout.CENTER);
		
		btnSaveConfirmCode.addActionListener(this);
		btnSaveOpenCashdrawerCode.addActionListener(this);
		setSize(500, 400);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}

	private void initData(){
		tfOldConfirmCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_CONFIRMCODE));
		tfOldOpenCashdrawerCode.setText(mainFrame.getConfigsMap().get(ConstantValue.CONFIGS_OPENCASHDRAWERCODE));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSaveConfirmCode){
			if (tfNewConfirmCode.getText() == null || tfNewConfirmCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No Input");
				return;
			}
			String url = "common/saveconfirmcode";
			HashMap<String, String> params = new HashMap<>();
			params.put("userId", MainFrame.getLoginUser().getId()+"");
			params.put("code", tfNewConfirmCode.getText());
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
		} else if (e.getSource() == btnSaveOpenCashdrawerCode){
			if (tfNewOpenCashdrawerCode.getText() == null || tfNewOpenCashdrawerCode.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "No Input");
				return;
			}
			String url = "common/saveopencashdrawercode";
			HashMap<String, String> params = new HashMap<>();
			params.put("userId", MainFrame.getLoginUser().getId()+"");
			params.put("code", tfNewOpenCashdrawerCode.getText());
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
}
