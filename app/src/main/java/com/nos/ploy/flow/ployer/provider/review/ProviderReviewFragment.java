package com.nos.ploy.flow.ployer.provider.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.ployer.provider.leavereview.LeaveReviewFragment;
import com.nos.ploy.utils.LanguageFormatter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 13/1/2560.
 */

public class ProviderReviewFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.button_ployee_review_leave_a_review)
    Button mButtonLeaveAReview;
    @BindView(R.id.ratingbar_ployee_review_overall)
    RatingBar mRatingAll;
    @BindView(R.id.textview_ployee_review_overall_point)
    TextView mTextViewOverAllPoint;
    @BindView(R.id.textview_ployee_review_overall_title)
    TextView mTextViewOverAllTitle;
    @BindView(R.id.ratingbar_ployee_review_competence)
    RatingBar mRatingCompetence;
    @BindView(R.id.textview_ployee_review_competence_point)
    TextView mTextViewCompetencePoint;
    @BindView(R.id.ratingbar_ployee_review_communitation)
    RatingBar mRatingCommunication;
    @BindView(R.id.textview_ployee_review_communitation_point)
    TextView mTextViewCommunicationPoint;
    @BindView(R.id.ratingbar_ployee_review_politeness)
    RatingBar mRatingPolite;
    @BindView(R.id.textview_ployee_review_politeness_point)
    TextView mTextViewPolitePoint;
    @BindView(R.id.ratingbar_ployee_review_professionalism)
    RatingBar mRatingProfessionalism;
    @BindView(R.id.textview_ployee_review_professionalism_point)
    TextView mTextViewProfessionalism;
    @BindView(R.id.ratingbar_ployee_review_punctuality)
    RatingBar mRatingPunctuality;
    @BindView(R.id.textview_ployee_review_punctuality_point)
    TextView mTextViewPunctualityPoint;
    @BindView(R.id.swiperefreshlayout_ployee_review)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview_ployee_review_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubtitle;
    @BindString(R.string.Overall)
    String LOverall;
    @BindString(R.string.Review)
    String LReview;
    @BindString(R.string.Reviews)
    String LReviews;
    private long mUserId;
    private PloyerApi mApi;

    private ProviderReviewRecyclerAdapter mAdapter = new ProviderReviewRecyclerAdapter();
    private ReviewGson.Data mData;

    public static ProviderReviewFragment newInstance(long userId) {

        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        ProviderReviewFragment fragment = new ProviderReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0);
        }
        mApi = getRetrofit().create(PloyerApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_review, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mData) {
            refreshData();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initView();
        initToolbar();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mTextViewTitle.setText(LReview);
    }

    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mButtonLeaveAReview.setOnClickListener(this);
    }

    private RetrofitCallUtils.RetrofitCallback<ReviewGson> mCallbackRefresh = new RetrofitCallUtils.RetrofitCallback<ReviewGson>() {
        @Override
        public void onDataSuccess(ReviewGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(ResponseMessage failCause) {
            dismissRefreshing();
        }
    };

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getReviewByUserId(mUserId), mCallbackRefresh).enqueue(getContext());
    }

    private void bindData(ReviewGson.Data data) {
        if (null != data) {
            mData = data;
            if (null != data.getReviewAverage()) {
                ReviewGson.Data.ReviewAverage reviewAverage = data.getReviewAverage();

                mRatingAll.setRating(reviewAverage.getAll());
                mTextViewOverAllPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getAll()));

                mRatingCommunication.setRating(reviewAverage.getCommunication());
                mTextViewCommunicationPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getCommunication()));

                mRatingCompetence.setRating(reviewAverage.getCompetence());
                mTextViewCompetencePoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getCompetence()));

                mRatingPolite.setRating(reviewAverage.getPoliteness());
                mTextViewPolitePoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getPoliteness()));

                mRatingProfessionalism.setRating(reviewAverage.getProfessionalism());
                mTextViewProfessionalism.setText(LanguageFormatter.formatDecimal(reviewAverage.getProfessionalism()));

                mRatingPunctuality.setRating(reviewAverage.getPunctuality());
                mTextViewPunctualityPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getPunctuality()));
            }

            if (null != data.getReviewDataList()) {
                int size = data.getReviewDataList().size();
                mTextViewOverAllTitle.setText(LOverall + " (" + size + ") ");
                mTextViewSubtitle.setVisibility(View.VISIBLE);
                if (size == 0 || size == 1) {
                    mTextViewSubtitle.setText(data.getReviewDataList().size() + " " + LReview);
                } else {
                    mTextViewSubtitle.setText(data.getReviewDataList().size() + " " + LReviews);
                }


                mAdapter.replaceData(data.getReviewDataList());
            }

        }
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonLeaveAReview.getId()) {
            if(UserTokenManager.isLogin(v.getContext())){
                AccountInfoLoader.getAccountGson(v.getContext(), mUserId, new Action1<AccountGson.Data>() {
                    @Override
                    public void call(AccountGson.Data data) {
                        if (null != data && null != mData) {
                            showFragment(LeaveReviewFragment.newInstance(data,mData.getReviewAverage()));
                        }

                    }
                });

            }
        }
    }
}
