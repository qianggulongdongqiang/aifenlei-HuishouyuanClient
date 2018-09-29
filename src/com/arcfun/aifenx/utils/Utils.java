package com.arcfun.aifenx.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.AddOwnerInfo;
import com.arcfun.aifenx.data.AllOrderInfo;
import com.arcfun.aifenx.data.AreaDetailInfo;
import com.arcfun.aifenx.data.AreaInfo;
import com.arcfun.aifenx.data.GoodInfo;
import com.arcfun.aifenx.data.HistoryGoodInfo;
import com.arcfun.aifenx.data.HistoryOrderInfo;
import com.arcfun.aifenx.data.OwnerInfo;
import com.arcfun.aifenx.data.PhoneInfo;
import com.arcfun.aifenx.data.PreGoodInfo;
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.data.RawOwnerInfo;
import com.arcfun.aifenx.data.ResultInfo;
import com.arcfun.aifenx.data.ScoreInfo;

public class Utils {
    private static final String TAG = "Utils";
    public static final String DIR = "goods";

    public static final long PERIOD_EXPIRED = 29 * 24 * 3600 * 1000L;

    /** 0=fail */
    public static int RESULT_FAIL = 0;
    /** 1=success; */
    public static int RESULT_OK = 1;
    /** 2=not login */
    public static int RESULT_UNKNOWN = 2;

    public static Toast mToast = null;

    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    private static final String PHONE_NUMBER_REGEX = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";

