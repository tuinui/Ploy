package com.nos.ploy.flow.ployee.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nos.ploy.R;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;

/**
 * Created by Saran on 25/12/2559.
 */

public abstract class TransportRecyclerAdapter extends RecyclerView.Adapter<TransportRecyclerAdapter.ViewHolder> {


    private LanguageAppLabelGson.Data language;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transport_item, parent, false));
    }

    public void setLanguage(LanguageAppLabelGson.Data language) {
        this.language = language;
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

    public String toTransportName(long id) {
        if (id == 1) {
            return language.profileScreenWalk;
        } else if (id == 2) {
            return language.profileScreenBicycle;
        } else if (id == 3) {
            return language.profileScreenCar;
        } else if (id == 4) {
            return language.profileScreenMototbike;
        } else if (id == 5) {
            return language.profileScreenTruck;
        } else if (id == 6) {
            return language.profileScreenPublicTransport;
        } else {
            return "";
        }
    }
}
