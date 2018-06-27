package com.krishbarcode.firebase_realtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Challan_Details extends AppCompatActivity {
    FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    String vehstring;
    private DatabaseReference mFirebaseParkingDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Challan> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan_detail);
        vehstring = getIntent().getStringExtra("vehno");
        mFirebaseParkingDatabase = FirebaseDatabase.getInstance().getReference("Vehicle No");
        mFirebaseParkingDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data = new ArrayList<Challan>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Challan challan = snapshot.getValue(Challan.class);

                    Log.d("card",challan.getDate()+"   "+challan.getTime());
                    data.add(new Challan(challan.getTime(), challan.getDate(), challan.getLoc(), challan.getVehno(), challan.isIs_paid()));




                    //                    User user = snapshot.getValue(User.class);
//                    //ed2.setText(user.getFull_name());
//                    //stringBuilder.append(user.getFull_name());
//                    //ed2.setText(stringBuilder.toString());
//                    al.add(user);
                }
                adapter = new challan_adapter(data);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
