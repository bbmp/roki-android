package com.youku.player.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
//import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.baseproject.image.ImageResizer;
import com.baseproject.utils.Logger;
import com.baseproject.utils.UIUtils;
import com.baseproject.utils.Util;
import com.youku.analytics.data.Device;
import com.youku.player.NewSurfaceView;
import com.youku.player.Track;
import com.youku.player.YoukuPlayerConfiguration;
import com.youku.player.ad.AdType;
import com.youku.player.apiservice.ICacheInfo;
import com.youku.player.config.MediaPlayerConfiguration;
import com.youku.player.goplay.Profile;
import com.youku.player.goplay.StaticsUtil;
import com.youku.player.module.PlayVideoInfo;
import com.youku.player.module.VideoCacheInfo;
import com.youku.player.module.VideoHistoryInfo;
import com.youku.player.module.VideoUrlInfo;
import com.youku.player.module.VideoUrlInfo.Source;
import com.youku.player.plugin.OnSurfaceStatusChangeListener;
import com.youku.player.plugin.PluginADPlay;
import com.youku.player.plugin.PluginManager;
import com.youku.player.plugin.PluginOverlay;
import com.youku.player.plugin.PluginSimplePlayer;
import com.youku.player.plugin.YoukuPlayerListener;
import com.youku.player.ui.R;
import com.youku.player.ui.interf.IBaseMediaPlayer;
import com.youku.player.ui.interf.IBaseMediaPlayer.OnPlayHeartListener;
import com.youku.player.ui.interf.IBasePlayerManager;
import com.youku.player.ui.interf.IMediaPlayerDelegate;
import com.youku.player.ui.widget.TudouEncryptDialog.OnPositiveClickListener;
import com.youku.player.util.AnalyticsWrapper;
import com.youku.player.util.DetailMessage;
import com.youku.player.util.DeviceOrientationHelper;
import com.youku.player.util.DeviceOrientationHelper.OrientationChangeCallback;
import com.youku.player.util.DisposableStatsUtils;
import com.youku.player.util.MediaPlayerProxyUtil;
import com.youku.player.util.PlayCode;
import com.youku.player.util.PlayerUiUtile;
import com.youku.player.util.PlayerUtil;
import com.youku.player.util.PreferenceUtil;
import com.youku.player.util.RemoteInterface;
import com.youku.player.util.SessionUnitil;
import com.youku.player.util.URLContainer;
import com.youku.statistics.IRVideoWrapper;
import com.youku.statistics.OfflineStatistics;
import com.youku.statistics.TaskSendPlayBreak;
import com.youku.uplayer.EGLUtil;
import com.youku.uplayer.MPPErrorCode;
import com.youku.uplayer.OnADCountListener;
import com.youku.uplayer.OnADPlayListener;
import com.youku.uplayer.OnCurrentPositionUpdateListener;
import com.youku.uplayer.OnHwDecodeErrorListener;
import com.youku.uplayer.OnLoadingStatusListener;
import com.youku.uplayer.OnNetworkSpeedListener;
import com.youku.uplayer.OnRealVideoStartListener;
import com.youku.uplayer.OnTimeoutListener;
import com.youku.uplayer.OnVideoIndexUpdateListener;

