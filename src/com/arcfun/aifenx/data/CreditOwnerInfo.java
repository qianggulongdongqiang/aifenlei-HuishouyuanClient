package com.arcfun.aifenx.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * api/common/getSecGoods.html
 * 
 */
public class CreditOwnerInfo implements Parcelable {
    /** id */
    private int id;
    /** mobile */
    private String mobile;
    /** score */
    private int score;
    /** cost */
    private int cost = 0;
    /** way */
    private String way = "";
    /** more */
    private String more = "";

    public CreditOwnerInfo(int _id, String _mobile, int _score, int _cost,
            String _way, String _more) {
        this.id = _id;
        this.mobile = _mobile;
        this.score = _score;
        this.cost = _cost;
        this.way = _way;
        this.more = _more;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    @Override
    public String toString() {
        return "[mobile=" + mobile + ", score=" + score + ", cost=" + cost
                + ", way=" + way + ", more=" + more + "]";
    }

    public static final Parcelable.Creator<CreditOwnerInfo> CREATOR = new Creator<CreditOwnerInfo>() {
        @Override
        public CreditOwnerInfo createFromParcel(Parcel source) {
            return new CreditOwnerInfo(source);
        }

        @Override
        public CreditOwnerInfo[] newArray(int size) {
            return new CreditOwnerInfo[size];
        }
    };

    protected CreditOwnerInfo(Parcel source) {
        id = source.readInt();
        mobile = source.readString();
        score = source.readInt();
        cost = source.readInt();
        way = source.readString();
        more = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mobile);
        dest.writeInt(score);
        dest.writeInt(cost);
        dest.writeString(way);
        dest.writeString(more);
    }
}