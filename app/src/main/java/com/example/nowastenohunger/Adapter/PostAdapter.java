package com.example.nowastenohunger.Adapter;


/*
    PostAdapter Class was used to Make a RecyclerViewAdapter for making User's Post.
 */

import android.content.Context;
update/modified2
import android.content.Intent;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowastenohunger.Class.Post;
import com.example.nowastenohunger.Class.UserPost;

import com.example.nowastenohunger.Class.Userinfo;


import com.example.nowastenohunger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<Post>mPost;
    public List<String>imageList;

    String currentUserID;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private  DatabaseReference databaseReference;

    public PostAdapter(Context mContext, List<Post> mPost, List<String> imageList) {
        this.mContext = mContext;
        this.mPost = mPost;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post, parent ,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(position);

        Post post = mPost.get(position);

        String imageURL = imageList.get(position);

        if(post.getPost().equals(""))
        {
            holder.description.setVisibility(View.GONE);
        }
        else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getPost());
            holder.time.setText(post.getTime());
            holder.fullname.setText(post.getfullname());

           // holder.contact.setText(post.getContact());
            final String name = post.getfullname();


            holder.donatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //// The work of Buttons in post can be done here.


            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileReference = storageReference.child("Users/" + imageURL + "/ProfileImage.jpg");
            profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }


                    Toast.makeText(mContext,name,Toast.LENGTH_SHORT).show();

                }
            });

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileReference = storageReference.child("Users/" + imageURL + "/ProfileImage.jpg");
            profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(holder.profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }


        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


        public TextView fullname,description,time,contact;
        public ImageView profileImage;
        public Button donatebtn;

        public TextView fullname,description,time;
        public ImageView profileImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullname =  itemView.findViewById(R.id.post_user);
            contact = itemView.findViewById(R.id.Contact);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.post_time);
            profileImage = itemView.findViewById(R.id.post_profile_image);

            donatebtn = itemView.findViewById(R.id.donatebtn);
        }

    }


}
