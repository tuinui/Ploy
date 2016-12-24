package com.nos.ploy.flow.ployee.profile.upload;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.PostUploadProfileImageGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.profile.upload.view.UploadPhotoRecyclerAdapter;
import com.nos.ploy.utils.ImagePickerUtils;
import com.nos.ploy.utils.MyFileUtils;
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

    @BindView(R.id.recyclerview_upload_photo)
    RecyclerView mRecyclerViewUploadPhoto;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.swiperefreshlayout_upload_photo)
    SwipeRefreshLayout mSwipreRefreshLaayout;
    @BindString(R.string.Upload_photo)
    String LUpload_photo;
    @BindDrawable(R.drawable.ic_add_gray_48dp)
    Drawable mDrawableAddGray;
    private List<ProfileImageGson.Data> mDatas = new ArrayList<>();
    private List<String> base64ToUploads = new ArrayList<>();
    private RetrofitCallUtils.RetrofitCallback<ProfileImageGson> mCallBackUpload = new RetrofitCallUtils.RetrofitCallback<ProfileImageGson>() {
        @Override
        public void onDataSuccess(ProfileImageGson data) {
            dismissLoading();
            if (data != null && null != data.getData()) {
                mOnDataChangeListener.onDataChange();
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(ResponseMessage failCause) {
            dismissLoading();
        }
    };


    private Action1<List<ProfileImageGson.Data>> mCallbackOnRefreshFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(List<ProfileImageGson.Data> datas) {
            dismissRefreshing();
            bindData(datas);
        }
    };


    private UploadPhotoRecyclerAdapter mAdapter = new UploadPhotoRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final UploadPhotoRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mDatas, position) && !TextUtils.isEmpty(mDatas.get(position).getImagePath())) {
                Glide.with(holder.imgUpload.getContext()).load(mDatas.get(position).getImagePath()).into(holder.imgUpload);
            } else {
                holder.imgUpload.setImageDrawable(mDrawableAddGray);
                holder.imgUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        ImagePickerUtils.pickImage(v.getContext(), new Action1<ImageEntry>() {
                            @Override
                            public void call(ImageEntry imageEntry) {
                                if (null != imageEntry && !TextUtils.isEmpty(imageEntry.path)) {
                                    Glide.with(v.getContext()).load(imageEntry.path).asBitmap().into(new BitmapImageViewTarget(holder.imgUpload) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            super.onResourceReady(resource, glideAnimation);
                                            holder.imgUpload.setImageBitmap(resource);
                                            base64ToUploads.add(MyFileUtils.encodeToBase64(resource));
                                        }
                                    });
                                }

                            }
                        });
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    };
    private static final String KEY_DATAS = "PROFILE_IMAGE_DATAS";
    private long mUserId;
    private AccountApi mApi;
    private OnDataChangeListener mOnDataChangeListener;

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
        mRecyclerViewUploadPhoto.setLayoutManager(new GridLayoutManager(getContext(), RecyclerUtils.calculateNoOfColumns(getContext(), 180)));
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
        mToolbar.setTitle(LUpload_photo);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    onClickDone();
                }
                return false;
            }
        });
    }

    private void onClickDone() {
        showLoading();
        RetrofitCallUtils.with(mApi.postUploadProfileImage(PostUploadProfileImageGson.with(mUserId).createNew(base64ToUploads))
                , mCallBackUpload)
                .enqueue(getContext());
    }

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.mOnDataChangeListener = listener;
    }


    public static interface OnDataChangeListener {
        void onDataChange();
    }
}
