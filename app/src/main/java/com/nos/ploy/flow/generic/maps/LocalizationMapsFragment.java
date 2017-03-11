package com.nos.ploy.flow.generic.maps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.FragmentTransactionUtils;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.PopupMenuUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 3/1/2560.
 */

public class LocalizationMapsFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {

    private static String KEY_CAN_CHANGE_LOCATION = "CAN_CHANGE_LOCATION";
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewToolbarTitle;

//    @BindView(R.id.fab_ployee_maps_direction)
//    FloatingActionButton mFabDirection;
    @BindView(R.id.fab_ployee_maps_my_location)
    FloatingActionButton mFabMyLocation;

    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private GoogleMap mGoogleMap;
    private LatLng mLatlng;
    private static final String KEY_LAT_LNG = "LAT_LNG";
    private static final String TAG = "LocalizationMapsFragment";
    private OnChooseLocationFinishListener listener;
    private MarkerOptions mMarkerOptions;
    private boolean mCanChangeLocation;

    public static LocalizationMapsFragment newInstance(LatLng latlng, OnChooseLocationFinishListener listener) {
        return newInstance(latlng, true, listener);
    }

    public static LocalizationMapsFragment newInstance(LatLng latlng, boolean canChangeLocation, OnChooseLocationFinishListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_LAT_LNG, latlng);
        args.putBoolean(KEY_CAN_CHANGE_LOCATION, canChangeLocation);
        LocalizationMapsFragment fragment = new LocalizationMapsFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(),R.id.menu_done_item_done,data.doneLabel);
        mTextViewToolbarTitle.setText(data.profileScreenLocalization);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mLatlng = getArguments().getParcelable(KEY_LAT_LNG);
            mCanChangeLocation = getArguments().getBoolean(KEY_CAN_CHANGE_LOCATION, true);
            if (mLatlng == null) {
                mLatlng = new LatLng(0, 0);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_maps, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        initToolbar();
        initView();
    }

    private void initView() {
        mFabMyLocation.setOnClickListener(this);
//        mFabDirection.setOnClickListener(this);
    }

    private void initToolbar() {
        mTextViewToolbarTitle.setText(R.string.Localization);
        enableBackButton(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_done_item_done) {
                    getListener().onFinishChoosingLocation(mLatlng);
                    dismiss();
                }
                return false;
            }
        });
    }

    private void initMap() {
        FragmentTransactionUtils.addFragmentToActivity(getChildFragmentManager(), mMapFragment, R.id.framelayout_ployee_maps_container);
        if (null != mMapFragment) {
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            setCurrentLatLng(mLatlng);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {

                    if (null != mGoogleMap && null != mGoogleMap.getMyLocation()) {
                        LatLng latLng = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                        onChangeLocation(latLng);
                    }


                    return false;
                }
            });
            // Setting a click event handler for the map
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    onChangeLocation(latLng);
                }
            });
        }
    }

    private void onChangeLocation(LatLng latLng) {
        if (mCanChangeLocation) {
            setCurrentLatLng(latLng);
        } else {
            animateCameraTo(latLng);
        }
    }

    private void setCurrentLatLng(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        mLatlng = latLng;
        setMarker(latLng);
    }

    private void setMarker(LatLng latLng) {
        if (null != mGoogleMap) {
            mGoogleMap.clear();
            mMarkerOptions = new MarkerOptions().position(latLng);
            mGoogleMap.addMarker(mMarkerOptions);
            animateCameraTo(latLng);
        }
    }

    private void animateCameraTo(LatLng latLng) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    public void setListener(OnChooseLocationFinishListener listener) {
        this.listener = listener;
    }

    public OnChooseLocationFinishListener getListener() {
        return listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mFabMyLocation.getId()) {
            goToMyLocation();
        }
//        else if (id == mFabDirection.getId()) {
//            getDirection(v.getContext());
//        }
    }


//    private void getDirection(Context context) {
//        if (null != mGoogleMap && mGoogleMap.getMyLocation() != null) {
//            IntentUtils.getDirection(context, new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude()), mLatlng);
//        }
//
//    }


    private void goToMyLocation() {
        if (null != mGoogleMap && mGoogleMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
            Location location = mGoogleMap.getMyLocation();
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            onChangeLocation(new LatLng(lat, lng));
        }

    }

    public static interface OnChooseLocationFinishListener {
        public void onFinishChoosingLocation(LatLng latLng);
    }
}
