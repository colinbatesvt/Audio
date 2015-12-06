package com.example.chris_000.sample;

import android.media.midi.MidiReceiver;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by chris_000 on 12/5/2015.
 */
public class TestReceiver extends MidiReceiver{
    TextView _text;

    public TestReceiver(TextView text) {
        super();
        _text = text;
    }

    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        _text.setText(new String(msg, "UTF-8"));
        //Log.d("DEBUG", new String(msg, "UTF-8"));
    }
}
