package com.nos.ploy.api.masterdata.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 3/1/2560.
 */

public class AppLanguageGson extends BaseResponse<ArrayList<AppLanguageGson.Data>> {


    public static class Data implements Parcelable {
        private long id;
        private String code;
        private String name;
        private boolean activeStatus;

        public Data() {
        }

        public long getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public boolean isActiveStatus() {
            return activeStatus;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.code);
            dest.writeString(this.name);
            dest.writeByte(this.activeStatus ? (byte) 1 : (byte) 0);
        }

        protected Data(Parcel in) {
            this.id = in.readLong();
            this.code = in.readString();
            this.name = in.readString();
            this.activeStatus = in.readByte() != 0;
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
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
