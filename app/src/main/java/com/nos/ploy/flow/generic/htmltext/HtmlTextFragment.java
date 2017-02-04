package com.nos.ploy.flow.generic.htmltext;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.HtmlAppGson;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.cache.SharePreferenceUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 25/12/2559.
 */

public class HtmlTextFragment extends BaseFragment {
    public static final int NONE = -404;
    public static final int POLICY = 0;
    public static final int TERM_AND_CONDITIONS = 1;
    public static final int LEGAL = 2;
    public static final int FAQ = 3;
    public static final int WHAT_IS_PLOYEE = 4;
    public static final int WHAT_IS_PLOYER = 5;
    private static final String KEY_MENU = "MENU";
    @BindView(R.id.textview_html_text)
    TextView mTextviewHtml;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.textview_main_appbar_title)
    TextView mTextViewTitle;
    @BindView(R.id.swiperefreshlayout_html_text)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindString(R.string.Privacy_Policy)
    String LPrivacy_Policy;
    @BindString(R.string.Term_and_Conditions)
    String LTerm_and_Conditions;
    @BindString(R.string.FAQ)
    String LFAQ;
    @BindString(R.string.Legal)
    String LLegal;
    @BindString(R.string.What_is_Ployee)
    String LWhat_is_Ployee;
    @BindString(R.string.What_is_Ployer)
    String LWhat_is_Ployer;
    private MasterApi mApi;
    private
    @Menu
    int mMenu;
    private HtmlAppGson mData;
    private RetrofitCallUtils.RetrofitCallback<HtmlAppGson> mCallbackLoadData = new RetrofitCallUtils.RetrofitCallback<HtmlAppGson>() {
        @Override
        public void onDataSuccess(HtmlAppGson data) {
            dismissRefreshing();
            bindData(data);
        }

        @Override
        public void onDataFailure(String failCause) {
            dismissRefreshing();
        }
    };

    public static HtmlTextFragment newInstance(@Menu int menu) {

        Bundle args = new Bundle();
        args.putInt(KEY_MENU, menu);
        HtmlTextFragment fragment = new HtmlTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mMenu = getArguments().getInt(KEY_MENU);
        }
        mApi = getRetrofit().create(MasterApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_html_text, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView();
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

        if (mData == null) {
            refreshData();
        }
    }

    private void bindData(final HtmlAppGson data) {
        if (null != data && null != data.getData()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextviewHtml.setText(Html.fromHtml(getTextString(data.getData())));
                }
            });

        }
    }

    private void refreshData() {
        showRefreshing();
        RetrofitCallUtils.with(mApi.getHtmlApp(SharePreferenceUtils.getCurrentActiveAppLanguageCode(getContext()), mMenu), mCallbackLoadData).enqueue(getContext());
    }

    private void initToolbar() {
        mTextViewTitle.setText(getTitleString());
        enableBackButton(mToolbar);
    }

    private String getTextString(HtmlAppGson.Data data) {
        if (null != data) {
            switch (mMenu) {
                case FAQ:
                    return data.getFaq();
                case LEGAL:
                    return data.getLegal();
                case POLICY:
                    return data.getPolicy();
                case TERM_AND_CONDITIONS:
                    return data.getTerm();
                case WHAT_IS_PLOYEE:
                    return data.getWhatIsPloyee();
                case WHAT_IS_PLOYER:
                    return data.getWhatIsPloyer();
                case NONE:
                default:
                    return "";
            }

        } else {
            return "";
        }
    }

    private String getTitleString() {
        switch (mMenu) {
            case FAQ:
                return LFAQ;
            case LEGAL:
                return LLegal;
            case POLICY:
                return LPrivacy_Policy;
            case TERM_AND_CONDITIONS:
                return LTerm_and_Conditions;
            case WHAT_IS_PLOYEE:
                return LWhat_is_Ployee;
            case WHAT_IS_PLOYER:
                return LWhat_is_Ployer;
            case NONE:
            default:
                return "";
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, POLICY, TERM_AND_CONDITIONS, LEGAL, FAQ, WHAT_IS_PLOYER, WHAT_IS_PLOYEE})
    public @interface Menu {
    }
}
