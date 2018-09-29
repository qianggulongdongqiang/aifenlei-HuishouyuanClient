package com.arcfun.aifenx.data;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * api/common/getPreOrders.html
 * 
 */
public class HistoryOrderInfo implements Parcelable {
    /** order_id */
    private int id;
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
    /** point */
    private int point;
    /** rfid_count */
    private int rfid_count;
    /** goods */
    private HistoryGoodInfo[] historyGoods;
    private String goodsName;

    public HistoryOrderInfo() {
    }

    public HistoryOrderInfo(String _sn, String _time, String _collerter,
            String _name, String _addr, int _addition, HistoryGoodInfo[] pres,
            String _mobile, int _point, int rfids) {
        this.sn = _sn;
        this.finished_time = _time;
        this.collecter_name = _collerter;
        this.buyer_name = _name;
        this.buyer_addr = _addr;
        this.addition = _addition;
        this.historyGoods = pres;
        this.goodsName = getCategory(pres);
        this.mobile = _mobile;
        this.point = _point;
        this.rfid_count = rfids;
    }

    private String getCategory(HistoryGoodInfo[] pres) {
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

    public int getAddition() {
        return addition;
    }

    public void setAddition(int addition) {
        this.addition = addition;
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

    public HistoryGoodInfo[] getHistoryGood() {
        return historyGoods;
    }

    public void setHistoryGood(HistoryGoodInfo[] preGood) {
        this.historyGoods = preGood;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRfidCount() {
        return rfid_count;
    }

    public void setRfidCount(int rfid_count) {
        this.rfid_count = rfid_count;
    }

    public static final Parcelable.Creator<HistoryOrderInfo> CREATOR = new Creator<HistoryOrderInfo>() {
        @Override
        public HistoryOrderInfo createFromParcel(Parcel source) {
            return new HistoryOrderInfo(source);
        }

        @Override
        public HistoryOrderInfo[] newArray(int size) {
            return new HistoryOrderInfo[size];
        }
    };

    protected HistoryOrderInfo(Parcel source) {
        sn = source.readString();
        collecter_name = source.readString();
        buyer_name = source.readString();
        buyer_addr = source.readString();
        addition = source.readInt();
        finished_time = source.readString();
        goodsName = source.readString();
        mobile = source.readString();
        point = source.readInt();
        rfid_count = source.readInt();
        Parcelable[] parcelables = source.readParcelableArray(PreGoodInfo.class
                .getClassLoader());
        if (parcelables != null) {
            historyGoods = Arrays.copyOf(parcelables, parcelables.length,
                    HistoryGoodInfo[].class);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeString(collecter_name);
        dest.writeString(buyer_name);
        dest.writeString(buyer_addr);
        dest.writeInt(addition);
        dest.writeString(finished_time);
        dest.writeString(goodsName);
        dest.writeString(mobile);
        dest.writeInt(point);
        dest.writeInt(rfid_count);
        dest.writeParcelableArray(historyGoods, flags);
    }

    @Override
    public String toString() {
        return "[id=" + id + ",sn=" + sn + ",finished_time=" + finished_time
                + ",point=" + point + ",rfid_count=" + rfid_count + "]";
    }
}