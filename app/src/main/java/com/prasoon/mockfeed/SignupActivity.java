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

public class SignupActivity extends AppCompatActivity {

    private EditText name, pwd, repwd;
    private Button signup, alreadyAccnt;
    private UserDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        name = findViewById(R.id.nameSignup);
        pwd = findViewById(R.id.passwordEditText);
        repwd = findViewById(R.id.retypePasswordEditText);

        signup = findViewById(R.id.signupBtn);
        alreadyAccnt = findViewById(R.id.alreadyHaveAnAccntBtn);

        //Get the database of the app
        database = UserDatabase.getInstance(this);

        //When signup is tapped, register the user after checking for few conditions
        signup.setOnClickListener(view -> {
            String username = name.getText().toString(),
                    password = pwd.getText().toString(),
                    rePassword = repwd.getText().toString();

            //Check to verify all fields have been filled
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)) {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(rePassword)) {//Check to avoid a typo in passwords
                Toast.makeText(this, "Passwords Mismatch", Toast.LENGTH_SHORT).show();
            } else {
                User newUser = new User(username, password);

                //Check if the user is already present. If found then we don't allow the signup
                new Thread(() -> {
                    List<User> allUsers = database.userDao().getAllUsers();

                    //Check for matching usernames
                    boolean alreadyUser = false;
                    for (User user : allUsers) {
                        if (user.getUsername().equals(newUser.getUsername())) {
                            alreadyUser = true;
                            break;
                        }
                    }

                    if (alreadyUser) {
                        runOnUiThread(() -> Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show());

                    } else {//If everything is right then allow user creation and sign in
                        database.userDao().insertUser(newUser);

                        //Before logging in, update the user status in shared preferences
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString(MainActivity.LOGIN_STATUS, MainActivity.LOGGED_IN)
                                .putString(MainActivity.USERNAME, newUser.getUsername())
                                .putString(MainActivity.PASSWORD, newUser.getPassword())
                                .apply();

                        //Log in now
                        startActivity(new Intent(this, MainActivity.class));
                    }
                }).start();
            }
        });

        alreadyAccnt.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }
}