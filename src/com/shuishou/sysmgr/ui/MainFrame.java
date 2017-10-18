package com.shuishou.sysmgr.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.Dish;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Printer;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;

public class MainFrame extends JFrame implements ActionListener{
	private final Logger logger = Logger.getLogger(MainFrame.class.getName());
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;
	public static int WINDOW_LOCATIONX;
	public static int WINDOW_LOCATIONY;
	public static String language;
	public static String SERVER_URL;
	private static final String CARDLAYOUT_MENUMGMT= "menumgmt"; 
	private JButton btnAccountMgr = new JButton(Messages.getString("MainFrame.ToolBar.AccountMgr"));
	private JButton btnMenuMgr = new JButton(Messages.getString("MainFrame.ToolBar.MenuMgr"));
	private JButton btnDeskMgr = new JButton(Messages.getString("MainFrame.ToolBar.DeskMgr"));
	private JButton btnPrinterMgr = new JButton(Messages.getString("MainFrame.ToolBar.PrinterMgr"));
	private JButton btnConfirmCode = new JButton(Messages.getString("MainFrame.ToolBar.ConfirmCode"));
	private JButton btnQueryLog = new JButton(Messages.getString("MainFrame.ToolBar.QueryLog"));
	private JButton btnQueryIndent = new JButton(Messages.getString("MainFrame.ToolBar.QueryIndent"));
	private JButton btnQuerySwiftWork = new JButton(Messages.getString("MainFrame.ToolBar.QuerySwiftWork"));
	private JPanel pContent = new JPanel(new CardLayout());
	
	private ArrayList<Category1> listCategory1s;
	private ArrayList<Printer> listPrinters;
	private static UserData loginUser;
	
	private MenuMgmtPanel pMenuMgmt;
	
	private Gson gson = new Gson();
	
	private MainFrame(){
		initUI();
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(WINDOW_LOCATIONX, WINDOW_LOCATIONY);
		setTitle(Messages.getString("MainFrame.FrameTitle")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initData();
	}
	
	private void initData(){
		reloadListCategory1s();
		reloadListPrinters();
		
	}
	
	private void initUI(){
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(0,0,10,0));
		toolbar.add(btnAccountMgr);
		toolbar.addSeparator();
		toolbar.add(btnMenuMgr);
		toolbar.addSeparator();
		toolbar.add(btnDeskMgr);
		toolbar.addSeparator();
		toolbar.add(btnPrinterMgr);
		toolbar.addSeparator();
		toolbar.add(btnConfirmCode);
		toolbar.addSeparator();
		toolbar.add(btnQueryLog);
		toolbar.addSeparator();
		toolbar.add(btnQueryIndent);
		toolbar.addSeparator();
		toolbar.add(btnQuerySwiftWork);
		btnAccountMgr.addActionListener(this);
		btnMenuMgr.addActionListener(this);
		btnDeskMgr.addActionListener(this);
		btnPrinterMgr.addActionListener(this);
		btnConfirmCode.addActionListener(this);
		btnQueryLog.addActionListener(this);
		btnQueryIndent.addActionListener(this);
		btnQuerySwiftWork.addActionListener(this);
		
		
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(toolbar, BorderLayout.NORTH);
		c.add(pContent, BorderLayout.CENTER);
	}
	
	public void startLogin(){
		LoginDialog dlg = new LoginDialog(this);
		dlg.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAccountMgr){
			
		} else if (e.getSource() == btnMenuMgr){
			//TODO: 
//			if (pMenuMgmt == null){
//				pMenuMgmt = new MenuMgmtPanel(this, listCategory1s);
//				pContent.add(pMenuMgmt, CARDLAYOUT_MENUMGMT);
//				pContent.updateUI();
//			} else {
//				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MENUMGMT);
//			}
			//TODO: load menu everytime
			reloadListCategory1s();
			
			pMenuMgmt = new MenuMgmtPanel(this, listCategory1s);
			pContent.removeAll();
			pContent.add(pMenuMgmt, CARDLAYOUT_MENUMGMT);
			pContent.updateUI();
		} else if (e.getSource() == btnDeskMgr){
			
		} else if (e.getSource() == btnPrinterMgr){
			
		} else if (e.getSource() == btnConfirmCode){
			
		} else if (e.getSource() == btnQueryLog){
			
		} else if (e.getSource() == btnQueryIndent){
			
		} else if (e.getSource() == btnQuerySwiftWork){
			
		} 
	}

	
	public static UserData getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(UserData loginUser) {
		this.loginUser = loginUser;
	}

	
	public ArrayList<Category1> getListCategory1s() {
		return listCategory1s;
	}

	public void reloadListCategory1s() {
		listCategory1s = HttpUtil.loadMenu(this, SERVER_URL + "menu/querymenu");
		Comparator comp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Category1 && o2 instanceof Category1)
					return ((Category1) o1).getSequence() - ((Category1) o2).getSequence();
				else if (o1 instanceof Category2 && o2 instanceof Category2)
					return ((Category2) o1).getSequence() - ((Category2) o2).getSequence();
				else if (o1 instanceof Dish && o2 instanceof Dish)
					return ((Dish) o1).getSequence() - ((Dish) o2).getSequence();
				return 0;
			}
		};
			
		Collections.sort(listCategory1s, comp);
		for(Category1 c1 : listCategory1s){
			Collections.sort(c1.getCategory2s(), comp);
			for(Category2 c2 : c1.getCategory2s()){
				Collections.sort(c2.getDishes(), comp);
			}
		}
	}
	
	public ArrayList<Printer> getListPrinters() {
		return listPrinters;
	}
	
	public void reloadListPrinters() {
		listPrinters = HttpUtil.loadPrinter(this, SERVER_URL + "common/getprinters");
	}

	public static void main(String[] args){
		//load properties
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = MainFrame.class.getClassLoader().getResourceAsStream("config.properties");
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Messages.initResourceBundle(prop.getProperty("language"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//windows 格式
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
//		Enumeration enums = UIManager.getDefaults().keys();
//		while(enums.hasMoreElements()){
//			Object key = enums.nextElement();
//			Object value = UIManager.get(key);
//			if (value instanceof Font){
//				UIManager.put(key, ConstantValue.FONT_20PLAIN);
//			}
//		}
		MainFrame.SERVER_URL = prop.getProperty("SERVER_URL");
		MainFrame.WINDOW_WIDTH = Integer.parseInt(prop.getProperty("mainframe.width"));
		MainFrame.WINDOW_HEIGHT = Integer.parseInt(prop.getProperty("mainframe.height"));
		MainFrame.WINDOW_LOCATIONX = Integer.parseInt(prop.getProperty("mainframe.locationx"));
		MainFrame.WINDOW_LOCATIONY = Integer.parseInt(prop.getProperty("mainframe.locationy"));
		MainFrame.language = prop.getProperty("language");
		MainFrame f = new MainFrame();
		
		f.setVisible(true);
		f.startLogin();
	}
}
