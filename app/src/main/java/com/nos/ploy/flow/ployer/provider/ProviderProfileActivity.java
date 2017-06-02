package com.nos.ploy.flow.ployer.provider;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.MemberProfileGson;
import com.nos.ploy.api.account.model.PloyeeProfileGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.account.model.TransportGsonVm;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.ployer.model.ReviewGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.cache.UserTokenManager;
import com.nos.ploy.flow.generic.maps.LocalizationMapsFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.view.AvailabilityRecyclerAdapter;
import com.nos.ploy.flow.ployee.profile.ImageSliderPagerAdapter;
import com.nos.ploy.flow.ployee.profile.TransportRecyclerAdapter;
import com.nos.ploy.flow.ployer.provider.leavereview.LeaveReviewFragment;
import com.nos.ploy.flow.ployer.provider.review.ProviderReviewFragment;
import com.nos.ploy.utils.GoogleApiAvailabilityUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RatingBarUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class ProviderProfileActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    public static final String KEY_PLOYEE_USER_SERVICE_DATA = "PLOYEE_USER_SERVICE_DATA";
    public static final String KEY_PARENT_DATA = "PARENT_DATA";
    public static final String KEY_IS_PREVIEW = "IS_PREVIEW";
    private ProviderUserListGson.Data.UserService mUserServiceData;
    private PloyerServicesGson.Data mParentData = null;

    @BindView(R.id.viewZoneAbout)
    View viewZoneAbout;
    @BindView(R.id.viewZoneLanguages)
    View viewZoneLanguages;
    @BindView(R.id.viewZoneEdu)
    View viewZoneEdu;
    @BindView(R.id.viewZoneWork)
    View viewZoneWork;
    @BindView(R.id.viewZoneInterests)
    View viewZoneInterests;
    @BindView(R.id.viewZoneTransport)
    View viewZoneTransport;

    @BindView(R.id.viewpager_profile_image_slider)
    ViewPager mViewPagerSlider;
    @BindView(R.id.tablayout_profile_image_slider_indicator)
    TabLayout mTabLayoutSliderIndicator;

    @BindView(R.id.textview_member_profile_phone_button)
    TextView mButtonPhone;

    @BindView(R.id.textview_member_profile_email_button)
    TextView mButtonEmail;

    @BindView(R.id.view_member_profile_phone_button)
    View mViewPhone;

    @BindView(R.id.view_member_profile_email_button)
    View mViewEmail;


    @BindView(R.id.linearlayout_member_profile_phone_email_container)
    LinearLayout mLinearLayoutPhoneEmailContainer;
    @BindView(R.id.imageview_member_profile_static_maps)
    ImageView mImageViewStaticMaps;
    @BindView(R.id.textview_member_profile_address)
    TextView mTextViewAddress;
    @BindView(R.id.textview_member_profile_addresss_distance)
    TextView mTextViewDistance;
    @BindView(R.id.textview_member_profile_about_me)
    TextView mTextViewAboutMe;
    @BindView(R.id.textview_member_profile_about_me_header)
    TextView mTextViewAboutMeHeader;
    @BindView(R.id.textview_member_profile_education)
    TextView mTextViewEducation;
    @BindView(R.id.textview_member_profile_education_header)
    TextView mTextViewEducationHeader;
    @BindView(R.id.textview_member_profile_languages)
    TextView mTextViewLanguages;
    @BindView(R.id.textview_member_profile_languages_header)
    TextView mTextViewLanguagesHeader;
    @BindView(R.id.textview_member_profile_work)
    TextView mTextViewWork;
    @BindView(R.id.textview_member_profile_work_header)
    TextView mTextViewWorkHeader;
    @BindView(R.id.textview_member_profile_interests)
    TextView mTextViewInterests;
    @BindView(R.id.textview_member_profile_interests_header)
    TextView mTextViewInterestsHeader;
    @BindView(R.id.textview_member_profile_transportation_header)
    TextView mTextViewTransportationheader;
    @BindView(R.id.recyclerview_member_profile_transportation)
    RecyclerView mRecyclerViewTransportation;
    @BindView(R.id.recyclerview_member_profile_services)
    RecyclerView mRecyclerViewServices;
    @BindView(R.id.recyclerview_member_profile_availability)
    RecyclerView mRecyclerViewAvailability;
    @BindView(R.id.swiperefresh_member_profile)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.cardview_member_profile_review)
    CardView mCardViewReview;
    @BindView(R.id.ratingbar_member_profile_rate)
    RatingBar mRatingBarRate;
    @BindView(R.id.textview_member_profile_rate_title)
    TextView mTextViewRatingPoint;
    @BindView(R.id.textview_member_profile_rate_count)
    TextView mTextViewRatingCount;
    private boolean mIsPreviewMode;
    private boolean isFirstTimeLoaded;
    private boolean isFirstTimeExpandedIfMatchServiceData = false;
