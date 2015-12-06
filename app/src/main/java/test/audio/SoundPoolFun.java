package test.audio;

import android.media.SoundPool;

import java.io.File;

/**
 * Created by chris_000 on 12/5/2015.
 */
public class SoundPoolFun {
    private SoundPool _soundPool;
    private String _filePath;
    private int[] _soundIDs;
    private boolean _paused;

    public SoundPoolFun(String filePath) {
        _soundIDs = new int[1];
        _paused = false;
        _soundPool = (new SoundPool.Builder()).setMaxStreams(4).build();
        _soundIDs[0] = _soundPool.load(filePath, 1);
    }

    public void play() {
        _soundPool.play(_soundIDs[0], 1, 1, 1, 0, (float)0.5);
    }

    public void pause() {
        if (!_paused) {
            _soundPool.pause(_soundIDs[0]);
            _paused = true;
        }
        else {
            _soundPool.resume(_soundIDs[0]);
            _paused = false;
        }

    }

    public void stop() {
        _soundPool.stop(_soundIDs[0]);
    }

    public void allDone() {
        _soundPool.unload(_soundIDs[0]);
        _soundPool.release();
    }
}
