package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneEnum;
import com.robam.common.util.NumberUtil;
import com.robam.roki.R;
import com.robam.roki.db.model.CookingStepsModel;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.dialog.CookbookRandomShareDialog;
import com.robam.roki.ui.dialog.ImageShareDialog;
import com.robam.roki.ui.dialog.KitchenSourceShareDialog;
import com.robam.roki.ui.mdialog.MenuDialog;
import com.robam.roki.ui.mdialog.RecipteMutiShareDialog;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * @author r210190
 * des 610多段菜谱adapter
 */
public class Rv610RecipeAdapter extends BaseQuickAdapter<RecipeBean, BaseViewHolder> {
    String[] strings = new String[]{"0" ,"一" , "二"  ,"三" ,"四" ,"四" ,"四" ,"四" ,"四" ,"四" };
    private int selectPosition = -1;

    private int delPosition = -1 ;

    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;

    public Rv610RecipeAdapter() {
        super(R.layout.item_d610_recipe);
    }

    public String saveBitmapFile(Bitmap bitmap){
        File file=new File(Environment.getExternalStorageDirectory()+"/pic/01.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            return file.getAbsolutePath();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeBean item) {
        if (item != null){
            if (holder.getLayoutPosition() == selectPosition ){
                holder.setVisible(R.id.iv_select  , true);
            }else {
                holder.setVisible(R.id.iv_select , false);
            }
            List<RecipeStepBean> recipeStepList = item.getRecipeStepList();
            SettingBar stbStep = (SettingBar)holder.getView(R.id.stb_step_top);

//            holder.getView(R.id.txt_share).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    RecipteMutiShareDialog mRecipteMultipleShareDialog=new RecipteMutiShareDialog(getContext(),item.getRecipe_names(),recipeStepList);
////                    mRecipteMultipleShareDialog.create();
////                    mRecipteMultipleShareDialog.show();
//
////                    ImageShareDialog.show(getContext());
////
//                }
//            });
            stbStep.setLeftText(item.getRecipe_names());
            stbStep.setRightText(item.getRecipeStepTimes() + "min");
//            addChildClickViewIds(R.id.stb_step_top , R.id.tv_del);
            addChildClickViewIds( R.id.tv_del);
            stbStep.getLeftView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectListener != null){
                        onSelectListener.onSelect(holder.getLayoutPosition());
                    }
                }
            });
//            addChildLongClickViewIds(R.id.stb_step_top);
            RecyclerView rv610Step = (RecyclerView) holder.getView(R.id.rv_610_step);
            rv610Step.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
//          rv610Step.setLayoutManager(new GridLayoutManager(getContext(), 2));
            Rv610RecipeStepAdapter rv610StepAdapter = new Rv610RecipeStepAdapter();
            rv610Step.setAdapter(rv610StepAdapter);
            rv610StepAdapter.addData(recipeStepList);

            rv610StepAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onSelectListener != null){
                        onSelectListener.onSelect(holder.getLayoutPosition());
                    }
                }
            });

            rv610StepAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onLeftTouchListener != null){
                        onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
                    }
                    return false;
                }
            });
//            rv610StepAdapter.addOnLeftTouchListener(new Rv610StepAdapter.OnLeftTouchListener() {
//                @Override
//                public void onLeftTouch(int position) {
//                    if (onLeftTouchListener != null){
//                        onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
//                    }
//                }
//            });

            View v_del = holder.getView(R.id.tv_del);
            if (holder.getLayoutPosition() == delPosition){
//                holder.setVisible(R.id.tv_del , holder.getLayoutPosition() == selectPosition ? true : false);
                v_del.setVisibility(View.VISIBLE);
            }else {
                v_del.setVisibility(View.GONE);
            }


//            stbStep.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            mPosX = event.getX();
//                            mPosY = event.getY();
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            mCurPosX = event.getX();
//                            mCurPosY = event.getY();
//
//                            break;
//                        case MotionEvent.ACTION_UP:
////                            if (mCurPosY - mPosY > 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向下滑動
////
////                            } else if (mCurPosY - mPosY < 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向上滑动
////                            }
//                            if (mCurPosX - mPosX > 0
//                                    && (Math.abs(mCurPosX - mPosX) > 3)) {
//                                //向右滑動
////                                ToastUtils.showShort("向右滑动");
//                                if (onLeftTouchListener != null){
//                                    onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
//                                    return true;
//                                }
//
//                            } else if (mCurPosX - mPosX < 0
//                                    && (Math.abs(mCurPosX - mPosX) > 3)) {
//                                //向左滑动
////                                ToastUtils.showShort("向左滑动");
//                                if (onLeftTouchListener != null){
//                                    onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
//                                    return true;
//                                }
//                            }
//                            break;
//                    }
//                    return false;
//                }
//            });
        }
    }

    public void setSelectPosition(int selectPosition) {
        if (this.selectPosition == selectPosition){
            this.selectPosition = -1 ;
        }else {
            this.selectPosition = selectPosition;
        }

        notifyDataSetChanged();
    }
    public void setDelPosition(int delPosition) {
        if (this.delPosition == delPosition){
            this.delPosition = -1 ;
            notifyDataSetChanged();
        }else {
            this.delPosition = delPosition;
            notifyDataSetChanged();
        }

    }
    public void setRightDelPosition(int delPosition) {
        if (this.delPosition == delPosition){
            this.delPosition = -1 ;
            notifyDataSetChanged();
        }

    }

    OnSelectListener onSelectListener ;

    String recipeName;
    public void setRecipeName(String recipeName) {
        this.recipeName=recipeName;

    }

    public interface OnSelectListener{
        void onSelect(int position);
    }

    public void addOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnLeftTouchListener{
        void onLeftTouch(int position);
    }
    Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener ;

    public void addOnLeftTouchListener(Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }

    public int getSelectPosition() {
        return selectPosition;
    }


}
