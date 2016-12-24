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
    private List<String> language = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
    @SerializedName("location")
    private ProfileGson.Data.Location location = new ProfileGson.Data.Location();
    @SerializedName("contactPhone")
    private boolean contactPhone;
    @SerializedName("contactEmail")
    private boolean contactEmail;

    public PostUpdateProfileGson() {
    }

    public PostUpdateProfileGson(ProfileGson.Data data, long userId) {
        if(null != data){
            userProfileId = data.getUserProfileId();
            this.userId = userId;
            aboutMe = data.getAboutMe();
            education = data.getEducation();
            work = data.getWork();
            interest = data.getInterest();
            language.clear();
            if(null != data.getLanguage()){
                for(ProfileGson.Data.Language language :  data.getLanguage()){
                    this.language.add(language.getSpokenLanguageCode());
                }
            }
            if(null != data.getTransport()){
                for(ProfileGson.Data.Transport transport : data.getTransport()){
                    this.transport.add(transport.getTransportId());
                }
            }
            if(null != data.getLocation()){
                location = new ProfileGson.Data.Location(data.getLocation().getLat(),data.getLocation().getLng());
            }
            contactPhone = data.isContactPhone();
            contactEmail = data.isContactEmail();
        }

    }

    public void setUserProfileId(Long userProfileId) {
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

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public void setTransport(List<Long> transport) {
        this.transport = transport;
    }

    public void setLocation(ProfileGson.Data.Location location) {
        this.location = location;
    }

    public void setContactPhone(boolean contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactEmail(boolean contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Long getUserProfileId() {
        return userProfileId;
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

    public List<String> getLanguage() {
        return language;
    }

    public List<Long> getTransport() {
        return transport;
    }

    public ProfileGson.Data.Location getLocation() {
        return location;
    }

    public boolean isContactPhone() {
        return contactPhone;
    }

    public boolean isContactEmail() {
        return contactEmail;
    }


}
