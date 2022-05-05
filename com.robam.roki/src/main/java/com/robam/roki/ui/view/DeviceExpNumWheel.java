package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.utils.LogUtils;
import com.robam.roki.R;

import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceExpNumWheel extends View {

    /**
     * 选择监听
     */
    public interface OnSelectListener {
        /**
         * 结束选择
         */
        void endSelect(int index, Object item);

        /**
         * 选中的内容
         */
        void selecting(int index, Object item);

    }

    /**
     * 当前选中项的id
     */
    protected int selectedId;
    /**
     * 控件宽度
     */
    protected float controlWidth;
    /**
     * 控件高度
     */
    protected int controlHeight;
    /**
     * 是否滑动中
     */
    protected boolean isScrolling = false;
    /**
     * 选择的内容
     */
    protected List<ItemObject> itemList = Lists.newArrayList();
    /**
     * 设置数据
     */
    protected List<?> dataList = Lists.newArrayList();
    /**
     * 按下的坐标
     */
    protected int downY;
    /**
     * 按下的时间
     */
    protected long downTime = 0;
    /**
     * 短促移动
     */
    protected long goonTime = 200;
    /**
     * 短促移动距离
     */
    protected int goonDistence = 100;
    /**
     * 画线画笔
     */
    protected Paint linePaint;
    /**
     * 线的默认颜色
     */
    protected int lineColor = 0xff000000;
    /**
     * 线的默认宽度
     */
    protected float lineWidth = 2f;
    /**
     * 默认字体
     */
    protected float normalFont = 14.0f;
    /**
     * 选中的时候字体
     */
    protected float selectedFont = 60.0f;
    /**
     * 单元格高度
     */
    protected int unitHeight = 50;
    /**
     * 高度递减
     */
    protected int heightDiv = 10;
    /**
     * 显示多少个内容
     */
    protected int itemNumber = 7;
    /**
     * 默认字体颜色
     */
    protected int normalColor = 0xff000000;
    /**
     * 选中时候的字体颜色
     */
    protected int selectedColor = 0xffff0000;
    /**
     * 蒙板高度
     */
    protected float maskHight = 48.0f;
    /**
     * 选择监听
     */
    protected OnSelectListener onSelectListener;
    /**
     * 是否可用
     */
    protected boolean isEnable = true;
    /**
     * 刷新界面
     */
    protected static final int REFRESH_VIEW = 0x001;
    /**
     * 移动距离
     */
    protected static int MOVE_NUMBER = 5;
    /**
     * 是否允许选空
     */
    protected boolean noEmpty = true;
    /**
     * 中间行上方Y值
     */
    protected int center_above_y;
    /**
     * 中间行下方Y值
     */
    protected int center_below_y;

    public float startx_offset;

    public void setColor0(int color0) {
        this.color0 = color0;
    }

    /**
     * 字体颜色
     */
    protected int color0 = 0xffffffff;
    protected int color1 = 0xff929292;
    protected int color2 = 0xff4f4f4f;
    protected int color3 = 0xff404040;
    protected int color4 = 0xff3e3e3e;

    /**
     * 正在修改数据，避免ConcurrentModificationException异常
     */
    private boolean isClearing = false;
    private String unit = "";

    public DeviceExpNumWheel(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        initData();
    }

    public DeviceExpNumWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initData();
    }

    public DeviceExpNumWheel(Context context) {
        super(context);
        initData();
    }

    /**
     * 初始化，获取设置的属性
     *
     * @param context
     * @param attrs
     */
    protected void init(Context context, AttributeSet attrs) {

        if (isInEditMode()) return;
        //selectedFont根据当前APP类型设置
        if (IAppType.RKPAD.equals(Plat.appType))
            selectedFont = 60.0f;
        TypedArray attribute = context.obtainStyledAttributes(attrs,
                com.legent.ui.R.styleable.WheelView);
        unitHeight = (int) attribute.getDimension(
                com.legent.ui.R.styleable.WheelView_unitHight, 32);
        heightDiv = (int) attribute.getDimension(
                com.legent.ui.R.styleable.WheelView_unitHight, 10);
        normalFont = attribute.getDimension(
                com.legent.ui.R.styleable.WheelView_normalTextSize, 14.0f);
        selectedFont = attribute.getDimension(
                com.legent.ui.R.styleable.WheelView_selectedTextSize, 22.0f);
        itemNumber = attribute.getInt(com.legent.ui.R.styleable.WheelView_itemNumber, 7);
        normalColor = attribute.getColor(
                com.legent.ui.R.styleable.WheelView_normalTextColor, 0xff000000);
        selectedColor = attribute.getColor(
                com.legent.ui.R.styleable.WheelView_selectedTextColor, 0xffff0000);
        lineColor = attribute.getColor(com.legent.ui.R.styleable.WheelView_lineColor,
                0xff000000);
        lineWidth = attribute.getDimension(com.legent.ui.R.styleable.WheelView_lineHeight, 2f);
        maskHight = attribute.getDimension(com.legent.ui.R.styleable.WheelView_maskHight,
                48.0f);
        noEmpty = attribute.getBoolean(com.legent.ui.R.styleable.WheelView_noEmpty, true);
        isEnable = attribute
                .getBoolean(com.legent.ui.R.styleable.WheelView_isEnable, true);
        attribute.recycle();

        controlHeight = itemNumber * unitHeight;
        center_above_y = controlHeight / 2 - unitHeight / 2;
        center_below_y = controlHeight / 2 + unitHeight / 2;
    }

    protected ItemObject getItemObject() {
        return new ItemObject();
    }

    /**
     * 初始化数据
     */
    protected synchronized void initData() {
        isClearing = true;
        itemList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            ItemObject itmItemObject = getItemObject();
            itmItemObject.id = i;
            itmItemObject.tag = dataList.get(i);
            itmItemObject.itemText = String.valueOf(dataList.get(i));
            itmItemObject.x = 0;
            itmItemObject.y = i * unitHeight;
            itemList.add(itmItemObject);
        }
        isClearing = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable)
            return true;
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                downTime = currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                isScrolling = true;
                if (isGoonMove) break;
                actionMove(y - downY);
                onSelectListener();
                break;
            case MotionEvent.ACTION_UP:
                isScrolling = false;
                int move = Math.abs(y - downY);
                // 判断段时间移动的距离
                long time_ = System.currentTimeMillis();
                float speed = (float) move / (float) (time_ - downTime);
                if (speed > 0.9 && speed < 4.0) {
                    MOVE_NUMBER = (int) (speed * 2.7);
                    goonMove(y - downY);
                } else if (speed >= 4.0 && speed <= 9.0) {
                    MOVE_NUMBER = (int) (speed * 6.7);
                    goonMove(y - downY);
                } else if (speed > 9.0) {
                    MOVE_NUMBER = 45;
                    goonMove(y - downY);
                } else {
                    actionUp(y - downY);
                }
                noEmpty();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawList(canvas);
