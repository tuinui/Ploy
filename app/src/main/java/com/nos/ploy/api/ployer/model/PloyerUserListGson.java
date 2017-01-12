package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 6/1/2560.
 */

public class PloyerUserListGson extends BaseResponse<PloyerUserListGson.Data> {


    public static class Data implements SortedListAdapter.ViewModel {
        @SerializedName("pagination")
        private Pagination pagination;

        @SerializedName("userList")
        private List<UserService> userServiceList = new ArrayList<>();


        public Data() {
        }

        public Pagination getPagination() {
            return pagination;
        }

        public List<UserService> getUserServiceList() {
            return userServiceList;
        }

        public static class Pagination {
            @SerializedName("pageNo")
            private Long pageNo;
            @SerializedName("total")
            private Long total;


            public Pagination() {
            }

            public Long getPageNo() {
                return null == pageNo ? 0 : pageNo;
            }

            public Long getTotal() {
                return null == total ? 0 : total;
            }
        }


        public static class UserService implements SortedListAdapter.ViewModel, Parcelable {
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
            @SerializedName("phone")
            private String phone;
            @SerializedName("isActive")
            private Boolean isActive;
            @SerializedName("remarkInActive")
            private String remarkInActive;
            @SerializedName("fbUserId")
            private String fbUserId;
            @SerializedName("description")
            private String description;
            @SerializedName("minPrice")
            private Long minPrice;
            @SerializedName("maxPrice")
            private Long maxPrice;
            @SerializedName("locationLat")
            private Double locationLat;
            @SerializedName("locationLng")
            private Double locationLng;
            @SerializedName("imagePath")
            private String imagePath;
            @SerializedName("reviewPoint")
            private Float reviewPoint;
            @SerializedName("reviewCount")
            private Long reviewCount;
            @SerializedName("serviceCount")
            private Long serviceCount;


            public UserService() {
            }

            public Long getUserId() {
                return null == userId ? 0 : userId;
            }

            public String getBirthDay() {
                return birthDay;
            }

            public String getDescription() {
                return description;
            }

            public String getEmail() {
                return email;
            }

            public String getFbUserId() {
                return fbUserId;
            }

            public String getFirstName() {
                return StringUtils.capitalize(firstName);
            }

            public String getFullName(){
                return firstName +" "+lastName;
            }

            public String getImagePath() {
                return imagePath;
            }

            public Boolean getActive() {
                return null != isActive && isActive;
            }

            public String getLastName() {
                return lastName;
            }

            public Double getLocationLat() {
                return null == locationLat ? 0 : locationLat;
            }

            public Double getLocationLng() {
                return null == locationLng ? 0 : locationLng;
            }

            public Long getMaxPrice() {
                return null == maxPrice ? 0 : maxPrice;
            }

            public Long getMinPrice() {
                return null == minPrice ? 0 : minPrice;
            }

            public String getPassword() {
                return password;
            }

            public String getPhone() {
                return phone;
            }

            public String getRemarkInActive() {
                return remarkInActive;
            }

            public Long getReviewCount() {
                return null == reviewCount ? 0 : reviewCount;
            }

            public Float getReviewPoint() {
                return null == reviewPoint ? 0 : reviewPoint;
            }

            public Long getServiceCount() {
                return null == serviceCount ? 0 : serviceCount;
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
                dest.writeString(this.phone);
                dest.writeValue(this.isActive);
                dest.writeString(this.remarkInActive);
                dest.writeString(this.fbUserId);
                dest.writeString(this.description);
                dest.writeValue(this.minPrice);
                dest.writeValue(this.maxPrice);
                dest.writeValue(this.locationLat);
                dest.writeValue(this.locationLng);
                dest.writeString(this.imagePath);
                dest.writeValue(this.reviewPoint);
                dest.writeValue(this.reviewCount);
                dest.writeValue(this.serviceCount);
            }

            protected UserService(Parcel in) {
                this.userId = (Long) in.readValue(Long.class.getClassLoader());
                this.firstName = in.readString();
                this.lastName = in.readString();
                this.birthDay = in.readString();
                this.email = in.readString();
                this.password = in.readString();
                this.phone = in.readString();
                this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
                this.remarkInActive = in.readString();
                this.fbUserId = in.readString();
                this.description = in.readString();
                this.minPrice = (Long) in.readValue(Long.class.getClassLoader());
                this.maxPrice = (Long) in.readValue(Long.class.getClassLoader());
                this.locationLat = (Double) in.readValue(Double.class.getClassLoader());
                this.locationLng = (Double) in.readValue(Double.class.getClassLoader());
                this.imagePath = in.readString();
                this.reviewPoint = (Float) in.readValue(Float.class.getClassLoader());
                this.reviewCount = (Long) in.readValue(Long.class.getClassLoader());
                this.serviceCount = (Long) in.readValue(Long.class.getClassLoader());
            }

            public static final Parcelable.Creator<UserService> CREATOR = new Parcelable.Creator<UserService>() {
                @Override
                public UserService createFromParcel(Parcel source) {
                    return new UserService(source);
                }

                @Override
                public UserService[] newArray(int size) {
                    return new UserService[size];
                }
            };
        }
    }
}
