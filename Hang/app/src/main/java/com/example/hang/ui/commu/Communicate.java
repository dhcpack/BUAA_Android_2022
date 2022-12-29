package com.example.hang.ui.commu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.commu.friends.FriendsListActivity;
import com.example.hang.ui.commu.posts.PostDetailActivity;
import com.example.hang.ui.commu.posts.PostsListActivity;
import com.example.hang.ui.learn.ShowItemsActivity;
import com.example.hang.ui.mine.myBooks.AddQuestionActivity;
import com.example.hang.ui.mine.myBooks.MyBooksActivity;
import com.example.hang.util.TimeSpliter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Communicate extends Fragment {

    private CommunicateViewModel mViewModel;
    private String username;
    private int type = 0;

    private ListView posts;
    private int postId;

    public static Communicate newInstance() {
        return new Communicate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicate, container, false);


        //获取传过来的数据
        Bundle bundle = getActivity().getIntent().getExtras();
        //System.out.println(bundle.getString("username"));
        username = bundle.getString("username");

        //按钮监听
        AppCompatButton btn_comu_friends = view.findViewById(R.id.btn_comu_friends);
        btn_comu_friends.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), FriendsListActivity.class);
            intent.putExtra("username", username);
            // 聊天系统
            startActivity(intent);
        });
        AppCompatButton btn_comu_books = view.findViewById(R.id.btn_comu_posts);
        btn_comu_books.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), PostsListActivity.class);
            intent.putExtra("username", username);
            // 帖子系统
            startActivity(intent);
        });

        //设置ListView
        //1.给链表添加数据
        username = getActivity().getIntent().getExtras().getString("username");
        List<Map<String, Object>> list = getData();

//        if(list.size() == 0){
//            TextView comment_split = view.findViewById(R.id.comment_split);
//            comment_split.setText("---------------还没有评论哦----------------");
//        }
        //2.适配器，刚刚重写的！
        PostsAdapter postsAdapter = new PostsAdapter(getActivity(), list);
        //3.设置适配器
        ListView post_list = view.findViewById(R.id.posts);
        post_list.setAdapter(postsAdapter);
        post_list.smoothScrollBy(30, 200);
        return view;
    }

    //填充数据
    public List<Map<String, Object>> getData() {
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getPostUrl, new ArrayList<>(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    //map.put("iv_icon_book", jsonObject.getString("pic"));
                    map.put("iv_pic_post", R.drawable.ic_book);
                    map.put("tv_post_title", jsonObject.getString("title"));
                    map.put("tv_post_content", jsonObject.getString("content"));
                    map.put("tv_post_tag", jsonObject.getString("tag"));
                    map.put("tv_post_favor", jsonObject.get("favor"));
                    map.put("post_time", jsonObject.get("time"));
                    map.put("post_id", jsonObject.get("id"));

                    //                    map.put("book_id", jsonObject.getInt("id"));
                    map.put("username", username);
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class PostsAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;

        public PostsAdapter(Context context, List<Map<String, Object>> data) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView iv_pic_post;
            private TextView tv_post_title;
            private TextView tv_post_tag;
            private TextView tv_favor_count;
        }

        //所有要返回的东西的数量（Id、信息等），都在data里面，从data里面取就好
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        //跟activity中的onCreate()差不多，目的就是给item布局中的各个控件对应好，并添加数据
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            PostsAdapter.Info info = new PostsAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.posts_layout, null);
            info.iv_pic_post = convertView.findViewById(R.id.post_image);
            info.tv_post_title = convertView.findViewById(R.id.tv_post_title);
            info.tv_post_tag = convertView.findViewById(R.id.tv_post_tag);
            info.tv_favor_count = convertView.findViewById(R.id.favor_count);

            /*
            *       map.put("iv_pic_post", R.drawable.ic_book);
                    map.put("tv_post_title", jsonObject.getString("title"));
                    map.put("tv_post_content", jsonObject.getString("content"));
                    map.put("tv_post_tag", jsonObject.getString("tag"));
                    map.put("tv_post_favor", jsonObject.get("favor"));
            * */

            //设置数据
            info.iv_pic_post.setImageResource((Integer) data.get(position).get("iv_pic_post"));
            info.tv_post_title.setText((String) data.get(position).get("tv_post_title"));
            info.tv_post_tag.setText(((String) data.get(position).get("tv_post_tag")) + " " + TimeSpliter.getDate((String) data.get(position).get("post_time")) + "发布");
            info.tv_favor_count.setText(String.valueOf(data.get(position).get("tv_post_favor")));
            info.tv_post_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    Integer post_id = (Integer) data.get(position).get("post_id");
                    bundle.putInt("post_id", post_id);
                    bundle.putString("username", (String) data.get(position).get("username"));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.tv_post_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    Integer post_id = (Integer) data.get(position).get("post_id");
                    bundle.putInt("post_id", post_id);
                    bundle.putString("username", (String) data.get(position).get("username"));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.iv_pic_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    Integer post_id = (Integer) data.get(position).get("post_id");
                    bundle.putInt("post_id", post_id);
                    bundle.putString("username", (String) data.get(position).get("username"));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);
        // TODO: Use the ViewModel
    }

}