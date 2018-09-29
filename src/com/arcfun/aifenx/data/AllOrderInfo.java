package com.arcfun.aifenx.data;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class AllOrderInfo implements Parcelable {
    private int code;
    private String msg;
    /** order_sn */
    private String sn;
    /** collecter_id */
    private String collecter_id;
    /** collecter_name */
    private String collecter_name;
    /** buyer_id */
    private int buyer_id;
    /** buyer_name */
    private String buyer_name;
    /** buyer_addr */
    private String buyer_addr;
    /** buyer_phone */
    private String buyer_phone;
    /** add_time */
    private long add_time;
    /** finished_time */
    private String finished_time;
    /** points_number */
    private int points_number;
    /** order_from */
    private int order_from;
    /** user_score */
    private int user_score;
    /** goods */
    private GoodInfo[] goods;

    public AllOrderInfo() {
    }

    public AllOrderInfo(int _code, String _msg) {
        this.code = _code;
        this.msg = _msg;
    }

    public AllOrderInfo(int _code, String _sn, String _time, String _name,
            String _addr, String _phone, GoodInfo[] pres, int points, int score) {
        this.code = _code;
        this.sn = _sn;
        this.buyer_name = _name;
        this.buyer_addr = _addr;
        this.finished_time = _time;
        this.buyer_phone = _phone;
        this.goods = pres;
        this.points_number = points;
        this.user_score = score;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int _code) {
        this.code = _code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String _msg) {
        this.msg = _msg;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCollecterId() {
        return collecter_id;
    }

    public void setCollecterId(String collecter_id) {
        this.collecter_id = collecter_id;
    }

    public String getCollecterName() {
        return collecter_name;
    }

    public void setCollecterName(String collecter_name) {
        this.collecter_name = collecter_name;
    }

    public float getBuyerId() {
        return buyer_id;
    }

    public long getAddTime() {
        return add_time;
    }

    public void setAddTime(long add_time) {
        this.add_time = add_time;
    }

    public int getPointsNumber() {
        return points_number;
    }

    public void setPointsNumber(int points_number) {
        this.points_number = points_number;
    }

    public String getBuyerName() {
        return buyer_name;
    }

    public void setBuyerName(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyerAddr() {
        return buyer_addr;
    }

    public void setBuyerAddr(String buyer_addr) {
        this.buyer_addr = buyer_addr;
    }

    public String getBuyerPhone() {
        return buyer_phone;
    }

    public void setBuyerPhone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getFinishedTime() {
        return finished_time;
    }

    public void setFinishedTime(String finished_time) {
        this.finished_time = finished_time;
    }

    public void setBuyerId(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getOrderFrom() {
        return order_from;
    }

    public void setOrderFrom(int order_from) {
        this.order_from = order_from;
    }

    public GoodInfo[] getGood() {
        return goods;
    }

    public void setGood(GoodInfo[] preGood) {
        this.goods = preGood;
    }

    public int getUserScore() {
        return user_score;
    }

    public void setUserScore(int user_score) {
        this.user_score = user_score;
    }

    public static final Parcelable.Creator<AllOrderInfo> CREATOR = new Creator<AllOrderInfo>() {
        @Override
        public AllOrderInfo createFromParcel(Parcel source) {
            return new AllOrderInfo(source);
        }

        @Override
        public AllOrderInfo[] newArray(int size) {
            return new AllOrderInfo[size];
        }
    };

    protected AllOrderInfo(Parcel source) {
        sn = source.readString();
        buyer_name = source.readString();
        buyer_addr = source.readString();
        finished_time = source.readString();
        buyer_phone = source.readString();
        points_number = source.readInt();
        user_score = source.readInt();
        Parcelable[] parcelables = source.readParcelableArray(GoodInfo.class
                .getClassLoader());
        if (parcelables != null) {
            goods = Arrays.copyOf(parcelables, parcelables.length,
                    GoodInfo[].class);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeString(buyer_name);
        dest.writeString(buyer_addr);
        dest.writeString(finished_time);
        dest.writeString(buyer_phone);
        dest.writeInt(points_number);
        dest.writeInt(user_score);
        dest.writeParcelableArray(goods, flags);
    }

    @Override
    public String toString() {
        return "[sn=" + sn + ",finished_time=" + finished_time
                + ",buyer_phone=" + buyer_phone + ",goods=" + "]";
    }
}