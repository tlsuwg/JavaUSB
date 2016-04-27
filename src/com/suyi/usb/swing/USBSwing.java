package com.suyi.usb.swing;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.suyi.usb.util.Constant;
import com.suyi.usb.util.Log;
import com.suyi.usb.util.ProProperty;
import com.suyi.usb.util.StringUtil;

public class USBSwing extends JFrame {

	static int pinontSize = 256;
	static boolean isShowLine = true;
	static boolean isShowLeft = false;

	String[] buttonStrings = new String[] { "开始采集", "停止采集", "图像输出", "清除", "退出" };

	Color colors[] = new Color[] { Color.decode("#FFFFFF"),
			Color.decode("#C2C2C2"), Color.decode("#636363"),
			Color.decode("#000000"), };
	Color bgColor = Color.decode("#8FBC8F");

	JComponent bts[];
	byte[] bs;
	JButton mJButtons[] = new JButton[buttonStrings.length];
	boolean isShow = true;
	JTextArea mLog;
	// JTextArea mJTextAreaForLink;
	JButton button;
	JPanel pMain;

	public void showLog(String info) {
		if (mLog != null)
			mLog.setText(info);
		Log.Log(info);
	}

	public byte[] getBytes() {
		return bs;
	}

	public USBSwing() throws HeadlessException {
		super();
		this.setSize(800, 600);
		this.setTitle("USB");
		initView();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		setLocationRelativeTo(null); // 让窗体居中显示
	}

	private void initView() {

		this.add(getEastPanel(), BorderLayout.EAST);
		this.add(getTopPanel(), BorderLayout.NORTH);
		this.add(getMainPanel(), BorderLayout.CENTER);
		doActione(0);
	}

	public void setColors(byte[] bs, boolean isMust) {
		if (!isMust && !isShow)
			return;
		if (bs.length != pinontSize) {
			showLog("数据不足：" + pinontSize);
			return;
		}

		this.bs = bs;
		int i = 0;
		for (byte b : bs) {
			bts[i++].setBackground(colors[b]);
		}
	}

	private Component getMainPanel() {
		pMain = new JPanel(new GridLayout(16, 16, 2, 2));
		pMain.setBackground(bgColor);

		bts = new JComponent[pinontSize]; // 创建按钮数组
		for (int i = 1; i <= pinontSize; i++) {
			JTextArea mJTextArea = new JTextArea();
			// mJTextArea.setHorizontalAlignment(JTextField.CENTER);
			if (isShowLine) {
				mJTextArea.setText(pinontSize - i + 1 + "");
				mJTextArea.setForeground(Color.blue);
				Font font = new Font("宋体", Font.PLAIN, 10);
				mJTextArea.setFont(font);
			}
			bts[i - 1] = mJTextArea;
			pMain.add(mJTextArea);
		}
		return pMain;
	}

	private JPanel getTopPanel() {
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout()); // 定义窗体布局为边界布局
		eastPanel.setBackground(bgColor);

		// eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		// eastPanel.add(Box.createVerticalStrut(5));

		final JTextField mJTextArea = new JTextField(2);
		Font font = new Font("宋体", Font.PLAIN, 25);
		mJTextArea.setFont(font);
		mJTextArea.setHorizontalAlignment(JTextField.CENTER);
		mJTextArea.setText("test");
		eastPanel.add(mJTextArea, BorderLayout.CENTER);

		String info = ProProperty.getKeyValue(Constant.topTitle);

		if (!StringUtil.isEmpty(info)) {
			mJTextArea.setText(info);
		}

		mJTextArea.getDocument().addDocumentListener(new DocumentListener() {
			private void keepE(DocumentEvent e) {
				try {

					String info = mJTextArea.getText();
					// /e.getDocument().getText(e.getOffset(),
					// e.getLength());
					// Log.Log(info);
					if (!StringUtil.isEmpty(info)) {
						ProProperty.putKeyValue(Constant.topTitle, info);
					} else {
						ProProperty.putKeyValue(Constant.topTitle, "");
					}
				} catch (Exception xe) {
				}
			}

			public void removeUpdate(DocumentEvent e) {
				keepE(e);
			}

			public void insertUpdate(DocumentEvent e) {
				keepE(e);
			}

			public void changedUpdate(DocumentEvent e) {
				keepE(e);
			}
		});

