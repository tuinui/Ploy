package com.nos.ploy.flow.ployee.home.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appyvet.rangebar.RangeBar;
import com.nos.ploy.R;
import com.nos.ploy.base.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 19/11/2559.
 */

public class PloyeeServiceFragment extends BaseFragment {


    @BindView(R.id.rangebar_ployee_service_rate)
    RangeBar mRangeBar;
    @BindView(R.id.edittext_ployee_service_price_from)
    MaterialEditText mEditTextPriceFrom;
    @BindView(R.id.edittext_ployee_service_price_to)
    MaterialEditText mEditTextPriceTo;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    public static PloyeeServiceFragment newInstance() {
        Bundle args = new Bundle();
        PloyeeServiceFragment fragment = new PloyeeServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ployee_service, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRangeBar();
    }

    private void initToolbar() {
        enableBackButton(mToolbar);
        mToolbar.setTitle(R.string.Aide_au_senior);
        mToolbar.setSubtitle(R.string.Service);
        mToolbar.inflateMenu(R.menu.menu_done);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done_item_done) {
                    showToast("Done");
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void setPriceText(MaterialEditText et, String price) {
        et.setText("$ " + price);
    }

    private void initRangeBar() {
        setPriceText(mEditTextPriceFrom, mRangeBar.getLeftPinValue());
        setPriceText(mEditTextPriceTo, mRangeBar.getRightPinValue());
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                setPriceText(mEditTextPriceFrom, leftPinValue);
                setPriceText(mEditTextPriceTo, rightPinValue);
            }
        });
    }


}
