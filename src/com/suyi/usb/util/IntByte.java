package com.suyi.usb.util;

public class IntByte {
	
	


	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// ���λ
		targets[1] = (byte) ((res >> 8) & 0xff);// �ε�λ
		targets[2] = (byte) ((res >> 16) & 0xff);// �θ�λ
		targets[3] = (byte) (res >>> 24);// ���λ,�޷������ơ�
		return targets;
	}

	public static int byte2int(byte[] res) {
		// һ��byte��������24λ���0x??000000��������8λ���0x00??0000

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | ��ʾ��λ��
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}
	
	
	  /** 
     * @���� ���������ֽڵ�ת�� 
     * @param ������ 
     * @return ��λ���ֽ����� 
     */  
    public static byte[] shortToByte(short number) {  
        int temp = number;  
        byte[] b = new byte[2];  
        for (int i = 0; i < b.length; i++) {  
            b[i] = new Integer(temp & 0xff).byteValue();// �����λ���������λ  
            temp = temp >> 8; // ������8λ  
        }  
        return b;  
    }  
  
    /** 
     * @���� �ֽڵ�ת��������� 
     * @param ��λ���ֽ����� 
     * @return ������ 
     */  
    public static short byteToShort(byte[] b) {  
        short s = 0;  
        short s0 = (short) (b[0] & 0xff);// ���λ  
        short s1 = (short) (b[1] & 0xff);  
        s1 <<= 8;  
        s = (short) (s0 | s1);  
        return s;  
    }  
	  


	
}
