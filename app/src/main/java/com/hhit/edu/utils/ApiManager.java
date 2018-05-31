package com.hhit.edu.utils;

import android.net.Uri;

/**
 * Created by 93681 on 2018.
 *
 */
public class ApiManager {
    //宿舍电脑电脑IP
    public static final String COMPUTER_BASE_URL="http://100.100.108.249:8080/AndroidServ/";
    //手机WIFE域名    "http://192.168.43.31:8080/AndroidSer/"
    //宿舍电脑域名    "http://192.168.137.1:8080/AndroidSer/"
    //图书馆IP         http://100.100.108.107:8080/AndroidServ
    //看房网站主机地址，域名
    public static final String BASE_URL="http://ikft.house.qq.com/";
    public static final String HOME_PAGE="index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&devid=866500021200250&appname=QQHouse&mod=appkft&reqnum=20&act=newslist&channel=71";
    /**
     * 获取首页Listview_item的资讯详情
     */
    public static final String DISCOVER_URL="http://m.db.house.qq.com/index.php?mod=appkft&act=discover&cityid=4&rf=kanfang";
}
