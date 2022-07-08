package com.robam.roki.utils.audio.player;

import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.robam.roki.utils.audio.AudioConfig;




public class AudioPCMPlayer extends AbsPlayer{

    private static final String TAG = "AudioPCMPlayer";

    // audio track
    private AudioTrack audioTrack;

    private int mPlaySize;
    private int offSet;
    private String targetPath;

    // init
    public AudioPCMPlayer() {

        int outBufferSize = AudioTrack.getMinBufferSize(
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_OUT,
                AudioConfig.AUDIO_ENCODING);

        mPlaySize = outBufferSize * 2;

        audioTrack = new AudioTrack(
                AudioConfig.AUDIO_SOURCE,
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_OUT,
                AudioConfig.AUDIO_ENCODING,
                outBufferSize,
                AudioTrack.MODE_STREAM);
    }

    @Override
    void onAudioPlay(String targetPath) {
        this.targetPath = targetPath;
        offSet = 0;
        new PlayTask().execute(targetPath);
    }

    @Override
    void onAudioResume() {
        new PlayTask().execute(targetPath);
    }

    @Override
    public void release() {
        if (audioTrack != null){
            if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED){
                audioTrack.stop();
            }
            audioTrack.release();
        }
    }


    @Override
    public void setOffSet(int offSet) {
        if(offSet % mPlaySize != 0){
            this.offSet = offSet / mPlaySize * mPlaySize;
        }else{
            this.offSet = offSet;
        }
    }


    private class PlayTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCallback != null){
                mCallback.onPlay();
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);


            File file = new File(params[0]);

            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("文件",e.toString());
            }

            long fileSize = file.length();
            Log.e("文件","----"+fileSize);
            byte[] audioData = new byte[(int) fileSize];

            try {
                inputStream.read(audioData, 0, audioData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            audioTrack.play();

            while(true){

                if (mState == PlayerState.Paused || mState == PlayerState.Stoped){
                    break;
                }

                int size = audioTrack.write(audioData, offSet, mPlaySize);
                offSet += size;

                if (offSet >= audioData.length) {
                    mState = PlayerState.Stoped;
                }

                publishProgress(offSet);
            }

            if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED){
                audioTrack.stop();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mCallback != null){
                mCallback.onProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mState == PlayerState.Paused){
                if (mCallback != null){
                    mCallback.onPause();
                }
            }else{
                if (mCallback != null){
                    mCallback.onStop();
                }
            }
        }
    }
}
