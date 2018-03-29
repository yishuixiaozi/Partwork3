package com.hhit.edu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hhit.edu.partwork3.R;

/**
 * 首页listview第2项 --8个按钮
 */
public class HomeSecondView extends FrameLayout {



    public HomeSecondView(Context context) {
        super(context);
        init();
    }

    public HomeSecondView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.home_second_listhead,this,true);


    }



}
