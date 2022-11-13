package com.whitebear.buaa;

import android.content.Context;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PracticingPageAdapter extends FragmentPagerAdapter {
    public  int PAGER_COUNT = 0;
    private String[][] questions;
    Context context;
    String subject;

    public PracticingPageAdapter(FragmentManager fm,String subject,String[][] questions,Context context) {
        super(fm);
        PAGER_COUNT=questions.length;
        this.questions=questions;
        this.subject=subject;
        this.context=context;
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
        Fragment fragment;
        String[] test=questions[position];
        int[] a={position,PAGER_COUNT};
        if(questions[position][9].equals("1")){
            fragment=new Single_Choice(context,subject,questions[position],a);
        }else{
            fragment=new Multiple_Choice(context,subject,questions[position],a);
        }
        return fragment;
    }
}
