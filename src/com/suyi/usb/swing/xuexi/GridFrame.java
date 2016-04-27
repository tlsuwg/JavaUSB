package com.suyi.usb.swing.xuexi;

import java.awt.*;

import javax.swing.*;

class GridFrame extends JFrame {

	// 定义面板，并设置为网格布局，4行4列，组件水平、垂直间距均为3

	JPanel p = new JPanel(new GridLayout(4, 4, 3, 3));

	JTextArea t = new JTextArea(); // 定义文本框

	// 定义字符串数组，为按钮的显示文本赋值

	// 注意字符元素的顺序与循环添加按钮保持一致

	String str[] = { "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3",
			"-", "0", ".", "=", "+" };

	public GridFrame(String s) {

		super(s); // 为窗体名称赋值

		setLayout(new BorderLayout()); // 定义窗体布局为边界布局

		JButton btn[]; // 声明按钮数组

		btn = new JButton[str.length]; // 创建按钮数组

		// 循环定义按钮，并添加到面板中

		for (int i = 0; i < str.length; i++) {

			btn[i] = new JButton(str[i]);

			p.add(btn[i]);

		}

		// 将文本框放置在窗体NORTH位置

		getContentPane().add(t, BorderLayout.NORTH);

		// 将面板放置在窗体CENTER位置

		getContentPane().add(p, BorderLayout.CENTER);

		setVisible(true);

		setSize(250, 200);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocationRelativeTo(null); // 让窗体居中显示

	}

	public static void main(String[] args) {

		GridFrame gl = new GridFrame("网格布局计算机！");

	}

}
