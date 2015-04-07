package org.bubblecloud.zigbee.androidconsole.io;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.bubblecloud.zigbee.util.CircularFIFOBufferImpl;
import org.bubblecloud.zigbee.util.FIFOBuffer;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Chris on 06/04/15.
 */
public final class EditTextInputStream extends InputStream
{
    final FIFOBuffer<Character> buffer = new CircularFIFOBufferImpl<Character>();

    public EditTextInputStream(final EditText editText, final Context context)
    {
        final TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    synchronized(buffer)
                    {
                        for(Character c : v.getText().toString().toCharArray())
                        {
                            buffer.push(c);
                        }
                    }

                    buffer.push('\n');
                    buffer.notify();

                    v.setText("");
                    v.clearFocus();

                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);

                    return true;
                }
                return false;
            }
        };

        editText.setOnEditorActionListener(actionListener);
    }

    @Override
    public int read() throws IOException
    {
        synchronized(buffer)
        {
            while(buffer.size() == 0)
            {
                try
                {
                    buffer.wait();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            return buffer.pop();
        }
    }
}
