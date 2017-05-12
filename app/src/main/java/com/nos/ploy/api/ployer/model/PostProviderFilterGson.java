package com.nos.ploy.api.ployer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 15/1/2560.
 */

public class PostProviderFilterGson implements Parcelable {
    //    public static final PostProviderFilterGson EMPTY_DATA = new PostProviderFilterGson();
    /*
    {
	"pageNo":1,
	"pageSize":10,
    "serviceId":1,
    "language": ["en"],
    "transport": [],
    "review":0,
    "priceMin": 0,
    "priceMax": 1000,
    "certificate": false,
    "equipment": false,
     "subServiceLV2IdList": [],
    "contactPhone": false,
    "contactEmail": false,
    "avai": [
   ]
  }
     */
    @SerializedName("pageNo")
    private long pageNo;
    @SerializedName("pageSize")
    private long pageSize;
    @SerializedName("serviceId")
    private long serviceId;
    @SerializedName("language")
    private ArrayList<String> languages = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transportIds = new ArrayList<>();
    @SerializedName("contactEmail")
    private boolean contactEmail;
    @SerializedName("contactPhone")
    private boolean contactPhone;
    @SerializedName("review")
    private Long review;
    @SerializedName("priceMin")
    private long priceMin;
    @SerializedName("priceMax")
    private long priceMax;
    @SerializedName("certificate")
    private boolean certificate;
    @SerializedName("equipment")
    private boolean equipment;

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    @SerializedName("subServiceLV2IdList")
    private List<Long> subServices = new ArrayList<>();
    @SerializedName("avai")
    private ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> availabilityItems = new ArrayList<>();

    private PostProviderFilterGson(ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> availabilityItems, boolean certificate, boolean contactEmail, boolean contactPhone, boolean equipment, ArrayList<String> languages, long pageNo, long pageSize, long priceMax, long priceMin, Long review, long serviceId, List<Long> subServices, List<Long> transportIds, double latitude, double longitude) {
        this.availabilityItems = availabilityItems;
        this.certificate = certificate;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.equipment = equipment;
        this.languages = languages;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.priceMax = priceMax;
        this.priceMin = priceMin;

        this.latitude = latitude;
        this.longitude = longitude;


        if (null == review) {
            this.review = 0L;
        } else {
            this.review = review;
        }

        this.serviceId = serviceId;
        this.subServices = subServices;
        this.transportIds = transportIds;
    }

    public PostProviderFilterGson cloneThis() {
        return new PostProviderFilterGson(new ArrayList<>(availabilityItems), isCertificate(), isContactEmail(), isContactPhone(), isEquipment(), new ArrayList<>(languages), pageNo, pageSize, getPriceMax(), getPriceMax(), review, getServiceId(), new ArrayList<>(subServices), new ArrayList<>(transportIds), latitude, longitude);
    }

    public PostProviderFilterGson() {
        priceMin = 0;
        priceMax = 1000;
        contactEmail = false;
        contactPhone = false;
        equipment = false;
        certificate = false;
        pageNo = 1;
        pageSize = 9999;
    }

    public void setAvailabilityItems(ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> availabilityItems) {
        this.availabilityItems = availabilityItems;
    }

    public void setCertificate(boolean certificate) {
        this.certificate = certificate;
    }

    public void setContactEmail(boolean contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactPhone(boolean contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setEquipment(boolean equipment) {
        this.equipment = equipment;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem> getAvailabilityItems() {
        return availabilityItems;
    }

    public boolean isCertificate() {
        return certificate;
    }

    public boolean isContactEmail() {
        return contactEmail;
    }

    public boolean isContactPhone() {
        return contactPhone;
    }

    public boolean isEquipment() {
        return equipment;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public long getPageNo() {
        return pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPriceMax() {
        return priceMax;
    }

    public long getPriceMin() {
        return priceMin;
    }

    public Long getReview() {
        return review;
    }

    public long getServiceId() {
        return serviceId;
    }




    public List<Long> getSubServices() {
        return subServices;
    }

    public List<Long> getTransportIds() {
        if (null == transportIds) {
            transportIds = new ArrayList<>();
        }
        return transportIds;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public void setPriceMax(long priceMax) {
        this.priceMax = priceMax;
    }

    public void setPriceMin(long priceMin) {
        this.priceMin = priceMin;
    }

    public void setReview(Long review) {
        this.review = review;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public void setSubServices(List<Long> subServices) {
        this.subServices = subServices;
    }

    public void setTransportIds(List<Long> transportIds) {
        this.transportIds = transportIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pageNo);
        dest.writeLong(this.pageSize);
        dest.writeLong(this.serviceId);
        dest.writeStringList(this.languages);
        dest.writeList(this.transportIds);
        dest.writeByte(this.contactEmail ? (byte) 1 : (byte) 0);
        dest.writeByte(this.contactPhone ? (byte) 1 : (byte) 0);
        dest.writeLong(this.review);
        dest.writeLong(this.priceMin);
        dest.writeLong(this.priceMax);
        dest.writeByte(this.certificate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.equipment ? (byte) 1 : (byte) 0);
        dest.writeList(this.subServices);
        dest.writeList(this.availabilityItems);

    }

    protected PostProviderFilterGson(Parcel in) {
        this.pageNo = in.readLong();
        this.pageSize = in.readLong();
        this.serviceId = in.readLong();
        this.languages = in.createStringArrayList();
        this.transportIds = new ArrayList<Long>();
        in.readList(this.transportIds, Long.class.getClassLoader());
        this.contactEmail = in.readByte() != 0;
        this.contactPhone = in.readByte() != 0;
        this.review = in.readLong();
        this.priceMin = in.readLong();
        this.priceMax = in.readLong();
        this.certificate = in.readByte() != 0;
        this.equipment = in.readByte() != 0;

        this.subServices = new ArrayList<Long>();
        in.readList(this.subServices, Long.class.getClassLoader());
        this.availabilityItems = new ArrayList<PloyeeAvailiabilityGson.Data.AvailabilityItem>();
        in.readList(this.availabilityItems, PloyeeAvailiabilityGson.Data.AvailabilityItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<PostProviderFilterGson> CREATOR = new Parcelable.Creator<PostProviderFilterGson>() {
        @Override
        public PostProviderFilterGson createFromParcel(Parcel source) {
            return new PostProviderFilterGson(source);
        }

        @Override
        public PostProviderFilterGson[] newArray(int size) {
            return new PostProviderFilterGson[size];
        }
    };

    public void addAllAvailabilityItem(List<PloyeeAvailiabilityGson.Data.AvailabilityItem> items) {
        if (availabilityItems == null) {
            availabilityItems = new ArrayList<>();
        }
        availabilityItems.clear();
        availabilityItems.addAll(items);
    }
}
