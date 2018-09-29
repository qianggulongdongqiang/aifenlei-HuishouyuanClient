package com.arcfun.aifenx.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.arcfun.aifenx.data.OwnerInfo;

public class SharedPreferencesUtils {
    private static final String TAG = "SharedPreferences";
    private static final String FILE_NAME = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_OWNER_NAME = "nickname";
    private static final String KEY_OWNER_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_MASTER = "isMaster";
    private static final String KEY_REGISTER = "isRegister";
    private static SharedPreferences sp = null;

    public static void setOwner(Context c, OwnerInfo info, long time) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        LogUtils.d(TAG, "set time=" + time + ", token=" + info.getToken());
        SharedPreferences.Editor editor = sp.edit();
        if (info != null) {
            editor.putString(KEY_TOKEN, info.getToken());
            editor.putString(KEY_OWNER_NAME, info.getName());
            editor.putInt(KEY_OWNER_ID, info.getId());
            editor.putBoolean(KEY_MASTER, info.isMaster());
            editor.putLong(KEY_TIME, time);
            editor.commit();
        }
    }

    public static void resetOwnerToken(Context c, String token) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_MASTER, false);
        editor.putLong(KEY_TIME, -1L);
        editor.putBoolean(KEY_REGISTER, false);
        editor.commit();
    }

    public static String getOwnerToken(Context c) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        long old = sp.getLong(KEY_TIME, -1L);
        long time = System.currentTimeMillis();
        LogUtils.d(TAG, "get old=" + old + ", time=" + time +
                ",diff=" + (time-old));
        if (old >0 && (time - old) < Utils.PERIOD_EXPIRED) {
            return sp.getString(KEY_TOKEN, null);
        }
        return null;
    }

    public static String getOwnerName(Context c) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(KEY_OWNER_NAME, null);
    }

    public static int getOwnerId(Context c) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(KEY_OWNER_ID, 0);
    }

    public static boolean getOwnerType(Context c) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(KEY_MASTER, false);
    }

    public static void setRegister(Context c, boolean able) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        LogUtils.d(TAG, "setRegister = " + able);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_REGISTER, able);
        editor.commit();
    }

    public static boolean getRegister(Context c) {
        if (sp == null) {
            sp = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(KEY_REGISTER, false);
    }
}