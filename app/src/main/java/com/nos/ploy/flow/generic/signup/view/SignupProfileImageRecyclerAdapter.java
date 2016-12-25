package com.nos.ploy.flow.generic.signup.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nos.ploy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saran on 18/12/2559.
 */

public abstract class SignupProfileImageRecyclerAdapter extends RecyclerView.Adapter<SignupProfileImageRecyclerAdapter.ViewHolder> {
    @Override
    public SignupProfileImageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_signup_detail_profile_image, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_generic_profile_image_160dp)
        public ImageView imgProfile;
        @BindView(R.id.fab_detail_profile_image_cancel)
        public FloatingActionButton fabCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
