package com.suyi.usb.swing.xuexi;
/**
* @author ����ΰ�� ��΢����http://weibo.com/taoandtao
* @date ��2012/12/10
* @place�����ݴ�ѧ�������ѧԺ
*/
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
public class MyFrame extends javax.swing.JFrame {
    public static void main(String[] args) {
        MyFrame f = new MyFrame();
    }

    JLabel label1;
    JLabel label2;
    JLabel label3;
    JTextField tf;
    JPasswordField psf;
    JRadioButton rb1;
    JRadioButton rb2;

    JButton bt1;
    JButton bt2;

    public MyFrame() {
        this.setVisible(true);
        this.setSize(250, 220);
        this.setVisible(true);
        this.setLocation(400, 200);

        label1 = new JLabel("����BBS��ݵ�½");
        label2 = new JLabel("�˺ţ�");
        label3 = new JLabel("���룺");
        tf = new JTextField();
        psf = new JPasswordField();
        rb1 = new JRadioButton("��ס����");
        rb2 = new JRadioButton("�Զ���½");
        bt1 = new JButton("��½");
        // Ϊָ���� Container ���� GroupLayout
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        //����GroupLayout��ˮƽ�����飬��Խ�ȼ����ParallelGroup�����ȼ�����Խ�ߡ�
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGap(5);//��Ӽ��
        hGroup.addGroup(layout.createParallelGroup().addComponent(label2)
                .addComponent(label3));
        hGroup.addGap(5);
        hGroup.addGroup(layout.createParallelGroup().addComponent(label1)
                .addComponent(psf).addComponent(rb1).addComponent(rb2)
                .addComponent(tf).addComponent(bt1));
        hGroup.addGap(5);
        layout.setHorizontalGroup(hGroup);
        //����GroupLayout�Ĵ�ֱ�����飬��Խ�ȼ����ParallelGroup�����ȼ�����Խ�ߡ�
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGap(10);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label1));
        vGroup.addGap(10);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label2)
                .addComponent(tf));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label3)
                .addComponent(psf));
        vGroup.addGroup(layout.createParallelGroup().addComponent(rb1));

        vGroup.addGroup(layout.createParallelGroup().addComponent(rb2));
        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
                .addComponent(bt1));
        vGroup.addGap(10);
        //���ô�ֱ��
        layout.setVerticalGroup(vGroup);
    }
}