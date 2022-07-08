package com.robam.roki.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.provider.MediaStore.Images.Thumbnails.MICRO_KIND
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.robam.roki.R
import com.robam.roki.ui.widget.view.PlayerView
import kotlinx.android.synthetic.main.widget_video_player_view.view.*
import java.io.IOException
import tv.danmaku.ijk.media.player.IjkMediaPlayer

import java.util.*
import android.view.Gravity

import android.view.SurfaceHolder

import android.view.SurfaceView
import android.widget.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.robam.base.action.ActivityAction
import com.robam.common.pojos.Images
import java.lang.IllegalArgumentException
import java.lang.RuntimeException


class AudioFocusHelper public constructor() {
    private var mAudioFocusChangeListener: OnAudioFocusChangeListener? = null
    private var mAudioManager: AudioManager? = null
    private var mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK

    /**
     * 设置 audiofocus changelistener,只调一次
     */
    fun setAudioFocusChangeListener(listener: OnAudioFocusChangeListener?) {
        if (null == mAudioManager && null != mContext) {
            mAudioManager = mContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        mAudioFocusChangeListener = listener

    }




//    /**
//     * get AudioFocus,播放前调用,需先调用setAudioFocusChangeListener
//     * return true: focus success
//     */
//    fun startFocus(): Boolean {
//
//        if (null != mAudioManager && mAudioFocus != AUDIO_FOCUSED) {
//            val result = mAudioManager!!.requestAudioFocus(
//                mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN
//            )
//            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                mAudioFocus = AUDIO_FOCUSED
//            }
//        }
//        return if (mAudioFocus == AUDIO_FOCUSED) {
//            true
//        } else {
//            false
//        }
//    }

    /**
     * give up AudioFocus，播放完成调用
     * return true: stop success
     */
    fun stopFocus(): Boolean {

        if (null != mAudioManager && mAudioFocus == AUDIO_FOCUSED) {
            if (mAudioManager!!.abandonAudioFocus(mAudioFocusChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK
            }
        }
        return if (mAudioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            true
        } else {
            false
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        mAudioManager = null
        instance = null
    }

    companion object {
        private var mContext: Context? = null
        private const val TAG = "AudioFocusHelper"

        @Volatile
        private var instance // 实例
                : AudioFocusHelper? = null
        private const val AUDIO_NO_FOCUS_NO_DUCK = 0
        private const val AUDIO_FOCUSED = 2
        fun getInstance(context: Context?): AudioFocusHelper? {
            mContext = context
            if (instance == null) {
                synchronized(AudioFocusHelper::class.java) {
                    if (instance == null) {
                        instance = AudioFocusHelper()
                    }
                }
            }
            return instance
        }
    }

    init {
        if (null != mContext) {
            mAudioManager = mContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
    }
}


abstract class VideoPlayerListener : IMediaPlayer.OnBufferingUpdateListener,
    IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener,
    IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener,
    IMediaPlayer.OnSeekCompleteListener


class IjkPlayerView: FrameLayout, ActivityAction, LifecycleEventObserver {



    /** 刷新间隔  */
    private val REFRESH_TIME = 1000

    /** 面板隐藏间隔  */
    private val CONTROLLER_TIME = 3000

    /** 提示对话框隐藏间隔  */
    private val DIALOG_TIME = 500

    /** 显示面板  */
    private var mControllerShow = true

    constructor(context: Context, attrs: AttributeSet?):super(context,attrs) {

        initVideoView(context);
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context,attrs,defStyleAttr) {
        initVideoView(context);
    }
    init {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        } catch (e: Exception) {


        }
    }


    constructor(context: Context):super(context){
        initVideoView(context);

    }

    /** 音量管理器  */
    private lateinit var mAudioManager: AudioManager

    private var mPath = ""
    private  var mMediaPlayer: IMediaPlayer?=null

    private  var listener: VideoPlayerListener? = null
    private lateinit var mContext: Context



    fun conversionTime(time: Long): String? {
        val formatter = Formatter(Locale.getDefault())
        // 总秒数
        val totalSeconds = time / 1000
        // 小时数
        val hours = totalSeconds / 3600
        // 分钟数
        val minutes = totalSeconds / 60 % 60
        // 秒数
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            formatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    /**
     * 显示面板
     */
    fun showController() {
        if (mControllerShow) {
            return
        }
        mControllerShow = true
        ObjectAnimator.ofFloat(ll_player_view_top, "translationY", -ll_player_view_top.height.toFloat(), 0f)
            .start()
        ObjectAnimator.ofFloat(
            ll_player_view_bottom,
            "translationY",
            ll_player_view_bottom.getHeight().toFloat(),
            0f
        ).start()
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 500
        animator.addUpdateListener { animation: ValueAnimator ->
            val alpha = animation.animatedValue as Float
            iv_player_view_lock.alpha = alpha
            iv_player_view_control.alpha = alpha
            if (alpha.toInt() != 1) {
                return@addUpdateListener
            }
            if (iv_player_view_lock.visibility == INVISIBLE) {
                iv_player_view_lock.visibility = VISIBLE
            }
            if (iv_player_view_control.visibility == INVISIBLE) {
                iv_player_view_control.visibility = VISIBLE
            }
        }
        animator.start()
    }
    /**
     * 显示控制面板
     */
    private val mShowControllerRunnable = Runnable {
        if (!mControllerShow) {
            showController()
        }
    }

    /**
     * 隐藏面板
     */
    fun hideController() {
        if (!mControllerShow) {
            return
        }
        mControllerShow = false
        ObjectAnimator.ofFloat(ll_player_view_top, "translationY", 0f, -ll_player_view_top.getHeight().toFloat())
            .start()
        ObjectAnimator.ofFloat(
            ll_player_view_bottom,
            "translationY",
            0f,
            ll_player_view_bottom.getHeight().toFloat()
        ).start()
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.duration = 500
        animator.addUpdateListener { animation: ValueAnimator ->
            val alpha = animation.animatedValue as Float
            iv_player_view_lock.setAlpha(alpha)
            iv_player_view_control.setAlpha(alpha)
            if (alpha != 0f) {
                return@addUpdateListener
            }
            if (iv_player_view_lock.visibility == VISIBLE) {
                iv_player_view_lock.visibility = INVISIBLE
            }
            if (iv_player_view_control.visibility == VISIBLE) {
                iv_player_view_control.visibility = INVISIBLE
            }
        }
        animator.start()
    }
    /**
     * 隐藏控制面板
     */
    private val mHideControllerRunnable = Runnable {
        if (mControllerShow) {
            hideController()
        }
    }
    var mCurrentProgress=1

    /**
     * 锁定控制面板
     */
    fun lock() {
        mLockMode = true
        iv_player_view_lock.setImageResource(R.drawable.video_lock_close_ic)
        ll_player_view_top.setVisibility(GONE)
        ll_player_view_bottom.setVisibility(GONE)
        iv_player_view_control.setVisibility(GONE)
        // 延迟隐藏控制面板
        removeCallbacks(mHideControllerRunnable)
        postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
    }


    /**
     * 解锁控制面板
     */
    fun unlock() {
        mLockMode = false
        iv_player_view_lock.setImageResource(R.drawable.video_lock_open_ic)
        ll_player_view_top.visibility = VISIBLE
        if (mMediaPlayer?.isPlaying == true) {
            ll_player_view_bottom.visibility = VISIBLE
        }
        iv_player_view_control.visibility = VISIBLE
        // 延迟隐藏控制面板
        removeCallbacks(mHideControllerRunnable)
        postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
    }

    lateinit var mHandler: Handler
    private fun initVideoView(context: Context) {
        mContext=context;


        this.mHandler=object:Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                Log.e("发送","----");
                ijk_player_view_video_frame_show.setImageBitmap(msg?.obj as Bitmap)
            }
        }
        LayoutInflater.from(context).inflate(R.layout.widget_video_player_view,this,true)
         setOnClickListener {
             // 先移除之前发送的
             removeCallbacks(mShowControllerRunnable)
             removeCallbacks(mHideControllerRunnable)
             if (mControllerShow) {
                 // 隐藏控制面板
                 post(mHideControllerRunnable)
             } else {
                 // 显示控制面板
                 post(mShowControllerRunnable)
                 postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
             }
         }
        sb_player_view_progress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tv_player_view_play_time.text = PlayerView.conversionTime(progress)
                    return
                }
                if (progress != 0) {
                    // 记录当前播放进度
                    mCurrentProgress = progress
                } else {
                    // 如果 Activity 返回到后台，progress 会等于 0，而 mVideoView.getDuration 会等于 -1
                    // 所以要避免在这种情况下记录当前的播放进度，以便用户从后台返回到前台的时候恢复正确的播放进度
                    if (mMediaPlayer?.duration!! > 0) {
                        mCurrentProgress = progress
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                removeCallbacks(mRefreshRunnable)
                removeCallbacks(mHideControllerRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                postDelayed(mRefreshRunnable,REFRESH_TIME.toLong())
                postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
                // 设置选择的播放进度
                // 设置选择的播放进度
                setProgress(seekBar!!.progress)
            }


        })

        iv_player_view_control.setOnClickListener{

            mMediaPlayer?.let {

                if (it.isPlaying) {
                    pause()
                } else {
                    start()
                }
                // 先移除之前发送的
                // 先移除之前发送的
                removeCallbacks(mShowControllerRunnable)
                removeCallbacks(mHideControllerRunnable)
                // 重置显示隐藏面板任务
                // 重置显示隐藏面板任务
                if (!mControllerShow) {
                    post(mShowControllerRunnable)
                }
                postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())


            }?: run {
                load()
            }



        }


        iv_player_view_lock.setOnClickListener {

            if (mLockMode) {
                unlock()
            } else {
                lock()
            }

        }


        mAudioManager = ContextCompat.getSystemService(
            getContext(),
            AudioManager::class.java
        )!!


        createSurfaceView()


        mAudioManager =
            mContext.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
       mAudioFocusHelper = AudioFocusHelper()
    }

