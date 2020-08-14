package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowastenohunger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView signInTextView1, signInTextView2;
    Button signInButton, signUpIntroButton;
    EditText signInEmailEditText, signInPasswordEditText;
    ProgressBar signInProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signInTextView1 = findViewById(R.id.signInTextView1);
        signInTextView2 = findViewById(R.id.signInTextView2);
        signInButton = findViewById(R.id.signInButton);
        signUpIntroButton = findViewById(R.id.signUpIntroButton);
        signInEmailEditText = findViewById(R.id.signInEmailEditText);
        signInPasswordEditText = findViewById(R.id.signInPasswordEditText);
        signInProgressBar = findViewById(R.id.signInProgressBar);

        signInTextView2.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        signUpIntroButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.signInTextView2)
        {
            Intent i = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
            signInPasswordEditText.setText("");
            signInEmailEditText.setText("");
        }
        else if(v.getId() == R.id.signInButton)
        {
            userLogin();
        }
        else if(v.getId() == R.id.signUpIntroButton)
        {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            signInPasswordEditText.setText("");
            signInEmailEditText.setText("");
        }
    }

    private void userLogin()
    {
        String email = signInEmailEditText.getText().toString().trim();
        String password = signInPasswordEditText.getText().toString().trim();

        if(email.isEmpty())
        {
            signInEmailEditText.setError("An Email is nedded!");
            signInEmailEditText.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailEditText.setError("Email is invalid!");
            signInEmailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            signInPasswordEditText.setError("Enter a password!");
            signInPasswordEditText.requestFocus();
            return;
        }

        //signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    if(mAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent i = new Intent(MainActivity.this, BottomNavigationMenuActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        //signInPasswordEditText.setText("");
                        //signInEmailEditText.setText("");

                        signInProgressBar.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please, verify your email!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Username & password don't match!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}