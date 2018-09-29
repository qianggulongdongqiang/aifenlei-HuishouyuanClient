package com.arcfun.aifenx.data;

public class AreaDetailInfo {
    private int detail_id;
    private String detail_name;
    public AreaDetailInfo(int _id, String _name) {
        detail_id = _id;
        detail_name = _name;
    }
    public int getId() {
        return detail_id;
    }
    public void setId(int detail_id) {
        this.detail_id = detail_id;
    }
    public String getName() {
        return detail_name;
    }
    public void setName(String detail_name) {
        this.detail_name = detail_name;
    }
}