package com.nos.ploy.flow.ployer.filter.language;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.account.model.PloyeeProfileGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.masterdata.model.LanguageGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.flow.ployee.profile.language.LanguageChooserRecyclerAdapter;
import com.nos.ploy.utils.PopupMenuUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 14/1/2560.
 */

public class FilterLanguageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.textview_main_appbar_subtitle)
    TextView mTextViewSubTitle;
    @BindView(R.id.recyclerview_filter_language)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_filter_language_no_preferences)
    Button mButtonNoPref;
    @BindView(R.id.swiperefreshlayout_filter_language)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> mCheckedLanguages = new ArrayList<>();
    private LanguageChooserRecyclerAdapter mAdapter = new LanguageChooserRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final LanguageChooserRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mDatas, position)) {
                PloyeeProfileGson.Data.Language language = mDatas.get(position);
                holder.radio.setText(language.getSpokenLanguageValue());
                holder.radio.setChecked(isLanguageChecked(language.getSpokenLanguageCode()));
                holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (RecyclerUtils.isAvailableData(mDatas, holder.getAdapterPosition())) {
                            PloyeeProfileGson.Data.Language language = mDatas.get(holder.getAdapterPosition());
                            if (isChecked) {
                                if (!mCheckedLanguages.contains(language.getSpokenLanguageCode())) {
                                    mCheckedLanguages.add(language.getSpokenLanguageCode());
                                }
                            } else {
                                if (mCheckedLanguages.contains(language.getSpokenLanguageCode())) {
                                    mCheckedLanguages.remove(language.getSpokenLanguageCode());
                                }
                            }

                        }

                        if (null != mOnDataChangedListener) {
                            mOnDataChangedListener.onClickDone(mCheckedLanguages);
                        }

                    }
                });
            }
        }

        private boolean isLanguageChecked(String languageCode) {
            return null != mCheckedLanguages && !mCheckedLanguages.isEmpty() && mCheckedLanguages.contains(languageCode);
        }

        @Override
        public int getItemCount() {
            return RecyclerUtils.getSize(mDatas);
        }
    };
    private RetrofitCallUtils.RetrofitCallback<LanguageGson> mCallbackLoadLanguage = new RetrofitCallUtils.RetrofitCallback<LanguageGson>() {
        @Override
        public void onDataSuccess(LanguageGson data) {
            dismissRefreshing();
            if (null != data && null != data.getData()) {
                bindData(data.getData());
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };

    private static final String KEY_SERVICE_DATA = "SERVICE_DATA";
    private static final String KEY_LANGUAGES = "LANGUAGES";
    private static final String KEY_TOTAL_COUNT = "TOTAL_COUNT";
    private PloyerServicesGson.Data mServiceData;
    private MasterApi mApi;
    private List<PloyeeProfileGson.Data.Language> mDatas = new ArrayList<>();
    private OnDataChangedListener mOnDataChangedListener;
    private long mTotal;
    private String strProvidersLabel = "";

    public static FilterLanguageFragment newInstance(PloyerServicesGson.Data data, long total, ArrayList<String> languages, OnDataChangedListener onDataChangedListener) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_SERVICE_DATA, data);
        args.putStringArrayList(KEY_LANGUAGES, languages);
        args.putLong(KEY_TOTAL_COUNT, total);
        FilterLanguageFragment fragment = new FilterLanguageFragment();
        fragment.setArguments(args);
        fragment.setListener(onDataChangedListener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mServiceData = getArguments().getParcelable(KEY_SERVICE_DATA);
            if (null != getArguments().getStringArrayList(KEY_LANGUAGES)) {
                mCheckedLanguages = getArguments().getStringArrayList(KEY_LANGUAGES);
            }
            mTotal = getArguments().getLong(KEY_TOTAL_COUNT, 0);
        }
        mApi = getRetrofit().create(MasterApi.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mDatas || mDatas.isEmpty()) {
            refreshData();
        }
    }

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getSpokenLanguageList(), mCallbackLoadLanguage).enqueue(getContext());
    }

    private void bindData(List<PloyeeProfileGson.Data.Language> data) {
        if (null != data) {
            mDatas.clear();
            mDatas.addAll(data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_language, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initView() {
        mButtonNoPref.setOnClickListener(this);
    }


    private void initToolbar() {
        mTextViewTitle.setText(R.string.Language_spoken);
        mTextViewSubTitle.setVisibility(View.VISIBLE);
//        mToolbar.inflateMenu(R.menu.menu_done);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.menu_done_item_done) {
//                    if (null != mOnDataChangedListener) {
//                        mOnDataChangedListener.onClickDone(mCheckedLanguages);
//                    }
//
////                    dismiss();
//                }
//                return false;
//            }
//        });
        enableBackButton(mToolbar);
    }

    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, data.doneLabel);
        mTextViewTitle.setText(data.profileScreenLanguageHeader);
        mButtonNoPref.setText(data.avaliabilityScreenNoPrefer);
        mTextViewSubTitle.setText(mTotal + " " + data.providersLabel);

        strProvidersLabel = data.providersLabel;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mButtonNoPref.getId()) {
            mCheckedLanguages.clear();
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());

            if (null != mOnDataChangedListener) {
                mOnDataChangedListener.onClickDone(mCheckedLanguages);
            }
        }
    }

    public void setListener(OnDataChangedListener listener) {
        this.mOnDataChangedListener = listener;
    }

    public void updateCountProviders(long mTotalCount) {
        mTextViewSubTitle.setText(mTotalCount + " " + strProvidersLabel);

    }

    public static interface OnDataChangedListener {
        public void onClickDone(ArrayList<String> datas);
    }

}
