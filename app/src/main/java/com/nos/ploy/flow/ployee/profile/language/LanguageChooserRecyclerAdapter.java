package com.nos.ploy.flow.ployee.profile.language;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;
import com.nos.ploy.custom.view.ToggleableRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 25/12/2559.
 */

public abstract class LanguageChooserRecyclerAdapter extends RecyclerView.Adapter<LanguageChooserRecyclerAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_language_chooser_radio_button, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.radiobutton_language_chooser)
        ToggleableRadioButton radio;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
