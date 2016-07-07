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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.hitangjun.desk.SerialPortException;
import com.hitangjun.desk.SerialPortsWindLines;
import com.suyi.SuSerialPortLinker;
import com.suyi.usb.util.Constant;
import com.suyi.usb.util.FileUtil;
import com.suyi.usb.util.IntByte;
import com.suyi.usb.util.ProProperty;
import com.suyi.usb.util.StringUtil;
import com.suyi.usb.util.SuLog;

public class USBSwing extends JFrame {

	static int pinontX = 16, pinontY = 16;
	static int pinontSize = pinontX * pinontY;
	static boolean isShowLine = true;// 展示数字标签
	static boolean isShowLeft = true;// 展示左侧
	static boolean isAutoShow = false;// 测试自动刷新
	static boolean isRecodeXY = true;// 自动记录位置
	static boolean isXSetting = false;
	static int logsSize = 5;

	// ===========================设置
	static String settingNameMV = "设置电压";
	static String[] settingStringsMV = new String[] { "min", "lev0", "lev1",
			"lev2", "max" };
	static boolean[] settingChangeSMV = new boolean[] { false, true, true,
			true, true };
	static short[] settingLeaveMV = new short[] { 50 - 1, 200, 600, 800,1000 + 1 };

	// ===========================设置

	static String settingNameTime = "设置时间";
	static String[] settingStringsTime = new String[] { "min", "lev0", "lev1",
			"lev2", "max" };
	static boolean[] settingChangeSTime = new boolean[] { false, true, true,
			true, true };
	static short[] settingLeaveTime = new short[] { 30 - 1, 80, 120, 160,
			200 + 1 };

	// ===========================

	static byte[] bsStart = new byte[] { 0x79, 0x03, 0x11, 0x00, 0x66 };
	static byte[] bsStop = new byte[] { 0x79, 0x03, 0x11, 0x01, 0x66 };
	static byte[] bsClean = new byte[] { 0x79, 0x03, 0x11, 0x02, 0x66 };

	static String[] buttonStrings = new String[] { "开始采集", "停止采集", "图像输出",
			"清除", "退出"
	// ,"调试"
	};

	Color colors[] = new Color[] { Color.decode("#FFFFFF"),
			Color.decode("#C2C2C2"), Color.decode("#636363"),
			Color.decode("#000000"), };
	Color bgColor = Color.decode("#8FBC8F");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");
	SimpleDateFormat sdfNo = new SimpleDateFormat("HH_mm_ss_SSS");
	JComponent bts[][];
	byte[] showBs;
	JButton mJButtons[] = new JButton[buttonStrings.length];
	boolean isShow = true;// 立即
	JTextArea mLog;
	// JTextArea mJTextAreaForLink;
	JButton linkStatusButton;
	JPanel pMain;

	int screenWidth, screenHeight;

	SuSerialPortLinker mSuSerialPortLinker;

	JTextArea[] mJTextAreaForLevsMV = new JTextArea[settingStringsMV.length];
	JTextArea[] mJTextAreaForLevsTime = new JTextArea[settingStringsTime.length];

	JButton buttonSettingMV, buttonSettingTime;

