package com.nos.ploy.api.ployee.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 21/12/2559.
 */

public class PloyeeAvailiabilityGson extends BaseResponse<PloyeeAvailiabilityGson.Data> {

    public class Data {
        @SerializedName("userId")
        private long userId;
        @SerializedName("holidayMode")
        private boolean holidayMode;
        @SerializedName("avaiItems")
        private List<AvailabilityItem> availabilityItems = new ArrayList<>();

        public Data() {
        }

        private Data(long userId, boolean holidayMode, List<AvailabilityItem> availabilityItems) {
            this.userId = userId;
            this.holidayMode = holidayMode;
            this.availabilityItems = availabilityItems;
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

        public List<AvailabilityItem> getAvailabilityItems() {
            return availabilityItems;
        }

        public void setAvailabilityItems(List<AvailabilityItem> availabilityItems) {
            this.availabilityItems = availabilityItems;
        }

        public Data cloneThis() {
            return new Data(userId, holidayMode, availabilityItems);
        }

        public class AvailabilityItem {
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
        }


    }
}
