package com.hhit.edu.my_interface;
import com.hhit.edu.bean.CollectionBean;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.SignupBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
/**
 * Created by 93681 on 2018/5/1.
 */
public interface SignupPageinterface {
    //这个是添加报名信息使用
    @POST("SignupServlet/addSignup")
    Observable<String> addSignup(@Body SignupBean signupBean);
    //查询某个用户是否报名某兼职信息
    @POST("SignupServlet/searchSignup")
    Observable<EntityResponse<SignupBean>> searchSignup(@Query("userid") String userid,
                                                        @Query("jobid") int jobid);
    //查询某个用户的报名信息
    @POST("SignupServlet/getAllSignup")
    Observable<ListResponse<SignupBean>> getAllSignup(@Query("userid") String userid);
    //取消报名的操作
    @POST("SignupServlet/deleteSignup")
    Observable<String> deleteSignup(@Query("signupid") int signupid);
    //查询某个招聘用户的发布的兼职信息
    @POST("JobServlet/queryMypost")
    Observable<ListResponse<JobBean>> queryMypost(@Query("userid") String userid);
    //删除某个用户某条兼职发布信息，通过兼职id
    @POST("JobServlet/deleteByid")
    Observable<String> deleteByid(@Query("jobid") int jobid);
    //获取报名表中某某条兼职的报名信息
    @POST("SignupServlet/getMypostsign")
    Observable<ListResponse<SignupBean>> getMypostsign(@Query("jobid") int jobid);
}
