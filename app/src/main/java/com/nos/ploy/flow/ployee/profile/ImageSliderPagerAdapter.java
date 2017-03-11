package com.nos.ploy.flow.ployee.profile;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.nos.ploy.R;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 24/12/2559.
 */

public class ImageSliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ProfileImageGson.Data> mDatas = new ArrayList<>();
    private ProfileImageGson.Data mEmptyData = new ProfileImageGson.Data();

    public ImageSliderPagerAdapter(Context context) {
        this.mContext = context;
    }

    public void replaceData(List<ProfileImageGson.Data> images) {
        mDatas.clear();
        notifyDataSetChanged();
        if (null == images || images.isEmpty()) {
            mDatas.add(mEmptyData);
        } else {
            mDatas.addAll(images);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = RecyclerUtils.getSize(mDatas);
        if(size >= 3){
            size = 3;
        }
        return size;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_profile_image_slider_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview_profile_image_slider_item);
        imageView.setImageDrawable(null);
        if (RecyclerUtils.isAvailableData(mDatas, position)) {
            ProfileImageGson.Data data = mDatas.get(position);
            Glide.with(imageView.getContext()).load(data.getImagePath()).fallback(R.drawable.ic_circle_profile_120dp).error(R.drawable.ic_circle_profile_120dp).placeholder(R.drawable.ic_circle_profile_120dp).into(imageView);
        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
