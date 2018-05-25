package com.hhit.edu.partwork3;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hhit.edu.bean.EntityResponse;
import com.hhit.edu.bean.UserBean;
import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.uikit.JellyInterpolator;
import com.hhit.edu.utils.ApiManager;
import com.hhit.edu.utils.RetrofitUtils;
import com.j256.ormlite.stmt.query.In;
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
import io.reactivex.schedulers.Schedulers;

public class LoginforestActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mBtnLogin;
    private TextView txt_register;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private RadioGroup loginradiogroup;
    RadioButton checkRaidio;
    EditText edt_loginid;
    EditText edt_loginpassword;
    ImageView im_qqlogin;
    ImageView im_wxlogin;
    private static final int DELAY=1500;
    private static final int Employe_login=1000;
    private static final int Employer_login=1001;
    private String usertype;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HomePageInterface request;
    int id;
    //QQ三方登录使用的内容
    private Tencent mTencent;
    private String openidString;
    private UserInfo info;
    private UserBean userBean=new UserBean();
    private BaseUiListener baseUiListener=new BaseUiListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginforest);
        sharedPreferences=this.getSharedPreferences("mydata",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        request= RetrofitUtils.newInstence(ApiManager.COMPUTER_BASE_URL).create(HomePageInterface.class);
        mTencent=Tencent.createInstance("1106841364",this.getApplicationContext());
        initView();
    }
    public void initView(){
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        edt_loginid= (EditText) findViewById(R.id.edt_loginid);
        edt_loginpassword= (EditText) findViewById(R.id.edt_loginpassword);
        im_qqlogin= (ImageView) findViewById(R.id.im_qqlogin);
        im_wxlogin= (ImageView) findViewById(R.id.im_wxlogin);
        txt_register= (TextView) findViewById(R.id.txt_register);
        txt_register.setOnClickListener(this);
        im_qqlogin.setOnClickListener(this);
        im_wxlogin.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        loginradiogroup= (RadioGroup) findViewById(R.id.radiogroup);
        id=loginradiogroup.getCheckedRadioButtonId();
        checkRaidio= (RadioButton) loginradiogroup.findViewById(loginradiogroup.getCheckedRadioButtonId());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_qqlogin://QQ三方登录
                switch (loginradiogroup.getCheckedRadioButtonId()){
                    case R.id.radio1:
                        usertype="Employee";
                        mTencent.login(LoginforestActivity.this,"all",baseUiListener);
                        System.out.println("学生用户登录-----说明整个QQ登录流程没有问题了="+mTencent.getOpenId());
                        break;
                    case R.id.radio2:
                        usertype="Employer";
                        mTencent.login(LoginforestActivity.this,"all",baseUiListener);
                        System.out.println("招聘单位用户-----说明整个QQ登录流程没有问题了="+mTencent.getOpenId());
                        break;
                    default:
                        Toast.makeText(LoginforestActivity.this,"请选择用户类型后在使用QQ三方登录",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.im_wxlogin://微信三方登录
                System.out.println("微信三方登录");
                break;
            case R.id.txt_register://用户注册（适用于自我的注册信息）
                /*startActivity(new Intent(this,registerActivity.class));*/
                switch (loginradiogroup.getCheckedRadioButtonId()){
                    case R.id.radio1:
                        usertype="Employee";
                        Intent intent=new Intent(this,registerActivity.class);
                        intent.putExtra("usertype",usertype);
                        startActivity(intent);
                        break;
                    case R.id.radio2:
                        usertype="Employer";
                        Intent intent1=new Intent(this,registerActivity.class);
                        intent1.putExtra("usertype",usertype);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(LoginforestActivity.this,"请选择用户类型后注册",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.main_btn_login://用户登录
                switch (loginradiogroup.getCheckedRadioButtonId()){
                    case R.id.radio1:
                        System.out.println(edt_loginid.getText().toString());
                        if (edt_loginid.getText().toString().equals("")||edt_loginpassword.getText().toString().equals("")){
                            Toast.makeText(LoginforestActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mWidth = mBtnLogin.getMeasuredWidth();
                            mHeight = mBtnLogin.getMeasuredHeight();
                            mName.setVisibility(View.INVISIBLE);
                            mPsw.setVisibility(View.INVISIBLE);
                            inputAnimator(mInputLayout,mWidth,mHeight);
                            handler.sendEmptyMessageDelayed(Employe_login,DELAY);
                        }
                        break;
                    case R.id.radio2:
                        if (edt_loginid.getText().toString().equals("")||edt_loginpassword.getText().toString().equals("")){
                            Toast.makeText(LoginforestActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mWidth = mBtnLogin.getMeasuredWidth();
                            mHeight = mBtnLogin.getMeasuredHeight();
                            mName.setVisibility(View.INVISIBLE);
                            mPsw.setVisibility(View.INVISIBLE);
                            inputAnimator(mInputLayout,mWidth,mHeight);
                            handler.sendEmptyMessageDelayed(Employer_login,DELAY);
                        }
                        break;
                    default:
                        Toast.makeText(LoginforestActivity.this,"请选择用户类型",Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    }

    //接受handler的处理信息
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case Employe_login:
                    login("Employee");
                    break;
                case Employer_login:
                    login("Employer");
                    break;
                default:
                    Toast.makeText(LoginforestActivity.this,"请登录用户类型！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 自我数据库登录
     * @param usertype
     */
    public void login(final String usertype){
        //登录信息，然后跳转MainActivity
        final String username=edt_loginid.getText().toString();
        final String password=edt_loginpassword.getText().toString();
        request.UserLogin(username,password,usertype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EntityResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

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
                                startActivity(new Intent(LoginforestActivity.this,MainActivity.class));
                            }else {
                                startActivity(new Intent(LoginforestActivity.this, Main2Activity.class));
                            }
                            LoginforestActivity.this.finish();//关闭当前活动
                        }
                        else {
                            Toast.makeText(LoginforestActivity.this,"用户名或密码错误！请重新登陆",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("---自我数据库登录出错");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * QQ用户获取的信息存入数据库内容
     * @param userBean
     */
    public void saveInfo(final UserBean userBean){
       /* System.out.println("userBean.getUserid()"+userBean.getUserid());*/
        System.out.println("userBean.getNickname()"+userBean.getNickname());
        request.UUserByUserid(userBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.println("----------三方QQ登录信息保存成功");
                       /* SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();*/
                        editor.putString("nickname",userBean.getNickname());
                        editor.putString("userid",userBean.getUserid());
                        editor.putString("usertype",usertype);
                        editor.commit();
                        if (usertype.equals("Employee")){
                            Intent intent=new Intent(LoginforestActivity.this, MainActivity.class);//这里需要加一个东西进行判断的
                            startActivity(intent);
                        }
                        else if (usertype.equals("Employer")){
                            Intent intent=new Intent(LoginforestActivity.this,Main2Activity.class);
                            startActivity(intent);
                        }else {
                            System.out.println("用户判断不成立，请检查问题");
                        }
                        finish();//结束登录界面
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("用户信息保存失败,请检查服务器的代码编写");

                    }
                    @Override
                    public void onComplete() {

                    }
                });


    }
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
            }
        });
    }
    //输入框动画
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

    //内部类处理QQ登录
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            System.out.println("登录成功了-------------------------");
            //登录成功获取用户基本信息方法
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
            info=new UserInfo(getApplicationContext(),qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    System.out.println("用户信息已获取");
                    try{
                        System.out.println("-----------------------"+((JSONObject) o).getString("nickname")+((JSONObject) o).getString("gender"));
                        //Toast.makeText(getApplicationContext(), ((JSONObject) o).getString("nickname")+((JSONObject) o).getString("gender"), Toast.LENGTH_SHORT).show();
                        //Log.v("UserInfo",o.toString());
                        //userBean.setUserid(((JSONObject) o).getString("nickname"));
                        userBean.setNickname(((JSONObject) o).getString("nickname"));
                        userBean.setYear(((JSONObject) o).getString("year"));
                        userBean.setCity(((JSONObject) o).getString("city"));
                        userBean.setGender(((JSONObject) o).getString("gender"));
                        userBean.setPhotouri(((JSONObject) o).getString("figureurl_qq_2"));
                        userBean.setUsertype(usertype);
                        saveInfo(userBean);//这里对QQ三方登录的用户进行注册处理
                      /*  SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("nickname",((JSONObject) o).getString("nickname"));
                        editor.commit();

                        if (usertype.equals("Employee")){
                            Intent intent=new Intent(LoginforestActivity.this, MainActivity.class);//这里需要加一个东西进行判断的
                            startActivity(intent);
                        }
                        else if (usertype.equals("Employer")){
                            Intent intent=new Intent(LoginforestActivity.this,Main2Activity.class);
                            startActivity(intent);
                        }else {
                            System.out.println("用户判断不成立，请检查问题");
                        }
                        finish();//结束登录界面*/
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(UiError uiError) {
                    System.out.println("获取QQ用户信息错误");
                }
                @Override
                public void onCancel() {
                    System.out.println("获取QQ用户信息取消");
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("QQ登录错误");
        }
        @Override
        public void onCancel() {
            System.out.println("QQ取消登录");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
    }
}
