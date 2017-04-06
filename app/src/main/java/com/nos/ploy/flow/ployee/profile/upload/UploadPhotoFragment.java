package com.nos.ploy.flow.ployee.profile.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.PostDeleteProfileImageGson;
import com.nos.ploy.api.account.model.PostUploadProfileImageGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.profile.upload.view.UploadPhotoRecyclerAdapter;
import com.nos.ploy.utils.ImagePickerUtils;
import com.nos.ploy.utils.MyFileUtils;
import com.nos.ploy.utils.PopupMenuUtils;
import com.nos.ploy.utils.RecyclerUtils;

import net.yazeed44.imagepicker.model.ImageEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 24/12/2559.
 */

public class UploadPhotoFragment extends BaseFragment {

    private static final String KEY_DATAS = "PROFILE_IMAGE_DATAS";
    @BindView(R.id.recyclerview_upload_photo)
    RecyclerView mRecyclerViewUploadPhoto;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.swiperefreshlayout_upload_photo)
    SwipeRefreshLayout mSwipreRefreshLaayout;
    @BindString(R.string.Upload_photo)
    String LUpload_photo;
    @BindDrawable(R.drawable.ic_add_gray_48dp)
    Drawable mDrawableAddGray;
    private List<ProfileImageGson.Data> mDatas = new ArrayList<>();
    private List<PostUploadProfileImageGson.ImageBody> mImageToUploads = new ArrayList<>();
    private long mUserId;
    private AccountApi mApi;
    private Action1<List<ProfileImageGson.Data>> mCallbackOnRefreshFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            dismissRefreshing();
            bindData(datas);
        }
    };
    private OnDataChangeListener mOnDataChangeListener;
    private RetrofitCallUtils.RetrofitCallback<ProfileImageGson> mCallBackUpload = new RetrofitCallUtils.RetrofitCallback<ProfileImageGson>() {
        @Override
        public void onDataSuccess(ProfileImageGson data) {
            dismissLoading();
            if (data != null && null != data.getData()) {
                mOnDataChangeListener.onDataChange();
                refreshData();
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();
        }
    };
    private RetrofitCallUtils.RetrofitCallback<BaseResponse> mCallbackDelete = new RetrofitCallUtils.RetrofitCallback<BaseResponse>() {
        @Override
        public void onDataSuccess(BaseResponse data) {
            dismissLoading();
            showToast("Success");
            mOnDataChangeListener.onDataChange();
            refreshData();
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();
        }
    };

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.profileScreenUploadHeader);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menu_done_item_done,data.doneLabel);
    }

    private UploadPhotoRecyclerAdapter mAdapter = new UploadPhotoRecyclerAdapter() {

        @Override
        public void onBindViewHolder(final UploadPhotoRecyclerAdapter.ViewHolder holder, int position) {
            View.OnClickListener onclick = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ImagePickerUtils.pickImage(v.getContext(), new Action1<ImageEntry>() {
                        @Override
                        public void call(final ImageEntry imageEntry) {
                            if (null != imageEntry && !TextUtils.isEmpty(imageEntry.path)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(v.getContext()).load(imageEntry.path).asBitmap().into(new BitmapImageViewTarget(holder.imgUpload) {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                super.onResourceReady(resource, glideAnimation);
                                                holder.imgUpload.setImageBitmap(resource);
                                                if (RecyclerUtils.isAvailableData(mDatas, holder.getAdapterPosition())) {
                                                    ProfileImageGson.Data data = mDatas.get(holder.getAdapterPosition());
                                                    mImageToUploads.add(new PostUploadProfileImageGson.ImageBody(data.getImgId(), MyFileUtils.encodeToBase64(resource)));
                                                } else {
                                                    mImageToUploads.add(new PostUploadProfileImageGson.ImageBody(MyFileUtils.encodeToBase64(resource)));
                                                }


                                            }
                                        });
                                    }
                                });
                            }

                        }
                    });
                }
            };
            if (RecyclerUtils.isAvailableData(mDatas, position) && !TextUtils.isEmpty(mDatas.get(position).getImagePath())) {
                Glide.with(holder.imgUpload.getContext()).load(mDatas.get(position).getImagePath()).into(holder.imgUpload);
                holder.imgUpload.setOnClickListener(onclick);
                holder.imgUpload.setLongClickable(true);
                holder.imgUpload.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (RecyclerUtils.isAvailableData(mDatas, holder.getAdapterPosition())) {
                            ProfileImageGson.Data data = mDatas.get(holder.getAdapterPosition());
                            attempDeleteProfileImage(v.getContext(), data.getImgId());
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                holder.imgUpload.setImageDrawable(mDrawableAddGray);
                holder.imgUpload.setOnClickListener(onclick);
                holder.imgUpload.setLongClickable(false);
                holder.imgUpload.setOnLongClickListener(null);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    };

    public static UploadPhotoFragment newInstance(long userId, ArrayList<ProfileImageGson.Data> datas, OnDataChangeListener listener) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_DATAS, datas);
        args.putLong(KEY_USER_ID, userId);
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        fragment.setArguments(args);
        fragment.setOnDataChangeListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments() && null != getArguments().getParcelableArrayList(KEY_DATAS)) {
            mDatas = getArguments().getParcelableArrayList(KEY_DATAS);
            mUserId = getArguments().getLong(KEY_USER_ID, 0);
        }
        mApi = getRetrofit().create(AccountApi.class);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_photo, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initToolbar();
        initRecyclerView();
    }

    private void initView() {
        setRefreshLayout(mSwipreRefreshLaayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        showRefreshing();
        AccountInfoLoader.getProfileImage(getContext(), mUserId, true, mCallbackOnRefreshFinish);
    }

    private void initRecyclerView() {
        mRecyclerViewUploadPhoto.setLayoutManager(new GridLayoutManager(getContext(), RecyclerUtils.calculateNoOfColumns(getContext(), 160)));
        mRecyclerViewUploadPhoto.setAdapter(mAdapter);
    }

    private void bindData(List<ProfileImageGson.Data> datas) {
        mDatas.clear();
        notifyDataSetChanged();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initToolbar() {
        mTextViewTitle.setText(LUpload_photo);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    requestSaveProfileImage();
                }
                return false;
            }
        });
    }

    private void requestSaveProfileImage() {
        showLoading();
        RetrofitCallUtils.with(mApi.postUploadProfileImage(PostUploadProfileImageGson.with(mUserId).create(mImageToUploads))
                , mCallBackUpload)
                .enqueue(getContext());
    }


    private void attempDeleteProfileImage(Context context, final long imageId) {
        PopupMenuUtils.showConfirmationAlertMenu(context, "Delete", "Do you want to delete this image?", new Action1<Boolean>() {
            @Override
            public void call(Boolean yes) {
                if (yes) {
                    requestDeleteProfileImage(imageId);
                }
            }
        });

    }

    private void requestDeleteProfileImage(long imageId) {
        showLoading();
        RetrofitCallUtils
                .with(mApi.postDeleteProfileImage(new PostDeleteProfileImageGson(imageId)), mCallbackDelete)
                .enqueue(getContext());
    }

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.mOnDataChangeListener = listener;
    }


    public static interface OnDataChangeListener {
        void onDataChange();
    }
}
