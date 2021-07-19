package com.example.googleloginexample.chatting;

import java.io.Serializable;

public class Chat_info implements Serializable {
    private String profile;
    private String img;
    private String id;
    private String info;


    public Chat_info(){}

    public Chat_info(String profile, String img, String id, String info) {
        this.profile = profile; //내가 채팅하기 누른 멘토 UID
        this.img = img; //멘토 프로필 이미지
        this.id = id; //멘토 닉네임
        this.info = info; //멘토 소개글
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
