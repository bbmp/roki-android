//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//
///**
// * WheelView滚轮 rent
// * <p/>
// */
//public class AbsWheelView extends AbsWheelView2 {
//
//
//    public AbsWheelView(Context context, AttributeSet attrs,
//                        int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public AbsWheelView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public AbsWheelView(Context context) {
//        super(context);
//    }
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawLine(canvas);
//        drawList(canvas);
//        //drawMask(canvas);
//    }
//
//    @Override
//    protected ItemObject getItemObject() {
//        return new ItemObjectex();
//    }
//
//    /**
//     * 绘制选中阴影
//     *
//     * @param canvas
//     */
//    protected void drawLine(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(0x343434);
//        canvas.drawRect(0, center_above_y, controlWidth, center_below_y, paint);
//    }
//
//    protected synchronized void drawList(Canvas canvas) {
//        if (isClearing)
//            return;
//        try {
//            for (ItemObject itemObject : itemList) {
//                itemObject.drawSelf(canvas);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 单条内容
//     *
//     * @author JiangPing
//     */
//    protected class ItemObjectex extends ItemObject {
//
//        public ItemObjectex() {
//            super();
//        }
//
//        /**
//         * 绘制自身
//         *
//         * @param canvas
//         */
//        @Override
//        protected void drawSelf(Canvas canvas) {
//            if (textPaint == null) {
//                textPaint = new Paint();
//                textPaint.setAntiAlias(true);
//            }
//
//            if (textRect == null)
//                textRect = new Rect();
//
//            // 判断是否被选择
//            if (isSelected()) {
//                textPaint.setColor(color0);
//                // 获取距离标准位置的距离
//                float moveToSelect = moveToSelected();
//                moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
//                        * (-1);
//                // 计算当前字体大小
//                float textSize = normalFont
//                        + ((selectedFont - normalFont) * (1.0f - moveToSelect
//                        / (float) unitHeight));
//                textPaint.setTextSize(textSize);
//            } else {
//                textPaint.setColor(normalColor);
//                textPaint.setTextSize(normalFont);
//            }
//
//            // 返回包围整个字符串的最小的一个Rect区域
//            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
//            // 判断是否可视
//            if (!isInView())
//                return;
//
//            // 绘制内容
//            // 绘制内容
//            canvas.drawText(getDrawText(itemText), x + controlWidth / 2 - textRect.width() / 2, y + move + unitHeight / 2 + textRect.height() / 2,
//                    textPaint);
//        }
//
//    }
//}
