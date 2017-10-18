package com.shuishou.sysmgr.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.entity.SerializableEntity;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;

public class Category1Panel extends JPanel implements CommonDialogOperatorIFC{
	private final Logger logger = Logger.getLogger(Category1Panel.class.getName());
	private MenuMgmtPanel parent;
	private JTextField tfChineseName= new JTextField(155);
	private JTextField tfEnglishName= new JTextField(155);
	private JTextField tfDisplaySeq= new JTextField(155);
	private Category1 c1;
	public Category1Panel(MenuMgmtPanel parent){
		this.parent = parent;
		initUI();
	}
	
	private void initUI(){
		JLabel lbChineseName = new JLabel("Chinese Name");
		JLabel lbEnglishName = new JLabel("English Name");
		JLabel lbDisplaySeq = new JLabel("Display Sequence");
		this.setLayout(new GridBagLayout());
		add(lbChineseName, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfChineseName, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbEnglishName, new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfEnglishName, new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), new GridBagConstraints(0, 3, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
//		tfChineseName.setEditable(false);
//		tfEnglishName.setEditable(false);
//		tfDisplaySeq.setEditable(false);
		tfChineseName.setMinimumSize(new Dimension(180,25));
		tfEnglishName.setMinimumSize(new Dimension(180,25));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
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
	
	

	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("chineseName", tfChineseName.getText());
		params.put("englishName", tfEnglishName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		String url = "menu/add_category1";
		if (c1 != null){
			url = "menu/update_category1";
			params.put("id", c1.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update category1. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update category1. URL = " + url);
			return false;
		}
		Gson gson = new Gson();
		HttpResult<Category1> result = gson.fromJson(response, new TypeToken<HttpResult<Category1>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update category1. URL = " + url);
			JOptionPane.showMessageDialog(this, "return false while add/update category1. URL = " + url);
			return false;
		}
		//update parent menu tree
		if (c1 == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, c1);
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
		return true;
	}
	
	public void setObjectValue(Category1 c1){
		this.c1 = c1;
		tfChineseName.setText(c1.getChineseName());
		tfEnglishName.setText(c1.getEnglishName());
		tfDisplaySeq.setText(c1.getSequence()+"");
	}
}