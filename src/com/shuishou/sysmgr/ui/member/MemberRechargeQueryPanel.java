package com.shuishou.sysmgr.ui.member;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.MemberBalance;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.JDatePicker;

public class MemberRechargeQueryPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(MemberRechargeQueryPanel.class.getName());
	private MainFrame mainFrame;
	private JDatePicker dpStartDate = new JDatePicker();
	private JDatePicker dpEndDate = new JDatePicker();
	private JButton btnQuery = new JButton("Query");
	private JButton btnToday = new JButton(Messages.getString("StatisticsPanel.Today"));
	private JButton btnYesterday = new JButton(Messages.getString("StatisticsPanel.Yesterday"));
	private JButton btnThisWeek = new JButton(Messages.getString("StatisticsPanel.Thisweek"));
	private JButton btnLastWeek = new JButton(Messages.getString("StatisticsPanel.Lastweek"));
	private JButton btnThisMonth = new JButton(Messages.getString("StatisticsPanel.Thismonth"));
	private JButton btnLastMonth = new JButton(Messages.getString("StatisticsPanel.Lastmonth"));
	private JLabel lbTotal = new JLabel();
	private JTable table = new JTable();
	private RechargeModel model = new RechargeModel();
	
	private ArrayList<MemberBalance> listRecharge = new ArrayList<>();
	
	public MemberRechargeQueryPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		initUI();
	}
	
	private void initUI(){
		JLabel lbStartDate = new JLabel("Start Date : ");
		JLabel lbEndDate = new JLabel("End Date : ");
		dpStartDate.setShowYearButtons(true);
		dpEndDate.setShowYearButtons(true);
		
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel pQueryTimeButton = new JPanel(new GridBagLayout());
		pQueryTimeButton.add(btnToday,		new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(btnYesterday,	new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(btnThisWeek,	new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(btnLastWeek,	new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(btnThisMonth,	new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(btnLastMonth,	new GridBagConstraints(5, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
		pQueryTimeButton.add(new JLabel(),	new GridBagConstraints(6, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		
		JPanel pCondition = new JPanel(new GridBagLayout());
		pCondition.add(lbStartDate,	new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(dpStartDate,	new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbEndDate,	new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(dpEndDate,	new GridBagConstraints(5, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(btnQuery,	new GridBagConstraints(6, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(new JLabel(),new GridBagConstraints(8, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		
		JPanel pQuery = new JPanel(new BorderLayout());
		pQuery.add(pCondition, BorderLayout.CENTER);
		pQuery.add(pQueryTimeButton, BorderLayout.SOUTH);
		
		btnQuery.addActionListener(this);
		btnToday.addActionListener(this);
		btnYesterday.addActionListener(this);
		btnThisWeek.addActionListener(this);
		btnLastWeek.addActionListener(this);
		btnThisMonth.addActionListener(this);
		btnLastMonth.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(jspTable, BorderLayout.CENTER);
		add(pQuery, BorderLayout.NORTH);
		add(lbTotal, BorderLayout.SOUTH);
	}
	
	private void doQuery(){
		String url = "member/querymemberrecharge";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		if (dpStartDate.getModel() != null && dpStartDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpStartDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			params.put("startTime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		if (dpEndDate.getModel() != null && dpEndDate.getModel().getValue() != null){
			Calendar c = (Calendar)dpEndDate.getModel().getValue();
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			params.put("endTime", ConstantValue.DFYMDHMS.format(c.getTime()));
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for query shiftwork. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for query shiftwork. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat(ConstantValue.DATE_PATTERN_YMDHMS).create();
		HttpResult<ArrayList<MemberBalance>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<MemberBalance>>>(){}.getType());
		if (!result.success){
			logger.error("return false while query shiftwork. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, result.result);
			return;
		}
		listRecharge = result.data;
		table.updateUI();
		doStatistics();
	}
	
	private void doStatistics(){
		double total = 0;
		HashMap<String, Double> hmPaywayValue = new HashMap<>();//金额
		HashMap<String, Integer> hmPaywayAmount = new HashMap<>();//数量
		for (int i = 0; i < listRecharge.size(); i++) {
			total += listRecharge.get(i).getAmount();
			String payway = listRecharge.get(i).getPayway();
			if (hmPaywayValue.get(payway) == null){
				hmPaywayValue.put(payway, listRecharge.get(i).getAmount());
			} else {
				hmPaywayValue.put(payway, listRecharge.get(i).getAmount() + hmPaywayValue.get(payway));
			}
			if (hmPaywayAmount.get(payway) == null){
				hmPaywayAmount.put(payway, 1);
			} else {
				hmPaywayAmount.put(payway, hmPaywayAmount.get(payway) + 1);
			}
		}
		String stat = "";
		for (Iterator<String> iterator = hmPaywayValue.keySet().iterator(); iterator.hasNext();) {
			String payway = iterator.next();
			stat += payway + ": $" + String.format(ConstantValue.FORMAT_DOUBLE, hmPaywayValue.get(payway))
			     + "/" + hmPaywayAmount.get(payway) + "  ";
			
		}
			
		lbTotal.setText("Total Money : $" + String.format(ConstantValue.FORMAT_DOUBLE, total)
		  + ", Items : " + listRecharge.size() + ", " + stat);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuery){
			doQuery();
		} else if (e.getSource() == btnToday){
			Calendar c = Calendar.getInstance();
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnYesterday){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnThisWeek){
			Calendar c = Calendar.getInstance(Locale.CHINA);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnLastWeek){
			Calendar c = Calendar.getInstance(Locale.CHINA);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnThisMonth){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		} else if (e.getSource() == btnLastMonth){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
//			dpStartDate.setDateTime(c);
			dpStartDate.getModel().setYear(c.get(Calendar.YEAR));
			dpStartDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpStartDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpStartDate.getModel().setSelected(true);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setYear(c.get(Calendar.YEAR));
			dpEndDate.getModel().setMonth(c.get(Calendar.MONTH));
			dpEndDate.getModel().setDay(c.get(Calendar.DAY_OF_MONTH));
			dpEndDate.getModel().setSelected(true);
		}
	}
	
	class RechargeModel extends AbstractTableModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] header = new String[]{"Member Card", "Member Name", "Branch", "Time", "Amount", "Balance", "Pay Way"};
		
		@Override
		public int getRowCount() {
			if (listRecharge == null) return 0;
			return listRecharge.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MemberBalance mb = listRecharge.get(rowIndex);
			switch(columnIndex){
			case 0:
				return mb.getMemberCard();
			case 1:
				return mb.getMemberName();
			case 2: 
				return mb.getPlace();
			case 3:
				return ConstantValue.DFYMDHMS.format(mb.getDate());
			case 4:
				return mb.getAmount();
			case 5:
				return mb.getNewValue();
			case 6:
				return mb.getPayway();
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public MemberBalance getObjectAt(int row){
			return listRecharge.get(row);
		}
	}

	
}