	// =============================设置
	public USBSwing() throws HeadlessException {
		super();

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		screenWidth = screenSize.width; // 获取屏幕的宽
		screenHeight = screenSize.height; // 获取屏幕的高
		this.setTitle("USB");
		setSize();
		initView();
		// this.addWindowStateListener(new WindowStateListener () {
		//
		// public void windowStateChanged(WindowEvent state) {
		//
		// if(state.getNewState() == 1 || state.getNewState() == 7) {
		// System.out.println("窗口最小化");
		// }else if(state.getNewState() == 0) {
		// System.out.println("窗口恢复到初始状态");
		// }else if(state.getNewState() == 6) {
		// System.out.println("窗口最大化");
		// }else{
		// System.out.println(state.toString());
		// }
		// }
		// });
		if (isRecodeXY)
			this.addComponentListener(new ComponentListener() {
				@Override
				public void componentShown(ComponentEvent e) {
					// TODO Auto-generated method stub
					// SuLog.Log("componentShown"+e);
				}

				@Override
				public void componentResized(ComponentEvent e) {
					// TODO Auto-generated method stub
					// SuLog.Log("componentResized " + e);
					// SuLog.Log(e.getComponent().getWidth() + " "
					// + e.getComponent().getHeight());
					try {
						ProProperty.putKeyValue(Constant.suWi,
								String.valueOf(e.getComponent().getWidth()));
						ProProperty.putKeyValue(Constant.suHi,
								String.valueOf(e.getComponent().getHeight()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				@Override
				public void componentMoved(ComponentEvent e) {
					// TODO Auto-generated method stub
					// SuLog.Log("componentMoved " + e);
					// SuLog.Log(e.getComponent().getX() + " "
					// + e.getComponent().getY());
					try {
						ProProperty.putKeyValue(Constant.suX,
								String.valueOf(e.getComponent().getX()));
						ProProperty.putKeyValue(Constant.suY,
								String.valueOf(e.getComponent().getY()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				@Override
				public void componentHidden(ComponentEvent e) {
					// TODO Auto-generated method stub
					// SuLog.Log("componentHidden"+e);
				}
			});

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		setLocation();
		doActione(1);
		if (isAutoShow) {
			new Thread(new Runnable() {
				public void run() {
					byte[] showBs = new byte[pinontSize];
					while (true) {
						for (int i = 0; i < pinontSize; i++) {
							showBs[i] = (byte) (Math.random() * 4);
						}
						setLink(showBs[0] > 2);
						setColors(showBs, false);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		} else {

			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// while (true) {
			// checkLink();
			// try {
			// Thread.sleep(5000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// }
			// }
			// }).start();
		}

	}

	private void setLocation() {
		// TODO Auto-generated method stub
		int w = 0, h = 0;
		if (isRecodeXY) {
			String wi = ProProperty.getKeyValue(Constant.suX);
			String hi = ProProperty.getKeyValue(Constant.suY);
			if (!StringUtil.isEmpty(wi) && !StringUtil.isEmpty(hi)) {
				try {
					w = Integer.parseInt(wi);
					h = Integer.parseInt(hi);
				} catch (Exception e) {
				}
			}
		}
		if (w <= 0 || h <= 0) {
			setLocationRelativeTo(null); // 让窗体居中显示
		} else {
			setLocation(w, h);
		}
	}

	private void setSize() {
		// TODO Auto-generated method stub
		int w = 0, h = 0;
		if (isRecodeXY) {
			String wi = ProProperty.getKeyValue(Constant.suWi);
			String hi = ProProperty.getKeyValue(Constant.suHi);
			if (!StringUtil.isEmpty(wi) && !StringUtil.isEmpty(hi)) {
				try {
					w = Integer.parseInt(wi);
					h = Integer.parseInt(hi);
				} catch (Exception e) {
				}
			}
		}
		if (w == 0) {
			w = screenWidth / 2;
		}
		if (h == 0) {
			h = screenHeight / 2;
		}
		this.setSize(w, h);
	}

	private void checkLink() {
		// TODO Auto-generated method stub
		if (mSuSerialPortLinker == null) {
			mSuSerialPortLinker = new SuSerialPortLinker();
			mSuSerialPortLinker.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					// TODO Auto-generated method stub
					// SuLog.Log(arg.toString());
					if (arg != null) {
						if (arg instanceof byte[]) {

							byte[] bs = (byte[]) arg;
							if (bs == null && bs.length < 5) {
								showLog("数据错误");
								return;
							}
							if (bs[0] != 0x86) {
								showLog("数据不规范");
								return;
							}

							// 1.参数设置：0X79,0X0D,0X10,电压等级1高位数据，电压等级1低位数据，电压等级2高位数据，电压等级2低位数据，电压等级3高位数据，电压等级3低位数据，
							//
							// 主板回码：0X86,0x03,0x10,0X88,0X66.
							//
							// 2.开始采集：0X79,0X03,0X11,0X00,0X66
							//
							// 主板回码：0X86,0x03,0x11,0X00,0X66.
							//
							// 3.停止采集：0X79,0X03,0X11,0x01,0x66.
							//
							// 主板回码：0X86,0x03,0x11,0X01,0X66.
							//
							// 4.清除数据：0X79,0X03,0X11,0x02,0x66.
							//
							// 主板回码：0X86,0x03,0x11,0X02,0X66.
							//

							//
							byte index_type = bs[2];
							switch (index_type) {
							case 0x10:
								showLog("回馈：设置成功");
								break;
							case 0x11:
								byte index_3 = bs[3];

								switch (index_3) {
								case 0X00:
									showLog("回馈：开始采集");
									break;
								case 0X01:
									showLog("回馈：停止采集");
									break;
								case 0X02:
									showLog("回馈：清除");
									break;

								default:
									break;
								}
								showLog("设置成功");
								break;

							case 0x02:
								// 主板 --------> PC软件
								//
								// 1.显示数据：0X86,0X03,0X02,位数据，等级数据（位数据0-255，等级数据1-3）
								//
								// 指令说明：头码 + 长度 + 命令 + 数据1 + ...... 数据n
								int length = bs[1] - 1;
								for (int i = 0; i < length / 2; i++) {
									byte index = bs[2 + i];
									byte value = bs[3 + i];

								}

								break;

							default:
								break;
							}

							setColors((byte[]) arg, false);

						} else if (arg instanceof Boolean) {
							setLink((Boolean) arg);
							sendSetting();
							sendByte(bsStart);
						} else if (arg instanceof String) {
							showLog((String) arg);
						} else {
						}
					}
				}
			});
		}
		
		
		if (!mSuSerialPortLinker.isOpen() && !mSuSerialPortLinker.isOpening()){
			try {
				mSuSerialPortLinker.startOpen();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				showLog(e.getMessage());
				setLink(false);
			}
		}else{
			sendByte(bsStart);
		}
	}

	private void initView() {
		this.add(getEastPanel(), BorderLayout.EAST);
		this.add(getTopPanel(), BorderLayout.NORTH);
		this.add(getMainPanel(), BorderLayout.CENTER);
	}

	public void setLink(boolean is) {
		if (is) {
			linkStatusButton.setBackground(Color.green);
			showLog("链接建立");
			setButtonAble(0, 1, 1, 1, 1);

		} else {
			linkStatusButton.setBackground(Color.red);
			showLog("链接中断");
			setButtonAble(1, 0, 1, 1, 1);
		}
	}

	private Stack<String> logs = new Stack<String>();

	public void showLog(String info) {
		logs.add(info);
		if (logs.size() > logsSize) {
			logs.remove(0);
		}
		String ins = sdfNo.format(new Date()) + "\n"
				+ StringUtil.getStrings(logs);
		if (mLog != null)
			mLog.setText(ins);
		SuLog.Log(ins);
	}

	public byte[] getBytes() {
		return showBs;
	}

	public void setColors(byte[] showBs, boolean isMust) {
		if (!isMust && !isShow)
			return;
		if (showBs.length != pinontSize) {
			showLog("数据出错：" + pinontSize);
			return;
		}

		this.showBs = showBs;
		showLog("获取到新数据");
		int i = 0;
		for (byte b : showBs) {
			bts[i++ / pinontX][i % pinontY].setBackground(colors[b]);
		}
	}

	private Component getMainPanel() {
		pMain = new JPanel(new GridLayout(pinontX, pinontY, 2, 2));
		pMain.setBackground(bgColor);

		bts = new JComponent[pinontX][pinontY]; // 创建按钮数组
		for (int Y = 0; Y < pinontY; Y++) {
			for (int X = 0; X < pinontX; X++) {
				JTextArea mJTextArea = new JTextArea();
				// mJTextArea.setHorizontalAlignment(JTextField.CENTER);
				if (isShowLine) {
					mJTextArea.setText((X + 1) + " " + (Y + 1));
					mJTextArea.setForeground(Color.blue);
					Font font = new Font("宋体", Font.PLAIN, 10);
					mJTextArea.setFont(font);
				}
				bts[X][Y] = mJTextArea;
			}
		}

		for (int Y = pinontY - 1; Y >= 0; Y--) {
			for (int X = 0; X < pinontX; X++) {
				pMain.add(bts[X][Y]);// 添加早在上面
			}
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

		linkStatusButton = new CircleButton("");
		linkStatusButton.setPreferredSize(new Dimension(50, 50));
		// linkStatusButton.setMargin(new Insets(100,100,100,100));
		linkStatusButton.setBackground(Color.red);
		eastPanel.add(linkStatusButton, BorderLayout.EAST);

		return eastPanel;
	}

	private JPanel getEastPanel() {
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(bgColor);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		// ==================
		eastPanel.add(Box.createVerticalStrut(5));
		// ================== 横向设置lev
		{
			
			JPanel	mMainSettingPanel=null;
			if(isXSetting){
//			横方式
			 mMainSettingPanel = new JPanel();
			mMainSettingPanel.setLayout(new BoxLayout(mMainSettingPanel,
					BoxLayout.X_AXIS));
			}else{
			mMainSettingPanel=eastPanel;
			}
			
			
			// =========// 设置mv
			{
				JPanel settingPanel = new JPanel();
				settingPanel.setLayout(new BoxLayout(settingPanel,
						BoxLayout.Y_AXIS));

				Label mLabel = new Label();
				mLabel.setText("设置级别(电压mv)：");
				settingPanel.add(mLabel);

				for (int i = 0; i < settingStringsMV.length; i++) {
					Color color = null;
					if (settingChangeSMV[i] == true) {
						color = colors[i - 1];
					}
					JPanel mPanel0 = getLineMV(settingStringsMV[i], i,
							settingChangeSMV[i], color);
					settingPanel.add(mPanel0);
				}
				getSettingInfoMV();
				setSettingViewMV();

				buttonSettingMV = new JButton();
				buttonSettingMV.setText(settingNameMV);
				buttonSettingMV.addActionListener(new ActionButton(
						settingNameMV));
				settingPanel.add(buttonSettingMV);

				mMainSettingPanel.add(settingPanel);
			}

			// ==================// 设置time
			{
				JPanel settingPanel = new JPanel();
				settingPanel.setLayout(new BoxLayout(settingPanel,
						BoxLayout.Y_AXIS));
				Label mLabel = new Label();
				mLabel.setText("设置级别(时间ms)：");
				settingPanel.add(mLabel);

				for (int i = 0; i < settingStringsTime.length; i++) {
					Color color = null;
					if (settingChangeSTime[i] == true) {
						color = colors[i - 1];
					}
					JPanel mPanel0 = getLineTime(settingStringsTime[i], i,
							settingChangeSTime[i], color);
					settingPanel.add(mPanel0);
				}
				getSettingInfoTime();
				setSettingViewTime();

				buttonSettingTime = new JButton();
				buttonSettingTime.setText(settingNameTime);
				buttonSettingTime.addActionListener(new ActionButton(
						settingNameTime));
				settingPanel.add(buttonSettingTime);

				mMainSettingPanel.add(settingPanel);

			}
//			横方式
			if(isXSetting){
			eastPanel.add(mMainSettingPanel);
			}
		}

		// ==================
		eastPanel.add(Box.createVerticalStrut(5));

		if (isShowLeft) {// 开发日志
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
			Label mLabel = new Label();
			mLabel.setText("日志：");
			topPanel.add(mLabel);

			mLog = new JTextArea(10, 1);
			mLog.setLineWrap(true);// 激活自动换行功能
			mLog.setWrapStyleWord(true);// 激活断行不断字功能
			Font font = new Font("宋体", Font.PLAIN, 10);
			mLog.setFont(font);

			topPanel.add(mLog);

			Label mLabelC = new Label();
			mLabelC.setText("操作：");
			topPanel.add(mLabelC);

			eastPanel.add(topPanel);
			eastPanel.add(Box.createVerticalStrut(5));
		}

		// ==================// mainbutton
		{
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

			// eastPanel.add(busPanel, BorderLayout.CENTER);
			eastPanel.add(busPanel);
		}
		eastPanel.add(Box.createVerticalStrut(5));
		return eastPanel;
	}

	private void setSettingViewMV() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mJTextAreaForLevsMV.length; i++) {
			mJTextAreaForLevsMV[i].setText("" + settingLeaveMV[i]);
		}
	}

	private void setSettingViewTime() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mJTextAreaForLevsTime.length; i++) {
			mJTextAreaForLevsTime[i].setText("" + settingLeaveTime[i]);
		}
	}

	private JPanel getLineMV(String title, int i, boolean isChange, Color colors) {
		// TODO Auto-generated method stub
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.X_AXIS));

		Label mLabelnull = new Label();
		mLabelnull.setText(" ");
		mLabelnull.setBackground(colors);
		settingPanel.add(mLabelnull);

		Label mLabel = new Label();
		mLabel.setText(title);
		settingPanel.add(mLabel);

		JTextArea mLog = new JTextArea();

		mJTextAreaForLevsMV[i] = mLog;
		if (isChange) {
			mLog.setBackground(Color.white);
		} else {
			mLog.setBackground(Color.gray);
			mLog.setEditable(false);
		}
		settingPanel.add(mLog);
		return settingPanel;
	}

	private JPanel getLineTime(String title, int i, boolean isChange,
			Color colors) {
		// TODO Auto-generated method stub
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.X_AXIS));

		Label mLabelnull = new Label();
		mLabelnull.setText(" ");
		// mLabelnull.setBackground(colors);
		settingPanel.add(mLabelnull);

		Label mLabel = new Label();
		mLabel.setText(title);
		settingPanel.add(mLabel);

		JTextArea mLog = new JTextArea();
		mJTextAreaForLevsTime[i] = mLog;
		if (isChange) {
			mLog.setBackground(Color.white);
		} else {
			mLog.setBackground(Color.gray);
			mLog.setEditable(false);
		}
		settingPanel.add(mLog);
		return settingPanel;
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
			showLog(name);
			if (name.equals(settingNameMV)) {
				index = 100;
				doActione(index);
				return;
			}
			if (name.equals(settingNameTime)) {
				index = 101;
				doActione(index);
				return;
			}

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
			showLog(index < buttonStrings.length ? buttonStrings[index] : "");
			isShow = true;
			setButtonAble(0, 1, 1, 1, 1);
			if (isAutoShow) {
			} else {
				checkLink();
			}
			break;
		case 1:
			isShow = false;
			setButtonAble(1, 0, 1, 1, 1);
			if (!isAutoShow && mSuSerialPortLinker != null
					&& mSuSerialPortLinker.isOpen()) {
				
				sendByte(bsStop);
//				try {
////					mSuSerialPortLinker.close();
//				} catch (SerialPortException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				mSuSerialPortLinker = null;
			}

			break;
		case 2:
			boolean is = isShow;
			isShow = false;
			Point p = pMain.getLocationOnScreen();
			Rectangle m = pMain.getBounds();
			try {
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(new Rectangle(
						(int) p.getX(), (int) p.getY(), (int) (m.getWidth()),
						(int) (m.getHeight())));
				String name = saveImage(image);
				if (!StringUtil.isEmpty(name)) {
					String names = name.split("/.")[0] + ".txt";
					FileUtil.save(names, showBs, pinontX);
				}
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (is) {
				isShow = true;
			}
			break;
		case 3:
			// isShow=false;
			byte[] showBs = new byte[pinontSize];
			setColors(showBs, true);
			logs.clear();
			sendByte(bsClean);
			showLog(index < buttonStrings.length ? buttonStrings[index]
					: "未知操作");
			break;
		case 4:
			System.exit(1);
			break;
		case 5:
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new SerialPortsWindLines(false).setVisible(true);
				}
			});
			break;

		case 100: {
			boolean isChangeSetting = false;
			short gets[] = new short[mJTextAreaForLevsMV.length];
			for (int i = 0; i < mJTextAreaForLevsMV.length; i++) {
				String info = mJTextAreaForLevsMV[i].getText();
				try {
					short pa = Short.parseShort(info);
					if (pa < settingLeaveMV[0]
							|| pa > settingLeaveMV[settingLeaveMV.length - 1]) {
						showSettingErr("设置越界:" + settingLeaveMV[0] + "to"
								+ settingLeaveMV[settingLeaveMV.length - 1]);
						setSettingViewMV();
						return;
					}
					gets[i] = pa;
				} catch (Exception e) {
					e.printStackTrace();
					showSettingErr("设置异常");
					setSettingViewMV();
					return;
				}
			}

			int old = -1;
			for (int i : gets) {
				if (i <= old) {
					showSettingErr("设置异常" + i + "必须>" + old);
					setSettingViewMV();
					return;
				}
				old = i;
			}

			for (int i = 1; i < mJTextAreaForLevsMV.length - 1; i++) {
				if (gets[i] != settingLeaveMV[i]) {
					isChangeSetting = true;
					showLog(settingStringsMV[i] + ":" + settingLeaveMV[i]
							+ "-->" + gets[i]);
					settingLeaveMV[i] = gets[i];
				}
			}

			if (isChangeSetting) {
				saveSettingInfoMV();
				sendSetting();
			} else {
				showLog("未变化");
			}
			buttonSettingMV.setBackground(null);
		}
			break;

		case 101: {
			boolean isChangeSetting = false;
			short gets[] = new short[mJTextAreaForLevsTime.length];
			for (int i = 0; i < mJTextAreaForLevsTime.length; i++) {
				String info = mJTextAreaForLevsTime[i].getText();
				try {
					short pa = Short.parseShort(info);
					if (pa < settingLeaveTime[0]
							|| pa > settingLeaveTime[settingLeaveTime.length - 1]) {
						showSettingErr("设置越界:" + settingLeaveTime[0] + "to"
								+ settingLeaveTime[settingLeaveTime.length - 1]);
						setSettingViewTime();
						return;
					}
					gets[i] = pa;
				} catch (Exception e) {
					e.printStackTrace();
					showSettingErr("设置异常");
					setSettingViewTime();
					return;
				}
			}

			int old = -1;
			for (int i : gets) {
				if (i <= old) {
					showSettingErr("设置异常" + i + "必须>" + old);
					setSettingViewTime();
					return;
				}
				old = i;
			}

			for (int i = 1; i < mJTextAreaForLevsTime.length - 1; i++) {
				if (gets[i] != settingLeaveTime[i]) {
					isChangeSetting = true;
					showLog(settingStringsTime[i] + ":" + settingLeaveTime[i]
							+ "-->" + gets[i]);
					settingLeaveTime[i] = gets[i];
				}
			}

			if (isChangeSetting) {
				saveSettingInfoTime();
				sendSetting();
			} else {
				showLog("未变化");
			}
			buttonSettingTime.setBackground(null);
		}
			break;

		default:
			break;
		}

	}

	private void sendSetting() {
		// TODO Auto-generated method stub

		try {
			int toshortOrInt = 2;// 遍short
			byte[] bs = new byte[((settingLeaveMV.length - 2) + (settingLeaveTime.length - 2))
					* toshortOrInt];
			int index = 0;
			for (int i = 1; i < settingLeaveMV.length - 2; i++) {
				byte[] ints = IntByte.shortToByte(settingLeaveMV[i]);
				System.arraycopy(ints, 0, bs, index, ints.length);
				index += ints.length;
			}

			for (int i = 1; i < settingLeaveTime.length - 2; i++) {
				byte[] ints = IntByte.shortToByte(settingLeaveTime[i]);
				System.arraycopy(ints, 0, bs, index, ints.length);
				index += ints.length;
			}

			// 1.参数设置：0X79,0X0D,0X10,电压等级1高位数据，电压等级1低位数据，电压等级2高位数据，电压等级2低位数据，电压等级3高位数据，电压等级3低位数据，
			// 时间等级1高位数据，时间等级1低位数据，时间等级2高位数据，时间等级2低位数据，时间等级3高位数据，时间等级3低位数据。
			// 主板回码：0X86,0x03,0x10,0X88,0X66.

			byte[] bsall = new byte[bs.length + 3];
			bsall[0] = (byte) 0x79;
			bsall[1] = (byte) 0x0D;
			bsall[02] = (byte) 0x10;
			System.arraycopy(bs, 0, bsall, 3, bs.length);
			sendByte(bsall);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showLog("错误：" + e.getMessage());
		}

	}

	private void sendByte(byte[] bsall) {
		// TODO Auto-generated method stub
		if (mSuSerialPortLinker != null) {
			try {
				mSuSerialPortLinker.send(bsall);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showLog("发送失败" + e.getMessage());
			}
		} else {
			showLog("链接不存在");
		}
	}

	private void saveSettingInfoMV() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 4; i++) {
			try {
				ProProperty.putKeyValue(Constant.settingLev_MV + "" + i,
						settingLeaveMV[i] + "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void saveSettingInfoTime() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 4; i++) {
			try {
				ProProperty.putKeyValue(Constant.settingLev_Time + "" + i,
						settingLeaveTime[i] + "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void getSettingInfoMV() {
		for (int i = 1; i <= settingLeaveMV.length - 1; i++) {
			try {
				String info = ProProperty.getKeyValue(Constant.settingLev_MV
						+ "" + i);
				if (!StringUtil.isEmpty(info)) {
					short in = Short.parseShort(info);
					settingLeaveMV[i] = in;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void getSettingInfoTime() {
		for (int i = 1; i <= settingLeaveTime.length - 1; i++) {
			try {
				String info = ProProperty.getKeyValue(Constant.settingLev_Time
						+ "" + i);
				if (!StringUtil.isEmpty(info)) {
					short in = Short.parseShort(info);
					settingLeaveTime[i] = in;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showSettingErr(String string) {
		// TODO Auto-generated method stub
		showLog(string);
		buttonSettingMV.setBackground(Color.red);
	}

	public void setButtonAble(int i, int j, int k, int l, int m) {
		mJButtons[0].setEnabled(i == 1);
		mJButtons[1].setEnabled(j == 1);
		mJButtons[2].setEnabled(k == 1);
		mJButtons[3].setEnabled(l == 1);
		mJButtons[4].setEnabled(m == 1);
	}

	// 保存图像到文件
	public String saveImage(BufferedImage image) throws IOException {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("保存");
		// 文件过滤器，用户过滤可选择文件
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG",
				"jpg");
		jfc.setFileFilter(filter);

		// 初始化一个默认文件（此文件会生成到桌面上）
		String fileName = sdf.format(new Date());

		String dir = ProProperty.getKeyValue(Constant.fileDir);
		File filePath = null;
		if (!StringUtil.isEmpty(dir)) {
			File filePath1 = new File(dir);
			if (filePath1.exists() && filePath1.isDirectory()) {
				filePath = filePath1;
			}
		}
		if (filePath == null)
			filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		File defaultFile = new File(filePath + File.separator + fileName
				+ ".jpg");
		jfc.setSelectedFile(defaultFile);

		int flag = jfc.showSaveDialog(this);
		if (flag == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			if (file.getParentFile() != defaultFile.getParentFile()) {
				ProProperty.putKeyValue(Constant.fileDir, file.getParentFile()
						.getAbsolutePath());
			}
			String path = file.getPath();
			// 检查文件后缀，放置用户忘记输入后缀或者输入不正确的后缀
			if (!(path.endsWith(".jpg") || path.endsWith(".JPG"))) {
				path += ".jpg";
			}
			// 写入文件
			ImageIO.write(image, "jpg", new File(path));
			return path;
		}

		return null;

		// jfc.

	}

}
