
package com.suyi;

/**
 * @author John
 * @since Jul 30, 2010
 */
import gnu.io.CommPort;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.TooManyListenersException;

import com.hitangjun.desk.Log;
import com.hitangjun.desk.SerialPortException;


/**
 * �������ݶ�ȡ��,����windows�Ĵ������ݶ�ȡ
 * 
 * @author Macro Lu
 * @version 2007-4-4
 */
public class SuSerialPortLinker extends Observable 
{
    static CommPortIdentifier portId;
    int delayRead = 200;
    int numBytes; // buffer�е�ʵ�������ֽ���
    private static byte[] readBuffer = new byte[4096]; // 4k��buffer�ռ�,���洮�ڶ��������
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    HashMap serialParams;
    //�˿��Ƿ����
    boolean isOpen = false;
    // �˿ڶ��������¼�������,�ȴ�n������ٶ�ȡ,�Ա�������һ���Զ���
    public static final String PARAMS_DELAY = "delay read"; // ��ʱ�ȴ��˿�����׼����ʱ��
    public static final String PARAMS_TIMEOUT = "timeout"; // ��ʱʱ��
    public static final String PARAMS_PORT = "port name"; // �˿�����
    public static final String PARAMS_DATABITS = "data bits"; // ����λ
    public static final String PARAMS_STOPBITS = "stop bits"; // ֹͣλ
    public static final String PARAMS_PARITY = "parity"; // ��żУ��
    public static final String PARAMS_RATE = "rate"; // ������

    public boolean isOpen(){
    	return isOpen;
    }
    /**
     * ��ʼ���˿ڲ����Ĳ���.
     * @throws SerialPortException 
     * 
     * @see
     */
    public SuSerialPortLinker()
    {
    	isOpen = false;
    }

    public void open(HashMap params) throws SerialPortException 
    {
    	serialParams = params;
    	if(isOpen){
    		close();
    	}
        try
        {
            // ������ʼ��
            int timeout = Integer.parseInt( serialParams.get( PARAMS_TIMEOUT )
                .toString() );
            int rate = Integer.parseInt( serialParams.get( PARAMS_RATE )
                .toString() );
            int dataBits = Integer.parseInt( serialParams.get( PARAMS_DATABITS )
                .toString() );
            int stopBits = Integer.parseInt( serialParams.get( PARAMS_STOPBITS )
                .toString() );
            int parity = Integer.parseInt( serialParams.get( PARAMS_PARITY )
                .toString() );
            delayRead = Integer.parseInt( serialParams.get( PARAMS_DELAY )
                .toString() );
            String port = serialParams.get( PARAMS_PORT ).toString();
            
            // �򿪶˿�
            portId = CommPortIdentifier.getPortIdentifier( port );
            serialPort = ( SerialPort ) portId.open( "SerialPortLinker", timeout );
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener( new SerialPortEventListener() {
				@Override
				public void serialEvent(SerialPortEvent ev) {
					// TODO Auto-generated method stub
					serialEvent2(ev);
				}
			} );
            serialPort.notifyOnDataAvailable( true );
            serialPort.setSerialPortParams( rate, dataBits, stopBits, parity );
            
            isOpen = true;
        }
        catch ( PortInUseException e )
        {
            throw new SerialPortException( "�˿�"+serialParams.get( PARAMS_PORT ).toString()+"�Ѿ���ռ��");
        }
        catch ( TooManyListenersException e )
        {
            throw new SerialPortException( "�˿�"+serialParams.get( PARAMS_PORT ).toString()+"�����߹���");
        }
        catch ( UnsupportedCommOperationException e )
        {
            throw new SerialPortException( "�˿ڲ������֧��");
        }
        catch ( NoSuchPortException e )
        {
            throw new SerialPortException( "�˿�"+serialParams.get( PARAMS_PORT ).toString()+"������");
        }
        catch ( IOException e )
        {
            throw new SerialPortException( "�򿪶˿�"+serialParams.get( PARAMS_PORT ).toString()+"ʧ��");
        }
        catch ( Exception e )
        {
            throw new SerialPortException( "ʧ��"+e.getMessage());
//            System.exit(0);
        }
      
    }

   

    public void close() throws SerialPortException
    {
        if (isOpen)
        {
            try
            {
            	serialPort.notifyOnDataAvailable(false);
            	serialPort.removeEventListener();
                inputStream.close();
                serialPort.close();
                isOpen = false;
            } catch (IOException ex)
            {
            	throw new SerialPortException("�رմ���ʧ��");
            }
        }
    }
    
    /**
     * Method declaration
     * 
     * @param event
     * @see
     */
    public void serialEvent2( SerialPortEvent event )
    {
        try
        {
            // �ȴ�1�����ô��ڰ�����ȫ�����պ��ڴ���
            Thread.sleep( delayRead );
            Log.debug( "serialEvent[" + event.getEventType() + "]    " );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
        switch ( event.getEventType() )
        {
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
                try
                {
                    // ��ζ�ȡ,���������ݶ���
                     while (inputStream.available() > 0) {
                     numBytes = inputStream.read(readBuffer);
                     }
                     
                     //��ӡ���յ����ֽ����ݵ�ASCII��
                     for(int i=0;i<numBytes;i++){
                    	 System.out.println("msg[" + numBytes + "]: [" +readBuffer[i] + "]:"+(char)readBuffer[i]);
                     }
//                    numBytes = inputStream.read( readBuffer );
                    changeMessage( readBuffer, numBytes );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    // ͨ��observer pattern���յ������ݷ��͸�observer
    // ��buffer�еĿ��ֽ�ɾ�����ٷ��͸�����Ϣ,֪ͨ�۲���
    public void changeMessage( byte[] message, int length )
    {
        setChanged();
        byte[] temp = new byte[length];
        System.arraycopy( message, 0, temp, 0, length );
        notifyObservers( temp );
    }

    static void listPorts()
    {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = ( CommPortIdentifier ) portEnum
                .nextElement();
            Log.debug( portIdentifier.getName() + " - "
                + getPortTypeName( portIdentifier.getPortType() ) );
        }
    }

    static String getPortTypeName( int portType )
    {
        switch ( portType )
        {
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
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts()
    {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while ( thePorts.hasMoreElements() )
        {
            CommPortIdentifier com = ( CommPortIdentifier ) thePorts
                .nextElement();
            switch ( com.getPortType() )
            {
                case CommPortIdentifier.PORT_SERIAL:
                    try
                    {
                        CommPort thePort = com.open( "CommUtil", 50 );
                        thePort.close();
                        h.add( com );
                    }
                    catch ( PortInUseException e )
                    {
                        System.out.println( "Port, " + com.getName()
                            + ", is in use." );
                    }
                    catch ( Exception e )
                    {
                        System.out.println( "Failed to open port "
                            + com.getName() + e );
                    }
            }
        }
        return h;
    }
}

