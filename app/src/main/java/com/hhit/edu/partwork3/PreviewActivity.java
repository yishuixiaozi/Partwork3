package com.hhit.edu.partwork3;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hhit.edu.utils.ImageCompressUtils;
import com.hhit.edu.utils.SDPathUtils;
import com.hhit.edu.view.ClipImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 该活动是图片预览
 */
public class PreviewActivity extends Activity {
	private ClipImageView imageView;
	private Button btnY, btnN;
	private Bitmap bitmap = null;
	private Button btnLeft;
	private Button btnRight;
	private int degrees = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.sp_crop_image);//显示画面的内容
		imageView = (ClipImageView) findViewById(R.id.src_pic);
		btnLeft = (Button) findViewById(R.id.btn_left);
		btnRight = (Button) findViewById(R.id.btn_right);

		initBitmap();//初始化图片就是一开始给圆圈里边的东西设置内容
		//图片左转
		/*btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				degrees -= 90;
				imageView.setImageBitmap(setBitmapRotate(bitmap, degrees));
			}
		});*/
		//图片右转
		/*btnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				degrees += 90;
				imageView.setImageBitmap(setBitmapRotate(bitmap, degrees));
			}
		});*/
        //这里是取消的代码
		btnN = (Button) findViewById(R.id.btn_crop_n);
		btnN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreviewActivity.this.finish();
			}
		});
		//确认按钮
		btnY = (Button) findViewById(R.id.btn_crop_y);
		btnY.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 此处获取剪裁后的bitmap
				degrees = 0;
				Bitmap bitmap = imageView.clip();

				// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
				byte[] bitmapByte = baos.toByteArray();

				Intent intent = new Intent();
				intent.putExtra("bitmap", bitmapByte);
				//调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
				setResult(3, intent);
				
				/*try {
					// 释放图片资源
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}*/
	
				// 结束当前这个Activity对象的生命,回到logo界面
				finish();
			}
		});
	}
	/**
	 * 初始化图片资源
	 * */
	@SuppressWarnings("deprecation")
	private void initBitmap() {
		System.out.println("图片初始化的操作");
		try {
			ImageCompressUtils compress = new ImageCompressUtils();//压缩对象
			ImageCompressUtils.CompressOptions options = new ImageCompressUtils.CompressOptions();
			options.uri = getIntent().getData();
			options.maxWidth = getWindowManager().getDefaultDisplay().getWidth();
			options.maxHeight = getWindowManager().getDefaultDisplay().getHeight();
			bitmap = compress.compressFromUri(this, options);//压缩

			float f = getBitmapDegree(SDPathUtils.getCachePath()+ "temp.jpg");//读取文件的旋转角度

			bitmap = rotateBitmapByDegree(bitmap, f);
			imageView.setImageBitmap(bitmap);//设置最后的图形内容

		} catch (Exception e) {
			// TODO: handle exception
			// showToast("图片太大剪切失败，请重试");
			finish();
		}
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("旋转角度", "" + degree);
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */

	public static Bitmap rotateBitmapByDegree(Bitmap bm, float degree) {
		Bitmap returnBm = null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 图片旋转
	 * */
	private Bitmap setBitmapRotate(Bitmap bitmap, float degrees) {

		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(degrees);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, false);
	}
}
