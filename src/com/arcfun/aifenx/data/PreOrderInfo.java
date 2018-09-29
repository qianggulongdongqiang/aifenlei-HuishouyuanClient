package com.arcfun.aifenx.data;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * api/common/getPreOrders.html
 * 
 */
public class PreOrderInfo implements Parcelable {
    /** order_id */
    private int id;
    /** order_sn */
    private String sn;
    /** collecter_id */
    private int collecter_id;
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
    /** order_state */
    private int order_state;
    /** order_from */
    private int order_from;
    /** addition */
    private int addition;
    /** mobile */
    private String mobile;
    /** rfid_count */
    private int rfid_count;
    /** goods */
    private PreGoodInfo[] preGoods;
    private String goodsName;

    public PreOrderInfo() {
    }

    public PreOrderInfo(int _id, String _sn, int _collecterId,
            String _collecter, String _time, String _name, String _addr,
            String _phone, PreGoodInfo[] pres, int _addtion, String _mobile,
            int rfids) {
        this.id = _id;
        this.sn = _sn;
        this.collecter_id = _collecterId;
        this.collecter_name = _collecter;
        this.buyer_name = _name;
        this.buyer_addr = _addr;
        this.finished_time = _time;
        this.buyer_phone = _phone;
        this.preGoods = pres;
        this.goodsName = getCategory(pres);
        this.addition = _addtion;
        this.mobile = _mobile;
        this.rfid_count = rfids;
    }

    private String getCategory(PreGoodInfo[] pres) {
        StringBuffer category = new StringBuffer();
        for (int i = 0; i < pres.length; i++) {
            category.append(pres[i].getName() + " ");
        }
        return category.toString();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getCollecterId() {
        return collecter_id;
    }

    public void setCollecterId(int collecter_id) {
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

    public int getOrderState() {
        return order_state;
    }

    public void setOrderState(int order_state) {
        this.order_state = order_state;
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

    public PreGoodInfo[] getPreGood() {
        return preGoods;
    }

    public void setPreGood(PreGoodInfo[] preGood) {
        this.preGoods = preGood;
    }

    public int getAddition() {
        return addition;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getRfidCount() {
        return rfid_count;
    }

    public void setRfidCount(int rfid_count) {
        this.rfid_count = rfid_count;
    }

    public static final Parcelable.Creator<PreOrderInfo> CREATOR = new Creator<PreOrderInfo>() {
        @Override
        public PreOrderInfo createFromParcel(Parcel source) {
            return new PreOrderInfo(source);
        }

        @Override
        public PreOrderInfo[] newArray(int size) {
            return new PreOrderInfo[size];
        }
    };

    protected PreOrderInfo(Parcel source) {
        id = source.readInt();
        sn = source.readString();
        collecter_id = source.readInt();
        collecter_name = source.readString();
        buyer_name = source.readString();
        buyer_addr = source.readString();
        finished_time = source.readString();
        buyer_phone = source.readString();
        goodsName = source.readString();
        addition = source.readInt();
        mobile = source.readString();
        rfid_count = source.readInt();
        Parcelable[] parcelables = source.readParcelableArray(PreGoodInfo.class
                .getClassLoader());
        if (parcelables != null) {
            preGoods = Arrays.copyOf(parcelables, parcelables.length,
                    PreGoodInfo[].class);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(sn);
        dest.writeInt(collecter_id);
        dest.writeString(collecter_name);
        dest.writeString(buyer_name);
        dest.writeString(buyer_addr);
        dest.writeString(finished_time);
        dest.writeString(buyer_phone);
        dest.writeString(goodsName);
        dest.writeInt(addition);
        dest.writeString(mobile);
        dest.writeInt(rfid_count);
        dest.writeParcelableArray(preGoods, flags);
    }

    @Override
    public String toString() {
        return "[id=" + id + ",sn=" + sn + ",collecter" + collecter_name
                + ",finished_time=" + finished_time + ",buyer_phone="
                + buyer_phone + ",goods=" + goodsName + "]";
    }
}