/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SerialPortsWindLines.java
 *
 * Created on 2010-7-29, 19:13:00
 */

package com.hitangjun.desk;

import gnu.io.SerialPort;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;


/**
 * http://hitangjun.com
 * @author John
 */
public class SerialPortsWindLines extends javax.swing.JFrame {

	 DynamicChart dynamicChart = new DynamicChart();
	 SerialPortReader sr =new SerialPortReader();
     
    /** Creates new form SerialPortsWindLines */
    public SerialPortsWindLines() {
        //���ô������Ϊwindows
    	try{
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch(Exception e){
            Log.debug("���ô���windows���ʧ��");
        }
        initComponents();
        //����Swing JFrame��ʼλ��Ϊ��Ļ����
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        //�򿪴���
        openSerialPort();
    }

    //ת��Ԥ�����żУ��λ��ֵ
    private int getParityValue(String parity){
    	 if("NONE".equals(parity)){
      	   return SerialPort.PARITY_NONE;
         }else if("ODD".equals(parity)){
        	 return SerialPort.PARITY_ODD;
         }else if("EVEN".equals(parity)){
        	 return SerialPort.PARITY_EVEN;
         }else if("MARK".equals(parity)){
        	 return SerialPort.PARITY_MARK;
         }else if("SPACE".equals(parity)){
        	 return SerialPort.PARITY_SPACE;
         }else{
        	 return SerialPort.PARITY_NONE;
         }
    }
    
    //��ȡ����ֵ,�򿪴���
    public void openSerialPort()
    {
        //0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13 0x14 0x15 0x16 0x17 0x18 0x19 0x20 0x21 0x22 0x23 0x24 0x25 0x26 0x27 0x28 0x29 0x30 0x31 0x32 0x33 0x34 0x35 0x36 0x37 0x38 0x39 0x40 0x41 0x42 0x43 0x44 0x45 0x46 0x47 0x48 0x49 0x50 0x51 0x52 0x53 0x54 0x55 0x56 0x00 0XFF 0x11 0x12 0x13
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();
        
        String port = portComboBox.getSelectedItem().toString();
        String rate = rateComboBox.getSelectedItem().toString();
        String dataBit = dataBitComboBox.getSelectedItem().toString();
        String stopBit = stopBitComboBox.getSelectedItem().toString();
        String parity = parityComboBox.getSelectedItem().toString();
        
        int parityInt = getParityValue(parity);
        String paramsMsg = "�򿪴��� "+port+","+rate+","+parity+","+dataBit+","+stopBit;
        
        params.put( SerialPortReader.PARAMS_PORT, port ); // �˿�����
        params.put( SerialPortReader.PARAMS_RATE, rate ); // ������
        params.put( SerialPortReader.PARAMS_DATABITS,dataBit  ); // ����λ
        params.put( SerialPortReader.PARAMS_STOPBITS, stopBit ); // ֹͣλ
        params.put( SerialPortReader.PARAMS_PARITY, parityInt ); // ����żУ��
        params.put( SerialPortReader.PARAMS_TIMEOUT, 1000 ); // �豸��ʱʱ�� 1��
        params.put( SerialPortReader.PARAMS_DELAY, 200 ); // �˿�����׼��ʱ�� 1��
        try {
			sr.open(params);
			sr.addObserver( new Observer() {
				
				@Override
				public void update(Observable o, Object arg) {
					// TODO Auto-generated method stub
					  //�����յ������ݽ��и���ͼ��
				    	Log.debug("��ȡ��������λ����"+(( byte[] ) arg).length);
				    	String v= new String( ( byte[] ) arg );
				    	updateChart(v);
				}
			} );
			successOpenPort(paramsMsg);
		} catch (SerialPortException e) {
			failOpenPort(e.getMessage());
		}
    }
    
    //���ô򿪶˿�ʧ�ܵ���Ϣ
    void successOpenPort(String text){
    	Log.debug(text);
    	setOperationText("����");
    	setResultText(text);
//    	���ô򿪻�رմ��ڰ�ť��״̬Ϊ�ر�
    	setToggleBtnClose();
    }
    
    //���ô򿪶˿�ʧ�ܵ���Ϣ
    void failOpenPort(String text){
    	JOptionPane.showMessageDialog(null, text, "", JOptionPane.WARNING_MESSAGE);
    	
    	Log.debug(text);
    	setResultText(text);
    	
    	//���ô򿪻�رմ��ڰ�ť��״̬Ϊ��
     	setToggleBtnOpen();
    }
    
    //���������ݵ��߼�
    private void sendDataBtnMouseClicked(java.awt.event.MouseEvent evt) {                                      
        updateChart(inputTextArea.getText());
        
    }                                     

    private void setToggleBtnOpen(){
    	portComboBox.setEnabled(true);
    	openOrCloseToggleButton.setText("�򿪴���");
    	openOrCloseToggleButton.setToolTipText("�򿪴���");
    }
    private void setToggleBtnClose(){
    	portComboBox.setEnabled(false);
    	openOrCloseToggleButton.setText("�رմ���" );
    	openOrCloseToggleButton.setToolTipText("�رմ���");
    }

    private void openOrCloseToggleButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	//���û�д���򿪶˿ڷ���رն˿�
    	if(!sr.isOpen()){
//        	setToggleBtnOpen();
	         openSerialPort();
        }else{
        	try {
        		sr.close();
        		setToggleBtnOpen();
        	} catch (SerialPortException e) {
        		setOperationText(e.getMessage());
        	}
        }
    }
    
