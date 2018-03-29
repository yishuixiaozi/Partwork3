package com.hhit.edu.partwork3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hhit.edu.my_interface.HomePageInterface;
import com.hhit.edu.utils.SDPathUtils;
import com.hhit.edu.view.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 93681 on 2018/3/15.
 */

public class EditPhotoActivity extends AppCompatActivity {
    private CircleImageView ivHeadLogo;//类对象
    private String localImg;//本地图像字符
    ImageLoader imageLoader= ImageLoader.getInstance();
    /* TApplication imageLoader= TApplication.getInstance();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        getSupportActionBar().hide();//隐藏标题栏
        imageLoader.init(ImageLoaderConfiguration.createDefault(EditPhotoActivity.this));
        //获取组布局对象
        ivHeadLogo= (CircleImageView) findViewById(R.id.iv_head);
        ivHeadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件showSheetDialog();
                showSheetDialog();//点击头像出现选择文本框
            }
        });
    }

    /*这个就是获得内容的*/
    private EditPhotoActivity getActivity(){
        return this;
    }

    /*这个注解的意思是不检测过期的方法，
    这样就过滤掉了很多不必要的检错内容*/
    @SuppressWarnings("deprecation")
    private void showSheetDialog(){
        System.out.println("显示diaog");
        View view=getLayoutInflater().inflate(R.layout.sp_photo_choose_dialog,null);
        final Dialog dialog=new Dialog(this,R.style.transparentFrameWindowStyle);
        dialog.setContentView(view,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        //这里只是形成一个dialog在界面底部供自己选择
        Window window=dialog.getWindow();
        window.setWindowAnimations(R.style.main_tab_bottom);
        WindowManager.LayoutParams wl = window.getAttributes();

        //设置宽高样式
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //点击的是拍照按钮
        Button btnCamera = (Button) view.findViewById(R.id.btn_to_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//销毁dialog，然后进入拍照的内容
                //权限检查,如果权限不足，返回内容提示
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 222);
                    return;
                } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    return;
                } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 222);
                    return;
                }else{
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    /*sendBroadcast(openCameraIntent);*/
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//目前是执行的这个
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDPathUtils.getCachePath(), "temp.jpg")));
                        startActivityForResult(openCameraIntent,2);
                    } else {
                        Uri imageUri = FileProvider.getUriForFile(EditPhotoActivity.this, "com.camera_photos.fileprovider", new File(SDPathUtils.getCachePath(), "temp.jpg"));
                        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, 2);
                    }
                }
            }
        });

        //点击的是从相册选择
        Button btnPhoto = (Button) view.findViewById(R.id.btn_to_photo);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(Intent.ACTION_PICK,null);//创建行动
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);//开启行动，并且处理结果内容
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btn_to_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    /**
     * 意向结果的处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {//这个是选取照片的
            System.out.println("这里是选取照片的方法返回");
            startPhotoZoom(data.getData());//data.getData();是一个uri
        } else if (requestCode == 2) {//这个是拍照的
            System.out.println("这里是拍照的返回");
            File temp = new File(SDPathUtils.getCachePath(), "temp.jpg");
            //temp =/storage/emulated/0/ZFCrash/cache/temp.jpg
            startPhotoZoom(Uri.fromFile(temp));
        } else if (requestCode == 3) {//这个是截图之后过来的
            System.out.println("截图过来看看的");
            if (data != null) {
                System.out.println("121212121221212-------");
                setPicToView(data);//设置图形图像的显示
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(getActivity(), PreviewActivity.class);
        intent.setDataAndType(uri, "image/*");
       /* System.out.println("uri="+uri);*/
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     * 这里尝试进行图片上传的操作
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bitmap bitmap = null;
        byte[] bis = picdata.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        localImg = System.currentTimeMillis() + ".JPEG";
        System.out.println("localImg="+localImg);
        if (bitmap != null) {
            SDPathUtils.saveBitmap(bitmap, localImg);
            Log.e("本地图片绑定", SDPathUtils.getCachePath() + localImg);
            setImageUrl(ivHeadLogo, "file:/" + SDPathUtils.getCachePath() + localImg, R.mipmap.head_logo);
            //file://storage/emulated/0/ZFCrash/cache/1521727873993.JPEG
        }

        String uri=SDPathUtils.getCachePath() + localImg;
        //这个地方进行各种方法的测试
       // connecttest();
        //upload(uri);
    }

    /**
     * 测试图片上传
     * @param uri
     */
    public void upload(String uri){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080/AndroidService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final HomePageInterface request=retrofit.create(HomePageInterface.class);
        //封装description
        String descr="this is a test";

        //封装图形题
        File file=new File(uri);

        //从file获取requestbody实体内容
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        //执行接口方法，开始数据传输
        Call<String> call =request.uploadimage(descr,requestBody);
        System.out.println("------------------------------------1");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Upload=");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("something");
            }
        });
    }


    /*
    * 测试连通要首先关闭防火墙和手机连接wifi
    * */

    public void connecttest(){
        System.out.println("------------------------------------");
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080/AndroidService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建接口实例
        final HomePageInterface request=retrofit.create(HomePageInterface.class);
        //传递了参数
        Call<String> call=request.getAllStudent();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("failure");
            }
        });
    }
    /*private DisplayImageOptions options;*/
    private DisplayImageOptions options;
    public void setImageUrl(ImageView ivId, String imageUrl, int emptyImgId) {
        if (options == null) {//每次 创建对象
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(emptyImgId)//图片下载期间显示图片
                    .showImageForEmptyUri(emptyImgId)//Uri错误显示的图片
                    .showImageOnFail(emptyImgId).cacheInMemory(true)//图片加载失败显示图片
                    .cacheOnDisk(true).considerExifParams(true)//缓存图片，旋转方砖
                    .bitmapConfig(Bitmap.Config.RGB_565).build();//图片解码类型
        }
        imageLoader.displayImage(imageUrl, ivId, options);//这里一定要初始化
    }







}
