package com.nos.ploy.api.account.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.ReviewGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 10/1/2560.
 */

public class MemberProfileGson extends BaseResponse<MemberProfileGson.Data> {
    public class Data {

        @SerializedName("userProfile")
        private PloyeeProfileGson.Data userProfile;

        @SerializedName("avai")
        private PloyeeAvailiabilityGson.Data availability;

        @SerializedName("images")
        private List<ProfileImageGson.Data> images;

        @SerializedName("serviceMapping")
        private List<PloyerServiceDetailGson.Data> serviceDetails = new ArrayList<>();

        @SerializedName("reviewAVG")
        private ReviewGson.Data.ReviewAverage reviewAverage;

        public Data() {
        }

        public PloyeeAvailiabilityGson.Data getAvailability() {
            return availability;
        }

        public List<ProfileImageGson.Data> getImages() {
            return images;
        }

        public List<PloyerServiceDetailGson.Data> getServiceDetails() {
            return serviceDetails;
        }

        public PloyeeProfileGson.Data getUserProfile() {
            return userProfile;
        }

        public ReviewGson.Data.ReviewAverage getReviewAverage() {
            return reviewAverage;
        }
    }
}