public abstract class YoukuBasePlayerManager extends IBasePlayerManager
		implements DetailMessage, OrientationChangeCallback {

	// ??????????????????
	int position = 0;
	NewSurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	YoukuPlayerView mYoukuPlayerView;
	PluginManager pluginManager;
	// IMediaPlayerDelegate mediaPlayerDelegate;
	public boolean autoPaly = true;// ??????????????????
	PluginOverlay mPluginSmallScreenPlay;
	public Context youkuContext;
	private boolean isSendPlayBreakEvent = false; // ???????????????????????????????????????????
	private YoukuPlayerListener mPlayerListener;
	// ??????onCreate???onDestroy?????????????????????????????????onDestroy??????surfaceholder??????
	private static int mCreateTime;

	public YoukuBasePlayerManager(Activity context) {
		super(context);
	}

	public IMediaPlayerDelegate getMediaPlayerDelegate() {
		return this.mediaPlayerDelegate;
	}

	int fullWidth, fullHeight;// , smallWidth, smallHeight;
	public static Handler handler = new Handler() {

	};
	// public String id;// ????????????id video/show
	public boolean autoPlay = false;// ??????????????????

	// ????????????????????????????
	private boolean firstLoaded = false;

	public static final int END_REQUEST = 201;

	public static final int END_PLAY = 202;

	// ????????????????g??????
	public boolean is3GPause = false;

	int land_height, land_width;
	int port_height, port_width;
	public ImageResizer mImageWorker; // unused
	private static String TAG = "YoukuBaseActivity";
	private static final boolean DEVELOPER_MODE = false;
	// PlayerApplication mApplication; //unused
	// ??????????????????dialog
	private Dialog mAdDialogHint = null;
	// ??????PlayVideo()?????????onActivityResult()??????
	private PlayVideoInfo mSavedPlayVideoInfo = null;
	// ????????????????????????????????????
	private boolean mWaitingLoginResult = false;
	// ??????????????????????????????????
	private static final int LOGIN_REQUEST_DIALOG_CODE = 10999;

	/**
	 * ?????????????????????????????????????????????????????????YoukuPlayer???setIEncryptVideoCallBack??????????
	 * YoukuBasePlayerManager??????????????????call????????????????????????????????????????????????????
	 */
	// public boolean isApiServiceAvailable = false;
	@Override
	public void onCreate() {
		Logger.d("PlayFlow", "YoukuBasePlayerManager->onCreate");
		if (DEVELOPER_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // ?????????????????????detectAll()
																			// ?????????????????????????????????I/O
					.penaltyLog() // ??????logcat???????????????????????????dropbox??????????????????????????????log
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // ??????SQLite????????????????
					.penaltyLog() // ??????logcat
					.penaltyDeath().build());
		}

		super.onCreate();

		if (mCreateTime == 0) {
			// ??????????????????????????????????????????????????????????
			Profile.getVideoQualityFromSharedPreferences(getBaseActivity());
		}
		++mCreateTime;
		youkuContext = getBaseActivity();
		// mImageWorker = (ImageResizer) getImageWorker(this);

		getBaseActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

		OfflineStatistics offline = new OfflineStatistics();
		offline.sendVV(getBaseActivity());

		ACTIVE_TIME = PreferenceUtil.getPreference(getBaseActivity(),
				"active_time");
		if (ACTIVE_TIME == null || ACTIVE_TIME.length() == 0) {
			ACTIVE_TIME = String.valueOf(System.currentTimeMillis());
			PreferenceUtil.savePreference(getBaseActivity(), "active_time",
					ACTIVE_TIME);
		}

		// ??????????????????????
		Profile.GUID = Device.guid;
		try {
			YoukuBasePlayerManager.versionName = getBaseActivity()
					.getPackageManager().getPackageInfo(
							getBaseActivity().getPackageName(),
							PackageManager.GET_META_DATA).versionName;
		} catch (NameNotFoundException e) {
			YoukuBasePlayerManager.versionName = "3.1";
			Logger.e(TAG_GLOBAL, e);
		}

		if (TextUtils.isEmpty(com.baseproject.utils.Profile.User_Agent)) {
			String plant = UIUtils.isTablet(getBaseActivity()) ? "Youku HD;"
					: "Youku;";
			com.baseproject.utils.Profile.initProfile("player", plant
					+ versionName + ";Android;"
					+ android.os.Build.VERSION.RELEASE + ";"
					+ android.os.Build.MODEL, getBaseActivity());
		}

		// mApplication = PlayerApplication.getPlayerApplicationInstance();
		flags = getBaseActivity().getApplicationInfo().flags;
		// ??????????????????????????????activity???????????????????????????????????????
		// com.baseproject.utils.Profile.mContext = getBaseActivity();
		if (MediaPlayerProxyUtil.isUplayerSupported()) { // -------------------->
			YoukuBasePlayerManager.isHighEnd = true;
			// ????????????
			PreferenceUtil.savePreference(getBaseActivity(),
					"isSoftwareDecode", true);
			com.youku.player.goplay.Profile.setVideoType_and_PlayerType(
					com.youku.player.goplay.Profile.FORMAT_FLV_HD,
					getBaseActivity());
		} else {
			YoukuBasePlayerManager.isHighEnd = false;
			com.youku.player.goplay.Profile.setVideoType_and_PlayerType(
					com.youku.player.goplay.Profile.FORMAT_3GPHD,
					getBaseActivity());
		}

		IMediaPlayerDelegate.is = getBaseActivity().getResources()
				.openRawResource(R.raw.aes); // -------------------------------------->
		orientationHelper = new DeviceOrientationHelper(getBaseActivity(), this);

		MediaPlayerConfiguration.getInstance().mPlantformController
				.initIRVideo(getBaseActivity());

	}

	// public ImageWorker getImageWorker(YoukuBasePlayerManager context) {
	// if (null == mImageWorker) {
	// final int height = Device.ht;
	// final int width = Device.wt;
	// final int shortest = height > width ? width : height;
	// mImageWorker = new ImageFetcher(context, shortest);
	// mImageWorker.setImageCache(ImageCache.findOrCreateCache(context,
	// Utils.IMAGE_CACHE_DIR));
	// mImageWorker.setImageFadeIn(true);
	// }
	// return mImageWorker;
	// }

	public void initLayoutView(YoukuPlayerView mYoukuPlayerView) { // ???YoukuPlayerView??????????????????
		this.mYoukuPlayerView = mYoukuPlayerView;
		onCreateInitialize(); // ??????????????????MediaPlayerDelegate
		// addPlugins();
	}

	FrameLayout player_holder;
	private PluginOverlay mPluginFullScreenPlay; // --------------------->????????????
	private PluginADPlay mPluginADPlay; // --------------------->????????????

	/**
     * 
     */
	protected void addPlugins() {
		getBaseActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				player_holder = mYoukuPlayerView
						.findViewById(R.id.player_holder_all);

				if (mPluginFullScreenPlay != null) {
					// ????????????
					pluginManager.addPlugin(mPluginFullScreenPlay,
							player_holder);
				}

				// ??????????????????
				if (mPluginSmallScreenPlay == null)
					mPluginSmallScreenPlay = new PluginSimplePlayer( // --------------------->
							YoukuBasePlayerManager.this, mediaPlayerDelegate);
				if(mPlayerListener!=null){
					mPluginSmallScreenPlay.youkuPlayerListener=mPlayerListener;
				}
				

				// ?????????????
				// ??????????????????
				PluginADPlay
						.setAdMoreBackgroundColor(Profile.PLANTFORM == Plantform.TUDOU);
				mPluginADPlay = new PluginADPlay(YoukuBasePlayerManager.this,
						mediaPlayerDelegate);

				// ??????????????????????
				mYoukuPlayerView.mMediaPlayerDelegate = mediaPlayerDelegate; // ----------------->
				pluginManager.addYoukuPlayerView(mYoukuPlayerView);
				pluginManager.addPlugin(mPluginSmallScreenPlay, player_holder);
				updatePlugin(PLUGIN_SHOW_NOT_SET);
			}
		});
	}

	private boolean isLand() {
		Display getOrient = getBaseActivity().getWindowManager()
				.getDefaultDisplay();
		return getOrient.getWidth() > getOrient.getHeight();
	}

	@SuppressWarnings("deprecation")
	private void initPlayAndSurface() {
		surfaceView = mYoukuPlayerView.surfaceView;
		mYoukuPlayerView.mMediaPlayerDelegate = mediaPlayerDelegate; // ?????????YoukuPlayerView??????MediaPlayerDelegate??????
		// mediaPlayerDelegate.mediaPlayer = BaseMediaPlayer.getInstance();
		// //MediaPlayerDelegate???????????????
		mediaPlayerDelegate.mediaPlayer = getMediaPlayer(); // MediaPlayerDelegate???????????????
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(mediaPlayerDelegate.mediaPlayer); // ???????????????surface
																	// holder??????????????????mediaplayer
		if (!isHighEnd)
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private IBaseMediaPlayer getMediaPlayer() {
		IBaseMediaPlayer baseMediaPlayer = RemoteInterface.baseMediaPlayer;
		Logger.d(TAG, "setOnSurfaceStatusChangeListener");
		baseMediaPlayer
				.setOnSurfaceStatusChangeListener(new OnSurfaceStatusChangeListener() {

					@Override
					public void onSurfaceCreated() {
						Logger.d(TAG, "onSurfaceCreated");
						for (OnSurfaceStatusChangeListener l : surfaceChangeListeners) {
							Logger.d(TAG, " for  onSurfaceCreated");
							l.onSurfaceCreated();
						}
					}
				});
		return baseMediaPlayer;
	}

	public List<OnSurfaceStatusChangeListener> surfaceChangeListeners = new ArrayList<OnSurfaceStatusChangeListener>();

	public void addOnSurfaceStatusChangeListener(OnSurfaceStatusChangeListener l) {
		if (l != null) {
			surfaceChangeListeners.add(l);
		}
	}

	public SurfaceHolder getSurfaceHolder() {
		return surfaceHolder;
	}

	// ????????????????????????
	public void initPlayerPart() {
		if (mediaPlayerDelegate != null
				&& mediaPlayerDelegate.mediaPlayer != null
				&& mediaPlayerDelegate.mediaPlayer.isListenerInit())
			return;
		initPlayAndSurface();
		initMediaPlayer();
	}

	private void initMediaPlayer() { // ---------->????????????
		// mediaPlayerDelegate.mediaPlayer = BaseMediaPlayer.getInstance();
		Logger.d(TAG, "initMediaPlayer");
		mediaPlayerDelegate.mediaPlayer = getMediaPlayer();
		mediaPlayerDelegate.mediaPlayer
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						if (onPause) {
							mp.release();
							return;
						}
						if (pluginManager == null)
							return;
						pluginManager.onBufferingUpdateListener(percent);
						if(mPlayerListener!=null){
							mPlayerListener.onBufferingUpdate(percent);
						}
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						if (mediaPlayerDelegate != null)
							mediaPlayerDelegate.onComplete();
						if (mYoukuPlayerView != null)
							mYoukuPlayerView.setPlayerBlack();
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						Logger.d("PlayFlow",
								"????????????????????? MediaPlayer onError what=" + what
										+ " !!!");
						if (mYoukuPlayerView != null)
							mYoukuPlayerView.setDebugText("????????????-->onError:"
									+ what);
						disposeAdErrorLoss(what);
						if (isAdPlayError(what)) {
							Logger.d("PlayFlow", "????????????:" + what
									+ " ????????????:??????????????????");
							return loadingADOverTime();
						}
						if (mediaPlayerDelegate != null
								&& !mYoukuPlayerView.realVideoStart) {
							getBaseActivity().runOnUiThread(new Runnable() {
								public void run() {
									detectPlugin();
								}
							});
						}
						if (!isSendPlayBreakEvent
								&& MediaPlayerConfiguration.getInstance()
										.trackPlayError()
								&& mYoukuPlayerView.realVideoStart
								&& mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null) {
							final String videoUrl = mediaPlayerDelegate.videoInfo
									.getWeburl();
							final TaskSendPlayBreak task = new TaskSendPlayBreak(
									videoUrl);
							task.execute();
							isSendPlayBreakEvent = true;
						}
						if (mYoukuPlayerView.realVideoStart
								&& mediaPlayerDelegate.isLoading)
							Track.onPlayLoadingEnd();
						onLoadingFailError();
						if (pluginManager == null) {
							Logger.d("PlayFlow", "onError????????????:" + what
									+ " pluginManager == null  return false");
							return false;
						}
						// Logger.d("PlayFlow", "????????????:" + what + " ????????????:??????????);"
						int nowPostition = mediaPlayerDelegate
								.getCurrentPosition();
						if (nowPostition > 0) {
							position = nowPostition;
						}
						// ??????????????????????????????????
						if (what == -38
								&& !MediaPlayerProxyUtil.isUplayerSupported()) {
							what = MPPErrorCode.MEDIA_INFO_PLAY_UNKNOW_ERROR;
						}
						if(mPlayerListener!=null){
							mPlayerListener.onError( what, extra);
						}
						return pluginManager.onError(what, extra);
					}

					private boolean isAdPlayError(int what) {
						return what == MPPErrorCode.MEDIA_INFO_PREPARED_AD_CHECK
								|| (what == MPPErrorCode.MEDIA_INFO_DATA_SOURCE_ERROR && !mediaPlayerDelegate
										.isAdvShowFinished())
								|| (what == MPPErrorCode.MEDIA_INFO_NETWORK_ERROR && mediaPlayerDelegate.isADShowing)
								|| (what == MPPErrorCode.MEDIA_INFO_NETWORK_CHECK && mediaPlayerDelegate.isADShowing);
					}

					// ?????????????????????????
					private void disposeAdErrorLoss(int what) {
						if (mediaPlayerDelegate == null
								|| mediaPlayerDelegate.videoInfo == null) {
							return;
						}
						if (what == MPPErrorCode.MEDIA_INFO_DATA_SOURCE_ERROR
								&& !mediaPlayerDelegate.videoInfo.isAdvEmpty()) {
							DisposableStatsUtils.disposeAdLoss(
									YoukuBasePlayerManager.this
											.getBaseActivity(),
									URLContainer.AD_LOSS_STEP4,
									SessionUnitil.playEvent_session,
									URLContainer.AD_LOSS_MF);
						}
						if (what == MPPErrorCode.MEDIA_INFO_PREPARED_AD_CHECK) {
							DisposableStatsUtils.disposeAdLoss(
									YoukuBasePlayerManager.this
											.getBaseActivity(),
									URLContainer.AD_LOSS_STEP6,
									SessionUnitil.playEvent_session,
									URLContainer.AD_LOSS_MF);
						}
					}
				});

		mediaPlayerDelegate.mediaPlayer
				.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						if (pluginManager == null)
							return;
						pluginManager.onPrepared();

					}
				});

		mediaPlayerDelegate.mediaPlayer
				.setOnSeekCompleteListener(new OnSeekCompleteListener() {

					@Override
					public void onSeekComplete(MediaPlayer mp) {
						if (mediaPlayerDelegate != null) {
							mediaPlayerDelegate.isLoading = false;
						}
						Track.setTrackPlayLoading(true);
						if (pluginManager == null)
							return;

						getBaseActivity().runOnUiThread(new Runnable() {
							public void run() {
								pluginManager.onSeekComplete();
								
								if(mPlayerListener!=null){
									mPlayerListener.onSeekComplete();
								}
							}
						});
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

					@Override
					public void onVideoSizeChanged(MediaPlayer mp, int width,
							int height) {
						if (pluginManager == null)
							return;
						pluginManager.onVideoSizeChanged(width, height);
						if(mPlayerListener!=null){
							mPlayerListener.onVideoSizeChanged(width, height);
						}
						Logger.e(TAG, "onVideoSizeChanged-->" + width + height);
						mediaPlayerDelegate.mediaPlayer.updateWidthAndHeight(
								width, height);

					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnTimeOutListener(new OnTimeoutListener() {

					@Override
					public void onTimeOut() {
						if (mediaPlayerDelegate == null)
							return;
						Logger.d("PlayFlow", "onTimeOut");
						mediaPlayerDelegate.release();
						getBaseActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Track.pause();
								onLoadingFailError();
							}
						});
						if (!isSendPlayBreakEvent
								&& MediaPlayerConfiguration.getInstance()
										.trackPlayError()
								&& mYoukuPlayerView.realVideoStart
								&& mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null) {
							final String videoUrl = mediaPlayerDelegate.videoInfo
									.getWeburl();
							final TaskSendPlayBreak task = new TaskSendPlayBreak(
									videoUrl);
							task.execute();
							isSendPlayBreakEvent = true;
						}

						getBaseActivity().runOnUiThread(new Runnable() {
							public void run() {
								if (pluginManager == null)
									return;
								pluginManager.onTimeout();
								if(mPlayerListener!=null){
									mPlayerListener.onTimeOut();
								}
							}
						});
					}

					@Override
					public void onNotifyChangeVideoQuality() {
						if (pluginManager == null)
							return;
						Logger.d("PlayFlow", "onNotifyChangeVideoQuality");
						pluginManager.onNotifyChangeVideoQuality();
						if(mPlayerListener!=null){
							mPlayerListener.onNotifyChangeVideoQuality();
						}
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnCurrentPositionUpdateListener(new OnCurrentPositionUpdateListener() {

					@Override
					public void onCurrentPositionUpdate(
							final int currentPosition) {
						if (pluginManager == null)
							return;
						getBaseActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								try {
									pluginManager
											.onCurrentPositionChange(currentPosition);
								} catch (Exception e) {
								}
								if(mPlayerListener!=null){
									mPlayerListener.onCurrentPositionUpdate(currentPosition);
								}
							}
						});
					}
				});
		
		if (PlayerUtil.useUplayer()) {
			Logger.d(TAG, "setOnADPlayListener");
			mediaPlayerDelegate.mediaPlayer
					.setOnADPlayListener(new OnADPlayListener() {

						@Override
						public boolean onStartPlayAD(int index) {
							Logger.d("PlayFlow", "onstartPlayAD");
							Logger.d(TAG, "onstartPlayAD");
							
							
							
							Track.onAdStart(YoukuPlayerConfiguration.context,
									mediaPlayerDelegate);
							String vid = "";
							if (mediaPlayerDelegate != null
									&& mediaPlayerDelegate.videoInfo != null)
								vid = mediaPlayerDelegate.videoInfo.getVid();
							Track.trackAdLoad(getBaseActivity(), vid);
							mYoukuPlayerView.setPlayerBlackGone();
							if (mediaPlayerDelegate != null) {
								mediaPlayerDelegate.isADShowing = true;
								mediaPlayerDelegate.isAdStartSended = true;
								if (mediaPlayerDelegate.videoInfo != null
										&& mediaPlayerDelegate.videoInfo.videoAdvInfo != null) {
									if (mediaPlayerDelegate.videoInfo.videoAdvInfo.SKIP != null
											&& mediaPlayerDelegate.videoInfo.videoAdvInfo.SKIP
													.equals("1")) {
										if (null != mPluginADPlay) {
											mPluginADPlay.setSkipVisible(true);
										}
									}

								}
							}
							Logger.d(TAG, "PLUGIN_SHOW_AD_PLAY");
							updatePlugin(PLUGIN_SHOW_AD_PLAY);
							if (null != pluginManager) {
								getBaseActivity().runOnUiThread(new Runnable() {
									public void run() {
										pluginManager.onLoaded();
										if (null != mPluginADPlay) {
											mPluginADPlay.setVisible(true);
										}
									}
								});
							}
							if (mediaPlayerDelegate != null
									&& mediaPlayerDelegate.videoInfo != null) {
								AnalyticsWrapper.adPlayStart(getBaseActivity(),
										mediaPlayerDelegate.videoInfo);
							}
							try {
								DisposableStatsUtils
										.disposeSUS(mediaPlayerDelegate.videoInfo);
							} catch (NullPointerException e) {
								Logger.e("sgh", e.toString());
							}

							if (mediaPlayerDelegate.videoInfo
									.getCurrentAdvInfo() != null
									&& (mediaPlayerDelegate.videoInfo
											.getCurrentAdvInfo().VSC == null || mediaPlayerDelegate.videoInfo
											.getCurrentAdvInfo().VSC
											.equalsIgnoreCase(""))) {
								DisposableStatsUtils
										.disposeVC(mediaPlayerDelegate.videoInfo);
							}

							return false;
						}

						@Override
						public boolean onEndPlayAD(int index) {
							Logger.d("PlayFlow", "onEndPlayAD");

							if (mediaPlayerDelegate != null) {
								mediaPlayerDelegate.isADShowing = false;
							}
							Track.onAdEnd();

							if (mediaPlayerDelegate != null
									&& mediaPlayerDelegate.videoInfo != null) {
								AnalyticsWrapper.adPlayEnd(getBaseActivity(),
										mediaPlayerDelegate.videoInfo);
							}
							// ?????????removePlayedAdv????????????
							DisposableStatsUtils
									.disposeSUE(mediaPlayerDelegate.videoInfo);
							// ??????????????????????????????????????????????????????
							mediaPlayerDelegate.videoInfo.removePlayedAdv();
							if (mediaPlayerDelegate.videoInfo.isCached()) {
								ICacheInfo download = IMediaPlayerDelegate.mICacheInfo;
								if (download != null) {
									if (download
											.isDownloadFinished(mediaPlayerDelegate.videoInfo
													.getVid())) {
										VideoCacheInfo downloadInfo = download
												.getDownloadInfo(mediaPlayerDelegate.videoInfo
														.getVid());
										if (YoukuBasePlayerManager.isHighEnd) {
											mediaPlayerDelegate.videoInfo.cachePath = PlayerUtil
													.getM3u8File(downloadInfo.savePath
															+ "youku.m3u8");
										}
									}
								}
							}

							Logger.e(TAG, "onEndPlayAD");
							return false;
						}
					});
			mediaPlayerDelegate.mediaPlayer
					.setOnADCountListener(new OnADCountListener() {

						@Override
						public void onCountUpdate(final int count) {

							position = mediaPlayerDelegate.getCurrentPosition();
							final int currentPosition = mediaPlayerDelegate
									.getCurrentPosition() / 1000;
							getBaseActivity().runOnUiThread(new Runnable() {
								public void run() {
									mPluginADPlay.notifyUpdate(count);
									mYoukuPlayerView.resizeMediaPlayer(false);

									DisposableStatsUtils.disposeSU(
											mediaPlayerDelegate.videoInfo,
											currentPosition);
								}
							});

						}
					});
			mediaPlayerDelegate.mediaPlayer
					.setOnNetworkSpeedListener(new OnNetworkSpeedListener() {

						@Override
						public void onSpeedUpdate(final int count) {
							if (null != pluginManager) {
								getBaseActivity().runOnUiThread(new Runnable() {
									public void run() {
										pluginManager.onNetSpeedChange(count);
										if(mPlayerListener!=null){
											mPlayerListener.onNetworkSpeedChanged(count);
										}
									}
								});
							}
						}
					});
		}
		mediaPlayerDelegate.mediaPlayer
				.setOnRealVideoStartListener(new OnRealVideoStartListener() {

					@Override
					public void onRealVideoStart() {
						if (onPause)
							return;
						// ??????listener?????????????????????????????????????????????????????????????
						// mediaPlayerDelegate????????????????????????
						Logger.d("PlayFlow", "?????????????????????????????????");
						Track.isRealVideoStarted = true;
						String vid = "";
						if (mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null)
							vid = mediaPlayerDelegate.videoInfo.getVid();
						Track.onRealVideoFirstLoadEnd(getBaseActivity(), vid);
						localStartSetDuration();
						sentonVVBegin();
						mYoukuPlayerView.setPlayerBlackGone();
						if (mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null) {
							mediaPlayerDelegate.isADShowing = false;
							Logger.e(TAG, "onRealVideoStart"
									+ mediaPlayerDelegate.videoInfo.IsSendVV);
						} else {
							Logger.e(TAG,
									"onRealVideoStart mediaPlayerDelegate??????");
						}
						mediaPlayerDelegate.isLoading = false;
						if (null != pluginManager) {
							getBaseActivity().runOnUiThread(new Runnable() {
								public void run() {
									detectPlugin();
									pluginManager.onRealVideoStart();
									pluginManager.onLoaded();
									//Fix ??????????????????setonpreparedlistener????????????????????????????????????????????????????????????????????????????????????
									//?????????????????????????????????
									if(mPlayerListener!=null){
										mPlayerListener.onPrepared();
									}
									if(mPlayerListener!=null){
										mPlayerListener.onRealVideoStart();
									}
								}
							});
						}

						if (mediaPlayerDelegate != null) {
							if (mediaPlayerDelegate.videoInfo != null
									&& mediaPlayerDelegate.videoInfo
											.getProgress() > 1000
									&& !mediaPlayerDelegate.videoInfo.isHLS) {
								mediaPlayerDelegate
										.seekTo(mediaPlayerDelegate.videoInfo
												.getProgress());
								Logger.e(
										"PlayFlow",
										"SEEK TO"
												+ mediaPlayerDelegate.videoInfo
														.getProgress());
							}
						}

					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnLoadingStatusListener(new OnLoadingStatusListener() {

					@Override
					public void onStartLoading() {
						Logger.e(TAG, "onStartLoading");
						if (pluginManager == null || onPause)
							return;
						Track.onPlayLoadingStart(mediaPlayerDelegate.mediaPlayer
								.getCurrentPosition());
						if (mediaPlayerDelegate != null) {
							mediaPlayerDelegate.isLoading = true;
						}
						getBaseActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (pluginManager == null)
									return;
								pluginManager.onLoading();
								if(mPlayerListener!=null){
									mPlayerListener.onStartBuffering();
								}
								if (PlayerUtil.useUplayer()
										&& !mediaPlayerDelegate.videoInfo
												.isUseCachePath())
									mediaPlayerDelegate.loadingPause();
							}

						});
					}

					@Override
					public void onEndLoading() {

						getBaseActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (pluginManager == null)
									return;
								pluginManager.onLoaded();
								if(mPlayerListener!=null){
									mPlayerListener.onEndBuffering();
								}
							}
						});
						Track.onPlayLoadingEnd();
						if (null != mediaPlayerDelegate) {
							mediaPlayerDelegate.isStartPlay = true;
							mediaPlayerDelegate.isLoading = false;
							if (null != mediaPlayerDelegate.videoInfo) {
								id = mediaPlayerDelegate.videoInfo.getVid();
								mediaPlayerDelegate.videoInfo.isFirstLoaded = true;
							}
							// ??????mp4??????????????????????
							if (PlayerUtil.useUplayer()
									&& !mediaPlayerDelegate.videoInfo
											.isUseCachePath())
								mediaPlayerDelegate.start();
						}

						if (!firstLoaded
								&& !isFromLocal()
								&& !PreferenceUtil.getPreferenceBoolean(
										YoukuBasePlayerManager.this
												.getBaseActivity(),
										"video_lock", false)) {
							if (mediaPlayerDelegate != null
									&& !mediaPlayerDelegate.isFullScreen
									&& mediaPlayerDelegate.videoInfo != null
									&& StaticsUtil.PLAY_TYPE_LOCAL
											.equals(mediaPlayerDelegate.videoInfo
													.getPlayType())
									|| !PlayerUtil
											.isYoukuTablet(YoukuBasePlayerManager.this
													.getBaseActivity())) {
								YoukuBasePlayerManager.this
										.getBaseActivity()
										.setRequestedOrientation(
												ActivityInfo.SCREEN_ORIENTATION_SENSOR);
							}
							Logger.d("lelouch",
									"onLoaded setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);");
							firstLoaded = true;
						}

					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnPlayHeartListener(new OnPlayHeartListener() {

					@Override
					public void onPlayHeart() {
						if (mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null)
							Track.trackPlayHeart(getBaseActivity(),
									mediaPlayerDelegate.videoInfo,
									mediaPlayerDelegate.isFullScreen);
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnVideoIndexUpdateListener(new OnVideoIndexUpdateListener() {

					@Override
					public void onVideoIndexUpdate(int currentIndex, int ip) {
						Logger.d("PlayFlow", "onVideoIndexUpdate:"
								+ currentIndex + "  " + ip);
						mPlayerListener.onVideoIndexUpdate(currentIndex, ip);
						
						if (mediaPlayerDelegate != null
								&& mediaPlayerDelegate.videoInfo != null)
							Track.onVideoIndexUpdate(getBaseActivity(),
									currentIndex, ip,
									mediaPlayerDelegate.videoInfo
											.getCurrentQuality());
					}
				});
		mediaPlayerDelegate.mediaPlayer
				.setOnHwDecodeErrorListener(new OnHwDecodeErrorListener() {

					@Override
					public void OnHwDecodeError() {
						Logger.d("PlayFlow", "OnHwDecodeError");
						// DisposableHttpTask task = new DisposableHttpTask(
						// URLContainer.getHwErrorUrl());
						// task.setRequestMethod(DisposableHttpTask.METHOD_POST);
						// task.start();
						MediaPlayerConfiguration.getInstance()
								.setUseHardwareDecode(false);
					}
				});
	}

	protected void sentonVVBegin() {
		if (null != mediaPlayerDelegate) {
			mediaPlayerDelegate.isStartPlay = true;
			if (null != mediaPlayerDelegate.videoInfo
					&& !mediaPlayerDelegate.videoInfo.isFirstLoaded) {
				id = mediaPlayerDelegate.videoInfo.getVid();
				mediaPlayerDelegate.videoInfo.isFirstLoaded = true;
				if (!mediaPlayerDelegate.videoInfo.IsSendVV) {
					mediaPlayerDelegate.onVVBegin();
				}
			}
		}
	}

	protected void localStartSetDuration() { // -------------????????????
		if (mediaPlayerDelegate == null
				|| mediaPlayerDelegate.videoInfo == null)
			return;
		if (StaticsUtil.PLAY_TYPE_LOCAL.equals(mediaPlayerDelegate.videoInfo
				.getPlayType())) {

			mediaPlayerDelegate.videoInfo
					.setDurationMills(mediaPlayerDelegate.mediaPlayer
							.getDuration());
			Logger.e("PlayFow", "?????????????????????????????? :"
					+ mediaPlayerDelegate.mediaPlayer.getDuration());
			getBaseActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (pluginManager != null) {
						pluginManager.onVideoInfoGetted();
					}
					if(mPlayerListener!=null){
						mPlayerListener.onGetInfoSuccess();
					}
				}
			});
		}
	}

	// ??????????????????????
	boolean isPause;

	private DeviceOrientationHelper orientationHelper;

	@Override
	public void onPause() {
		Logger.d("NewDetailActivity#onPause()", "onpause");
		onPause = true;
		super.onPause();
		// if(!ApiManager.getInstance().getApiServiceState())return;
		if (pluginManager != null) {
			// ????????????Activity??????onPause
			pluginManager.onPause();
		}
		if (!mYoukuPlayerView.firstOnloaded) {
			pauseBeforeLoaded = true;
		}
		if (null != handler)
			handler.removeCallbacksAndMessages(null);
		if (mediaPlayerDelegate != null && mediaPlayerDelegate.hasRight) {
			try {
				// 19575 ???????????????????????????loading????
				// if (Profile.PLANTFORM != Plantform.YOUKU)
				// pluginManager.onLoaded();
			} catch (Exception e) {
			}
			int nowPostition = mediaPlayerDelegate.getCurrentPosition();
			if (nowPostition > 0) {
				position = nowPostition;
			}
			mediaPlayerDelegate.isPause = true;
			mediaPlayerDelegate.release();
			mediaPlayerDelegate.isLoading = false;
			if (mediaPlayerDelegate.videoInfo != null)
				mediaPlayerDelegate.videoInfo.isFirstLoaded = false;
			mediaPlayerDelegate.onChangeOrient = true;
		}
		if (surfaceHolder != null && mediaPlayerDelegate.mediaPlayer != null
				&& !mediaPlayerDelegate.hasRight) {
			surfaceHolder.removeCallback(mediaPlayerDelegate.mediaPlayer);
		}
		if (mYoukuPlayerView != null) {
			mYoukuPlayerView.setPlayerBlack();
		}
		Track.pause();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onResume() {
		Logger.d("PlayFlow", "YoukuBasePlayerManager->onResume()");
		onPause = false;
		getBaseActivity().setTitle("");
		super.onResume();
		// if(!ApiManager.getInstance().getApiServiceState()){
		// showApiServiceNotAvailableDialog();
		// return;
		// }else{
		if (null != mYoukuPlayerView) {
			mYoukuPlayerView.onConfigrationChange();
		}
		// ??????????
		if (mediaPlayerDelegate != null && !mediaPlayerDelegate.hasRight)
			return;

		if (null != handler) {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					surfaceHolder = surfaceView.getHolder();
					// ??????android4.4???????????????????????????????????????????????????surfaceChanged????????????????????????
					if (UIUtils.hasKitKat()) {
						surfaceView.requestLayout();
					}
					if (null != mediaPlayerDelegate && null != surfaceHolder) {
						if (!mediaPlayerDelegate.isAdvShowFinished()) {
							if (mediaPlayerDelegate.mAdType == AdType.AD_TYPE_VIDEO) {
								updatePlugin(PLUGIN_SHOW_AD_PLAY);
								if (null != mPluginADPlay) {
									mPluginADPlay.showPlayIcon();
								}
							} else if (mediaPlayerDelegate.mAdType == AdType.AD_TYPE_IMAGE
									&& !isImageADShowing) {
								updatePlugin(PLUGIN_SHOW_NOT_SET);
							}

						} else {
							// ????????????????????????????????????????????????????????????????????????????????????
							if (mPluginADPlay != null
									&& mPluginADPlay.getVisibility() == View.VISIBLE
									&& mPluginADPlay.isCountUpdateVisible()) {
								mPluginADPlay.showPlayIcon();
							}
						}
					}

				}
			}, 100);
		}

		callPluginBack();
		mWaitingLoginResult = false;

		mediaPlayerDelegate.dialogShowing = false;
		mediaPlayerDelegate.goFor3gSetting = false;
		if (mediaPlayerDelegate.isFullScreen) {
			getBaseActivity().getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			changeConfiguration(new Configuration());
		}

		// }

	}

	private void callPluginBack() { // ------------------->????????????
		if (null != mPluginSmallScreenPlay)
			mPluginSmallScreenPlay.back();

		if (null != mPluginFullScreenPlay) {
			mPluginFullScreenPlay.back();
		}
		// ????????????????????????????????????dialog?????????????????????error
		if (pauseBeforeLoaded
				&& !isImageADShowing
				&& !(mAdDialogHint != null && mAdDialogHint.isShowing() && !mWaitingLoginResult)) {
			pluginManager.onError(MPPErrorCode.MEDIA_INFO_PREPARE_ERROR, 0);
			if(mPlayerListener!=null){
				mPlayerListener.onError( MPPErrorCode.MEDIA_INFO_PREPARE_ERROR, 0);
			}
		}
		// ????????????back()??????
		pauseBeforeLoaded = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.d("PlayFlow", "YoukuBasePlayerManager->onDestroy");
		if (mediaPlayerDelegate != null) {
			// mediaPlayerDelegate.onVVEnd();

			Track.forceEnd(getBaseActivity(), mediaPlayerDelegate.videoInfo,
					mediaPlayerDelegate.isFullScreen);
			Track.clear();
			try {
				if (surfaceHolder != null
						&& mediaPlayerDelegate.mediaPlayer != null)
					surfaceHolder
							.removeCallback(mediaPlayerDelegate.mediaPlayer);

				if (null != handler)
					handler.removeCallbacksAndMessages(null);
			} catch (Exception e) {

			}
			if (orientationHelper != null) {
				orientationHelper.disableListener();
				orientationHelper.setCallback(null);
				orientationHelper = null;
			}

			mediaPlayerDelegate.mediaPlayer.setOnPreparedListener(null);
			mediaPlayerDelegate.mediaPlayer.clearListener();
			mediaPlayerDelegate.mediaPlayer = null;
			// RemoteInterface.clear();
			mPluginSmallScreenPlay = null;
			pluginManager = null;
			mPlayerListener=null;
			mPluginFullScreenPlay = null;
			mediaPlayerDelegate = null;
			surfaceView = null;
			surfaceHolder = null;
			mYoukuPlayerView = null;
			youkuContext = null;
			if (--mCreateTime <= 0)
				EGLUtil.setSurfaceHolder(null);
		}
	}

	public void playNoRightVideo(String mUri) { // unused

		if (mUri == null || mUri.trim().equals("")) {
			Profile.from = Profile.PHONE;
			return;
		}
		if (mUri.startsWith("youku://")) {
			mUri = mUri.replaceFirst("youku://", "http://");
		} else {
			Profile.from = Profile.PHONE;
			return;
		}
		// ???????????????????????????????????????????????????????
		// initPlayAndSurface();
		if (mediaPlayerDelegate.videoInfo == null)
			mediaPlayerDelegate.videoInfo = new VideoUrlInfo();
		final int queryPosition = mUri.indexOf("?");
		if (queryPosition != -1) {
			String url = new String(URLUtil.decode(mUri.substring(0,
					queryPosition).getBytes()));
			if (PlayerUtil.useUplayer()) {
				StringBuffer m3u8Url = new StringBuffer();
				m3u8Url.append("#PLSEXTM3U\n#EXT-X-TARGETDURATION:10000\n")
						.append("#EXT-X-VERSION:2\n#EXT-X-DISCONTINUITY\n")
						.append("#EXTINF:10000\n").append(url)
						.append("\n#EXT-X-ENDLIST\n");
				mediaPlayerDelegate.videoInfo.setUrl(m3u8Url.toString());
			} else {
				mediaPlayerDelegate.videoInfo.setUrl(url);
			}
			String[] params = mUri.substring(queryPosition + 1).split("&");
			for (int i = 0; i < params.length; i++) {
				String[] param = params[i].split("=");
				if (param[0].trim().equals("vid")) {
					mediaPlayerDelegate.videoInfo.setVid(param[1].trim());
				}
				if (param[0].trim().equals("title")) {
					try {
						mediaPlayerDelegate.videoInfo.setTitle(URLDecoder
								.decode(param[1].trim(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			return;
		}
		onParseNoRightVideoSuccess();
	}

	public void onParseNoRightVideoSuccess() { // unused
		if (PlayerUtil.useUplayer()) {
			Profile.setVideoType_and_PlayerType(Profile.FORMAT_FLV_HD,
					getBaseActivity());
		} else {
			Profile.setVideoType_and_PlayerType(Profile.FORMAT_3GPHD,
					getBaseActivity());
		}
		Profile.from = Profile.PHONE_BROWSER;
		pluginManager.onVideoInfoGetted();
		if(mPlayerListener!=null){
			mPlayerListener.onGetInfoSuccess();
		}
		pluginManager.onChangeVideo();
		goFullScreen();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		// super.onConfigurationChanged(newConfig);
		Logger.d("PlayFlow", "onConfigurationChange:" + newConfig.orientation);
		if (Profile.PHONE_BROWSER == Profile.from
				|| mediaPlayerDelegate.videoInfo != null
				&& (StaticsUtil.PLAY_TYPE_LOCAL
						.equals(mediaPlayerDelegate.videoInfo.getPlayType()) || mediaPlayerDelegate.videoInfo.isHLS))
			return;
		//20160831??????????????????????????????
//		if (mediaPlayerDelegate.isFullScreen
//				&& PlayerUtil.isYoukuTablet(YoukuBasePlayerManager.this
//						.getBaseActivity())) {
//			return;
//		}
		// ??????????????????????????
		if (isLand()) {
			fullWidth = getBaseActivity().getWindowManager()
					.getDefaultDisplay().getWidth();
			Logger.d("lelouch", "isLand");
			Logger.d("PlayFlow",
					"isTablet:" + PlayerUtil.isYoukuTablet(getBaseActivity()));
			// ??????????????????????
			//20160831??????????????????????????????
			onFullscreenListener();
			getBaseActivity().closeOptionsMenu();
			setPlayerFullScreen(false);
			getBaseActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mYoukuPlayerView.onConfigrationChange();
				}
			});

			if (null != handler) {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (mAdDialogHint != null
								&& mAdDialogHint.isShowing()) {
							mAdDialogHint.cancel();
							mAdDialogHint = null;
						}
					}
				}, 100);
			}
			return;
			
//			getBaseActivity().setRequestedOrientation(
//					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			setPadHorizontalLayout();
//			if (isImageADShowing) {
//				// ???????????????????????????????????????????????????????????????
//				// showPauseAdv();
//			} else {
//				updatePlugin(!mediaPlayerDelegate.isAdvShowFinished() ? PLUGIN_SHOW_AD_PLAY
//						: PLUGIN_SHOW_NOT_SET);
//			}
//			Logger.d("PlayFlow", "????????????????????????");
//			layoutHandler.removeCallbacksAndMessages(null);

		} else {
			// if(Profile.PLANTFORM == Plantform.TUDOU && onPause) return;
			goSmall();
			if (UIUtils.hasKitKat()) {
				showSystemUI(mPluginADPlay);
			}
			orientationHelper.fromUser = false;
			if (isImageADShowing) {
				// ???????????????????????????????????????????????????????????????
				// showPauseAdv();
			} else {
				updatePlugin(!mediaPlayerDelegate.isAdvShowFinished() ? PLUGIN_SHOW_AD_PLAY
						: PLUGIN_SHOW_NOT_SET);
			}
			// fitsSystemWindows???true????????????????????????????????????padding??????????????????
			player_holder.setPadding(0, 0, 0, 0);
			Logger.d("PlayFlow", "??????????????????");
		}

		if (mAdDialogHint != null && mAdDialogHint.isShowing()) {
			mAdDialogHint.cancel();
			mAdDialogHint = null;
		}
	}

	public abstract void setPadHorizontalLayout();

	protected void changeConfiguration(Configuration newConfig) {
		// super.onConfigurationChanged(newConfig);
		Logger.d("PlayFlow", "changeConfiguration");
		if (Profile.PHONE_BROWSER == Profile.from
				|| mediaPlayerDelegate.videoInfo != null
				&& (StaticsUtil.PLAY_TYPE_LOCAL
						.equals(mediaPlayerDelegate.videoInfo.getPlayType()) || mediaPlayerDelegate.videoInfo.isHLS))
			return;
		//20160831??????????????????????????????
//		if (mediaPlayerDelegate.isFullScreen
//				&& PlayerUtil.isYoukuTablet(YoukuBasePlayerManager.this
//						.getBaseActivity())) {
//			return;
//		}
		// ??????????????????????????
		if (isLand()) {
			fullWidth = getBaseActivity().getWindowManager()
					.getDefaultDisplay().getWidth();
			Logger.d("lelouch", "isLand");
			Logger.d("PlayFlow",
					"isTablet:" + PlayerUtil.isYoukuTablet(getBaseActivity()));
			// ??????????????????????
			
			onFullscreenListener();
			getBaseActivity().closeOptionsMenu();
			setPlayerFullScreen(false);
			getBaseActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mYoukuPlayerView.onConfigrationChange();
				}
			});
			// detectPlugin();
			return;
