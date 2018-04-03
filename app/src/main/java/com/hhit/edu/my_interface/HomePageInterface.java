package com.hhit.edu.my_interface;

import com.hhit.edu.bean.JobBean;
import com.hhit.edu.bean.ListResponse;
import com.hhit.edu.utils.ApiManager;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public interface HomePageInterface {
    /**
     * 这里是获取QQ看房网接口的数据内容并且进行填充
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
                             @Part("file\";filename=\"image.png\"")RequestBody imgs);


    @POST("StudentServlet/getAllStudent")
    Call<String> getAllStudent();

    /**
     *  MyhomeFragment use
     * @return 获取所有简约兼职信息
     */
    @POST("JobServlet/getAllJob")
    Observable<ListResponse<JobBean>> getAllJob();

    /**
     * 每八条显示内容
     * @param pagenum
     * @return
     */
    @POST("JobServlet/getJobByPage")
    Observable<ListResponse<JobBean>> getJobByPage(@Query("pagenum") int pagenum);

    /*@GET(ApiManager.HOME_BANNER)
    Call<BannerBean> getBannerBean(@Query("cityid") String cityid);
*/
}
