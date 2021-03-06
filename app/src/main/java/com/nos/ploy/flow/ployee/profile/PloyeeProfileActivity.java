package com.nos.ploy.flow.ployee.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.nos.ploy.MyApplication;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.PloyeeProfileGson;
import com.nos.ploy.api.account.model.PostUpdateProfileGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.account.model.TransportGson;
import com.nos.ploy.api.account.model.TransportGsonVm;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.api.utils.loader.AccountInfoLoader;
import com.nos.ploy.base.BaseActivity;
import com.nos.ploy.flow.generic.maps.LocalizationMapsFragment;
import com.nos.ploy.flow.ployee.profile.language.SpokenLanguageChooserFragment;
import com.nos.ploy.flow.ployee.profile.upload.UploadPhotoFragment;
import com.nos.ploy.flow.ployer.provider.ProviderProfileActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.PopupMenuUtils;
import com.nos.ploy.utils.RecyclerUtils;
import com.nos.ploy.utils.URLHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import rx.functions.Action1;

/**
 * Created by Saran on 15/12/2559.
 */

public class PloyeeProfileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.button_ployee_profile_show_email)
    Button mButtonEmail;
    @BindView(R.id.button_ployee_profile_show_phone_no)
    Button mButtonPhone;
    @BindView(R.id.textview_ployee_profile_address)
    TextView mTextViewAddress;
    @BindView(R.id.edittext_ployee_profile_about_me)
    MaterialEditText mEditTextAboutMe;
    @BindView(R.id.edittext_ployee_profile_education)
    MaterialEditText mEditTextEducation;
    @BindView(R.id.edittext_ployee_profile_work)
    MaterialEditText mEditTextProfileWork;
    @BindView(R.id.textview_ployee_profile_language_label)
    TextView mTextViewLanguageLabel;
    @BindView(R.id.edittext_ployee_profile_interest)
    MaterialEditText mEditTextInterest;
    @BindView(R.id.textview_ployee_profile_transport_label)
    TextView mTextViewTransportLabel;
    @BindView(R.id.textview_ployee_profile_language_support)
    TextView mTextViewLanguageSupport;
    @BindView(R.id.linearlayout_ployee_profile_language_label_container)
    ForegroundLinearLayout mLinearLayoutLanguageContainer;
    @BindView(R.id.recyclerview_ployee_profile_tranportation)
    RecyclerView mRecyclerViewTransport;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubtitle;

    @BindView(R.id.imageview_ployee_profile_static_maps)
    ImageView mImageViewStaticMaps;
    @BindString(R.string.Profile)
    String LProfile;
    @BindView(R.id.scrollview_ployee_profile)
    NestedScrollView mScrollView;
    @BindView(R.id.fab_ployee_profile_image)
    FloatingActionButton mFabProfileImage;
    @BindView(R.id.viewpager_profile_image_slider)
    ViewPager mViewPagerSlider;
    @BindView(R.id.tablayout_profile_image_slider_indicator)
    TabLayout mTabLayoutSliderIndicator;
    @BindView(R.id.swiperefreshlayout_ployee_profile)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.button_ployee_profile_preview)
    Button mButtonPreview;
    @BindView(R.id.imageview_ployee_profile_checkin)
    ImageView mImageButtonCheckin;
    @BindDrawable(R.drawable.nonselecteditem_dot)
    Drawable mDrawableNonSelecteddot;
    @BindDrawable(R.drawable.selecteditem_dot)
    Drawable mDrawableSelectedDot;
    @BindView(R.id.textview_ployee_profile_contact_method_label)
    TextView mTextViewContactMethod;
    //    private GoogleMap mGoogleMap;
