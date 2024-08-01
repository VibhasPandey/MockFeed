package com.prasoon.mockfeed;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public static String[] userNames, userInfos, postTitles, postSmallInfos;//Static so that PostActivity can also access them
    Context context;
    public static TypedArray profilePics, postPics;

    Adapter(TypedArray profPics, String[] usrNames, String[] usrInfos, TypedArray pstPics, String[] pstTitles, String[] pstSmallInfos, Context context) {
        //Get the data from the main activity
        profilePics = profPics;
        userNames = usrNames;
        userInfos = usrInfos;
        postPics = pstPics;
        postTitles = pstTitles;
        postSmallInfos = pstSmallInfos;

        this.context = context;
    }

    /*
    When a view is initially created then a viewholder has to be created for it,
    which will be recycled and binded to new data when necessary
     **/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false), this);
    }

    /*
    When the lay manager requires a view, it asks the recyclerview (getViewForPosition())
    The RV checks the cache and if no view is found then the recycled pool is queried (getViewHolderByType())
    If it's found there then the RV calls the Adapter's onBindViewHolder() and passes the
    holder(which is to be recycled right now) and also the position on the list for which all of this
    work is being done for.
    This method then 'binds' the data according to the position, to the given ViewHolder
    * */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.profilePic.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), profilePics.getResourceId(position, 0), context.getTheme()));
        holder.userName.setText(userNames[position]);
        holder.userInfo.setText(userInfos[position]);
        holder.postPic.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), postPics.getResourceId(position, 0), context.getTheme()));
        holder.postTitle.setText(postTitles[position]);
        holder.postSmallInfo.setText(postSmallInfos[position]);
    }

    /*
    Returns the number of items in the list
    (NOTE: returning 0 causes nothing to be displayed)
    * */
    @Override
    public int getItemCount() {
        return userNames.length;
    }

    /*
    * A view holder is an object which represents an item on the recycler view list. Point to be noted:
    * It doesn't represent any SPECIFIC item but can represent different item whenever required
    * Eg. A certain item gets scrolled away from the screen and a newer one appears. The holder whose
    * item went off-screen will now start representing the new item i.e. the instance variables will
    * become the references for the fields in the newer item layout
    * */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profilePic, postPic;
        TextView userName, userInfo, postTitle, postSmallInfo;
        Adapter adapter;

        public ViewHolder(@NonNull View itemView, Adapter adapter) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            postPic = itemView.findViewById(R.id.post_pic);
            userName = itemView.findViewById(R.id.user_name);
            userInfo = itemView.findViewById(R.id.user_info);
            postTitle = itemView.findViewById(R.id.post_title);
            postSmallInfo = itemView.findViewById(R.id.post_small_info);

            this.adapter = adapter;

            itemView.setOnClickListener(this);
        }

        /*
        The post activity will be displaying the post in detail and thus, needs to know the specific
        data to be used. Instead of passing the position specific data, only the position can also be
        passed and the other activity will just use the static variables of this class for the data
         **/

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra("position", getAdapterPosition());

            //Start the post activity with a shared element transition. The shared element is the post picture here
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, postPic, "sharedTransition");
            context.startActivity(intent, options.toBundle());
        }
    }
}
