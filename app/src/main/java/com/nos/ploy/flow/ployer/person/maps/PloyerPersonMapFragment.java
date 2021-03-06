package com.nos.ploy.flow.ployer.person.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployer.person.PloyerPersonActivity;
import com.nos.ploy.flow.ployer.person.maps.viewmodel.PloyerPersonMapViewModel;
import com.nos.ploy.flow.ployer.provider.ProviderProfileActivity;
import com.nos.ploy.utils.DrawableUtils;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RatingBarUtils;
import com.nos.ploy.utils.RecyclerUtils;
import com.nos.ploy.utils.URLHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 8/1/2560.
 */

public class PloyerPersonMapFragment extends BaseFragment implements OnMapReadyCallback, SearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    @BindView(R.id.nestedscrollview_ployer_person_maps_bottomsheet)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.cardview_ployer_person_maps_bottomsheet_container)
    CardView mCardViewSheetContainer;
    @BindView(R.id.textview_ployer_person_maps_bottom_sheet_item_price)
    TextView mTextViewPrice;
    @BindView(R.id.imageview_ployer_person_maps_bottom_sheet_item_profile_photo)
    ImageView mImageViewProfilePhoto;
    @BindView(R.id.ratingbar_ployer_person_maps_bottom_sheet_item_rate)
    RatingBar mRatingBarRate;
    @BindView(R.id.textview_ployer_person_maps_bottom_sheet_item_title)
    TextView mTextViewSheetTitle;
    @BindView(R.id.textview_ployer_maps_bottom_sheet_item_subtitle)
    TextView mTExtViewSheetSubTitle;
    @BindView(R.id.textview_ployer_person_maps_bottom_sheet_distance)
    TextView mTextViewDistance;
    @BindView(R.id.textview_ployer_person_maps_bottom_sheet_item_description)
    TextView mTextViewDescription;
    @BindView(R.id.textview_ployer_person_maps_bottom_sheet_item_review_count)
    TextView mTextViewReviewCount;
    @BindView(R.id.textview_ployer_maps_bottom_sheet_item_rate)
    TextView mTextViewRating;
    @BindDimen(R.dimen.dp24)
    int dp24;
    private BottomSheetBehavior<NestedScrollView> mBottomSheetBehavior;
    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private GoogleMap mGoogleMap;
    private List<PloyerPersonMapViewModel> mDatas = new ArrayList<>();

    public static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private PloyerServicesGson.Data mServiceData;
    private PloyerApi mApi;
    private boolean isRequesting;
    private GoogleApiClient mGoogleApiClient;
    private ProviderUserListGson.Data.UserService mCurrentSelectedData;

    public static PloyerPersonMapFragment newInstance(PloyerServicesGson.Data data, GoogleApiClient googleApiClient) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        PloyerPersonMapFragment fragment = new PloyerPersonMapFragment();
        fragment.setArguments(args);
        fragment.setGoogleApiClient(googleApiClient);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployer_person_maps, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mServiceData = getArguments().getParcelable(KEY_SERVICE_DATA);
        }

        if (null == mApi) {
            mApi = getRetrofit().create(PloyerApi.class);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        initView();
    }

    private void initMap() {
        FragmentTransactionUtils.addFragmentToActivity(getChildFragmentManager(), mMapFragment, R.id.framelayout_ployer_person_maps_maps_container);
        if (null != mMapFragment) {
            mMapFragment.getMapAsync(this);
        }
    }

    private void initView() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mNestedScrollView);
        mBottomSheetBehavior.setPeekHeight(0);
        mCardViewSheetContainer.setOnClickListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (null != mGoogleMap) {
            mGoogleMap.setOnMarkerClickListener(this);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showDialogAlertLocation();
            }else{
                mGoogleMap.setMyLocationEnabled(true);
            }
            mGoogleMap.setPadding(0, 0, 0, dp24);
        }
        bindDataToMap();
    }


    public void bindData(final List<ProviderUserListGson.Data.UserService> datas) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              mDatas.clear();
                              if (null != datas && !datas.isEmpty()) {
                                  for (ProviderUserListGson.Data.UserService data : datas) {
                                      mDatas.add(new PloyerPersonMapViewModel(data, intoMarker(data)));
                                  }
                              }
                              bindDataToMap();
                          }
                      }
        );

    }

    private void bindDataToMap() {
        if (mGoogleMap != null) {

            if (RecyclerUtils.getSize(mDatas) > 0) {
                mGoogleMap.clear();
                LatLngBounds.Builder latlngBoundBuilder = new LatLngBounds.Builder();
                boolean isInclude = false;
                for (PloyerPersonMapViewModel data : mDatas) {


                    if (null != data.getData() && null != data.getMarkerOptions()) {
                        if (data.getData().getLocationLat()  > 0 && data.getData().getLocationLng() > 0){

                            Marker marker = mGoogleMap.addMarker(data.getMarkerOptions());
                            marker.setTag(data.getData());
                            latlngBoundBuilder.include(marker.getPosition());
                            isInclude = true;
                        }


                    }
                }
                if (isInclude) {
//                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBoundBuilder.build(), 15));
                }
            } else {
                mGoogleMap.clear();

            }


            Location location = MyLocationUtils.getLastKnownLocation(getActivity(), mGoogleApiClient);

            double latitude = MyLocationUtils.DEFAULT_LATLNG.latitude;
            double longitude = MyLocationUtils.DEFAULT_LATLNG.longitude;

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));



        }

    }

    private MarkerOptions intoMarker(final ProviderUserListGson.Data.UserService data) {
        if (null == data || null == data.getLocationLat() || null == data.getLocationLng()) {
            return null;
        }
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_maps_pin_box, null);
        TextView tvMarkerTitle = (TextView) v.findViewById(R.id.textview_maps_pin_box);
        tvMarkerTitle.setText(mLanguageData.currencyLabel + " " + data.getMinPrice());
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(data.getLocationLat(), data.getLocationLng()));
        marker.icon(BitmapDescriptorFactory.fromBitmap(DrawableUtils.createBitmapFromView(getContext(), v)));
        return marker;
    }

    private void onClickMarker(final ProviderUserListGson.Data.UserService data) {
        if (null != data) {
            mCurrentSelectedData = data;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mImageViewProfilePhoto.getContext()).load(URLHelper.changURLEndpoint(data.getImagePath())).error(R.drawable.ic_ployer_item_placeholder).into(mImageViewProfilePhoto);
                }
            });

            mTextViewSheetTitle.setText(data.getFullName());
            mTExtViewSheetSubTitle.setText(mServiceData.getServiceName());
            mTextViewDescription.setText(data.getDescription());
            mTextViewPrice.setText(mLanguageData.labelFrom + " " + mLanguageData.currencyLabel + " " + data.getMinPrice() + mServiceData.getPriceUnit());
            mTextViewReviewCount.setText("" + data.getReviewCount());
            mTextViewRating.setText(data.getReviewPoint() + "/5");
            mRatingBarRate.setRating(RatingBarUtils.getRatingbarRoundingNumber(data.getReviewPoint()));
            if (null != data.getLocationLat() && null != data.getLocationLng() && MyLocationUtils.locationProviderEnabled(mTextViewDistance.getContext())) {
                mTextViewDistance.setText(MyLocationUtils.getDistanceFromCurrentLocation(mTextViewDistance.getContext(), mGoogleApiClient, new LatLng(data.getLocationLat(), data.getLocationLng())));
            } else {
                mTextViewDistance.setText("-");
            }

            expandBottomSheet(true);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null != marker) {
            if (null != marker.getTag() && marker.getTag() instanceof ProviderUserListGson.Data.UserService) {
                ProviderUserListGson.Data.UserService data = (ProviderUserListGson.Data.UserService) marker.getTag();
                onClickMarker(data);
            }
        }
        return false;
    }


    private void expandBottomSheet(boolean expand) {
        if (null != mBottomSheetBehavior) {
            if (expand) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCardViewSheetContainer.getId()) {
            expandBottomSheet(false);
            if (null != mCurrentSelectedData) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ProviderProfileActivity.KEY_PLOYEE_USER_SERVICE_DATA, mCurrentSelectedData);
                bundle.putParcelable(ProviderProfileActivity.KEY_PARENT_DATA, mServiceData);
                IntentUtils.startActivity(getActivity(), ProviderProfileActivity.class, bundle);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
    }

    public void onSuggestionClick(String s) {
        if (mDatas != null && !mDatas.isEmpty()) {
            for (PloyerPersonMapViewModel data : mDatas) {
                if (null != data.getData()) {
                    String name = data.getData().getFullName();
                    if (name.contains(s)) {
                        onClickMarker(data.getData());
                    }
                }
            }
        }
    }
}
