package com.nos.ploy.flow.ployer.provider;

import android.animation.LayoutTransition;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.PloyeeServiceDetailSubServiceRecyclerAdapter;
import com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel.PloyeeServiceDetailSubServiceItemBaseViewModel;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 10/1/2560.
 */

public class ProviderServiceRecyclerAdapter extends RecyclerView.Adapter<ProviderServiceRecyclerAdapter.ViewHolder> {

    private List<PloyerServiceDetailGson.Data> mDatas = new ArrayList<>();
    private LanguageAppLabelGson.Data mLanguage;
    private PloyerServicesGson.Data mParentData;
    private RecyclerView mRecyclerView;


    public ProviderServiceRecyclerAdapter() {
        super();
    }


    public void replaceData(List<PloyerServiceDetailGson.Data> datas) {
        mDatas.clear();
//        for (int i = 0; i < 2; i++) {
        mDatas.addAll(datas);
//        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_member_profile_service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            holder.bindData(mDatas.get(position));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return RecyclerUtils.getSize(mDatas);
    }

    public void setLanguage(LanguageAppLabelGson.Data language) {
        this.mLanguage = language;
    }

    public void expandWithData(PloyerServicesGson.Data parentData) {
        this.mParentData = parentData;
        notifyItemRangeChanged(0, RecyclerUtils.getSize(mDatas));
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.toolbar_member_profile_service_item_title)
        public Toolbar toolbar;
        @BindView(R.id.textview_member_profile_service_item_price)
        public TextView tvPrice;
        @BindView(R.id.textview_member_profile_service_item_description_label)
        public TextView tvDescriptionLabel;
        @BindView(R.id.textview_member_profile_service_item_description)
        public TextView tvDescription;
        @BindView(R.id.textview_member_profile_service_item_certificate_label)
        public TextView tvCertificateLabel;
        @BindView(R.id.textview_member_profile_service_item_certificate)
        public TextView tvCertificate;
        @BindView(R.id.textview_member_profile_service_item_equipment_needed_label)
        public TextView tvEquipmentLabel;
        @BindView(R.id.textview_sub_services_header)
        public TextView tvSubServicesHeader;
        @BindView(R.id.textview_member_profile_service_item_equipment)
        public TextView tvEquipment;
        @BindView(R.id.recyclerview_ployee_service_sub_service)
        public RecyclerView recyclerViewSubService;
        @BindView(R.id.scrollview_member_profile_service_item_container)
        public LinearLayout scrollview;
        @BindView(R.id.linearlayout_member_profile_service_item_container)
        public LinearLayout rootView;
        @BindDrawable(R.drawable.ic_expand_more_white_24dp)
        Drawable mDrawableMore;
        @BindDrawable(R.drawable.ic_expand_less_white_24dp)
        Drawable mDrawableLess;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 2) {
                @Override
                public boolean isAutoMeasureEnabled() {
                    return true;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (null != recyclerViewSubService.getAdapter()) {
                        @PloyeeServiceDetailSubServiceItemBaseViewModel.ViewType int viewType = recyclerViewSubService.getAdapter().getItemViewType(position);
                        if (viewType == PloyeeServiceDetailSubServiceItemBaseViewModel.ITEM || viewType == PloyeeServiceDetailSubServiceItemBaseViewModel.SPACE_ONE_ELEMENT) {
                            return 1;
                        }
                        return 2;
                    }
                    return 2;
                }
            });
            recyclerViewSubService.setLayoutManager(gridLayoutManager);
        }

        private void bindLanguage() {
            tvDescriptionLabel.setText(mLanguage.serviceScreenDescriptLabel);
            tvCertificateLabel.setText(mLanguage.serviceScreenCertificateLabel);
            tvEquipmentLabel.setText(mLanguage.serviceScreenEquipmentLabel);
            tvSubServicesHeader.setText(mLanguage.serviceScreenHeader);
        }


        public void bindData(PloyerServiceDetailGson.Data data) {

            bindLanguage();
            if (null != data) {
                if (data.getServiceId() == -1) {
                    toolbar.setTitle(data.getServiceNameOthers());
                } else {
                    toolbar.setTitle(data.getServiceName());
                }
                tvPrice.setText(mLanguage.serviceScreenFrom + " " + mLanguage.currencyLabel + data.getPriceMin() + " " + data.getPriceUnit() + "");
                tvDescription.setText(data.getDescription());


                if (!TextUtils.isEmpty(data.getCertificate())) {
                    tvCertificateLabel.setVisibility(View.VISIBLE);
                    tvCertificate.setVisibility(View.VISIBLE);
                    tvCertificate.setText(data.getCertificate());
                } else {
                    tvCertificateLabel.setVisibility(View.GONE);
                    tvCertificate.setVisibility(View.GONE);
                    tvCertificate.setText("-");
                }


                if (!TextUtils.isEmpty(data.getEquipment())) {
                    tvEquipmentLabel.setVisibility(View.VISIBLE);
                    tvEquipment.setVisibility(View.VISIBLE);
                    tvEquipment.setText(data.getEquipment());
                } else {
                    tvEquipmentLabel.setVisibility(View.GONE);
                    tvEquipment.setVisibility(View.GONE);
                    tvEquipment.setText("-");
                }

                PloyeeServiceDetailSubServiceRecyclerAdapter adapter = new PloyeeServiceDetailSubServiceRecyclerAdapter(true, null);
                adapter.replaceData(data.getSubServices());
                recyclerViewSubService.setAdapter(adapter);

                if (data.getServiceId() == -1){
                    tvSubServicesHeader.setVisibility(View.GONE);
                    recyclerViewSubService.setVisibility(View.GONE);
                }else {
                    tvSubServicesHeader.setVisibility(View.VISIBLE);
                    recyclerViewSubService.setVisibility(View.VISIBLE);
                }

                tvPrice.setOnClickListener(this);
                if (null != mParentData && mParentData.getId() == data.getServiceId()) {
                    mParentData = null;
                    expandServiceView();
                }
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == tvPrice.getId()) {
                toggleShowServiceDetail();
            }
        }

        private void expandServiceView() {
            scrollview.setVisibility(View.VISIBLE);
            tvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableLess, null);
//                scrollview.requestFocus();
//                scrollview.fullScroll(View.FOCUS_DOWN);
            rootView.getLayoutTransition().addTransitionListener(new LayoutTransition.TransitionListener() {
                @Override
                public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
//                        viewExpandListener.onServiceExpand(view);
                    scrollview.requestFocus();
                }

                @Override
                public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

                }
            });
        }

        private void toggleShowServiceDetail() {
            if (scrollview.getVisibility() == View.GONE) {
                expandServiceView();

            } else if (scrollview.getVisibility() == View.VISIBLE) {
                scrollview.setVisibility(View.GONE);
                tvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableMore, null);
            }
        }
    }

}
