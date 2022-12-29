package com.example.hang.util;

import com.example.hang.R;

public class PostPicGetter {
    public static Object getPostPic(String posttitle) {
        if (posttitle.equals("中外人员往来暂行措施将更好保障对外交流合作")) {
            return R.drawable.post_8;
        } else if (posttitle.equals("高效学习的11个方法分享")) {
            return R.drawable.post_14;
        } else if (posttitle.equals("原版英文书籍推荐")) {
            return R.drawable.post_15;
        } else if (posttitle.equals("前后端项目入门")) {
            return R.drawable.post_16;
        } else if (posttitle.equals("地道表达法（第296期）")) {
            return R.drawable.post_17;
        } else {
            return R.drawable.post_init;
        }
    }
}
