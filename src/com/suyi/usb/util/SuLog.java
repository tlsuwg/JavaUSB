package com.suyi.usb.util;

public class SuLog {
	
	public static void Log(String info){
		if(Constant.isDev)
		System.out.println("Suwg:"+info);
	}
	public static void LogE(String info){
		if(Constant.isDev)
		System.err.println("Suwg:"+info);
	}
	public static void LogE(Exception e) {
		// TODO Auto-generated method stub
		if(Constant.isDev)
		e.printStackTrace();
		
	}

	
}
