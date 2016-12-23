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
    "userId": 1,
    "aboutMe": "testsss",
    "education": "sssssstest",
    "work": "test",
    "interest": "test",
    "language": [],
    "transport": [],
    "location": {}
     */
    public static class Data {
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
        private Location location;

        public Data() {
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
