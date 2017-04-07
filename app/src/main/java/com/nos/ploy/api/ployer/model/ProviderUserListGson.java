package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.response.BaseResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 6/1/2560.
 */

public class ProviderUserListGson extends BaseResponse<ProviderUserListGson.Data> {


    public static class Data implements SortedListAdapter.ViewModel, Parcelable {
        @SerializedName("pagination")
        private Pagination pagination = new Pagination();

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

        public static class Pagination implements Parcelable {
            @SerializedName("pageNo")
            private Long pageNo;
            @SerializedName("total")
            private Long total;


            public Pagination() {
            }

            public Long getPageNo() {
                return null == pageNo ? 1 : pageNo;
            }

            public Long getTotal() {
                return null == total ? 0 : total;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(this.pageNo);
                dest.writeValue(this.total);
            }

            protected Pagination(Parcel in) {
                this.pageNo = (Long) in.readValue(Long.class.getClassLoader());
                this.total = (Long) in.readValue(Long.class.getClassLoader());
            }

            public static final Creator<Pagination> CREATOR = new Creator<Pagination>() {
                @Override
                public Pagination createFromParcel(Parcel source) {
                    return new Pagination(source);
                }

                @Override
                public Pagination[] newArray(int size) {
                    return new Pagination[size];
                }
            };
        }


        public static class UserService implements SortedListAdapter.ViewModel, Parcelable {
            @SerializedName("userId")
            private Long userId = 0L;
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
            private Long minPrice = 0L;
            @SerializedName("maxPrice")
            private Long maxPrice = 0L;
            @SerializedName("locationLat")
            private Double locationLat;
            @SerializedName("locationLng")
            private Double locationLng;
            @SerializedName("imagePath")
            private String imagePath;


            @SerializedName("reviewPoint")
            private Float reviewPoint = 0F;
            @SerializedName("reviewCount")
            private Long reviewCount = 0L;
            @SerializedName("serviceCount")
            private Long serviceCount = 0L;


            public UserService() {
            }
            public void setReviewCount(Long reviewCount) {
                this.reviewCount = reviewCount;
            }

            public void setReviewPoint(Float reviewPoint) {
                this.reviewPoint = reviewPoint;
            }
            public UserService(long userId, AccountGson.Data accountGson, LatLng latLng) {
                this.userId = userId;
                if (null != accountGson) {
                    this.firstName = accountGson.getFirstName();
                    this.lastName = accountGson.getLastName();
                    this.email = accountGson.getEmail();
                    this.password = accountGson.getPassword();
                    this.phone = accountGson.getPhone();
                    this.fbUserId = accountGson.getFbUserId();
                }

                if (null != latLng) {
                    this.locationLat = latLng.latitude;
                    this.locationLng = latLng.longitude;
                }

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

            public String getFullName() {
                String lastNameToDisplay = "";
                if (!TextUtils.isEmpty(getLastName())) {
                    lastNameToDisplay = getLastName().substring(0, 1) + ".";
                }
                return getFirstName() + " " + lastNameToDisplay;
            }

            public String getImagePath() {
                return imagePath;
            }

            public Boolean getActive() {
                return null != isActive && isActive;
            }

            public String getLastName() {
                return StringUtils.capitalize(lastName);
            }

            public Double getLocationLat() {
                return locationLat;
            }

            public Double getLocationLng() {
                return  locationLng;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.pagination, flags);
            dest.writeTypedList(this.userServiceList);
        }

        protected Data(Parcel in) {
            this.pagination = in.readParcelable(Pagination.class.getClassLoader());
            this.userServiceList = in.createTypedArrayList(UserService.CREATOR);
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
