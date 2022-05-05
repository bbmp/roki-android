package com.robam.roki.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.aispeech.ailog.AILog;
import com.aispeech.ailog.AILog;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;
import com.robam.roki.model.bean.MessageBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.RvSpeechRecipeAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.RecipeDetailPage;

import java.util.LinkedList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit.http.POST;


public class DialogAdapter extends RecyclerView.Adapter {

    private static final String TAG = "DialogAdapter";
    public static LinkedList<MessageBean> mList;

    public DialogAdapter(LinkedList<MessageBean> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AILog.i(TAG, "onCreateViewHolder" + viewType);
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MessageBean.TYPE_INPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_input, parent, false);
                holder = new InputViewHolder(view);
                break;
            case MessageBean.TYPE_OUTPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_output, parent, false);
                holder = new OutputViewHolder(view);
                break;
            case MessageBean.TYPE_WIDGET_LIST:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_output_result, parent, false);
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_output_recipe_list, parent, false);
                holder = new RecipeViewHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, position + "");

        final MessageBean message = mList.get(position);
        LogUtils.i(TAG, "mList:" + mList.size());
        int itemViewType = mList.get(position).getType();
        switch (itemViewType) {
            case MessageBean.TYPE_INPUT:

                if (holder instanceof InputViewHolder) {
                    if (position == 0) {
                        ((InputViewHolder) holder).content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    } else if (position == 1) {
                        ((InputViewHolder) holder).content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    }
                    ((InputViewHolder) holder).content.setText(message.getText());
                }

                break;
            case MessageBean.TYPE_OUTPUT:
                if (holder instanceof OutputViewHolder) {
                    if (position == 0) {
                        ((OutputViewHolder) holder).content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else if (position == 1) {
                        ((OutputViewHolder) holder).content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    }
                    String text = message.getText();
                    if("ROKI没有听清，请再说一遍好吗？".equals(text)){
                        ((OutputViewHolder) holder).content.setText(text);
                    }else {
                        SpannableString spannableString = new SpannableString(text);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                                0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FB7432")),
                                8, text.length() - 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                                text.length() - 11, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ((OutputViewHolder) holder).content.setText(spannableString);
                    }
                }
            case MessageBean.TYPE_WIDGET_LIST:
                if (holder instanceof RecipeViewHolder) {
                    RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
                    LogUtils.i(TAG, " message widget list recipe ");
                    recipeViewHolder.tvMoreSearchRecipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.i(TAG, "message text" + message.getText());
                            Bundle bundle = new Bundle();
                            bundle.putString(PageArgumentKey.text, message.getText());
                            bundle.putString(PageArgumentKey.come_from ,"SpeechRecipePage");
                            UIService.getInstance().postPage(PageKey.RecipeSearch, bundle);
                        }
                    });

                    recipeViewHolder.rvSpeechRecipeList.setLayoutManager(new LinearLayoutManager(recipeViewHolder.itemView.getContext(), RecyclerView.HORIZONTAL, false));
//                    recipeViewHolder.rvSpeechRecipeList.setAdapter(new SpeechRecipeAdapter(recipeViewHolder.itemView.getContext(), message.getRecipeList()));
                    RvSpeechRecipeAdapter rvSpeechRecipeAdapter = new RvSpeechRecipeAdapter();
                    recipeViewHolder.rvSpeechRecipeList.setAdapter(rvSpeechRecipeAdapter);
                    rvSpeechRecipeAdapter.addData(message.getRecipeList());
                    rvSpeechRecipeAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                            Recipe item = rvSpeechRecipeAdapter.getItem(position);
                            RecipeDetailPage.show(item.id, item.sourceType);
                        }
                    });

                }

                break;
            default:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType " + position);
        return mList.get(position).getType();
    }

    class OutputViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public OutputViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    class InputViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public InputViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFindRecipeNote;
        private TextView tvMoreSearchRecipe;
        private RecyclerView rvSpeechRecipeList;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            tvFindRecipeNote = (TextView) itemView.findViewById(R.id.tv_find_recipe_note);
            tvMoreSearchRecipe = (TextView) itemView.findViewById(R.id.tv_more_search_recipe);
            rvSpeechRecipeList = (RecyclerView) itemView.findViewById(R.id.rv_speech_recipe_list);
        }
    }


}
