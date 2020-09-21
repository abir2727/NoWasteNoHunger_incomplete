package com.example.nowastenohunger.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nowastenohunger.Activity.EditProfileActivity;
import com.example.nowastenohunger.Activity.MapsActivity;
import com.example.nowastenohunger.Class.Post;
import com.example.nowastenohunger.Class.UpdatedName;
import com.example.nowastenohunger.Class.UserPost;
import com.example.nowastenohunger.Activity.OptionsActivity;
import com.example.nowastenohunger.Class.Userinfo;
import com.example.nowastenohunger.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeDonationsFragment extends Fragment
{
    private TextView makeDonationsFragmentTextView;
    private ImageView options;
    private EditText amount,item,location;
    private Button button;
    private ImageView confirmLocation;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    String currentUserID, currentUID;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    String latitude="",longitude="",type;
    private static final String[] types = {"kg", "gm", "pieces"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_make_donations, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentUID = user.getUid();
        currentUserID = UpdatedName.getUsername();
        //System.out.println(currentUserID);

        makeDonationsFragmentTextView = view.findViewById(R.id.makeDonationsFragmentTextView);
        options = view.findViewById(R.id.options);
        amount = (EditText) view.findViewById(R.id.amount);
        item = (EditText) view.findViewById(R.id.item);
        button = (Button) view.findViewById(R.id.button);
        confirmLocation = (ImageView) view.findViewById(R.id.confirmLocation);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(getContext(),R.array.weights,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("weights", (String) parent.getItemAtPosition(position));
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                type = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        location = (EditText) view.findViewById(R.id.location);
        location.setHint("Location");

        //Toast.makeText(getContext(), latitude+ " "+longitude, Toast.LENGTH_SHORT).show();
        //System.out.println(latitude+ " "+longitude);
        //location.setText(latitude+" "+longitude);
        locationGeocode();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });


        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MapsActivity.class));
            }
        });

        return view;
    }

    private void locationGeocode() {

        databaseReference.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("postLocationLat").exists()) {
                    Double latitude = new Double(String.valueOf(dataSnapshot.child("postLocationLat").getValue()));
                    Double longitude = new Double(String.valueOf(dataSnapshot.child("postLocationlong").getValue()));
                    System.out.println(longitude+" hi"+latitude);
                    try {
                        // Build a Mapbox geocoding request
                        MapboxGeocoding client = MapboxGeocoding.builder()
                                .accessToken(getString(R.string.access_token))
                                .query(Point.fromLngLat(longitude, latitude))
                                .geocodingTypes(GeocodingCriteria.TYPE_POI)
                                .mode(GeocodingCriteria.MODE_PLACES)
                                .build();
                        client.enqueueCall(new Callback<GeocodingResponse>() {
                            @Override
                            public void onResponse(Call<GeocodingResponse> call,
                                                   Response<GeocodingResponse> response) {
                                if (response.body() != null && response.body().features().size()>0) {
                                    //List<CarmenFeature> results = response.body().features();
                                    //if (results.size() > 0) {

                                    // Get the first Feature from the successful geocoding response
                                    //CarmenFeature feature = results.get(0);

                                    // Get the address string from the CarmenFeature
                                    //String carmenFeatureAddress = feature.placeName();
                                    //  location.setText(carmenFeatureAddress);
                                    String placeName = response.body().features().get(0).placeName();
                                    location.setText(placeName);
                                    //response.body().features().set(0,null);
                                    //response.body()=null*/
                                    //String address = feature.placeName().replaceFirst(feature.text().concat(", "),"");
                                    //Toast.makeText(MapsActivity.this, placeName,Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(getContext(), "Location not confirmed", Toast.LENGTH_SHORT).show();
                                }
                                //}
                            }

                            @Override
                            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                                //Log.e(TAG,"Geocoding Failure: " + throwable.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        //Logd.e(TAG,"Error geocoding: " + servicesException.toString());
                        //Toast.makeText(getContext(), "Location not confirmed", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    /*
        post method was used to read data from MakeDonationsFragment's TextViews & send these data to
        database.
     */

    public void post()
    {
        final String Amount = amount.getText().toString().trim();
        final String Item = item.getText().toString().trim();
        final String Location = location.getText().toString().trim();

        if(TextUtils.isEmpty(Amount) || TextUtils.isEmpty(Item) || TextUtils.isEmpty(Location))
        {
            Toast.makeText(getContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {

            //databaseReference.updateChildren(updates);
            //Toast.makeText(getContext(), "Post Successful.\nClick on the search icon to see your post.", Toast.LENGTH_SHORT).show();


            databaseReference.child(currentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()==false) {

                        Toast.makeText(getContext(), "Please update your profile before posting",Toast.LENGTH_SHORT).show();
                        return;

                    }


                    databaseReference = databaseReference.child(currentUID);

                    String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

                    String post = "Has "+ Amount + " " + type + " of " + Item + " left .";

                    final Map<String, Object> updates = new HashMap<String,Object>();
                    updates.put("post",post);
                    updates.put("time",currentTime);
                    updates.put("postlocation",Location);

                    databaseReference.updateChildren(updates);
                    Toast.makeText(getContext(), "Post Successful.\nClick on the search icon to see your post.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

}
