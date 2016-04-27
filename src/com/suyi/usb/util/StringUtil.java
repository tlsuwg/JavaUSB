package com.suyi.usb.util;

public class StringUtil {

	public static boolean isEmpty(String info){
		if(info==null||"".equals(info)||info.length()==0)return true;
		return false;
	}
}
