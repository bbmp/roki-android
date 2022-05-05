package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.ui.R;
import com.legent.utils.api.DisplayUtils;

public class TitleBar extends FrameLayout {

    public final static int Content_Left = 1;
    public final static int Content_Right = 2;
    public final static int Content_Middle = 3;

    public static ImageView newTitleIconView(Context cx, int imgResid, View.OnClickListener listener) {
        ImageView img = newTitleIconView(cx, imgResid);
        img.setOnClickListener(listener);
        return img;
    }

    public static ImageView newTitleIconView(Context cx, int imgResid) {
        ImageView img = newTitleIconView(cx);
        img.setImageResource(imgResid);
        return img;
    }

    public static ImageView newTitleIconView(Context cx) {
        ImageView img = (ImageView) LayoutInflater.from(cx).inflate(R.layout.view_titlebar_icon, null);

        int imgWidth = (int) DisplayUtils.getActionBarHeight(cx);
        img.setLayoutParams(new ViewGroup.LayoutParams(
                imgWidth,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return img;
    }

    public static TextView newTitleTextView(Context cx, String text, View.OnClickListener listener) {
        TextView txt = newTitleTextView(cx, text);
        txt.setOnClickListener(listener);
        return txt;
    }

    public static TextView newTitleTextView(Context cx, String text) {
        TextView txt = newTitleTextView(cx);
        txt.setText(text );
        return txt;
    }

    public static TextView newTitleTextView(Context cx) {
        TextView txt = (TextView) LayoutInflater.from(cx).inflate(R.layout.view_titlebar_text, null);
        txt.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return txt;
    }

    //----------------------------------------------------------------------------------
    // TitleBar
    //----------------------------------------------------------------------------------
    private TextView txtTitle;
    private RelativeLayout pnlMiddle;
    private RelativeLayout rel_titlebar;
    private LinearLayout pnlLeft, pnlRight;
    private View divider;


    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void init(Context cx, AttributeSet attrs) {

        View titleView = LayoutInflater.from(cx).inflate(
                R.layout.view_titlebar, this, true);
        divider = titleView.findViewById(R.id.divider);
        txtTitle = titleView.findViewById(R.id.txtTitle);
        pnlLeft = titleView.findViewById(R.id.pnlLeft);
        pnlRight = titleView.findViewById(R.id.pnlRight);
        pnlMiddle = titleView.findViewById(R.id.pnlMiddle);
        rel_titlebar = titleView.findViewById(R.id.rel_titlebar);
        txtTitle.setText(null);
        pnlRight.removeAllViews();

    }

    //----------------------------------------------------------------------------------
    // Middle TitleText
    //----------------------------------------------------------------------------------

    public void setTitle(int stringResId) {
        txtTitle.setText(stringResId);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setTitleColor(int color) {
        txtTitle.setTextColor(color);
    }

    public void setTitleSize(float spSize) {
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
    }
    public void setTitleBarBackgroundTransparent() {
        rel_titlebar.setBackgroundColor(getResources().getColor(R.color.Transparent));
    }

    //----------------------------------------------------------------------------------
    // Divider
    //----------------------------------------------------------------------------------

    public void setDividerVisible(boolean visible) {
        divider.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setDividerBackground(int resid) {

        divider.setBackgroundResource(resid);
        divider.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
    }

    public void setDividerHeight(int dpHeight) {

        ViewGroup.LayoutParams params = divider.getLayoutParams();
        params.height = dpHeight;
        divider.setLayoutParams(params);
    }

    //----------------------------------------------------------------------------------
    // Middle Content
    //----------------------------------------------------------------------------------

    public void replaceMiddle(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        replaceMiddle(view);
    }

    public void replaceMiddle(View view) {
        replaceContentView(Content_Middle, view);
    }

    public void resetMiddle(View view) {
        replaceContentView(Content_Middle, txtTitle);
    }


    //----------------------------------------------------------------------------------
    // Left Content
    //----------------------------------------------------------------------------------

    public void addLeft(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        addLeft(view);
    }

    public void addLeft(View view) {
        addContentView(Content_Left, view);
    }

    public void replaceLeft(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        replaceLeft(view);
    }

    public void replaceLeft(View view) {
        replaceContentView(Content_Left, view);
    }


    public void clearLeft() {
        clearContentView(Content_Left);
    }

    //----------------------------------------------------------------------------------
    // Left Content
    //----------------------------------------------------------------------------------

    public void addRight(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        addRight(view);
    }

    public void addRight(View view) {
        addContentView(Content_Right, view);
    }

    public void replaceRight(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        replaceRight(view);
    }

    public void replaceRight(View view) {
        replaceContentView(Content_Right, view);
    }

    public void clearRight() {
        clearContentView(Content_Right);
    }

    //----------------------------------------------------------------------------------
    // protected
    //----------------------------------------------------------------------------------


    protected void addContentView(int type, View view) {
        if (view == null) return;

        ViewGroup group = null;
        switch (type) {
            case Content_Left:
                group = pnlLeft;
                break;
            case Content_Right:
                group = pnlRight;
                break;
            case Content_Middle:
                group = pnlMiddle;
                break;
        }

        if (group != null) {
            group.addView(view);
        }
    }


    protected void replaceContentView(int type, View view) {
        if (view == null) return;

        ViewGroup group = null;
        switch (type) {
            case Content_Left:
                group = pnlLeft;
                break;
            case Content_Right:
                group = pnlRight;
                break;
            case Content_Middle:
                group = pnlMiddle;
                break;
        }

        if (group != null) {
            group.removeAllViews();
            group.addView(view);
        }
    }


    protected void clearContentView(int type) {
        ViewGroup group = null;
        switch (type) {
            case Content_Left:
                group = pnlLeft;
                break;
            case Content_Right:
                group = pnlRight;
                break;
            case Content_Middle:
                group = pnlMiddle;
                break;
        }

        if (group != null) {
            group.removeAllViews();
        }
    }


}
