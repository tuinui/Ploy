package com.nos.ploy.flow.ployer.ployeelist.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nos.ploy.R;

/**
 * Created by User on 10/11/2559.
 */

public abstract class PloyeeListRecyclerAdapter extends RecyclerView.Adapter<PloyeeListRecyclerAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployee_list_item,parent,false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
