package com.suyi.usb.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 制作一个圆形的按钮时，需要做两件事： 第一件事是重载一个适当的绘画方法以画出一个圆形。
 * 第二件事是设置一些事件使得只有当你点击圆形按钮的范围中的时侯按钮才会作出响应
 */
public class CircleButton extends JButton {

	public CircleButton(String label) {
		super(label);

		// 获取按钮的最佳大小
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		setContentAreaFilled(false);
	}

	// 画圆的按钮的背景和标签
	@Override
	protected void paintComponent(Graphics g) {

		if (getModel().isArmed()) {
			g.setColor(Color.lightGray); // 点击时高亮
		} else {
			g.setColor(getBackground());
		}
		// fillOval方法画一个矩形的内切椭圆，并且填充这个椭圆，
		// 当矩形为正方形时，画出的椭圆便是圆
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		super.paintComponent(g);
	}

	// 用简单的弧画按钮的边界。
	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(Color.white);
		// drawOval方法画矩形的内切椭圆，但不填充。只画出一个边界
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	// shape对象用于保存按钮的形状，有助于侦听点击按钮事件
	Shape shape;

	@Override
	public boolean contains(int x, int y) {

		if ((shape == null) || (!shape.getBounds().equals(getBounds()))) {
			// 构造一个椭圆形对象
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		// 判断鼠标的x、y坐标是否落在按钮形状内。
		return shape.contains(x, y);
	}

	public static void main(String[] args) {
		JButton button = new CircleButton("");
		button.setBackground(Color.orange);

		JFrame frame = new JFrame("圆形按钮");
		frame.getContentPane().setBackground(Color.pink);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(button);
		frame.setSize(200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}