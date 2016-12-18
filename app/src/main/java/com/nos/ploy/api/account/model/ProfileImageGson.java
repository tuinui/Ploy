package com.nos.ploy.api.account.model;

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
    public static class Data {

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
    }

}
