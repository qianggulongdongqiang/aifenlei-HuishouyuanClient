package com.arcfun.aifenx.data;

public class OwnerInfo {
    /** id */
    private int id;
    /** user_nickname */
    private String name;
    private int sex;
    private String birthday;
    private int score;
    /** token */
    private String token;
    /** isMaster */
    private boolean isMaster;

    public OwnerInfo(int _id, String _name, String _token, boolean _master) {
        id = _id;
        name = _name;
        token = _token;
        isMaster = _master;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

}