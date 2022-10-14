package com.example.hang;

import android.os.Bundle;

import com.example.hang.database.UserDatabase;
import com.example.hang.database.dao.UserDao;
import com.example.hang.database.entities.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.hang.databinding.ActivityMainBinding;

import java.util.List;


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
    }


}