package com.example.hang;

import android.os.Bundle;
import android.os.StrictMode;

import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hang.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle bundle = getIntent().getExtras();
        //System.out.println(bundle.getString("username"));
        username = bundle.getString("username");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_learn, R.id.navigation_recommend, R.id.navigation_communicate,
                R.id.navigation_mine).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        /*
        ArrayList<String> bookid = new ArrayList<>();
        bookid.add("2");
        ArrayList<String> notExist = new ArrayList<>();
        notExist.add("999");
        try {
            JSONArray jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getQuestionUrl, bookid, true);
            System.out.println(jsonArray);
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getQuestionUrl, notExist, true);
            System.out.println(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        /*
        ArrayList<String> user = new ArrayList<>();
        user.add("test");
        try {
            JSONArray jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, user, true);
            System.out.println(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);    //延时2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> notexist = new ArrayList<>();
        notexist.add("test1");
        try {
            JSONArray jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, notexist, true);
            System.out.println(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */



        // 下面的代码不一定对
        /*
         * 新建 查询 修改 删除记忆本
        ArrayList<String> user = new ArrayList<>();
        user.add("testport");
        HashMap<String, String> userBook1 = new HashMap<>();
        userBook1.put("nickname", "testport");
        userBook1.put("bookname", "算法设计与分析");
        userBook1.put("public", "False");
        userBook1.put("tag", "算法");
        HashMap<String, String> userBook2 = new HashMap<>();
        userBook2.put("nickname", "testport");
        userBook2.put("bookname", "Android");
        userBook2.put("public", "True");
        JSONObject response = null;
        JSONArray jsonArray = null;
        try {
            response = HttpUtil.httpPost(Ports.addBookUrl, userBook1);
            System.out.println(response);
            Thread.sleep(2000);    //延时2秒

            response = HttpUtil.httpPost(Ports.addBookUrl, userBook2);
            System.out.println(response);
            Thread.sleep(2000);    //延时2秒

            response = HttpUtil.httpPost(Ports.addBookUrl, userBook1);
            System.out.println(response);
            Thread.sleep(2000);    //延时2秒

            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, user, true);
            System.out.println(jsonArray);
            Thread.sleep(2000);    //延时2秒

            userBook2.put("public", "False");
            response = HttpUtil.httpPut(Ports.modifyBookUrl, userBook2);
            System.out.println(response);
            Thread.sleep(2000);    //延时2秒

            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, user, true);
            System.out.println(jsonArray);
            Thread.sleep(2000);    //延时2秒

            user.add("Android");
            response = HttpUtil.httpDelete(Ports.deleteBookUrl, user);
            System.out.println(response);
            Thread.sleep(2000);    //延时2秒

            user.remove("Android");
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, user, true);
            System.out.println(jsonArray);
            Thread.sleep(2000);    //延时2秒
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
       */





        /*
        * 修改用户信息
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", "porttest");
        params.put("password", "123456");
        params.put("target", "形式语言");
        params.put("stuId", "20231164");
        params.put("institute", "未来/高工学院");
        params.put("major", "6");
        params.put("grade", "3");
        JSONObject jsonObject = null;
        try {
            jsonObject = HttpUtil.httpPut(Ports.modifyDetailUrl, params);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, String> notexist = new HashMap<>();
        params.put("nickname", "notexist");
        params.put("password", "123456");
        params.put("target", "形式语言");
        params.put("stuId", "20231164");
        params.put("institute", "沈元学院");
        params.put("major", "6");
        params.put("grade", "3");
        jsonObject = null;
        try {
            jsonObject = HttpUtil.httpPut(Ports.modifyDetailUrl, params);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        * */



        /*
        * Room操作数据库示例 Unused
            UserDatabase db = Room.databaseBuilder(getApplicationContext(),
                    UserDatabase.class, "database-name").allowMainThreadQueries().build();
            UserDao userDao = db.userDao();
            User nuser = new User("hello", "123");
            userDao.insertAll(nuser);
            userDao.insertAll(nuser);
            List<User> users = userDao.getAll();

            System.out.println("begin print");
            for (User user : users) {
                System.out.println(user.id);
            }
            System.out.println("end print");
        */

        /* GET 登录
        ArrayList<String> signIn = new ArrayList<>();
        signIn.add("test");
        signIn.add("123");
        JSONObject jsonObject = null;
        try {
            jsonObject = HttpUtil.httpGet(Ports.signInUrl, signIn);
            System.out.println(jsonObject);
        } catch (IOException) {
            e.printStackTrace();
        }
        */

        /*
        * POST 注册
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", "porttest");
        params.put("password", "123456");
        params.put("target", "形式语言");
        params.put("stuId", "20231164");
        params.put("institute", "高工");
        params.put("major", "6");
        params.put("grade", "3");
        JSONObject jsonObject = null;
        try {
            jsonObject = HttpUtil.httpPost(Ports.signUpUrl, params);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        * 打卡
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", "porttest");
        JSONObject jsonObject = null;
        try {
            jsonObject = HttpUtil.httpPost(Ports.checkUrl, params);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> checkDetail = new ArrayList<>();
        checkDetail.add("porttest");
        try {
            jsonObject = HttpUtil.httpGet(Ports.checkDetail, checkDetail);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


    }
}