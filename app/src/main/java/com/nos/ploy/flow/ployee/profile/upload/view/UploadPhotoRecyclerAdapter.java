package com.nos.ploy.flow.ployee.profile.upload.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.nos.ploy.R;

/**
 * Created by Saran on 24/12/2559.
 */

public abstract class UploadPhotoRecyclerAdapter extends RecyclerView.Adapter<UploadPhotoRecyclerAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_photo_item, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton imgUpload;

        public ViewHolder(View v) {
            super(v);
            imgUpload = (ImageButton) v.findViewById(R.id.imagebutton_upload_photo_item);
        }
    }
}
