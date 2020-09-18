package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nowastenohunger.Class.UpdatedName;
import com.example.nowastenohunger.Class.Userinfo;
import com.example.nowastenohunger.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    //private String title=null,post=null;
    private EditText  fullName, number, address;
    private Spinner userType, cuisineType;
    private Button save;
    private FirebaseAuth mAuth;
    String currentUserID;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar editProfileToolBar = findViewById(R.id.editProfileToolBar);
        setSupportActionBar(editProfileToolBar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editProfileToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner UserTypeSpinner = findViewById(R.id.UserTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UserTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UserTypeSpinner.setAdapter(adapter);
        UserTypeSpinner.setOnItemSelectedListener(this);

        Spinner CuisineTypeSpinner = findViewById(R.id.CuisineTypeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.CuisineTypes, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CuisineTypeSpinner.setAdapter(adapter1);
        CuisineTypeSpinner.setOnItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentUserID = user.getUid();
        UpdatedName updatedName = new UpdatedName(currentUserID);

        save = (Button) findViewById(R.id.saveButton);
        fullName = (EditText) findViewById(R.id.showName);
        number = (EditText) findViewById(R.id.showNumber);
        address = (EditText) findViewById(R.id.showAddress);
        userType = (Spinner) findViewById(R.id.UserTypeSpinner);
        cuisineType = (Spinner) findViewById(R.id.CuisineTypeSpinner);

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        save.setOnClickListener(this);

        databaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String username = String.valueOf(dataSnapshot.child("fullname").getValue());
                    String userNumber = String.valueOf(dataSnapshot.child("number").getValue());
                    String userAddress = String.valueOf(dataSnapshot.child("address").getValue());

                    fullName.setText(username);
                    number.setText(userNumber);
                    address.setText(userAddress);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        final String name = fullName.getText().toString().trim();
        final String contactNumber = number.getText().toString().trim();
        final String area = address.getText().toString().trim();
        final String accounttype = userType.getSelectedItem().toString().trim();
        final String prefCuisineType = cuisineType.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contactNumber) || TextUtils.isEmpty(area) || TextUtils.isEmpty(accounttype) || TextUtils.isEmpty(prefCuisineType)){
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        else{
            Userinfo info = new Userinfo(name, contactNumber, area, accounttype, prefCuisineType);

            /////////////////// Modified Code /////////////////////////

            Map<String, Object> updates = new HashMap<String,Object>();

            databaseReference = databaseReference.child(currentUserID);
            updates.put("accountType",info.getAccountType());
            updates.put("address",info.getAddress());
            updates.put("cuisineType",info.getCuisineType());
            updates.put("fullname",info.getFullname());
            updates.put("number",info.getNumber());

            databaseReference.updateChildren(updates);

            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
            finish();

            /////////////////// Modified Code /////////////////////////
            //databaseReference.child(currentUserID).setValue(info);
            //Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();

        }
    }
}