    XYTextAnnotation textpointer;
    XYAnnotation bestBid;
    //̽�����ݵ㣬����ǳ���
    private void dectiveButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	try {
			int index = Integer.valueOf(detectiveSpinner.getValue().toString());
			
			//��ȡ�����������ֵ
			XYDataItem item = dynamicChart.getXYSeries().getDataItem(index);
			double clickX = Double.valueOf(item.getX().toString());
			double clickY = Double.valueOf(item.getY().toString());
			
			final CircleDrawer cd = new CircleDrawer(Color.red, new BasicStroke(5.0f), new Color(231 ,242 ,255  ));// ����ȦԲȦ�İ뾶
            bestBid = new XYDrawableAnnotation(clickX, clickY, 0.1, 0.1, cd);
            dynamicChart.getXYPlot().addAnnotation(bestBid);
            
            double x = clickX+15;
            double y = clickY-15;
            
            if(y<5){
            	y=clickY+15;
            }
            
//          �ı���ʾ����
			textpointer = new XYTextAnnotation(clickX+","+clickY,clickX+15 , clickY-15);
			textpointer.setBackgroundPaint(Color.YELLOW);
			textpointer.setPaint(Color.CYAN);
			dynamicChart.getXYPlot().addAnnotation(textpointer);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "����������ֵ", "", JOptionPane.WARNING_MESSAGE);
			setResultText("����������ֵ");
		}
    }

    //�������������
    private void clearButtonMouseClicked(java.awt.event.MouseEvent evt) {
        inputTextArea.setText("");
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SerialPortsWindLines().setVisible(true);
            }
        });
    }
    
    int lastIndex = 0;
  
    
    //����ͼ��
    public void updateChart(String data){
    	
    	//�������ʵ������������ݼ�
//    	dynamicChart.getXYPlot().removeAnnotation(bestBid);
//    	dynamicChart.getXYPlot().removeAnnotation(textpointer);
    	
    	data= data.trim();
    	Log.debug(data);
    	XYSeries xySeries = dynamicChart.getXYSeries();
        int[] values = hexStrToInt(data);
        if(null != values){
	        for(int i=0;i<values.length;i++){
	        	lastIndex+=i;
	        	xySeries.add(lastIndex,values[i]);
	        }
	        if(lastIndex >=511){
	        	lastIndex = 0;
	        	xySeries.clear();
	        }
	   }
    }
    
    //��16�����ַ���ת����int
    public int[] hexStrToInt(String hexStr){
    	clearResultText();
        String[] hexStrArray = hexStr.split( " " );
        int[] result;
		try {
			result = new int[hexStrArray.length];
			for(int i=0;i<hexStrArray.length;i++){
			    result[i] = Integer.parseInt( hexStrArray[i].substring( 2 ), 16 );
			}
			return result;
		} catch (NumberFormatException e) {
			setResultText("��������ݲ���ȷ");
		}
		return null;
    }
    
    /**
     * ��ղ�����ʾ�ı�
     *
     */
    private void clearOperationText(){
        operationLabel.setText("");
    }
    /**
     * ���ò�����ʾ�ı�
     *
     */
    private void setOperationText(String text){
        operationLabel.setText(text);
    }

    /**
     * ��ղ��������ʾ�ı�
     *
     */
    private void clearResultText(){
        operResultLabel.setText("");
    }
    
    /**
     * ���ò��������ʾ�ı�
     * @param text
     */
    private void setResultText(String text){
        operResultLabel.setText(text);
    }
    
    
    
