package com.suyi.usb.util;

import java.io.FileNotFoundException;

public class Log {
	
	public static void Log(String info){
		if(Constant.isDev)
		System.out.println(info);
	}
	public static void LogE(String info){
		if(Constant.isDev)
		System.err.println(info);
	}
	public static void LogE(Exception e) {
		// TODO Auto-generated method stub
		if(Constant.isDev)
		e.printStackTrace();
		
	}

	
}
