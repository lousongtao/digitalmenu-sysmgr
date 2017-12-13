package com.shuishou.sysmgr.ui.material;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Material;
import com.shuishou.sysmgr.beans.MaterialCategory;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialogOperatorIFC;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class MaterialPanel extends JPanel implements CommonDialogOperatorIFC, ActionListener{
	private final Logger logger = Logger.getLogger(MaterialPanel.class.getName());
	private int qrCodeSize = 500;
	private MaterialMgmtPanel parent;
	private JTextField tfName = new JTextField(155);
	private JTextField tfUnit = new JTextField(155);
	private NumberTextField tfDisplaySeq= new NumberTextField(false);
	private NumberTextField tfLeftAmount= new NumberTextField(true);
	private NumberTextField tfAlarmAmount= new NumberTextField(true);
	private JComboBox cbCategory = new JComboBox();
	private JButton btnPrintCode = new JButton("Print QR Code by Name");
	private Material material;
	private MaterialCategory parentCategory;
	
	public MaterialPanel(MaterialMgmtPanel parent){
		this.parent = parent;
		initUI();
		initData();
	}
	
	public MaterialPanel(MaterialMgmtPanel parent, MaterialCategory parentCategory){
		this.parent = parent;
		this.parentCategory = parentCategory;
		initUI();
		initData();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel(Messages.getString("MaterialPanel.Name"));
		JLabel lbUnit = new JLabel(Messages.getString("MaterialPanel.Unit"));
		JLabel lbLeftAmount = new JLabel(Messages.getString("MaterialPanel.LeftAmount"));
		JLabel lbAlarmAmount = new JLabel(Messages.getString("MaterialPanel.AlarmAmount"));
		JLabel lbDisplaySeq = new JLabel(Messages.getString("MaterialPanel.DisplaySequence"));
		JLabel lbCategory = new JLabel(Messages.getString("MaterialPanel.Category"));
		cbCategory.setRenderer(new CategoryListRender());
		this.setLayout(new GridBagLayout());
		add(lbName, 		new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfName, 		new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbDisplaySeq, 	new GridBagConstraints(0, 1, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfDisplaySeq, 	new GridBagConstraints(1, 1, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbUnit, 		new GridBagConstraints(0, 2, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfUnit, 		new GridBagConstraints(1, 2, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbLeftAmount, 	new GridBagConstraints(0, 3, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfLeftAmount, 	new GridBagConstraints(1, 3, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbAlarmAmount, 	new GridBagConstraints(0, 4, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(tfAlarmAmount, 	new GridBagConstraints(1, 4, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(lbCategory, 	new GridBagConstraints(0, 5, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(cbCategory, 	new GridBagConstraints(1, 5, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(btnPrintCode, 	new GridBagConstraints(1, 6, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10,0,0,0), 0, 0));
		add(new JPanel(), 	new GridBagConstraints(0, 7, 1, 1,0,1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		tfName.setMinimumSize(new Dimension(180,25));
		tfUnit.setMinimumSize(new Dimension(180,25));
		tfLeftAmount.setMinimumSize(new Dimension(180,25));
		tfAlarmAmount.setMinimumSize(new Dimension(180,25));
		tfDisplaySeq.setMinimumSize(new Dimension(180,25));
		cbCategory.setMinimumSize(new Dimension(180, 25));
		btnPrintCode.addActionListener(this);
	}

	@Override
	public boolean doSave() {
		if (!doCheckInput())
			return false;
		
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("name", tfName.getText());
		params.put("sequence", tfDisplaySeq.getText());
		params.put("leftAmount", tfLeftAmount.getText());
		params.put("alarmAmount", tfAlarmAmount.getText());
		params.put("unit", tfUnit.getText());
		params.put("categoryId", ((MaterialCategory)cbCategory.getSelectedItem()).getId()+"");
		String url = "material/addmaterial";
		if (material != null){
			url = "material/updatematerial";
			params.put("id", material.getId() + "");
		}
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add/update material. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add/update material. URL = " + url);
			return false;
		}
		Gson gson = new Gson();
		HttpResult<Material> result = gson.fromJson(response, new TypeToken<HttpResult<Material>>(){}.getType());
		if (!result.success){
			logger.error("return false while add/update material. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while add/update material. URL = " + url + ", response = "+response);
			return false;
		}
		result.data.setMaterialCategory((MaterialCategory)cbCategory.getSelectedItem());
		//update parent menu tree
		if (material == null){
			parent.insertNode(result.data);
		} else {
			parent.updateNode(result.data, material);
		}
		return true;
	}
	
	private boolean doCheckInput(){
		if (tfName.getText() == null || tfName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Name");
			return false;
		}
		if (tfUnit.getText() == null || tfUnit.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Unit");
			return false;
		}
		if (tfDisplaySeq.getText() == null || tfDisplaySeq.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "Please input Display Sequence");
			return false;
		}
		return true;
	}
	
	public void setObjectValue(Material material){
		this.material = material;
		tfName.setText(material.getName());
		tfUnit.setText(material.getUnit());
		tfLeftAmount.setText(material.getLeftAmount()+"");
		tfAlarmAmount.setText(material.getAlarmAmount() + "");
		tfDisplaySeq.setText(material.getSequence()+"");
		cbCategory.setSelectedItem(material.getMaterialCategory());
	}
	
	public void initData(){
		ArrayList<MaterialCategory> listCategory = parent.getMainFrame().getListMaterialCategory();
		cbCategory.removeAllItems();
		for(MaterialCategory mc : listCategory){
			cbCategory.addItem(mc);
		}
		if (parentCategory != null){
			cbCategory.setSelectedItem(parentCategory);
		}
	}

	private BufferedImage generateQRCode(String txt) throws WriterException{
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(txt,BarcodeFormat.QR_CODE, 100, 100, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		return image;
	}
	

	private void printQRCode(String txt){
		BufferedImage image = null;
		try {
			image = generateQRCode(txt);
		} catch (WriterException e1) {
			logger.error("", e1);
		}
		PrinterJob pj = PrinterJob.getPrinterJob();
		PageFormat pf = new PageFormat();
		Paper paper = new Paper();
		paper.setImageableArea(0, 0, 200, 200);
		pf.setPaper(paper);
		
		if (pj.printDialog()) {
			try {
				pj.setPrintable(new QRCodePrinter(image, txt), pf);
				pj.print();
			} catch (PrinterException e) {
				logger.error("", e);
			}
		}
	}
	
	class QRCodePrinter implements Printable {
		private BufferedImage image;
		private String txt;
		public QRCodePrinter(BufferedImage image, String txt){
			this.image = image;
			this.txt = txt;
		}

		@Override
		public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
			if (page > 0) {
				return NO_SUCH_PAGE;
			}
			Font font = new Font("Serif", Font.BOLD, 24);
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			g.setFont(font);
			g.drawImage(image, 0, 0, null);
			//if txt is too long, it could not be printed totally, so must cut to 2 lines
			if (txt.length() > 4){
				g.drawString(txt.substring(0, 4), 100, 30);
				g.drawString(txt.substring(4), 100, 70);
			} else {
				g.drawString(txt, 100, 10);
			}
			
			return PAGE_EXISTS;
		}
	} 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnPrintCode){
			if (material != null)
				printQRCode(material.getName());
		}
	}
	
	class CategoryListRender extends JLabel implements ListCellRenderer{
		
		public CategoryListRender(){}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value != null)
				setText(((MaterialCategory)value).getName());
			return this;
		}
		
	}
}