//         drawMask(canvas);
    }

    protected synchronized void drawList(Canvas canvas) {
        if (isClearing)
            return;
        try {
            for (ItemObject itemObject : itemList) {
                itemObject.drawSelf(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        controlWidth = getWidth();
        LogUtils.i("20180417", " controlWidth:" + controlWidth);
       /* if (controlWidth == 0) {
            controlWidth = 314;
        }*/
        if (controlWidth != 0) {
//            setMeasuredDimension(getWidth(), itemNumber * unitHeight - (itemNumber / 2) * (itemNumber / 2 + 1) * heightDiv);
            setMeasuredDimension((int) controlWidth, controlHeight);
        }

    }

    /**
     * 继续移动一定距离
     */
    protected boolean isGoonMove;
    protected Object object = new Object();

    protected synchronized void goonMove(final int move) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (object) {
                    isGoonMove = true;
                    int distance = 0;
                    int maxDistance = unitHeight * MOVE_NUMBER;
                    int speed = 0;
                    int seconds = 3;
                    boolean notRunAllOver = false;
                    while (distance < maxDistance) {
                        if (isScrolling) {
                            isGoonMove = false;
                            for (ItemObject item : itemList) {
                                item.newY(item.move + 0);
                            }
                            return;
                        }
                        try {
                            Thread.sleep(seconds);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!actionThreadMove(move > 0 ? distance : distance * (-1))) {
                            notRunAllOver = true;
                            break;
                        }
                        int subdistance = maxDistance - distance;
                        int speedrate = (int) ((float) subdistance / (float) maxDistance) * 100;
                        if (speedrate < 10) {
                            speed = subdistance / 130 + 1;
                            seconds = 1;
                            distance += speed;
                        } else {
                            distance += 25;
                        }

                    }
                    if (!notRunAllOver)
                        actionUp(move > 0 ? distance - speed : distance * (-1) + speed);
                    else if (speed > 0)
                        actionUp(move > 0 ? distance - speed : distance * (-1) + speed);
                    else
                        actionUp(move > 0 ? distance - 25 : distance * (-1) + 25);
                    noEmpty();
                    isGoonMove = false;
                }
            }
        }).start();
    }

    /**
     * 不能为空，必须有选项
     */
    protected void noEmpty() {
        if (true) return;
        try {
            if (!noEmpty)
                return;
            for (ItemObject item : itemList) {
                if (item.isSelected())
                    return;
            }
            int move = (int) itemList.get(0).moveToSelected();
            if (move < 0) {
                defaultMove(move);
            } else {
                defaultMove((int) itemList.get(itemList.size() - 1)
                        .moveToSelected());
            }
            for (ItemObject item : itemList) {
                if (item.isSelected()) {
                    onEndSelect(item);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动的时候
     *
     * @param move
     */
    protected synchronized void actionMove(int move) {
        int first_start = itemList.get(0).y + move;
        int last_start = itemList.get(itemList.size() - 1).y + move;
        if (first_start > center_above_y || last_start < center_above_y) {
            return;
        }
        try {
            for (ItemObject item : itemList) {
                item.move(move);
            }
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 移动，线程中调用
     *
     * @param move
     */
    protected boolean actionThreadMove(int move) {
        int first_start = itemList.get(0).y + move;
        int last_start = itemList.get(itemList.size() - 1).y + move;
        if (first_start > center_above_y || last_start < center_above_y) {
            return false;
        }
        try {
            for (ItemObject item : itemList) {
                item.move(move);
            }
            Message rMessage = new Message();
            rMessage.what = REFRESH_VIEW;
            handler.sendMessage(rMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 松开的时候
     *
     * @param move 此处的move
     */
    protected synchronized void actionUp(int move) {
        try {
            int fromUpLine = 0;
            if (move > 0) {
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).isSelected()) {
                        selectedId = i;
                        fromUpLine = (int) itemList.get(i).moveToSelected();
                        onEndSelect(itemList.get(i));
                        break;
                    }
                }
            } else {
                for (int i = itemList.size() - 1; i >= 0; i--) {
                    if (itemList.get(i).isSelected()) {
                        selectedId = i;
                        fromUpLine = (int) itemList.get(i).moveToSelected();
                        onEndSelect(itemList.get(i));
                        break;
                    }
                }
            }
            int first_start = itemList.get(0).y + move;
            int last_start = itemList.get(itemList.size() - 1).y + move;
            if (first_start > center_above_y || last_start < center_above_y) {
                for (ItemObject item : itemList) {
                    item.newY(item.move + 0);
                }
                slowMove(fromUpLine);
                return;
            }
            for (ItemObject item : itemList) {
                item.newY(move + 0);
            }
            slowMove(fromUpLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓慢移动
     *
     * @param move_offset 偏差距离
     */
    protected synchronized void slowMove(final int move_offset) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (object) {
                    try {
                        // 判断政府
                        int absdistance = move_offset > 0 ? move_offset : move_offset * (-1);
                        int dircetion = move_offset > 0 ? 1 : (-1);
                        // 移动速度
                        int speed = 1;
                        while (true) {
                            absdistance = absdistance - speed;
                            if (absdistance <= 0) {
                                break;
                            }
                            for (ItemObject item : itemList) {
                                item.newY(speed * dircetion);
                            }
                            Message rMessage = new Message();
                            rMessage.what = REFRESH_VIEW;
                            handler.sendMessage(rMessage);
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        for (ItemObject item : itemList) {
                            if (item.isSelected()) {
                                onEndSelect(item);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 移动到默认位置
     *
     * @param move
     */
    synchronized protected void defaultMove(int move) {
        try {
            if (itemList == null || itemList.size() == 0) return;

            for (ItemObject item : itemList) {
                item.newY(move);
            }

            Message rMessage = new Message();
            rMessage.what = REFRESH_VIEW;
            handler.sendMessage(rMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 滑动监听
     */
    protected void onSelectListener() {
        try {
            if (onSelectListener == null)
                return;
            for (ItemObject item : itemList) {
                if (item.isSelected()) {
                    onSelecting(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘制选中阴影
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(getContext().getResources().getColor(R.color.c11));
        if (Build.VERSION.SDK_INT >= 26){
            canvas.drawLine(unitHeight*3, controlHeight / 2 - unitHeight / 2, controlWidth-unitHeight/4, controlHeight / 2 - unitHeight / 2, paint);
            canvas.drawLine(unitHeight*3, controlHeight / 2 + unitHeight / 2, controlWidth-unitHeight/4, controlHeight / 2 + unitHeight / 2, paint);
        }else {
            canvas.drawLine(unitHeight/2, controlHeight / 2 - unitHeight / 2, controlWidth-unitHeight/4, controlHeight / 2 - unitHeight / 2, paint);
            canvas.drawLine(unitHeight/2, controlHeight / 2 + unitHeight / 2, controlWidth-unitHeight/4, controlHeight / 2 + unitHeight / 2, paint);
        }
//        canvas.drawRect(unitHeight/2, controlHeight / 2 - unitHeight / 2 + 2, controlWidth, controlHeight / 2 + unitHeight / 2, paint);
       // canvas.drawLine(0, controlHeight / 2 - unitHeight / 2, controlWidth - unitHeight, controlHeight / 2 - unitHeight / 2, paint);
       // canvas.drawLine(0, controlHeight / 2 + unitHeight / 2, controlWidth - unitHeight, controlHeight / 2 + unitHeight / 2, paint);
    }

    /**
     * 绘制遮盖板
     *
     * @param canvas
     */
    protected void drawMask(Canvas canvas) {
        LinearGradient lg = new LinearGradient(0, 0, 0, maskHight, 0x00f2f2f2,
                0x00f2f2f2, Shader.TileMode.MIRROR);
        Paint paint = new Paint();
        paint.setShader(lg);
        paint.setColor(lineColor);
        canvas.drawRect(0, 0, controlWidth, maskHight, paint);

        LinearGradient lg2 = new LinearGradient(0, controlHeight - maskHight,
                0, controlHeight, 0x00f2f2f2, 0x00f2f2f2, Shader.TileMode.MIRROR);
        Paint paint2 = new Paint();
        paint2.setShader(lg2);
        canvas.drawRect(0, controlHeight - maskHight, controlWidth,
                controlHeight, paint2);
    }


    /**
     * 设置数据 （第一次）
     *
     * @param data
     */
    public void setData(List<?> data) {
        this.dataList = data;
        initData();
    }

    public List<?> getData() {
        return dataList;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 重置数据
     *
     * @param data
     */
    public void resetData(List<String> data) {
        setData(data);
        invalidate();
    }

    /**
     * 获取返回项 id
     *
     * @return
     */
    public int getSelected() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.id;
        }
        return -1;
    }

    /**
     * @return
     */
    public Object getSelectedTag() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.tag;
        }
        return 100;
    }

    /**
     * 获取返回的内容
     *
     * @return
     */
    public String getSelectedText() throws Exception {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.itemText;
        }
        throw new NullPointerException();
    }


    /**
     * 是否正在滑动
     *
     * @return
     */
    public boolean isScrolling() {
        return isScrolling;
    }

    /**
     * 是否可用
     *
     * @return
     */
    public boolean isEnable() {
        return isEnable;
    }

    /**
     * 设置是否可用
     *
     * @param isEnable
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * 设置默认选项
     *
     * @param index
     */
    public void setDefault(int index) {
        if (index < 0 || index > itemList.size() - 1)
            return;
        float move = itemList.get(index).moveToSelected();
        defaultMove((int) move);

        ItemObject item = itemList.get(index);
        onEndSelect(item);
    }

    /**
     * 获取列表大小
     *
     * @return
     */
    public int getListSize() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }

    /**
     * 获取某项的内容
     *
     * @param index
     * @return
     */
    public String getItemText(int index) {
        if (itemList == null)
            return "";
        return itemList.get(index).itemText;
    }

    /**
     * 监听
     *
     * @param onSelectListener
     */
    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    invalidate();
                    break;
                default:
                    break;
            }
        }

    };

    protected String getDrawText(String itemText) {
        return itemText;
    }

    /**
     * 单条内容
     *
     * @author JiangPing
     */
    protected class ItemObject {
        /**
         * id
         */
        public int id = 0;
        /**
         * 内容
         */
        public String itemText = "";

        public Object tag;

        /**
         * x坐标
         */
        public int x = 0;
        /**
         * y坐标
         */
        public int y = 0;
        /**
         * 移动距离
         */
        protected int move = 0;
        /**
         * 字体画笔
         */
        protected Paint textPaint;
        /**
         * 字体范围矩形
         */
        protected Rect textRect;

        public ItemObject() {
            super();
        }

        /**
         * 绘制自身
         *
         * @param canvas
         */
        protected void drawSelf(Canvas canvas) {
            if (textPaint == null) {
                textPaint = new Paint();
                textPaint.setAntiAlias(true);
            }

            if (textRect == null)
                textRect = new Rect();

            // 判断是否被选择
            if (isSelected()) {
                textPaint.setColor(selectedColor);
                // 获取距离标准位置的距离
                float moveToSelect = moveToSelected();
                moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
                        * (-1);
                // 计算当前字体大小
                float textSize = normalFont
                        + ((selectedFont - normalFont) * (1.0f - moveToSelect
                        / (float) unitHeight));
                textPaint.setTextSize(textSize);
            } else {
                textPaint.setColor(normalColor);
                textPaint.setTextSize(normalFont);
            }
            // 返回包围整个字符串的最小的一个Rect区域
            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
            // 判断是否可视
            if (!isInView())
                return;


            if (Build.VERSION.SDK_INT >= 26){
                if (startx_offset != 0) {
                    canvas.drawText(itemText + " " + unit, x + startx_offset + 30, y + move + unitHeight / 2 + textRect.height() / 2,
                            textPaint);
                }else {
                    canvas.drawText(itemText + " " + unit, x + controlWidth + 30, y + move + unitHeight / 2 + textRect.height() / 2,
                            textPaint);
                }
            }else {
                if (startx_offset != 0) {
                    canvas.drawText(itemText + " " + unit, x + startx_offset - textRect.width()/2, y + move + unitHeight / 2 + textRect.height() / 2,
                            textPaint);
                }else {
                    canvas.drawText(itemText + " " + unit, x + controlWidth / 2 - textRect.width() / 2-20, y + move + unitHeight / 2 + textRect.height() / 2,
                            textPaint);
                }
            }



           /* if (startx_offset != 0) {
                canvas.drawText(itemText + " " + unit, x + startx_offset - textRect.width() / 2 - 100, y + move + unitHeight / 2 + textRect.height() / 2,
                        textPaint);

            } else {
                canvas.drawText(itemText + " " + unit, x + controlWidth / 2 - textRect.width() / 2 - 100, y + move + unitHeight / 2 + textRect.height() / 2,
                        textPaint);
            }*/


        }

        /**
         * 是否在可视界面内
         *
         * @return
         */
        protected boolean isInView() {
            return y + move <= controlHeight
                    && (y + move + unitHeight / 2 + textRect.height() / 2) >= 0;
        }

        /**
         * 移动距离
         *
         * @param _move
         */
        protected void move(int _move) {
            this.move = _move;
        }

        /**
         * 设置新的坐标
         *
         * @param _move
         */
        protected void newY(int _move) {
            this.move = 0;
            this.y = y + _move;
        }

        /**
         * 判断是否在选择区域内
         *
         * @return
         */
        public boolean isSelected() {
            if ((y + move) >= center_above_y + 2
                    && (y + move) <= center_below_y - 2)
                return true;
            if ((y + move + unitHeight) >= center_above_y
                    + 2
                    && (y + move + unitHeight) <= center_below_y - 2)
                return true;
            return (y + move) <= center_above_y + 2
                    && (y + move + unitHeight) >= center_below_y - 2;
        }

        /**
         * 获取移动到标准位置需要的距离
         */
        protected float moveToSelected() {
            return (center_above_y) - (y + move);
        }
    }

    protected void onEndSelect(ItemObject item) {
        if (onSelectListener != null) {
            onSelectListener.endSelect(item.id, item.tag);
        }
    }

    protected void onSelecting(ItemObject item) {
        if (onSelectListener != null) {
            onSelectListener.selecting(item.id, item.tag);
        }
    }
}