    lateinit var mAudioFocusHelper:AudioFocusHelper

    //创建一个新的player
    private fun createPlayer(): IMediaPlayer? {
        val ijkMediaPlayer = IjkMediaPlayer()
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
        return ijkMediaPlayer
    }



    public fun setMute(){
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    public fun setMute2(){
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
    }
   private fun setLister(){

       setListener(object :VideoPlayerListener(){
            override fun onBufferingUpdate(p0: IMediaPlayer, precent: Int) {
                sb_player_view_progress.secondaryProgress = ((precent / 100f * p0.duration).toInt())
                var progress: Int = p0.currentPosition.toInt()
                // 这里优化了播放的秒数计算，将 800 毫秒估算成 1 秒
                // 这里优化了播放的秒数计算，将 800 毫秒估算成 1 秒
                if (progress + 1000 < mMediaPlayer?.getDuration()!!) {
                    // 进行四舍五入计算
                    progress = Math.round(progress / 1000f) * 1000
                }
                sb_player_view_progress.progress=progress

                tv_player_view_play_time.text = PlayerView.conversionTime(progress)
            }

            override fun onCompletion(p0: IMediaPlayer?) {


            }

            override fun onPrepared(p0: IMediaPlayer) {
                tv_player_view_total_time.text = conversionTime(p0?.duration)
                sb_player_view_progress.max = p0.duration.toInt()

                val params: ViewGroup.LayoutParams = fragment_player_layout.layoutParams
                params.width=width
                params.height=p0.videoHeight*width/p0.videoWidth
                ijk_player_view_video.layoutParams = params
                fragment_player_layout.layoutParams = params
                start()


            }

            override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {

                return false
            }

            override fun onVideoSizeChanged(p0: IMediaPlayer?, width:Int, height:Int, sar_num:Int, sar_den:Int) {

                // 重新设置 VideoView 的宽高
//                val params: ViewGroup.LayoutParams = fragment_player_layout.layoutParams
//                params.width = getWidth()
//                params.height = height* getWidth()/width
//
//                fragment_player_layout.layoutParams = params
            }

            override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                return false
            }

            override fun onSeekComplete(p0: IMediaPlayer?) {

            }


        })
        if (listener != null) {
            mMediaPlayer?.setOnPreparedListener(listener)
            mMediaPlayer?.setOnInfoListener(listener)
            mMediaPlayer?.setOnSeekCompleteListener(listener)
            mMediaPlayer?.setOnBufferingUpdateListener(listener)
            mMediaPlayer?.setOnErrorListener(listener)
        }


    }


