package com.robam.roki.utils.audio.recorder;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import com.robam.roki.utils.audio.AudioConfig;

/**
 *
 *
 */

public class AudioPCMRecorder extends AbsRecorder{

//
//    static {
//        System.loadLibrary("mp3test");
//    }
//    public native static void close();
//
//    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);
//
//    public native static int flush(byte[] mp3buf);
//
//    public native static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);
//    public native String stringFromJNI();
    private static final String TAG = "AudioRecordRecorder";

    // audiorecord
    private AudioRecord audioRecord;
    private int inBufferSize;
    private String targetPath;

    public AudioPCMRecorder(){

//        Log.e(TAG,stringFromJNI());

        // bufferSize = samplerate x bit-width x 采样时间 x channel_count
        inBufferSize = AudioRecord.getMinBufferSize(
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_IN,
                AudioConfig.AUDIO_ENCODING);

        audioRecord = new AudioRecord(
                AudioConfig.AUDIO_SOURCE,
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_IN,
                AudioConfig.AUDIO_ENCODING,
                inBufferSize);
    }

    @Override
    void onAudioRecord(String targetPath) {
        this.targetPath = targetPath;
        CongTimer.resetTimer();
        new RecordTask().execute(targetPath);
    }

    @Override
    void onAudioResume() {
        new RecordTask().execute(targetPath);
    }

    @Override
    public void release() {
        if (audioRecord != null){
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED){
                audioRecord.stop();
            }
            audioRecord.release();
        }
    }

    private class RecordTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCallback != null){
                mCallback.onRecord();
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

            RandomAccessFile randomAccessFile = null;

            try {
                randomAccessFile = new RandomAccessFile(params[0], "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (randomAccessFile == null){
                return null;
            }

            try {
                byte[] buffer = new byte[inBufferSize/4];
                audioRecord.startRecording();
                CongTimer.startTimer();

                while(true) {
                    if (mState == RecordState.Paused || mState == RecordState.Stoped){
                        CongTimer.stopTimer();
                        break;
                    }
                    audioRecord.read(buffer, 0, buffer.length);

                    //向原文件中追加内容
                    randomAccessFile.seek(randomAccessFile.length());
                    randomAccessFile.write(buffer, 0, buffer.length);

                    publishProgress(CongTimer.workingTime);
                }

                if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED){
                    audioRecord.stop();
                }
                randomAccessFile.close();

            } catch (Throwable t) {
                Log.e("AudioRecord", "Recording Failed");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (mCallback != null){
                mCallback.onProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mState == RecordState.Paused){
                if (mCallback != null){
                    mCallback.onPause();
                }
            } else {
                mCallback.onStop();
            }
        }
    }

    // Timer
    private static class CongTimer{
        private static Handler mHandler = new Handler();
        private static int workingTime;
        private static boolean isTimerStoped;

        private static void startTimer(){
            isTimerStoped = false;
            mHandler.postDelayed(recordTime, 0);
        }

        private static void resetTimer(){
            workingTime = 0;
        }

        private static void stopTimer(){
            isTimerStoped = true;
            mHandler.removeCallbacks(recordTime);
        }

        private static Runnable recordTime = new Runnable() {
            @Override
            public void run() {
                if (!isTimerStoped){
                    workingTime += 1;
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
    }
}
