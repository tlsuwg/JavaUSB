package com.suyi;

/**
 * @author John
 * @since Jul 30, 2010
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Observable;

import com.hitangjun.desk.Log;

/**
 * �������ݶ�ȡ��,����windows�Ĵ������ݶ�ȡ
 * 
 * @author Macro Lu
 * @version 2007-4-4
 */
public class SuPortManger  {

	public static void listPorts() {//ֻ�ǻ�ȡ
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = (CommPortIdentifier) portEnum
					.nextElement();
			Log.debug(portIdentifier.getName() + " - "
					+ getPortTypeName(portIdentifier.getPortType()));
		}
	}
	
	  //ת��Ԥ�����żУ��λ��ֵ
	public static int getParityValue(String parity){
    	 if("NONE".equals(parity)){
      	   return SerialPort.PARITY_NONE;
         }else if("ODD".equals(parity)){
        	 return SerialPort.PARITY_ODD;
         }else if("EVEN".equals(parity)){
        	 return SerialPort.PARITY_EVEN;
         }else if("MARK".equals(parity)){
        	 return SerialPort.PARITY_MARK;
         }else if("SPACE".equals(parity)){
        	 return SerialPort.PARITY_SPACE;
         }else{
        	 return SerialPort.PARITY_NONE;
         }
    }

	public static String getPortTypeName(int portType) {
		switch (portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

	/**
	 * @return A HashSet containing the CommPortIdentifier for all serial ports
	 *         that are not currently being used.
	 */
	public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {//�����ܹ�����
		HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
		Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
		while (thePorts.hasMoreElements()) {
			CommPortIdentifier com = (CommPortIdentifier) thePorts
					.nextElement();
			switch (com.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				try {
					CommPort thePort = com.open("CommUtil", 50);
					thePort.close();
					h.add(com);
					System.out
							.println("Port, " + com.getName() + ", test open ok ");
				} catch (PortInUseException e) {
					System.out.println("Port, " + com.getName()
							+ ", is in use.");
				} catch (Exception e) {
					System.out.println("Failed to open port " + com.getName()
							+ e);
				}
			}
		}
		return h;
	}

	public static void main(String args[]) {
		listPorts();

		getAvailableSerialPorts();
	}
	
	
}
