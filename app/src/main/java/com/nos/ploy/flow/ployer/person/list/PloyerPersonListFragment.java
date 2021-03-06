package com.nos.ploy.flow.ployer.person.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployer.person.list.view.PloyerPersonListRecyclerAdapter;
import com.nos.ploy.flow.ployer.provider.ProviderProfileActivity;
import com.nos.ploy.utils.IntentUtils;
import com.nos.ploy.utils.MyLocationUtils;
import com.nos.ploy.utils.RatingBarUtils;
import com.nos.ploy.utils.URLHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.empty_view)
    TextView emptyView;

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
            Glide.with(holder.imgPhoto.getContext()).load(URLHelper.changURLEndpoint(data.getImagePath())).error(R.drawable.ic_circle_profile_120dp).into(holder.imgPhoto);
            holder.tvTitle.setText(data.getFullName());


            if (TextUtils.isEmpty(data.getOther())){
                holder.tvSubTitle.setText(mServiceData.getServiceName());
            }else{
                holder.tvSubTitle.setText(data.getOther());
            }

            holder.tvDescription.setText(data.getDescription());

            holder.tvPrice.setText( labelFrom + " " + mLanguageData.currencyLabel + " " +  data.getMinPrice()   + mServiceData.getPriceUnit());


            holder.tvReviewCount.setText("" + data.getReviewCount());
            holder.tvRate.setText(data.getReviewPoint() + "/5");
            holder.ratingBar.setRating(RatingBarUtils.getRatingbarRoundingNumber(data.getReviewPoint()));
            if (data.getDistance() > 0 && data.getDistance() < 1000) {

                holder.tvDistance.setText(data.getDistance() + " km.");
            }
            else if(data.getDistance() == 0){
                holder.tvDistance.setText("<100 m.");
            }else{
                holder.tvDistance.setText("-");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Bundle bundle = new Bundle();
                                                       bundle.putParcelable(ProviderProfileActivity.KEY_PLOYEE_USER_SERVICE_DATA, data);
                                                       bundle.putParcelable(ProviderProfileActivity.KEY_PARENT_DATA,mServiceData);
                                                       IntentUtils.startActivity(v.getContext(), ProviderProfileActivity.class, bundle);
                                                   }
                                               }
            );
        }
    };

    private GoogleApiClient mGoogleApiClient;
    private List<ProviderUserListGson.Data.UserService> mDatas = new ArrayList<>();
    private OnFragmentInteractionListener listener;
    private String labelFrom = "From";


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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initView();



    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);

        emptyView.setText(data.filtersscreenResultNotFound);
        labelFrom = data.labelFrom;

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
            if(data.getUserServiceList().size() == 0){
                emptyView.setVisibility(View.VISIBLE);
            }else {
                emptyView.setVisibility(View.GONE);
            }
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

    public List<ProviderUserListGson.Data.UserService> getDatas(){
        return mDatas;
    }

    private void initRecyclerView() {
        mAdapter = new PloyerPersonListRecyclerAdapter(mOnDataBindListener);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);
        getListener().onRecyclerViewCreated(mRecyclerView,glm);

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
        public void onRecyclerViewCreated(RecyclerView recyclerView,GridLayoutManager layoutManager);
        public void onRefreshData();
    }
}
