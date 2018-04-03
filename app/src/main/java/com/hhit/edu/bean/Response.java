package com.hhit.edu.bean;

import java.io.Serializable;

/**
 * Created by 93681 on 2018/4/1.
 * 继承Serializable
 */
public class Response implements Serializable {
    private String code;
    private String msg;
    private Long time=System.currentTimeMillis();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