    public static String decode(String bytes) {
        String hexString = "0123456789ABCDEF";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    public static String formatPhoneNumber(String args) {
        Matcher matcher = Pattern.compile(PHONE_NUMBER_REGEX).matcher(args);
        if (matcher.find()) {
            return matcher.group().replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})",
                    "*");
        }
        return args;
    }

    public static String formatArrayList(List<String> array) {
        if (array == null || array.size() == 0)
            return "";

        StringBuilder buffer = new StringBuilder();
        int offset = array.size() - 1;
        for (int i = 0; i < offset; i++) {
            buffer.append(array.get(i)).append(",");
        }
        buffer.append(array.get(offset));
        return buffer.toString();
    }

    public static int formatInt(float orginal) {
        BigDecimal b = new BigDecimal(orginal);
        return b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static float formatFloat(float orginal) {
        BigDecimal b = new BigDecimal(orginal);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static void showMsg(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showMsg(Context context, CharSequence msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showMsg(Context context, int id) {
        if (mToast == null) {
            mToast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(id);
        }
        mToast.show();
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     * */
    private final static String MOBILE_NUMBER_CHARS = "^[+0-9][-0-9]{1,}$";

    public static boolean isValidMobileNumber(String s) {
        if (TextUtils.isEmpty(s))
            return true;
        Pattern p = Pattern.compile(MOBILE_NUMBER_CHARS);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }

    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static boolean isReadableASCII(CharSequence string) {
        if (TextUtils.isEmpty(string))
            return false;
        try {
            Pattern p = Pattern.compile("[\\x20-\\x7E]+");
            return p.matcher(string).matches();
        } catch (Throwable e) {
            return true;
        }
    }

    public static String getDeviceId(Context context) {
        return JPushInterface.getUdid(context);
    }

    public static String getRegistrationID(Context context) {
        return JPushInterface.getRegistrationID(context);
    }

    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return imei;
    }

    public static String buildAccountJson(String account, String password) {
        JSONObject object = new JSONObject();
        try {
            object.put("username", account);
            object.put("password", password);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildRfidJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildRfidJson:" + object.toString());
        return object.toString();
    }

    public static RawOwnerInfo parseOwnerInfo(String json) {
        RawOwnerInfo result = null;
        OwnerInfo info = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == RESULT_OK) {
                JSONObject object = jsonObject.getJSONObject("data");
                info = new OwnerInfo(object.getInt("id"),
                        object.getString("user_nickname"),
                        object.getString("token"),
                        object.getBoolean("isMaster"));
            }
            result = new RawOwnerInfo(code, msg, info);
        } catch (Exception e) {
            LogUtils.e(TAG, "parseResultData:" + e.toString());
        }
        return result;
    }

    public static String buildAddOwnerJson(AddOwnerInfo info) {
        JSONObject object = new JSONObject();
        try {
            if (!TextUtils.isEmpty(info.getAccount())) {
                object.put("rfids", info.getAccount());
            }
            object.put("name", info.getName());
            object.put("sex", info.getSex());
            object.put("mobile", info.getMobile());
            object.put("addr", info.getAddr());
            object.put("token", info.getToken());
        } catch (Exception e) {
            LogUtils.e(TAG, "buildAddOwnerJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildAddOwnerJson:" + object.toString());
        return object.toString();
    }

    public static ResultInfo parseResultInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == RESULT_OK) {
                return new ResultInfo(code, msg);
            } else if (!TextUtils.isEmpty(msg)) {
                return new ResultInfo(code, msg);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseResultData:" + e.toString());
        }
        return null;
    }

    public static List<AreaInfo> parseArea(String json) {
        List<AreaInfo> areaInfos = new ArrayList<AreaInfo>();
        List<AreaDetailInfo> detailInfos = new ArrayList<AreaDetailInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONArray jsonArray = jsonObject.getJSONArray("data")
                        .getJSONObject(0).getJSONArray("items");// shanghai
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject cityObject = jsonArray.getJSONObject(i);
                    int cityId = cityObject.getInt("id");
                    String city = cityObject.getString("name");
                    JSONArray districtArray = cityObject.getJSONArray("items");
                    for (int j = 0; j < districtArray.length(); j++) {
                        JSONObject object = districtArray.getJSONObject(j);
                        AreaDetailInfo detail = new AreaDetailInfo(
                                object.getInt("id"), object.getString("name"));
                        detailInfos.add(detail);
                    }
                    areaInfos.add(new AreaInfo(cityId, city, detailInfos));
                }

                return areaInfos;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseArea:" + e.toString());
        }
        return null;
    }

    public static List<GoodInfo> parseJsonData(String json) {
        List<GoodInfo> goods = new ArrayList<GoodInfo>();
        GoodInfo info = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray result = new JSONArray(jsonArray.getString(i));
                    for (int j = 0; j < result.length(); j++) {
                        JSONObject object = result.getJSONObject(j);
                        info = new GoodInfo(object.getInt("id"),
                                object.getString("name"),
                                object.getString("unit_name"),
                                object.getString("unit"),
                                Float.parseFloat(object
                                        .getString("purchasing_point")));
                        goods.add(info);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseJsonData:" + e.toString());
        }
        return goods;
    }

    public static String buildAddOrderJson(String token, String phone,
            List<GoodInfo> goods, String sn, String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("mobile", phone);
            JSONArray array = new JSONArray();
            for (int i = 0; i < goods.size(); i++) {
                JSONObject good = new JSONObject();
                good.put("id", goods.get(i).getId());
                good.put("num", goods.get(i).getVendor());
                good.put("name", goods.get(i).getName());
                array.put(good);
            }
            object.put("goods", array);
            object.put("type", 2);
            if (sn != null && sn.length() > 0) {
                object.put("sn", sn);
            }
            if (code != null && code.length() > 0) {
                object.put("code", code);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "buildAddOrderJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildAddOrderJson:" + object.toString());
        return object.toString();
    }

    public static AllOrderInfo parseAddOrder(String json) {
        AllOrderInfo orderInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray goods = dataObject.getJSONArray("goods");
                GoodInfo[] goodList = new GoodInfo[goods.length()];
                for (int i = 0; i < goods.length(); i++) {
                    JSONObject good = goods.getJSONObject(i);
                    goodList[i] = new GoodInfo(good.getInt("id"),
                            good.getString("name"),
                            good.getString("unit_name"),
                            good.getString("unit"),
                            Float.valueOf(good.getString("p")),
                            Float.valueOf(good.getString("num")));
                }
                orderInfo = new AllOrderInfo(jsonObject.getInt("code"),
                        dataObject.getString("order_sn"),
                        dataObject.getString("finished_time"),
                        dataObject.getString("buyer_name"),
                        dataObject.getString("buyer_addr"),
                        dataObject.getString("buyer_phone"), goodList,
                        dataObject.getInt("points_number"),
                        dataObject.getInt("user_score"));
            } else {
                orderInfo = new AllOrderInfo(jsonObject.getInt("code"),
                        jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseAddOrder:" + e.toString());
        }
        return orderInfo;
    }

    public static String buildPreOrderJson(String token, int pages, int num, int type) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("p", pages);
            object.put("s", num);
            object.put("type", type);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildPreOrderJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildPreOrderJson:" + object.toString());
        return object.toString();
    }

    public static List<PreOrderInfo> parsePreOrder(String json) {
        List<PreOrderInfo> preOrderInfos = new ArrayList<PreOrderInfo>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray items = dataObject.getJSONArray("items");
                LogUtils.d(TAG,
                        "parsePreOrder total:" + dataObject.getInt("total"));
                for (int i = 0; i < items.length(); i++) {
                    JSONObject object = items.getJSONObject(i);
                    JSONArray goods = object.getJSONArray("goods");
                    PreGoodInfo[] goodList = new PreGoodInfo[goods.length()];
                    for (int j = 0; j < goods.length(); j++) {
                        JSONObject good = goods.getJSONObject(j);
                        goodList[j] = new PreGoodInfo(good.getString("id"),
                                Float.valueOf(good.getString("num")),
                                good.getString("name"));
                    }
                    JSONObject user = object.getJSONObject("user");
                    preOrderInfos.add(new PreOrderInfo(
                            object.getInt("order_id"),
                            object.getString("order_sn"),
                            object.getInt("collecter_id"),
                            object.getString("collecter_name"),
                            object.getString("finished_time"),
                            object.getString("buyer_name"),
                            object.getString("buyer_addr"),
                            object.getString("buyer_phone"),
                            goodList, object.getInt("addition"),
                            user.getString("mobile"),
                            user.getInt("rfid_count")));
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parsePreOrder:" + e.toString());
        }
        return preOrderInfos;
    }

    public static String buildAllMobile(String token, String key) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            if (key != null && key.length() > 0) {
                object.put("key", key);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "buildAllMobile:" + e.toString());
        }
        LogUtils.d(TAG, "buildAllMobile:" + object.toString());
        return object.toString();
    }

    public static List<PhoneInfo> parseAllMobile(String json) {
        List<PhoneInfo> mobiles = new ArrayList<PhoneInfo>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray users = dataObject.getJSONArray("user");
                for (int i = 0; i < users.length(); i++) {
                    mobiles.add(new PhoneInfo(users.getJSONObject(i).getString(
                            "mobile"), users.getJSONObject(i).getString("name")));
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseAllMobile:" + e.toString());
        }
        return mobiles;
    }

    public static String buildAllOrderJson(String token, int pages, int num,
            String from, String to) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("p", pages);
            object.put("s", num);
            object.put("from", from);
            object.put("to", to);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildAllOrderJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildAllOrderJson:" + object.toString());
        return object.toString();
    }

    public static List<HistoryOrderInfo> parseAllOrder(Context c, String json) {
        List<HistoryOrderInfo> allOrderInfos = new ArrayList<HistoryOrderInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray items = dataObject.getJSONArray("items");
                LogUtils.d(TAG,
                        "parseAllOrder total:" + dataObject.getInt("total"));
                for (int i = 0; i < items.length(); i++) {
                    JSONObject object = items.getJSONObject(i);
                    JSONArray goods = object.getJSONArray("goods");
                    HistoryGoodInfo[] goodList = new HistoryGoodInfo[goods
                            .length()];
                    for (int j = 0; j < goods.length(); j++) {
                        JSONObject good = goods.getJSONObject(j);
                        goodList[j] = new HistoryGoodInfo(good.getInt("id"),
                                good.getString("name"), good.getString("num"),
                                good.getString("unit"), Float.valueOf(good
                                        .getString("p")), good.getInt("point"));
                    }
                    JSONObject user = object.getJSONObject("user");
                    allOrderInfos.add(new HistoryOrderInfo(
                            object.getString("order_sn"),
                            Utils.timeAPFormat(c, object.getLong("finished_time")),
                            object.getString("collecter_name"),
                            object.getString("buyer_name"),
                            object.getString("buyer_addr"),
                            object.getInt("addition"), goodList,
                            user.getString("mobile"),
                            dataObject.getInt("point"),
                            user.getInt("rfid_count")));
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseAllOrder:" + e.toString());
        }
        return allOrderInfos;
    }

    public static String buildQueryScoreJson(String token, String mobile) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("mobile", mobile);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildQueryScoreJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildQueryScoreJson:" + object.toString());
        return object.toString();
    }

    public static String buildScoreCodeJson(String mobile) {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", mobile);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildScoreCodeJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildScoreCodeJson:" + object.toString());
        return object.toString();
    }

    public static ScoreInfo parseScoreCode(String json) {
        ScoreInfo scoreInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                scoreInfo = new ScoreInfo(code, jsonObject.getString("msg"),
                        dataObject.getInt("id"),
                        dataObject.getString("name"),
                        dataObject.getString("mobile"),
                        dataObject.getInt("score"));
            } else {
                scoreInfo = new ScoreInfo(code, jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseScoreCode:" + e.toString());
        }
        return scoreInfo;
    }

    public static String buildAddScoreJson(String token, int id, String code,
            int point, String type) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("user_id", id);
            object.put("type", 2);
            object.put("captcha", code);
            object.put("point", point);
            object.put("ctype", type);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildAddScoreJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildAddScoreJson:" + object.toString());
        return object.toString();
    }

    public static ScoreInfo parseAddScore(String json) {
        ScoreInfo scoreInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                scoreInfo = new ScoreInfo(code, jsonObject.getString("msg"),
                        dataObject.getInt("id"),
                        dataObject.getString("name"),
                        dataObject.getString("mobile"),
                        dataObject.getInt("score"));
            } else {
                scoreInfo = new ScoreInfo(code, jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseAddScore:" + e.toString());
        }
        return scoreInfo;
    }

    public static String buildId(String token, String id) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("id", id);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildId:" + e.toString());
        }
        LogUtils.d(TAG, "buildId:" + object.toString());
        return object.toString();
    }

    public static PreOrderInfo parseNotification(String json) {
        PreOrderInfo preOrderInfo = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == RESULT_OK) {
                JSONObject object = jsonObject.getJSONObject("data");
                JSONArray goods = object.getJSONArray("goods");
                PreGoodInfo[] goodList = new PreGoodInfo[goods.length()];
                for (int j = 0; j < goods.length(); j++) {
                    JSONObject good = goods.getJSONObject(j);
                    goodList[j] = new PreGoodInfo(good.getString("id"),
                            Float.valueOf(good.getString("num")),
                            good.getString("name"));
                }
                JSONObject user = object.getJSONObject("user");
                preOrderInfo = new PreOrderInfo(
                        object.getInt("order_id"),
                        object.getString("order_sn"),
                        object.getInt("collecter_id"),
                        object.getString("collecter_name"),
                        object.getString("finished_time"),
                        object.getString("buyer_name"),
                        object.getString("buyer_addr"),
                        object.getString("buyer_phone"),
                        goodList, object.getInt("addition"),
                        user.getString("mobile"),
                        user.getInt("rfid_count"));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parseNotification:" + e.toString());
        }
        return preOrderInfo;
    }

    public static String buildPushCode(String token, String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("code", code);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildPushCode:" + e.toString());
        }
        LogUtils.d(TAG, "buildPushCode:" + object.toString());
        return object.toString();
    }

    public static ResultInfo parsePushCode(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == RESULT_OK) {
                return new ResultInfo(code, msg);
            } else if (!TextUtils.isEmpty(msg)) {
                return new ResultInfo(code, msg);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "parsePushCode:" + e.toString());
        }
        return null;
    }

    public static String buildTokenIdJson(String token, int id) {
        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("id", id);
        } catch (Exception e) {
            LogUtils.e(TAG, "buildTokenIdJson:" + e.toString());
        }
        LogUtils.d(TAG, "buildTokenIdJson:" + object.toString());
        return object.toString();
    }

    public static void dumpJson2Db(Context context, byte[] data, String name) {
        File cache = context.getExternalFilesDir(DIR);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        File file = new File(cache, name);
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.e(TAG, "createNewFile:" + e.toString());
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (Exception e) {
            LogUtils.e(TAG, "dumpJson2Db:" + e.toString());
        }
    }

    public static byte[] getdumpFromDb(Context context, String name) {
        byte[] buffer = new byte[4096];
        File cache = context.getExternalFilesDir(DIR);
        File file = new File(cache, name);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            fis.close();
            return out.toByteArray();
        } catch (IOException e) {
            LogUtils.e(TAG, "getdumpFromDb:" + e.toString());
        }
        return null;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String timeFormat(final long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(time * 1000));
    }

    /** 0=AM;1=PM */
    public static String timeAPFormat(Context context, final long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        int apm = calendar.get(Calendar.AM_PM);
        return sdf.format(calendar.getTime())
                + " " + context.getResources().getString(
                (apm == 0) ? R.string.date_am : R.string.date_pm);
    }

    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        String today = sdf.format(cal.getTime());
        System.out.println("today = " + today);
        return today;
    }

    public static String getWeekFirstDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        String firstWeek = sdf.format(cal.getTime());
        System.out.println("today week = " + firstWeek);
        return firstWeek;
    }

    public static String getMonthFirstDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        int dayMonth = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DATE, - dayMonth + 1);
        String firstMonth = sdf.format(cal.getTime());
        System.out.println("today month = " + firstMonth);
        return firstMonth;
    }

    public static boolean isValidNumber(final String number) {
        return isPhone(number) || isMobile(number);
    }

    private static boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static boolean isMobile(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        String number = normalizeNumber(str);
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3-9][0-9]{9}$");
        m = p.matcher(number);
        b = m.matches();
        return b;
    }

    private static final SparseIntArray KEYPAD_MAP = new SparseIntArray();
    static {
        KEYPAD_MAP.put('a', '2'); KEYPAD_MAP.put('b', '2'); KEYPAD_MAP.put('c', '2');
        KEYPAD_MAP.put('A', '2'); KEYPAD_MAP.put('B', '2'); KEYPAD_MAP.put('C', '2');

        KEYPAD_MAP.put('d', '3'); KEYPAD_MAP.put('e', '3'); KEYPAD_MAP.put('f', '3');
        KEYPAD_MAP.put('D', '3'); KEYPAD_MAP.put('E', '3'); KEYPAD_MAP.put('F', '3');

        KEYPAD_MAP.put('g', '4'); KEYPAD_MAP.put('h', '4'); KEYPAD_MAP.put('i', '4');
        KEYPAD_MAP.put('G', '4'); KEYPAD_MAP.put('H', '4'); KEYPAD_MAP.put('I', '4');

        KEYPAD_MAP.put('j', '5'); KEYPAD_MAP.put('k', '5'); KEYPAD_MAP.put('l', '5');
        KEYPAD_MAP.put('J', '5'); KEYPAD_MAP.put('K', '5'); KEYPAD_MAP.put('L', '5');

        KEYPAD_MAP.put('m', '6'); KEYPAD_MAP.put('n', '6'); KEYPAD_MAP.put('o', '6');
        KEYPAD_MAP.put('M', '6'); KEYPAD_MAP.put('N', '6'); KEYPAD_MAP.put('O', '6');

        KEYPAD_MAP.put('p', '7'); KEYPAD_MAP.put('q', '7'); KEYPAD_MAP.put('r', '7'); KEYPAD_MAP.put('s', '7');
        KEYPAD_MAP.put('P', '7'); KEYPAD_MAP.put('Q', '7'); KEYPAD_MAP.put('R', '7'); KEYPAD_MAP.put('S', '7');

        KEYPAD_MAP.put('t', '8'); KEYPAD_MAP.put('u', '8'); KEYPAD_MAP.put('v', '8');
        KEYPAD_MAP.put('T', '8'); KEYPAD_MAP.put('U', '8'); KEYPAD_MAP.put('V', '8');

        KEYPAD_MAP.put('w', '9'); KEYPAD_MAP.put('x', '9'); KEYPAD_MAP.put('y', '9'); KEYPAD_MAP.put('z', '9');
        KEYPAD_MAP.put('W', '9'); KEYPAD_MAP.put('X', '9'); KEYPAD_MAP.put('Y', '9'); KEYPAD_MAP.put('Z', '9');
    }
    public static String normalizeNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            // Character.digit() supports ASCII and Unicode digits (fullwidth, Arabic-Indic, etc.)
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                sb.append(digit);
            } else if (sb.length() == 0 && c == '+') {
                sb.append(c);
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                sb.append((char) KEYPAD_MAP.get(c, c));;
            }
        }
        return sb.toString();
    }
}