    //设置是否开启硬解码
    private fun setEnableMediaCodec(ijkMediaPlayer: IjkMediaPlayer, isEnable: Boolean) {
        val value = if (isEnable) 1 else 0
        ijkMediaPlayer.setOption(
            IjkMediaPlayer.OPT_CATEGORY_PLAYER,
            "mediacodec",
            value.toLong()
        ) //开启硬解码
        ijkMediaPlayer.setOption(
            IjkMediaPlayer.OPT_CATEGORY_PLAYER,
            "mediacodec-auto-rotate",
            value.toLong()
        )
        ijkMediaPlayer.setOption(
            IjkMediaPlayer.OPT_CATEGORY_PLAYER,
            "mediacodec-handle-resolution-change",
            value.toLong()
        )
    }


    private fun createSurfaceView() {
        ijk_player_view_video.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {}
            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                if (mMediaPlayer != null) {
                    mMediaPlayer!!.setDisplay(surfaceHolder)
                }
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
        })
//        val layoutParams = LayoutParams(
//            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER
//        )
//        //        mSurfaceView.setLayoutParams(layoutParams);
//        addView(mSurfaceView, 0, layoutParams)
    }


    /**
     * 设置视频播放进度
     */
    fun setProgress(progress: Int) {
        var progress = progress
        if (progress > getDuration()) {
            progress = getDuration().toInt()
        }
        if (Math.abs(progress - getCurrentPosition()) > 1000) {
            seekTo(progress.toLong())
            setProgress(progress)
        }
    }

    /** 锁定面板  */
    private var mLockMode = false
    /**
     * 刷新任务
     */
    private val mRefreshRunnable: Runnable = object : Runnable {
        override fun run() {
            mMediaPlayer?.let {

                var progress: Int = it.getCurrentPosition().toInt()
                // 这里优化了播放的秒数计算，将 800 毫秒估算成 1 秒
                if (progress + 1000 < it.getDuration()) {
                    // 进行四舍五入计算
                    progress = Math.round(progress / 1000f) * 1000
                }
                tv_player_view_play_time.text =conversionTime(progress.toLong())
                sb_player_view_progress.progress = progress

                if (it.isPlaying()) {

                    if (!mLockMode && ll_player_view_bottom.visibility == GONE) {
                        ll_player_view_bottom.visibility = VISIBLE
                    }
                } else {
                    if (ll_player_view_bottom.visibility == VISIBLE) {
                        ll_player_view_bottom.visibility = GONE
                    }
                }
//                if (mListener != null) {
////                    mListener.onPlayProgress(this)
//                }
                postDelayed(this, REFRESH_TIME.toLong())



            }

        }
    }

