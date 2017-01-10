package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Saran on 3/1/2560.
 */

public class PloyerServicesGson extends BaseResponse<ArrayList<PloyerServicesGson.Data>> {
    public static class Data implements SortedListAdapter.ViewModel, Parcelable {
        /*
         "id": 1,
      "serviceName": "Children",
      "imgUrl": "http://localhost:8080/image/service_1481436000929.jpg",
      "ployeeCount": 5
         */
        @SerializedName("id")
        private Long id;
        @SerializedName("serviceName")
        private String serviceName;
        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("ployeeCount")
        private Long ployeeCount;

        public Data() {
        }

        public Long getId() {
            return null == id ? 0 : id;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public long getPloyeeCount() {
            return null == ployeeCount ? 0 : ployeeCount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.serviceName);
            dest.writeString(this.imgUrl);
            dest.writeLong(this.ployeeCount);
        }

        protected Data(Parcel in) {
            this.id = in.readLong();
            this.serviceName = in.readString();
            this.imgUrl = in.readString();
            this.ployeeCount = in.readLong();
        }

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }
}
