package com.nos.ploy.flow.ployer.provider.review;

import android.os.Bundle;
import android.os.Handler;
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
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.ployer.provider.leavereview.LeaveReviewFragment;
import com.nos.ploy.utils.LanguageFormatter;
import com.nos.ploy.utils.RatingBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.ratingbar_ployee_review_communication)
    RatingBar mRatingCommunication;
    @BindView(R.id.textview_ployee_review_communication_point)
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

    @BindView(R.id.textview_ployee_review_competence_title)
    TextView mTextViewCompetenceTitle;
    @BindView(R.id.textview_ployee_review_politeness_title)
    TextView mTextViewPolitenessTitle;
    @BindView(R.id.textview_ployee_review_communication_title)
    TextView mTextViewCommunicationTitle;
    @BindView(R.id.textview_ployee_review_professionalism_title)
    TextView mTextViewProfessionalismTitle;
    @BindView(R.id.textview_ployee_review_punctuality_title)
    TextView mTextViewPunctualityTitle;

    @BindString(R.string.Overall)
    String LOverall;
    @BindString(R.string.Review)
    String LReview;
    @BindString(R.string.Reviews)
    String LReviews;
    private long mUserIdToReview;
    private PloyerApi mApi;
    private static final String KEY_USER_SERVICE_DATA = "USER_SERVICE_DATA";
    private ProviderReviewRecyclerAdapter mAdapter = new ProviderReviewRecyclerAdapter();
    private ReviewGson.Data mData;
    private ProviderUserListGson.Data.UserService mUserServiceData;
    private LeaveReviewFragment.OnReviewFinishListener listener;

    public static ProviderReviewFragment newInstance(long userId, ProviderUserListGson.Data.UserService userData, LeaveReviewFragment.OnReviewFinishListener listener) {

        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        args.putParcelable(KEY_USER_SERVICE_DATA, userData);
        ProviderReviewFragment fragment = new ProviderReviewFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserIdToReview = getArguments().getLong(KEY_USER_ID, 0);
            mUserServiceData = getArguments().getParcelable(KEY_USER_SERVICE_DATA);
        }
        mApi = getRetrofit().create(PloyerApi.class);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUserServiceData.getReviewCount() == 0){
                    showFragment(LeaveReviewFragment.newInstance(mUserIdToReview, mUserServiceData, new LeaveReviewFragment.OnReviewFinishListener() {
                        @Override
                        public void onReviewFinish() {
                            getListener().onReviewFinish();
                            refreshData();
                        }
                    }));
                }
            }
        }, 1000);

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

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.reviewScreenHeader);
        mTextViewSubtitle.setText(data.profileScreenReviews);
        mTextViewCompetenceTitle.setText(data.reviewScreenCompetence);
        mTextViewCommunicationTitle.setText(data.reviewScreenCommunication);
        mTextViewPolitenessTitle.setText(data.reviewScreenPoliteness);
        mTextViewPunctualityTitle.setText(data.reviewScreenPunctuality);
        mTextViewProfessionalismTitle.setText(data.reviewScreenProfession);
        mButtonLeaveAReview.setText(data.reivewScreenLeaveReview);

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
        if (null != UserTokenManager.getToken(getContext())) {
            long reviewerUserId = UserTokenManager.getToken(getContext()).getUserId();
            if (mUserIdToReview == reviewerUserId) {
                mButtonLeaveAReview.setVisibility(View.GONE);
            }
        }

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
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getReviewByUserId(mUserIdToReview), mCallbackRefresh).enqueue(getContext());
    }

    private void bindData(ReviewGson.Data data) {
        if (null != data) {
            mData = data;
            if (null != data.getReviewAverage()) {
                ReviewGson.Data.ReviewAverage reviewAverage = data.getReviewAverage();

                mRatingAll.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getAll()));
                mTextViewOverAllPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getAll()));

                mRatingCommunication.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getCommunication()));
                mTextViewCommunicationPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getCommunication()));

                mRatingCompetence.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getCompetence()));
                mTextViewCompetencePoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getCompetence()));

                mRatingPolite.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getPoliteness()));
                mTextViewPolitePoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getPoliteness()));

                mRatingProfessionalism.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getProfessionalism()));
                mTextViewProfessionalism.setText(LanguageFormatter.formatDecimal(reviewAverage.getProfessionalism()));

                mRatingPunctuality.setRating(RatingBarUtils.getRatingbarRoundingNumber(reviewAverage.getPunctuality()));
                mTextViewPunctualityPoint.setText(LanguageFormatter.formatDecimal(reviewAverage.getPunctuality()));
            }

            if (null != data.getReviewDataList()) {
                int size = data.getReviewDataList().size();
                mTextViewOverAllTitle.setText(mLanguageData.reviewScreenOverall + " (" + size + " "+mLanguageData.reviewLabel+") ");
                mTextViewSubtitle.setVisibility(View.VISIBLE);
//                if (size == 0 || size == 1) {
//                    mTextViewSubtitle.setText(data.getReviewDataList().size() + " " + LReview);
//                } else {
//                    mTextViewSubtitle.setText(data.getReviewDataList().size() + " " + LReviews);
//                }

                mTextViewSubtitle.setText(mData.getReviewDataList().size() + " " + mLanguageData.profileScreenReviews);
                moveToFirstIfIsOwn(data.getReviewDataList());
                mAdapter.replaceData(data.getReviewDataList());
            }

        }
    }
    private void moveToFirstIfIsOwn(List<ReviewGson.Data.ReviewData> reviewDataList){
        UserTokenGson.Data token = UserTokenManager.getToken(getContext());
        if(token != null){

//            List<ReviewGson.Data.ReviewData> results = new ArrayList<>();
            List<ReviewGson.Data.ReviewData> tempDatas = new ArrayList<>(reviewDataList);
            for(ReviewGson.Data.ReviewData data : tempDatas){
                if(token.getUserId() == data.getUserReview().getUserId()){
                    reviewDataList.remove(data);
                    reviewDataList.add(0,data);
                }
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
            if (UserTokenManager.isLogin(v.getContext())) {
                showFragment(LeaveReviewFragment.newInstance(mUserIdToReview, mUserServiceData, new LeaveReviewFragment.OnReviewFinishListener() {
                    @Override
                    public void onReviewFinish() {
                        getListener().onReviewFinish();
                        refreshData();
                    }
                }));
            }
        }
    }

    public void setListener(LeaveReviewFragment.OnReviewFinishListener listener) {
        this.listener = listener;
    }

    public LeaveReviewFragment.OnReviewFinishListener getListener() {
        return listener;
    }
}