//    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private boolean isContentChanged = false;
    private AccountApi mAccountApi;
    private PostUpdateProfileGson mData;
    private MasterApi mMasterApi;
    private ImageSliderPagerAdapter mAdapter;
    public LatLng mCurrentLatLng;
    private List<TransportGsonVm> mAllDataTransports = new ArrayList<>();
    private TextWatcher mContentChangedTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setIsContentChanged(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TransportRecyclerAdapter mTransportRecyclerAdapter = new TransportRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mAllDataTransports, position)) {
                TransportGsonVm data = mAllDataTransports.get(position);
                holder.imgTransport.setImageResource(data.getDrawable());
                holder.tvTitle.setText(toTransportName(data.getId()));

                holder.imgTransport.setActivated(isTransportActivated(data.getId()));
                holder.imgTransport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RecyclerUtils.isAvailableData(mAllDataTransports, holder.getAdapterPosition())) {
                            TransportGsonVm data = mAllDataTransports.get(holder.getAdapterPosition());
                            toggleEnableTransport(data.getId(), holder.getAdapterPosition());
                            setIsContentChanged(true);
                        }
                    }
                });
            }
        }


        private void toggleEnableTransport(long transportId, int position) {
            if (isTransportActivated(transportId)) {
                for (Long id : new ArrayList<>(mData.getTransport())) {
                    if (id == transportId) {
                        mData.getTransport().remove(id);
                    }
                }
            } else {
                if (null == mData.getTransport()) {
                    ArrayList<Long> ids = new ArrayList<>();
                    mData.setTransport(ids);
                }
                mData.getTransport().add(transportId);
            }
            if (getItemCount() > 0) {
                notifyItemChanged(position);
            }
        }


        @Override
        public int getItemCount() {
            return RecyclerUtils.getSize(mAllDataTransports);
        }
    };
    private RetrofitCallUtils.RetrofitCallback<TransportGson> mCallbackTransport = new RetrofitCallUtils.RetrofitCallback<TransportGson>() {
        @Override
        public void onDataSuccess(TransportGson data) {
            dismissLoading();
            if (null != data && null != data.getData()) {
                bindTransportData(data.getData());
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();
        }
    };
    private PloyeeProfileGson.Data mOriginalData;
    private RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson> mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson>() {
        @Override
        public void onDataSuccess(PloyeeProfileGson data) {
            dismissLoading();
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();
            dismissRefreshing();
            bindData(new PloyeeProfileGson.Data());
        }
    };
    private Action1<List<ProfileImageGson.Data>> mOnLoadProfileImageFinish = new Action1<List<ProfileImageGson.Data>>() {
        @Override
        public void call(final List<ProfileImageGson.Data> datas) {
            if (null != datas) {
                runOnUiThread(new Action1<Context>() {
                    @Override
                    public void call(Context context) {
                        mAdapter.replaceData(datas);
                    }
                });
            }
        }
    };
    private RetrofitCallUtils.RetrofitCallback<BaseResponse> mCallbackUpdateData = new RetrofitCallUtils.RetrofitCallback<BaseResponse>() {
        @Override
        public void onDataSuccess(BaseResponse data) {
            dismissLoading();
            showToastLong(MyApplication.getInstance().getLabelLanguage().labelSaved);
            refreshData(PloyeeProfileActivity.this, mCallbackLoadData);
            setIsContentChanged(false);

        }

        @Override
        public void onDataFailure(String failCause) {
            dismissLoading();
        }
    };
    private PostUpdateProfileGson mOriginalPostData;
    private boolean isFirstLoaded;
    private String doneLabel = "Save";


    private void bindData(PloyeeProfileGson.Data data) {
        mOriginalData = data;
        mOriginalPostData = new PostUpdateProfileGson(data, mUserId);
        mData = new PostUpdateProfileGson(data, mUserId);
        requestTransportData();
        bindData(mData);
    }

    private void onFinishChangeLanguage(final PloyeeProfileGson.Data data) {
        runOnUiThread(new Action1<Context>() {
            @Override
            public void call(Context context) {
                String languageSupports = "";
                if (data.getLanguage() != null && !data.getLanguage().isEmpty()) {
                    for (PloyeeProfileGson.Data.Language language : data.getLanguage()) {
                        languageSupports += " " + language.getSpokenLanguageValue() + ",";
                    }
                }
                languageSupports = removeLastCharacter(languageSupports);
                mTextViewLanguageSupport.setText(languageSupports);
            }

        });

    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);

        doneLabel = data.doneLabel;
        mTransportRecyclerAdapter.setLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, data.doneLabel);
