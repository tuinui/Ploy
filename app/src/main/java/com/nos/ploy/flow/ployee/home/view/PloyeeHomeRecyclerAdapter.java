package com.nos.ploy.flow.ployee.home.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;

/**
 * Created by User on 11/11/2559.
 */

public abstract class PloyeeHomeRecyclerAdapter extends RecyclerView.Adapter<PloyeeHomeRecyclerAdapter.ViewHolder> {
    @Override
    public PloyeeHomeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_textview_list_item,parent,false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
