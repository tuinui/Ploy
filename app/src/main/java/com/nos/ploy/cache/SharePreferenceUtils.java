package com.nos.ploy.cache;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nos.ploy.api.authentication.model.AccountGson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Saran on 29/11/2559.
 */

public class SharePreferenceUtils {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({USER})
    public @interface FileType {
    }

    public static final int USER = 1;

    public static final String SHAREPREF_FILE_USER = "SHAREPREF_FILE_USER";

    private static final String KEY_ACCOUNT_GSON = "ACCOUNT_GSON";

    public static SharedPreferences with(Context context, @FileType int fileType) {
        return context.getSharedPreferences(convertToFileName(fileType), Context.MODE_PRIVATE);
    }

    public static void saveAccountGson(Context context, AccountGson accountInfo) {
        if (null == context) {
            return;
        }
        String accountGsonString = new Gson().toJson(accountInfo, AccountGson.class);
        with(context, USER).edit().putString(KEY_ACCOUNT_GSON, accountGsonString).apply();
    }

    public static AccountGson getAccountGson(Context context) {
        if (null == context) {
            return null;
        }
        String accountGsonString = with(context, USER).getString(KEY_ACCOUNT_GSON, "");
        if (TextUtils.isEmpty(accountGsonString)) {
            return null;
        }

        return new Gson().fromJson(accountGsonString, AccountGson.class);
    }

    private static String convertToFileName(@FileType int fileType) {
        switch (fileType) {

            case USER:
                return SHAREPREF_FILE_USER;
            default:
                return null;
        }
    }
}
