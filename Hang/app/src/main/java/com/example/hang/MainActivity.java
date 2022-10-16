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
        * Room操作数据库示例
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

        // 发送http请求

//        String nickname = "test";
//        String password = "1234";
//        System.out.println(HttpUtil.get(Ports.signInUrl + nickname + "/" + password + "/", new HashMap<>()));
        ArrayList<String> signIn = new ArrayList<>();
        signIn.add("test");
        signIn.add("123");
        JSONObject jsonObject = null;
        try {
            jsonObject = HttpUtil.httpGet(Ports.signInUrl, signIn);
            System.out.println(jsonObject.getString("nickname"));
            System.out.println(jsonObject.getString("institute"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}