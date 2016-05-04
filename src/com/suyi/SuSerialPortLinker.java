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
 * �������ݶ�ȡ��,����windows�Ĵ������ݶ�ȡ
 * 
 */
public class SuSerialPortLinker extends Observable {
	
	CommPortIdentifier portId;
	int delayRead = 150;
	int numBytes; // buffer�е�ʵ�������ֽ���
	private static byte[] readBuffer = new byte[4096]; // 4k��buffer�ռ�,���洮�ڶ��������
	InputStream inputStream;
	SerialPort serialPort;
	// �˿��Ƿ����
	volatile boolean isOpen = false;
	volatile boolean isOpening = false;

	// �˿ڶ��������¼�������,�ȴ�n������ٶ�ȡ,�Ա�������һ���Զ���

	public boolean isOpen() {
		return isOpen;
	}
	
	private void setIsOpen(boolean b) {
		// TODO Auto-generated method stub
		this.isOpen=b;
		notifyObservers(isOpen);
	}


	/**
	 * ��ʼ���˿ڲ����Ĳ���.
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
				notifyObservers("��ѯ��com�˿�:"+mset.size());
				boolean is = false;
				Iterator<CommPortIdentifier> it = mset.iterator();
				while (it.hasNext()) {
					CommPortIdentifier com = it.next();
					try {
						notifyObservers("��������"+com.getName());
						open(1000, com.getName(), 9600, 8, 1, "NONE", 150);
						is = true;
						break;
					} catch (SerialPortException e) {
						continue;
					}
				}
				if (!is) {
					throw new Exception("û�в�ѯ������ʹ�õĶ˿�");
				}
			} else {
				throw new Exception("û�в�ѯ������ʹ�õĶ˿�");
			}
		} finally {
			isOpening = false;
		}
	}

	// �豸��ʱʱ�� 1��
	// �˿�����
	// ������
	// ����λ
	// ֹͣλ
	// ����żУ��
	// �˿�����׼��ʱ�� 1��

	public void open(int timeout, String port, int rate, int dataBits,
			int stopBits, String parityStr, int delayRead)
			throws SerialPortException {

		// String paramsMsg =
		// "�򿪴��� "+port+","+rate+","+parity+","+dataBit+","+stopBit;
		// �򿪴��� COM1,9600,NONE,8,1

		int parityInt = SuPortManger.getParityValue(parityStr);

		if (isOpen) {
			close();
		}
		try {
			this.delayRead = delayRead;
			// �򿪶˿�
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
			throw new SerialPortException("�˿�" + port + "�Ѿ���ռ��");
		} catch (TooManyListenersException e) {
			throw new SerialPortException("�˿�" + port + "�����߹���");
		} catch (UnsupportedCommOperationException e) {
			throw new SerialPortException("�˿ڲ������֧��");
		} catch (NoSuchPortException e) {
			throw new SerialPortException("�˿�" + port + "������");
		} catch (IOException e) {
			throw new SerialPortException("�򿪶˿�" + port + "ʧ��");
		} catch (Exception e) {
			throw new SerialPortException("ʧ��" + e.getMessage());
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
				throw new SerialPortException("�رմ���ʧ��");
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
			// �ȴ�1�����ô��ڰ�����ȫ�����պ��ڴ���
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
				// ��ζ�ȡ,���������ݶ���
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
				}
				// ��ӡ���յ����ֽ����ݵ�ASCII��
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

	// ͨ��observer pattern���յ������ݷ��͸�observer
	// ��buffer�еĿ��ֽ�ɾ�����ٷ��͸�����Ϣ,֪ͨ�۲���
	public void changeMessage(byte[] message, int length) {
		setChanged();
		byte[] temp = new byte[length];
		System.arraycopy(message, 0, temp, 0, length);
		notifyObservers(temp);
	}

}
