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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        * 修改用户信息
        * */
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
            jsonObject = HttpUtil.httpPut(Ports.signUpUrl, params);
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
            jsonObject = HttpUtil.httpPut(Ports.signUpUrl, params);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }




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