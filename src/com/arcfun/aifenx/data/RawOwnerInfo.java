package com.arcfun.aifenx.data;

public class RawOwnerInfo {
    private int code;
    private String msg;
    private OwnerInfo info;
    public RawOwnerInfo(int _code, String _msg, OwnerInfo _info) {
        code = _code;
        msg = _msg;
        info = _info;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public OwnerInfo getInfo() {
        return info;
    }
    public void setInfo(OwnerInfo info) {
        this.info = info;
    }
}