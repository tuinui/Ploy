package com.nos.ploy.flow.ployer.provider;

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
import android.view.View;
import android.widget.ImageView;
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
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.flow.generic.maps.LocalizationMapsFragment;
import com.nos.ploy.flow.ployee.home.content.availability.contract.AvailabilityViewModel;
import com.nos.ploy.flow.ployee.home.content.availability.contract.NormalItemAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.contract.WeekAvailabilityVM;
import com.nos.ploy.flow.ployee.home.content.availability.view.AvailabilityRecyclerAdapter;
import com.nos.ploy.flow.ployee.profile.ImageSliderPagerAdapter;
import com.nos.ploy.flow.ployee.profile.TransportRecyclerAdapter;
import com.nos.ploy.flow.ployer.provider.review.ProviderReviewFragment;
import com.nos.ploy.utils.GoogleApiAvailabilityUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProviderProfileActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    public static final String KEY_PLOYEE_USER_SERVICE_DATA = "PLOYEE_USER_SERVICE_DATA";
    public static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private ProviderUserListGson.Data.UserService mUserServiceData;
    @BindView(R.id.viewpager_profile_image_slider)
    ViewPager mViewPagerSlider;
    @BindView(R.id.tablayout_profile_image_slider_indicator)
    TabLayout mTabLayoutSliderIndicator;
    @BindView(R.id.textview_member_profile_phone_button)
    TextView mButtonPhone;
    @BindView(R.id.textview_member_profile_email_button)
    TextView mButtonEmail;
    @BindView(R.id.imageview_member_profile_static_maps)
    ImageView mImageViewStaticMaps;
    @BindView(R.id.textview_member_profile_address)
    TextView mTextViewAddress;
    @BindView(R.id.textview_member_profile_addresss_distance)
    TextView mTextViewDistance;
    @BindView(R.id.ratingbar_member_profile_rate)
    RatingBar mRatingBar;
    @BindView(R.id.textview_member_profile_rate_title)
    TextView mTextViewRateTitle;
    @BindView(R.id.textview_member_profile_rate_count)
    TextView mTextViewRateCount;
    @BindView(R.id.textview_member_profile_about_me)
    TextView mTextViewAboutMe;
    @BindView(R.id.textview_member_profile_education)
    TextView mTextViewEducation;
    @BindView(R.id.textview_member_profile_languages)
    TextView mTextViewLanguages;
    @BindView(R.id.textview_member_profile_work)
    TextView mTextViewWork;
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
    private PloyerServicesGson.Data mServiceData;

    @OnClick(R.id.view_activity_member_profile_availability_block)
    public void onClickAvailabilityBlock(View view) {
    }


    private ImageSliderPagerAdapter mImageSliderAdapter;
    private PloyerApi mApi;
    private MemberProfileGson.Data mData;
    private ProviderServiceRecyclerAdapter mServiceAdapter = new ProviderServiceRecyclerAdapter();
    private AvailabilityRecyclerAdapter mAvailabilityAdapter = new AvailabilityRecyclerAdapter() {
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
                holder.tvTitle.setText(data.getTitle());
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
            bindData(data.getData());
        }


        @Override
        public void onDataFailure(ResponseMessage failCause) {
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
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        if (null != mUserServiceData) {
            mTextViewTitle.setText(mUserServiceData.getFullName());
        }
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
        RetrofitCallUtils.with(mApi.getProviderProfileGson(mUserServiceData.getUserId(), mServiceData.getId(), SharePreferenceUtils.getCurrentActiveAppLanguageCode(this)), mCallbackRefreshData).enqueue(this);
    }


    private void initData() {
        if (null != getIntent() && null != getIntent().getExtras()) {
            mUserServiceData = getIntent().getExtras().getParcelable(KEY_PLOYEE_USER_SERVICE_DATA);
            mServiceData = getIntent().getExtras().getParcelable(KEY_SERVICE_DATA);
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
        mData = data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != data.getUserProfile()) {
                    bindUserProfile(data.getUserProfile());
                }

                if (null != data.getAvailability()) {
                    bindAvailability(data.getAvailability());
                }

                if (null != data.getImages()) {
                    bindProfileImages(data.getImages());
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
            mTransportRecyclerAdapter.notifyDataSetChanged();
            mTextViewAboutMe.setText(data.getAboutMe());
            String languageSupports = "";
            if (data.getLanguage() != null && !data.getLanguage().isEmpty()) {
                int size = data.getLanguage().size();
                for (int i = 0; i < size; i++) {
                    PloyeeProfileGson.Data.Language language = data.getLanguage().get(i);
                    if (i == size - 1) {
                        languageSupports += language.getSpokenLanguageValue();
                    } else {
                        languageSupports += language.getSpokenLanguageValue() + " ,";
                    }

                }
            }
            mTextViewLanguages.setText(languageSupports);
            mTextViewWork.setText(data.getWork());
            if (null != data.getLocation()) {
                LatLng latLng = new LatLng(data.getLocation().getLat(), data.getLocation().getLng());
                mTextViewAddress.setText(MyLocationUtils.getCompleteAddressString(this, data.getLocation().getLat(), data.getLocation().getLng()));
                mTextViewDistance.setText(MyLocationUtils.getDistanceFromCurrentLocation(this, mGoogleApiClient, latLng));
                Glide.with(mImageViewStaticMaps.getContext()).load(MyLocationUtils.getStaticMapsUrl(latLng)).into(mImageViewStaticMaps);
            }

            mTextViewEducation.setText(data.getEducation());
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
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mData == null) {
            refreshData();
        }
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
            if (null != mUserServiceData) {
                IntentUtils.sendEmail(v.getContext(), mUserServiceData.getEmail());
            }
        } else if (id == mButtonPhone.getId()) {
            if (null != mUserServiceData) {
                IntentUtils.makeACall(v.getContext(), mUserServiceData.getPhone());
            }
        } else if (id == mImageViewStaticMaps.getId()) {
            openLocalizationMaps();
        } else if (id == mCardViewReview.getId()) {
            if(null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getUserId()){
                showFragment(ProviderReviewFragment.newInstance(mData.getUserProfile().getUserId()));
            }
        }
    }

    private void openLocalizationMaps() {
        if (null != mData && null != mData.getUserProfile() && null != mData.getUserProfile().getLocation()) {
            LatLng latLng = new LatLng(mData.getUserProfile().getLocation().getLat(), mData.getUserProfile().getLocation().getLng());
            showFragment(LocalizationMapsFragment.newInstance(latLng, false, new LocalizationMapsFragment.OnChooseLocationFinishListener() {
                @Override
                public void onFinishChoosingLocation(LatLng latLng) {

                }
            }));
        }
    }
}