package com.arcfun.aifenx.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcfun.aifenx.utils.Utils;

public class PhoneInfo implements Parcelable {
    private String number;
    private String name;

    public PhoneInfo() {
    }

    public PhoneInfo(String _number, String _name) {
        this.number = _number;
        this.name = _name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Parcelable.Creator<PhoneInfo> CREATOR = new Creator<PhoneInfo>() {
        @Override
        public PhoneInfo createFromParcel(Parcel source) {
            return new PhoneInfo(source);
        }

        @Override
        public PhoneInfo[] newArray(int size) {
            return new PhoneInfo[size];
        }
    };

    protected PhoneInfo(Parcel source) {
        number = source.readString();
        name = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        if (Utils.isValidNumber(number)) {
            return number;
        }
        return "";
    }
}