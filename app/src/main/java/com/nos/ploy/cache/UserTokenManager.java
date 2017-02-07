package com.nos.ploy.cache;

import android.content.Context;

import com.facebook.login.LoginManager;
import com.nos.ploy.DrawerController;
import com.nos.ploy.api.authentication.model.UserTokenGson;

/**
 * Created by Saran on 19/12/2559.
 */

public class UserTokenManager extends SharePreferenceUtils {
    public static boolean isLogin(Context context) {
        if (null == context) {
            return false;
        }
        UserTokenGson.Data token = getToken(context);
        return null != token;
    }

    public static UserTokenGson.Data getToken(Context context) {
        if (null == context) {
            return null;
        }
        return getUserTokenGson(context);
    }

    public static void saveToken(Context context, UserTokenGson.Data token) {
        if (null == context) {
            return;
        }
        saveUserTokenGson(context, token);
    }

    public static void clearData(Context context) {
        SharePreferenceUtils.with(context, USER).edit().clear().apply();
        LoginManager.getInstance().logOut();
        DrawerController.PLOYER_MENUS.remove(DrawerController.MENU_ACCOUNT);
    }
}
