package com.hhit.edu.partwork3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by 93681 on 2018/3/30.
 */

public class SearchActivity extends BaseActivity implements TextWatcher, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

    }
}
