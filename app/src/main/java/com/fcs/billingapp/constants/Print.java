package com.fcs.billingapp.constants;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;

public class Print {

    public static String label1 ;
    public static String label;

    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothSocket mBluetoothSocket;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream mOutputStream;
    public static InputStream mInputStream;
    public static volatile boolean stopWorker;


    public static byte[] TEXT_MODE = {0x1B,0x21,1};

    public static byte[] CHARACTER_FONT = {0x1B,0x21,1};

    public static byte[]  BOLD_START = {0x1B,0x45,1};
    public static byte[]  BOLD_END = {0x1B,0x45,0};

    public static byte[]  DOUBLE_HEIGHT_START = {0x1D,0x21,0x01,2};
    public static byte[]  DOUBLE_HEIGHT_END = {0x1D,0x21,0x00,0};

    public static byte[]  DOUBLE_WIDTH_START = {0x1D,0x21,6};
    public static byte[]  DOUBLE_WIDTH_END = {0x1D,0x21,4};

    public static byte[]  PRINT_BARCODE = {0x1D,0x6B,73,20};

    public static byte[] START = {0x1B};
    public static byte[] END   = {0x0D,0x0D,0x0D,0x0D};
    //public static byte[] TEXT_MODE = {0x21};
    public static byte[] BAR_CODE  = {0x24};
    /*public static byte[] NORMAL_FONT = {0x01};*/
    public static byte[] NORMAL_FONT = {0x01};
    /*public static byte[] BOLD_FONT = {0x09};*/
    public static byte[] BOLD_FONT = {0x47};
    public static byte[] DOUBLE_HEIGHT_FONT = {0x11};
    public static byte[] BOLD_DOUBLE_HEIGHT_FONT = {0x19};
    public static byte[] CARRIAGE_RETURN = {0x0D};
    //public static byte[] LINE_FEED = {0x0D,0x0D};
    public static byte[] LINE_FEED = {0X22};
    public final static String SPACE18 = "                  ";
    public final static String SPACE5 = "     ";
    public final static String SPACE10 = "          ";
    public final static String SPACE9 = "         ";
    public final static String SPACE7 = "         ";
    public static byte[] RESET = {0X2A};
}
