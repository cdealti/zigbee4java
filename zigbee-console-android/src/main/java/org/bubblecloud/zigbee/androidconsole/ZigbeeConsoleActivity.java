package org.bubblecloud.zigbee.androidconsole;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bubblecloud.zigbee.ZigBeeConsole;
import org.bubblecloud.zigbee.androidconsole.io.EditTextInputStream;
import org.bubblecloud.zigbee.androidconsole.io.TextViewOutputStream;
import org.bubblecloud.zigbee.network.port.AndroidUsbSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterOutputStream;


public class ZigbeeConsoleActivity extends ActionBarActivity implements View.OnClickListener
{
//    private static final int
//            Zigbee_Channel = 11,
//            Zigbee_PAN_ID  = 4952;

    private boolean serviceBound = false;

    private final ServiceConnection zigbeeConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceBound = true;

            zigbeeAndroidService = ((ZigbeeAndroidService.ZigbeeAndroidServiceBinder)service).getService();

            zigbeeAndroidService.setStreams(inputStream, outputStream);

            if(zigbeeAndroidService.isConsoleStarted()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startOrStopBtn.setText("Stop Zigbee Console");
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startOrStopBtn.setText("Start Zigbee Console");
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private EditTextInputStream  inputStream;
    private TextViewOutputStream outputStream;

    private LinearLayout mainScreen;

    private ZigbeeAndroidService zigbeeAndroidService;

    private Button startOrStopBtn;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zigbee_android_console);

//        final EditText commandInputEditText  = (EditText)findViewById(R.id.consoleInputEditText);
//        final TextView logOutputTextView = (TextView)findViewById(R.id.consoleOutputTextView);
//
//        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//
//        final AndroidUsbSerialPort usbSerialPort = new AndroidUsbSerialPort(usbManager);
//
//        final InputStream  commandInputStream = new EditTextInputStream(commandInputEditText, this);
//        final OutputStream logOutputStream    = new TextViewOutputStream(logOutputTextView);
//
//        final ZigBeeConsole console = new ZigBeeConsole(usbSerialPort, Zigbee_PAN_ID, Zigbee_Channel, false, commandInputStream, logOutputStream);
//
//        console.start();

        mainScreen = (LinearLayout)findViewById(R.id.main_screen);



        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final EditText editText = (EditText)layoutInflater.inflate(R.layout.input_entry,
                                                                   mainScreen,
                                                                   false);

        final TextView logText = (TextView)layoutInflater.inflate(R.layout.display_entry,mainScreen,false);

        inputStream = new EditTextInputStream(editText);

        outputStream = new TextViewOutputStream(logText, handler);

        startOrStopBtn = (Button)findViewById(R.id.btn_start_stop_service);

        startOrStopBtn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!serviceBound){
            bindService(new Intent(this, ZigbeeAndroidService.class), zigbeeConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceBound){
            unbindService(zigbeeConnection);
        }
    }

    @Override
    public void onClick(View v) {
        if(zigbeeAndroidService.isConsoleStarted()){
            stopZigbeeService();
        }else{
            startZigbeeService();
        }
    }

    private void startZigbeeService(){
        startService(new Intent(this, ZigbeeAndroidService.class));
        handler.post(new Runnable() {
            @Override
            public void run() {
                startOrStopBtn.setText("Stop Zigbee Console");
            }
        });
    }

    private void stopZigbeeService(){
        stopService(new Intent(this, ZigbeeAndroidService.class));
        handler.post(new Runnable() {
            @Override
            public void run() {
                startOrStopBtn.setText("Start Zigbee Console");
            }
        });

    }
}
