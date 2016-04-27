package com.suyi.usb.swing.xuexi;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

 

public class Outer extends JFrame implements ActionListener{
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 public static final int WIDTH = 300;
 public static final int HEIGTH = 200;
 private JPanel redPanel;
 private JPanel whitePanel;
 private JPanel bluePanel;
 
 public static void main(String []args)
 { 
  Outer gui = new Outer();
  gui.setVisible(true);
 }
 public Outer()
 {
  super("Panel Demonstration");
  this.setSize(WIDTH,HEIGTH);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setLayout(new BorderLayout());
  
  JPanel biggerPanel = new JPanel();
  biggerPanel.setLayout(new GridLayout(1,3));
  
  redPanel = new JPanel();
  redPanel.setBackground(Color.LIGHT_GRAY);
  biggerPanel.add(redPanel);
  
  whitePanel = new JPanel();
  whitePanel.setBackground(Color.LIGHT_GRAY);
  biggerPanel.add(whitePanel);
  
  bluePanel = new JPanel();
  bluePanel.setBackground(Color.LIGHT_GRAY);
  biggerPanel.add(bluePanel);
  
  this.add(biggerPanel,BorderLayout.CENTER);
  JPanel buttonPanel = new JPanel();
  buttonPanel.setBackground(Color.LIGHT_GRAY);
  buttonPanel.setLayout(new FlowLayout());
  
  JButton redButton = new JButton("Red");
  redButton.setBackground(Color.RED);
  redButton.addActionListener(this);
  buttonPanel.add(redButton);
  
  JButton whiteButton = new JButton("White");
  whiteButton.setBackground(Color.WHITE);
  whiteButton.addActionListener(this);
  buttonPanel.add(whiteButton);
  
  JButton blueButton = new JButton("Blue");
  blueButton.setBackground(Color.BLUE);
  blueButton.addActionListener(this);
  buttonPanel.add(blueButton);
  
  this.add(buttonPanel,BorderLayout.SOUTH);
 }
 public void actionPerformed(ActionEvent e)
 {
  String str = e.getActionCommand();
  
  if(str.equals("Red"))
   redPanel.setBackground(Color.RED);
  else if(str.equals("White"))
   whitePanel.setBackground(Color.WHITE);
  else if(str.equals("Blue"))
   bluePanel.setBackground(Color.BLUE);
 }
}