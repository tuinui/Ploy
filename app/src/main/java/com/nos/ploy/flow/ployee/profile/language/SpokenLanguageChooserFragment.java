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
import com.nos.ploy.api.account.model.PloyeeProfileGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.masterdata.model.LanguageGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.PopupMenuUtils;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

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
    private ArrayList<String> mSpokenCheckedCodes = new ArrayList<>();
    private OnDataChangedListener listener;
    private MasterApi mApi;
    private List<PloyeeProfileGson.Data.Language> mDatas = new ArrayList<>();
    boolean isContentChanged = false;
    private LanguageChooserRecyclerAdapter mAdapter = new LanguageChooserRecyclerAdapter() {
        @Override
        public void onBindViewHolder(final LanguageChooserRecyclerAdapter.ViewHolder holder, int position) {
            if (RecyclerUtils.isAvailableData(mDatas, position)) {
                PloyeeProfileGson.Data.Language language = mDatas.get(position);
                holder.radio.setText(language.getSpokenLanguageValue());
                holder.radio.setChecked(isLanguageSupported(language.getSpokenLanguageCode()));
                holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (RecyclerUtils.isAvailableData(mDatas, holder.getAdapterPosition())) {

                            PloyeeProfileGson.Data.Language language = mDatas.get(holder.getAdapterPosition());
                            if (isChecked) {
                                if (!mSpokenCheckedCodes.contains(language.getSpokenLanguageCode())) {
                                    mSpokenCheckedCodes.add(language.getSpokenLanguageCode());
                                    isContentChanged = true;
                                }
                            } else {
                                if (mSpokenCheckedCodes.contains(language.getSpokenLanguageCode())) {
                                    mSpokenCheckedCodes.remove(language.getSpokenLanguageCode());
                                    isContentChanged = true;
                                }
                            }

                        }

                    }
                });
            }
        }

        private boolean isLanguageSupported(String languageCode) {
            return null != mSpokenCheckedCodes && !mSpokenCheckedCodes.isEmpty() && mSpokenCheckedCodes.contains(languageCode);
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

            ArrayList<String> codes = getArguments().getStringArrayList(KEY_SPOKEN_LANGUAGE_DATA);
            if (RecyclerUtils.getSize(codes) > 0) {
                mSpokenCheckedCodes.clear();
                mSpokenCheckedCodes.addAll(codes);
            }

        }
        mApi = getRetrofit().create(MasterApi.class);
    }


    @Override
    public boolean onBackPressed() {
        if (isContentChanged) {
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
        } else {
            dismiss();
            return false;
        }
    }


    @Override
    protected void bindLanguage(LanguageAppLabelGson.Data data) {
        super.bindLanguage(data);
        PopupMenuUtils.setMenuTitle(mToolbar.getMenu(), R.id.menu_done_item_done, data.doneLabel);
        mTextViewTitle.setText(data.profileScreenLanguage);
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

    private void initToolbar() {
        mTextViewTitle.setText(R.string.Language_spoken);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    getListener().onClickDone(mSpokenCheckedCodes);
                    dismiss();
                }
                return false;
            }
        });
        enableBackButton(mToolbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
