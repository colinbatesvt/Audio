package com.example.chris_000.sample;

import android.media.midi.MidiDeviceService;
import android.media.midi.MidiReceiver;
import android.widget.TextView;

public class TestMidi extends MidiDeviceService {

        TestReceiver _receiver;

        public TestMidi(TextView text) {
            super();
            _receiver = new TestReceiver(text);
        }

        @Override
        // Declare the receivers associated with input ports.
        public MidiReceiver[] onGetInputPortReceivers() {
            return new MidiReceiver[]{_receiver};
        }
}