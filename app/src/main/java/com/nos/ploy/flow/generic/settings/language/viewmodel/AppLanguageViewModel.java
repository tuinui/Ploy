package com.nos.ploy.flow.generic.settings.language.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.nos.ploy.api.masterdata.model.AppLanguageGson;

/**
 * Created by Saran on 3/1/2560.
 */

public class AppLanguageViewModel implements Parcelable {
    private AppLanguageGson.Data data;
    private String languageName;
    private boolean isCurrentActive;
    private String languageCode;


    public AppLanguageViewModel(AppLanguageGson.Data data,String currentActiveLanguageCode) {
        this.data = data;
        if (null != data) {
            languageName = data.getName();
            languageCode = data.getCode();
            isCurrentActive = TextUtils.equals(currentActiveLanguageCode,data.getCode());
        }


    }

    public AppLanguageGson.Data getData(){
        return data;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public boolean isCurrentActive(){
        return isCurrentActive;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.languageName);
        dest.writeByte(this.isCurrentActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.languageCode);
    }

    protected AppLanguageViewModel(Parcel in) {
        this.data = in.readParcelable(AppLanguageGson.Data.class.getClassLoader());
        this.languageName = in.readString();
        this.isCurrentActive = in.readByte() != 0;
        this.languageCode = in.readString();
    }

    public static final Creator<AppLanguageViewModel> CREATOR = new Creator<AppLanguageViewModel>() {
        @Override
        public AppLanguageViewModel createFromParcel(Parcel source) {
            return new AppLanguageViewModel(source);
        }

        @Override
        public AppLanguageViewModel[] newArray(int size) {
            return new AppLanguageViewModel[size];
        }
    };
}
