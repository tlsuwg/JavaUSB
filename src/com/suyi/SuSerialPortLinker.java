package com.suyi;

/**
 * @author John
 * @since Jul 30, 2010
 */
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.TooManyListenersException;

import com.hitangjun.desk.Log;
import com.hitangjun.desk.SerialPortException;
import com.hitangjun.desk.SerialPortReader;

/**
 * 串口数据读取类,用于windows的串口数据读取
 * 
 */
public class SuSerialPortLinker extends Observable {
	
	CommPortIdentifier portId;
	int delayRead = 150;
	int numBytes; // buffer中的实际数据字节数
	private static byte[] readBuffer = new byte[4096]; // 4k的buffer空间,缓存串口读入的数据
	InputStream inputStream;
	SerialPort serialPort;
	// 端口是否打开了
	volatile boolean isOpen = false;
	volatile boolean isOpening = false;

	// 端口读入数据事件触发后,等待n毫秒后再读取,以便让数据一次性读完

	public boolean isOpen() {
		return isOpen;
	}
	
	private void setIsOpen(boolean b) {
		// TODO Auto-generated method stub
		this.isOpen=b;
		notifyObservers(isOpen);
	}


	/**
	 * 初始化端口操作的参数.
	 * 
	 * @throws SerialPortException
	 * @see
	 */
	public SuSerialPortLinker() {
		super();
		setIsOpen(false);
	}

	public boolean isOpening() {
		// TODO Auto-generated method stub
		return isOpening;
	}

	public void startOpen() throws Exception {
		// TODO Auto-generated method stub
		isOpening = true;
		try {
			HashSet<CommPortIdentifier> mset = SuPortManger
					.getAvailableSerialPorts();
			if (mset != null) {
				notifyObservers("查询到com端口:"+mset.size());
				boolean is = false;
				Iterator<CommPortIdentifier> it = mset.iterator();
				while (it.hasNext()) {
					CommPortIdentifier com = it.next();
					try {
						notifyObservers("尝试链接"+com.getName());
						open(1000, com.getName(), 9600, 8, 1, "NONE", 150);
						is = true;
						break;
					} catch (SerialPortException e) {
						continue;
					}
				}
				if (!is) {
					throw new Exception("没有查询到可以使用的端口");
				}
			} else {
				throw new Exception("没有查询到可以使用的端口");
			}
		} finally {
			isOpening = false;
		}
	}

	// 设备超时时间 1秒
	// 端口名称
	// 波特率
	// 数据位
	// 停止位
	// 无奇偶校验
	// 端口数据准备时间 1秒

	public void open(int timeout, String port, int rate, int dataBits,
			int stopBits, String parityStr, int delayRead)
			throws SerialPortException {

		// String paramsMsg =
		// "打开串口 "+port+","+rate+","+parity+","+dataBit+","+stopBit;
		// 打开串口 COM1,9600,NONE,8,1

		int parityInt = SuPortManger.getParityValue(parityStr);

		if (isOpen) {
			close();
		}
		try {
			this.delayRead = delayRead;
			// 打开端口
			portId = CommPortIdentifier.getPortIdentifier(port);
			serialPort = (SerialPort) portId.open("SerialPortLinker", timeout);
			inputStream = serialPort.getInputStream();
			serialPort.addEventListener(new SerialPortEventListener() {
				@Override
				public void serialEvent(SerialPortEvent ev) {
					// TODO Auto-generated method stub
					getSerialEvent(ev);
				}
			});
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(rate, dataBits, stopBits, parityInt);
			setIsOpen(true);

		} catch (PortInUseException e) {
			throw new SerialPortException("端口" + port + "已经被占用");
		} catch (TooManyListenersException e) {
			throw new SerialPortException("端口" + port + "监听者过多");
		} catch (UnsupportedCommOperationException e) {
			throw new SerialPortException("端口操作命令不支持");
		} catch (NoSuchPortException e) {
			throw new SerialPortException("端口" + port + "不存在");
		} catch (IOException e) {
			throw new SerialPortException("打开端口" + port + "失败");
		} catch (Exception e) {
			throw new SerialPortException("失败" + e.getMessage());
			// System.exit(0);
		}

	}

	public void close() throws SerialPortException {
		if (isOpen) {
			try {
				serialPort.notifyOnDataAvailable(false);
				serialPort.removeEventListener();
				inputStream.close();
				serialPort.close();
				isOpen = false;
			} catch (IOException ex) {
				throw new SerialPortException("关闭串口失败");
			}
			setIsOpen( false);
		}
	}

	
	/**
	 * Method declaration
	 * 
	 * @param event
	 * @see
	 */
	public void getSerialEvent(SerialPortEvent event) {
		try {
			// 等待1秒钟让串口把数据全部接收后在处理
			Thread.sleep(delayRead);
			Log.debug("serialEvent[" + event.getEventType() + "]    ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (event.getEventType()) {
		case SerialPortEvent.BI: // 10
		case SerialPortEvent.OE: // 7
		case SerialPortEvent.FE: // 9
		case SerialPortEvent.PE: // 8
		case SerialPortEvent.CD: // 6
		case SerialPortEvent.CTS: // 3
		case SerialPortEvent.DSR: // 4
		case SerialPortEvent.RI: // 5
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
			break;
		case SerialPortEvent.DATA_AVAILABLE: // 1
			try {
				// 多次读取,将所有数据读入
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
				}
				// 打印接收到的字节数据的ASCII码
				for (int i = 0; i < numBytes; i++) {
					System.out.println("msg[" + numBytes + "]: ["
							+ readBuffer[i] + "]:" + (char) readBuffer[i]);
				}
				// numBytes = inputStream.read( readBuffer );
				changeMessage(readBuffer, numBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	// 通过observer pattern将收到的数据发送给observer
	// 将buffer中的空字节删除后再发送更新消息,通知观察者
	public void changeMessage(byte[] message, int length) {
		setChanged();
		byte[] temp = new byte[length];
		System.arraycopy(message, 0, temp, 0, length);
		notifyObservers(temp);
	}

}
