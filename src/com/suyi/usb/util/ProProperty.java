package com.suyi.usb.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author
 * @version
 */
public class ProProperty {

	// �����ļ���·��
	static String profilepath = "UsbSetting.properties";
	/**
	 * ���þ�̬����
	 */
	private static Properties props = new Properties();
	static {
		try {
			props.load(new FileInputStream(profilepath));
		} catch (FileNotFoundException e) {
			Log.LogE(e);
		} catch (IOException e) {
			Log.LogE(e);
		}
	}

	/**
	 * ��ȡ�����ļ�����Ӧ����ֵ
	 * 
	 * @param key
	 *            ����
	 * @return String
	 */
	public static String getKeyValue(String key) {
		return props.getProperty(key);
	}

	public static void putKeyValue(String key, String value) throws IOException {
		// props.put(key, value);
		OutputStream fos = new FileOutputStream(profilepath);
		props.setProperty(key, value);
		props.store(fos, "Update '" + key + "' value");
	}

	public static void showAll() {
		Enumeration enum1 = props.propertyNames();// �õ������ļ�������
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = props.getProperty(strKey);

			Log.Log(strKey + "=" + strValue);
		}
	}

	// /**
	// * ��������key��ȡ������ֵvalue
	// * @param filePath �����ļ�·��
	// * @param key ����
	// */
	// public static String readValue(String filePath, String key) {
	// Properties props = new Properties();
	// try {
	// InputStream in = new BufferedInputStream(new FileInputStream(
	// filePath));
	// props.load(in);
	// String value = props.getProperty(key);
	// System.out.println(key +"����ֵ�ǣ�"+ value);
	// return value;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * ���£�����룩һ��properties��Ϣ(���������ֵ) ����������Ѿ����ڣ����¸�������ֵ�� ��������������ڣ�����һ�Լ�ֵ��
	 * 
	 * @param keyname
	 *            ����
	 * @param keyvalue
	 *            ��ֵ
	 */
	// public static void writeProperties(String keyname,String keyvalue) {
	// try {
	// // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
	// // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
	// OutputStream fos = new FileOutputStream(profilepath);
	// props.setProperty(keyname, keyvalue);
	// // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
	// // ���� Properties ���е������б�����Ԫ�ضԣ�д�������
	// props.store(fos, "Update '" + keyname + "' value");
	// } catch (IOException e) {
	// System.err.println("�����ļ����´���");
	// }
	// }
	//
	// /**
	// * ����properties�ļ��ļ�ֵ��
	// * ����������Ѿ����ڣ����¸�������ֵ��
	// * ��������������ڣ�����һ�Լ�ֵ��
	// * @param keyname ����
	// * @param keyvalue ��ֵ
	// */
	// public void updateProperties(String keyname,String keyvalue) {
	// try {
	// props.load(new FileInputStream(profilepath));
	// // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
	// // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
	// OutputStream fos = new FileOutputStream(profilepath);
	// props.setProperty(keyname, keyvalue);
	// // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
	// // ���� Properties ���е������б�����Ԫ�ضԣ�д�������
	// props.store(fos, "Update '" + keyname + "' value");
	// } catch (IOException e) {
	// System.err.println("�����ļ����´���");
	// }
	// }
	// ���Դ���
	public static void main(String[] args) {
		// readValue("UsbSetting.properties", "MAIL_SERVER_PASSWORD");
		// writeProperties("MAIL_SERVER_INCOMING", "327@qq.com");
		// System.out.println("�������");
		try {
			ProProperty.putKeyValue("key22sss", "value22");
			ProProperty.showAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
