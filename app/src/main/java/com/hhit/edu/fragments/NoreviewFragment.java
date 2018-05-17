package com.hhit.edu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhit.edu.partwork3.R;

/**
 * Created by 93681 on 2018/5/17.
 */

public class NoreviewFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noreview, container,false);
        //inflater.inflate(R.layout.fragment_through, container,false);
    }
}
