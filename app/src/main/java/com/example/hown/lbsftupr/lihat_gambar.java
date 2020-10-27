package com.example.hown.lbsftupr;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hown.lbsftupr.model.Jadwal;
import com.example.hown.lbsftupr.model.Lantai;
import com.example.hown.lbsftupr.model.Ruangan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;



public class lihat_gambar extends AppCompatActivity {


    DatabaseReference db;
    TextView t1;
    ImageView iv;


    StorageReference islandRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_gambar);

        final ArrayList<String> matakuliahs = new ArrayList<>();
        final ArrayList<String> jams = new ArrayList<>();
        final ArrayList<String> lokasis = new ArrayList<>();
        final ArrayList<String> dsn_koors = new ArrayList<>();

        final FirebaseStorage storage = FirebaseStorage.getInstance();


        db= FirebaseDatabase.getInstance().getReference().child("ruangan");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                    // while((iterator.hasNext())){
                    Lantai value = postSnapshot2.getValue(Lantai.class);
                    lokasis.add(value.getid());

                    ((CustomListRuangan) (((ListView) findViewById(R.id.listView2)).getAdapter())).notifyDataSetChanged();

                }
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.listView2)).
                setAdapter(new CustomListRuangan (lokasis, this));

        ((ListView)findViewById(R.id.listView2)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String text = parent.getItemAtPosition(position).toString().trim();

                //int
                if (position == 0) {
                    //var gsReference = storage.refFromURL('gs://lbs-penyajian-ruang.appspot.com/r5.jpg');
                    //islandRef = storage.getReferenceFromUrl("gs://lbs-penyajian-ruang.appspot.com/r"+i+".jpg");

                    iv = (ImageView) findViewById(R.id.iv);
                   // iv.setBackground(islandRef);


                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r1.jpg?alt=media&token=2e0667d1-3bb8-44e3-92b4-785e64488623" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                } else
                if(position == 1){
                    iv = (ImageView) findViewById(R.id.iv);
                    // iv.setBackground(islandRef);

                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r2.jpg?alt=media&token=3f93cbe1-2808-4274-9245-ae3f608455ed" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                } else

                if(position == 2){
                    iv = (ImageView) findViewById(R.id.iv);
                    // iv.setBackground(islandRef);

                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r3.jpg?alt=media&token=8e4082cb-8c17-40cf-8fec-3daadecd3be6" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                } else

                if(position == 3){
                    iv = (ImageView) findViewById(R.id.iv);
                    // iv.setBackground(islandRef);

                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r4.jpg?alt=media&token=29c89d15-44c5-404d-a847-cf249bcd99fb" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                } else

                if(position == 4){
                    iv = (ImageView) findViewById(R.id.iv);
                    // iv.setBackground(islandRef);

                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r5.jpg?alt=media&token=845dd38d-455e-427f-86da-34858ff8bdf7" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                    } else

                if(position == 5){
                    iv = (ImageView) findViewById(R.id.iv);
                    // iv.setBackground(islandRef);

                    String url="https://firebasestorage.googleapis.com/v0/b/lbs-penyajian-ruang.appspot.com/o/r6.jpg?alt=media&token=e2f00daf-2c4a-4fe6-baac-cd8f84951b37" ;

                    Glide.with(getApplicationContext()).load(url).into(iv);
                }
            }
        });
    }
}