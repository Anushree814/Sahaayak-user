package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NoParking_Report extends AppCompatActivity {

    private String vehno;
    private TextView name, contact;
    private ImageView imageView;
    private FirebaseFirestore firestore;
    private StorageReference proimageref, storageReference;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private android.net.Uri Uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_parking__report);
        vehno = getIntent().getStringExtra("vehino");
        name = (TextView) findViewById(R.id.name);
        contact = (TextView) findViewById(R.id.contact_no);
        imageView = (ImageView) findViewById(R.id.i1);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = storage.getReference();
        String dwnldurl = "uploads/" + vehno + ".jpg";
        proimageref = storageReference.child(dwnldurl);
        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(imageView);
        firestore.collection(vehno + "prodata")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("data", "=>" + document.getData() + document.get("name") + document.get("veh") + document.get("email"));
                                name.setText("Name    :    " + document.get("name").toString());
                                contact.setText("Contact :    " + document.get("con").toString());

                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });


    }

    public void message(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.getText()));
        //intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    public void call(View view) {
        String uri = "tel:" + contact.getText().toString().substring(contact.getText().toString().length() - 10, contact.getText().toString().length());
        /*Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;*/

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(uri));//change the number
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);


//        }
//        startActivity(intent);


       }
}
