//package com.nos.ploy.flow.ployee.profile;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.nos.ploy.R;
//import com.nos.ploy.api.account.AccountApi;
//import com.nos.ploy.api.account.model.PostUpdateProfileGson;
//import com.nos.ploy.api.account.model.PloyeeProfileGson;
//import com.nos.ploy.api.account.model.ProfileImageGson;
//import com.nos.ploy.api.account.model.TransportGson;
//import com.nos.ploy.api.account.model.TransportGsonVm;
//import com.nos.ploy.api.base.RetrofitCallUtils;
//import com.nos.ploy.api.base.response.ResponseMessage;
//import com.nos.ploy.api.masterdata.MasterApi;
//import com.nos.ploy.api.utils.loader.AccountInfoLoader;
//import com.nos.ploy.base.BaseActivity;
//import com.nos.ploy.flow.ployee.profile.language.SpokenLanguageChooserFragment;
//import com.nos.ploy.flow.ployee.profile.upload.UploadPhotoFragment;
//import com.nos.ploy.utils.FragmentTransactionUtils;
//import com.nos.ploy.utils.GoogleApiAvailabilityUtils;
//import com.nos.ploy.utils.MyLocationUtils;
//import com.nos.ploy.utils.RecyclerUtils;
//import com.rengwuxian.materialedittext.MaterialEditText;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindDrawable;
//import butterknife.BindString;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import rx.functions.Action1;
//
///**
// * Created by Saran on 15/12/2559.
// */
//
//public class PloyeeProfileActivity_Deprecated extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//    public LatLng mCurrentLatLng;
//    @BindView(R.id.button_ployee_profile_show_email)
//    Button mButtonEmail;
//    @BindView(R.id.button_ployee_profile_show_phone_no)
//    Button mButtonPhone;
//    @BindView(R.id.textview_ployee_profile_address)
//    TextView mTextViewAddress;
//    @BindView(R.id.edittext_ployee_profile_about_me)
//    MaterialEditText mEditTextAboutMe;
//    @BindView(R.id.edittext_ployee_profile_education)
//    MaterialEditText mEditTextEducation;
//    @BindView(R.id.edittext_ployee_profile_work)
//    MaterialEditText mEditTextProfileWork;
//    @BindView(R.id.edittext_ployee_profile_interest)
//    MaterialEditText mEditTextInterest;
//    @BindView(R.id.textview_ployee_profile_language_support)
//    TextView mTextViewLanguageSupport;
//    @BindView(R.id.recyclerview_ployee_profile_tranportation)
//    RecyclerView mRecyclerViewTransport;
//    @BindView(R.id.toolbar_main)
//    Toolbar mToolbar;
//    @BindView(R.id.textview_main_appbar_title)
//    TextView mTextViewTitle;
//    @BindString(R.string.Profile)
//    String LProfile;
//    @BindView(R.id.scrollview_ployee_profile)
//    NestedScrollView mScrollView;
//    @BindView(R.id.fab_ployee_profile_image)
//    FloatingActionButton mFabProfileImage;
//    @BindView(R.id.viewpager_profile_image_slider)
//    ViewPager mViewPagerSlider;
//    @BindView(R.id.tablayout_profile_image_slider_indicator)
//    TabLayout mTabLayoutSliderIndicator;
//    @BindView(R.id.swiperefreshlayout_ployee_profile)
//    SwipeRefreshLayout mSwipeRefreshLayout;
//    @BindView(R.id.imageview_ployee_profile_checkin)
//    ImageView mImageButtonCheckin;
//    @BindDrawable(R.drawable.nonselecteditem_dot)
//    Drawable mDrawableNonSelecteddot;
//    @BindDrawable(R.drawable.selecteditem_dot)
//    Drawable mDrawableSelectedDot;
//    private GoogleMap mGoogleMap;
//    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
//    private AccountApi mAccountApi;
//    private PostUpdateProfileGson mData;
//    private MasterApi mMasterApi;
//    private boolean shouldRequestCallSave = false;
//    private ImageSliderPagerAdapter mAdapter;
//    private GoogleApiClient mGoogleApiClient;
//    private List<TransportGsonVm> mAllDataTransports = new ArrayList<>();
//    private TransportRecyclerAdapter mTransportRecyclerAdapter = new TransportRecyclerAdapter() {
//        @Override
//        public void onBindViewHolder(final TransportRecyclerAdapter.ViewHolder holder, int position) {
//            if (RecyclerUtils.isAvailableData(mAllDataTransports, position)) {
//                TransportGsonVm data = mAllDataTransports.get(position);
////                Glide.with(holder.imgTransport.getContext()).load(data.getDrawable()).into(holder.imgTransport);
//                holder.imgTransport.setImageResource(data.getDrawable());
//                holder.tvTitle.setText(data.getTitle());
//
//                holder.imgTransport.setActivated(isTransportActivated(data.getId()));
//                holder.imgTransport.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (RecyclerUtils.isAvailableData(mAllDataTransports, holder.getAdapterPosition())) {
//                            TransportGsonVm data = mAllDataTransports.get(holder.getAdapterPosition());
//                            toggleEnableTransport(data.getId(), holder.getAdapterPosition());
//                        }
//                    }
//                });
//            }
//        }
//
//
//        private void toggleEnableTransport(long transportId, int position) {
//            if (isTransportActivated(transportId)) {
//                for (Long id : new ArrayList<>(mData.getTransport())) {
//                    if (id == transportId) {
//                        mData.getTransport().remove(id);
//                    }
//                }
//            } else {
//                if (null == mData.getTransport()) {
//                    ArrayList<Long> ids = new ArrayList<>();
//                    mData.setTransport(ids);
//                }
//                mData.getTransport().add(transportId);
//            }
//            if (getItemCount() > 0) {
//                notifyItemChanged(position);
//            }
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return RecyclerUtils.getSize(mAllDataTransports);
//        }
//    };
//    private RetrofitCallUtils.RetrofitCallback<TransportGson> mCallbackTransport = new RetrofitCallUtils.RetrofitCallback<TransportGson>() {
//        @Override
//        public void onDataSuccess(TransportGson data) {
//            dismissLoading();
//            if (null != data && null != data.getData()) {
//                bindTransportData(data.getData());
//            }
//        }
//
//        @Override
//        public void onDataFailure(ResponseMessage failCause) {
//            dismissLoading();
//        }
//    };
//    private PloyeeProfileGson.Data mOriginalData;
//    private RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson> mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson>() {
//        @Override
//        public void onDataSuccess(PloyeeProfileGson data) {
//            dismissLoading();
//            dismissRefreshing();
//            if (null != data && null != data.getData()) {
//                bindData(data.getData());
//            }
//        }
//
//        @Override
//        public void onDataFailure(ResponseMessage failCause) {
//            dismissLoading();
//            dismissRefreshing();
//            if (TextUtils.equals(failCause.getMessageCode(), ResponseMessage.CODE_DATA_NOT_FOUND)) {
//                shouldRequestCallSave = true;
//            }
//            bindData(new PloyeeProfileGson.Data());
//        }
//    };
//    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileImageFinish = new Action1<List<ProfileImageGson.Data>>() {
//        @Override
//        public void call(final List<ProfileImageGson.Data> datas) {
//            if (null != datas) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.replaceData(datas);
////                        setUiPageViewController();
//                    }
//                });
//            }
//        }
//    };
//    private RetrofitCallUtils.RetrofitCallback<Object> mCallbackUpdateData = new RetrofitCallUtils.RetrofitCallback<Object>() {
//        @Override
//        public void onDataSuccess(Object data) {
//            dismissLoading();
//            showToastLong("Success");
//            refreshData(PloyeeProfileActivity_Deprecated.this);
//
//        }
//
//        @Override
//        public void onDataFailure(ResponseMessage failCause) {
//            dismissLoading();
//            if (TextUtils.equals(failCause.getMessageCode(), ResponseMessage.CODE_DATA_NOT_FOUND)) {
//                shouldRequestCallSave = true;
//            }
//
//        }
//    };
//
//    private void bindData(PloyeeProfileGson.Data data) {
//        mOriginalData = data;
//        mData = new PostUpdateProfileGson(data, mUserId);
//        requestTransportData();
//        bindData(mData);
//    }
//
//    private void bindData(PostUpdateProfileGson data) {
//        if (null != data) {
//            mEditTextAboutMe.setText(data.getAboutMe());
//            mEditTextEducation.setText(data.getEducation());
//            mEditTextInterest.setText(data.getInterest());
//            mEditTextProfileWork.setText(data.getWork());
//            mButtonEmail.setActivated(data.isContactEmail());
//            mButtonPhone.setActivated(data.isContactPhone());
//
//
//            PloyeeProfileGson.Data.Location locationData = mData.getLocation();
//            if (null != locationData) {
//                setCurrentLatLng(new LatLng(locationData.getLat(), locationData.getLng()));
//            } else {
//                getLocationAndSetToAddressView();
//            }
//
//
//        }
//
//        if (null != mOriginalData) {
//            String languageSupports = "";
//            if (mOriginalData.getLanguage() != null && !mOriginalData.getLanguage().isEmpty()) {
//                for (PloyeeProfileGson.Data.Language language : mOriginalData.getLanguage()) {
//                    languageSupports += language.getSpokenLanguageValue() + " ,";
//                }
//            }
//            mTextViewLanguageSupport.setText(languageSupports);
//        }
//
//    }
//
//    private void requestTransportData() {
//        showLoading();
//        RetrofitCallUtils
//                .with(mMasterApi.getTransportList(), mCallbackTransport)
//                .enqueue(this);
//    }
//
//    private void bindTransportData(List<PloyeeProfileGson.Data.Transport> data) {
//        mAllDataTransports.clear();
//        mTransportRecyclerAdapter.notifyDataSetChanged();
////        for(int i = 0 ;i<10;i++){
//        mAllDataTransports.addAll(toVm(data));
////        }
//        mTransportRecyclerAdapter.notifyItemRangeChanged(0, mTransportRecyclerAdapter.getItemCount());
//    }
//
//    private List<TransportGsonVm> toVm(List<PloyeeProfileGson.Data.Transport> datas) {
//        List<TransportGsonVm> vms = new ArrayList<>();
//        for (PloyeeProfileGson.Data.Transport data : datas) {
//            vms.add(new TransportGsonVm(data.getTransportId(), data.getTransportName(), toTransportIcon(data.getTransportId())));
//        }
//        return vms;
//    }
//
//    private
//    @DrawableRes
//    int toTransportIcon(long transportId) {
//        if (transportId == 1) {
//            return R.drawable.selector_drawable_walk_blue_gray;
//        } else if (transportId == 2) {
//            return R.drawable.selector_drawable_bike_blue_gray;
//        } else if (transportId == 3) {
//            return R.drawable.selector_drawable_car_blue_gray;
//        } else if (transportId == 4) {
//            return R.drawable.selector_drawable_motobike_blue_gray;
//        } else if (transportId == 5) {
//            return R.drawable.selector_drawable_truck_blue_gray;
//        } else if (transportId == 6) {
//            return R.drawable.selector_drawable_bus_blue_gray;
//        } else {
//            return R.drawable.ic_close_white_24dp;
//        }
//    }
//
//    private boolean isTransportActivated(long transportId) {
//        return null != mData && null != mData.getTransport() && !mData.getTransport().isEmpty() && mData.getTransport().contains(transportId);
//    }
//
//    private void refreshData(Context context) {
//        if (null != context && isReady()) {
//            showRefreshing();
//            RetrofitCallUtils.with(mAccountApi.getProfileGson(mUserId), mCallbackLoadData).enqueue(context);
//            refreshSlider();
//        }
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_ployee_profile_deprecated);
//        ButterKnife.bind(this);
//        mAccountApi = getRetrofit().create(AccountApi.class);
//        mMasterApi = getRetrofit().create(MasterApi.class);
//        initToolbar();
//        initView();
//        initSlider();
//        initRecyclerView();
//        if (GoogleApiAvailabilityUtils.checkPlayServices(this)) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
//        }
//    }
//
//    private void initRecyclerView() {
//        mRecyclerViewTransport.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        });
//        mRecyclerViewTransport.setAdapter(mTransportRecyclerAdapter);
//
//    }
//
//    private void initSlider() {
//        mAdapter = new ImageSliderPagerAdapter(this);
//        mViewPagerSlider.setAdapter(mAdapter);
//        mViewPagerSlider.setCurrentItem(0);
//        mTabLayoutSliderIndicator.setupWithViewPager(mViewPagerSlider);
//    }
//
////    private void setUiPageViewController() {
////        mLinearLayoutDotsContainer.removeAllViews();
////        mDotsCount = mAdapter.getCount();
////        if (mDotsCount > 0) {
////            mImageViewDots.clear();
////            for (int i = 0; i < mDotsCount; i++) {
////                ImageView img = new ImageView(this);
////                img.setImageDrawable(mDrawableNonSelecteddot);
////                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
////                        LinearLayout.LayoutParams.WRAP_CONTENT,
////                        LinearLayout.LayoutParams.WRAP_CONTENT
////                );
////
////                params.setMargins(4, 0, 4, 0);
////                mLinearLayoutDotsContainer.addView(img, params);
////                mImageViewDots.add(img);
////
////
////            }
////            if (RecyclerUtils.isAvailableData(mImageViewDots, 0)) {
////                mImageViewDots.get(0).setImageDrawable(mDrawableSelectedDot);
////            }
////
////        }
////
////    }
//
//    private void refreshSlider() {
//        AccountInfoLoader.getProfileImage(this, mUserId, true, mOnLoadProfileImageFinish);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (null != mGoogleApiClient) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        if (null != mGoogleApiClient) {
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mAllDataTransports.isEmpty()) {
//            requestTransportData();
//        }
//    }
//
//    private void initToolbar() {
//        mTextViewTitle.setText(LProfile);
//        enableBackButton(mToolbar);
//        mToolbar.inflateMenu(R.menu.menu_done);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.menu_done_item_done) {
//                    onClickDone();
//                }
//                return false;
//            }
//
//
//        });
//    }
//
//    private void onClickDone() {
//        showLoading();
//        if (shouldRequestCallSave) {
//            RetrofitCallUtils.with(mAccountApi.postSaveProfileGson(gatheredData()), mCallbackUpdateData).enqueue(this);
//        } else {
//            RetrofitCallUtils.with(mAccountApi.postSaveProfileGson(gatheredData()), mCallbackUpdateData).enqueue(this);
//        }
//    }
//
//    private PostUpdateProfileGson gatheredData() {
//        if (mData != null) {
//            mData.setAboutMe(extractString(mEditTextAboutMe));
//            mData.setEducation(extractString(mEditTextEducation));
//            mData.setInterest(extractString(mEditTextInterest));
//            mData.setUserId(mUserId);
//            mData.setWork(extractString(mEditTextProfileWork));
//            mData.setContactEmail(mButtonEmail.isActivated());
//            mData.setContactPhone(mButtonPhone.isActivated());
//            if (mCurrentLatLng != null) {
//                mData.setLocation(new PloyeeProfileGson.Data.Location(mCurrentLatLng.latitude, mCurrentLatLng.longitude));
//            }
//
//
//            return mData;
//        } else {
//            return new PostUpdateProfileGson();
//        }
//
//    }
//
//    private void initView() {
//        mButtonEmail.setOnClickListener(this);
//        mButtonPhone.setOnClickListener(this);
//        mImageButtonCheckin.setOnClickListener(this);
//        mTextViewLanguageSupport.setOnClickListener(this);
//        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData(mSwipeRefreshLayout.getContext());
//            }
//        });
//        mFabProfileImage.setOnClickListener(this);
//        mEditTextAboutMe.setFocusFraction(1f);
//        mEditTextEducation.setFocusFraction(1f);
//        mEditTextProfileWork.setFocusFraction(1f);
//        mEditTextInterest.setFocusFraction(1f);
//
//    }
//
//    private void initMap() {
//        FragmentTransactionUtils.addFragmentToActivity(getSupportFragmentManager(), mMapFragment, R.id.framelayout_ployee_profile_maps_container);
//        if (null != mMapFragment) {
//            mMapFragment.getMapAsync(this);
//            if (null != mMapFragment.getMarkerOptions()) {
//                mMapFragment.getMarkerOptions().setClickable(false);
//            }
//
//        }
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//        if (mGoogleMap != null) {
//            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        }
//        if (null == mData) {
//            refreshData(PloyeeProfileActivity_Deprecated.this);
//        }
//    }
//
//    private void moveCameraToCurrentLocation(LatLng latLng) {
//        if (mGoogleMap != null) {
//            mGoogleMap.clear();
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
//            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == mButtonEmail.getId()) {
//            mButtonEmail.setActivated(!mButtonEmail.isActivated());
//        } else if (id == mButtonPhone.getId()) {
//            mButtonPhone.setActivated(!mButtonPhone.isActivated());
//        } else if (id == mFabProfileImage.getId()) {
//            showUploadPhotoFragment();
//        } else if (id == mImageButtonCheckin.getId()) {
//            getLocationAndSetToAddressView();
//        } else if (id == mTextViewLanguageSupport.getId()) {
//            showLanguageChooser();
//        }
//    }
//
//    private void showLanguageChooser() {
//        showFragment(SpokenLanguageChooserFragment.newInstance(mUserId, mData.getLanguage(), new SpokenLanguageChooserFragment.OnDataChangedListener() {
//            @Override
//            public void onClickDone(ArrayList<String> datas) {
//                mData = gatheredData();
//                mData.setLanguage(datas);
//                bindData(mData);
//                PloyeeProfileActivity_Deprecated.this.onClickDone();
//            }
//        }));
//
//    }
//
//    private void getLocationAndSetToAddressView() {
//        MyLocationUtils.getLastKnownLocation(this, mGoogleApiClient, true, new Action1<Location>() {
//            @Override
//            public void call(Location location) {
//                if (null != location) {
//                    setCurrentLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                }
//            }
//        });
//
//    }
//
//
//    private void setCurrentLatLng(LatLng latlng) {
//        mCurrentLatLng = latlng;
//        moveCameraToCurrentLocation(mCurrentLatLng);
//        String address = MyLocationUtils.getCompleteAddressString(PloyeeProfileActivity_Deprecated.this, mCurrentLatLng.latitude, mCurrentLatLng.longitude);
//        mTextViewAddress.setText(address);
//    }
//
////    private void dummyData() {
////        mEditTextProfileWork.setText("workๆๆๆๆ");
////        mEditTextInterest.setText("ก้สนใจยู่อิอิ");
////        mEditTextEducation.setText("เอดูดุ้000");
////        mEditTextAboutMe.setText("หล่อ");
////    }
//
//    private void showUploadPhotoFragment() {
//        AccountInfoLoader.getProfileImage(this, mUserId, false, new Action1<List<ProfileImageGson.Data>>() {
//                    @Override
//                    public void call(List<ProfileImageGson.Data> datas) {
//                        showFragment(UploadPhotoFragment.newInstance(mUserId, new ArrayList<>(datas), new UploadPhotoFragment.OnDataChangeListener() {
//                            @Override
//                            public void onDataChange() {
//                                refreshSlider();
//                            }
//                        }));
//                    }
//                }
//        );
//    }
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        initMap();
//    }
//
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//}
