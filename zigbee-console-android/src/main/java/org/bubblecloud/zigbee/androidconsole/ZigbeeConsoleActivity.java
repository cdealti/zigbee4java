package org.bubblecloud.zigbee.androidconsole;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import org.bubblecloud.zigbee.ZigBeeConsole;
import org.bubblecloud.zigbee.network.port.AndroidUsbSerialPort;
import org.bubblecloud.zigbee.network.port.ZigBeePort;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class ZigbeeConsoleActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zigbee_console);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment())
                .commit();
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_zigbee_console, container, false);

            final EditText commandInputEditText  = (EditText)rootView.findViewById(R.id.consoleInputEditText);
            final TextView consoleOutputTextView = (TextView)rootView.findViewById(R.id.consoleOutputTextView);

            final UsbManager usbManager = (UsbManager) inflater.getContext().getSystemService(Context.USB_SERVICE);

            final AndroidUsbSerialPort usbSerialPort = new AndroidUsbSerialPort(usbManager);

            final int
                    channel = 11,
                    panId   = 4952;

            final PipedInputStream commandInputStream;
            final PrintStream      commandPrintStream;

            // Setup input
            try
            {
                final PipedOutputStream textBoxOutputStream = new PipedOutputStream();
                commandPrintStream = new PrintStream(textBoxOutputStream, true);

                commandInputEditText.addTextChangedListener(new TextWatcher()
                {
                    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
                    {
                    }

                    public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
                    {
                    }

                    public void afterTextChanged(final Editable s)
                    {
                        final String commandString = s.toString();
                        commandPrintStream.println(commandString);
                        s.clear();
                    }
                });

                commandInputStream = new PipedInputStream(textBoxOutputStream);
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }

            // Setup output
            final OutputStream logOutputStream = new OutputStream()
            {
                final StringBuilder stringBuilder = new StringBuilder();

                @Override
                public void write(final int oneByte) throws IOException
                {
                    stringBuilder.append((char)oneByte);

                    if(oneByte=='\n')
                    {
                        consoleOutputTextView.append(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                }
            };

            final ZigBeeConsole console = new ZigBeeConsole(usbSerialPort, panId, channel, false, commandInputStream, logOutputStream);

            console.start();

            return rootView;
        }
    }
}
