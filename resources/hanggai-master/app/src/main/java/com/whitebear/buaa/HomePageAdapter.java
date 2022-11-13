package com.whitebear.buaa;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePageAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    public Home_shouye home_shouye = null;
    public Home_xuexi home_xuexi = null;
    public Home_bangzhu home_bangzhu = null;

    public HomePageAdapter(FragmentManager fm, int[] progress, String subject, Context context) {
        super(fm);
        home_shouye=new Home_shouye(progress,subject,context);
        home_xuexi= new Home_xuexi(subject,context);
        home_bangzhu= new Home_bangzhu();

    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = home_shouye;
                break;
            case MainActivity.PAGE_TWO:
                fragment = home_xuexi;
                break;
            case MainActivity.PAGE_THREE:
                fragment = home_bangzhu;
                break;
        }
        return fragment;
    }
}
