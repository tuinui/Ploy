
package com.nos.ploy.api.masterdata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

public class LanguageAppLabelGson_Deprecated extends BaseResponse<LanguageAppLabelGson_Deprecated.Data>{


    public class Data {
        @SerializedName("id")
        public int id;
        @SerializedName("lgCode")
        public String lgCode;
        @SerializedName("appName")
        public String appName;
        @SerializedName("searchService")
        public String searchService;
        @SerializedName("searchJob")
        public String searchJob;
        @SerializedName("logIn")
        public String logIn;
        @SerializedName("signUp")
        public String signUp;
        @SerializedName("signUpWithFacebook")
        public String signUpWithFacebook;
        @SerializedName("firstName")
        public String firstName;
        @SerializedName("lastName")
        public String lastName;
        @SerializedName("birthDay")
        public String birthDay;
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;
        @SerializedName("passwordReset")
        @Expose
        public String passwordReset;
        @SerializedName("createAccount")
        public String createAccount;
        @SerializedName("ployee")
        public String ployee;
        @SerializedName("serviceName")
        public String serviceName;
        @SerializedName("serviceDescription")
        public String serviceDescription;
        @SerializedName("servicePrice")
        public String servicePrice;
        @SerializedName("serviceCertificate")
        public String serviceCertificate;
        @SerializedName("serviceEquipment")
        public String serviceEquipment;
        @SerializedName("serviceOther")
        public String serviceOther;
        @SerializedName("menuSetting")
        public String menuSetting;
        @SerializedName("menuAccount")
        public String menuAccount;
        @SerializedName("menuAvai")
        public String menuAvai;
        @SerializedName("menuWhatIsPloyer")
        public String menuWhatIsPloyer;
        @SerializedName("menuWhatIsPloyee")
        public String menuWhatIsPloyee;
        @SerializedName("switchToPloyee")
        public String switchToPloyee;
        @SerializedName("switchToPloyer")
        public String switchToPloyer;
        @SerializedName("profile")
        public String profile;
        @SerializedName("profileAboutMe")
        public String profileAboutMe;
        @SerializedName("profileEdu")
        public String profileEdu;
        @SerializedName("profileWork")
        public String profileWork;
        @SerializedName("profileInterest")
        public String profileInterest;
        @SerializedName("profileLanguage")
        public String profileLanguage;
        @SerializedName("profileTransport")
        public String profileTransport;
        @SerializedName("viewOnlyProfile")
        public String viewOnlyProfile;
        @SerializedName("profileUploadPhoto")
        public String profileUploadPhoto;
        @SerializedName("profileLanguageSpoken")
        public String profileLanguageSpoken;
        @SerializedName("setting")
        public String setting;
        @SerializedName("settingLogout")
        public String settingLogout;
        @SerializedName("settingLanguage")
        public String settingLanguage;
        @SerializedName("account")
        public String account;
        @SerializedName("accountChangePassword")
        public String accountChangePassword;
        @SerializedName("accountPhoneNumber")
        public String accountPhoneNumber;
        @SerializedName("accountSelectCountry")
        public String accountSelectCountry;
        @SerializedName("avaiSun")
        public String avaiSun;
        @SerializedName("avaiMon")
        public String avaiMon;
        @SerializedName("avaiTue")
        public String avaiTue;
        @SerializedName("avaiWed")
        public String avaiWed;
        @SerializedName("avaiThu")
        public String avaiThu;
        @SerializedName("avaiFri")
        public String avaiFri;
        @SerializedName("avaiSat")
        public String avaiSat;
        @SerializedName("avaiHoloday")
        public String avaiHoloday;
        @SerializedName("filter")
        public String filter;
        @SerializedName("filterClear")
        public String filterClear;
        @SerializedName("localization")
        public String localization;
        @SerializedName("review")
        public String review;
        @SerializedName("reviewOverall")
        public String reviewOverall;
        @SerializedName("reviewLeavAReview")
        public String reviewLeavAReview;
        @SerializedName("reviewCompetence")
        public String reviewCompetence;
        @SerializedName("reviewPunctuality")
        public String reviewPunctuality;
        @SerializedName("reviewPoliteness")
        public String reviewPoliteness;
        @SerializedName("reviewCommunication")
        public String reviewCommunication;
        @SerializedName("reviewProfessionalism")
        public String reviewProfessionalism;

