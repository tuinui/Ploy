package com.nos.ploy.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nos.ploy.DrawerController;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.api.masterdata.model.AppLanguageGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 29/11/2559.
 */

public class SharePreferenceUtils {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({USER, APP_SETTINGS})
    public @interface FileType {
    }

    public static final int USER = 1;
    public static final int APP_SETTINGS = 2;

    public static final String SHAREPREF_FILE_USER = "SHAREPREF_FILE_USER";
    public static final String SHAREPREF_FILE_APP = "SHAREPREF_FILE_APP";
    private static final String KEY_ACCOUNT_GSON = "ACCOUNT_GSON";
    private static final String KEY_PROFILE_IMAGE_GSON = "PROFILE_IMAGE_GSON";
    private static final String KEY_USER_TOKEN_GSON = "USER_TOKEN_GSON";
    private static final String KEY_APP_LANGUAGE_GSON = "APP_LANGUAGE_GSON";
    private static final String KEY_APP_LANGUAGE_LABEL_GSON_PREFIX = "APP_LANGUAGE_LABEL_GSON_";
    private static final String KEY_CURRENT_ACTIVE_APP_LANGUAGE = "CURRENT_APP_LANGUAGE";

    public static SharedPreferences with(Context context, @FileType int fileType) {
        return context.getSharedPreferences(convertToFileName(fileType), Context.MODE_PRIVATE);
    }

    protected static void saveUserTokenGson(Context context, UserTokenGson.Data userTokenGson) {
        if (null == context) {
            return;
        }
        String userTokenGsonString = "";
        try {
            userTokenGsonString = new Gson().toJson(userTokenGson, UserTokenGson.Data.class);
            with(context, USER).edit().putString(KEY_USER_TOKEN_GSON, userTokenGsonString).commit();
            DrawerController.PLOYER_MENUS.add(DrawerController.MENU_ACCOUNT);
        } catch (Exception e) {
            with(context, USER).edit().putString(KEY_USER_TOKEN_GSON, "").commit();
        }
    }

    protected static UserTokenGson.Data getUserTokenGson(Context context) {
        if (null == context) {
            return null;
        }
        String accountGsonString = with(context, USER).getString(KEY_USER_TOKEN_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return null;
        }

        return new Gson().fromJson(accountGsonString, UserTokenGson.Data.class);
    }

    public static void saveAccountGson(Context context, AccountGson.Data data) {
        if (null == context) {
            return;
        }
        String accountGsonString = new Gson().toJson(data, AccountGson.Data.class);
        with(context, USER).edit().putString(KEY_ACCOUNT_GSON, accountGsonString).apply();
    }

    public static void saveProfileImageGson(Context context, ProfileImageGson data) {
        if (null == context) {
            return;
        }
        String accountGsonString = new Gson().toJson(data, ProfileImageGson.class);
        with(context, USER).edit().putString(KEY_PROFILE_IMAGE_GSON, accountGsonString).apply();
    }

    public static void saveAppLanguageGson(Context context, AppLanguageGson data) {
        if (null == context) {
            return;
        }
        String appLanguageGsonString = new Gson().toJson(data, AppLanguageGson.class);
        with(context, APP_SETTINGS).edit().putString(KEY_APP_LANGUAGE_GSON, appLanguageGsonString).apply();
    }

    public static ArrayList<AppLanguageGson.Data> getAppLanguageList(Context context) {
        ArrayList<AppLanguageGson.Data> results = new ArrayList<>();
        if (null == context) {
            return results;
        }

        String accountGsonString = with(context, APP_SETTINGS).getString(KEY_APP_LANGUAGE_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return results;
        }

        AppLanguageGson data = new Gson().fromJson(accountGsonString, AppLanguageGson.class);
        if (null != data && null != data.getData()) {
            results.addAll(data.getData());
        }
        return results;
    }

    public static String getCurrentActiveAppLanguageCode(Context context) {
        String languageCode = "en";
        if (null == context) {
            return languageCode;
        }

        languageCode = with(context, APP_SETTINGS).getString(KEY_CURRENT_ACTIVE_APP_LANGUAGE, "en");

        return languageCode;
    }

    public static void setCurrentActiveAppLanguageCode(Context context, String languageCode) {
        if (null == context) {
            return;
        }

        with(context, APP_SETTINGS).edit().putString(KEY_CURRENT_ACTIVE_APP_LANGUAGE, languageCode).apply();
    }


    public static List<ProfileImageGson.Data> getProfileImages(Context context) {
        List<ProfileImageGson.Data> results = new ArrayList<>();
        if (null == context) {
            return results;
        }
        String accountGsonString = with(context, USER).getString(KEY_PROFILE_IMAGE_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return results;
        }
        ProfileImageGson data = new Gson().fromJson(accountGsonString, ProfileImageGson.class);
        if (null != data && null != data.getData()) {
            results.addAll(data.getData());
        }
        return results;
    }

    public static AccountGson.Data getAccountGson(Context context) {
        if (null == context) {
            return null;
        }
        String accountGsonString = with(context, USER).getString(KEY_ACCOUNT_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return null;
        }

        return new Gson().fromJson(accountGsonString, AccountGson.Data.class);
    }

    public static void saveAppLanguageLabelGson(Context context, String languageLabel, String lgCode) {
        if (null == context) {
            return;
        }
        try {
            with(context, APP_SETTINGS).edit().putString(KEY_APP_LANGUAGE_LABEL_GSON_PREFIX + lgCode, languageLabel).commit();
        } catch (Exception ignored) {

        }

    }

    public static LanguageAppLabelGson getAppLanguageLabelGson(Context context, String lgCode) {
        if (null == context) {
            return null;
        }
        try {
            String appLabel = with(context, APP_SETTINGS).getString(KEY_APP_LANGUAGE_LABEL_GSON_PREFIX + lgCode, "");
            return new Gson().fromJson(appLabel, LanguageAppLabelGson.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String convertToFileName(@FileType int fileType) {
        switch (fileType) {

            case USER:
                return SHAREPREF_FILE_USER;
            case APP_SETTINGS:
                return SHAREPREF_FILE_APP;
            default:
                return null;
        }
    }

}
