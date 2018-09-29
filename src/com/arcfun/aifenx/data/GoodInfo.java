package com.arcfun.aifenx.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcfun.aifenx.utils.Utils;

/**
 * api/common/getSecGoods.html
 * 
 */
public class GoodInfo implements Parcelable {
    /** id */
    private int id;
    /** name */
    private String name;
    /** unit_name */
    private String unit_name;
    /** unit */
    private String unit;
    /** purchasing_price */
    private float price;
    /** purchasing_point */
    private float point;
    private float vendor = 0f;

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GoodInfo(GoodInfo _info) {
        this(_info.getId(), _info.getName(), _info.getUnit_name(),
                _info.getUnit(), _info.getPoint());
    }

    public GoodInfo(int id, String name, String uint_name, String unit,
            float point) {
        this.id = id;
        this.name = name;
        this.unit_name = uint_name;
        this.unit = unit;
        this.point = point;
    }

    public GoodInfo(int id, String name, String uint_name, String unit,
            float point, float vendor) {
        this.id = id;
        this.name = name;
        this.unit_name = uint_name;
        this.unit = unit;
        this.point = point;
        this.vendor = vendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public float getVendor() {
        return Utils.formatFloat(vendor);
    }

    public void setVendor(float vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "[id=" + id + ",name=" + name + ",u_name=" + unit_name +
                ",unit=" + unit + ",point=" + point + ",vendor=" + vendor + "]";
    }

    public static final Parcelable.Creator<GoodInfo> CREATOR = new Creator<GoodInfo>() {
        @Override
        public GoodInfo createFromParcel(Parcel source) {
            return new GoodInfo(source);
        }

        @Override
        public GoodInfo[] newArray(int size) {
            return new GoodInfo[size];
        }
    };

    protected GoodInfo(Parcel source) {
        id = source.readInt();
        name = source.readString();
        unit_name = source.readString();
        unit = source.readString();
        vendor = source.readFloat();
        point = source.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(unit_name);
        dest.writeString(unit);
        dest.writeFloat(vendor);
        dest.writeFloat(point);
    }
}