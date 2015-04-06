package org.bubblecloud.zigbee.androidconsole;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.bubblecloud.zigbee.ZigBeeConsole;
import org.bubblecloud.zigbee.androidconsole.io.EditTextInputStream;
import org.bubblecloud.zigbee.androidconsole.io.TextViewOutputStream;
import org.bubblecloud.zigbee.network.port.AndroidUsbSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ZigbeeConsoleActivity extends ActionBarActivity
{
    private static final int
            Zigbee_Channel = 11,
            Zigbee_PAN_ID  = 4952;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zigbee_console);

        final EditText commandInputEditText  = (EditText)findViewById(R.id.consoleInputEditText);
        final TextView logOutputTextView = (TextView)findViewById(R.id.consoleOutputTextView);

        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        final AndroidUsbSerialPort usbSerialPort = new AndroidUsbSerialPort(usbManager);

        final InputStream  commandInputStream = new EditTextInputStream(commandInputEditText, this);
        final OutputStream logOutputStream    = new TextViewOutputStream(logOutputTextView);

        final ZigBeeConsole console = new ZigBeeConsole(usbSerialPort, Zigbee_PAN_ID, Zigbee_Channel, false, commandInputStream, logOutputStream);

        console.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zigbee_console, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
