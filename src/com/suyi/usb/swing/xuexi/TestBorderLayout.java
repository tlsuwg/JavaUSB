package com.suyi.usb.swing.xuexi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * BorderLayout是某些容器组件的默认布局管理器，它将容器分为NORTH, SOUTH,CENTER，WEST，EAST五个区域，每个区域最多只能有一个组件，所以它也只适用于容器内组件较少（不大于5个）的布局管理。使用方式：
Container.add（component, BorderLayout.SOUTH）;
如果第二个参数未指定，那么默认在BorderLayout.CENTER区域显示。
如果对同一个区域添加了多个组件，那么只显示最后添加的组件。
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
        this.setTitle("测试");
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