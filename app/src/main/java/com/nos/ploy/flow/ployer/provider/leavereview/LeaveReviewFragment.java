package com.nos.ploy.flow.ployer.provider.leavereview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 13/1/2560.
 */

public class LeaveReviewFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.imageview_ployee_leave_review_profile_image)
    ImageView mImageViewProfileImage;
    @BindView(R.id.textview_ployee_leave_review_name)
    TextView mTextViewName;
    @BindView(R.id.ratingbar_ployee_leave_review_competence)
    RatingBar mRatingBarCompetence;
    @BindView(R.id.ratingbar_ployee_leave_review_communication)
    RatingBar mRatingCommunication;
    @BindView(R.id.ratingbar_ployee_leave_review_politeness)
    RatingBar mRatingBarPoliteness;
    @BindView(R.id.ratingbar_ployee_leave_review_professionalism)
    RatingBar mRatingBarProfessional;
    @BindView(R.id.ratingbar_ployee_leave_review_punctuality)
    RatingBar mRatingBarPunctuality;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbarMain;
    @BindView(R.id.button_ployee_leave_a_review_post)
    Button mButtonPost;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;

    private static final String KEY_REVIEWER_GSON = "REVIEWER_GSON";
    private static final String KEY_PROVIDER_GSON = "PROVIDER_GSON";
    private AccountGson.Data mReviewerData;
    private ReviewGson.Data.ReviewAverage mProviderData;
    private PloyerApi mApi;

    public static LeaveReviewFragment newInstance(AccountGson.Data reviewerData, ReviewGson.Data.ReviewAverage providerData) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_REVIEWER_GSON, reviewerData);
        args.putParcelable(KEY_PROVIDER_GSON, providerData);
        LeaveReviewFragment fragment = new LeaveReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mReviewerData = getArguments().getParcelable(KEY_REVIEWER_GSON);
            mProviderData = getArguments().getParcelable(KEY_PROVIDER_GSON);
        }
        mApi = getRetrofit().create(PloyerApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_leave_a_review, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        bindData();
    }

    private void bindData() {
        if (mReviewerData != null) {
//            mTextViewName.setText(mProviderData.getFullName());
        }

        AccountInfoLoader.getProfileImage(getContext(), mReviewerData.getUserId(), new Action1<List<ProfileImageGson.Data>>() {
            @Override
            public void call(List<ProfileImageGson.Data> datas) {
                if (null != datas && !datas.isEmpty()) {
                    Glide.with(mImageViewProfileImage.getContext()).load(datas.get(0).getImagePath()).error(R.drawable.ic_circle_profile_120dp).into(mImageViewProfileImage);
                }
            }
        });
    }

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Review);
        enableBackButton(mToolbarMain);
    }

    private void initView() {
        mButtonPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonPost.getId()) {
            requestPostReview();
        }
    }

    private void requestPostReview() {
        if (null != mReviewerData) {
            showLoading();
//            mApi.postSaveReview(new ReviewGson.Data.ReviewData.Review(mU))
        }

    }
}
