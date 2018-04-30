package com.hhit.edu.my_interface;

import com.hhit.edu.bean.CollectionBean;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.utils.ApiManager;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public interface HomePageInterface {
    /**
     * 这里是获取QQ看房网接口的数据内容并且进行填充
     *
     * @param pageflag
     * @param buttonmore
     * @param cityid
     * @return
     */
    @GET(ApiManager.HOME_PAGE)
    Call<String> getListContent(@Query("pageflag") String pageflag,
                                @Query("buttonmore") String buttonmore,
                                @Query("cityid") String cityid);

    @Multipart
    @POST("UploadServlet/uploadimage")
    Call<String> uploadimage(@Part("fileName") String description,
                             @Part("file\";filename=\"image.png\"") RequestBody imgs);

    @POST("StudentServlet/getAllStudent")
    Call<String> getAllStudent();

    /**
     * MyhomeFragment use
     *
     * @return 获取所有简约兼职信息
     */
    @POST("JobServlet/getAllJob")
    Observable<ListResponse<JobBean>> getAllJob();

    /**
     * 每八条显示内容
     *
     * @param pagenum
     * @return
     */
    @POST("JobServlet/getJobByPage")
    Observable<ListResponse<JobBean>> getJobByPage(@Query("pagenum") int pagenum);
    /**
     * 这个地方是组合查询和关键字查询的地方所需要的内容
     * @param job
     * @return
     */
    @POST("JobServlet/getJobByType")
    Observable<ListResponse<JobBean>> getJobByType(@Body JobBean job,
                                                   @Query("pagenum") int pagenum);
    /**
     * 依据Id查找兼职详细信息的
     *
     * @param id
     * @return
     */
    @POST("JobServlet/getJobById")
    Observable<EntityResponse<JobBean>> getJobById(@Query("id") int id);

    //查询用户单独信息使用
    @POST("UserServlet/getUserById")
    Observable<EntityResponse<UserBean>> getUserById(@Query("userid") int userid);

    //QQ三方登录使用
    @POST("UserServlet/UUserByUserid")
    Observable<String> UUserByUserid(@Body UserBean user);

    //自己用户：求职者用户注册
    @POST("UserServlet/UserRegister")
    Observable<String> UserRegister(@Body UserBean user);
    //这个是求职者用户登录使用
    @POST("UserServlet/getUser")
    Observable<EntityResponse<UserBean>> getUserLoginInfo(@Query("username") String username,
                                                          @Query("password") String password);

    //这里是招聘者用户登录使用
    @POST("UserServlet/getUser2")
    Observable<EntityResponse<UserBean>> getUserLoginInfo2(@Query("username") String username,
                                                           @Query("password") String password);

    //两个公用一个登录接口，到服务器再去判断
    @POST("UserServlet/getUser")
    Observable<EntityResponse<UserBean>> UserLogin(@Query("username") String username,
                                                   @Query("password") String password,
                                                   @Query("usertype") String usertype);
    @POST("CollectionServlet/addCollection")
    Observable<String> addCollection(@Body CollectionBean collection);

    @POST("CollectionServlet/getAllCollection")
    Observable<ListResponse<CollectionBean>> getAllCollection(@Query("userid") String userid);
}
