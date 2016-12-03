package com.nos.ploy.flow.ployee.home.content.service.list.viewmodel;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;

/**
 * Created by Saran on 30/11/2559.
 */

public class PloyeeServiceItemViewModel implements SortedListAdapter.ViewModel {
    private long mId;
    private String mText;
    private String mImageUrl;

    public PloyeeServiceItemViewModel(PloyeeServiceListGson.PloyeeServiceItemGson data) {
        if (null != data) {
            mId = data.getId();
            mText = data.getServiceName();
            mImageUrl = data.getImageUrl();
        }

    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PloyeeServiceItemViewModel model = (PloyeeServiceItemViewModel) o;

        if (mId != model.mId) return false;
        return mText != null ? mText.equals(model.mText) : model.mText == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mText != null ? mText.hashCode() : 0);
        return result;
    }
}
