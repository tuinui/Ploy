package com.nos.ploy.api.account.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.List;

/**
 * Created by Saran on 18/12/2559.
 */

public class ProfileImageGson extends BaseResponse<List<ProfileImageGson.Data>> {
    //    {
//        "responseMsg": {
//        "msgCode": "000",
//                "msgDesc": null
//    },
//        "data": [
//        {
//            "imgId": 17,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1481976616026.jpg",
//                "imageName": "user-profile_1481976616026.jpg"
//        },
//        {
//            "imgId": 18,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482066227506.jpg",
//                "imageName": "user-profile_1482066227506.jpg"
//        },
//        {
//            "imgId": 19,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482066495351.jpg",
//                "imageName": "user-profile_1482066495351.jpg"
//        },
//        {
//            "imgId": 20,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482066565994.jpg",
//                "imageName": "user-profile_1482066565994.jpg"
//        },
//        {
//            "imgId": 21,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482069639904.jpg",
//                "imageName": "user-profile_1482069639904.jpg"
//        },
//        {
//            "imgId": 22,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482073752063.jpg",
//                "imageName": "user-profile_1482073752063.jpg"
//        },
//        {
//            "imgId": 23,
//                "userId": 1,
//                "imagePath": "http://138.68.72.222:8080/images/user-profile_1482073935532.jpg",
//                "imageName": "user-profile_1482073935532.jpg"
//        }
//        ]
//    }
    public static class Data implements Parcelable {

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
        @SerializedName("imgId")
        private Long imgId;
        @SerializedName("userId")
        private Long userId;
        @SerializedName("imagePath")
        private String imagePath;
        @SerializedName("imageName")
        private String imageName;

        public Data() {
        }

        protected Data(Parcel in) {
            this.imgId = (Long) in.readValue(Long.class.getClassLoader());
            this.userId = (Long) in.readValue(Long.class.getClassLoader());
            this.imagePath = in.readString();
            this.imageName = in.readString();
        }

        public Data(String imagePath) {
            this.imagePath = imagePath;
        }

        public Long getImgId() {
            return imgId;
        }

        public Long getUserId() {
            return userId;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getImageName() {
            return imageName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.imgId);
            dest.writeValue(this.userId);
            dest.writeString(this.imagePath);
            dest.writeString(this.imageName);
        }
    }

}
