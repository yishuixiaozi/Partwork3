package com.hhit.edu.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hhit.edu.partwork3.R;

/**
 * Created by 93681 on 2018/5/19.
 */

public class ToorbarView extends FrameLayout {
    public ToorbarView(Context context) {
        super(context);
        init();
    }

    public ToorbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.toorbar_search, this, true);
    }
}