//        mTextViewTitle.setText(data.profileScreenHeader);
        mTextViewSubtitle.setVisibility(View.VISIBLE);
        mTextViewSubtitle.setText(data.profileSubtitleEditprofile);


        mButtonEmail.setText(data.profileScreenEmail);
        mEditTextAboutMe.setFloatingLabelText(data.profileScreenAboutMe);
        mEditTextAboutMe.setHint(data.profileScreenAboutMeHint);
        mTextViewLanguageLabel.setText(data.profileScreenLanguage);
        mEditTextEducation.setFloatingLabelText(data.profileScreenEducation);
        mEditTextEducation.setHint(data.profileScreenEducationHint);
        mEditTextProfileWork.setFloatingLabelText(data.profileScreenWork);
        mEditTextProfileWork.setHint(data.profileScreenWorkHint);
        mEditTextInterest.setFloatingLabelText(data.profileScreenInterest);
        mEditTextInterest.setHint(data.profileScreenInterestHint);
        mTextViewTransportLabel.setText(data.profileScreenTransport);
        mButtonEmail.setText(data.profileScreenShowEmail);
        mButtonPhone.setText(data.profileScreenShowPhone);
        mTextViewContactMethod.setText(data.profileScreenContactMethod);
        mButtonPreview.setText(data.profileScreenPreview);
    }

    private void setText(EditText editText, String text) {
        editText.removeTextChangedListener(mContentChangedTextWatcher);
        editText.setText(text);
        editText.addTextChangedListener(mContentChangedTextWatcher);
    }

    private void bindData(final PostUpdateProfileGson data) {
        if (null != data) {
            runOnUiThread(new Action1<Context>() {
                              @Override
                              public void call(Context context) {

                                  setText(mEditTextAboutMe, data.getAboutMe());
                                  setText(mEditTextEducation, data.getEducation());
                                  setText(mEditTextInterest, data.getInterest());
                                  setText(mEditTextProfileWork, data.getWork());
                                  mButtonEmail.setActivated(data.isContactEmail());
                                  mButtonPhone.setActivated(data.isContactPhone());


                                  PloyeeProfileGson.Data.Location locationData = mData.getLocation();
                                  if (null != mOriginalData && null != mOriginalData.getLocation()) {
                                      if (mOriginalData.getLocation().neverPinLocationBefore()) {
                                          setCurrentLatLng(MyLocationUtils.DEFAULT_LATLNG);
                                      } else if (null != locationData) {
                                          setCurrentLatLng(new LatLng(locationData.getLat(), locationData.getLng()));
                                      }
                                  }

                              }

                          }
            );


        }

        if (null != mOriginalData) {
            onFinishChangeLanguage(mOriginalData);
        }

    }


    public String removeLastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void requestTransportData() {
        showLoading();
        RetrofitCallUtils
                .with(mMasterApi.getTransportList(), mCallbackTransport)
                .enqueue(this);
    }

    private void bindTransportData(List<PloyeeProfileGson.Data.Transport> data) {
        mAllDataTransports.clear();
        mTransportRecyclerAdapter.notifyDataSetChanged();
//        for(int i = 0 ;i<10;i++){
        mAllDataTransports.addAll(toVm(data));
//        }
        mTransportRecyclerAdapter.notifyItemRangeChanged(0, mTransportRecyclerAdapter.getItemCount());
    }

    private List<TransportGsonVm> toVm(List<PloyeeProfileGson.Data.Transport> datas) {
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

    private boolean isTransportActivated(long transportId) {
        return null != mData && null != mData.getTransport() && !mData.getTransport().isEmpty() && mData.getTransport().contains(transportId);
    }

    private void refreshData(Context context, RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson> onfinish) {
        if (null != context && isReady()) {
            showRefreshing();
            RetrofitCallUtils.with(mAccountApi.getProfileGson(mUserId), onfinish).enqueueDontToast(context);
            refreshSlider();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ployee_profile);
        ButterKnife.bind(this);
        mAccountApi = getRetrofit().create(AccountApi.class);
        mMasterApi = getRetrofit().create(MasterApi.class);
        initToolbar();
        initView();
        initSlider();
        initRecyclerView();

        detectContentChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isFirstLoaded) {
            isFirstLoaded = true;
            refreshData(this, mCallbackLoadData);
        }
    }

    private void detectContentChanged() {

//        List<EditText> editTextList = getAllEditTexts(mSwipeRefreshLayout);
//        for (EditText editText : editTextList) {
//            editText.addTextChangedListener(mContentChangedTextWatcher);
//        }
        mEditTextAboutMe.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextEducation.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextInterest.addTextChangedListener(mContentChangedTextWatcher);
        mEditTextProfileWork.addTextChangedListener(mContentChangedTextWatcher);
    }

    private void showStaticMaps(final LatLng latLng) {
        runOnUiThread(new Action1<Context>() {
            @Override
            public void call(Context context) {
                String address = MyLocationUtils.getCompleteAddressString(PloyeeProfileActivity.this, mCurrentLatLng.latitude, mCurrentLatLng.longitude);
//                if (null != mOriginalData && null != mOriginalData.getLocation() && mOriginalData.getLocation().neverPinLocationBefore() && !MyLocationUtils.locationProviderEnabled(this)) {
//                    mTextViewAddress.setText(mLanguageData.profileScreenLocation);
//                } else {
//                    mTextViewAddress.setText(address);
//                }
                if (null != mOriginalData && null != mOriginalData.getLocation() && mOriginalData.getLocation().neverPinLocationBefore()) {
                    Glide.with(context).load(URLHelper.changURLEndpoint(MyLocationUtils.getParisStaticMaps())).into(mImageViewStaticMaps);
                    mTextViewAddress.setText(mLanguageData.profileScreenLocation);
                } else {
                    Glide.with(context).load(URLHelper.changURLEndpoint(MyLocationUtils.getStaticMapsUrl(latLng))).into(mImageViewStaticMaps);
                    mTextViewAddress.setText(address);
                }

            }


        });

    }

    private void initRecyclerView() {
        mRecyclerViewTransport.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerViewTransport.setAdapter(mTransportRecyclerAdapter);

    }

    private void initSlider() {
        mAdapter = new ImageSliderPagerAdapter(this);
        mViewPagerSlider.setAdapter(mAdapter);
        mViewPagerSlider.setCurrentItem(0);
        mTabLayoutSliderIndicator.setupWithViewPager(mViewPagerSlider);
    }

