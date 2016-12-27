package com.nos.ploy.flow.ployee.profile.language;

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
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 25/12/2559.
 */

public class SpokenLanguageChooserFragment extends BaseFragment {
    private static final String KEY_SPOKEN_LANGUAGE_DATA = "LANGUAGE_DATA";
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.recyclerview_language_chooser)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout_language_chooser)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private long mUserId;
    private ArrayList<String> mSpokenCodes = new ArrayList<>();
    private OnDataChangedListener listener;
    private MasterApi mApi;
    private List<ProfileGson.Data.Language> mDatas = new ArrayList<>();
    private LanguageChooserRecyclerAdapter mAdapter = new LanguageChooserRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final LanguageChooserRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mDatas, position)) {
                ProfileGson.Data.Language language = mDatas.get(position);
                holder.radio.setText(language.getSpokenLanguageValue());
                holder.radio.setChecked(isLanguageSupported(language.getSpokenLanguageCode()));
                holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (RecyclerUtils.isAvailableData(mDatas, holder.getAdapterPosition())) {
                            ProfileGson.Data.Language language = mDatas.get(holder.getAdapterPosition());
                            if (isChecked) {
                                if (!mSpokenCodes.contains(language.getSpokenLanguageCode())) {
                                    mSpokenCodes.add(language.getSpokenLanguageCode());
                                }
                            } else {
                                if (mSpokenCodes.contains(language.getSpokenLanguageCode())) {
                                    mSpokenCodes.remove(language.getSpokenLanguageCode());
                                }
                            }

                        }

                    }
                });
            }
        }

        private boolean isLanguageSupported(String languageCode) {
            return null != mSpokenCodes && !mSpokenCodes.isEmpty() && mSpokenCodes.contains(languageCode);
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
        public void onDataFailure(ResponseMessage failCause) {
            dismissRefreshing();
        }
    };

    private boolean mSingleChoiceMode = false;

    public static SpokenLanguageChooserFragment newInstance(long userId, ArrayList<String> datas, OnDataChangedListener listener) {

        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userId);
        args.putStringArrayList(KEY_SPOKEN_LANGUAGE_DATA, datas);
        SpokenLanguageChooserFragment fragment = new SpokenLanguageChooserFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public void setSingleChoiceMode(boolean singleChoiceMode) {
        this.mSingleChoiceMode = singleChoiceMode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUserId = getArguments().getLong(KEY_USER_ID, 0);
            mSpokenCodes = getArguments().getStringArrayList(KEY_SPOKEN_LANGUAGE_DATA);
        }
        mApi = getRetrofit().create(MasterApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_language_chooser, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        initView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        setRefreshLayout(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
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
        RetrofitCallUtils.with(mApi.getLanguageList(), mCallbackLoadLanguage).enqueue(getContext());
    }

    private void bindData(List<ProfileGson.Data.Language> data) {
        if (null != data) {
            mDatas.clear();
            mDatas.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Language_spoken);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    getListener().onClickDone(mSpokenCodes);
                    dismiss();
                }
                return false;
            }
        });
        enableBackButton(mToolbar);
    }

    public OnDataChangedListener getListener() {
        return listener;
    }

    public void setListener(OnDataChangedListener listener) {
        this.listener = listener;
    }

    public static interface OnDataChangedListener {
        public void onClickDone(ArrayList<String> datas);
    }
}
