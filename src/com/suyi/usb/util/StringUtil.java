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
			// 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			// 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt(b & 0x0f));
			sb.append("__");
		}

		return sb.toString();
	}

	public static String get64(byte b) {
		final String HEX = "0123456789abcdef";
		StringBuilder sb = new StringBuilder();
		// 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
		sb.append(HEX.charAt((b >> 4) & 0x0f));
		// 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数

		sb.append(HEX.charAt(b & 0x0f));

		return sb.toString();
	}

}
