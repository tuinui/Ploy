package com.nos.ploy.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 29/11/2559.
 */

public class SharePreferenceUtils {

    public static final int USER = 1;
    public static final String SHAREPREF_FILE_USER = "SHAREPREF_FILE_USER";
    private static final String KEY_ACCOUNT_GSON = "ACCOUNT_GSON";
    private static final String KEY_PROFILE_IMAGE_GSON = "PROFILE_IMAGE_GSON";
    private static final String KEY_USER_TOKEN_GSON = "USER_TOKEN_GSON";

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
            with(context, USER).edit().putString(KEY_USER_TOKEN_GSON, userTokenGsonString).apply();
        } catch (Exception e) {
            with(context, USER).edit().putString(KEY_USER_TOKEN_GSON, "").apply();
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

    public static List<ProfileImageGson.Data> getProfileImages(Context context) {
        List<ProfileImageGson.Data> results = new ArrayList<>();
        if (null == context) {
            return null;
        }
        String accountGsonString = with(context, USER).getString(KEY_PROFILE_IMAGE_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return null;
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

    private static String convertToFileName(@FileType int fileType) {
        switch (fileType) {

            case USER:
                return SHAREPREF_FILE_USER;
            default:
                return null;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({USER})
    public @interface FileType {
    }
}
