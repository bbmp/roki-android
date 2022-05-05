package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by chenguoming on 15/7/24.
 */
public class MyScrollView extends ScrollView{
    public static final String TAG = "MyScrollView";
    public float downRawY,lastY,downY;
    private boolean isPulling,refreshAble;
    private float headHeight;
    private onPullToRefreshListener mOnPullToRefreshListener;
    private onRefreshAbleStatusChangedListener onRefreshAbleStatusChangedListener;

    public void setOnRefreshAbleStatusChangedListener(MyScrollView.onRefreshAbleStatusChangedListener onRefreshAbleStatusChangedListener) {
        this.onRefreshAbleStatusChangedListener = onRefreshAbleStatusChangedListener;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyScrollView(Context context) {
        super(context);
    }

    public void setOnPullToRefreshListener(onPullToRefreshListener mOnPullToRefreshListener) {
        this.mOnPullToRefreshListener = mOnPullToRefreshListener;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "DY" + downRawY);
                Log.d(TAG,"getRawY"+event.getRawY());
                Log.d(TAG,"getScrollY"+getScrollY());
                if(event.getRawY() - downRawY>0 && getScrollY() == 0){
                    isPulling = true;
                    Log.d(TAG,"intercept_ACTION_MOVE_RETURN:"+"TRUE");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isPulling()){
                    isPulling = false;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * true is refreshing
     * @return
     */
    public boolean isPulling(){
        return isPulling;
    }

    /**
     * called by {@link #onInterceptHoverEvent(MotionEvent)} or {@link #onTouchEvent(MotionEvent)}
     * @param event
     */
    public void onDown(MotionEvent event){
        if(getScrollY() == 0){
            lastY =  downRawY = event.getRawY();
            downY = getY();
        }
        Log.d(TAG, "ACTION_DOWN:" + event.getRawY());
    }

    /**
     * if ScrollView is pulling will call it
     * @param event
     */
    public void onPullMove(MotionEvent event){
        getChildAt(0).setPadding(0,-getHeadHeight ()+(int) ((event.getRawY() - downRawY) * 0.5), 0, 0);
        Log.d(TAG, "ACTION_MOVE:" + event.getRawY());
        lastY = event.getRawY();
        if((event.getRawY() - downRawY) * 0.5 > getHeadHeight()){
            if(!refreshAble){
                if(onRefreshAbleStatusChangedListener!=null){
                    onRefreshAbleStatusChangedListener.onRefreshAble(true);
                }
            }
                refreshAble = true;
        }else{
            if(refreshAble){
                if(onRefreshAbleStatusChangedListener!=null){
                    onRefreshAbleStatusChangedListener.onRefreshAble(false);
                }
            }
                refreshAble=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getChildAt(0).getPaddingTop() == 0&&!isPulling()){
            getChildAt(0).setPadding(0, -getHeadHeight(), 0, 0);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onDown(event);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if(isPulling()){
                        onPullMove(event);
                        return true;
                    }else{
                        if(getScrollY() == 0 && event.getRawY() - downRawY>0){
                            isPulling = true;
                            onPullMove(event);
                            return true;
                        }
                    }
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "ACTION_UP:" + event.getRawY());
                    if (isPulling) {
                        isPulling = false;
                        if(refreshAble){
                            getChildAt(0).setPadding(0,1,0,0);
                            if(mOnPullToRefreshListener!=null){
                                mOnPullToRefreshListener.onPullToRefresh();
                            }
                        }else{
                            getChildAt(0).setPadding(0,-getHeadHeight(),0,0);

                        }
                        return true;
                    }
            }
        return super.onTouchEvent(event);
    }

    /**
     * you can implement this interface to do something
     * when begin refresh
     */
    public interface onPullToRefreshListener{
        void onPullToRefresh();
    }

    /**
     * you can implement this interface to do something
     * when refreshable is changed
     */
    public interface onRefreshAbleStatusChangedListener{
        void onRefreshAble(boolean refreshAble);
    }

    /**
     * get the header height.
     * @return
     */
    private int getHeadHeight(){
        return ((ViewGroup)getChildAt(0)).getChildAt(0).getMeasuredHeight();
    }

    /**
     * when refresh completed invoke.
     */
    public void refreshCompleted(){
        getChildAt(0).setPadding(0,-getHeadHeight(),0,0);
    }
}
