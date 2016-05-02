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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
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

import com.suyi.usb.util.Constant;
import com.suyi.usb.util.Log;
import com.suyi.usb.util.ProProperty;
import com.suyi.usb.util.StringUtil;

public class USBSwing extends JFrame {

	static int pinontX=16,pinontY = 16;
	static int pinontSize = pinontX*pinontY;
	static boolean isShowLine = true;
	static boolean isShowLeft = false;

	String[] buttonStrings = new String[] { "开始采集", "停止采集", "图像输出", "清除", "退出" };

	Color colors[] = new Color[] { Color.decode("#FFFFFF"),
			Color.decode("#C2C2C2"), Color.decode("#636363"),
			Color.decode("#000000"), };
	Color bgColor = Color.decode("#8FBC8F");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");

	JComponent bts[][];
	byte[] bs;
	JButton mJButtons[] = new JButton[buttonStrings.length];
	boolean isShow = true;
	JTextArea mLog;
	// JTextArea mJTextAreaForLink;
	JButton button;
	JPanel pMain;
	int screenWidth, screenHeight;

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

		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		screenWidth = screenSize.width; // 获取屏幕的宽
		screenHeight = screenSize.height; // 获取屏幕的高
		this.setSize(screenWidth / 2, screenHeight / 2);
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
			bts[i++/pinontX][i%pinontY].setBackground(colors[b]);
		}
	}

	private Component getMainPanel() {
		pMain = new JPanel(new GridLayout(pinontX, pinontY, 2, 2));
		pMain.setBackground(bgColor);

		bts = new JComponent[pinontX][pinontY]; // 创建按钮数组
		for (int Y = 0; Y <pinontY; Y++) {
			for (int X = 0; X < pinontX; X++) {
			JTextArea mJTextArea = new JTextArea();
			// mJTextArea.setHorizontalAlignment(JTextField.CENTER);
			if (isShowLine) {
				mJTextArea.setText((X+1)+" "+(Y+1));
				mJTextArea.setForeground(Color.blue);
				Font font = new Font("宋体", Font.PLAIN, 10);
				mJTextArea.setFont(font);
			}
			bts[X][Y] = mJTextArea;
			}
		}
		
		for (int Y = pinontY-1; Y >=0; Y--) {
			for (int X = 0; X < pinontX; X++) {
				pMain.add(bts[X][Y]);//添加早在上面
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

		button = new CircleButton("");
		button.setBackground(Color.red);
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
			Point p = pMain.getLocationOnScreen();
			Rectangle m = pMain.getBounds();
			try {
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(new Rectangle(
						(int) p.getX(), (int) p.getY(), (int) (m.getWidth()),
						(int) (m.getHeight())));
				String name=saveImage(image);
				if(!StringUtil.isEmpty(name)){
				String names=name.split("/.")[0]+".txt";
				FileUtil.save(names,bs);
				}
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
