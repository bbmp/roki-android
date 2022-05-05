package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.DataInfo;

import java.util.ArrayList;

public class WaterPurifiyHistogramView extends View {

	private Paint paint;// 柱状图画笔
	private Paint mlpaint;// ml数值画笔
	private Paint tpaint;// 时间文字画笔
	private Paint dpaint;//分割线画笔

	private int[] ml= new int[7];
	private int[] ml1=new int[7];
	private String[] time=new String[]{" "," "," "," "," "," "," "};
	private String[] time1=new String[]{" "," ", " "," "," "," "," "};
	private int max=1500;
	private RectF rect;
	private int people;
	int i=0;
	String type;
	ArrayList<DataInfo> list=new ArrayList<DataInfo>();

	public WaterPurifiyHistogramView(Context context){
		super(context);
		init(context,null);
	}

	public WaterPurifiyHistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}


	public void updateThisData(int[] Dateml,String[] time)
	{
		for (int j = 0; j <ml.length ; j++) {
			ml[j]=0;
		}
			ml1 = Dateml;
		int i=0;
		for (int j = 0; j < time.length; j++) {
			LogUtils.i("20170719","time::"+time[j]);
			if (time[j]!=null){
				ml[i]=ml1[j];
				i++;
			}
		}
		/*for (int j = 0; j < ml1.length; j++) {
			*//*if (ml1[j]!=0){
				ml[i]=ml1[j];
				i++;
			}*//*
			ml[i]=ml1[j];
			i++;
		}*/

		this.postInvalidate();
		//this.postInvalidate();  //可以子线程 更新视图的方法调用。
	}
	public void updateLastData(String[] dateTime)
	{
		LogUtils.i("20170321","datetime:"+dateTime.length);
		for (int j = 0; j < time1.length; j++) {
			time1[j]=" ";
		}
		time = dateTime;
		int i=0;
		for (int j = 0; j < time.length; j++) {
			if (time[j]!=null){
				time1[i]=time[j];
				i++;
			}
		}

		this.postInvalidate();  //可以子线程 更新视图的方法调用。
	}
	public void updateMax(int familyPeople,String timeType)
	{
		LogUtils.i("20170803","people::"+familyPeople);
		type=timeType;
		people=familyPeople;
		if(timeType.equals("day")){
			max=people*1500;
		}else if(timeType.equals("week")){
			max=people*1500*7;
		}else if(timeType.equals("month")){
			max=people*1500*30;
		}
		LogUtils.i("max","max:"+max);

		this.postInvalidate();  //可以子线程 更新视图的方法调用。
	}

	private void init(Context context, AttributeSet attrs) {
		//ml = new int[] { 800, 750, 1800, 300, 450};
		//time=new String[]{"20","21","22","23","24"};

		//画的柱状图的颜色
		paint = new Paint();
		paint.setColor(Color.parseColor("#ff3e18"));
		// paint.setTextAlign(Align.CENTER);
		//设置数值的颜色
		mlpaint = new Paint();
		mlpaint.setTextSize(27);
		mlpaint.setColor(Color.parseColor("#ffffff"));
		//设置时间文字的颜色
		tpaint = new Paint();
		tpaint.setTextSize(32);
		tpaint.setColor(Color.parseColor("#ff3e18"));
		//设置分割线的颜色
		dpaint=new Paint();
		//dpaint.setColor(Color.parseColor("#37ACC8"));

	}

	//@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = getWidth() - 100;
		int height = getHeight();
		LogUtils.i("20170315","height:"+height);
		//横线分割
		for (int i = 0; i < height+20; i=i+15) {
			if(i>height-50)	break;

			if (i +15>height-50) {
				dpaint.setColor(Color.parseColor("#ffffff"));
				RectF r = new RectF(0, i, width + 100, i + 2);
				canvas.drawRect(r, dpaint);
			}else {
				if (i>(height+20)/2 && i<(height+20)/2+15){
					dpaint.setColor(Color.parseColor("#ff3e18"));
					RectF r = new RectF(0, i, width + 100, i + 2);
					canvas.drawRect(r, dpaint);
				}

				if(i>(height+20)/2+15){
					dpaint.setColor(Color.parseColor("#26dadada"));
					RectF r = new RectF(0, i, width + 100, i + 2);
					canvas.drawRect(r, dpaint);
				}
			}

		}

		//竖线分隔
		for (int i = 0; i < width+100; i=i+15) {
			dpaint.setColor(Color.parseColor("#33dadada"));
			RectF r=new RectF(i, 0, i+2, height-50);
			canvas.drawRect(r, dpaint);
		}


		//绘制单位文字
		//canvas.drawText("单位:ml",, t - 10, mlpaint);

		if (ml.length <= 0&&ml.length!=time1.length)
			return;
		//计算平均每个柱状图宽度
		//int average_width = width / (2 * ml.length - 1);
		int average_width = width / (2 * 7 - 1);
		//开始绘制
		for (int i = 0; i <ml.length; i++) {
			float top = 0;
			if (ml[i]==0){
				top=(float)height-60;
			}else if (ml[i] < max) {
				try{
					if (max/ml[i]>=8){
						top=height-70;
					}else{
						top = height  - ((float) ml[i] / max )* (height -90-(height+40)/3)-10;
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				//top= 250-((float) ( ml[i] /max) * 100);
			} else {
				//if(ml[i]/max==100)top=30;
				//top= ((float) max / ml[i] * (height - 50-((height+20)/3))*2);
				if (max!=0){
					LogUtils.i("20170731","total:::"+((float)ml[i]/max));
					if (((float)ml[i]/max)>=2.5){
						top=10;
					}else{
						top=250-(float)(ml[i]/max)*100;
					}
				}
			}
			int left = i * average_width * 2;
			int right = i * average_width * 2 + average_width;
			int bottom = height - 60;
			int t = (int) top;
			//绘制圆角柱状图
			rect = new RectF(10 + left, t, right, bottom);
			canvas.drawRoundRect(rect,20,20,paint);
			//canvas.drawRect(rect, paint);

			//绘制ml数值
			//canvas.drawText(String.valueOf(ml[i]), 50 + left, t - 10, mlpaint);
			//绘制时间文字
			/*if(type.equals("day")){
				canvas.drawText(String.valueOf(time[i]), 10 + left, bottom+35, tpaint);
			}else if(type.equals("week")){
				canvas.drawText(String.valueOf(time[i])+"周", 10 + left, bottom+35, tpaint);
			}else if(type.equals("month")){
				canvas.drawText(String.valueOf(time[i])+"月", 10 + left, bottom+35, tpaint);
			}*/
			LogUtils.i("20170321","time1:"+time1.length);
			canvas.drawText(time1[i], 10 + left, bottom+35, tpaint);
		}
	}

}