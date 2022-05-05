package com.legent.ui.ext.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

/**
 * Created by sylar on 15/7/15.
 */
public class TagCloudView extends RelativeLayout {

    static final long FPS_PERIOD = 1000 / 60;

    int width, height;
    boolean isInAuto, isInInertial;

    PointF center, last;
    DBPoint normalDirection;
    double velocity;

    List<View> tags = Lists.newArrayList();
    List<DBPoint> coordinate = Lists.newArrayList();
    Random random = new Random();


    //---------------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------------

    public TagCloudView(Context context) {
        super(context);
        init(context, null);
    }

    public TagCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TagCloudView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        setGestureListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isInAuto = true;
        isInInertial = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isInAuto = false;
        isInInertial = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = measure(widthMeasureSpec);
        height = measure(heightMeasureSpec);
        center = new PointF(width / 2f, height / 2f);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    void setGestureListener(View view) {
        final GestureDetector gd = new GestureDetector(getContext(), new GestureListener());
        view.setClickable(true);
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }


    //---------------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------------

    public void setCloudTags(List<TextView> array) {
        this.removeAllViews();
        tags.clear();
        coordinate.clear();
        tags.addAll(array);
        for (View view : tags) {
            this.addView(view);
            setCenter(view, center);
        }

        double p1 = Math.PI * (3 - Math.sqrt(5));
        double p2 = 2.0 / tags.size();

        for (int i = 0; i < tags.size(); i++) {

            double y = i * p2 - 1 + (p2 / 2);
            double r = Math.sqrt(1 - y * y);
            double p3 = i * p1;
            double x = Math.cos(p3) * r;
            double z = Math.sin(p3) * r;

            DBPoint point = DBPointMake((float) x, (float) y, (float) z);
            coordinate.add(point);
//            setTagOfPoint(point, i);
        }

        startAnim();

        int a = random.nextInt(10) - 5;
        int b = random.nextInt(10) - 5;
        normalDirection = DBPointMake(a, b, 0);

        start();
    }

