package com.example.googleloginexample.match;

import java.io.Serializable;

public class Match_info implements Serializable {
    private String profile;
    private String img;
    private String id;
    private String info;


    public Match_info(){}

    public Match_info(String profile,String img, String id, String info) {
        this.profile = profile;
        this.img = img;
        this.id = id;
        this.info = info;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
