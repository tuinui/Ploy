package com.nos.ploy.api.account.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 25/12/2559.
 */

public class PostUpdateProfileGson {

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
    private ArrayList<String> language = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
    @SerializedName("location")
    private PloyeeProfileGson.Data.Location location;
    @SerializedName("contactPhone")
    private boolean contactPhone;
    @SerializedName("contactEmail")
    private boolean contactEmail;

    public PostUpdateProfileGson() {
    }

    public PostUpdateProfileGson(PloyeeProfileGson.Data data, long userId) {
        if (null != data) {
            userProfileId = data.getUserProfileId();
            this.userId = userId;
            aboutMe = data.getAboutMe();
            education = data.getEducation();
            work = data.getWork();
            interest = data.getInterest();
            language.clear();
            if (null != data.getLanguage()) {
                for (PloyeeProfileGson.Data.Language language : data.getLanguage()) {
                    this.language.add(language.getSpokenLanguageCode());
                }
            }
            if (null != data.getTransport()) {
                for (PloyeeProfileGson.Data.Transport transport : data.getTransport()) {
                    this.transport.add(transport.getTransportId());
                }
            }
            if (null != data.getLocation()) {
                location = new PloyeeProfileGson.Data.Location(data.getLocation().getLat(), data.getLocation().getLng());
            }
            contactPhone = data.isContactPhone();
            contactEmail = data.isContactEmail();
        }

    }

    public void addLanguage(String code) {
        if (language == null) {
            language = new ArrayList<>();
        }

        if (!language.contains(code)) {
            language.add(code);
        }

    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public ArrayList<String> getLanguage() {
        return language;
    }

    public void setLanguage(ArrayList<String> language) {
        this.language = language;
    }

    public List<Long> getTransport() {
        return transport;
    }

    public void setTransport(List<Long> transport) {
        this.transport = transport;
    }

    public PloyeeProfileGson.Data.Location getLocation() {
        return location;
    }

    public void setLocation(PloyeeProfileGson.Data.Location location) {
        this.location = location;
    }

    public boolean isContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(boolean contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(boolean contactEmail) {
        this.contactEmail = contactEmail;
    }


}