        public Data() {
        }

        public String getAccount() {
            return account;
        }

        public String getAccountChangePassword() {
            return accountChangePassword;
        }

        public String getAccountPhoneNumber() {
            return accountPhoneNumber;
        }

        public String getAccountSelectCountry() {
            return accountSelectCountry;
        }

        public String getAppName() {
            return appName;
        }

        public String getAvaiFri() {
            return avaiFri;
        }

        public String getAvaiHoloday() {
            return avaiHoloday;
        }

        public String getAvaiMon() {
            return avaiMon;
        }

        public String getAvaiSat() {
            return avaiSat;
        }

        public String getAvaiSun() {
            return avaiSun;
        }

        public String getAvaiThu() {
            return avaiThu;
        }

        public String getAvaiTue() {
            return avaiTue;
        }

        public String getAvaiWed() {
            return avaiWed;
        }

        public String getBirthDay() {
            return birthDay;
        }

        public String getCreateAccount() {
            return createAccount;
        }

        public String getEmail() {
            return email;
        }

        public String getFilter() {
            return filter;
        }

        public String getFilterClear() {
            return filterClear;
        }

        public String getFirstName() {
            return firstName;
        }

        public int getId() {
            return id;
        }

        public String getLastName() {
            return lastName;
        }

        public String getLgCode() {
            return lgCode;
        }

        public String getLocalization() {
            return localization;
        }

        public String getLogIn() {
            return logIn;
        }

        public String getMenuAccount() {
            return menuAccount;
        }

        public String getMenuAvai() {
            return menuAvai;
        }

        public String getMenuSetting() {
            return menuSetting;
        }

        public String getMenuWhatIsPloyee() {
            return menuWhatIsPloyee;
        }

        public String getMenuWhatIsPloyer() {
            return menuWhatIsPloyer;
        }

        public String getPassword() {
            return password;
        }

        public String getPasswordReset() {
            return passwordReset;
        }

        public String getPloyee() {
            return ployee;
        }

        public String getProfile() {
            return profile;
        }

        public String getProfileAboutMe() {
            return profileAboutMe;
        }

        public String getProfileEdu() {
            return profileEdu;
        }

        public String getProfileInterest() {
            return profileInterest;
        }

        public String getProfileLanguage() {
            return profileLanguage;
        }

        public String getProfileLanguageSpoken() {
            return profileLanguageSpoken;
        }

        public String getProfileTransport() {
            return profileTransport;
        }

        public String getProfileUploadPhoto() {
            return profileUploadPhoto;
        }

        public String getProfileWork() {
            return profileWork;
        }

        public String getReview() {
            return review;
        }

        public String getReviewCommunication() {
            return reviewCommunication;
        }

        public String getReviewCompetence() {
            return reviewCompetence;
        }

        public String getReviewLeavAReview() {
            return reviewLeavAReview;
        }

        public String getReviewOverall() {
            return reviewOverall;
        }

        public String getReviewPoliteness() {
            return reviewPoliteness;
        }

        public String getReviewProfessionalism() {
            return reviewProfessionalism;
        }

        public String getReviewPunctuality() {
            return reviewPunctuality;
        }

        public String getSearchJob() {
            return searchJob;
        }

        public String getSearchService() {
            return searchService;
        }

        public String getServiceCertificate() {
            return serviceCertificate;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public String getServiceEquipment() {
            return serviceEquipment;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getServiceOther() {
            return serviceOther;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public String getSetting() {
            return setting;
        }

        public String getSettingLanguage() {
            return settingLanguage;
        }

        public String getSettingLogout() {
            return settingLogout;
        }

        public String getSignUp() {
            return signUp;
        }

        public String getSignUpWithFacebook() {
            return signUpWithFacebook;
        }

        public String getSwitchToPloyee() {
            return switchToPloyee;
        }

        public String getSwitchToPloyer() {
            return switchToPloyer;
        }

        public String getViewOnlyProfile() {
            return viewOnlyProfile;
        }
    }
}
