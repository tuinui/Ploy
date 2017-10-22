package com.nos.ploy.flow.ployer.provider.leavereview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.api.ployer.model.SaveReviewResponseGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.utils.URLHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 13/1/2560.
 */

public class LeaveReviewFragment extends BaseFragment implements View.OnClickListener, SimpleRatingBar.OnRatingBarChangeListener {


    @BindView(R.id.imageview_ployee_leave_review_profile_image)
    ImageView mImageViewProfileImage;
    @BindView(R.id.textview_ployee_leave_review_name)
    TextView mTextViewName;
    @BindView(R.id.ratingbar_ployee_leave_review_competence)
    SimpleRatingBar mRatingBarCompetence;
    @BindView(R.id.ratingbar_ployee_leave_review_communication)
    SimpleRatingBar mRatingBarCommunication;
    @BindView(R.id.ratingbar_ployee_leave_review_politeness)
    SimpleRatingBar mRatingBarPoliteness;
    @BindView(R.id.ratingbar_ployee_leave_review_professionalism)
    SimpleRatingBar mRatingBarProfessional;
    @BindView(R.id.ratingbar_ployee_leave_review_punctuality)
    SimpleRatingBar mRatingBarPunctuality;
    @BindView(R.id.edittext_ployee_leave_a_review)
    MaterialEditText mEditTextReview;
    @BindView(R.id.textview_ployee_leave_review_competence_title)
    TextView mTextViewCompetence;
    @BindView(R.id.textview_ployee_leave_review_politeness_title)
    TextView mTextViewPoliteness;
    @BindView(R.id.textview_ployee_leave_review_communication_title)
    TextView mTextViewCommunication;
    @BindView(R.id.textview_ployee_leave_review_professionalism_title)
    TextView mTextViewProfessionalism;
    @BindView(R.id.textview_ployee_leave_review_punctuality_title)
    TextView mTextViewPunctuality;

    @BindView(R.id.toolbar_main)
    Toolbar mToolbarMain;
    @BindView(R.id.button_ployee_leave_a_review_post)
    Button mButtonPost;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;

    private static final String KEY_USER_SERVICE_DATA = "REVIEWER_GSON";
    private static final String KEY_PROVIDER_GSON = "PROVIDER_GSON";
    private static final String KEY_USER_ID_TO_REVIEW = "USER_ID_TO_REVIEW";
    private PloyerApi mApi;
    private Long mReviewerUserId;
    private long mUserIdToReview;
    private ProviderUserListGson.Data.UserService mUserServiceData;
    private OnReviewFinishListener listener;
    private float mRatingCompetence;
    private float mRatingCommunication;
    private float mRatingPunctuality;
    private float mRatingProfessional;
    private float mRatingPoliteness;

    public static LeaveReviewFragment newInstance(long userIdToReview, ProviderUserListGson.Data.UserService userData, OnReviewFinishListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_USER_SERVICE_DATA, userData);
        args.putLong(KEY_USER_ID_TO_REVIEW, userIdToReview);
        LeaveReviewFragment fragment = new LeaveReviewFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserServiceData = getArguments().getParcelable(KEY_USER_SERVICE_DATA);
            mUserIdToReview = getArguments().getLong(KEY_USER_ID_TO_REVIEW);
            if (null != UserTokenManager.getToken(getContext())) {
                mReviewerUserId = UserTokenManager.getToken(getContext()).getUserId();
            }

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
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTextViewTitle.setText(data.reviewScreenHeader);
        mTextViewCompetence.setText(data.reviewScreenCompetence);
        mTextViewCommunication.setText(data.reviewScreenCommunication);
        mTextViewPoliteness.setText(data.reviewScreenPoliteness);
        mTextViewPunctuality.setText(data.reviewScreenPunctuality);
        mTextViewProfessionalism.setText(data.reviewScreenProfession);
        mEditTextReview.setHint(data.reviewScreenHintMsg);
        mEditTextReview.setFloatingLabelText(data.reviewScreenHintMsg);
        mButtonPost.setText(data.reivewScreenPost);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        bindData();
    }

    private void bindData() {
        if (mReviewerUserId != null) {
            mTextViewName.setText(mUserServiceData.getFullName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mImageViewProfileImage.getContext()).load(URLHelper.changURLEndpoint(mUserServiceData.getImagePath())).error(R.drawable.ic_circle_profile_120dp).into(mImageViewProfileImage);
                }
            });

        }

    }

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Review);
        enableBackButton(mToolbarMain);
    }

    private void initView() {
        mButtonPost.setOnClickListener(this);
        mRatingBarCompetence.setOnRatingBarChangeListener(this);
        mRatingBarCommunication.setOnRatingBarChangeListener(this);
        mRatingBarPoliteness.setOnRatingBarChangeListener(this);
        mRatingBarProfessional.setOnRatingBarChangeListener(this);
        mRatingBarPunctuality.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonPost.getId()) {
            requestPostReview();
        }
    }

    //    long userId, long reviewerUserId, long competence, long punctuality, long politeness, long communication, long professionalism, String reviewText
    private void requestPostReview() {
        if (null != mReviewerUserId) {
            showLoading();

            RetrofitCallUtils.with(mApi.postSaveReview(gatheredCurrentData()), new RetrofitCallUtils.RetrofitCallback<SaveReviewResponseGson>() {
                @Override
                public void onDataSuccess(SaveReviewResponseGson data) {
                    dismissLoading();
                    getListener().onReviewFinish();
                    dismiss();
                }

                @Override
                public void onDataFailure(String failCause) {
                    getListener().onReviewFinish();
                    dismissLoading();
                }
            }).enqueue(getContext());
        }

    }

    private ReviewGson.Data.ReviewData.Review gatheredCurrentData() {
        return new ReviewGson.Data.ReviewData.Review(mUserIdToReview, mReviewerUserId, (long) mRatingCompetence, (long) mRatingPunctuality, (long) mRatingPoliteness, (long) mRatingCommunication, (long) mRatingProfessional, extractString(mEditTextReview));
    }

    public void setListener(OnReviewFinishListener listener) {
        this.listener = listener;
    }

    public OnReviewFinishListener getListener() {
        return listener;
    }

    @Override
    public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
        int id = simpleRatingBar.getId();
        if(id == mRatingBarCompetence.getId()){
            mRatingCompetence = rating;
        }else if(id == mRatingBarPoliteness.getId()){
            mRatingPoliteness = rating;
        }else if(id == mRatingBarProfessional.getId()){
            mRatingProfessional = rating;
        }else if(id == mRatingBarPunctuality.getId()){
            mRatingPunctuality = rating;
        }else if(id == mRatingBarCommunication.getId()){
            mRatingCommunication = rating;
        }
    }

    public static interface OnReviewFinishListener {
        public void onReviewFinish();
    }
}
