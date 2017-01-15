package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 13/1/2560.
 */

public class ReviewGson extends BaseResponse<ReviewGson.Data> {


    public static class Data {
        @SerializedName("reviewAVG")
        private ReviewAverage reviewAverage;
        @SerializedName("reviewDataList")
        private List<ReviewData> reviewDataList = new ArrayList<>();

        public Data() {
        }

        public ReviewAverage getReviewAverage() {
            return reviewAverage;
        }

        public List<ReviewData> getReviewDataList() {
            return reviewDataList;
        }

        public static class ReviewAverage implements Parcelable {
            /*
             "userId": 1,
      "allAvg": 2.4,
      "competenceAvg": 3,
      "punctualityAvg": 2,
      "politenessAvg": 3,
      "communicationAvg": 4,
      "professionalismAvg": 0
             */
            @SerializedName("userId")
            private Long userId;
            @SerializedName("allAvg")
            private Float all;
            @SerializedName("competenceAvg")
            private Float competence;
            @SerializedName("punctualityAvg")
            private Float punctuality;
            @SerializedName("politenessAvg")
            private Float politeness;
            @SerializedName("communicationAvg")
            private Float communication;
            @SerializedName("professionalismAvg")
            private Float professionalism;

            public ReviewAverage() {
            }

            public Float getAll() {
                return null == all ? 0 : all;
            }

            public Float getCommunication() {
                return null == communication ? 0 : communication;
            }

            public Float getCompetence() {
                return null == competence ? 0 : competence;
            }

            public Float getPoliteness() {
                return null == politeness ? 0 : politeness;
            }

            public Float getProfessionalism() {
                return null == professionalism ? 0 : professionalism;
            }

            public Float getPunctuality() {
                return null == punctuality ? 0 : punctuality;
            }

            public Long getUserId() {
                return userId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(this.userId);
                dest.writeValue(this.all);
                dest.writeValue(this.competence);
                dest.writeValue(this.punctuality);
                dest.writeValue(this.politeness);
                dest.writeValue(this.communication);
                dest.writeValue(this.professionalism);
            }

            protected ReviewAverage(Parcel in) {
                this.userId = (Long) in.readValue(Long.class.getClassLoader());
                this.all = (Float) in.readValue(Float.class.getClassLoader());
                this.competence = (Float) in.readValue(Float.class.getClassLoader());
                this.punctuality = (Float) in.readValue(Float.class.getClassLoader());
                this.politeness = (Float) in.readValue(Float.class.getClassLoader());
                this.communication = (Float) in.readValue(Float.class.getClassLoader());
                this.professionalism = (Float) in.readValue(Float.class.getClassLoader());
            }

            public static final Parcelable.Creator<ReviewAverage> CREATOR = new Parcelable.Creator<ReviewAverage>() {
                @Override
                public ReviewAverage createFromParcel(Parcel source) {
                    return new ReviewAverage(source);
                }

                @Override
                public ReviewAverage[] newArray(int size) {
                    return new ReviewAverage[size];
                }
            };
        }

        public static class ReviewData {
            /*
             "review": {
          "id": 4,
          "userId": 1,
          "reviewerUserId": 3,
          "competence": 3,
          "punctuality": 2,
          "politeness": 3,
          "communication": 4,
          "professionalism": 0,
          "createdDate": "2016-12-24",
          "reviewText": "tttt"
        },
        "userProfileImage": {
          "imgId": 6,
          "userId": 1,
          "imagePath": "http://localhost:8080/images/user-profile_1483427298135.jpg",
          "imageName": "user-profile_1483427298135.jpg"
        }
             */
            @SerializedName("review")
            private Review review;

            @SerializedName("userProfileImage")
            private ProfileImageGson.Data userProfileImage;

            public ReviewData() {
            }

            public Review getReview() {
                return review;
            }

            public ProfileImageGson.Data getUserProfileImage() {
                return userProfileImage;
            }

            public static class Review {
                /*
                "id": 3,
          "userId": 1,
          "reviewerUserId": 2,
          "competence": 3,
          "punctuality": 2,
          "politeness": 3,
          "communication": 4,
          "professionalism": 0,
          "createdDate": "2016-12-24",
          "reviewText": "tttt"
                 */
                @SerializedName("id")
                private Long id;
                @SerializedName("userId")
                private Long userId;
                @SerializedName("reviewerUserId")
                private Long reviewerUserId;
                @SerializedName("competence")
                private Long competence;
                @SerializedName("punctuality")
                private Long punctuality;
                @SerializedName("politeness")
                private Long politeness;
                @SerializedName("communication")
                private Long communication;
                @SerializedName("professionalism")
                private Long professionalism;
                @SerializedName("createdDate")
                private String createdDate;
                @SerializedName("reviewText")
                private String reviewText;

                public Review() {
                }

                public Review(long userId, long reviewerUserId, long competence, long punctuality, long politeness, long communication, long professionalism, String reviewText) {
                    this.userId = userId;
                    this.reviewerUserId = reviewerUserId;
                    this.communication = communication;
                    this.competence = competence;
                    this.punctuality = punctuality;
                    this.politeness = politeness;
                    this.professionalism = professionalism;
                    this.reviewText = reviewText;
                }

                public Long getCommunication() {
                    return communication;
                }

                public Long getCompetence() {
                    return competence;
                }

                public String getCreatedDate() {
                    return createdDate;
                }

                public Long getId() {
                    return id;
                }

                public Long getPoliteness() {
                    return politeness;
                }

                public Long getProfessionalism() {
                    return professionalism;
                }

                public Long getPunctuality() {
                    return punctuality;
                }

                public Long getReviewerUserId() {
                    return reviewerUserId;
                }

                public String getReviewText() {
                    return reviewText;
                }

                public Long getUserId() {
                    return userId;
                }
            }
        }
    }
}