//  �������ݰ�ť��������¼�
    private void sendDataBtnMouseEntered(java.awt.event.MouseEvent evt) {
//        setOperationText("��������");
    }

    //�������ݰ�ť����뿪�¼�
    private void sendDataBtnMouseExited(java.awt.event.MouseEvent evt) {
//        clearOperationText();
    }

    //�򿪻�رմ��ڰ�ť��������¼�
    private void openOrCloseToggleButtonMouseEntered(java.awt.event.MouseEvent evt) {
//        setResultText(openOrCloseToggleButton.getToolTipText());
    }

//  �򿪻�رմ��ڰ�ť����뿪�¼�
    private void openOrCloseToggleButtonMouseExited(java.awt.event.MouseEvent evt) {
//        clearResultText();
    }

    //������ݰ�ť��������¼�
    private void clearButtonMouseEntered(java.awt.event.MouseEvent evt) {
    }

    //������ݰ�ť����뿪�¼�
    private void clearButtonMouseExited(java.awt.event.MouseEvent evt) {
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        portConfigPanel = new javax.swing.JPanel();
        portLabel = new javax.swing.JLabel();
        rateLabel = new javax.swing.JLabel();
        parityBitLabel = new javax.swing.JLabel();
        dataBitLabel = new javax.swing.JLabel();
        stopBitLabel = new javax.swing.JLabel();
        portComboBox = new javax.swing.JComboBox();
        rateComboBox = new javax.swing.JComboBox();
        parityComboBox = new javax.swing.JComboBox();
        dataBitComboBox = new javax.swing.JComboBox();
        stopBitComboBox = new javax.swing.JComboBox();
        openOrCloseToggleButton = new javax.swing.JToggleButton();
        chartPanel = new javax.swing.JPanel();
        dectivePanel = new javax.swing.JPanel();
        detectiveSpinner = new javax.swing.JSpinner();
        dectiveButton = new javax.swing.JButton();
        sendDataPanel = new javax.swing.JPanel();
        sendDataLabel = new javax.swing.JLabel();
        clearButton = new javax.swing.JButton();
        sendDataBtn = new javax.swing.JButton();
        checkHexCheckBox = new javax.swing.JCheckBox();
        inputTextScrollPane = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        operationToolBar = new javax.swing.JToolBar();
        operationLabel = new javax.swing.JLabel();
        operaResultToolBar = new javax.swing.JToolBar();
        operResultLabel = new javax.swing.JLabel();
        anthorToolBar = new javax.swing.JToolBar();
        authorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("��������");
        setName("SerialPortsWindLinesFrame"); // NOI18N
        setResizable(false);

        portConfigPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("��������"));
        portConfigPanel.setToolTipText("");
        portConfigPanel.setName("serialPortsConfigPanel"); // NOI18N

        portLabel.setText("����");
        portLabel.setName("portLabel"); // NOI18N

        rateLabel.setText("������");
        rateLabel.setName("rateLabel"); // NOI18N

        parityBitLabel.setText("У��λ");
        parityBitLabel.setName("parityBitLabel"); // NOI18N

        dataBitLabel.setText("����λ");
        dataBitLabel.setName("dataBitLabel"); // NOI18N

        stopBitLabel.setText("ֹͣλ");
        stopBitLabel.setName("stopBitLabel"); // NOI18N

        portComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4" }));
        portComboBox.setToolTipText("ѡ�񴮿�");
        portComboBox.setName("portComboBox"); // NOI18N

        rateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4800", "9600", "14400", "19200", "38400", "56000", "57600", "115200" }));
        rateComboBox.setSelectedIndex(1);
        rateComboBox.setToolTipText("ѡ������");
        rateComboBox.setName("rateComboBox"); // NOI18N

        parityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONE", "ODD", "EVEN", "MARK", "SPACE" }));
        parityComboBox.setToolTipText("ѡ��У��λ");
        parityComboBox.setName("parityComboBox"); // NOI18N

        dataBitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "6", "7", "8" }));
        dataBitComboBox.setSelectedIndex(3);
        dataBitComboBox.setToolTipText("ѡ������λ");
        dataBitComboBox.setName("dataBitComboBox"); // NOI18N

        stopBitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));
        stopBitComboBox.setToolTipText("ѡ��ֹͣλ");
        stopBitComboBox.setName("stopBitComboBox"); // NOI18N

        openOrCloseToggleButton.setText("�رմ���");
        openOrCloseToggleButton.setToolTipText("�رմ���");
        openOrCloseToggleButton.setName("openOrCloseToggleButton"); // NOI18N
        openOrCloseToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	openOrCloseToggleButtonMouseClicked(evt);
            }
        	public void mouseEntered(java.awt.event.MouseEvent evt) {
                openOrCloseToggleButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openOrCloseToggleButtonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout portConfigPanelLayout = new javax.swing.GroupLayout(portConfigPanel);
        portConfigPanel.setLayout(portConfigPanelLayout);
        portConfigPanelLayout.setHorizontalGroup(
            portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(portConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(portConfigPanelLayout.createSequentialGroup()
                        .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rateLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(parityBitLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dataBitLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(stopBitLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(parityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataBitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stopBitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(openOrCloseToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        portConfigPanelLayout.setVerticalGroup(
            portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(portConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rateLabel))
                .addGap(18, 18, 18)
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parityBitLabel)
                    .addComponent(parityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataBitLabel)
                    .addComponent(dataBitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(portConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stopBitLabel)
                    .addComponent(stopBitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(openOrCloseToggleButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        ChartPanel chartPanel = dynamicChart.createChartpanel();
//        dc.startThread();
        chartPanel.setName("chartPanel"); // NOI18N

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        dectivePanel.setName("dectivePanel"); // NOI18N

        detectiveSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 511, 1));
        detectiveSpinner.setName("detectiveSpinner"); // NOI18N

        dectiveButton.setText("̽��");
        dectiveButton.setName("dectiveButton"); // NOI18N
        dectiveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	dectiveButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout dectivePanelLayout = new javax.swing.GroupLayout(dectivePanel);
        dectivePanel.setLayout(dectivePanelLayout);
        dectivePanelLayout.setHorizontalGroup(
            dectivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dectivePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detectiveSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(dectiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        dectivePanelLayout.setVerticalGroup(
            dectivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dectivePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dectivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detectiveSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dectiveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sendDataPanel.setName("sendDataPanel"); // NOI18N

        sendDataLabel.setBackground(new java.awt.Color(153, 204, 0));
        sendDataLabel.setForeground(new java.awt.Color(51, 255, 51));
        sendDataLabel.setText("���Ϳ�����");
        sendDataLabel.setName("sendDataLabel"); // NOI18N

        clearButton.setText("���");
        clearButton.setToolTipText("�������");
        clearButton.setName("clearButton"); // NOI18N
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButtonMouseExited(evt);
            }
        });

        sendDataBtn.setText("��������");
        sendDataBtn.setToolTipText("��������");
        sendDataBtn.setName("sendDataBtn"); // NOI18N
        sendDataBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendDataBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendDataBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendDataBtnMouseExited(evt);
            }
        });

        checkHexCheckBox.setText("ʮ������");
        checkHexCheckBox.setName("checkHexCheckBox"); // NOI18N

        javax.swing.GroupLayout sendDataPanelLayout = new javax.swing.GroupLayout(sendDataPanel);
        sendDataPanel.setLayout(sendDataPanelLayout);
        sendDataPanelLayout.setHorizontalGroup(
            sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendDataPanelLayout.createSequentialGroup()
                .addGroup(sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkHexCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sendDataPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendDataBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sendDataPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(sendDataLabel)
                        .addGap(25, 25, 25))))
        );
        sendDataPanelLayout.setVerticalGroup(
            sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sendDataPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(sendDataLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sendDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkHexCheckBox)
                    .addComponent(sendDataBtn))
                .addGap(25, 25, 25))
        );

        inputTextScrollPane.setName("inputTextScrollPane"); // NOI18N

        inputTextArea.setColumns(20);
        inputTextArea.setRows(5);
        inputTextArea.setToolTipText("��������");
        inputTextArea.setName("inputTextArea"); // NOI18N
        inputTextScrollPane.setViewportView(inputTextArea);

        operationToolBar.setFloatable(false);
        operationToolBar.setRollover(true);
        operationToolBar.setEnabled(false);
        operationToolBar.setName("operationToolBar"); // NOI18N

        operationLabel.setName("operationLabel"); // NOI18N
        operationLabel.setPreferredSize(new java.awt.Dimension(80, 20));
        operationToolBar.add(operationLabel);

        operaResultToolBar.setFloatable(false);
        operaResultToolBar.setRollover(true);
        operaResultToolBar.setName("operaResultToolBar"); // NOI18N

        operResultLabel.setName("operResultLabel"); // NOI18N
        operaResultToolBar.add(operResultLabel);

        anthorToolBar.setFloatable(false);
        anthorToolBar.setRollover(true);
        anthorToolBar.setToolTipText("QQ:9069602");
        anthorToolBar.setName("anthorToolBar"); // NOI18N

        authorLabel.setText("����֧��:suwg");
        authorLabel.setName("authorLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(operationToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(operaResultToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(authorLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(sendDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputTextScrollPane))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dectivePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(portConfigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anthorToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(portConfigPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dectivePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTextScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sendDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(authorLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(operaResultToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(operationToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(anthorToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JToolBar anthorToolBar;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JCheckBox checkHexCheckBox;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox dataBitComboBox;
    private javax.swing.JLabel dataBitLabel;
    private javax.swing.JButton dectiveButton;
    private javax.swing.JPanel dectivePanel;
    private javax.swing.JSpinner detectiveSpinner;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JScrollPane inputTextScrollPane;
    private javax.swing.JToggleButton openOrCloseToggleButton;
    private javax.swing.JLabel operResultLabel;
    private javax.swing.JToolBar operaResultToolBar;
    private javax.swing.JLabel operationLabel;
    private javax.swing.JToolBar operationToolBar;
    private javax.swing.JLabel parityBitLabel;
    private javax.swing.JComboBox parityComboBox;
    private javax.swing.JComboBox portComboBox;
    private javax.swing.JPanel portConfigPanel;
    private javax.swing.JLabel portLabel;
    private javax.swing.JComboBox rateComboBox;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JButton sendDataBtn;
    private javax.swing.JLabel sendDataLabel;
    private javax.swing.JPanel sendDataPanel;
    private javax.swing.JComboBox stopBitComboBox;
    private javax.swing.JLabel stopBitLabel;
    // End of variables declaration

}
