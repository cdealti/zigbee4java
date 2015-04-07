package org.bubblecloud.zigbee.androidconsole.io;

import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by Chris on 06/04/15.
 */
public final class TextViewOutputStream extends OutputStream
{
    private final TextView textView;

    public TextViewOutputStream(final TextView textView)
    {
        this.textView = textView;
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
                textView.append(logLine);
                stringBuilder.setLength(0);
            }
        }
    }
}
