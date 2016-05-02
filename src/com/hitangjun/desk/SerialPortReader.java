
package com.hitangjun.desk;

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


/**
 * �������ݶ�ȡ��,����windows�Ĵ������ݶ�ȡ
 * 
 * @author Macro Lu
 * @version 2007-4-4
 */
public class SerialPortReader extends Observable implements Runnable
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
    public SerialPortReader()
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
            serialPort = ( SerialPort ) portId.open( "SerialReader", timeout );
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
        Thread readThread = new Thread( this );
        readThread.start();
    }

    /**
     * Method declaration
     * 
     * @see
     */
    public void run()
    {
        try
        {
            Thread.sleep( 100 );
        }
        catch ( InterruptedException e )
        {
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

//ASCII��
//-------------------------------------------------------------
//                 ASCII Characters                            
//                            
//Dec   Hex       Char    Code   Dec   Hex  Char
//                            
//0     0         NUL            64    40    @
//1     1         SOH            65    41    A
//2     2         STX            66    42    B
//3     3         ETX            67    43    C
//4     4         EOT            68    44    D
//5     5         ENQ            69    45    E
//6     6         ACK            70    46    F
//7     7         BEL            71    47    G
//8     8         BS             72    48    H
//9     9         HT             73    49    I
//10    0A        LF             74    4A    J
//11    0B        VT             75    4B    K
//12    0C        FF             76    4C    L
//13    0D        CR             77    4D    M
//14    0E        SO             78    4E    N
//15    0F        SI             79    4F    O
//16    10        SLE            80    50    P
//17    11        CS1            81    51    Q
//18    12        DC2            82    52    R
//19    13        DC3            83    53    S
//20    14        DC4            84    54    T
//21    15        NAK            85    55    U
//22    16        SYN            86    56    V
//23    17        ETB            87    57    W
//24    18        CAN            88    58    X
//25    19        EM             89    59    Y
//26    1A        SIB            90    5A    Z
//27    1B        ESC            91    5B    [
//                               92    5C     \
//28    1C        FS             93    5D    ]
//29    1D        GS             94    5E    ^
//30    1E        RS             95    5F    _
//31    1F        US             96    60    `
//32    20    (space)            97    61    a
//33    21        !              98    62    b
//34    22        "    
//                               99    63    c
//35    23        #              100    64    d
//36    24        $                    
//37    25        %              101    65    e
//38    26        &              102    66    f
//39    27        '              103    67    g
//40    28        (              104    68    h
//41    29        )              105    69    i
//42    2A        *              106    6A    j
//43    2B        +              107    6B    k
//44    2C        ,              108    6C    l
//45    2D        -              109    6D    m
//46    2E        .              110    6E    n
//47    2F        /              111    6F    o
//48    30        0              112    70    p
//49    31        1              113    72    q
//50    32        2              114    72    r
//51    33        3              115    73    s
//52    34        4              116    74    t
//53    35        5              117    75    u
//54    36        6              118    76    v
//55    37        7              119    77    w
//56    38        8              120    78    x
//57    39        9              121    79    y
//58    3A        :              122    7A    z
//59    3B        ;              123    7B    {
//60    3C        <              124    7C    |
//61    3D        =              125    7D    }
//62    3E        >              126    7E    ~
//63    3F        ?              127    7F    