//    override fun setVideoPath(path:String){
//         this.mPath=path
//
//        if (TextUtils.equals("", mPath)) {
//            //如果是第一次播放视频，那就创建一个新的surfaceView
//            mPath = path;
//            createSurfaceView();
//        } else {
//            //否则就直接load
//            mPath = path;
//            load();
//        }
//
//    }




    /**
     * surfaceView的监听器
     */
//    inner class LmnSurfaceCallback : SurfaceHolder.Callback {
//        override fun surfaceCreated(holder: SurfaceHolder) {}
//        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//            //surfaceview创建成功后，加载视频
//            load()
//        }
//
//        override fun surfaceDestroyed(holder: SurfaceHolder) {}
//    }

//    /**
//     * 加载视频
//     */
//    private fun load() {
//        //每次都要重新创建IMediaPlayer
//        createPlayer()
//        try {
//            mMediaPlayer?.dataSource = mPath
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        //给mediaPlayer设置视图
//        mMediaPlayer?.setDisplay(ijk_player_view_video.holder)
//        mMediaPlayer?.prepareAsync()
//    }

    /** 手势开关  */
    private var mGestureEnabled = false

    /** 最大音量值  */
    private var mMaxVoice = 0

    /** 当前音量值  */
    private var mCurrentVolume = 0

    /** 当前亮度值  */
    private var mCurrentBrightness = 0f

    /** 当前窗口对象  */
    private var mWindow: Window? = null


    /** 触摸按下的 X 坐标  */
    private var mViewDownX = 0f

    /** 触摸按下的 Y 坐标  */
    private var mViewDownY = 0f


    /** 触摸方向  */
    private var mTouchOrientation = -1

    /**
     * 显示提示
     */
    private val mShowMessageRunnable = Runnable {
        hideController()
        cv_player_view_message.setVisibility(VISIBLE)
    }

    /**
     * 隐藏提示
     */
    private val mHideMessageRunnable = Runnable {
        cv_player_view_message.setVisibility(
            GONE
        )
    }

    //设置播放地址
    fun setPath(path: String) {
      this.mPath=path
        Thread{
            var bitmap=  createVideoThumbnail(path,width,height)
            var message=Message()
            message.what=0x12
            message.obj=bitmap
            mHandler.sendMessage(message)
        }.start()

//        ijk_player_view_video_frame_show.scaleType= ImageView.ScaleType.CENTER_CROP
//        Glide.with(context)
//            .setDefaultRequestOptions(
//                RequestOptions()
//                    .frame(1000000)
//                    .centerCrop()
//                    .error(R.drawable.abc_vector_test)
//            )
//            .load(path)
//            .into(ijk_player_view_video_frame_show)
    }

    private fun createVideoThumbnail(url: String, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        val kind: Int = MediaStore.Video.Thumbnails.MINI_KIND
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, HashMap<String, String>())
            } else {
                retriever.setDataSource(url)
            }
            bitmap = retriever.frameAtTime
        } catch (ex: IllegalArgumentException) {
            // Assume this is a corrupt video file
        } catch (ex: RuntimeException) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

    /** 调整秒数  */
    private var mAdjustSecond = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {


        // 满足任一条件：关闭手势控制、处于锁定状态、处于缓冲状态
        if (!mGestureEnabled || mLockMode || lav_player_view_lottie.isAnimating()) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mMaxVoice = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                //                mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mCurrentVolume = 0
                mWindow = getActivity().getWindow()
                mCurrentBrightness = mWindow?.getAttributes()?.screenBrightness!!
                // 如果当前亮度是默认的，那么就获取系统当前的屏幕亮度
                if (mCurrentBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
                    try {
                        mCurrentBrightness = Settings.System.getInt(
                            context.contentResolver,
                            Settings.System.SCREEN_BRIGHTNESS
                        ) / 255f
                    } catch (ignored: SettingNotFoundException) {
                        mCurrentBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
                    }
                }
                mViewDownX = event.x
                mViewDownY = event.y
                removeCallbacks(mHideControllerRunnable)
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算偏移的距离（按下的位置 - 当前触摸的位置）
                val distanceX: Float = mViewDownX - event.x
                val distanceY: Float = mViewDownY - event.y
                // 手指偏移的距离一定不能太短，这个是前提条件
                if (Math.abs(distanceY) < ViewConfiguration.get(context).scaledTouchSlop) {
                    return true
                }
                if (mTouchOrientation == -1) {
                    // 判断滚动方向是垂直的还是水平的
                    if (Math.abs(distanceY) > Math.abs(distanceX)) {
                        mTouchOrientation = LinearLayout.VERTICAL
                    } else if (Math.abs(distanceY) < Math.abs(distanceX)) {
                        mTouchOrientation = LinearLayout.HORIZONTAL
                    }
                }

                // 如果手指触摸方向是水平的
                if (mTouchOrientation == LinearLayout.HORIZONTAL) {
                    val second = (-(distanceX / width.toFloat() * 60f)).toInt()
                    val progress: Int = getCurrentPosition().toInt() + second * 1000
                    if (progress >= 0 && progress <= getDuration()) {
                        mAdjustSecond = second
                        lav_player_view_lottie.setImageResource(if (mAdjustSecond < 0) R.drawable.video_schedule_rewind_ic else R.drawable.video_schedule_forward_ic)
                        tv_player_view_message.setText(String.format("%s s", Math.abs(mAdjustSecond)))
                        post(mShowMessageRunnable)
                    }

                }

                // 如果手指触摸方向是垂直的
                if (mTouchOrientation == LinearLayout.VERTICAL) {

                    if (event.x.toInt()<  width / 2){
                        // 手指在屏幕左边

                        val delta =
                            distanceY / height * WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
                        if (delta == 0f) {

                        }

                        // 更新系统亮度
                        val brightness = Math.min(
                            Math.max(
                                mCurrentBrightness + delta,
                                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
                            ),
                            WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
                        )
                        val attributes: WindowManager.LayoutParams? = mWindow?.getAttributes()
                        attributes?.screenBrightness = brightness
                        mWindow?.attributes = attributes
                        val percent = (brightness * 100).toInt()
                        @DrawableRes val iconId: Int
                        iconId = if (percent > 100 / 3 * 2) {
                            R.drawable.video_brightness_high_ic
                        } else if (percent > 100 / 3) {
                            R.drawable.video_brightness_medium_ic
                        } else {
                            R.drawable.video_brightness_low_ic
                        }
                        lav_player_view_lottie.setImageResource(iconId)
                        tv_player_view_message.setText(String.format("%s %%", percent))
                        post(mShowMessageRunnable)
                        return true
                    }

                    // 手指在屏幕右边
                    val delta: Float = distanceY / height * mMaxVoice
                    if (delta == 0f) {
                        return true
                    }

                    // 更新系统音量
                    val voice =
                        (mCurrentVolume + delta).coerceAtLeast(0f).coerceAtMost(mMaxVoice.toFloat())
                            .toInt()
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice, 0)
                    val percent: Int = voice * 100 / mMaxVoice
                    @DrawableRes val iconId: Int
                    iconId = if (percent > 100 / 3 * 2) {
                        R.drawable.video_volume_high_ic
                    } else if (percent > 100 / 3) {
                        R.drawable.video_volume_medium_ic
                    } else if (percent != 0) {
                        R.drawable.video_volume_low_ic
                    } else {
                        R.drawable.video_volume_mute_ic
                    }
                    lav_player_view_lottie.setImageResource(iconId)
                    tv_player_view_message.setText(String.format("%s %%", percent))
                    post(mShowMessageRunnable)

                }
            }
            MotionEvent.ACTION_UP -> {
                if (Math.abs(mViewDownX - event.x) <= ViewConfiguration.get(context).scaledTouchSlop &&
                    Math.abs(mViewDownY - event.y) <= ViewConfiguration.get(context).scaledTouchSlop
                ) {
                    // 如果整个视频播放区域太大，触摸移动会导致触发点击事件，所以这里换成手动派发点击事件
                    if (isEnabled && isClickable) {
                        performClick()
                    }
                }
                mTouchOrientation = -1
                mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                if (mAdjustSecond != 0) {
                    // 调整播放进度
                    setProgress(getCurrentPosition().toInt() + mAdjustSecond * 1000)
                    mAdjustSecond = 0
                }
                postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
                postDelayed(mHideMessageRunnable,DIALOG_TIME.toLong())
            }
            MotionEvent.ACTION_CANCEL -> {
                mTouchOrientation = -1
                mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                if (mAdjustSecond != 0) {
                    setProgress(getCurrentPosition().toInt() + mAdjustSecond * 1000)
                    mAdjustSecond = 0
                }
                postDelayed(mHideControllerRunnable,CONTROLLER_TIME.toLong())
                postDelayed(mHideMessageRunnable, DIALOG_TIME.toLong())
            }
            else -> {
            }
        }
        return true
    }



    /**
     * 创建一个新的player
     */
