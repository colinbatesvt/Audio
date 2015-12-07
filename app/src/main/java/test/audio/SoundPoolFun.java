package test.audio;

import android.media.SoundPool;

import java.io.File;
import java.util.List;

/**
 * Created by chris_000 on 12/5/2015.
 */
public class SoundPoolFun {
    private SoundPool _soundPool;
    private List<Integer> _playings;
    private int[] _soundIDs;
    private boolean _paused;

    public SoundPoolFun(String filePath) {
        _soundIDs = new int[1];
        _paused = false;
        _soundPool = (new SoundPool.Builder()).setMaxStreams(4).build();
        _soundIDs[0] = _soundPool.load(filePath, 1);
    }

    public void play() {
       int result = _soundPool.play(_soundIDs[0], 1, 1, 1, 0, (float)0.5);
        _playings.add(result);
    }



    public void pause() {
        if (!_paused && !_playings.isEmpty()) {
            _soundPool.autoPause();
            _paused = true;
        }
        else {
            _soundPool.autoResume();
            _paused = false;
        }

    }

    public void stop() {
        for( int i : _playings) {
            _soundPool.stop(_playings.get(i));
            _playings.remove(_playings.get(i));
        }
    }

    public void allDone() {
        _soundPool.unload(_soundIDs[0]);
        _soundPool.release();
    }
}
