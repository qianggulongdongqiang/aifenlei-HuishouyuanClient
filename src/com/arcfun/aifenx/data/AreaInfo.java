package com.arcfun.aifenx.data;

import java.util.List;

public class AreaInfo {
    private int id;
    private String name;
    private List<AreaDetailInfo> infos;
    public AreaInfo(int _id, String _name, List<AreaDetailInfo> _infos) {
        id = _id;
        name = _name;
        infos = _infos;
    }
    public int getId() {
        return id;
    }
    public void setId(int _id) {
        this.id = _id;
    }
    public String getName() {
        return name;
    }
    public void setname(String _name) {
        this.name = _name;
    }
    public List<AreaDetailInfo> getInfo() {
        return infos;
    }
    public void setInfo(List<AreaDetailInfo> _infos) {
        this.infos = _infos;
    }
}