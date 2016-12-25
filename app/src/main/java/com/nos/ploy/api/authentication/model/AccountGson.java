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
        @SerializedName("fbUserId")
        private String fbUserId;

        public Data() {
        }

        public Data(Long userId, String firstName, String lastName, String birthDay, String email, String password, String userType, String phone, String fbUserId) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDay = birthDay;
            this.email = email;
            this.password = password;
            this.userType = userType;
            this.phone = phone;
            this.fbUserId = fbUserId;
        }

        public Data cloneThis() {
            return new Data(getUserId(),firstName,lastName,birthDay,email,password,userType,phone,fbUserId);
        }
        public String getBirthDay() {
            return birthDay;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setBirthDay(String birthDay) {
            this.birthDay = birthDay;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setFbUserId(String fbUserId) {
            this.fbUserId = fbUserId;
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

        public String getFbUserId() {
            return fbUserId;
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
            dest.writeString(this.fbUserId);
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
            this.fbUserId = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
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
