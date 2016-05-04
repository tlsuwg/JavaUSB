package com.suyi.usb.util;

import java.util.Stack;

public class StringUtil {

	public static boolean isEmpty(String info){
		if(info==null||"".equals(info)||info.length()==0)return true;
		return false;
	}

	public static String getStrings(Stack<String> logs) {
		// TODO Auto-generated method stub
		if(logs!=null&&logs.size()>0){
			StringBuffer sb=new StringBuffer();
			
			for(int i=logs.size()-1;i>=0;i--){
				sb.append(logs.get(i)).append("\n");
			}
			
			return sb.toString();
		}
		return "";
	}
}
