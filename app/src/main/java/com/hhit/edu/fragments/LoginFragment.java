package com.hhit.edu.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
/*import android.support.v7.app.;*/
import android.support.annotation.Nullable;
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
import com.hhit.edu.partwork3.Main2Activity;
import com.hhit.edu.partwork3.MainActivity;
import com.hhit.edu.partwork3.R;
import com.hhit.edu.partwork3.changepasswordActivity;
import com.hhit.edu.partwork3.findpasswordActivity;
import com.hhit.edu.partwork3.registerActivity;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.MyBaseUIlistener;
import com.hhit.edu.utils.RetrofitUtils;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
   /* private BaseUiListener baseUiListene=new BaseUiListener();*/
    private MyBaseUIlistener myBaseUIlistener;
    private static final String ARG_SECTION_NUMBER = "section_number";
    HomePageInterface request;

    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request=RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences=getActivity().getSharedPreferences("mydata",MODE_PRIVATE);
        editor=preferences.edit();
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
                i.putExtra("usertype",loginTitle.getText().toString());
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
                        System.out.println("自己数据库登录操作获取返回信息MSg"+userBeanEntityResponse.getMsg());
                        if ("success".equals(userBeanEntityResponse.getMsg())){
                            editor.putString("nickname",username);
                            editor.putString("usertype",usertype);
                            editor.putString("userid",userBeanEntityResponse.getCode());
                            editor.commit();
                            //这里要进行一个判断，求职者到常规页面，招聘者到招聘界面
                            if (usertype.equals("Employee")){
                                startActivity(new Intent(getActivity(),MainActivity.class));
                            }else {
                                startActivity(new Intent(getActivity(), Main2Activity.class));
                            }
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
    /**
     * 三方登录保存信息使用
     * @param userBean
     */
    public void saveInfo(final UserBean userBean){
        startActivity(new Intent(getActivity(),MainActivity.class));
        getActivity().finish();
       /* System.out.println("------------saveInfo- usertype--"+userBean.getNickname()+userBean.getUsertype());
        request.UUserByUserid(userBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("----onSubscribe");
                    }
                    @Override
                    public void onNext(@NonNull String s) {//执行成功后就跳转到主页面// ;
                        System.out.println("success--------------------------");
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("---存储失败");
                    }
                    @Override
                    public void onComplete() {

                    }
                });*/
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
