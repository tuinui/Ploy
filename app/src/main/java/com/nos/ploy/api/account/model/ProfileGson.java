package com.nos.ploy.api.account.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 24/12/2559.
 */

public class ProfileGson extends BaseResponse<ProfileGson.Data> {

    /*
  {
  "responseMsg": {
    "msgCode": "000",
    "msgDesc": null
  },
  "data": {
    "userProfileId": null,
    "userId": 235,
    "aboutMe": "test",
    "education": "test",
    "work": "test",
    "interest": "test",
    "language": [
      "de",
      "en"
    ],
    "transport": [
    ],
    "location": {
    },
    "contactPhone": false,
    "contactEmail": false
  }
}
     */
    public static class Data {
        @SerializedName("userProfileId")
        private Long userProfileId;
        @SerializedName("userId")
        private Long userId;
        @SerializedName("aboutMe")
        private String aboutMe;
        @SerializedName("education")
        private String education;
        @SerializedName("work")
        private String work;
        @SerializedName("interest")
        private String interest;
        @SerializedName("language")
        private ArrayList<Language> language = new ArrayList<>();
        @SerializedName("transport")
        private List<Transport> transport = new ArrayList<>();
        @SerializedName("location")
        private Location location = new Location();
        @SerializedName("contactPhone")
        private Boolean contactPhone;
        @SerializedName("contactEmail")
        private Boolean contactEmail;

        public Data() {
        }

        private Data(Long userProfileId, long userId, String aboutMe, String education, String work, String interest, ArrayList<Language> language, List<Transport> transport, Location location, boolean contactPhone, boolean contactEmail) {
            this.userProfileId = userProfileId;
            this.userId = userId;
            this.aboutMe = aboutMe;
            this.education = education;
            this.work = work;
            this.interest = interest;
            this.language = language;
            this.transport = transport;
            this.location = location;
            this.contactEmail = contactEmail;
            this.contactPhone = contactPhone;
        }

        public Data cloneThis() {
            return new Data(null, userId, aboutMe, education, work, interest, language, transport, location, contactEmail, contactPhone);
        }


        public void setUserProfileId(long userProfileId) {
            this.userProfileId = userProfileId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setAboutMe(String aboutMe) {
            this.aboutMe = aboutMe;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public Boolean isContactPhone() {
            return null != contactPhone && contactPhone;
        }

        public Boolean isContactEmail() {
            return null != contactEmail && contactEmail;
        }

        public void setContactPhone(boolean contactPhone) {
            this.contactPhone = contactPhone;
        }

        public void setContactEmail(boolean contactEmail) {
            this.contactEmail = contactEmail;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public void setLanguage(ArrayList<Language> language) {
            this.language = language;
        }

        public void setTransport(List<Transport> transport) {
            this.transport = transport;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Long getUserId() {
            return null != userId ? userId : Long.valueOf(0L);
        }

        public String getAboutMe() {
            return aboutMe;
        }

        public String getEducation() {
            return education;
        }

        public String getWork() {
            return work;
        }

        public String getInterest() {
            return interest;
        }

        public ArrayList<Language> getLanguage() {
            return language;
        }

        public List<Transport> getTransport() {
            return transport;
        }

        public Location getLocation() {
            return location;
        }

        public Long getUserProfileId() {
            return null != userProfileId ? userProfileId : Long.valueOf(0L);
        }

        public static class Language implements Parcelable {
            /*
              "spokenLanguageCode": "de",
        "spokenLanguageValue": "Germany"
             */
            @SerializedName("spokenLanguageCode")
            private String spokenLanguageCode;
            @SerializedName("spokenLanguageValue")
            private String spokenLanguageValue;

            public Language() {
            }


            public String getSpokenLanguageCode() {
                return spokenLanguageCode;
            }

            public String getSpokenLanguageValue() {
                return spokenLanguageValue;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.spokenLanguageCode);
                dest.writeString(this.spokenLanguageValue);
            }

            protected Language(Parcel in) {
                this.spokenLanguageCode = in.readString();
                this.spokenLanguageValue = in.readString();
            }

            public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
                @Override
                public Language createFromParcel(Parcel source) {
                    return new Language(source);
                }

                @Override
                public Language[] newArray(int size) {
                    return new Language[size];
                }
            };
        }


        public static class Transport {
            /*
            {
        "transportId": 2,
        "transportName": "Bicycle"
      },
      {
        "transportId": 3,
        "transportName": "Car"
      }
             */
            @SerializedName("transportId")
            private long transportId;
            @SerializedName("transportName")
            private String transportName;

            public Transport() {
            }

            public Transport(long transportId) {
                this.transportId = transportId;
            }

            public long getTransportId() {
                return transportId;
            }

            public String getTransportName() {
                return transportName;
            }
        }

        public static class Location {
            /*
                "lat": 40.3342,
                "lng": 20.333
             */
            @SerializedName("lat")
            private Double lat;
            @SerializedName("lng")
            private Double lng;

            public Location() {
            }

            public Location(Double lat, Double lng) {
                this.lat = lat;
                this.lng = lng;
            }

            public Double getLat() {
                return null != lat ? lat : Double.valueOf(0D);
            }

            public Double getLng() {
                return null != lng ? lng : Double.valueOf(0D);
            }
        }
    }
}
