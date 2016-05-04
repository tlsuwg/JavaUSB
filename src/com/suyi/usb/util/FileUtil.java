package com.suyi.usb.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileUtil {

	public static void save(String names, byte[] bs, int pinontX) throws IOException {
		// TODO Auto-generated method stub
		if (bs == null || bs.length == 0)
			return;

		File f = new File(names);
		File p = f.getParentFile();
		if (p.exists() && p.isDirectory()) {
			p.mkdirs();
		}
		f.delete();
		f.createNewFile();
		
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
				f), "gbk");
		
		StringBuffer sb = new StringBuffer();
		int k=0;
		for (byte b : bs) {
			sb.append(b + "").append(",");
			if(k++==pinontX-1){
				sb.append("\n\r");
				k=0;
			}
		}
		SuLog.Log(sb.toString());

		
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(sb.toString()+"\r\t ");
		writer.flush();
		writer.close();

	}

}
