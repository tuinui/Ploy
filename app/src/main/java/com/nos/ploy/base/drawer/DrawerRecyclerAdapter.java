package com.nos.ploy.base.drawer;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nos.ploy.R;

/**
 * Created by User on 13/11/2559.
 */

public abstract class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.ViewHolder> {
    @ColorInt
    int textColor = Color.WHITE;

    public DrawerRecyclerAdapter(int textColor) {
        super();
        this.textColor = textColor;
    }

    public DrawerRecyclerAdapter() {
        super();
    }

    @Override
    public DrawerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_textview_list_item_sidemenu, parent, false),textColor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvItem;

        public ViewHolder(View itemView, int textColor) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.textview_list_item);
            tvItem.setTextColor(textColor);
        }
    }
}
