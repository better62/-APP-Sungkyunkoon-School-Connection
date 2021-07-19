//user.java

package models;

public class Modify {
    private String profile;
    private String name;
    private String major;
    private String spec1;
    private String spec2;
    private String spec3;
    private String phone;
    private String account;
    private String time;
    private String rate;

    public Modify() {}

    public Modify(String profile, String name, String major, String spec1, String spec2, String spec3, String phone, String account, String time, String rate) { //생성자
        this.profile = profile;
        this.name = name;
        this.major = major;
        this.spec1 = spec1;
        this.spec2 = spec2;
        this.spec3 = spec3;
        this.phone = phone;
        this.account = account;
        this.time = time;
        this.rate = rate;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) {this.phone = phone;}

    public String getAccount() { return account; }

    public void setAccount(String account) {this.account = account;}

    public String getTime() { return time; }

    public void setTime(String time) {this.time = time;}

    public String getRate() { return rate; }

    public void setRate(String rate) {this.rate = rate;}
}