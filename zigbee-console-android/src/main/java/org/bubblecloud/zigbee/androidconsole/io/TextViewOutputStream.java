package org.bubblecloud.zigbee.androidconsole.io;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bubblecloud.zigbee.androidconsole.R;

import java.io.IOException;
import java.io.OutputStream;



/**
 * Created by Chris on 06/04/15.
 */
public final class TextViewOutputStream extends OutputStream
{
    private final Context context;

    private final LinearLayout mainScreen;

    private Handler handler;


    public TextViewOutputStream(Context context, LinearLayout screen)
    {
        this.context = context;
        this.mainScreen = screen;

        handler = new Handler(context.getMainLooper());
    }

    final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void write(final int character) throws IOException
    {
        stringBuilder.append((char)character);

        switch(character)
        {
            case '\n':
            case '\r':
            {
                final String logLine = stringBuilder.toString();
                addLogEntry(logLine);
                stringBuilder.setLength(0);
            }
        }
    }

    public void addLogEntry(final String log){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final TextView logText = (TextView)inflater.inflate(R.layout.display_entry,mainScreen,false);

        logText.setText(log);

        handler.post(new Runnable() {
            @Override
            public void run() {
                mainScreen.addView(logText);
            }
        });


    }
}
