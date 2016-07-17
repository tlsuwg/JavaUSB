package com.suyi.usb.util;

import java.util.Stack;

public class StringUtil {

	public static boolean isEmpty(String info) {
		if (info == null || "".equals(info) || info.length() == 0)
			return true;
		return false;
	}

	public static String getStrings(Stack<String> logs) {
		// TODO Auto-generated method stub
		if (logs != null && logs.size() > 0) {
			StringBuffer sb = new StringBuffer();

			for (int i = logs.size() - 1; i >= 0; i--) {
				sb.append(logs.get(i)).append("\n");
			}

			return sb.toString();
		}
		return "";
	}

	public static String getByte(byte[] bytes) {
		final String HEX = "0123456789abcdef";
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			// ȡ������ֽڵĸ�4λ��Ȼ����0x0f�����㣬�õ�һ��0-15֮������ݣ�ͨ��HEX.charAt(0-15)��Ϊ16������
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			// ȡ������ֽڵĵ�λ����0x0f�����㣬�õ�һ��0-15֮������ݣ�ͨ��HEX.charAt(0-15)��Ϊ16������
			sb.append(HEX.charAt(b & 0x0f));
			sb.append("__");
		}

		return sb.toString();
	}

	public static String get64(byte b) {
		final String HEX = "0123456789abcdef";
		StringBuilder sb = new StringBuilder();
		// ȡ������ֽڵĸ�4λ��Ȼ����0x0f�����㣬�õ�һ��0-15֮������ݣ�ͨ��HEX.charAt(0-15)��Ϊ16������
		sb.append(HEX.charAt((b >> 4) & 0x0f));
		// ȡ������ֽڵĵ�λ����0x0f�����㣬�õ�һ��0-15֮������ݣ�ͨ��HEX.charAt(0-15)��Ϊ16������

		sb.append(HEX.charAt(b & 0x0f));

		return sb.toString();
	}

}
