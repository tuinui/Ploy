package com.nos.ploy.api.ployee.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 21/12/2559.
 */

public class PloyeeAvailiabilityGson extends BaseResponse<PloyeeAvailiabilityGson.Data> {

    public  class Data {
        @SerializedName("holidayMode")
        private Boolean holidayMode;
        @SerializedName("avaItems")
        private List<AvailabilityItem> availabilityItems = new ArrayList<>();

        public Data() {
        }

        public Boolean getHolidayMode() {
            return holidayMode;
        }

        public List<AvailabilityItem> getAvailabilityItems() {
            return availabilityItems;
        }

        public  class AvailabilityItem {
            @SerializedName("durationId")
            private Long durationId;
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

            public Long getDurationId() {
                return durationId;
            }

            public String getDurationValue() {
                return durationValue;
            }

            public boolean isSun() {
                return sun;
            }

            public void setDurationId(Long durationId) {
                this.durationId = durationId;
            }

            public void setDurationValue(String durationValue) {
                this.durationValue = durationValue;
            }

            public void setSun(boolean sun) {
                this.sun = sun;
            }

            public void setMon(boolean mon) {
                this.mon = mon;
            }

            public void setTues(boolean tues) {
                this.tues = tues;
            }

            public void setWed(boolean wed) {
                this.wed = wed;
            }

            public void setThurs(boolean thurs) {
                this.thurs = thurs;
            }

            public void setFri(boolean fri) {
                this.fri = fri;
            }

            public void setSat(boolean sat) {
                this.sat = sat;
            }

            public boolean isMon() {
                return mon;
            }

            public boolean isTues() {
                return tues;
            }

            public boolean isWed() {
                return wed;
            }

            public boolean isThurs() {
                return thurs;
            }

            public boolean isFri() {
                return fri;
            }

            public boolean isSat() {
                return sat;
            }
        }


    }
}
