package com.nos.ploy.flow.ployer.person.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.nos.ploy.R;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployer.person.list.view.PloyerPersonListRecyclerAdapter;
import com.nos.ploy.flow.ployer.provider.ProviderProfileActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RatingBarUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public class PloyerPersonListFragment extends BaseFragment implements SearchView.OnQueryTextListener {


    @BindView(R.id.recyclerview_ployer_service_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout_ployer_home_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private PloyerServicesGson.Data mServiceData;
    private PloyerApi mApi;
    private PloyerPersonListRecyclerAdapter mAdapter;
    private Comparator<ProviderUserListGson.Data.UserService> USER_ID_COMPARATOR = new Comparator<ProviderUserListGson.Data.UserService>() {
        @Override
        public int compare(ProviderUserListGson.Data.UserService o1, ProviderUserListGson.Data.UserService o2) {
            return o1.getUserId().compareTo(o2.getUserId());
        }
    };
    private PloyerPersonListRecyclerAdapter.OnDataBindListener mOnDataBindListener = new PloyerPersonListRecyclerAdapter.OnDataBindListener() {
        @Override
        public void onDataBind(PloyerPersonListRecyclerAdapter.ViewHolder holder, final ProviderUserListGson.Data.UserService data) {
            Glide.with(holder.imgPhoto.getContext()).load(data.getImagePath()).error(R.drawable.ic_ployer_item_placeholder).into(holder.imgPhoto);
            holder.tvTitle.setText(data.getFullName());
            holder.tvDescription.setText(data.getDescription());
            holder.tvPrice.setText("$" + data.getMinPrice());
            holder.tvReviewCount.setText("" + data.getReviewCount());
            holder.tvRate.setText(data.getReviewPoint() + "/5");
            holder.ratingBar.setRating(RatingBarUtils.getRatingbarRoundingNumber(data.getReviewPoint()));
            if (null != data.getLocationLat() && null != data.getLocationLng()) {
                holder.tvDistance.setText(MyLocationUtils.getDistanceFromCurrentLocation(holder.tvDistance.getContext(), mGoogleApiClient, new LatLng(data.getLocationLat(), data.getLocationLng())));
            } else {
                holder.tvDistance.setText("-");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Bundle bundle = new Bundle();
                                                       bundle.putParcelable(ProviderProfileActivity.KEY_PLOYEE_USER_SERVICE_DATA, data);
                                                       IntentUtils.startActivity(v.getContext(), ProviderProfileActivity.class, bundle);
                                                   }
                                               }
            );
        }
    };

    private GoogleApiClient mGoogleApiClient;
    private List<ProviderUserListGson.Data.UserService> mDatas = new ArrayList<>();
    private OnFragmentInteractionListener listener;


    public static PloyerPersonListFragment newInstance(PloyerServicesGson.Data data, GoogleApiClient googleApiClient, OnFragmentInteractionListener listener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        PloyerPersonListFragment fragment = new PloyerPersonListFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        fragment.setGoogleApiClient(googleApiClient);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployer_person_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        initRecyclerView();
        initView();
    }


    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListener().onRefreshData();
                dismissRefreshing();
            }
        });
    }

    public void bindData(ProviderUserListGson.Data data) {
        if (null != data && null != data.getUserServiceList()) {
            mDatas.clear();
            mDatas.addAll(data.getUserServiceList());
            if (null != mAdapter) {
//                mAdapter.edit()
//                        .replaceAll(mDatas)
//                        .commit();
                mAdapter.replaceData(mDatas);
            }
        }
    }

    private void initRecyclerView() {
        mAdapter = new PloyerPersonListRecyclerAdapter(mOnDataBindListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<ProviderUserListGson.Data.UserService> filteredModelList = filter(mDatas, query);
        if (null != mAdapter) {
//            mAdapter.edit()
//                    .replaceAll(filteredModelList)
//                    .commit();
            mAdapter.filterList(query);
        }
        if (null != mRecyclerView) {
            mRecyclerView.scrollToPosition(0);
        }

        return true;
    }

    private static List<ProviderUserListGson.Data.UserService> filter(List<ProviderUserListGson.Data.UserService> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ProviderUserListGson.Data.UserService> filteredModelList = new ArrayList<>();
        for (ProviderUserListGson.Data.UserService model : models) {
            final String text = model.getFullName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void setListener(OnFragmentInteractionListener listener) {
        this.listener = listener;
    }

    public OnFragmentInteractionListener getListener() {
        return listener;
    }


    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
    }


    public static interface OnFragmentInteractionListener {

        public void onRefreshData();
    }
}
