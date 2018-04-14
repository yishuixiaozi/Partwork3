package com.hhit.edu.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by 93681 on 2018/2/24.
 */

public class GuideViewPagerAdapter extends PagerAdapter{
    private List<View> mViews;
    private Context mContext;
    private static final String[] sTitles={"PageOne","PageTwo","PageThree","PageFour"};

    public GuideViewPagerAdapter(Context context, List<View> view) {
        this.mViews = view;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mViews.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    //看看这些方法是否用得到
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }

    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    public int getItemPosition(Object object) {
        return mViews.indexOf(object);
    }

    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(mViews.get(position));
    }

    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mViews.get(position));
        return mViews.get(position);
    }

}
