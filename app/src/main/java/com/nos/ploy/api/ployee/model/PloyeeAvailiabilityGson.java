package com.nos.ploy.api.ployee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Saran on 21/12/2559.
 */

public class PloyeeAvailiabilityGson extends BaseResponse<PloyeeAvailiabilityGson.Data> {

    public static class Data {
        @SerializedName("userId")
        private long userId;
        @SerializedName("holidayMode")
        private boolean holidayMode;
        @SerializedName("avaiItems")
        private ArrayList<AvailabilityItem> availabilityItems = new ArrayList<>();

        public Data() {
        }

        private Data(long userId, boolean holidayMode, ArrayList<AvailabilityItem> availabilityItems) {
            this.userId = userId;
            this.holidayMode = holidayMode;
            this.availabilityItems.clear();
            this.availabilityItems.addAll(availabilityItems);
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public boolean getHolidayMode() {
            return holidayMode;
        }

        public void setHolidayMode(boolean holidayMode) {
            this.holidayMode = holidayMode;
        }

        public ArrayList<AvailabilityItem> getAvailabilityItems() {
            return availabilityItems;

        }

        public void setAvailabilityItems(ArrayList<AvailabilityItem> availabilityItems) {
            this.availabilityItems = availabilityItems;
        }

        public Data cloneThis() {
            return new Data(userId, holidayMode, availabilityItems);
        }

        public static class AvailabilityItem implements Parcelable,Serializable {
            @SerializedName("durationId")
            private long durationId;
            @SerializedName("durationValue")
            private String durationValue;
            @SerializedName("sun")
            private boolean sun;
            @SerializedName("mon")
            private boolean mon;
            @SerializedName("tues")
            private boolean tues;
            @SerializedName("wed")
            private boolean wed;
            @SerializedName("thurs")
            private boolean thurs;
            @SerializedName("fri")
            private boolean fri;
            @SerializedName("sat")
            private boolean sat;

            public AvailabilityItem() {
            }

            public AvailabilityItem(AvailabilityItem clone) {
                setFri(clone.isFri());
                setMon(clone.isMon());
                setTues(clone.isTues());
                setThurs(clone.isThurs());
                setWed(clone.isWed());
                setFri(clone.isFri());
                setSat(clone.isSat());
                setSun(clone.isSun());
                setDurationId(clone.getDurationId());
                setDurationValue(clone.getDurationValue());
            }


            public long getDurationId() {
                return durationId;
            }

            public void setDurationId(Long durationId) {
                this.durationId = durationId;
            }

            public String getDurationValue() {
                return durationValue;
            }

            public void setDurationValue(String durationValue) {
                this.durationValue = durationValue;
            }

            public boolean isSun() {
                return sun;
            }

            public void setSun(boolean sun) {
                this.sun = sun;
            }

            public boolean isMon() {
                return mon;
            }

            public void setMon(boolean mon) {
                this.mon = mon;
            }

            public boolean isTues() {
                return tues;
            }

            public void setTues(boolean tues) {
                this.tues = tues;
            }

            public boolean isWed() {
                return wed;
            }

            public void setWed(boolean wed) {
                this.wed = wed;
            }

            public boolean isThurs() {
                return thurs;
            }

            public void setThurs(boolean thurs) {
                this.thurs = thurs;
            }

            public boolean isFri() {
                return fri;
            }

            public void setFri(boolean fri) {
                this.fri = fri;
            }

            public boolean isSat() {
                return sat;
            }

            public void setSat(boolean sat) {
                this.sat = sat;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.durationId);
                dest.writeString(this.durationValue);
                dest.writeByte(this.sun ? (byte) 1 : (byte) 0);
                dest.writeByte(this.mon ? (byte) 1 : (byte) 0);
                dest.writeByte(this.tues ? (byte) 1 : (byte) 0);
                dest.writeByte(this.wed ? (byte) 1 : (byte) 0);
                dest.writeByte(this.thurs ? (byte) 1 : (byte) 0);
                dest.writeByte(this.fri ? (byte) 1 : (byte) 0);
                dest.writeByte(this.sat ? (byte) 1 : (byte) 0);
            }

            protected AvailabilityItem(Parcel in) {
                this.durationId = in.readLong();
                this.durationValue = in.readString();
                this.sun = in.readByte() != 0;
                this.mon = in.readByte() != 0;
                this.tues = in.readByte() != 0;
                this.wed = in.readByte() != 0;
                this.thurs = in.readByte() != 0;
                this.fri = in.readByte() != 0;
                this.sat = in.readByte() != 0;
            }

            public static final Parcelable.Creator<AvailabilityItem> CREATOR = new Parcelable.Creator<AvailabilityItem>() {
                @Override
                public AvailabilityItem createFromParcel(Parcel source) {
                    return new AvailabilityItem(source);
                }

                @Override
                public AvailabilityItem[] newArray(int size) {
                    return new AvailabilityItem[size];
                }
            };
        }


    }
}
