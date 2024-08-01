package com.prasoon.mockfeed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //These variables are used by the shared preferences to remember which user is currently logged in
    public static final String LOGIN_STATUS = "login status";
    public static final String LOGGED_OUT = "log out status";
    public static final String LOGGED_IN = "log in status";

    public static final String USERNAME = "username present in shared preferences";
    public static final String PASSWORD = "password present in shared preferences";


    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUserState();

        Toolbar toolbar = findViewById(R.id.toolbar);

        //Set toolbar as the action bar
        setSupportActionBar(toolbar);

        //Remove the default text because we'll be using a custom textview instead
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Implement the navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_start, R.string.nav_stop);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Getting the data from the app resources
        String[] userNames, userInfos, postTitles, postSmallInfos;
        TypedArray profilePics, postPics; //TypedArray are good when we need to handle imageview
        profilePics = getResources().obtainTypedArray(R.array.profile_pics);
        userNames = getResources().getStringArray(R.array.user_names);
        userInfos = getResources().getStringArray(R.array.user_infos);
        postPics = getResources().obtainTypedArray(R.array.post_pics);
        postTitles = getResources().getStringArray(R.array.post_titles);
        postSmallInfos = getResources().getStringArray(R.array.post_small_infos);

        //Set up the recycler view list
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(profilePics, userNames, userInfos, postPics, postTitles, postSmallInfos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Implement the logout feature
        ((NavigationView) findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_option_6:
                    //Before logging out, update the user status in shared preferences
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putString(MainActivity.LOGIN_STATUS, MainActivity.LOGGED_OUT)
                            .apply();

                    //Log out
                    startActivity(new Intent(this, LoginActivity.class));
                    return true;

                default:
                    return true;
            }

        });
    }

    //Necessary for the nav drawer button in the action bar to work. Don't know why
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //First verifies that the required keys have been created by the app and then checks if the user
    //is already logged in or not
    void checkUserState() {
        //Check the required key creation
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getAll().size() == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGIN_STATUS, LOGGED_OUT);
            editor.putString(USERNAME, "John Doe");
            editor.putString(PASSWORD, "Jane Doe");
            editor.apply();

        }

        //Check the login status of the user
        String loginStatus = sharedPreferences.getString(LOGIN_STATUS, null);
        if (loginStatus != null && loginStatus.equals(LOGGED_OUT))
            startActivity(new Intent(this, LoginActivity.class));


    }

    @Override
    public void onBackPressed() {
        //Removed to avoid the user back navigating to the login/signup page
    }
}