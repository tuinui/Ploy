package com.nos.ploy.flow.generic.maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.FragmentTransactionUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 3/1/2560.
 */

public class PloyeeMapsFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewToolbarTitle;
    @BindString(R.string.My_Location)
    String LMy_Location;

    private SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
    private GoogleMap mGoogleMap;
    private LatLng mLatlng;
    private static final String KEY_LAT_LNG = "LAT_LNG";
    private static final String TAG = "PloyeeMapsFragment";
    private OnChooseLocationFinishListener listener;
    private MarkerOptions mMarkerOptions;

    public static PloyeeMapsFragment newInstance(LatLng latlng, OnChooseLocationFinishListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_LAT_LNG, latlng);
        Log.i(TAG, "before lat : " + latlng.latitude + "   \n lng : " + latlng.longitude);
        PloyeeMapsFragment fragment = new PloyeeMapsFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mLatlng = getArguments().getParcelable(KEY_LAT_LNG);
            Log.i(TAG, "after lat : " + mLatlng.latitude + "   \n lng : " + mLatlng.longitude);
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
    }

    private void initToolbar() {
        mTextViewToolbarTitle.setText(LMy_Location);
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
                    setCurrentLatLng(new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude()));
                    return false;
                }
            });
            // Setting a click event handler for the map
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    setCurrentLatLng(latLng);
                }
            });
        }
    }

    private void setCurrentLatLng(LatLng latLng) {
        mLatlng = latLng;
        setMarker(latLng);
    }
    private void setMarker(LatLng latLng) {
        if (null != mGoogleMap) {
            mGoogleMap.clear();
            mMarkerOptions = new MarkerOptions().position(latLng);
            mGoogleMap.addMarker(mMarkerOptions);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    public void setListener(OnChooseLocationFinishListener listener) {
        this.listener = listener;
    }

    public OnChooseLocationFinishListener getListener() {
        return listener;
    }

    public static interface OnChooseLocationFinishListener {
        public void onFinishChoosingLocation(LatLng latLng);
    }
}
