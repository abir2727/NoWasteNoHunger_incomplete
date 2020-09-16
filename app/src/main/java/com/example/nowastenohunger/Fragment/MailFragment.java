package com.example.nowastenohunger.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nowastenohunger.Activity.OptionsActivity;
import com.example.nowastenohunger.Class.Post;
import com.example.nowastenohunger.Class.UpdatedName;
import com.example.nowastenohunger.Class.Userinfo;
import com.example.nowastenohunger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MailFragment extends Fragment
{
    private TextView mailFragmentTextView,etTo,to;
    private EditText etmessage,etsubject;
    private Button etbutton;
    private ImageView options;
    String currentUserEmail, currentUserID;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mail, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        currentUserEmail = user.getEmail();
        currentUserID = user.getUid();

        mailFragmentTextView = view.findViewById(R.id.notifyFragmentTextView);
        options = view.findViewById(R.id.options);
        etTo = view.findViewById(R.id.etTo);
        to = view.findViewById(R.id.to);
        etmessage = view.findViewById(R.id.etmessage);
        etsubject = view.findViewById(R.id.etsubject);
        etbutton=view.findViewById(R.id.btsend);


        etbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println(recipient);
               sendmail();
            }
        });


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        return view;
    }

    void sendmail()
    {
        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String username = String.valueOf(dataSnapshot.child("fullname").getValue());
                    String contact = String.valueOf(dataSnapshot.child("number").getValue());
                    String recipient = etTo.getText().toString().trim();
                    String subject = etsubject.getText().toString();
                    String message = etmessage.getText().toString();

                    String txt1= "Sent By , <br/>";
                    String txt2 = "<b> App Username : </b>"+username +".<br/>";
                    String txt3 = "<b> Contact No.  : </b>"+contact;

                    Intent send = new Intent(Intent.ACTION_SENDTO);
                    String uriText = "mailto:" + Uri.encode(recipient) +
                            "?subject=" + Uri.encode(subject) +
                            "&body=" + Uri.encode(message+"<br/><br/>"+txt1+txt2+txt3);
                    Uri uri = Uri.parse(uriText);
                    send.setData(uri);
                    startActivity(Intent.createChooser(send, "Send mail Via"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Intent mailIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+recipient));
       // mailIntent.putExtra(Intent.EXTRA_EMAIL,recipient);
        mailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT,message);

        //mailIntent.setType("message/rfc822");
        startActivity(mailIntent);
        //try {
            //startActivity(Intent.createChooser(mailIntent,"Choose an Email"));

       // }catch (Exception e)
        //{
            //Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        //}*/
    }
}
