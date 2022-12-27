package com.example.hang.ui.learn;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.hang.ui.learn.util.ListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LearnCardPagerAdapater extends FragmentPagerAdapter {
    public  int PAGER_COUNT = 0;
    private final ArrayList<ListBean> allQues;
    private final Context mContext;
    private boolean readOnly;

    public LearnCardPagerAdapater(FragmentManager fm, Context context, ArrayList<ListBean> allQues, boolean readonly) {
        super(fm);
        PAGER_COUNT = allQues.size();
        this.allQues = allQues;
        this.mContext = context;
        this.readOnly = readonly;
        System.out.println("adapter size " + PAGER_COUNT);
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    //实例化指定位置的页面，并将其添加到容器中
    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        ListBean jo = allQues.get(position);
        int type = 1;
        type = jo.getType();
        //System.out.println(jo.getId());
        if (type == 1) {
            fragment = new FillBlankFragment();
        } else if (type == 2) {
            fragment = new SingleChoiceFragment(mContext, allQues.get(position), readOnly);
        } else if (type == 3) {
            fragment = new MultiChoiceFragment();
        } else {
            fragment = new ImageFragment();
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String type = "题目";
        switch (allQues.get(position).getType()) {
            case 1:
                type = "简答题";
                break;
            case 2:
                type = "单选题";
                break;
            case 3:
                type = "多选题";
                break;
            default:
                type = "图片题";
                break;
        }
        return position + " " + type;
    }
}
