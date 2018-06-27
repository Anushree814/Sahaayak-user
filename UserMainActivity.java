package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView name, navname, veh, navemail;
    ImageView proimage, navimage;
    StorageReference proimageref, storageReference;
    FirebaseStorage storage;
    FirebaseUser firebaseUser;
    View header;
    DatabaseReference mDatabase;
    FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String vehno1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        String vehi = getIntent().getStringExtra("takevehno");
    //  Toast.makeText(this, ""+vehi, Toast.LENGTH_SHORT).show();

        firestore = FirebaseFirestore.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        header = navigationView.getHeaderView(0);
        //header = navigationView.inflateHeaderView(R.layout.nav_header_user_main);
        //header = LayoutInflater.from(this).inflate(R.layout.nav_header_user_main,navigationView,false);
        //navigationView.addHeaderView(header);


        name = (TextView) findViewById(R.id.name);
        veh = (TextView) findViewById(R.id.veh_no);
        proimage = (ImageView) findViewById(R.id.proimage);
        navimage = (ImageView) header.findViewById(R.id.navproimage);
        navname = (TextView) header.findViewById(R.id.navname);
        navemail = (TextView) header.findViewById(R.id.navemail);

        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = storage.getReference();
        vehno1 = fetchvehno();
//        try {
//            synchronized (this)
//            {
//            this.wait(5000);
//        } }catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Log.v("try",vehno1);
        String dwnldurl = "uploads/" + "MH49AN2001" + ".jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(proimage);






Log.v("load","start image loading");

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(navimage);

        Log.v("load","image loaded");



//        Log.v("try","start of fetching data");
        Log.v("load","start fetching data");
        firestore = FirebaseFirestore.getInstance();
        Log.v("load",vehno1+"prodata");
        firestore.collection("MH49AN2001"+"prodata")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Log.v("load","adding data to component");
                                Log.d("data", "=>" + document.getData() + document.get("name") + document.get("veh") + document.get("email"));
                                name.setText(document.get("name").toString());
                                navname.setText(document.get("name").toString());
                                veh.setText(document.get("veh").toString());
                                navemail.setText(document.get("email").toString());
                         }

                        } else {

                            Log.v("load","error");
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.v("load","data not found");
               // Toast.makeText(UserMainActivity.this, "document not found", Toast.LENGTH_SHORT).show();
            }
        });

        firestore.collection(vehno1+"contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {


                                Log.d("data", "=>" + document.getData() + document.get("contact1") + document.get("contact2") + document.get("contact3"));


                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });



    }

    private String fetchvehno() {
        final String[] vehno = {""};
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        Log.v("load",firebaseUser.getUid().toString()+"vehno");
        firestore.collection(firebaseUser.getUid()+"vehno").get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        vehno[0] = document.get("vehno").toString();
                            // Toast.makeText(UserMainActivity.this, ""+ vehno[0], Toast.LENGTH_SHORT).show();
                        Log.d("load", "=>" +document.get("vehno").toString());

                    }

                } else {
                    Log.e("data", "error occured user main" + task.getException());
                }

            }
        });
        return vehno[0];

    }


    public Bitmap getRoundedShape(Bitmap scaleBitmaoimage) {
        int width = 50;
        int height = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) width - 1) / 2, ((float) height - 1) / 2, (Math.min(((float) width), ((float) height)) / 2), Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path);
        }
        Bitmap sourceBitmap = scaleBitmaoimage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, width, height), null);
        return targetBitmap;


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_pro) {
            startActivity(new Intent(UserMainActivity.this, Profile.class));

        } else if (id == R.id.log_out) {

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(UserMainActivity.this, MainActivity.class));
        } else if (id == R.id.addcontacts) {
            startActivity(new Intent(UserMainActivity.this, AddContacts.class));
        } else if (id == R.id.changepropic) {
            startActivity(new Intent(UserMainActivity.this, SetProfileImage.class));
        } else if (id == R.id.upload_docs) {
            startActivity(new Intent(this,Documents.class));

        } else if (id == R.id.view_document) {
            startActivity(new Intent(this,View_Documents.class));

        }
        else if (id == R.id.noParking) {
            startActivity(new Intent(this,NoParking_Search.class));

        }
        else if (id == R.id.report_crime) {
            Intent i=new Intent(Intent.ACTION_SEND);
            i.setData(Uri.parse("email"));
            i.putExtra(Intent.EXTRA_EMAIL,"kriteshsharma2014@gmail.com");
            i.putExtra(Intent.EXTRA_SUBJECT,"Filing a Complaint");
            i.putExtra(Intent.EXTRA_TEXT,"This is to file a complaint regarding a crime");
            i.setType("message/rfc822");
            Intent chooser = Intent.createChooser(i,"Launch Email");
            startActivity(chooser);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reportAccident(View view) {
        Intent i3=new Intent(getApplicationContext(),Report_Accident.class);
        startActivity(i3);
    }

    public void challanDetails(View view)
    {
        Intent i2=new Intent(getApplicationContext(),Challan_Details.class);
        i2.putExtra("vehno", veh.getText().toString().trim());
        startActivity(i2);
    }

    public void challanPayment(View view)
    {
        Toast.makeText(this, "Redirect to Payemnt Porta", Toast.LENGTH_SHORT).show();
        //Intent i1=new Intent(getApplicationContext(),payment.class);
     //   startActivity(i1);
    }
}
