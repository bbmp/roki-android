package com.legent.utils.api;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class MediaUtils {

	public static void playFromRes(Context cx, int resId) {
		try {
			MediaPlayer player = MediaPlayer.create(cx, resId);
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playFromAssets(Context cx,String ringName) {

		try {
			AssetFileDescriptor fd = cx.getAssets().openFd(
					ringName);

			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
					fd.getLength());

			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {

					try {
						mp.release();
						mp = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(final MediaPlayer mp) {

					try {
						mp.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			mp.prepareAsync();
			// mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
