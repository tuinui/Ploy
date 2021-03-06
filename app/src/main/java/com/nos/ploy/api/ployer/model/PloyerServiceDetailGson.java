package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyerServiceDetailGson extends BaseResponse<PloyerServiceDetailGson.Data> {
    /*
    {
        "data": {
            "serviceId": 1,
            "userId": 1,
            "serviceMappingId": 1,
            "serviceNameOthers": "xx",
            "description": "Service Description",
            "priceMin": 50,
            "priceMax": 500,
            "certificate": "test en",
            "equipment": "test en",
            "subService": [
              {
                "subServiceLV1": {
                  "subServiceLv1Id": 1,
                  "name": "Keep",
                  "serviceId": 1
                },
                "subServiceLV2": [
                  {
                    "subServiceLv2Id": 1,
                    "subServiceLv1Id": 1,
                    "name": "Guard 7 days / 7",
                    "checked": true
                  },
                  {
                    "subServiceLv2Id": 2,
                    "subServiceLv1Id": 1,
                    "name": "Night guard",
                    "checked": true
                  }
                ]
              },
              {
                "subServiceLV1": {
                  "subServiceLv1Id": 2,
                  "name": "Comfort",
                  "serviceId": 1
                },
                "subServiceLV2": [
                  {
                    "subServiceLv2Id": 3,
                    "subServiceLv1Id": 2,
                    "name": "Manage",
                    "checked": true
                  }
                ]
              }
            ]
          }
        }
     */


    public static class Data implements Parcelable {


        @SerializedName("serviceId")
        private long serviceId;
        @SerializedName("userId")
        private long userId;
        @SerializedName("serviceMappingId")
        private long serviceMappingId;
        @SerializedName("serviceName")
        private String serviceName;
        @SerializedName("serviceNameOthers")
        private String serviceNameOthers;
        @SerializedName("description")
        private String description;
        @SerializedName("priceMin")
        private long priceMin;
        @SerializedName("priceMax")
        private long priceMax;
        @SerializedName("certificate")
        private String certificate;
        @SerializedName("equipment")
        private String equipment;
        @SerializedName("subService")
        private List<SubService> subServices = new ArrayList<>();
        @SerializedName("priceUnit")
        private String priceUnit;

        public Data() {
        }

        private Data(long serviceId, long userId, long serviceMappingId, String serviceNameOthers,String serviceName, String description, long priceMin, long priceMax, String certificate, String equipment, List<SubService> subServices) {
            this.serviceId = serviceId;
            this.userId = userId;
            this.serviceMappingId = serviceMappingId;
            this.serviceNameOthers = serviceNameOthers;
            this.serviceName = serviceName;
            this.description = description;
            this.priceMin = priceMin;
            this.priceMax = priceMax;
            this.certificate = certificate;
            this.equipment = equipment;
            this.subServices = subServices;
        }

        public long getServiceId() {
            return serviceId;
        }

        public void setServiceId(Long serviceId) {
            this.serviceId = serviceId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public long getServiceMappingId() {
            return serviceMappingId;
        }

        public void setServiceMappingId(Long serviceMappingId) {
            this.serviceMappingId = serviceMappingId;
        }

        public String getServiceNameOthers() {
            return serviceNameOthers;
        }

        public void setServiceNameOthers(String serviceNameOthers) {
            this.serviceNameOthers = serviceNameOthers;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getPriceMin() {
            return priceMin;
        }

        public void setPriceMin(long priceMin) {
            this.priceMin = priceMin;
        }

        public long getPriceMax() {
            return priceMax;
        }

        public void setPriceMax(long priceMax) {
            this.priceMax = priceMax;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public List<SubService> getSubServices() {
            return subServices;
        }

        public void setSubServices(List<SubService> subServices) {
            this.subServices = subServices;
        }

        public Data cloneThis() {
            return new Data(serviceId, userId, serviceMappingId, serviceNameOthers,serviceName, description, priceMin, priceMax, certificate, equipment, subServices);
        }

        public String getPriceUnit() {
            return priceUnit;
        }

        public String getServiceName() {
            return serviceName;
        }

        public static class SubService {
            @SerializedName("subServiceLV1")
            private SubServiceLv1 subServiceLV1;
            @SerializedName("subServiceLV2")
            private List<SubServiceLv2> subServiceLV2;

            public SubService() {
            }

            public SubServiceLv1 getSubServiceLV1() {
                return subServiceLV1;
            }

            public List<SubServiceLv2> getSubServiceLV2() {
                return subServiceLV2;
            }

            public static class SubServiceLv2 {
                @SerializedName("subServiceLV2Id")
                private Long subServiceLv2Id;
                @SerializedName("subServiceLV1Id")
                private Long subServiceLv1Id;
                @SerializedName("name")
                private String name;
                @SerializedName("checked")
                private Boolean checked;

                public SubServiceLv2() {
                }

                public Boolean getChecked() {
                    return checked;
                }

                public void setChecked(Boolean checked) {
                    this.checked = checked;
                }

                public long getSubServiceLv2Id() {
                    return null != subServiceLv2Id ? subServiceLv2Id : -404;
                }

                public void setSubServiceLv2Id(Long subServiceLv2Id) {
                    this.subServiceLv2Id = subServiceLv2Id;
                }

                public long getSubServiceLv1Id() {
                    return null != subServiceLv1Id ? subServiceLv1Id : -404;
                }

                public void setSubServiceLv1Id(Long subServiceLv1Id) {
                    this.subServiceLv1Id = subServiceLv1Id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Boolean isChecked() {
                    return null != checked && checked;
                }
            }


            public static class SubServiceLv1 {
                @SerializedName("subServiceLv1Id")
                private Long subServiceLv1Id;
                @SerializedName("name")
                private String name;
                @SerializedName("serviceId")
                private Long serviceId;

                public SubServiceLv1() {
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Long getSubServiceLv1Id() {
                    return subServiceLv1Id;
                }

                public void setSubServiceLv1Id(Long subServiceLv1Id) {
                    this.subServiceLv1Id = subServiceLv1Id;
                }

                public Long getServiceId() {
                    return serviceId;
                }

                public void setServiceId(Long serviceId) {
                    this.serviceId = serviceId;
                }
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.serviceId);
            dest.writeLong(this.userId);
            dest.writeLong(this.serviceMappingId);
            dest.writeString(this.serviceName);
            dest.writeString(this.serviceNameOthers);
            dest.writeString(this.description);
            dest.writeLong(this.priceMin);
            dest.writeLong(this.priceMax);
            dest.writeString(this.certificate);
            dest.writeString(this.equipment);
            dest.writeList(this.subServices);
            dest.writeString(this.priceUnit);
        }

        protected Data(Parcel in) {
            this.serviceId = in.readLong();
            this.userId = in.readLong();
            this.serviceMappingId = in.readLong();
            this.serviceName = in.readString();
            this.serviceNameOthers = in.readString();
            this.description = in.readString();
            this.priceMin = in.readLong();
            this.priceMax = in.readLong();
            this.certificate = in.readString();
            this.equipment = in.readString();
            this.subServices = new ArrayList<SubService>();
            in.readList(this.subServices, SubService.class.getClassLoader());
            this.priceUnit = in.readString();
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
