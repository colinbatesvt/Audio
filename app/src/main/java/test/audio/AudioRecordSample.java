package test.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by colin on 12/6/2015.
 */
public class AudioRecordSample {

    private AudioRecord mRecorder;
    private File mRecordingFile;
    private Thread mRecordingThread;

    private static int[] mSampleRates = new int[]{44100, 22050, 11025, 8000};
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    private static final int BytesPerElement = 2; // 2 bytes in 16bit format

    //initialize our recorder
    public AudioRecordSample(File recordingLocation)
    {
        //find a usable sample rate
        for (int rate : mSampleRates) {
            Log.d("DEBUG", "Attempting rate " + rate + "Hz");
            int bufferSize = AudioRecord.getMinBufferSize(rate, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

            if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                // check if we can instantiate and have a success
                mRecorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

                if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    return;
                }
            }

            mRecorder = null;
        }
    }

    public void startRecording()
    {
        if(mRecorder != null && mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            if(mRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
                return;

            mRecorder.startRecording();

            mRecordingThread = new Thread(new Runnable() {
                public void run() {
                    writeAudioDataToFile();
                }
            }, "AudioRecorder Thread");
            mRecordingThread.start();
        }
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in bytes
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(mRecordingFile.getPath(), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (mRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            // gets the voice output from microphone to byte format

            mRecorder.read(sData, 0, BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        // stops the recording activity
        if (mRecorder!=null && mRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            mRecorder.stop();
            mRecorder.release();
            mRecordingThread = null;
        }
    }

    public void playAudio()
    {
        int bufferSize = android.media.AudioTrack.getMinBufferSize(mRecorder.getSampleRate(), AudioFormat.CHANNEL_OUT_MONO,
                                                                AudioFormat.ENCODING_PCM_16BIT);


        AudioTrack audioPlayer = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(mRecorder.getSampleRate()).build()
                , bufferSize, AudioTrack.MODE_STREAM, 12345);

        int count = 1024; // 1 kb
        //Reading the file..
        byte[] byteData = null;

        byteData = new byte[(int) count];
        FileInputStream in = null;
        try {
            in = new FileInputStream(mRecordingFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChannel fc = in.getChannel();
        int ret = 0;
        int bytesRead = 0;
        int size = (int) mRecordingFile.length();

        audioPlayer.play();
        while (bytesRead < size) {
            try {
                fc.position(bytesRead);
                ret = in.read(byteData, 0, count);
                //Log.d("DEBUG", String.valueOf(ret));
                if (ret != -1) {
                    // Write the byte array to the track
                    audioPlayer.write(byteData, 0, ret);
                    bytesRead += ret;
                } else
                    break;
                in.close();
                audioPlayer.stop();
                audioPlayer.release();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
