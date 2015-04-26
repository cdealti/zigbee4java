package org.bubblecloud.zigbee.androidconsole.io;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bubblecloud.zigbee.androidconsole.R;
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

    final LinearLayout mainScreen;

    final Context context;

    private Handler handler;

    private final static String TAG = EditTextInputStream.class.getSimpleName();

    final TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if(actionId == EditorInfo.IME_ACTION_GO)
            {
                synchronized(buffer)
                {
                    for(Character c : v.getText().toString().toCharArray())
                    {
                        buffer.push(c);
                    }

                    for(Character c:System.getProperty("line.separator").toCharArray()){
                        buffer.push(c);
                    }

                    buffer.notify();
                }



//                v.setText("");
//                v.clearFocus();
//
//                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.toggleSoftInput(0, 0);

                addInputEntry();
                return true;
            }
            return false;
        }
    };

    public EditTextInputStream(final Context context, LinearLayout mainScreen)
    {
        this.context = context;

        this.mainScreen = mainScreen;

        handler = new Handler(context.getMainLooper());

    }

    @Override
    public int read() throws IOException
    {
        Log.i(TAG, "Read called");
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

    public void addInputEntry(){

       LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       final EditText editText = (EditText)layoutInflater.inflate(R.layout.input_entry, mainScreen, false);

       editText.setOnEditorActionListener(actionListener);


        handler.post(new Runnable() {
            @Override
            public void run() {
                mainScreen.addView(editText);
            }
        });


    }


}