		// mJTextAreaForLink = new JTextArea(1, 1);
		// // mJTextArea.setHorizontalAlignment(JTextField.CENTER);
		// // mJTextArea2.setText("ss");
		// mJTextAreaForLink.setBackground(Color.red);
		// mJTextAreaForLink.setMargin(new Insets(10, 10, 10, 10));
		// eastPanel.add(mJTextAreaForLink, BorderLayout.EAST);

		button = new CircleButton("");
		button.setBackground(Color.orange);
		eastPanel.add(button, BorderLayout.EAST);

		return eastPanel;
	}

	public void setLink(boolean is) {
		if (is) {
			button.setBackground(Color.green);
		} else {
			button.setBackground(Color.red);
		}
	}

	private JPanel getEastPanel() {

		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(bgColor);
		eastPanel.setLayout(new BorderLayout()); // 定义窗体布局为边界布局

		if (isShowLeft) {
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
			Label mLabel = new Label();
			mLabel.setText("日志：");
			topPanel.add(mLabel);

			mLog = new JTextArea(10, 1);
			mLog.setLineWrap(true);// 激活自动换行功能
			mLog.setWrapStyleWord(true);// 激活断行不断字功能

			topPanel.add(mLog);

			Label mLabelC = new Label();
			mLabelC.setText("操作：");
			topPanel.add(mLabelC);

			eastPanel.add(topPanel, BorderLayout.NORTH);
		}

		JPanel busPanel = new JPanel();
		// busPanel.setBackground(Color.red);

		int i = 0;
		for (String name : buttonStrings) {
			JButton button1 = new JButton();
			button1.setText(name);
			button1.setToolTipText(name);
			button1.addActionListener(new ActionButton(name));
			mJButtons[i++] = button1;
			busPanel.add(button1);
		}

		busPanel.setLayout(new GridLayout(i, 1, 2, 2));

		eastPanel.add(busPanel, BorderLayout.CENTER);
		return eastPanel;
	}

	public class ActionButton implements ActionListener {
		String name;

		public ActionButton(String name) {
			// TODO Auto-generated constructor stub
			super();
			this.name = name;
		}

		public void actionPerformed(ActionEvent e) {
			int index = 0;
			for (int i = 0; i < buttonStrings.length; i++) {
				if (name.equals(buttonStrings[i])) {
					index = i;
					doActione(index);
					break;
				}
			}
		}
	}

	private void doActione(int index) {

		switch (index) {
		case 0:
			isShow = true;
			setButtonAble(0, 1, 1, 1, 1);
			break;
		case 1:
			isShow = false;
			setButtonAble(1, 0, 1, 1, 1);
			break;
		case 2:
			boolean is = isShow;
			isShow = false;

			int windowWidth = this.getWidth(); // 获得窗口宽
			int windowHeight = this.getHeight(); // 获得窗口高
			Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
			Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
			int screenWidth = screenSize.width; // 获取屏幕的宽
			int screenHeight = screenSize.height; // 获取屏幕的高
			
			System.out.println(windowWidth +"  "+windowHeight+" "+screenWidth+" "+screenHeight);
			
			// this.setLocation(screenWidth/2-windowWidth/2,
			// screenHeight/2-windowHeight/2);//设置窗口居中显示

			
			double x = pMain.getWidth();
			double y = pMain.getHeight();
			Log.Log(x + "  " + y);

			// 截取屏幕
			try {
				Robot robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BufferedImage
			// image = robot.createScreenCapture(new Rectangle(0, 0, d.width,
			// d.height));

			if (is) {
				isShow = true;
			}
			break;
		case 3:
			// isShow=false;
			byte[] bs = new byte[pinontSize];
			setColors(bs, true);
			break;
		case 4:
			System.exit(1);
			break;
		default:
			break;
		}
	}

	public void setButtonAble(int i, int j, int k, int l, int m) {
		mJButtons[0].setEnabled(i == 1);
		mJButtons[1].setEnabled(j == 1);
		mJButtons[2].setEnabled(k == 1);
		mJButtons[3].setEnabled(l == 1);
		mJButtons[4].setEnabled(m == 1);
	}

	public static void main(String[] a) {
		final USBSwing mUSBSwing = new USBSwing();

		new Thread(new Runnable() {
			public void run() {

				byte[] bs = new byte[pinontSize];
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < pinontSize; i++) {
						bs[i] = (byte) (Math.random() * 4);
					}

					mUSBSwing.setLink(bs[0] > 2);
					mUSBSwing.setColors(bs, false);
				}
			}
		}).start();
	}

}
