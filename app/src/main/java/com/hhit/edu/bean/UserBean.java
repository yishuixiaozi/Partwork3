package com.hhit.edu.bean;

/**
 * Created by 93681 on 2018/4/10.
 */

public class UserBean {
    private int id;
    private String username;
    private String password;
    private String photouri;
    private int concernum;
    private int fansnum;
    private String company;
    private String comnature;
    //new here 2018.4.15
    private String userid;
    private String nickname;
    private String year;
    private String city;
    private String gender;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUserid() {
        return userid;
    }


    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotouri() {
        return photouri;
    }

    public void setPhotouri(String photouri) {
        this.photouri = photouri;
    }

    public int getConcernum() {
        return concernum;
    }

    public void setConcernum(int concernum) {
        this.concernum = concernum;
    }

    public int getFansnum() {
        return fansnum;
    }

    public void setFansnum(int fansnum) {
        this.fansnum = fansnum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getComnature() {
        return comnature;
    }

    public void setComnature(String comnature) {
        this.comnature = comnature;
    }
}
