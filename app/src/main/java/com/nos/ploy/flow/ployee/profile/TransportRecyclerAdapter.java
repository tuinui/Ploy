package com.nos.ploy.flow.ployee.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.R;

/**
 * Created by Saran on 25/12/2559.
 */

public abstract class TransportRecyclerAdapter extends RecyclerView.Adapter<TransportRecyclerAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transport_item, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgTransport;
        public TextView tvTitle;

        public ViewHolder(View v) {
            super(v);
            imgTransport = (ImageView) v.findViewById(R.id.imageview_transport_item);
            tvTitle = (TextView) v.findViewById(R.id.textiew_transport_item_title);
        }
    }
}
