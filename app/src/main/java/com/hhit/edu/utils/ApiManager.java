package com.hhit.edu.utils;

import android.net.Uri;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class ApiManager {
    //宿舍电脑电脑IP
    public static final String COMPUTER_BASE_URL="http://192.168.137.1:8080/AndroidServ/";
    //手机WIFE域名"http://192.168.43.31:8080/AndroidService/"
    //电脑域名    "http://192.168.137.1:8080/AndroidService/"
    public static final String PHONE_BASE_URL="http://192.168.43.31:8080/AndroidService/";
    //我的远程主机域名
    public static final String LINUX_BASE_URL="";
    //家中WLAN局域网域名
    public static final String HOME_BASE_URL="http://192.168.0.101:8080/AndroidService/";
    //看房网站主机地址，域名
    public static final String BASE_URL="http://ikft.house.qq.com/";

    //城市选择
    public static final String CITY_CHOICE="index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&act=kftcitylistnew&channel=71&devid=866500021200250&appname=QQHouse&mod=appkft";
    /**
     * 首页 ListView内容
     */
    public static final String HOME_PAGE="index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&devid=866500021200250&appname=QQHouse&mod=appkft&reqnum=20&act=newslist&channel=71";
    public static final String HOME_ITEM_URL="http://m.house.qq.com/a/";
    /**
     * 获取首页Listview_item的资讯详情
     */
    public static final String HOUSE_DETAIL="index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&devid=866500021200250&appname=QQHouse&mod=appkft&act=newsdetail&channel=71";
    /**
     * 发现Fragment加载的URL地址
     */
    public static final String DISCOVER_URL="http://m.db.house.qq.com/index.php?mod=appkft&act=discover&cityid=4&rf=kanfang";
}
