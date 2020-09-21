package com.example.nowastenohunger.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowastenohunger.Activity.BottomNavigationMenuActivity;
import com.example.nowastenohunger.Activity.OptionsActivity;
import com.example.nowastenohunger.Activity.SmsActivity;
import com.example.nowastenohunger.Adapter.PostAdapter;
import com.example.nowastenohunger.Class.Post;
import com.example.nowastenohunger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchDonationsFragment extends Fragment {
    private TextView searchDonationsFragmentTextView;
    private ImageView options;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUserID;
    private List<Post>postList;
    private List<String> imageURLList;
    private List<String>Users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_donations, container, false);

        recyclerView = view.findViewById(R.id.myrecycleview);
        options = view.findViewById(R.id.options);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);

        postList = new ArrayList<>();
        imageURLList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postList, imageURLList);
        recyclerView.setAdapter(postAdapter);
        readPosts();  /* This method was used to check which user has 'post' key in Database. */

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        return view;
    }


    private void readPosts()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren())
                {
                    String number = postsnapshot.child("number").getValue().toString();
                    String id = postsnapshot.getKey();
                    Post post= postsnapshot.getValue(Post.class);
                    post.setContact(number);
                    System.out.println(post.getfullname());
                    System.out.println(post.getTime());
                    post.setUID(id); //Getting UID from firebase.
                        if(post.getPost()!=null) {
                            postList.add(post);
                            imageURLList.add(postsnapshot.getKey());
                        }
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
