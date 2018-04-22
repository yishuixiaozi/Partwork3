package com.hhit.edu.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
/*import android.support.v7.app.;*/
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.partwork3.LoginActivity;
import com.hhit.edu.partwork3.LoginnewActivity;
import com.hhit.edu.partwork3.MainActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.partwork3.changepasswordActivity;
import com.hhit.edu.partwork3.findpasswordActivity;
import com.hhit.edu.partwork3.registerActivity;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.MyBaseUIlistener;
import com.hhit.edu.utils.RetrofitUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 93681 on 2018/4/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{
    //
    private TextView loginTitle;
    private EditText loginId;
    private EditText loginPassword;
    private Button loginBtn;
    private Button loginMissps;
    private Button loginNewUser;
    private Button loginChangePw;
    //这个用于测试内容
    private ImageView im_loginqq;
    private ImageView im_loginweixin;
    //免登录使用
    private Button nologin;

    private String openidString;
    private UserInfo info;
    private Tencent mTencent;
    private UserBean userBean=new UserBean();
   /* private BaseUiListener baseUiListene=new BaseUiListener();*/

    private MyBaseUIlistener myBaseUIlistener;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int sectionnumber=getArguments().getInt(ARG_SECTION_NUMBER);
        initview(view,sectionnumber);
           /* int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) view.findViewById(R.id.section_label);
            System.out.println("----"+getString(R.string.page,sectionNumber));
            textView.setText(getString(R.string.page, sectionNumber));*/
    }

    public void initview(View view,int sectionnumber){
        loginId = (EditText) view.findViewById(R.id.loginId);
        loginPassword = (EditText) view.findViewById(R.id.loginPassword);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        loginMissps = (Button) view.findViewById(R.id.loginMissps);
        loginMissps.setOnClickListener(this);
        loginNewUser = (Button) view.findViewById(R.id.loginNewUser);
        loginNewUser.setOnClickListener(this);
        loginChangePw = (Button) view.findViewById(R.id.loginChangePw);
        loginChangePw.setOnClickListener(this);
        //微信QQ的地三方登录
        im_loginqq= (ImageView) view.findViewById(R.id.im_loginqq);
        im_loginqq.setOnClickListener(this);
        im_loginweixin= (ImageView) view.findViewById(R.id.im_loginweixin);
        im_loginweixin.setOnClickListener(this);
        //免登录控件后期需要删掉
        nologin= (Button) view.findViewById(R.id.nologin);
        nologin.setOnClickListener(this);
        loginTitle= (TextView) view.findViewById(R.id.loginTitle);
        if (sectionnumber==2){//这里边可以把图片也换掉，这样两个就有点不一样了
            loginTitle.setText(getString(R.string.employee));
            System.out.println("------loginTitle---"+loginTitle.getText().toString());
        }else{
            loginTitle.setText(getString(R.string.employer));
            System.out.println("------loginTitle---"+loginTitle.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                login(loginTitle.getText().toString());
                break;
            case R.id.loginMissps:
                Intent a = new Intent(getActivity(), findpasswordActivity.class);
                startActivity(a);
                break;
            case R.id.loginNewUser:
                System.out.println("注册");
                Intent i = new Intent(getActivity(), registerActivity.class);
                startActivity(i);
                break;
            case R.id.loginChangePw:
                System.out.println("更改密码");
                Intent l = new Intent(getActivity(),
                        changepasswordActivity.class);
                startActivity(l);
                break;
            case R.id.im_loginqq:
                System.out.println("---------------------------------第三方qq登录");
                System.out.println("-----------点击图标=="+loginTitle.getText().toString());
                mTencent=Tencent.createInstance("1106841364",getActivity().getApplicationContext());
                myBaseUIlistener=new MyBaseUIlistener(getActivity(),loginTitle.getText().toString());
                mTencent.login(getActivity(),"all",myBaseUIlistener);
                break;
            case R.id.im_loginweixin:
                System.out.println("-----------------微信登录");
                break;
        }
    }


    /**
     * 数据库用户输入登录
     */
    public void login(final String usertype){
        System.out.println("登录操作--------");
        final String username=loginId.getText().toString();
        String password=loginPassword.getText().toString();
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.UserLogin(username,password,usertype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("----onSubscribe-----");
                    }
                    @Override
                    public void onNext(@NonNull EntityResponse<UserBean> userBeanEntityResponse) {
                        System.out.println("返回信息值处理"+userBeanEntityResponse.getMsg());
                        if ("success".equals(userBeanEntityResponse.getMsg())){
                            SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("nickname",username);
                            editor.putString("usertype",usertype);
                            editor.commit();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                            getActivity().finish();//关闭当前活动
                        }
                        else {
                            Toast.makeText(getActivity(),"用户名或密码错误！请重新登陆",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }
    //自定义一个内部类用来处理接口回调信息
/*    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            System.out.println("登录成功了-------------------------");
            try {
                System.out.println("-------0.tostring--------"+o.toString());
                openidString=((JSONObject)o).getString("openid");//openid是腾讯返回的固定内容
                //给mTencent对象设置内容
                userBean.setUserid(((JSONObject)o).getString("openid"));
                mTencent.setOpenId(openidString);
                mTencent.setAccessToken(((JSONObject) o).getString("access_token"),((JSONObject) o).getString("expires_in"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            //进一步获取详细信息
            QQToken qqToken=mTencent.getQQToken();
            info=new UserInfo(getActivity().getApplicationContext(),qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    System.out.println("用户信息已获取");
                    try{
                        System.out.println("-----------------------"+((JSONObject) o).getString("nickname")+((JSONObject) o).getString("gender"));
                        SharedPreferences preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("nickname",((JSONObject) o).getString("nickname"));
                        editor.putString("usertype",loginTitle.getText().toString());
                        editor.commit();
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        initsave(o);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(UiError uiError) {
                    System.out.println("---------------用户信息获取失败");
                }
                @Override
                public void onCancel() {
                    System.out.println("----------------用户信息获取取消");
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("---------用户登录失败-------");
        }
        @Override
        public void onCancel() {
            System.out.println("---------用户登录取消--------");
        }
    }
    */

    /**
     * 三方登录保存信息使用
     * @param userBean
     */
    public void saveInfo(UserBean userBean){
        System.out.println("------------saveInfo- usertype--"+userBean.getNickname()+userBean.getUsertype());
        //System.out.println("-----logint----11212121"+loginTitle.getText().toString());
        final HomePageInterface request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        request.UUserByUserid(userBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("----onSubscribe");
                    }
                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("success--------------------------");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("---存储失败");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * 获取接口传递过来的
     * @param object
     */
    public void getUserInfo(Object object,String openid,String usertype){
        System.out.println("值拿出来了"+object.toString());
        JSONObject jsonObject= (JSONObject) object;
        System.out.println("------usertype-----======"+usertype);
        try {
            System.out.println("----------------"+jsonObject.getString("nickname"));
            userBean.setUserid(openid);
            userBean.setNickname(jsonObject.getString("nickname"));
            userBean.setYear(jsonObject.getString("year"));
            userBean.setCity(jsonObject.getString("city"));
            userBean.setGender(jsonObject.getString("gender"));
            userBean.setPhotouri(jsonObject.getString("figureurl_qq_2"));
            userBean.setUsertype(usertype);
        }catch (JSONException e){
            e.printStackTrace();
        }
        saveInfo(userBean);
    }

}