//    private PloyeeProfileGson.Data mPreviewData;


    @OnClick(R.id.view_activity_member_profile_availability_block)
    public void onClickAvailabilityBlock(View view) {
    }


    private ImageSliderPagerAdapter mImageSliderAdapter;
    private PloyerApi mApi;
    private MemberProfileGson.Data mData;
    private ProviderServiceRecyclerAdapter mServiceAdapter = new ProviderServiceRecyclerAdapter();
    private AvailabilityRecyclerAdapter mAvailabilityAdapter = new AvailabilityRecyclerAdapter(null) {
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
        }
    };

    private TransportRecyclerAdapter mTransportRecyclerAdapter = new TransportRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mTransportDatas, position)) {
                TransportGsonVm data = mTransportDatas.get(position);
                holder.imgTransport.setImageResource(data.getDrawable());
                holder.tvTitle.setText(toTransportName(data.getId()));
                holder.imgTransport.setActivated(true);
            }

        }

        @Override
        public int getItemCount() {
            return RecyclerUtils.getSize(mTransportDatas);
        }
    };


    private RetrofitCallUtils.RetrofitCallback<MemberProfileGson> mCallbackRefreshData = new RetrofitCallUtils.RetrofitCallback<MemberProfileGson>() {
        @Override
        public void onDataSuccess(MemberProfileGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                mData = data.getData();
                bindData(mData);
            }
        }


        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };
    private GoogleApiClient mGoogleApiClient;
    private List<TransportGsonVm> mTransportDatas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        ButterKnife.bind(this);

        mApi = getRetrofit().create(PloyerApi.class);
        if (GoogleApiAvailabilityUtils.checkPlayServices(this)) {
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        }

        initData();
        initToolbar();
        initView();
        initRecyclerView();
        initSlider();
        bindUserServiceData(mUserServiceData);
    }

    private void bindUserServiceData(ProviderUserListGson.Data.UserService data) {
        mData = new MemberProfileGson.Data(data);
        bindData(mData);
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        mTransportRecyclerAdapter.setLanguage(data);
        mServiceAdapter.setLanguage(data);
        mAvailabilityAdapter.setLanguage(data);
        mButtonEmail.setText(data.profileScreenEmail);
        mTextViewAboutMeHeader.setText(data.profileScreenAboutMe);
        mTextViewLanguagesHeader.setText(data.profileScreenLanguage);
        mTextViewEducationHeader.setText(data.profileScreenEducation);
        mTextViewWorkHeader.setText(data.profileScreenWork);
        mTextViewInterestsHeader.setText(data.profileScreenInterest);
        mTextViewTransportationheader.setText(data.profileScreenTransport);
        mButtonPhone.setText(data.profileScreenPhone);
        mButtonEmail.setText(data.profileScreenEmail);
        mTextViewRatingCount.setText(mUserServiceData.getReviewCount() + " " + mLanguageData.profileScreenReviews);
        if (null != mUserServiceData && mIsPreviewMode) {
            mTextViewTitle.setText(mUserServiceData.getFullName() + " (" + mLanguageData.profileScreenPreview + ")");
        } else {
            mTextViewTitle.setText(mUserServiceData.getFullName());
        }


    }

    private void requestReviewData() {
        if (null == mUserServiceData) {
            return;
        }
        RetrofitCallUtils.with(mApi.getReviewByUserId(mUserServiceData.getUserId()), new RetrofitCallUtils.RetrofitCallback<ReviewGson>() {
            @Override
            public void onDataSuccess(ReviewGson data) {
                if (null != data && null != data.getData()) {
                    if (null != data.getData().getReviewDataList()) {
                        mUserServiceData.setReviewCount((long) data.getData().getReviewDataList().size());
                    }
                    if (null != data.getData().getReviewAverage() && null != data.getData().getReviewAverage().getAll()) {
                        mUserServiceData.setReviewPoint(data.getData().getReviewAverage().getAll());
                    }
                    mTextViewRatingCount.setText(mUserServiceData.getReviewCount() + " " + mLanguageData.profileScreenReviews);
                    initToolbar();
                }

            }

            @Override
            public void onDataFailure(String failCause) {

            }
        }).enqueue(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initRecyclerView() {
        mRecyclerViewAvailability.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }
        });
        mRecyclerViewAvailability.setAdapter(mAvailabilityAdapter);

        mRecyclerViewTransportation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }
        });
        mRecyclerViewTransportation.setAdapter(mTransportRecyclerAdapter);

        mRecyclerViewServices.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }
        });
        mRecyclerViewServices.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerViewServices.setAdapter(mServiceAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mButtonEmail.setOnClickListener(this);
        mButtonPhone.setOnClickListener(this);
        mImageViewStaticMaps.setOnClickListener(this);
        mCardViewReview.setOnClickListener(this);


    }

    //userId=1&serviceId=1&lgCode=en
    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getProviderProfileGson(mUserServiceData.getUserId(), SharePreferenceUtils.getCurrentActiveAppLanguageCode(this)), mCallbackRefreshData).enqueueDontToast(this);
        requestReviewData();
    }


    private void initData() {
        if (null != getIntent() && null != getIntent().getExtras()) {
            mUserServiceData = getIntent().getExtras().getParcelable(KEY_PLOYEE_USER_SERVICE_DATA);
            mParentData = getIntent().getExtras().getParcelable(KEY_PARENT_DATA);
            mIsPreviewMode = getIntent().getExtras().getBoolean(KEY_IS_PREVIEW, false);
        }
    }


    private void initSlider() {
        mImageSliderAdapter = new ImageSliderPagerAdapter(this);
        mViewPagerSlider.setAdapter(mImageSliderAdapter);
        mViewPagerSlider.setCurrentItem(0);
        mTabLayoutSliderIndicator.setupWithViewPager(mViewPagerSlider);
    }


    private void bindData(final MemberProfileGson.Data data) {
        if (null == data) {
            return;
        }

        runOnUiThread(new Action1<Context>() {
            @Override
            public void call(Context context) {

                if (null != data.getReviewAverage()) {
                    mRatingBarRate.setRating(RatingBarUtils.getRatingbarRoundingNumber(data.getReviewAverage().getAll()));
                    mTextViewRatingPoint.setText(data.getReviewAverage().getAll() + "/5");
                }

                if (null != data.getUserProfile()) {
                    bindUserProfile(data.getUserProfile());
                }

                if (null != data.getAvailability()) {
                    bindAvailability(data.getAvailability());
                }

                if (RecyclerUtils.getSize(data.getImages()) > 0) {
                    bindProfileImages(data.getImages());
                } else if (null != data.getUserProfile()) {
                    AccountInfoLoader.getProfileImage(ProviderProfileActivity.this, data.getUserProfile().getUserId(), true, new Action1<List<ProfileImageGson.Data>>() {
                        @Override
                        public void call(List<ProfileImageGson.Data> datas) {
                            data.setImages(datas);
                            bindProfileImages(data.getImages());
                        }
                    });
                }

                if (null != data.getServiceDetails()) {
                    bindServices(data.getServiceDetails());
                }
            }
        });
    }

    private void bindUserProfile(PloyeeProfileGson.Data data) {
        if (null != data) {
            mTransportDatas.clear();
            mTransportDatas.addAll(toTransportVm(data.getTransport()));
//            if (id == mButtonEmail.getId()) {
//                if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getEmail()) {
//                    IntentUtils.sendEmail(v.getContext(), mData.getUserProfile().getEmail());
//                }
//            } else if (id == mButtonPhone.getId()) {
//                if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getPhone()) {
//                    IntentUtils.makeACall(v.getContext(), mData.getUserProfile().getPhone());
//                }
            boolean shouldShowEmailButton = !TextUtils.isEmpty(data.getEmail()) && data.isContactEmail();
            boolean shouldShowPhoneButton = !TextUtils.isEmpty(data.getPhone()) && data.isContactPhone();
            if (!shouldShowEmailButton && !shouldShowPhoneButton) {
                mLinearLayoutPhoneEmailContainer.setVisibility(View.GONE);
            } else {
                mLinearLayoutPhoneEmailContainer.setVisibility(View.VISIBLE);
                if (shouldShowEmailButton) {
                    mButtonEmail.setVisibility(View.VISIBLE);
                    mViewEmail.setVisibility(View.VISIBLE);
                } else {
                    mButtonEmail.setVisibility(View.GONE);
                    mViewEmail.setVisibility(View.GONE);
                }

                if (shouldShowPhoneButton) {
                    mButtonPhone.setVisibility(View.VISIBLE);
                    mViewPhone.setVisibility(View.VISIBLE);
                } else {
                    mButtonPhone.setVisibility(View.GONE);
                    mViewPhone.setVisibility(View.GONE);
                }

            }


            mTextViewInterests.setText(data.getInterest());
            mTransportRecyclerAdapter.notifyDataSetChanged();
            mTextViewAboutMe.setText(data.getAboutMe());
            String languageSupports = "";
            if (data.getLanguage() != null && !data.getLanguage().isEmpty()) {
                int size = data.getLanguage().size();
                for (int i = 0; i < size; i++) {
                    PloyeeProfileGson.Data.Language language = data.getLanguage().get(i);

                    if (!TextUtils.isEmpty(languageSupports)) {
                        languageSupports += ", ";
                    }

                    languageSupports += language.getSpokenLanguageValue();

                }
            }
            mTextViewLanguages.setText(languageSupports);
            mTextViewWork.setText(data.getWork());
            if (null != data.getLocation()) {
                final LatLng latLng = new LatLng(data.getLocation().getLat(), data.getLocation().getLng());
                mTextViewAddress.setText(MyLocationUtils.getCompleteAddressString(this, data.getLocation().getLat(), data.getLocation().getLng()));
                if (MyLocationUtils.locationProviderEnabled(this)) {
                    mTextViewDistance.setText(MyLocationUtils.getDistanceFromCurrentLocation(this, mGoogleApiClient, latLng));
                }
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {
                        Glide.with(context).load(MyLocationUtils.getStaticMapsUrl(latLng)).into(mImageViewStaticMaps);
                    }

                });

            } else {
                mTextViewDistance.setText("");
                mTextViewAddress.setText(mLanguageData.profileScreenLocation);
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {
                        Glide.with(context).load(MyLocationUtils.getStaticMapsUrl(new LatLng(0, 0))).into(mImageViewStaticMaps);
                    }

                });

            }

            mTextViewEducation.setText(data.getEducation());


            if (TextUtils.isEmpty(data.getAboutMe())) {
                viewZoneAbout.setVisibility(View.GONE);
            } else {
                viewZoneAbout.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(languageSupports)) {
                viewZoneLanguages.setVisibility(View.GONE);
            } else {
                viewZoneLanguages.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(data.getEducation())) {
                viewZoneEdu.setVisibility(View.GONE);
            } else {
                viewZoneEdu.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(data.getWork())) {
                viewZoneWork.setVisibility(View.GONE);
            } else {
                viewZoneWork.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(data.getInterest())) {
                viewZoneInterests.setVisibility(View.GONE);
            } else {
                viewZoneInterests.setVisibility(View.VISIBLE);
            }

            if (mTransportDatas.size() == 0) {
                viewZoneTransport.setVisibility(View.GONE);
            } else {
                viewZoneTransport.setVisibility(View.VISIBLE);
            }

        }
    }


    private void bindAvailability(PloyeeAvailiabilityGson.Data data) {
        if (null != data) {
            mAvailabilityAdapter.replaceData(toAvailabilityVm(data.getAvailabilityItems()));

        }
    }


    private void bindProfileImages(List<ProfileImageGson.Data> datas) {
        mImageSliderAdapter.replaceData(datas);
    }

    private void bindServices(List<PloyerServiceDetailGson.Data> datas) {
        mServiceAdapter.replaceData(datas);
        if(!isFirstTimeExpandedIfMatchServiceData){
            isFirstTimeExpandedIfMatchServiceData = true;
            mServiceAdapter.expandWithData(mParentData);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!isFirstTimeLoaded) {
            isFirstTimeLoaded = true;
            refreshData();
        }

        if (mUserServiceData == null || mUserServiceData.getReviewPoint() == 0) {
            requestReviewData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFirstTimeLoaded = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private List<AvailabilityViewModel> toAvailabilityVm(List<PloyeeAvailiabilityGson.Data.AvailabilityItem> datas) {
        List<AvailabilityViewModel> vms = new ArrayList<>();
        vms.add(WeekAvailabilityVM.create());
        for (PloyeeAvailiabilityGson.Data.AvailabilityItem item : datas) {
            vms.add(new NormalItemAvailabilityVM(item));
        }
        return vms;
    }

    private List<TransportGsonVm> toTransportVm(List<PloyeeProfileGson.Data.Transport> datas) {
        List<TransportGsonVm> vms = new ArrayList<>();
        for (PloyeeProfileGson.Data.Transport data : datas) {
            vms.add(new TransportGsonVm(data.getTransportId(), data.getTransportName(), toTransportIcon(data.getTransportId())));
        }
        return vms;
    }

    private
    @DrawableRes
    int toTransportIcon(long transportId) {
        if (transportId == 1) {
            return R.drawable.selector_drawable_walk_blue_gray;
        } else if (transportId == 2) {
            return R.drawable.selector_drawable_bike_blue_gray;
        } else if (transportId == 3) {
            return R.drawable.selector_drawable_car_blue_gray;
        } else if (transportId == 4) {
            return R.drawable.selector_drawable_motobike_blue_gray;
        } else if (transportId == 5) {
            return R.drawable.selector_drawable_truck_blue_gray;
        } else if (transportId == 6) {
            return R.drawable.selector_drawable_bus_blue_gray;
        } else {
            return R.drawable.ic_close_white_24dp;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonEmail.getId()) {
            if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getEmail()) {
                IntentUtils.sendEmail(v.getContext(), mData.getUserProfile().getEmail());
            }
        } else if (id == mButtonPhone.getId()) {
            if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getPhone()) {
                IntentUtils.makeACall(v.getContext(), mData.getUserProfile().getPhone());
            }
        } else if (id == mImageViewStaticMaps.getId()) {
            openLocalizationMaps();
        } else if (id == mCardViewReview.getId()) {
            if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getUserId()) {
                if (UserTokenManager.isLogin(v.getContext())) {
                    if (mUserServiceData.getReviewCount() > 0) {
                        showFragment(ProviderReviewFragment.newInstance(mData.getUserProfile().getUserId(), mUserServiceData,mParentData, new LeaveReviewFragment.OnReviewFinishListener() {
                            @Override
                            public void onReviewFinish() {
                                refreshData();
                            }
                        }));

                    } else {
                        showFragment(LeaveReviewFragment.newInstance(mData.getUserProfile().getUserId(), mUserServiceData, new LeaveReviewFragment.OnReviewFinishListener() {
                            @Override
                            public void onReviewFinish() {
                                refreshData();
                            }
                        }));
                    }
                }
            }
        }
    }


    private void openLocalizationMaps() {
        if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getLocation()) {
            LatLng latLng = new LatLng(mData.getUserProfile().getLocation().getLat(), mData.getUserProfile().getLocation().getLng());
            showFragment(LocalizationMapsFragment.newInstance(latLng, false,false, new LocalizationMapsFragment.OnChooseLocationFinishListener() {
                @Override
                public void onFinishChoosingLocation(LatLng latLng) {

                }
            }));
        }
    }
}