//    private fun createPlayer() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer?.stop()
//            mMediaPlayer?.setDisplay(null)
//            mMediaPlayer?.release()
//        }
//        val ijkMediaPlayer = IjkMediaPlayer()
//        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//
//
//        mMediaPlayer = ijkMediaPlayer
//
//        setListener(object :VideoPlayerListener(){
//            override fun onBufferingUpdate(p0: IMediaPlayer, precent: Int) {
//                sb_player_view_progress.secondaryProgress = ((precent / 100f * p0.duration).toInt())
//                var progress: Int = p0.currentPosition.toInt()
//                // 这里优化了播放的秒数计算，将 800 毫秒估算成 1 秒
//                // 这里优化了播放的秒数计算，将 800 毫秒估算成 1 秒
//                if (progress + 1000 < mMediaPlayer?.getDuration()!!) {
//                    // 进行四舍五入计算
//                    progress = Math.round(progress / 1000f) * 1000
//                }
//                sb_player_view_progress.progress=progress
//
//                tv_player_view_play_time.text = PlayerView.conversionTime(progress)
//            }
//
//            override fun onCompletion(p0: IMediaPlayer?) {
//
//
//            }
//
//            override fun onPrepared(p0: IMediaPlayer) {
//                tv_player_view_total_time.text = conversionTime(p0?.duration)
//                sb_player_view_progress.max = p0.duration.toInt()
//                start()
//
//            }
//
//            override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
//
//                return false
//            }
//
//            override fun onVideoSizeChanged(p0: IMediaPlayer?, width:Int, height:Int, sar_num:Int, sar_den:Int) {
//
//                // 重新设置 VideoView 的宽高
//                val params: ViewGroup.LayoutParams = fragment_player_layout.getLayoutParams()
//                params.width = width
//                params.height = height
//
//                fragment_player_layout.layoutParams = params
//            }
//
//            override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
//                return false
//            }
//
//            override fun onSeekComplete(p0: IMediaPlayer?) {
//
//            }
//
//
//        })
//        if (listener != null) {
//            mMediaPlayer?.setOnPreparedListener(listener)
//            mMediaPlayer?.setOnInfoListener(listener)
//            mMediaPlayer?.setOnSeekCompleteListener(listener)
//            mMediaPlayer?.setOnBufferingUpdateListener(listener)
//            mMediaPlayer?.setOnErrorListener(listener)
//        }
//    }


    fun setListener(listener: VideoPlayerListener) {
        this.listener = listener
        if (mMediaPlayer != null) {
            mMediaPlayer?.setOnPreparedListener(listener)
        }
    }


    fun load() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer?.setDisplay(null)
            mMediaPlayer!!.release()
            mMediaPlayer=null;
        }
        mMediaPlayer = createPlayer()
        setLister()
        mMediaPlayer?.setDisplay(ijk_player_view_video.holder)
        try {
        mMediaPlayer?.setDataSource(mContext, Uri.parse(mPath))
        }catch (e:Exception){

        }
        mMediaPlayer!!.prepareAsync()
    }

    fun start() {
        if (mMediaPlayer==null){
            load()
        }else {

            if (mMediaPlayer != null) {
                mMediaPlayer!!.start()
//            mAudioFocusHelper.requestFocus()
                iv_player_view_control.setImageResource(R.drawable.video_play_pause_ic)
                // 延迟隐藏控制面板
                // 延迟隐藏控制面板
                removeCallbacks(mHideControllerRunnable)
                postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
            }
        }
    }

    fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
