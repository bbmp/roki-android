package com.robam.roki.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.RecipePraiseEvent;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.ui.ext.views.HorizontalListView;
import com.legent.utils.EventUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.CookMomentsRefreshEvent;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.ui.page.RecipeShowPage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RecipeDetailAlbumView extends FrameLayout {

//    DisplayImageOptions DisplayImageOptions_UserFace = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.mipmap.ic_recipe_figure_default)
//            .showImageForEmptyUri(R.mipmap.ic_recipe_figure_default)
//            .showImageOnFail(R.mipmap.ic_recipe_figure_default).cacheInMemory(true)
//            .cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
    RequestOptions DisplayImageOptions_UserFace = new RequestOptions().centerCrop()
            .placeholder(R.mipmap.ic_recipe_figure_default)
            .error(R.mipmap.ic_recipe_figure_default)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(new CircleCrop());

    @InjectView(R.id.albumsView)
    HorizontalListView albumsView;

    Adapter adapter;
    long bookId;

    int gridWidth, gridImageHeight;


    public RecipeDetailAlbumView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeDetailAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeDetailAlbumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    @Subscribe
    public void onEvent(CookMomentsRefreshEvent event) {
        loadData(this.bookId);
    }

    @OnClick(R.id.pnlUpload)
    public void onClick(View view) {
        onShowCooking();
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_detail_album,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            computeSize();

            adapter = new Adapter();
            albumsView.setAdapter(adapter);

            albumsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CookAlbum album = adapter.getEntity(position);
                    if (album.id == 0) {
                        onShowCooking();
                    }
                }
            });
        }
    }

    void computeSize() {
        Context cx = getContext();
        int screenWidth = DisplayUtils.getScreenWidthPixels(cx);
        gridWidth = (screenWidth - DisplayUtils.dip2px(cx, 5 * 3)) / 2;
        gridImageHeight = (gridWidth - DisplayUtils.dip2px(cx, 5 * 2)) * 6 / 7;

        ViewGroup.LayoutParams lp = albumsView.getLayoutParams();
        lp.height = gridWidth * 351 / 302;
        albumsView.setLayoutParams(lp);
    }

    public void onShowCooking() {
        Activity atv = UIService.getInstance().getMain().getActivity();
        RecipeShowPage.showCooking(atv, bookId);
    }

    public void loadData(long bookId) {
        this.bookId = bookId;
        CookbookManager.getInstance().getMyCookAlbumByCookbook(bookId,
                new Callback<CookAlbum>() {

                    @Override
                    public void onSuccess(CookAlbum result) {
                        adapter.insertTop(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

        CookbookManager.getInstance().getOtherCookAlbumsByCookbook_new(bookId, 0, 100,
                new Callback<List<CookAlbum>>() {
                    @Override
                    public void onSuccess(List<CookAlbum> result) {
                        adapter.appendList(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

    }


    class Adapter extends ExtBaseAdapter<CookAlbum> {

        CookAlbum top, last;
        List<CookAlbum> appendList;

        public Adapter() {
            last = new CookAlbum();
            last.id = 0;
        }

        public void insertTop(CookAlbum album) {
            this.top = album;
            refresh();
        }

        public void appendList(List<CookAlbum> albums) {
            appendList = albums;
            refresh();
        }

        private void refresh() {
            list.clear();
            if (top != null) {
                list.add(0, top);
            }

            if (appendList != null && appendList.size() > 0) {
                list.addAll(appendList);
            }

            if (list.size() >= 1) {
                list.add(last);
            }

            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.view_recipe_detail_album_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final CookAlbum album = list.get(position);
            vh.showData(album);

            return convertView;
        }

        class ViewHolder {

            @InjectView(R.id.imgPhoto)
            ImageView imgPhoto;
            @InjectView(R.id.txtDesc)
            TextView txtDesc;
            @InjectView(R.id.imgOwnerFigure)
            ImageView imgOwnerFigure;
            @InjectView(R.id.txtOwnerName)
            TextView txtOwnerName;
            @InjectView(R.id.txtPraiseCount)
            TextView txtPraiseCount;
            @InjectView(R.id.imgParise)
            ImageView imgParise;

            CookAlbum album;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);

                view.getLayoutParams().width = gridWidth;
                imgPhoto.getLayoutParams().height = gridImageHeight;
            }

            void showData(CookAlbum album) {
                this.album = album;

                final boolean isCamera = album == last;
                txtPraiseCount.setVisibility(isCamera ? GONE : VISIBLE);
                imgParise.setEnabled(!isCamera);

                if (isCamera) {
                    imgOwnerFigure.setImageResource(R.mipmap.ic_recipe_figure_default);
                    imgPhoto.setImageResource(R.mipmap.ic_recipe_camera);
                    txtOwnerName.setText("昵称");
                    txtDesc.setText("上传你烹饪的美味佳肴吧");
                } else {
                    imgPhoto.setImageBitmap(null);
                    imgOwnerFigure.setImageResource(R.mipmap.ic_recipe_figure_default);
                    ImageUtils.displayImage(getContext(), album.imgUrl, imgPhoto);

                    txtDesc.setText(album.desc);
                    txtPraiseCount.setText(String.format("%s 赞", album.praiseCount));
                    imgParise.setSelected(album.hasPraised);

                    Plat.accountService.getUser(album.ownerId, new Callback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            txtOwnerName.setText(user.name);
                            ImageUtils.displayImage(getContext(), user.figureUrl, imgOwnerFigure, DisplayImageOptions_UserFace);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
            }

            void refresh() {
                txtPraiseCount.setText(String.valueOf(album.praiseCount));
                imgParise.setSelected(album.hasPraised);
            }

            @OnClick(R.id.imgParise)
            public void onClickParise() {
                if (album == last) return;

                if (album.hasPraised) {
                    CookbookManager.getInstance().unpraiseCookAlbum(album.id,
                            new VoidCallback() {

                                @Override
                                public void onSuccess() {
                                    album.hasPraised = !album.hasPraised;
                                    album.praiseCount--;
                                    refresh();
                                    EventUtils.postEvent(new RecipePraiseEvent(album.id+"",false));
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    ToastUtils.showThrowable(t);
                                }
                            });
                } else {
                    CookbookManager.getInstance().praiseCookAlbum(album.id,
                            new VoidCallback() {

                                @Override
                                public void onSuccess() {
                                    album.hasPraised = !album.hasPraised;
                                    album.praiseCount++;
                                    refresh();
                                    EventUtils.postEvent(new RecipePraiseEvent(album.id+"",true));
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    ToastUtils.showThrowable(t);
                                }
                            });
                }
            }

        }
    }

}
