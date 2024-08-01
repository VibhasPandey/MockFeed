package com.prasoon.mockfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login, gotoSignup;

    //Instance variable to hold a reference to the app's database
    private UserDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get the reference to the app's database
        database = UserDatabase.getInstance(this);

        username = findViewById(R.id.nameLogin);
        password = findViewById(R.id.passwordLogin);

        login = findViewById(R.id.loginBtn);
        gotoSignup = findViewById(R.id.signupBtn);

        //Verify login details with the database and allow login
        login.setOnClickListener(view -> {
            //Get the entered username/password
            String inputName = username.getText().toString(), inputPwd = password.getText().toString();

            //Check to verify all fields have been filled
            if (TextUtils.isEmpty(inputName) || TextUtils.isEmpty(inputPwd)) {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                //Verify with the database
                new Thread(() -> {
                    List<User> allUsers = database.userDao().getAllUsers();//Get list of all users

                    boolean validUser = false;
                    for (User user : allUsers) {
                        if (user.getUsername().equals(inputName) && user.getPassword().equals(inputPwd)) {
                            validUser = true;
                            break;
                        }
                    }

                    //If a match is found then allow the login
                    if (validUser) {
                        //Before logging in, update the user status in shared preferences
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString(MainActivity.LOGIN_STATUS, MainActivity.LOGGED_IN)
                                .putString(MainActivity.USERNAME, inputName)
                                .putString(MainActivity.PASSWORD, inputPwd)
                                .apply();

                        //Log in
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show());
                    }

                }).start();
            }
        });

        //If clicked, the user should be redirected to the account creation page
        gotoSignup.setOnClickListener(view -> startActivity(new Intent(this, SignupActivity.class)));
    }

    @Override
    public void onBackPressed() {
        //Removed to avoid the user back navigating to the home page
    }
}