//            mAudioFocusHelper.abandonFocus()
        }
    }

    fun pause() {


        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
//            mAudioFocusHelper.abandonFocus()
            iv_player_view_control.setImageResource(R.drawable.video_play_start_ic)
            // 延迟隐藏控制面板
            // 延迟隐藏控制面板
            removeCallbacks(mHideControllerRunnable)
            postDelayed(mHideControllerRunnable, CONTROLLER_TIME.toLong())
        }
    }

    fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
//          mAudioFocusHelper.ab()
        }
    }


    fun reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
//           mAudioFocusHelper.abandonFocus()
        }
    }


    fun getDuration(): Long {
        return if (mMediaPlayer != null) {
            mMediaPlayer!!.duration
        } else {
            0
        }
    }


    fun getCurrentPosition(): Long {
        return if (mMediaPlayer != null) {
            mMediaPlayer!!.currentPosition
        } else {
            0
        }
    }


    fun seekTo(l: Long) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(l)
        }
    }

    fun isPlaying(): Boolean {
        return if (mMediaPlayer != null) {
            mMediaPlayer!!.isPlaying
        } else false
    }


    /**
     * 设置播放器生命管控（自动回调生命周期方法）
     */
    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(p0: LifecycleOwner, p1: Lifecycle.Event) {

        Log.e("onStateChanged","-----")
            when (p1) {
                Lifecycle.Event.ON_DESTROY -> release()
                else -> {
                }
            }

    }


//    private fun createSurfaceView() {
//        ijk_player_view_video.holder.addCallback(LmnSurfaceCallback())
//        val layoutParams = LayoutParams(
//            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
//        )
//        ijk_player_view_video.layoutParams = layoutParams
//
//    }

}