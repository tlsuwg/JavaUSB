package com.suyi.usb.swing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.suyi.usb.util.Log;

public class FileUtil {

	public static void save(String names, byte[] bs) throws IOException {
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

		BufferedWriter writeBuf = new BufferedWriter(new FileWriter(f));
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(b + "").append(",");
		}
		Log.Log(sb.toString());
		writeBuf.write(sb.toString());
		writeBuf.flush();
		writeBuf.close();

	}

}
