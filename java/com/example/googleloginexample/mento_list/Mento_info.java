package com.example.googleloginexample.mento_list;

import java.io.Serializable;

public class Mento_info implements Serializable {
    private String profile;
    private String img;
    private String id;
    private String info;
    private String document;


    public Mento_info(){}

    public Mento_info(String profile,String img ,String id, String info,String document) {
        this.profile = profile;
        this.img=img;
        this.id = id;
        this.info = info;
        this.document = document;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
