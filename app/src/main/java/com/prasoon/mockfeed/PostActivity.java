package com.prasoon.mockfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);//Get the position of the post which was tapped pn by the user

        //Set toolbar as the action bar
        setSupportActionBar(findViewById(R.id.toolbar));

        // Remove the default text because we'll be using a textview instead
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Get the post's specific information to be displayed to the user
        ImageView profilePic = findViewById(R.id.profile_pic), postPic = findViewById(R.id.post_pic);
        TextView userName = findViewById(R.id.user_name), userInfo = findViewById(R.id.user_info),
                postTitle = findViewById(R.id.post_title), postDesc = findViewById(R.id.post_description);
        String[] postDescriptions = getResources().getStringArray(R.array.post_descriptions);

        //Set the views of current activity
        profilePic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), Adapter.profilePics.getResourceId(position, 0), getTheme()));
        userName.setText(Adapter.userNames[position]);
        userInfo.setText(Adapter.userInfos[position]);
        postPic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), Adapter.postPics.getResourceId(position, 0), getTheme()));
        postTitle.setText(Adapter.postTitles[position]);
        postDesc.setText(postDescriptions[position]);

        /*
        Exclude the toolbar and the status bar from the enter transition of the post activity
        This prevents them from 'blinking'/'flickering' when another activity is launched
        * */
        getWindow().getEnterTransition().excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().getEnterTransition().excludeTarget(R.id.customToolbar, true);

    }
}