package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.robam.roki.R;

public class RecipeStepCountDownView extends RelativeLayout {
	private int currentSec;
	private int totalSec;
	private boolean startCountDown;
	private Paint yellowPaint;
	private Paint blackPaint;
	private Context cx;
	public void setCurrentSec(int currentSec) {
		this.currentSec = currentSec;
	}

	public void setStartCountDown(boolean startCountDown) {
		this.startCountDown = startCountDown;
	}

	public void setTotalSec(int totalSec) {
		this.totalSec = totalSec;
	}

	public RecipeStepCountDownView(Context context) {
		super(context);
		init(context, null);
	}

	public RecipeStepCountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.cx=context;
		yellowPaint=new Paint();
		yellowPaint.setColor(context.getResources().getColor(R.color.c02));
		blackPaint=new Paint();
		blackPaint.setColor(context.getResources().getColor(R.color.black));
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("recipe_downview",startCountDown+";"+totalSec+";"+currentSec);
		if(!startCountDown||totalSec<=0){
			super.onDraw(canvas);
			return;
		}
		int height=getHeight();
		int width=getWidth();
		if(currentSec>totalSec){
			currentSec=totalSec;
		}
		if(currentSec<=0){
			currentSec=0;
		}
		float currentwidth=(float)width-((float)currentSec/(float)totalSec)*width;
		//int currentwidth=width-(currentSec/totalSec)*width;
		//绘制底部黄色背景色
		if(blackPaint==null){
			blackPaint=new Paint();
			blackPaint.setColor(cx.getResources().getColor(R.color.black));
		}
		Rect blackRect=new Rect(0,0,getWidth(),getHeight());
		canvas.drawRect(blackRect,blackPaint);
		//绘制黑色倒计时区域
		if(yellowPaint==null){
			yellowPaint=new Paint();
			yellowPaint.setColor(cx.getResources().getColor(R.color.c02));
		}
		Rect yellowRect=new Rect(0,0,(int)currentwidth,getHeight());
		canvas.drawRect(yellowRect,yellowPaint);
		Log.i("recipe_downview",currentwidth+"");
		super.onDraw(canvas);
	}
}