package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 93681 on 2018/5/17.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    /**
     * 需要创造构造器不然类就出错了
     * @param fm
     * @param list
     */
    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }

    /**
     * 返回的是第几个Fragment
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    /**
     * 返回fragment的数量
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }
}
