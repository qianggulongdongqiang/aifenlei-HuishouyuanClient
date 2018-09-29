package com.arcfun.aifenx.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PreGoodInfo implements Parcelable {
    /** id */
    private String id;
    /** num */
    private float num;
    /** name */
    private String name;

    public PreGoodInfo(String _id, float _num, String _name) {
        this.id = _id;
        this.num = _num;
        this.name = _name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Parcelable.Creator<PreGoodInfo> CREATOR = new Creator<PreGoodInfo>() {
        @Override
        public PreGoodInfo createFromParcel(Parcel source) {
            return new PreGoodInfo(source);
        }

        @Override
        public PreGoodInfo[] newArray(int size) {
            return new PreGoodInfo[size];
        }
    };

    protected PreGoodInfo(Parcel source) {
        id = source.readString();
        num = source.readInt();
        name = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(num);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "[id=" + id + ",num=" + num + ",name=" + name + "]";
    }
}