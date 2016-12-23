package com.nos.ploy.api.authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

/**
 * Created by Saran on 29/11/2559.
 */

public class AccountGson extends BaseResponse<AccountGson.Data> {

    /* 29/11/229
      "data": {
    "userId": 10,
    "firstName": "test",
    "lastName": "test",
    "birthDay": 1454346000000,
    "email": "test4@gmail.com",
    "password": "2dCpxnfLev8fsQSrT6afPw==",
    "userType": "ROLE_USER",
    "phone": null
  }
     */

    public AccountGson() {
    }

    public static class Data implements Parcelable {
        @SerializedName("userId")
        private Long userId;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("birthDay")
        private String birthDay;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("userType")
        private String userType;
        @SerializedName("phone")
        private String phone;

        public Data() {
        }

        public String getBirthDay() {
            return birthDay;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getName() {
            return firstName + " " + lastName;
        }

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return phone;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUserType() {
            return userType;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.userId);
            dest.writeString(this.firstName);
            dest.writeString(this.lastName);
            dest.writeString(this.birthDay);
            dest.writeString(this.email);
            dest.writeString(this.password);
            dest.writeString(this.userType);
            dest.writeString(this.phone);
        }

        protected Data(Parcel in) {
            this.userId = (Long) in.readValue(Long.class.getClassLoader());
            this.firstName = in.readString();
            this.lastName = in.readString();
            this.birthDay = in.readString();
            this.email = in.readString();
            this.password = in.readString();
            this.userType = in.readString();
            this.phone = in.readString();
        }

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
    }

}
