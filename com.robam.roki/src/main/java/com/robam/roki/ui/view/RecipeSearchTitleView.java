package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.utils.LogUtils;
import com.legent.utils.api.SoftInputUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RecipeSearchTitleView extends FrameLayout {

    public interface OnCancelSearchCallback {
        void onCancel();

        void onWordChanged(String word);

        void onSearchReturn();
    }


    public RecipeSearchTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeSearchTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    @InjectView(R.id.iv_delete_search_word)
    ImageView ivDeleteSearchWord;

    @InjectView(R.id.edtSearch)
    EditText edtSearch;

    @InjectView(R.id.search_return)
    ImageView search_return;

    OnCancelSearchCallback callback;
    boolean isEdit;

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(
                R.layout.view_recipe_search_title, this, true);


        if (!isInEditMode()) {
            ButterKnife.inject(this, view);
            edtSearch.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            );
            edtSearch.addTextChangedListener(watcher);
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        if (callback != null) {
                            LogUtils.i("20180302", "edtSearCh:" + edtSearch.getText().toString());
                            if (!TextUtils.isEmpty(edtSearch.getText())) {
                                callback.onWordChanged(edtSearch.getText().toString());
                            } else {
                                ToastUtils.show("请输入关键字", Toast.LENGTH_SHORT);
                            }

                        }
                    }
                    return false;
                }
            });
            toggleSoftInput();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftInputFromWindow(edtSearch);
                }
            },300);

        }
    }


    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
//            Log.i("onTextChanged", s.toString());
            if (s != null && s.toString() != null && s.toString().length() != 0){
                if (ivDeleteSearchWord != null)
                ivDeleteSearchWord.setVisibility(VISIBLE);
            }else {
                if (ivDeleteSearchWord != null)
                ivDeleteSearchWord.setVisibility(INVISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
//            Log.i("beforeTextChanged", s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
//            Log.i("afterTextChanged", s.toString());
//            if (s.length() > MAX_NUM) {
//                s.delete(MAX_NUM, s.length());
//            }
//            int num = MAX_NUM - s.length();
//            tvLeftNum.setText(String.valueOf(num));
        }
    };


    /**
     * EditText获取焦点弹出软键盘
     *
     * @param editText
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }

    public EditText getEdtSearch() {
        return edtSearch;
    }

    //    @OnClick(R.id.txtCanel)
//    public void onCancel() {
//        if (callback != null) {
//            LogUtils.i("20180302","edtSearCh:"+edtSearch.getText().toString());
//            if (!TextUtils.isEmpty(edtSearch.getText())){
//                callback.onWordChanged(edtSearch.getText().toString());
//            }else{
//                ToastUtils.show("请输入关键字", Toast.LENGTH_SHORT);
//            }
//        }
//    }


    @OnClick(R.id.iv_delete_search_word)
    public void onDeleteSearchWord() {
        edtSearch.setText("");
    }

    @OnClick(R.id.search_return)
    public void onReturn() {
        if (callback != null) {
            callback.onSearchReturn();
        }
    }

    private void toggleSoftInput() {
        SoftInputUtils.show(getContext(), edtSearch);
    }

    public void setOnCancelSearchCallback(OnCancelSearchCallback callabck) {
        this.callback = callabck;
    }

    public void setWord(final String word) {
        edtSearch.setText(word);
    }


    void search(String word) {
        if (callback != null) {
            callback.onWordChanged(word);
        }
    }

    int msgCount = 0;
    final int MSGCODE = 0;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSGCODE) {
                if (msgCount == 1) {
                    search(edtSearch.getText().toString());
                    msgCount = 0;
                } else {
                    msgCount--;
                }

            }
        }
    };
}
