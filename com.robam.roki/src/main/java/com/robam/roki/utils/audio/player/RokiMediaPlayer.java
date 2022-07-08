package com.robam.roki.utils.audio.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.robam.roki.MobApp;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


public class RokiMediaPlayer extends AbsPlayer {

    private MediaPlayer mMediaPlayer;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private android.os.Handler mHandler=new android.os.Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            mCallback.onProgress((Integer) msg.obj);
        }
    };
    public RokiMediaPlayer() {
        mState = PlayerState.Stoped;
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void play(String targetPath) {
        if (mState == PlayerState.Stoped){
            mMediaPlayer.reset();
            try {
                if (TextUtils.isEmpty(targetPath)){
                    return;
                }
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(targetPath);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnCompletionListener(mp -> {
                    this.mCallback.onStop();
                });

                final long[] time = {0};

                mMediaPlayer.setOnPreparedListener(mp -> {
                    this.mCallback.onPlay();

                    mTimer = new Timer();
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {

                            if (mp.getDuration()<time[0]){
                                mTimer.cancel();
                                mTimer=null;
                            }
                            Message message=Message.obtain();
                            message.what=0x2;
                            message.obj=(int) (time[0] +10L)/1000;
                            mHandler.sendMessage(message);
                            time[0] +=10L;
                        }
                    };
                    mTimer.schedule(mTimerTask, 0, 10);
                    mMediaPlayer.start();
                });

                mMediaPlayer.setOnErrorListener((mp, what, extra) ->{
                    Log.e(TAG,what+"---"+extra+"");
                    return false;
                        });

                mMediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
                    int musicTime = mp.getCurrentPosition() / 1000;
                    Log.e(TAG,mp.getCurrentPosition()+"---"+mp.getDuration()+"----"+percent);

                });
                mMediaPlayer.setOnCompletionListener(mp ->
                {

                    Log.e(TAG,"播放完成"+"setOnCompletionListener");
                    if (mTimer!=null) {

                        mTimer.cancel();
                        mTimer=null;
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static final String TAG = "RokiMediaPlayer";

    @Override
    void onAudioPlay(String targetPath) {

    }

    public int getDuration(){
        return mMediaPlayer.getDuration();
    }

    @Override
    public void pause() {

        mMediaPlayer.pause();

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {
          if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
              mMediaPlayer.stop();

        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void addCallback(PlayerCallback callback) {

        this.mCallback=callback;
    }
}
