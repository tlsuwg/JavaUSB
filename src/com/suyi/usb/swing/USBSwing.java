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
import java.util.HashMap;
import java.util.Map;
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
	static int logsSize = 5;

	String[] buttonStrings = new String[] { "开始采集", "停止采集", "图像输出", "清除", "退出"
	// ,"调试"
	};
	String dangqian = "当前：";
	String morenIndex="X1";
	Color colors[] = new Color[] { Color.decode("#FFFFFF"),
			Color.decode("#C2C2C2"), Color.decode("#636363"),
			Color.decode("#000000"), };
	Color bgColor = Color.decode("#8FBC8F");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");
	SimpleDateFormat sdfNo = new SimpleDateFormat("HH_mm_ss_SSS");
	JComponent mButtons[][];// 数据区域
	byte[] showBs;
	JButton mJButtons[] = new JButton[buttonStrings.length];
	boolean isShow = true;// 立即
	JTextArea mLog;
	// JTextArea mJTextAreaForLink;
	JButton linkStatusButton;
	JPanel pMain;

	int screenWidth, screenHeight;

	SuSerialPortLinker mSuSerialPortLinker;

	Map<String, int[]> leveMap = new HashMap<String, int[]>();

	// ===========================设置
	String settingName = "设置";
	String[] settingStrings = new String[] { "min", "lev0", "lev1", "lev2",
			"lev3", "max" };
	boolean[] settingChangeS = new boolean[] { false, true, true, true, true,
			false };
	int[] settingLeave = new int[] { 0, 1000, 2000, 4000, 5000, 6000 };
	JTextArea[] mJTextAreaForLevs = new JTextArea[settingStrings.length];// 当前
	JButton buttonSetting;

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
							setColors((byte[]) arg, false);
						} else if (arg instanceof Boolean) {
							setLink((Boolean) arg);
						} else if (arg instanceof String) {
							showLog((String) arg);
						} else {
						}
					}
				}
			});
		}
		if (!mSuSerialPortLinker.isOpen() && !mSuSerialPortLinker.isOpening())
			try {
				mSuSerialPortLinker.startOpen();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				showLog(e.getMessage());
				setLink(false);
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
			sendSetting();
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
			mButtons[i++ / pinontX + 1][i % pinontY + 1]
					.setBackground(colors[b]);
		}
	}

	private Component getMainPanel() {
		pMain = new JPanel(new GridLayout(pinontX + 1, pinontY + 1, 2, 2));
		pMain.setBackground(bgColor);

		mButtons = new JComponent[pinontX + 1][pinontY + 1]; // 创建按钮数组
		for (int Y = 0; Y < pinontY + 1; Y++) {
			for (int X = 0; X < pinontX + 1; X++) {

				JComponent mJComponent = null;
				// mJTextArea.setHorizontalAlignment(JTextField.CENTER);
				if (isShowLine) {
					Font font = null;
					if (X == 0 || Y == 0) {
						// mJTextArea.setBackground(Color.blue);
						JButton mJButton = new JButton();
						String t = (X == 0 ? "Y" + (Y) : "X" + (X));
						if (X == 0 && Y == 0) {
							t = "(00)";
						} else {
							mJButton.addActionListener(new SettingActionButton(
									t));
						}
						mJButton.setText(t);
						// font = new Font("宋体", Font.PLAIN, 13);

						mJComponent = mJButton;
						// mJComponent.setFont(font);
					} else {
						JTextArea mJTextArea = new JTextArea();
						font = new Font("宋体", Font.PLAIN, 10);
						mJTextArea.setText((X) + " " + (Y));
						mJTextArea.setForeground(Color.blue);
						mJComponent = mJTextArea;
						mJComponent.setFont(font);
					}

				}
				mButtons[X][Y] = mJComponent;
			}
		}

		for (int Y = pinontY + 1 - 1; Y >= 0; Y--) {// 全部添加上去
			for (int X = 0; X < pinontX + 1; X++) {
				pMain.add(mButtons[X][Y]);// 添加早在上面
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

	Label mLabelXY;

	private JPanel getEastPanel() {

		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(bgColor);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		eastPanel.add(Box.createVerticalStrut(5));
		{// 设置
			JPanel settingPanel = new JPanel();
			settingPanel
					.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
			Label mLabel = new Label();
			mLabel.setText("设置级别(mv)：");
			mLabelXY = new Label();
			mLabelXY.setText(dangqian + morenIndex);
			settingPanel.add(mLabel);
			settingPanel.add(mLabelXY);

			for (int i = 0; i < settingStrings.length; i++) {
				Color color = null;
				if (settingChangeS[i] == true) {
					color = colors[i - 1];
				}
				JPanel mPanel0 = getLine(settingStrings[i], i,
						settingChangeS[i], color);
				settingPanel.add(mPanel0);
			}

			for (int i = 1; i < pinontX; i++) {
				getSettingInfoXY("X" + i);
			}
			for (int i = 1; i < pinontY; i++) {
				getSettingInfoXY("Y" + i);
			}

			getSettingInfoXY(morenIndex);
			setLeveSettingView();

			buttonSetting = new JButton();
			buttonSetting.setText(settingName);
			buttonSetting.addActionListener(new ActionButton(settingName));
			settingPanel.add(buttonSetting);

			eastPanel.add(settingPanel);
		}
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

		{// mainbutton
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

	private void setLeveSettingView() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mJTextAreaForLevs.length; i++) {
			mJTextAreaForLevs[i].setText("" + settingLeave[i]);
		}
	}

	private JPanel getLine(String title, int i, boolean isChange, Color colors) {
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
		mJTextAreaForLevs[i] = mLog;
		if (isChange) {
			mLog.setBackground(Color.white);

		} else {
			mLog.setBackground(Color.gray);
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
			if (name.equals(settingName)) {
				index = 100;
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

	public class SettingActionButton implements ActionListener {
		String name;

		public SettingActionButton(String name) {
			// TODO Auto-generated constructor stub
			super();
			this.name = name;
		}

		public void actionPerformed(ActionEvent e) {
			String indexS = null;
			mLabelXY.setText(dangqian + name);
			getSettingInfoXY(name);
			setLeveSettingView();

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
				try {
					mSuSerialPortLinker.close();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		case 100:
			boolean isChangeSetting = false;
			int gets[] = new int[mJTextAreaForLevs.length];

			for (int i = 0; i < mJTextAreaForLevs.length; i++) {
				String info = mJTextAreaForLevs[i].getText();
				try {
					int pa = Integer.parseInt(info);
					if (pa < settingLeave[0]
							|| pa > settingLeave[settingLeave.length - 1]) {
						showSettingErr("设置越界:" + settingLeave[0] + "to"
								+ settingLeave[settingLeave.length - 1]);
						setLeveSettingView();
						return;
					}
					gets[i] = pa;
				} catch (Exception e) {
					e.printStackTrace();
					showSettingErr("设置异常");
					setLeveSettingView();
					return;
				}
			}

			int old = -1;
			for (int i : gets) {
				if (i <= old) {
					showSettingErr("设置异常" + i + "必须>" + old);
					setLeveSettingView();
					return;
				}
				old = i;
			}

			for (int i = 1; i < mJTextAreaForLevs.length - 1; i++) {
				if (gets[i] != settingLeave[i]) {
					isChangeSetting = true;
					showLog(settingStrings[i] + ":" + settingLeave[i] + "-->"
							+ gets[i]);
					settingLeave[i] = gets[i];
				}
			}

			if (isChangeSetting) {
				saveSettingInfo(XYLe);
				sendSetting();
			} else {
				showLog("未变化");
			}
			buttonSetting.setBackground(null);
			break;
		default:
			break;
		}

	}

	private void sendSetting() {
		// TODO Auto-generated method stub
		if (mSuSerialPortLinker != null) {
			try {
				byte[] bs = new byte[(settingLeave.length - 2) * 4];
				for (int i = 1; i < settingLeave.length - 2; i++) {
					byte[] ints = IntByte.int2byte(settingLeave[i]);
					System.arraycopy(ints, 0, bs, (i - 1) * 4, ints.length);
				}
				mSuSerialPortLinker.send(bs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void saveSettingInfo(String name) {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 4; i++) {
			try {
				ProProperty.putKeyValue(Constant.settingLev + name +"_"+ i,
						settingLeave[i] + "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	String XYLe;
	void getSettingInfoXY(String name) {
		
		if (leveMap.containsKey(name)) {
			settingLeave= leveMap.get(name);
		}else{
			for (int i = 1; i <= 4; i++) {
				try {
					String info = ProProperty.getKeyValue(Constant.settingLev + name +"_"+ i);
					if (!StringUtil.isEmpty(info)) {
						int in = Integer.parseInt(info);
						settingLeave[i] = in;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			leveMap.put(name, settingLeave);
		}
		XYLe=name;
	}

	private void showSettingErr(String string) {
		// TODO Auto-generated method stub
		showLog(string);
		buttonSetting.setBackground(Color.red);
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
