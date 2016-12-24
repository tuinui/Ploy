package com.nos.ploy.api.account.model;

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
        private long userId;
        @SerializedName("aboutMe")
        private String aboutMe;
        @SerializedName("education")
        private String education;
        @SerializedName("work")
        private String work;
        @SerializedName("interest")
        private String interest;
        @SerializedName("language")
        private List<Language> language = new ArrayList<>();
        @SerializedName("transport")
        private List<Transport> transport = new ArrayList<>();
        @SerializedName("location")
        private Location location = new Location();
        @SerializedName("contactPhone")
        private boolean contactPhone;
        @SerializedName("contactEmail")
        private boolean contactEmail;

        public Data() {
        }

        private Data(Long userProfileId, long userId, String aboutMe, String education, String work, String interest, List<Language> language, List<Transport> transport, Location location,boolean contactPhone,boolean contactEmail) {
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

        public Data cloneThis(){
            return new Data(null,userId,aboutMe,education,work,interest,language,transport,location,contactEmail,contactPhone);
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

        public boolean isContactPhone() {
            return contactPhone;
        }

        public boolean isContactEmail() {
            return contactEmail;
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

        public void setLanguage(List<Language> language) {
            this.language = language;
        }

        public void setTransport(List<Transport> transport) {
            this.transport = transport;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public long getUserId() {
            return userId;
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

        public List<Language> getLanguage() {
            return language;
        }

        public List<Transport> getTransport() {
            return transport;
        }

        public Location getLocation() {
            return location;
        }

        public long getUserProfileId() {
            return userProfileId;
        }

        private class Language {
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
        }


        private class Transport {
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

            public long getTransportId() {
                return transportId;
            }

            public String getTransportName() {
                return transportName;
            }
        }

        private class Location {
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

            public Double getLat() {
                return lat;
            }

            public Double getLng() {
                return lng;
            }
        }
    }
}
