package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.common.collect.Lists;
import com.legent.utils.graphic.BitmapUtils;
import com.robam.roki.R;

import java.util.List;

/**
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceOvenModeWheel extends View {

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
    private int selectedId;
    /**
     * 控件宽度
     */
    private float controlWidth;
    /**
     * 控件高度
     */
    private float controlHeight;
    /**
     * 是否滑动中
     */
    private boolean isScrolling = false;
    /**
     * 选择的内容
     */
    private List<ItemObject> itemList = Lists.newArrayList();
    /**
     * 设置数据
     */
    private List<?> dataList = Lists.newArrayList();
    /**
     * 按下的坐标
     */
    private int downY;
    /**
     * 按下的时间
     */
    private long downTime = 0;
    /**
     * 短促移动
     */
    private long goonTime = 200;
    /**
     * 短促移动距离
     */
    private int goonDistence = 100;
    /**
     * 线的默认颜色
     */
    private int lineColor = 0xff000000;
    /**
     * 线的默认宽度
     */
    private float lineWidth = 2f;
    /**
     * 默认字体
     */
    private float normalFont = 14.0f;
    /**
     * 选中的时候字体
     */
    private float selectedFont = 60.0f;
    /**
     * 单元格高度
     */
    private int unitHeight = 50;
    /**
     * 高度递减
     */
    private int heightDiv = 10;
    /**
     * 显示多少个内容
     */
    private int itemNumber = 7;
    /**
     * 默认字体颜色
     */
    private int normalColor = 0xff000000;
    /**
     * 选中时候的字体颜色
     */
    private int selectedColor = 0xffff0000;
    /**
     * 蒙板高度
     */
    private float maskHight = 48.0f;
    /**
     * 选择监听
     */
    private OnSelectListener onSelectListener;
    /**
     * 是否可用
     */
    private boolean isEnable = true;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 移动距离
     */
    private static final int MOVE_NUMBER = 5;
    /**
     * 是否允许选空
     */
    private boolean noEmpty = true;
    /**
     * 字体颜色
     */
    private int color0 = 0xffffffff;
    private int color1 = 0xff929292;
    private int color2 = 0xff4f4f4f;
    private int color3 = 0xff404040;
    private int color4 = 0xff3e3e3e;

    /**
     * 正在修改数据，避免ConcurrentModificationException异常
     */
    private boolean isClearing = false;

    public DeviceOvenModeWheel(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        initData();
    }

    public DeviceOvenModeWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initData();
    }

    public DeviceOvenModeWheel(Context context) {
        super(context);
        initData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnable)
            return true;
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrolling = true;
                downY = (int) event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(y - downY);
                onSelectListener();
                break;
            case MotionEvent.ACTION_UP:
                int move = Math.abs(y - downY);
                // 判断段时间移动的距离
                if (System.currentTimeMillis() - downTime < goonTime
                        && move > goonDistence) {
                    goonMove(y - downY);
                } else {
                    actionUp(y - downY);
                }
                noEmpty();
                isScrolling = false;
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
        drawMask(canvas);
    }

    private synchronized void drawList(Canvas canvas) {
        if (isClearing)
            return;
        try {
            for (ItemObject itemObject : itemList) {
                itemObject.drawSelf(canvas);
            }
        } catch (Exception e) {
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
        if (controlWidth != 0) {
            setMeasuredDimension(getWidth(), itemNumber * unitHeight);
            controlWidth = getWidth();
        }

    }

    /**
     * 继续移动一定距离
     */
    private synchronized void goonMove(final int move) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                int distence = 0;
                while (distence < unitHeight * MOVE_NUMBER) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionThreadMove(move > 0 ? distence : distence * (-1));
                    distence += 10;

                }
                actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
                noEmpty();
            }
        }).start();
    }

    /**
     * 不能为空，必须有选项
     */
    private void noEmpty() {
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
    }

    /**
     * 初始化数据
     */
    private void initData() {
        isClearing = true;
        itemList.clear();
        List<String> list = Lists.newArrayList();
        list.add("快热");
        list.add("风焙烤");
        list.add("焙烤");
        list.add("底加热");
        list.add("解冻");
        list.add("风扇烤");
        list.add("烤烧");
        list.add("强烤烧");
        this.dataList = list;
        for (int i = 0; i < dataList.size(); i++) {
            ItemObject itmItemObject = new ItemObject();
            itmItemObject.id = i;
            itmItemObject.tag = dataList.get(i);
            itmItemObject.itemText = dataList.get(i).toString();
            itmItemObject.x = 0;
            itmItemObject.y = i * unitHeight;
            itemList.add(itmItemObject);
        }
        isClearing = false;
    }

    private int chooseRes(int i, boolean isSelected) {
        int ans = 0;
        switch (i) {
            case 0:
                ans = isSelected ? R.mipmap.img_device_oven_quick_heating : R.mipmap.img_device_oven_quick_heating;
                break;
            case 1:
                ans = isSelected ? R.mipmap.img_device_oven_air_baking : R.mipmap.img_device_oven_air_baking;
                break;
            case 2:
                ans = isSelected ? R.mipmap.img_device_oven_toast : R.mipmap.img_device_oven_toast;
                break;
            case 3:
                ans = isSelected ? R.mipmap.img_device_oven_bottom_heating : R.mipmap.img_device_oven_bottom_heating;
                break;
            case 4:
                ans = isSelected ? R.mipmap.img_device_oven_unfreeze : R.mipmap.img_device_oven_unfreeze;
                break;
            case 5:
                ans = isSelected ? R.mipmap.img_device_oven_air_barbecue : R.mipmap.img_device_oven_air_barbecue;
                break;
            case 6:
                ans = isSelected ? R.mipmap.img_device_oven_strong_barbecue : R.mipmap.img_device_oven_strong_barbecue;
                break;
            case 7:
                ans = isSelected ? R.mipmap.img_device_oven_ferment_selected : R.mipmap.img_device_oven_ferment_selected;
                break;
        }
        return ans;
    }

    /**
     * 移动的时候
     *
     * @param move
     */
    private void actionMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        invalidate();
    }

    /**
     * 移动，线程中调用
     *
     * @param move
     */
    private void actionThreadMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    /**
     * 松开的时候
     *
     * @param move
     */
    private void actionUp(int move) {
        int newMove = 0;
        if (move > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).isSelected()) {
                    selectedId = i;
                    newMove = (int) itemList.get(i).moveToSelected();
                    onEndSelect(itemList.get(i));
                    break;
                }
            }
        } else {
            for (int i = itemList.size() - 1; i >= 0; i--) {
                if (itemList.get(i).isSelected()) {
                    selectedId = i;
                    newMove = (int) itemList.get(i).moveToSelected();
                    onEndSelect(itemList.get(i));
                    break;
                }
            }
        }
        for (ItemObject item : itemList) {
            item.newY(move + 0);
        }
        slowMove(newMove);
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);

    }

    /**
     * 缓慢移动
     *
     * @param move
     */
    private synchronized void slowMove(final int move) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 判断政府
                int m = move > 0 ? move : move * (-1);
                int i = move > 0 ? 1 : (-1);
                // 移动速度
                int speed = 1;
                while (true) {
                    m = m - speed;
                    if (m <= 0) {
                        for (ItemObject item : itemList) {
                            item.newY(m * i);
                        }
                        Message rMessage = new Message();
                        rMessage.what = REFRESH_VIEW;
                        handler.sendMessage(rMessage);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    for (ItemObject item : itemList) {
                        item.newY(speed * i);
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

            }
        }).start();
    }

    /**
     * 移动到默认位置
     *
     * @param move
     */
    synchronized private void defaultMove(int move) {
        if (itemList == null || itemList.size() == 0) return;

        for (ItemObject item : itemList) {
            item.newY(move);
        }

        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    /**
     * 滑动监听
     */
    private void onSelectListener() {
        if (onSelectListener == null)
            return;
        for (ItemObject item : itemList) {
            if (item.isSelected()) {
                onSelecting(item);
            }
        }
    }

    /**
     * 绘制选中阴影
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.c03));
        canvas.drawRect(0, controlHeight / 2 - unitHeight / 2 + 2, controlWidth, controlHeight / 2 + unitHeight / 2, paint);
    }

    /**
     * 绘制遮盖板
     *
     * @param canvas
     */
    private void drawMask(Canvas canvas) {
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
     * 初始化，获取设置的属性
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        if (isInEditMode()) return;

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
        return -1;
    }

    /**
     * 获取返回的内容
     *
     * @return
     */
    public String getSelectedText() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.itemText;
        }
        return "";
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

    /**
     * 单条内容
     *
     * @author JiangPing
     */
    private class ItemObject {
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
        public int move = 0;
        /**
         * 字体画笔
         */
        private Paint textPaint;
        /**
         * 字体范围矩形
         */
        private Rect textRect;

        public ItemObject() {
            super();
        }

        /**
         * 绘制自身
         *
         * @param canvas
         */
        public void drawSelf(Canvas canvas) {

            int res = chooseRes(id, isSelected());
            int width = 30;
            int height = 30;

            if (textPaint == null) {
                textPaint = new Paint();
                textPaint.setAntiAlias(true);
            }

            if (textRect == null)
                textRect = new Rect();

            // 判断是否被选择
            if (isSelected()) {
                textPaint.setColor(color0);
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
                int divFont = Math.abs(id - selectedId) - 1;
                if (divFont == 0) {
                    textPaint.setColor(color1);
                    textPaint.setTextSize(28.0f);
                    width = 26;
                    height = 26;
                } else if (divFont == 1) {
                    textPaint.setColor(color2);
                    textPaint.setTextSize(26.0f);
                    width = 22;
                    height = 22;
                } else if (divFont == 2) {
                    textPaint.setColor(color3);
                    textPaint.setTextSize(24.0f);
                    width = 18;
                    height = 18;
                } else if (divFont >= 3) {
                    textPaint.setColor(color4);
                    textPaint.setTextSize(20.0f);
                    width = 14;
                    height = 14;
                }
            }

            // 返回包围整个字符串的最小的一个Rect区域
            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
            // 判断是否可视
            if (!isInView())
                return;

            // 绘制内容
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getContext(), res, width, height);
            canvas.drawBitmap(bitmap, x + 40, y + 20 + move, null);
            canvas.drawText(itemText, x + 120, y + move + unitHeight / 2 + textRect.height() / 2,
                    textPaint);

        }

        /**
         * 是否在可视界面内
         *
         * @return
         */
        public boolean isInView() {
            return !(y + move > controlHeight)
                    && (y + move + unitHeight / 2 + textRect.height() / 2) >= 0;
        }

        /**
         * 移动距离
         *
         * @param _move
         */
        public void move(int _move) {
            this.move = _move;
        }

        /**
         * 设置新的坐标
         *
         * @param _move
         */
        public void newY(int _move) {
            this.move = 0;
            this.y = y + _move;
        }

        /**
         * 判断是否在选择区域内
         *
         * @return
         */
        public boolean isSelected() {
            if ((y + move) >= controlHeight / 2 - unitHeight / 2 + 2
                    && (y + move) <= controlHeight / 2 + unitHeight / 2 - 2)
                return true;
            if ((y + move + unitHeight) >= controlHeight / 2 - unitHeight / 2
                    + 2
                    && (y + move + unitHeight) <= controlHeight / 2
                    + unitHeight / 2 - 2)
                return true;
            return (y + move) <= controlHeight / 2 - unitHeight / 2 + 2
                    && (y + move + unitHeight) >= controlHeight / 2
                    + unitHeight / 2 - 2;
        }

        /**
         * 获取移动到标准位置需要的距离
         */
        public float moveToSelected() {
            return (controlHeight / 2 - unitHeight / 2) - (y + move);
        }
    }

    void onEndSelect(ItemObject item) {
        if (onSelectListener != null) {
            onSelectListener.endSelect(item.id, item.tag);
        }
    }

    void onSelecting(ItemObject item) {
        if (onSelectListener != null) {
            onSelectListener.selecting(item.id, item.tag);
        }
    }
}

