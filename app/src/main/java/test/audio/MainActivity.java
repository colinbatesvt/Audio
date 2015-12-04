package test.audio;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.IOException;


public class MainActivity extends Activity
{
    private String mFileName;

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    private boolean mRecording = false;
    private boolean mPlaying = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFileName =  getApplicationContext().getDir("Speeches", Context.MODE_PRIVATE)+"/testAudio.3gp";

    }

    public void onRecord(View v) {
        if (!mRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(View v) {
        if (!mPlaying && !mRecording) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }


    private void startRecording() {
        mRecording = true;

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecording = false;

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void startPlaying() {
        mPlaying = true;

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        mPlaying = false;
        mPlayer.release();
        mPlayer = null;
    }

}