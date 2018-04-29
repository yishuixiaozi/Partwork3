package com.hhit.edu.bean;

/**
 * Created by 93681 on 2018/4/1.
 * 这个是首页显示的职业简介列表信息实体
 */

public class JobBean {
    private int id;
    private int userid;
    private String title;
    private String paymoney;
    private String payway;
    private String worktime;
    private String jobimageuri;
    //新增内容
    private String beigintime;
    private int viewtimes;
    private int peoplenun;
    private String bftime;
    private String workplace;
    private String workdescribe;
    //4/26新增内容处理
    private String jobtype;
    private String gender;

    private String querytype;
    private String queryfield;

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQuerytype() {
        return querytype;
    }

    public void setQuerytype(String querytype) {
        this.querytype = querytype;
    }

    public String getQueryfield() {
        return queryfield;
    }

    public void setQueryfield(String queryfield) {
        this.queryfield = queryfield;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getBeigintime() {
        return beigintime;
    }

    public void setBeigintime(String beigintime) {
        this.beigintime = beigintime;
    }

    public int getViewtimes() {
        return viewtimes;
    }

    public void setViewtimes(int viewtimes) {
        this.viewtimes = viewtimes;
    }

    public int getPeoplenun() {
        return peoplenun;
    }

    public void setPeoplenun(int peoplenun) {
        this.peoplenun = peoplenun;
    }

    public String getBftime() {
        return bftime;
    }

    public void setBftime(String bftime) {
        this.bftime = bftime;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getWorkdescribe() {
        return workdescribe;
    }

    public void setWorkdescribe(String workdescribe) {
        this.workdescribe = workdescribe;
    }

    public String getJobimageuri() {
        return jobimageuri;
    }

    public void setJobimageuri(String jobimageuri) {
        this.jobimageuri = jobimageuri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }
}
