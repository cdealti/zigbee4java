package org.bubblecloud.zigbee.androidconsole;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;

import org.bubblecloud.zigbee.ZigBeeConsole;
import org.bubblecloud.zigbee.androidconsole.io.EditTextInputStream;
import org.bubblecloud.zigbee.androidconsole.io.TextViewOutputStream;
import org.bubblecloud.zigbee.network.port.AndroidUsbSerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Jichen on 7/04/15.
 */
public class ZigbeeAndroidService extends Service{

    private static final int
            Zigbee_Channel = 11,
            Zigbee_PAN_ID  = 4952;


    private final IBinder localBinder = new ZigbeeAndroidServiceBinder();

    private ZigBeeConsole console;

    private boolean consoleStarted;

    @Override
    public void onCreate() {

        super.onCreate();

        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        final AndroidUsbSerialPort usbSerialPort = new AndroidUsbSerialPort(usbManager);

        //final InputStream commandInputStream = new EditTextInputStream(commandInputEditText, this);
        //final OutputStream logOutputStream    = new TextViewOutputStream(logOutputTextView);

        console = new ZigBeeConsole(usbSerialPort, Zigbee_PAN_ID, Zigbee_Channel, false, null, null);


    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    class ZigbeeAndroidServiceBinder extends Binder{

        ZigbeeAndroidService getService(){
            return ZigbeeAndroidService.this;
        }
    }

    public void startConsole(){
        console.start();
        consoleStarted = true;
    }

    public void stopConsole(){
        console.stop();
        consoleStarted = false;
    }

    public void setInput(InputStream input){
        console.setInputStream(input);
    }

    public void setOutput(OutputStream output){
        console.setPrintStream(new PrintStream(output));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!consoleStarted){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startConsole();
                }
            }).start();

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(consoleStarted){
            stopConsole();
        }
    }

    public boolean isConsoleStarted(){
        return consoleStarted;
    }
}
