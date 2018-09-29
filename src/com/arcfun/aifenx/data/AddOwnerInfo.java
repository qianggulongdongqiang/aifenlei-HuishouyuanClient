package com.arcfun.aifenx.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AddOwnerInfo implements Parcelable {
    private String account;
    private String name;
    private String sex;
    private String mobile;
    private String addr;
    private String token;

    public AddOwnerInfo() {
    }

    public AddOwnerInfo(String _account, String _name, String _sex,
            String _mobile, String _addr, String _token) {
        account = _account;
        name = _name;
        sex = _sex;
        mobile = _mobile;
        addr = _addr;
        token = _token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static final Parcelable.Creator<AddOwnerInfo> CREATOR =
            new Creator<AddOwnerInfo>() {
        @Override
        public AddOwnerInfo createFromParcel(Parcel source) {
            return new AddOwnerInfo(source);
        }

        @Override
        public AddOwnerInfo[] newArray(int size) {
            return new AddOwnerInfo[size];
        }
    };

    protected AddOwnerInfo(Parcel source) {
        account = source.readString();
        name = source.readString();
        sex = source.readString();
        mobile = source.readString();
        addr = source.readString();
        token = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(mobile);
        dest.writeString(addr);
        dest.writeString(token);
    }

}