//    private void setUiPageViewController() {
//        mLinearLayoutDotsContainer.removeAllViews();
//        mDotsCount = mAdapter.getCount();
//        if (mDotsCount > 0) {
//            mImageViewDots.clear();
//            for (int i = 0; i < mDotsCount; i++) {
//                ImageView img = new ImageView(this);
//                img.setImageDrawable(mDrawableNonSelecteddot);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//
//                params.setMargins(4, 0, 4, 0);
//                mLinearLayoutDotsContainer.addView(img, params);
//                mImageViewDots.add(img);
//
//
//            }
//            if (RecyclerUtils.isAvailableData(mImageViewDots, 0)) {
//                mImageViewDots.get(0).setImageDrawable(mDrawableSelectedDot);
//            }
//
//        }
//
//    }

    private void refreshSlider() {
        AccountInfoLoader.getProfileImage(this, mUserId, true, mOnLoadProfileImageFinish);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAllDataTransports.isEmpty()) {
            requestTransportData();
        }


        AccountInfoLoader.getAccountGson(this, mUserId, new Action1<AccountGson.Data>() {
            @Override
            public void call(AccountGson.Data data) {

                mTextViewTitle.setText(data.getFirstName() + " " + data.getLastName());


            }
        });


    }

    private void initToolbar() {
        mTextViewTitle.setText(LProfile);
        enableBackButton(mToolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void onClickDone() {
        showLoading();
        RetrofitCallUtils.with(mAccountApi.postSaveProfileGson(gatheredData()), mCallbackUpdateData).enqueue(this);
    }

    private void postSaveProfileGson(PostUpdateProfileGson data, final Action1<PloyeeProfileGson> onFinish) {
        showLoading();
        RetrofitCallUtils.with(mAccountApi.postSaveProfileGson(data), new RetrofitCallUtils.RetrofitCallback<BaseResponse>() {
            @Override
            public void onDataSuccess(BaseResponse data) {
                dismissLoading();
                showToastLong(MyApplication.getInstance().getLabelLanguage().labelSaved);
                refreshData(PloyeeProfileActivity.this, new RetrofitCallUtils.RetrofitCallback<PloyeeProfileGson>() {
                    @Override
                    public void onDataSuccess(PloyeeProfileGson data) {
                        dismissLoading();
                        dismissRefreshing();
                        if (null != data && null != data.getData()) {
                            mOriginalData = data.getData();
                            onFinish.call(data);
                        }
                    }

                    @Override
                    public void onDataFailure(String failCause) {
                        dismissLoading();
                        dismissRefreshing();
//                        bindData(new PostUpdateProfileGson());
                        onFinish.call(null);

                    }
                });
            }

            @Override
            public void onDataFailure(String failCause) {
                dismissLoading();
                dismissRefreshing();
            }
        }).enqueue(this);
    }


    private PostUpdateProfileGson gatheredData() {
        if (mData != null) {
            mData.setAboutMe(extractString(mEditTextAboutMe));
            mData.setEducation(extractString(mEditTextEducation));
            mData.setInterest(extractString(mEditTextInterest));
            mData.setUserId(mUserId);
            mData.setWork(extractString(mEditTextProfileWork));
            mData.setContactEmail(mButtonEmail.isActivated());
            mData.setContactPhone(mButtonPhone.isActivated());
            if (mCurrentLatLng != null) {
                mData.setLocation(new PloyeeProfileGson.Data.Location(mCurrentLatLng.latitude, mCurrentLatLng.longitude));
            }


            return mData;
        } else {
            return new PostUpdateProfileGson();
        }

    }

    private void initView() {
        mButtonEmail.setOnClickListener(this);
        mButtonPhone.setOnClickListener(this);
        mImageButtonCheckin.setOnClickListener(this);
        mLinearLayoutLanguageContainer.setOnClickListener(this);
        mImageViewStaticMaps.setOnClickListener(this);
        mButtonPreview.setOnClickListener(this);

        mSwipeRefreshLayout.setEnabled(false);
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(mSwipeRefreshLayout.getContext(), mCallbackLoadData);
            }
        });
        mFabProfileImage.setOnClickListener(this);
        mEditTextAboutMe.setFocusFraction(1f);
        mEditTextEducation.setFocusFraction(1f);
        mEditTextProfileWork.setFocusFraction(1f);
        mEditTextInterest.setFocusFraction(1f);

    }

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

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//        if (mGoogleMap != null) {
//            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        }
//        if (null == mData) {
//            refreshData(PloyeeProfileActivity.this);
//        }
//    }


    @Override
    public void onBackPressed() {
        if (isContentChanged) {
            PopupMenuUtils.showConfirmationAlertMenu(this, null, mLanguageData.accountScreenConfirmBeforeBack, mLanguageData.okLabel, mLanguageData.cancelLabel, new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (yes) {
                        finishThisActivity();
                    }
                }
            });
        } else {
            finishThisActivity();
        }
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == mButtonEmail.getId()) {
            mButtonEmail.setActivated(!mButtonEmail.isActivated());
            setIsContentChanged(true);
        } else if (id == mButtonPhone.getId()) {
            AccountInfoLoader.getAccountGson(v.getContext(), mUserId, new Action1<AccountGson.Data>() {
                @Override
                public void call(AccountGson.Data data) {
                    if (!TextUtils.isEmpty(data.getPhone())) {
                        mButtonPhone.setActivated(!mButtonPhone.isActivated());
                        setIsContentChanged(true);
                    } else {
                        PopupMenuUtils.showConfirmationAlertMenu(v.getContext(), null, mLanguageData.profileScreenNoPhone, mLanguageData.okLabel, null, null);
                    }
                }
            });

        } else if (id == mFabProfileImage.getId()) {
            showUploadPhotoFragment();
        } else if (id == mImageButtonCheckin.getId()) {
            getLocationAndSetToAddressView();

        } else if (id == mLinearLayoutLanguageContainer.getId()) {
            showLanguageChooser();
        } else if (id == mImageViewStaticMaps.getId()) {
            showFragment(LocalizationMapsFragment.newInstance(mCurrentLatLng, true, mOriginalData.getLocation().neverPinLocationBefore(), new LocalizationMapsFragment.OnChooseLocationFinishListener() {
                @Override
                public void onFinishChoosingLocation(LatLng latLng) {
                    setCurrentLatLng(latLng);
                    mData.setLocation(new PloyeeProfileGson.Data.Location(latLng.latitude, latLng.longitude));
                    postSaveProfileGson(mData, new Action1<PloyeeProfileGson>() {
                        @Override
                        public void call(PloyeeProfileGson data) {
                            if (null != data.getData().getLocation()) {
                                setCurrentLatLng(new LatLng(data.getData().getLocation().getLat(), data.getData().getLocation().getLng()));
                            }

                        }
                    });
                }
            }));
        } else if (id == mButtonPreview.getId()) {
            onClickPreview(v.getContext());
        }
    }

    private void setIsContentChanged(boolean isContentChanged) {
        this.isContentChanged = isContentChanged;
        if (isContentChanged) {

            PopupMenuUtils.clearAndInflateMenu(mToolbar, R.menu.menu_done, new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.menu_done_item_done) {
                        onClickDone();
                    }
                    return false;
                }
            });
            MenuItem menu = mToolbar.getMenu().findItem(R.id.menu_done_item_done);
            menu.setTitle(doneLabel);
        } else {
            PopupMenuUtils.clearMenu(mToolbar);
        }
    }


    private void onClickPreview(final Context context) {
        AccountInfoLoader.getAccountGson(this, mUserId, new Action1<AccountGson.Data>() {
            @Override
            public void call(AccountGson.Data data) {
                Bundle bundle = new Bundle();
                ProviderUserListGson.Data.UserService mockUserService = new ProviderUserListGson.Data.UserService(mUserId, data, mCurrentLatLng);
                bundle.putParcelable(ProviderProfileActivity.KEY_PLOYEE_USER_SERVICE_DATA, mockUserService);
                bundle.putBoolean(ProviderProfileActivity.KEY_IS_PREVIEW, true);
                IntentUtils.startActivity(context, ProviderProfileActivity.class, bundle);
            }
        });

    }

    private void showLanguageChooser() {
        showFragment(SpokenLanguageChooserFragment.newInstance(mUserId, mData.getLanguage(), new SpokenLanguageChooserFragment.OnDataChangedListener() {
            @Override
            public void onClickDone(ArrayList<String> datas) {
                mData.setLanguage(datas);
                postSaveProfileGson(mData, new Action1<PloyeeProfileGson>() {
                    @Override
                    public void call(PloyeeProfileGson ployeeProfileGson) {
                        if (null != ployeeProfileGson && null != ployeeProfileGson.getData()) {
                            onFinishChangeLanguage(ployeeProfileGson.getData());
                        }
                    }
                });
            }
        }));

    }


    private void getLocationAndSetToAddressView() {
//        MyLocationUtils.getLastKnownLocation(this, mGoogleApiClient, true);
        if (MyLocationUtils.checkLocationEnabled(this)) {
            SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(final Location location) {
                    if (null != location) {
                        runOnUiThread(new Action1<Context>() {
                            @Override
                            public void call(Context context) {
                                setIsContentChanged(true);
                                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                String address = MyLocationUtils.getCompleteAddressString(PloyeeProfileActivity.this, mCurrentLatLng.latitude, mCurrentLatLng.longitude);
                                Glide.with(PloyeeProfileActivity.this).load(URLHelper.changURLEndpoint(MyLocationUtils.getStaticMapsUrl(mCurrentLatLng))).into(mImageViewStaticMaps);
                                mTextViewAddress.setText(address);
                            }
                        });

                    }
                }
            });

        }
    }


    private void setCurrentLatLng(LatLng latlng) {
        mCurrentLatLng = latlng;
        showStaticMaps(latlng);
    }

    private void showUploadPhotoFragment() {
        AccountInfoLoader.getProfileImage(this, mUserId, false, new Action1<List<ProfileImageGson.Data>>() {
                    @Override
                    public void call(List<ProfileImageGson.Data> datas) {
                        showFragment(UploadPhotoFragment.newInstance(mUserId, new ArrayList<>(datas), new UploadPhotoFragment.OnDataChangeListener() {
                            @Override
                            public void onDataChange() {
                                refreshSlider();
                            }
                        }));
                    }
                }
        );
    }


}
