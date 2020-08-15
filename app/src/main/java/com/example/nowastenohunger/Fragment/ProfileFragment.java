package com.example.nowastenohunger.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nowastenohunger.Activity.EditProfileActivity;
import com.example.nowastenohunger.Activity.OptionsActivity;
import com.example.nowastenohunger.Class.UpdatedName;
import com.example.nowastenohunger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment  implements View.OnClickListener {

    //private ProgressBar progressBar;
    private TextView profileFragmentTextView;
    private ImageView options;
    private ImageView editButton;
    private TextView profileName, profileEmail, profileAddress, profileAccountType, profileCuisineType, profileNumber;
    String currentUserEmail, currentUserID;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        currentUserEmail = user.getEmail();
        currentUserID = user.getUid();

        UpdatedName updatedName = new UpdatedName(currentUserID);

        //progressBar = view.findViewById(R.id.fragmentsProgressBar);
        profileEmail = (TextView) view.findViewById(R.id.showEmail);
        profileName = (TextView) view.findViewById(R.id.showName);
        profileNumber = (TextView) view.findViewById(R.id.showNumber);
        profileAddress = (TextView) view.findViewById(R.id.showAddress);
        profileAccountType = (TextView) view.findViewById(R.id.showType);
        profileCuisineType = (TextView) view.findViewById(R.id.showCuisine);

        profileEmail.setText(currentUserEmail);

        editButton = view.findViewById(R.id.editProfile);

        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String username = String.valueOf(dataSnapshot.child("fullname").getValue());
                    String userNumber = String.valueOf(dataSnapshot.child("number").getValue());
                    String userAddress = String.valueOf(dataSnapshot.child("address").getValue());
                    String userAccountType = String.valueOf(dataSnapshot.child("accountType").getValue());
                    String userPrefCuisine = String.valueOf(dataSnapshot.child("cuisineType").getValue());

                    profileName.setText(username);
                    profileNumber.setText(userNumber);
                    profileAddress.setText(userAddress);
                    profileAccountType.setText(userAccountType);
                    profileCuisineType.setText(userPrefCuisine);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileFragmentTextView = view.findViewById(R.id.profileFragmentTextView);
        options = view.findViewById(R.id.options);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        editButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), EditProfileActivity.class));
    }
}