    void startAnim() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
//        anim.setTarget(this);
        anim.setDuration(1500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float per = (Float) animation.getAnimatedValue();

                DBPoint point, p;
                for (int i = 0; i < tags.size(); i++) {
                    point = coordinate.get(i);
                    p = DBPointMake(point.x * per, point.y * per, point.z * per);

                    setTagOfPoint(p, i);
                }
            }
        });

        anim.start();
    }

    void updateFrameOfPoint(int index, DBPoint direction, float angle) {

        DBPoint point = coordinate.get(index);
        point = DBPointMakeRotation(point, direction, angle);
        coordinate.set(index, point);

        setTagOfPoint(point, index);

    }

    void setTagOfPoint(DBPoint point, int index) {
        View view = tags.get(index);

        float x = (point.x + 1) * width / 2f;
        float y = (point.y + 1) * width / 2f;

        PointF p = new PointF(x, y);
        setCenter(view, p);

        float transform = (point.z + 2) / 3;
        view.setClickable(point.z >= 0);
        view.setAlpha(transform);
        view.setScaleX(transform);
        view.setScaleY(transform);
    }

    void start() {
        removeCallbacks(timerFunc);
        postDelayed(timerFunc, FPS_PERIOD);
    }

    Runnable timerFunc = new Runnable() {
        @Override
        public void run() {
            if (isShown()) {
                if (isInInertial) {
                    inertiaStep();
                } else if (isInAuto) {
                    autoTurnRotation();
                }
            }

            start();
        }
    };


    void timerStart() {
        isInInertial = false;
        isInAuto = true;
    }

    void timerStop() {
        isInAuto = false;
    }

    void autoTurnRotation() {
        for (int i = 0; i < tags.size(); i++) {
            updateFrameOfPoint(i, normalDirection, 0.002f);
        }
    }

    void inertiaStart() {
        isInAuto = false;
        isInInertial = true;
    }

    void inertiaStop() {
        isInInertial = false;
        isInAuto = true;
    }

    void inertiaStep() {
        if (velocity <= 0) {
            inertiaStop();
        } else {
            velocity -= 80.0;
            double angle = velocity / width * 2.0 * (FPS_PERIOD / 1000.0);
            for (int i = 0; i < tags.size(); i++) {
                updateFrameOfPoint(i, normalDirection, (float) angle);
            }
        }

    }


    //---------------------------------------------------------------------------------------
    //GestureListener
    //---------------------------------------------------------------------------------------

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        //刚刚手指接触到触摸屏的那一刹那，就是触的那一下。
        @Override
        public boolean onDown(MotionEvent e) {
            last = new PointF(e.getX(), e.getY());
            timerStop();
            inertiaStop();

            return super.onDown(e);
        }

        //手指离开触摸屏的那一刹那
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            timerStart();
            return super.onSingleTapUp(e);
        }

        //手指在触摸屏上迅速移动，并松开的动作。
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            inertiaStart();

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        //手指在触摸屏上滑动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            PointF current = new PointF(e2.getX(), e2.getY());
            DBPoint direction = DBPointMake(last.y - current.y, current.x - last.x, 0);
            double distance = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
            double angle = distance / (width / 2.0);
            for (int i = 0; i < tags.size(); i++) {
                updateFrameOfPoint(i, direction, (float) angle);
            }
            normalDirection = direction;
            last = current;


            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    //---------------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------------

    static class DBPoint {
        float x, y, z;

        public DBPoint(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

    static class DBMatrix {
        int row, column;
        float[][] matrix = new float[4][4];

        public DBMatrix(int column, int row) {
            this.row = row;
            this.column = column;
            this.matrix = new float[column][row];
        }

        public DBMatrix(int column, int row, float[][] matrix) {
            this(column, row);
            this.matrix = matrix;
        }


    }

    //---------------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------------

    static PointF getCenter(View view) {
        PointF p = new PointF(view.getLeft() + view.getWidth() / 2f, view.getTop() + view.getHeight() / 2f);
        return p;
    }

    static void setCenter(View view, PointF p) {
        if (p == null) return;
        view.setX(p.x - view.getWidth() / 2f);
        view.setY(p.y - view.getHeight() / 2f);
    }


    static DBPoint DBPointMake(float x, float y, float z) {
        return new DBPoint(x, y, z);
    }

    static DBMatrix DBMatrixMake(int column, int row) {
        return new DBMatrix(column, row);
    }

    static DBMatrix DBMatrixMakeFromArray(int column, int row, float[][] data) {
        return new DBMatrix(column, row, data);
    }

    static DBMatrix DBMatrixMutiply(DBMatrix a, DBMatrix b) {
        DBMatrix result = DBMatrixMake(a.column, b.row);
        for (int i = 0; i < a.column; i++) {
            for (int j = 0; j < b.row; j++) {
                for (int k = 0; k < a.row; k++) {
                    result.matrix[i][j] += a.matrix[i][k] * b.matrix[k][j];
                }
            }
        }
        return result;
    }


    static DBPoint DBPointMakeRotation(DBPoint point, DBPoint direction, float angle) {
        if (angle == 0) {
            return point;
        }

        float[][] temp2 = {{point.x, point.y, point.z, 1}};

        DBMatrix result = DBMatrixMakeFromArray(1, 4, temp2);

        if (direction.z * direction.z + direction.y * direction.y != 0) {
            float cos1 = direction.z / (float) Math.sqrt(direction.z * direction.z + direction.y * direction.y);
            float sin1 = direction.y / (float) Math.sqrt(direction.z * direction.z + direction.y * direction.y);
            float[][] t1 = {{1, 0, 0, 0}, {0, cos1, sin1, 0}, {0, -sin1, cos1, 0}, {0, 0, 0, 1}};

            DBMatrix m1 = DBMatrixMakeFromArray(4, 4, t1);
            result = DBMatrixMutiply(result, m1);
        }

        if (direction.x * direction.x + direction.y * direction.y + direction.z * direction.z != 0) {
            float cos2 = (float) Math.sqrt(direction.y * direction.y + direction.z * direction.z) / (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
            float sin2 = -direction.x / (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
            float[][] t2 = {{cos2, 0, -sin2, 0}, {0, 1, 0, 0}, {sin2, 0, cos2, 0}, {0, 0, 0, 1}};

            DBMatrix m2 = DBMatrixMakeFromArray(4, 4, t2);
            result = DBMatrixMutiply(result, m2);
        }

        float cos3 = (float) Math.cos(angle);
        float sin3 = (float) Math.sin(angle);
        float[][] t3 = {{cos3, sin3, 0, 0}, {-sin3, cos3, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        DBMatrix m3 = DBMatrixMakeFromArray(4, 4, t3);
        result = DBMatrixMutiply(result, m3);

        if (direction.x * direction.x + direction.y * direction.y + direction.z * direction.z != 0) {
            float cos2 = (float) Math.sqrt(direction.y * direction.y + direction.z * direction.z) / (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
            float sin2 = -direction.x / (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
            float[][] t2_ = {{cos2, 0, sin2, 0}, {0, 1, 0, 0}, {-sin2, 0, cos2, 0}, {0, 0, 0, 1}};

            DBMatrix m2_ = DBMatrixMakeFromArray(4, 4, t2_);
            result = DBMatrixMutiply(result, m2_);
        }

        if (direction.z * direction.z + direction.y * direction.y != 0) {
            float cos1 = direction.z / (float) Math.sqrt(direction.z * direction.z + direction.y * direction.y);
            float sin1 = direction.y / (float) Math.sqrt(direction.z * direction.z + direction.y * direction.y);
            float[][] t1_ = {{1, 0, 0, 0}, {0, cos1, -sin1, 0}, {0, sin1, cos1, 0}, {0, 0, 0, 1}};

            DBMatrix m1_ = DBMatrixMakeFromArray(4, 4, t1_);
            result = DBMatrixMutiply(result, m1_);
        }

        DBPoint resultPoint = DBPointMake(result.matrix[0][0], result.matrix[0][1], result.matrix[0][2]);

        return resultPoint;
    }
}
