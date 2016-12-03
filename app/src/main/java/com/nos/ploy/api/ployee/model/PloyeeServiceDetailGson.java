package com.nos.ploy.api.ployee.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailGson extends BaseResponse<PloyeeServiceDetailGson> {
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
    @SerializedName("serviceId")
    private Long serviceId;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("serviceMappingId")
    private Long serviceMappingId;
    @SerializedName("serviceNameOthers")
    private String serviceNameOthers;
    @SerializedName("description")
    private String description;
    @SerializedName("priceMin")
    private Long priceMin;
    @SerializedName("priceMax")
    private Long priceMax;
    @SerializedName("certificate")
    private String certificate;
    @SerializedName("equipment")
    private String equipment;
    @SerializedName("subService")
    private List<SubService> subServices = new ArrayList<>();

    public PloyeeServiceDetailGson() {
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setServiceMappingId(Long serviceMappingId) {
        this.serviceMappingId = serviceMappingId;
    }

    public void setServiceNameOthers(String serviceNameOthers) {
        this.serviceNameOthers = serviceNameOthers;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriceMin(Long priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(Long priceMax) {
        this.priceMax = priceMax;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setSubServices(List<SubService> subServices) {
        this.subServices = subServices;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getServiceMappingId() {
        return serviceMappingId;
    }

    public String getServiceNameOthers() {
        return serviceNameOthers;
    }

    public String getDescription() {
        return description;
    }

    public Long getPriceMin() {
        return priceMin;
    }

    public Long getPriceMax() {
        return priceMax;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getEquipment() {
        return equipment;
    }

    public List<SubService> getSubServices() {
        return subServices;
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
            @SerializedName("subServiceLv2Id")
            private Long subServiceLv2Id;
            @SerializedName("subServiceLv1Id")
            private Long subServiceLv1Id;
            @SerializedName("name")
            private String name;
            @SerializedName("checked")
            private Boolean checked;

            public SubServiceLv2() {
            }

            public Long getSubServiceLv2Id() {
                return subServiceLv2Id;
            }

            public Long getSubServiceLv1Id() {
                return subServiceLv1Id;
            }

            public String getName() {
                return name;
            }

            public Boolean getChecked() {
                return checked;
            }

            public void setSubServiceLv2Id(Long subServiceLv2Id) {
                this.subServiceLv2Id = subServiceLv2Id;
            }

            public void setSubServiceLv1Id(Long subServiceLv1Id) {
                this.subServiceLv1Id = subServiceLv1Id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setChecked(Boolean checked) {
                this.checked = checked;
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

            public void setSubServiceLv1Id(Long subServiceLv1Id) {
                this.subServiceLv1Id = subServiceLv1Id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setServiceId(Long serviceId) {
                this.serviceId = serviceId;
            }

            public String getName() {
                return name;
            }

            public Long getSubServiceLv1Id() {
                return subServiceLv1Id;
            }

            public Long getServiceId() {
                return serviceId;
            }
        }
    }

}
