package com.arcfun.aifenx.data;

import com.arcfun.aifenx.utils.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryGoodInfo implements Parcelable {
    /** id */
    private int id;
    /** name */
    private String name;
    /** num */
    private float num;
    /** unit */
    private String unit;
    /** p */
    private float point;
    /** point */
    private int points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public HistoryGoodInfo(HistoryGoodInfo _info) {
        this(_info.getId(), _info.getName(), String.valueOf(_info.getNum()),
                _info.getUnit(), _info.getPoint(), _info.getPoints());
    }

    public HistoryGoodInfo(int id, String name, String num, String unit,
            float point, int points) {
        this.id = id;
        this.name = name;
        this.num = Utils.formatFloat(Float.valueOf(num));
        this.unit = unit;
        this.point = point;
        this.points = points;
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

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "[id=" + id + ",name=" + name + ",num=" + num + ",unit=" + unit
                + ",point=" + point + ",points=" + points + "]";
    }

    public static final Parcelable.Creator<HistoryGoodInfo> CREATOR = new Creator<HistoryGoodInfo>() {
        @Override
        public HistoryGoodInfo createFromParcel(Parcel source) {
            return new HistoryGoodInfo(source);
        }

        @Override
        public HistoryGoodInfo[] newArray(int size) {
            return new HistoryGoodInfo[size];
        }
    };

    protected HistoryGoodInfo(Parcel source) {
        id = source.readInt();
        name = source.readString();
        num = source.readFloat();
        unit = source.readString();
        point = source.readFloat();
        points = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(num);
        dest.writeString(unit);
        dest.writeFloat(point);
        dest.writeInt(points);
    }
}