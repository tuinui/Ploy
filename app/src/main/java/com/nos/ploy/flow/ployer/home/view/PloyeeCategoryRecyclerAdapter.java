package com.nos.ploy.flow.ployer.home.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 10/11/2559.
 */

public abstract class PloyeeCategoryRecyclerAdapter extends RecyclerView.Adapter<PloyeeCategoryRecyclerAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ployer_service_item, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_ployer_service_item_image)
        public ImageView imgServiceImage;
        @BindView(R.id.textview_ployer_service_item_title)
        public TextView tvTitle;
        @BindView(R.id.textview_ployer_service_item_subtitle)
        public TextView tvSubtitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
