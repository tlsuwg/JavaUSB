package com.suyi.usb.swing.xuexi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * BorderLayout��ĳЩ���������Ĭ�ϲ��ֹ�����������������ΪNORTH, SOUTH,CENTER��WEST��EAST�������ÿ���������ֻ����һ�������������Ҳֻ������������������٣�������5�����Ĳ��ֹ���ʹ�÷�ʽ��
Container.add��component, BorderLayout.SOUTH��;
����ڶ�������δָ������ôĬ����BorderLayout.CENTER������ʾ��
�����ͬһ����������˶���������ôֻ��ʾ�����ӵ������
 * @author HZ20232
 *
 */
public class TestBorderLayout extends JFrame{
    private static final long serialVersionUID = 6819222900970457455L;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    public TestBorderLayout(){
        this.setSize(600,400);
        this.setTitle("����");
        init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    private void init(){
        button1 = new JButton("NORTH");
        button2 = new JButton("SOUTH");
        button3 = new JButton("EAST");
        button4 = new JButton("WEST");
        button5 = new JButton("CENTER");
        this.setLayout(new BorderLayout());
        this.add(button1,BorderLayout.NORTH);
        this.add(button2,BorderLayout.SOUTH);
        this.add(button3,BorderLayout.EAST);
        this.add(button4,BorderLayout.WEST);
        this.add(button5,BorderLayout.CENTER);
    }
    public static void main(String args[]){
        TestBorderLayout test = new TestBorderLayout();
    }
}