package com.example.chris_000.sample;

import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiDeviceService;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TestMidi _test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView results = (TextView)findViewById(R.id.myText);

        _test = new TestMidi(results);

        Context context = this.getApplicationContext();
        MidiManager m = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);

    }

    public void clicked(View button) {
        MidiReceiver receiver = _test.onGetInputPortReceivers()[0];
        byte[] strBytes = "This is a MIDI message! WOAH!".getBytes();
        try {
            receiver.send(strBytes, 0, strBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
