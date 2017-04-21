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
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.PopupMenuUtils;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import rx.functions.Action1;

/**
 * Created by Saran on 3/1/2560.
 */

public class LocalizationMapsFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {

    private static String KEY_IS_PLOYEE_PROFILE = "IS_PLOYEE_PROFILE";
    private static final String KEY_NEVER_SET_LOCATION_BEFORE = "NEVER_SET_LOCATION_BEFORE";
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewToolbarTitle;

    @BindView(R.id.fab_ployee_maps_my_location)
    FloatingActionButton mFabMyLocation;
    @BindView(R.id.fab_ployee_maps_direction)
    FloatingActionButton mFabDirection;

    @BindDimen(R.dimen.fab_margin)
    int dp16;

    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private GoogleMap mGoogleMap;
    private LatLng mLatlng;
    private static final String KEY_LAT_LNG = "LAT_LNG";
    private static final String TAG = "LocalizationMapsFragment";
    private OnChooseLocationFinishListener listener;
    private MarkerOptions mMarkerOptions;
    private boolean mIsPloyeeProfile;
    private boolean isContentChanged = false;
    private boolean mIsNeverSetLocationBefore;
    //

    public static LocalizationMapsFragment newInstance(LatLng latlng, boolean isPloyeeProfile, boolean neverSetLocationBefore, OnChooseLocationFinishListener listener) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_LAT_LNG, latlng);
        args.putBoolean(KEY_IS_PLOYEE_PROFILE, isPloyeeProfile);
        args.putBoolean(KEY_NEVER_SET_LOCATION_BEFORE, neverSetLocationBefore);
        LocalizationMapsFragment fragment = new LocalizationMapsFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, data.doneLabel);
        mTextViewToolbarTitle.setText(data.profileScreenLocalization);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mLatlng = getArguments().getParcelable(KEY_LAT_LNG);
            mIsPloyeeProfile = getArguments().getBoolean(KEY_IS_PLOYEE_PROFILE, true);
            mIsNeverSetLocationBefore = getArguments().getBoolean(KEY_NEVER_SET_LOCATION_BEFORE, false);
            if (mLatlng == null) {
                mLatlng = MyLocationUtils.DEFAULT_LATLNG;
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

    @Override
    public void onResume() {
        super.onResume();
        if (MyLocationUtils.checkLocationEnabled(getContext()) && mIsNeverSetLocationBefore) {
            setToCurrentGpsLatLng();
        }
    }

    private void initView() {
        if (mIsPloyeeProfile) {
            mFabDirection.setVisibility(View.GONE);
            mFabMyLocation.setVisibility(View.GONE);
        } else {
            mFabMyLocation.setVisibility(View.VISIBLE);
            mFabDirection.setVisibility(View.VISIBLE);
            mFabMyLocation.setOnClickListener(this);
            mFabDirection.setOnClickListener(this);
        }

    }

    private void initToolbar() {
        mTextViewToolbarTitle.setText(R.string.Localization);
        enableBackButton(mToolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onBackPressed()) {
                    dismiss();
                }
            }
        });

        if (mIsPloyeeProfile) {

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
    }

    @Override
    public boolean onBackPressed() {
        if (mIsPloyeeProfile && isContentChanged) {
            PopupMenuUtils.showConfirmationAlertMenu(getContext(), null, mLanguageData.accountScreenConfirmBeforeBack, mLanguageData.okLabel, mLanguageData.cancelLabel, new Action1<Boolean>() {
                @Override
                public void call(Boolean yes) {
                    if (yes) {
                        isContentChanged = false;
                        dismiss();
                    }
                }
            });
            return true;
        }
        return super.onBackPressed();

    }

    private void initMap() {
        FragmentTransactionUtils.addFragmentToActivity(getChildFragmentManager(), mMapFragment, R.id.framelayout_ployee_maps_container);
        if (null != mMapFragment) {
            mMapFragment.getMapAsync(this);
        }
    }

    private void setToCurrentGpsLatLng() {
        SmartLocation.with(getContext()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                isContentChanged = true;
                setCurrentLatLng(new LatLng(location.getLatitude(), location.getLongitude()), true);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            final boolean setMarker = !mIsNeverSetLocationBefore || MyLocationUtils.locationProviderEnabled(getContext());
            if (mIsNeverSetLocationBefore && MyLocationUtils.locationProviderEnabled(getContext())) {
                setToCurrentGpsLatLng();
            } else {
                setCurrentLatLng(mLatlng, setMarker);
            }


            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
//                    if (MyLocationUtils.checkLocationEnabled(getContext())) {
                    if (null != mGoogleMap && null != mGoogleMap.getMyLocation()) {

                        LatLng latLng = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                        onChangeLocation(latLng);
                        isContentChanged = true;
                    }
//                    }
                    return false;
                }
            });
            // Setting a click event handler for the map
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    isContentChanged = true;
                    onChangeLocation(latLng);
                }
            });
        }
    }

    private void onChangeLocation(LatLng latLng) {
        if (mIsPloyeeProfile) {
            setCurrentLatLng(latLng, true);
        } else {
            animateCameraTo(latLng);
        }
    }

    private void setCurrentLatLng(LatLng latLng, boolean setMarker) {
        if (latLng == null) {
            return;
        }
        mLatlng = latLng;
        if (setMarker) {
            setMarker(latLng);
        } else {
            animateCameraTo(latLng);
        }

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
            goToCurrentMarkerLocation();
        } else if (id == mFabDirection.getId()) {
            getDirection(v.getContext());
        }
    }


    private void getDirection(Context context) {
        if (MyLocationUtils.checkLocationEnabled(getContext()) && null != mGoogleMap && mGoogleMap.getMyLocation() != null) {
            IntentUtils.getDirection(context, new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude()), mLatlng);
        }

    }


    private void goToCurrentMarkerLocation() {
        if (mMarkerOptions != null && null != mMarkerOptions.getPosition()) {
            LatLng latLng = mMarkerOptions.getPosition();
            onChangeLocation(latLng);
        } else if (null != mGoogleMap && mGoogleMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
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
