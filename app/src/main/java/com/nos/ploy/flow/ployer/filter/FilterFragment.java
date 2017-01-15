package com.nos.ploy.flow.ployer.filter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.account.model.PloyeeProfileGson;
import com.nos.ploy.api.account.model.TransportGson;
import com.nos.ploy.api.account.model.TransportGsonVm;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PostProviderFilterGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.custom.view.InputFilterMinMax;
import com.nos.ploy.custom.view.NumberTextWatcher;
import com.nos.ploy.flow.ployee.profile.TransportRecyclerAdapter;
import com.nos.ploy.flow.ployer.filter.availability.FilterAvailabilityFragment;
import com.nos.ploy.flow.ployer.filter.language.FilterLanguageFragment;
import com.nos.ploy.flow.ployer.filter.services.FilterServicesFragment;
import com.nos.ploy.flow.ployer.filter.view.FilterRatingRecyclerAdapter;
import com.nos.ploy.utils.RecyclerUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Saran on 14/1/2560.
 */

public class FilterFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.textview_filter_language)
    TextView mTextViewLanguage;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubTitle;
    @BindView(R.id.textview_filter_availability)
    TextView mTextViewAvailability;
    @BindView(R.id.textview_filter_services)
    TextView mTextViewServices;
    @BindView(R.id.button_filter_phone)
    Button mButtonPhone;
    @BindView(R.id.button_filter_email)
    Button mButtonEmail;
    @BindView(R.id.edittext_filter_to)
    MaterialEditText mEditTextTo;
    @BindView(R.id.edittext_filter_from)
    MaterialEditText mEditTextFrom;
    @BindView(R.id.recyclerview_filter_rating)
    RecyclerView mRecyclerViewRating;
    @BindView(R.id.recyclerview_filter_transportation)
    RecyclerView mRecyclerViewTransportation;
    @BindView(R.id.button_filter_clear)
    Button mButtonClear;
    @BindView(R.id.button_filter_filter)
    Button mButtonFilter;


    private PostProviderFilterGson mPostData = new PostProviderFilterGson();
    private FilterRatingRecyclerAdapter mRatingAdapter = new FilterRatingRecyclerAdapter(new Action1<Long>() {
        @Override
        public void call(Long aLong) {
            mPostData.setReview(aLong);
        }
    });
    private TransportRecyclerAdapter mTransportAdapter = new TransportRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final TransportRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mTransportVms, position)) {
                TransportGsonVm data = mTransportVms.get(position);
                holder.imgTransport.setImageResource(data.getDrawable());
                holder.tvTitle.setText(data.getTitle());
                holder.imgTransport.setActivated(data.isCheck());
                holder.imgTransport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RecyclerUtils.isAvailableData(mTransportVms, holder.getAdapterPosition())) {
                            TransportGsonVm data = mTransportVms.get(holder.getAdapterPosition());
                            boolean newValue = !data.isCheck();
                            data.setCheck(newValue);
                            if(newValue){
                                if(!mPostData.getTransportIds().contains(data.getId())){
                                    mPostData.getTransportIds().add(data.getId());
                                }
                            }else{
                                if(mPostData.getTransportIds().contains(data.getId())){
                                    mPostData.getTransportIds().remove(data.getId());
                                }
                            }
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return RecyclerUtils.getSize(mTransportVms);
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
        public void onDataFailure(ResponseMessage failCause) {
            dismissLoading();
        }
    };


    private MasterApi mMasterApi;
    private List<TransportGsonVm> mTransportVms = new ArrayList<>();
    private static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private static final String KEY_POST_DATA = "POST_DATA";
    private PloyerServicesGson.Data mData;
    private PloyerApi mPloyerApi;
    private OnFilterConfirmListener listener;

    public static FilterFragment newInstance(PloyerServicesGson.Data data, PostProviderFilterGson postData, OnFilterConfirmListener listener) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        args.putParcelable(KEY_POST_DATA, postData);
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mData = getArguments().getParcelable(KEY_SERVICE_DATA);
            if (null != getArguments().getParcelable(KEY_POST_DATA)) {
                mPostData = getArguments().getParcelable(KEY_POST_DATA);
            }

            if (mData != null) {
                mPostData.setServiceId(mData.getId());
            }

        }
        mMasterApi = getRetrofit().create(MasterApi.class);
        mPloyerApi = getRetrofit().create(PloyerApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        initRecyclerView();
        bindData(mPostData);
    }

    private void initToolbar() {
        if (null != mData) {
            mTextViewTitle.setText(mData.getServiceName());
            mTextViewSubTitle.setText(mData.getPloyeeCountDisplay());
        }
        enableBackButton(mToolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTransportVms.isEmpty()) {
            requestTransportData();
        }
    }

    private void requestTransportData() {
        showLoading();
        RetrofitCallUtils
                .with(mMasterApi.getTransportList(), mCallbackTransport)
                .enqueue(getActivity());
    }

    private void bindTransportData(List<PloyeeProfileGson.Data.Transport> data) {
        mTransportVms.clear();
        mTransportAdapter.notifyDataSetChanged();
        mTransportVms.addAll(toVm(data));
        mTransportAdapter.notifyItemRangeChanged(0, mTransportAdapter.getItemCount());
    }

    private List<TransportGsonVm> toVm(List<PloyeeProfileGson.Data.Transport> datas) {
        List<TransportGsonVm> vms = new ArrayList<>();
        for (PloyeeProfileGson.Data.Transport data : datas) {
            vms.add(new TransportGsonVm(data.getTransportId(), data.getTransportName(), toTransportIcon(data.getTransportId())));
        }

        for (TransportGsonVm vm : vms) {
            if (mPostData != null && mPostData.getTransportIds() != null) {
                for (Long checkedId : mPostData.getTransportIds()) {
                    if (checkedId == vm.getId()) {
                        vm.setCheck(true);

                    }
                }
            }
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


    private void initRecyclerView() {
        mRecyclerViewRating.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewRating.setAdapter(mRatingAdapter);

        mRecyclerViewTransportation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTransportation.setAdapter(mTransportAdapter);
    }


    private void bindData(PostProviderFilterGson data) {
        mPostData = data;
        if (null != data) {
            mEditTextFrom.setText("" + data.getPriceMin());
            mEditTextTo.setText("" + data.getPriceMax());
            mButtonEmail.setActivated(data.isContactEmail());
            mButtonPhone.setActivated(data.isContactPhone());
            mRatingAdapter.replaceData(data.getReview());
            for (TransportGsonVm transportVm : mTransportVms) {
                if (data.getTransportIds().contains(transportVm.getId())) {
                    transportVm.setCheck(true);
                } else {
                    transportVm.setCheck(false);
                }
            }

            mTransportAdapter.notifyItemRangeChanged(0, mTransportAdapter.getItemCount());
        }
    }


    private void initView() {
        disableEditable(mEditTextFrom);
        disableEditable(mEditTextTo);
        mEditTextFrom.setOnClickListener(this);
        mEditTextFrom.addTextChangedListener(new NumberTextWatcher(mEditTextFrom));
//        mEditTextPriceFrom.setFilters(mInputMinMaxFilter);

        mEditTextTo.setOnClickListener(this);
        mEditTextTo.addTextChangedListener(new NumberTextWatcher(mEditTextTo));
        mTextViewLanguage.setOnClickListener(this);
        mTextViewAvailability.setOnClickListener(this);
        mTextViewServices.setOnClickListener(this);
        mButtonEmail.setOnClickListener(this);
        mButtonPhone.setOnClickListener(this);
        mButtonClear.setOnClickListener(this);
        mButtonFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mTextViewLanguage.getId()) {
            showFragment(getLanguageFragment());
        } else if (id == mTextViewAvailability.getId()) {
            showFragment(getAvailabilityFragment());
        } else if (id == mTextViewServices.getId()) {
            showFragment(getServicesFragment());
        } else if (id == mButtonPhone.getId()) {
            mPostData.setContactPhone(!mButtonPhone.isActivated());
            bindData(mPostData);
        } else if (id == mButtonEmail.getId()) {
            mPostData.setContactEmail(!mButtonEmail.isActivated());
            bindData(mPostData);
        } else if (id == mButtonClear.getId()) {
            onClickClear();
        } else if (id == mButtonFilter.getId()) {
            attemptRequestPostFilter();
        } else if (id == mEditTextFrom.getId()) {
            showPopupAlertEditTextMenu(mEditTextFrom.getContext(), String.valueOf(mEditTextFrom.getHint()), String.valueOf(mEditTextFrom.getText()), new Action1<String>() {
                @Override
                public void call(String s) {
                    mPostData.setPriceMin(extractLong(s));
                    bindData(mPostData);
                }
            });
        } else if (id == mEditTextTo.getId()) {
            showPopupAlertEditTextMenu(mEditTextTo.getContext(), String.valueOf(mEditTextTo.getHint()), String.valueOf(mEditTextTo.getText()), new Action1<String>() {
                @Override
                public void call(String s) {
                    mPostData.setPriceMax(extractLong(s));
                    bindData(mPostData);
                }
            });
        }
    }

    private void attemptRequestPostFilter() {
        if (null != mTransportVms && !mTransportVms.isEmpty()) {
            for (TransportGsonVm vm : mTransportVms) {
                if (vm.isCheck()) {
                    mPostData.getTransportIds().add(vm.getId());
                } else {
                    if (mPostData.getTransportIds().contains(vm.getId())) {
                        mPostData.getTransportIds().remove(vm.getId());
                    }
                }
            }
        }
        getListener().onFilterConfirm(mPostData);
        dismiss();
    }

    private void showPopupAlertEditTextMenu(Context context, String title, String defaultValue, final Action1<String> onConfirm) {
        if (null == context) {
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_edittext_in_alert_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.edittext_in_alert_dialog);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (!TextUtils.isEmpty(defaultValue) && defaultValue.contains(",")) {
            defaultValue = String.valueOf(defaultValue.replaceAll(",", ""));
        }
        editText.setText(defaultValue);
        editText.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.MAX_VALUE, editText)});

        alert.setView(view);
        alert.setTitle(title);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (onConfirm != null) {
                    onConfirm.call(editText.getText().toString());
                }

            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        if (null != dialog) {
            dialog.show();
        }
    }

    private void onClickClear() {
        bindData(new PostProviderFilterGson());
    }

    private FilterLanguageFragment getLanguageFragment() {

        return FilterLanguageFragment.newInstance(mData, mPostData.cloneThis().getLanguages(), new FilterLanguageFragment.OnDataChangedListener() {
            @Override
            public void onClickDone(ArrayList<String> datas) {
                mPostData.setLanguages(datas);
                bindData(mPostData);
            }
        });
    }

    private FilterServicesFragment getServicesFragment() {
        return FilterServicesFragment.newInstance(mData, mPostData.cloneThis(), new FilterServicesFragment.OnClickDoneListener() {
            @Override
            public void onClickDone(List<Long> subServiceLv2Ids, boolean certificate, boolean equipment) {
                mPostData.setCertificate(certificate);
                mPostData.setEquipment(equipment);
                mPostData.setSubServices(subServiceLv2Ids);
                bindData(mPostData);
            }
        });
    }

    private FilterAvailabilityFragment getAvailabilityFragment() {
        return FilterAvailabilityFragment.newInstance(mData, mPostData.cloneThis().getAvailabilityItems(), new FilterAvailabilityFragment.OnClickDoneListener() {
            @Override
            public void onClickDone(PloyeeAvailiabilityGson.Data data) {
                    mPostData.addAllAvailabilityItem(data.getAvailabilityItems());

                bindData(mPostData);
            }
        });
    }

    public void setListener(OnFilterConfirmListener listener) {
        this.listener = listener;
    }

    public OnFilterConfirmListener getListener() {
        return listener;
    }

    public static interface OnFilterConfirmListener {
        public void onFilterConfirm(PostProviderFilterGson data);
    }
}
