package com.hhit.edu.partwork3;

import android.content.Context;
import android.view.View;

import com.hhit.me.model.SearchInfo;

/**
 * Created by 93681 on 2018/3/29.
 * 这个类没有用，被ResultexpressActivity代替
 */
public class ResultActivity extends BaseActivity implements View.OnClickListener{

    public static void start(Context context, SearchInfo searchInfo) {
       /* Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(Extras.SEARCH_INFO, searchInfo);
        context.startActivity(intent);*/
    }
    @Override
    public void onClick(View v) {

    }
}