//			getBaseActivity().setRequestedOrientation(
//					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			setPadHorizontalLayout();
//			updatePlugin(!mediaPlayerDelegate.isAdvShowFinished() ? PLUGIN_SHOW_AD_PLAY
//					: PLUGIN_SHOW_NOT_SET);
//			Logger.d("PlayFlow", "????????????????????????");
//			layoutHandler.removeCallbacksAndMessages(null);

		} else {
			// if(Profile.PLANTFORM == Plantform.TUDOU && onPause) return;
			goSmall();
			updatePlugin(!mediaPlayerDelegate.isAdvShowFinished() ? PLUGIN_SHOW_AD_PLAY
					: PLUGIN_SHOW_NOT_SET);
			Logger.d("PlayFlow", "??????????????????");
		}

	}

	// private void setVerticalPlayerSmall(boolean setRequestOreitation) {
	// if(setRequestOreitation)
	// setScreenPortrait();

	private final int GET_LAND_PARAMS = 800; // ------>????????????
	private final int GET_PORT_PARAMS = 801;
	private Handler layoutHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ??????
			case GET_LAND_PARAMS: {
				getBaseActivity().getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				mYoukuPlayerView.resizeMediaPlayer(false);
				changeConfiguration(new Configuration());
				if (null != mediaPlayerDelegate)
					mediaPlayerDelegate.currentOriention = Orientation.LAND;
				break;
			}
			// ??????
			case GET_PORT_PARAMS: {
				getBaseActivity().getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				if (PlayerUtil.isYoukuTablet(YoukuBasePlayerManager.this
						.getBaseActivity())) {
					//orientationHelper.disableListener();
				} else {
					// Bug 7019 ???onConfigurationChanged???videoinfo??????????????????????????????
				}
				if (mediaPlayerDelegate != null
						&& mediaPlayerDelegate.videoInfo != null
						&& StaticsUtil.PLAY_TYPE_LOCAL
								.equals(mediaPlayerDelegate.videoInfo
										.getPlayType())) {
					getBaseActivity().getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
					mediaPlayerDelegate.currentOriention = Orientation.LAND;
					return;
				}
				getBaseActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				if (null != mediaPlayerDelegate)
					mediaPlayerDelegate.currentOriention = Orientation.VERTICAL;
				break;
			}
			}
			if (mediaPlayerDelegate.isFullScreen) {
				mYoukuPlayerView.setBackgroundResource(R.color.black);
			} else {
				mYoukuPlayerView.setBackgroundResource(R.color.white);
			}
		}
	};

	public void setPlayerFullScreen(final boolean lockSensor) {
		if (mediaPlayerDelegate == null)
			return;
		if (lockSensor) {
			if (UIUtils.hasGingerbread()
					&& !PreferenceUtil.getPreferenceBoolean(getBaseActivity(),
							"video_lock", false)) {
				getBaseActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			} else {
				getBaseActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}

		mediaPlayerDelegate.onChangeOrient = true;
		mediaPlayerDelegate.isFullScreen = true;
		if (isImageADShowing) {
			// ???????????????????????????????????????????????????????????????
			// showPauseAdv();
		} else {
			updatePlugin(!mediaPlayerDelegate.isAdvShowFinished() ? PLUGIN_SHOW_AD_PLAY
					: PLUGIN_SHOW_NOT_SET);
		}

		mYoukuPlayerView.setFullscreenBack();
		// ????????????????????????fillpearent
		getBaseActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getBaseActivity().getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		});
	}

	public void goFullScreen() {
		if (null != mediaPlayerDelegate)
			mediaPlayerDelegate.isFullScreen = true;
		onFullscreenListener();
		if (PlayerUtil.isYoukuTablet(getBaseActivity())) {
			//setPlayerFullScreen(true);
		} else {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		}
		if (Profile.from != Profile.PHONE_BROWSER
				&& !PlayerUtil.isYoukuTablet(getBaseActivity())
				&& mediaPlayerDelegate != null
				&& (mediaPlayerDelegate.videoInfo == null || !StaticsUtil.PLAY_TYPE_LOCAL
						.equals(mediaPlayerDelegate.videoInfo.getPlayType()))) {
			orientationHelper.enableListener();
			orientationHelper.isFromUser();
		}
		setPlayerFullScreen(true);
	}

	private void setPlayerSmall(boolean setRequsetOreitation) { // -------???????????????
		mediaPlayerDelegate.isFullScreen = false;
		// if (PlayerUtil.isYoukuTablet(this)
		// && isLand()) {
		// layoutHandler.sendEmptyMessageDelayed(GET_LAND_PARAMS, 0);
		// } else {
		Message message = layoutHandler.obtainMessage();
		message.what = GET_PORT_PARAMS;
		message.obj = setRequsetOreitation;

		layoutHandler.sendMessage(message);

		mYoukuPlayerView.setVerticalLayout();
		// }
	}

	public void goSmall() {
		onSmallscreenListener();
		setPluginHolderPaddingZero();
		mediaPlayerDelegate.isFullScreen = false;

		if (PlayerUtil.isYoukuTablet(getBaseActivity())) {
			//setPlayerSmall(true);
		} else {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		}
		if (mediaPlayerDelegate != null && !mediaPlayerDelegate.isComplete) {
			orientationHelper.enableListener();
			orientationHelper.isFromUser();
		}
		setPlayerSmall(true);
	}

	public void playCompleteGoSmall() {
		onSmallscreenListener();
		// mPluginFullScreenPlay.showSystemUI();
		if (mediaPlayerDelegate == null)
			return;
		if (PlayerUtil.isYoukuTablet(getBaseActivity())) {
			//setPlayerSmall(true);
		} else {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		}
		setPlayerSmall(true);
		Logger.d("test2", "goSmall isLand :" + isLand());
		orientationHelper.enableListener();
		orientationHelper.isFromUser();
		orientationHelper.isFromComplete();
	}

	public void notifyUp() {
		pluginManager.setUp();
	}

	public void notifyDown() {
		pluginManager.setDown();
	}

	// @Override
	public boolean onSearchRequested() { // android????????????
		return mediaPlayerDelegate.isFullScreen;
	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		shouldCallSuperKeyDown = false;
		try {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				// ????????????menu??????????????????????
				if (event.getRepeatCount() > 0) {
					return true;
				}
				return mediaPlayerDelegate.isFullScreen;
			case KeyEvent.KEYCODE_BACK:
				// ????????????????????????????
				if (event.getRepeatCount() > 0) {
					return true;
				}
				if (!mediaPlayerDelegate.isDLNA) {
					if (mediaPlayerDelegate.isFullScreen
							&& !isFromLocal()
							&& (mediaPlayerDelegate.videoInfo != null && !mediaPlayerDelegate.videoInfo.isHLS)) {
						goSmall();
						return true;
					} else {
						onkeyback();
						return true;
					}
				} else {
					return true;
				}
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				return volumeDown();
			case KeyEvent.KEYCODE_VOLUME_UP:
				return volumeUp();
			case KeyEvent.KEYCODE_SEARCH:
				return mediaPlayerDelegate.isFullScreen;
			case 125:
				/** ???????????????????????????popupwindow */
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		shouldCallSuperKeyDown = true;
		return false;
	}

	private boolean shouldCallSuperKeyDown = false;

	public boolean shouldCallSuperKeyDown() {
		return shouldCallSuperKeyDown;
	}

	protected void onkeyback() {
		Logger.d("sgh", "onkeyback");
		try {

			if ((mediaPlayerDelegate != null && mediaPlayerDelegate.videoInfo != null) && mediaPlayerDelegate.videoInfo.isHLS) {
				IRVideoWrapper.videoEnd(getBaseActivity());
				return;
			}

			if (!mediaPlayerDelegate.isStartPlay
					&& !mediaPlayerDelegate.isVVBegin998Send) {
				if (mediaPlayerDelegate.videoInfo == null
						|| TextUtils.isEmpty(mediaPlayerDelegate.videoInfo
								.getVid())) {
					Track.onError(
							getBaseActivity(),
							id,
							Device.guid,
							StaticsUtil.PLAY_TYPE_NET,
							PlayCode.USER_RETURN,
							mediaPlayerDelegate.videoInfo == null ? Source.YOUKU
									: mediaPlayerDelegate.videoInfo.mSource,
							Profile.videoQuality, 0,
							mediaPlayerDelegate.isFullScreen);
					mediaPlayerDelegate.isVVBegin998Send = true;
				} else if (!mediaPlayerDelegate.videoInfo.IsSendVV
						&& !mediaPlayerDelegate.videoInfo.isSendVVEnd) {
					if (mediaPlayerDelegate.isADShowing) {
						Track.onError(getBaseActivity(),
								mediaPlayerDelegate.videoInfo.getVid(),
								Device.guid,
								mediaPlayerDelegate.videoInfo.playType,
								PlayCode.VIDEO_ADV_RETURN,
								mediaPlayerDelegate.videoInfo.mSource,
								mediaPlayerDelegate.videoInfo
										.getCurrentQuality(),
								mediaPlayerDelegate.videoInfo.getProgress(),
								mediaPlayerDelegate.isFullScreen);
					} else {
						Track.onError(
								getBaseActivity(),
								mediaPlayerDelegate.videoInfo.getVid(),
								Device.guid,
								PlayerUtil
										.isBaiduQvodSource(mediaPlayerDelegate.videoInfo.mSource) ? StaticsUtil.PLAY_TYPE_NET
										: mediaPlayerDelegate.videoInfo.playType,
								PlayCode.USER_LOADING_RETURN,
								mediaPlayerDelegate.videoInfo.mSource,
								mediaPlayerDelegate.videoInfo
										.getCurrentQuality(),
								mediaPlayerDelegate.videoInfo.getProgress(),
								mediaPlayerDelegate.isFullScreen);
						if (!mediaPlayerDelegate.videoInfo.isAdvEmpty()) {
							DisposableStatsUtils.disposeAdLoss(
									YoukuBasePlayerManager.this
											.getBaseActivity(),
									URLContainer.AD_LOSS_STEP3,
									SessionUnitil.playEvent_session,
									URLContainer.AD_LOSS_MF);
						}
					}
				}
			}
			mediaPlayerDelegate.isStartPlay = false;
			if (!mediaPlayerDelegate.isVVBegin998Send) {
				mediaPlayerDelegate.onVVEnd();
			} else {
				mediaPlayerDelegate.videoInfo.isSendVVEnd = true;
			}
		} catch (Exception e) {
		} finally {
			// setResult(RESULT_OK);
			Logger.d("sgh", "onkeyback finally finish");
			getBaseActivity().finish();
		}
	}

	/** ???????????? */
	private boolean isMute = false;
	private AudioManager am;
	private int currentSound;

	private void initSound() {
		am = (AudioManager) getBaseActivity().getSystemService(
				Context.AUDIO_SERVICE);
		currentSound = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private boolean volumeUp() {
		if (!isMute) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC, Math.min(
					am.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
					am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
		} else {
			if (currentSound >= 0) {
				am.setStreamVolume(AudioManager.STREAM_MUSIC, Math.min(
						currentSound + 1,
						am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)), 0);
			}

		}
		pluginManager.onVolumnUp();
		return false;
	}

	private boolean volumeDown() {
		if (!isMute) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC,
					am.getStreamVolume(AudioManager.STREAM_MUSIC) - 1, 0);
		} else {
			if (currentSound >= 0) {
				int destSound = currentSound - 1;
				am.setStreamVolume(AudioManager.STREAM_MUSIC,
						destSound >= 0 ? destSound : 0, 0);
			}
		}
		pluginManager.onVolumnDown();
		return false;
	}

	private boolean isFromLocal() {

        return Profile.from == Profile.PHONE_BROWSER
                || (mediaPlayerDelegate.videoInfo != null && StaticsUtil.PLAY_TYPE_LOCAL
                .equals(mediaPlayerDelegate.videoInfo.getPlayType()));

    }

	private void onLoadingFailError() {
		if (null == mediaPlayerDelegate)
			return;
		try {
			mediaPlayerDelegate.onVVEnd();
			// mediaPlayerDelegate.onVVBegin996();
		} catch (Exception e) {

		}
	}

	// @Override
	public void onBackPressed() { // android????????????
		Logger.d("sgh", "onBackPressed before super");
		// super.onBackPressed();
		Logger.d("sgh", "onBackPressed");
		onkeyback();
	}

	public abstract void onInitializationSuccess(YoukuPlayer player);

	/**
	 * ??????????????????????????????????????????????????????????view????????????gone
	 */
	public abstract void onFullscreenListener();

	/**
	 * ?????????????????? ?????????????????????????????????????????????view
	 */
	public abstract void onSmallscreenListener();

	public void onCreateInitialize() {
		getBaseActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		pluginManager = new PluginManager(this);
		// mediaPlayerDelegate = new IMediaPlayerDelegate(pluginManager, this);
		// //?????????MediaPlayerDelegate
		if (mediaPlayerDelegate == null) {
			Logger.d(TAG,
					"onCreateInitialize()  mediaPlayerDelegate== null!!! ");
			PlayerUiUtile.refreshMediaplayerDelegate(youkuContext);
			mediaPlayerDelegate = RemoteInterface.mediaPlayerDelegate;
		}

		Logger.d(TAG,
				"onCreateInitialize() after refreshMediaplayerDelegate... = "
						+ mediaPlayerDelegate);
		if (mediaPlayerDelegate != null) {
			Logger.d(TAG,
					"onCreateInitialize() mediaplayerDelegate initial()... ");
			mediaPlayerDelegate.initial(pluginManager, this);
		}

		initPlayerPart();
		if (!Util.hasInternet()
				&& (mediaPlayerDelegate.videoInfo == null || !mediaPlayerDelegate.videoInfo
						.isCached())) {
			// Util.showTips("!Util.hasInternet(this)");
		} else {
			if ((mediaPlayerDelegate.videoInfo == null || !mediaPlayerDelegate.videoInfo
					.isCached()) && Util.hasInternet() && !Util.isWifi()) {
				// ??????????????????wifi
				// Util.showTips("mediaPlayerDelegate.videoInfo == null ");
			}
			// ????????????????????????
			getIntentData();
//			autoPlay = PreferenceManager.getDefaultSharedPreferences(
//					YoukuBasePlayerManager.this.getBaseActivity()).getBoolean(
//					"ifautoplay", true);
			autoPlay=false;
			if (mediaPlayerDelegate.videoInfo == null) {
			} else {
				pluginManager.onVideoInfoGetted();
				if(mPlayerListener!=null){
					mPlayerListener.onGetInfoSuccess();
				}
			}

		}

		initSound();
	}

	String currentVid;

	public void loadEndInfo(String vid) {
		currentVid = vid;
	}

	public void resizeMediaPlayer(boolean force) {
		mYoukuPlayerView.resizeMediaPlayer(true);
	}

	public void resizeMediaPlayer(int percent) {
		mYoukuPlayerView.resizeVideoView(percent, false);
	}

	@Override
	public void land2Port() { // ??????????????????
		if (!PreferenceUtil.getPreferenceBoolean(getBaseActivity(),
				"video_lock", false)) {
			getBaseActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		Logger.d("lelouch", "land2Port");
	}

	@Override
	public void port2Land() {// ??????????????????
		if (null != orientationHelper)
			orientationHelper.disableListener();
		if (PreferenceUtil.getPreferenceBoolean(getBaseActivity(),
				"video_lock", false)) {
			getBaseActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			layoutHandler.removeCallbacksAndMessages(null);
			getBaseActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	@Override
	public void reverseLand() { // OrientationChangeCallback????????????
		if (null != orientationHelper)
			orientationHelper.disableListener();
		if (!PreferenceUtil.getPreferenceBoolean(getBaseActivity(),
				"video_lock", false)) {
			layoutHandler.removeCallbacksAndMessages(null);
			getBaseActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			getBaseActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		}
	}

	@Override
	public void reversePort() {
		getBaseActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onFullScreenPlayComplete() {

		if (null != orientationHelper)
			orientationHelper.disableListener();
		getBaseActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void setOrientionDisable() {
		if (null != orientationHelper)
			orientationHelper.disableListener();
	}

	public void setOrientionEnable() {
		if (null != orientationHelper)
			orientationHelper.enableListener();
	}

	public void resizeVideoVertical() {

		// mYoukuPlayerView.resizeVideoVertical();
	}

	// ???youkuApplicaiton ??????YoukuBaseActivity
	public static final String TAG_GLOBAL = "YoukuBaseActivity";

	// public static boolean isHighEnd; // ??????????????????
	public static String versionName;
	/** ????????????? */
	public static String ACTIVE_TIME = "";

	public static String NO_WLAN_DOWNLOAD_FLG = "nowlandownload";

	public static String NO_WLAN_UPLOAD_FLG = "nowlanupload";

	/**
	 * 3.0??????Home??????????
	 */

	// Flags associated with the application.
	public static int flags = 7;

	public static boolean isAlertNetWork = false;

	public static final int TIMEOUT = 30000;

	// @Override
	public void onLowMemory() { // android????????????
		// super.onLowMemory();
		Logger.d("----------LowMemory----------");
		System.gc();
	}

	/**
	 * ???????????? ????????????????????????????????????????????????????????????
	 */
	public static String suggest_videotype;

	public static void setSuggestVideoType(String VideoType, Context context) {
		if (PlayerUtil.isNull(VideoType))
			return;

		if (VideoType.equals("5")) {
			Profile.playerType = Profile.PLAYER_OUR;
		} else {
			Profile.playerType = Profile.PLAYER_DEFAULT;
		}
		// if(F.isNeonSupported())
		// try {
		// if(F.getUPlayer(context)!=null)
		// Profile.playerType = Profile.PLAYER_OUR;
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		Logger.d("playertype:" + Profile.playerType);
		// 1- mp4?????????mp4????
		// 2- 3gp
		// 3- flv
		// 4- 3gphd?????????mp4????
		// 5- flvhd
		// 6- m3u8
		suggest_videotype = VideoType;

	}

	public static String getSuggestVideoType() {
		return suggest_videotype;
	}

	public static int getFormat(int videoType) {
		// if (F.isNull(videoType))
		// return "";
		if (videoType == 4) {
			PlayerUtil.out(1, "3gphd");
		} else if (videoType == 1) {
			PlayerUtil.out(1, "mp4");
		} else if (videoType == 2) {
			PlayerUtil.out(1, "3gp");
		} else if (videoType == 5) {
			PlayerUtil.out(1, "flv");
		} else if (videoType == 6) {
			PlayerUtil.out(1, "m3u8");
		}
		return videoType;
	}

	/**
	 * added for Uplayer 2.4.1 ??????url??????????
	 * http://test.api.3g.youku.com/layout/phone2_1
	 * /play.text?point=1&id=XNDQyMDcxODQ4
	 * &pid=a1c0f66d02e2a816&format=6,5,1,7&language
	 * =guoyu&audiolang=1&guid=a83cf513b78750adcab88523857b652d
	 * &ver=2.4.0.1&network=WIFI ???????????????mp4???flv???hd2???????????????????????????????????????????????????????????????????????????URL
	 * m3u8????????????????????????????????????????????????????
	 * 
	 * @param videoType
	 *            ???????????????????????????
	 * @return ?????????????????????format
	 */
	public static String getFormatAll(String videoType) {
		if (PlayerUtil.isNull(videoType))
			return "";
		if (videoType.equals("4")) {
			PlayerUtil.out(1, "3gphd");
		} else if (videoType.equals("2")) {
			PlayerUtil.out(1, "3gp");
		} else if (videoType.equals("6")) {
			PlayerUtil.out(1, "m3u8");
		} else if (videoType.equals("5")) {
			PlayerUtil.out(1, "flv");
			return "1,5,7"; // ??????????
		} else if (videoType.equals("7")) {
			PlayerUtil.out(1, "hd2");
			return "1,5,7"; // ??????????
		} else if (videoType.equals("1")) {
			PlayerUtil.out(1, "mp4");
			return "1,5,7"; // ??????????
		}
		return videoType;
	}

	public static boolean isnofreedata(String name) {
		if (name.startsWith("GT-I9228") || name.startsWith("Note")
				|| name.startsWith("9220") || name.startsWith("I889")
				|| name.startsWith("I717") || name.startsWith("I9228")) {
			Logger.e("Youku", "isnofreedata ERROR");
			return true;
		}
		return false;
	}

	/**
	 * ????????????
	 * 
	 * @param isADshowing
	 *            ????????????????????????
	 */
	public void updatePlugin(final int pluginID) {
		Logger.e(TAG, "???????????? updatePlugin:"+pluginID);
		Logger.e(TAG, "pluginManager == null"+(pluginManager == null));
		if (pluginManager == null) {
			return;
		}
		getBaseActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				switch (pluginID) {
				case PLUGIN_SHOW_AD_PLAY: {
					Logger.e(TAG, "addPluginAD");
					pluginManager.addPlugin(mPluginADPlay, player_holder);
					break;
				}
				case PLUGIN_SHOW_IMAGE_AD: {
					// ????????????
					isImageADShowing = true;
					pluginManager.addPlugin(mPluginADPlay, player_holder);
					break;
				}
				case PLUGIN_SHOW_INVESTIGATE: {
					break;
				}
				default:
					detectPlugin();
					break;
				}
			}

		});
	}

	protected void detectPlugin() {
		if (pluginManager == null) {
			return;
		}
		if (mediaPlayerDelegate != null && mediaPlayerDelegate.isFullScreen) {
			if (mPluginFullScreenPlay == null) {
				// ??????????????????????????????
				pluginManager.addPlugin(mPluginSmallScreenPlay, player_holder);
				mPluginSmallScreenPlay.pluginEnable = true;
				return;
			}
			pluginManager.addPlugin(mPluginFullScreenPlay, player_holder);
			if (mediaPlayerDelegate.videoInfo == null) {
				return;
			}
		} else {
			pluginManager.addPlugin(mPluginSmallScreenPlay, player_holder);
			mPluginSmallScreenPlay.pluginEnable = true;
		}
	}

	boolean isPauseADShowing = false;

	// public boolean onPause;
	// public boolean isImageADShowing = false;

	/**
	 * ????????????????????????
	 */
	// public boolean pauseBeforeLoaded = false;

	public void notifyFav() {
		setFav();
		if (pluginManager != null)
			pluginManager.setFav();
	}

	private void setFav() {
		if (mediaPlayerDelegate != null
				&& mediaPlayerDelegate.videoInfo != null) {
			mediaPlayerDelegate.videoInfo.isFaved = true;
		}
	}

	public void clearUpDownFav() {
		if (pluginManager != null)
			pluginManager.clearUpDownFav();
	}

	/**
	 * ???????????????????????????????????????????????????????
	 */
	public void onVideoChange() {

	}

	public void setmPluginFullScreenPlay(PluginOverlay mPluginFullScreenPlay) {
		this.mPluginFullScreenPlay = mPluginFullScreenPlay;
	}

	public void setmPluginSmallScreenPlay(PluginOverlay mPluginSmallScreenPlay) {
		this.mPluginSmallScreenPlay = mPluginSmallScreenPlay;
	}

	public static VideoUrlInfo getRecordFromLocal(VideoUrlInfo mVideoUrlInfo) { // ????????????SDK????????????
		if (mVideoUrlInfo.getVid() != null
				&& IMediaPlayerDelegate.mIVideoHistoryInfo != null) {
			VideoHistoryInfo mVideoInfo = IMediaPlayerDelegate.mIVideoHistoryInfo
					.getVideoHistoryInfo(mVideoUrlInfo.getVid());
			if (mVideoInfo != null) {
				int playHistory = mVideoInfo.playTime * 1000;
				if (playHistory > mVideoUrlInfo.getProgress())
					mVideoUrlInfo.setProgress(playHistory);
			}
		}
		return mVideoUrlInfo;
	}

	/**
	 * @param ispause
	 *            ????????????????????????
	 */
	public void startPlay() {
		if (mYoukuPlayerView != null) {
			mYoukuPlayerView.setDebugText("YoukuBasePlayerManager startPlay ");
		}
		if (mediaPlayerDelegate == null)
			return;
		if (mediaPlayerDelegate.isPause
				&& mediaPlayerDelegate.isAdvShowFinished()) {
			mediaPlayerDelegate.isPause = false;
		} else {
			mediaPlayerDelegate.start();
			mediaPlayerDelegate.seekToPausedADShowing(position);
		}
	}

	private boolean isLocalPlay() {
		return Profile.USE_SYSTEM_PLAYER
				|| (mediaPlayerDelegate != null
						&& mediaPlayerDelegate.videoInfo != null && StaticsUtil.PLAY_TYPE_LOCAL
							.equals(mediaPlayerDelegate.videoInfo.playType));
	}

	private boolean loadingADOverTime() {
		mediaPlayerDelegate.playVideoWhenADOverTime();
		updatePlugin(PLUGIN_SHOW_NOT_SET);
		if (pluginManager != null) {
			getBaseActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					pluginManager.onVideoInfoGetting();
					if(mPlayerListener!=null){
						mPlayerListener.onGetInfoSuccess();
					}
					pluginManager.onVideoInfoGetted();
					pluginManager.onLoading();
				}
			});
		}
		return true;
	}

	public void alertRetry() {
		getBaseActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// if (mPluginSmallScreenPlay != null)
				// mPluginSmallScreenPlay.showAlert();
			}
		});
	}

	private void getIntentData() {
		Intent intent = getBaseActivity().getIntent();
		if (null != intent && null != intent.getExtras()) {
			String tidString = intent.getExtras().getString("video_id");
			id = tidString;
		}
	}

	public void setDebugInfo(String string) {
		if (null != mYoukuPlayerView) {
			mYoukuPlayerView.setDebugText(string);
		}
	}

	public void interuptAD() {
		if (mediaPlayerDelegate != null) {
			mediaPlayerDelegate.isADShowing = false;
			getBaseActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					detectPlugin();
				}
			});
		}
	}

	public void setPlayerBlack() {
		if (mYoukuPlayerView != null) {
			mYoukuPlayerView.setPlayerBlack();
		}
	}

	public void playReleateNoRightVideo() {
		pluginManager.onPlayReleateNoRightVideo();
	}

	public int getVideoPosition() {
		return position;
	}

	public void showDialog() {
		showPasswordInputDialog(R.string.player_error_dialog_password_required);
	}

	class DialogPositiveClick implements OnPositiveClickListener {

		@Override
		public void onClick(String passWord) {
			doPositiveClick(passWord);
		}
	}

	private void doPositiveClick(String password) {
		MediaPlayerConfiguration.getInstance().mPlantformController
				.playVideoWithPassword(mediaPlayerDelegate, password);
	}

	private void doNegativeClick() {

	}

	// public class PasswordInputDialog{

	// public static PasswordInputDialog newInstance(int title) {
	// PasswordInputDialog frag = new PasswordInputDialog();
	// Bundle args = new Bundle();
	// args.putInt("title", title);
	// frag.setArguments(args);
	// return frag;
	// }

	private void showPasswordInputDialog(int title) {

		if (getBaseActivity() == null || getBaseActivity().isFinishing()) {
			// ????????????????????????????????????????????????
			return;
		}

		LayoutInflater factory = LayoutInflater.from(getBaseActivity());
		final View textEntryView = factory.inflate(
				R.layout.yp_youku_dialog_password_interact, null);
		AlertDialog dlg = new AlertDialog.Builder(getBaseActivity())
				.setTitle(title)
				.setView(textEntryView)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								EditText passwordEditText = textEntryView
										.findViewById(R.id.password_edit);
								String password = passwordEditText.getText()
										.toString();

								YoukuBasePlayerManager.this
										.doPositiveClick(password);
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								YoukuBasePlayerManager.this.doNegativeClick();
							}
						}).create();

		dlg.show();
	}

	// }

	public void onStart() {
		// super.onStart();
		if (pluginManager != null)
			pluginManager.onStart();
		
	}

	public void onPayClick() {
		if (mediaPlayerDelegate != null
				&& IMediaPlayerDelegate.mIPayCallBack != null
				&& mediaPlayerDelegate.videoInfo != null) {
			mediaPlayerDelegate.release();
			mediaPlayerDelegate.onVVEnd();
			IMediaPlayerDelegate.mIPayCallBack.needPay(
					mediaPlayerDelegate.videoInfo.getVid(),
					mediaPlayerDelegate.videoInfo.mPayInfo);
		}
	}

	@SuppressLint("NewApi")
	public void hideSystemUI(PluginOverlay plugin) {
		if (mediaPlayerDelegate.isFullScreen) {
			plugin.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
	}

	@SuppressLint("NewApi")
	public void showSystemUI(PluginOverlay plugin) {
		plugin.setSystemUiVisibility(0);
	}

	public void setPluginHolderPaddingZero() {
		if (player_holder != null) {
			player_holder.setPadding(0, 0, 0, 0);
		}
	}

	public void recreateSurfaceHolder() {
		surfaceView.recreateSurfaceHolder();
	}

	@Override
	public void showPauseAdv() {
		updatePlugin(PLUGIN_SHOW_IMAGE_AD);
	}
	public void setPlayerListener(YoukuPlayerListener mPlayerListener) {
		this.mPlayerListener = mPlayerListener;
		if(mPluginSmallScreenPlay!=null){
			mPluginSmallScreenPlay.youkuPlayerListener=mPlayerListener;
		}
	}

}