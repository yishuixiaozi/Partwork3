package com.hhit.edu.my_interface;

import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobneedBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.UserBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 93681 on 2018/5/25.
 */

public interface JobneedPageinterface {

    //求职用户需求发布内容
    @POST("JobneedServlet/postJobneed")
    Observable<String> postJobneed(@Body JobneedBean jobneedBean);

    @POST("JobneedServlet/updateJobneed")
    Observable<String> updateJobneedpost(@Body JobneedBean jobneedBean);
    //求职者用户查询自己所有的求职发布
    @POST("JobneedServlet/querymypost")
    Observable<ListResponse<JobneedBean>> querymypost(@Query("userid") String userid);

    //求职用户删除自己的求职信息
    @POST("JobneedServlet/deletemypost")
    Observable<String> deletemypost(@Query("jobneedid") int jobneedid);

    @POST("UserServlet/getAlluser")
    Observable<ListResponse<UserBean>> getAlluser();

    //求职者用户查询自己某一条兼职信息的详细内容
    @POST("JobneedServlet/getJobneeddetail")
    Observable<EntityResponse<JobneedBean>> getJobneeddetail(@Query("jobneedid") int jobneedid);
}
