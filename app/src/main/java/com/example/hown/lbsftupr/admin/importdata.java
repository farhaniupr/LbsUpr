package com.example.hown.lbsftupr.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.JadwalCSV;
import com.example.hown.lbsftupr.model.JadwalUCSV;
import com.example.hown.lbsftupr.model.User;
import com.example.hown.lbsftupr.reader.CSVReader;
import com.example.hown.lbsftupr.reader.CSVReaderUAS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class importdata extends AppCompatActivity {

    public static String TAG =  importdata.class.getSimpleName();

    public Button b1, b2, b3;

    private EditText e1, e2, e3;

    String  keynext, keynextUAS, keynextAkun;

    final String[] key = new String[1];
    final String[] keyUAS = new String[1];
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    final String[] keyAkun = new String[1];

    ProgressDialog pd;
    File yourFile, yourFile1, yourFile2;

    static List<User> studentList = new ArrayList<User>();





    private static final String DEBUG_TAG = "CSVReader";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importdata);

        //pd = new ProgressDialog(importdata.this);

        //pd.setMessage("Tunggu");


        e1 = (EditText) findViewById(R.id.editText5);  // jadwal kuliah

        e2 = (EditText) findViewById(R.id.editText6);  // jadwal UAS

        e3 = (EditText) findViewById(R.id.editText7);  // akun

        b1 = (Button) findViewById(R.id.buttonOpen);

        e1.setEnabled(false);

        e2.setEnabled(false);

        e3.setEnabled(false);



        if (CheckPermission(importdata .this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // you have permission go ahead

        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(importdata.this, android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_RUNTIME_PERMISSION);
        }



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 1);

                 //   } else {
                  //      requestPermission();
                  //  }
               // }

            }
        });



        b2 = (Button) findViewById(R.id.buttonOpenUAS);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 2);
                //GetFile1.execute();
            }
        });

        b3 = (Button) findViewById(R.id.buttonOpenAkun);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 3);
            }
        });
        //readData();
    }

    public void showProgress(boolean show)
    {

        if (show)
        {
            pd.setCancelable(false);
            if (!pd.isShowing())
                pd.show();
        }
        else
        {
            //Log.i(TAG, " " + mProgressDialog.isShowing());
            pd.setCancelable(true);
            if (pd.isShowing())
            {
                pd.dismiss();
            }
        }}

    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case REQUEST_RUNTIME_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // you have permission go ahead

                } else {
                    // you do not have permission show toast.
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission)) {
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Permission},
                        Code);
            }
        }
    }

    public boolean CheckPermission(Activity context, String Permission) {
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK) {

                    String PathHolder = data.getData().getPath();

                    String fi=PathHolder.substring(PathHolder.lastIndexOf("/")+1);

                    //File yourFile = new File(dir, PathHolder);
                    //File myFile = new File("/sdcard/"+filename);
                    //Environment.getExternalStorageDirectory().getAbsolutePath()
                    //File yourFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fi);

                    //File yourFile = new File("/sdcard/","test.csv");
                    //File yourFile = new File("/Kartu SD", "test.csv");


                    //untuk emulator
                    //yourFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                      //      +fi);
                    //device
                    yourFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS), fi);

                    e1.setText(yourFile.toString());
                    Toast.makeText(importdata.this, yourFile.toString(), Toast.LENGTH_LONG).show();
                    new GetFile1().execute();
                    Toast.makeText(importdata.this, "Berhasil Diimport ", Toast.LENGTH_LONG).show();
                }
                break;

            case 2:
                if(resultCode==RESULT_OK){
                    String PathHolder = data.getData().getPath();

                    //Toast.makeText(importdata.this, PathHolder , Toast.LENGTH_LONG).show();

                    File dir = Environment.getExternalStorageDirectory();
                    //File yourFile = new File(dir, PathHolder);

                    String d = data.getData().toString();
                    //File dir = Environment.getExternalStorageDirectory();
                    String fi=PathHolder.substring(PathHolder.lastIndexOf("/")+1);
                    //File yourFile = new File(PathHolder);


                    //untuk emulator
                    //yourFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                     //      +fi);


                    //untuk device > internal > documents
                    yourFile1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fi);


                    Toast.makeText(importdata.this, yourFile1.toString(), Toast.LENGTH_LONG).show();

                    e2.setText(yourFile1.toString());

                    //example
                    /**String str = "one<1234567>,two<98765432>,three<878897656>";
                     Matcher m = Pattern.compile("<(.+?)>").matcher(str);
                     while(m.find()) {
                     String v = m.group(1);
                     Toast.makeText(importdata.this, v, Toast.LENGTH_LONG).show();
                     }*/

                    //final ProgressDialog progress = new ProgressDialog(importdata.this);
                    //progress.show(importdata.this, "Loading", "Tunggu...");

                    new GetFile2().execute();
                    Toast.makeText(importdata.this, "Berhasil Diimport ", Toast.LENGTH_LONG).show();

                }
                break;

            case 3:
                if(resultCode==RESULT_OK){
                    String PathHolder = data.getData().getPath();

                    Toast.makeText(importdata.this, (Environment.getExternalStorageState()) , Toast.LENGTH_LONG).show();

                    File dir = Environment.getExternalStorageDirectory();

                    String d = data.getData().toString();
                    //File dir = Environment.getExternalStorageDirectory();
                    String fi=PathHolder.substring(PathHolder.lastIndexOf("/")+1);
                    //File yourFile = new File(PathHolder);

                     //untuk emulator
                    //yourFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                      //      +fi);

                    //untuk device > internal > documents
                    yourFile2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fi);


                    e3.setText(yourFile2.toString());


                    new GetFile3().execute();
                    Toast.makeText(importdata.this, "Berhasil Diimport ", Toast.LENGTH_LONG).show();



                }
                break;

        }
    }

    private static void ReadXLS3(File yourFile) throws IOException {
        //List<User> studentList = new ArrayList<User>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(yourFile);

            // Using XSSF for xlsx format, for xls use HSSF
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            int numberOfSheets = workbook.getNumberOfSheets();

            //looping over each workbook sheet
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();

                //iterating over each row
                while (rowIterator.hasNext()) {

                    User student = new User();
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    //Iterating over each cell (column wise)  in a particular row.
                    while (cellIterator.hasNext()) {

                        Cell cell = cellIterator.next();
                        //The Cell Containing String will is name.
                       /** if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                            student.setNim(cell.getStringCellValue());

                            //The Cell Containing numeric value will contain marks
                        } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {

                            //Cell with index 1 contains marks in Maths
                            if (cell.getColumnIndex() == 1) {
                                student.setMaths(String.valueOf(cell.getNumericCellValue()));
                            }
                            //Cell with index 2 contains marks in Science
                            else if (cell.getColumnIndex() == 2) {
                                student.setScience(String.valueOf(cell.getNumericCellValue()));
                            }
                            //Cell with index 3 contains marks in English
                            else if (cell.getColumnIndex() == 3) {
                                student.setEnglish(String.valueOf(cell.getNumericCellValue()));
                            }
                        }*/

                        student.setNim(String.valueOf(cell.getNumericCellValue()));
                        student.setNama(cell.getStringCellValue());
                        //student.setJadwal(cell.get




                    }
                    //end iterating a row, add all the elements of a row in list
                    studentList.add(student);
                }
            }

            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return studentList;
    }

    private class GetFile1  extends AsyncTask<Void, Void, Void> {

        private static final String LOG_TAG = "t";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = new ProgressDialog(importdata.this);
            pd.setMessage("Tunggu...");
            //pd.setCancelable(false);
            pd.show();



        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {

                final List<JadwalCSV> users = CSVReader.readFile(importdata.this, yourFile, JadwalCSV.class);

                //FileInputStream fis = new FileInputStream(yourFile);

                //Workbook workbook = new XSSFWorkbook(fis);

                //ReadXLS3(yourFile);


                for (final JadwalCSV o: users) {
                    Log.d(DEBUG_TAG, o.toString());

                    final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwal");

                    final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("ruangan");



                    //final DatabaseReference myDatabaseReference

                    //final String getk = myDatabaseReference.push().getKey();

                    Map<String, Object> jadwal = new HashMap<>();
                    jadwal.put("matakuliah",o.getmatakuliah().toString());
                    jadwal.put("hari",o.gethari().toString());
                    jadwal.put("jam",o.getjam().toString());
                    jadwal.put("dosen_koordinator",o.getDosen_koordinator().toString());
                    //jadwal.put("lokasi",o.getlokasi().toString());
                    myDatabaseReference.child(o.getKodeMk().toString()).child(o.gethari().toString()).updateChildren(jadwal);


                    String str = o.getlokasi().toString() ;
                    Matcher m = Pattern.compile("((.+?))").matcher(str);
                    while(m.find()) {
                        String v = m.group(1);
                        //Toast.makeText(importdata.this, v, Toast.LENGTH_LONG).show();

                        Query q1  =  myDatabaseReferenceruangan.orderByChild("id").equalTo(v);

                        q1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    //jadwalkey[0] = childSnapshot.getKey();

                                    final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("jadwal").child(o.getKodeMk().toString()).child("ruangan");
                                    key[0] = childSnapshot.getKey();
                                    keynext = key[0];
                                    Log.d("Key Ruangan :", key[0]);
                                    //Toast.makeText(importdata.this, keynext, Toast.LENGTH_LONG).show();

                                    Map<String, Object> r1 = new HashMap<>();
                                    r1.put(keynext,true);

                                    db2.updateChildren(r1);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }


                //Toast.makeText(importdata.this, "Berhasil Diimport data jadwal kuliah", Toast.LENGTH_LONG).show();

            }

            catch(IOException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(IllegalAccessException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InstantiationException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InvocationTargetException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }




            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pd.isShowing())
                pd.dismiss();

        }


    }

    private class GetFile2  extends AsyncTask<Void, Void, Void> {

        private static final String LOG_TAG = "t";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = new ProgressDialog(importdata.this);
            pd.setMessage("Tunggu...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {

                //ArrayList<Jadwal> jad = new ArrayList<Jadwal>();
                final List<JadwalUCSV> users = CSVReaderUAS.readFile(importdata.this, yourFile1, JadwalUCSV.class);
                //List<Jadwal>)(Object) users;
                for (final JadwalUCSV o: users) {
                    Log.d(DEBUG_TAG, o.toString());

                    final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwalU");

                    final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("ruangan");



                    //final DatabaseReference myDatabaseReference

                    //final String getk1 = myDatabaseReference.push().getKey();

                    Map<String, Object> jadwal = new HashMap<>();
                    jadwal.put("matakuliah",o.getmatakuliah().toString());
                    jadwal.put("hari",o.gethari().toString());
                    jadwal.put("jam",o.getjam().toString());
                    jadwal.put("dosen_koordinator",o.getDosen_koordinator().toString());
                    //jadwal.put("lokasi",o.getlokasi().toString());
                    jadwal.put("tanggal", o.getTanggal());
                    myDatabaseReference.child(o.getKodeMK().toString()).updateChildren(jadwal);


                    String str = o.getlokasi().toString() ;
                    Matcher m = Pattern.compile("((.+?))").matcher(str);
                    while(m.find()) {
                        String v = m.group(1);
                        //Toast.makeText(importdata.this, v, Toast.LENGTH_LONG).show();

                        Query q1  =  myDatabaseReferenceruangan.orderByChild("id").equalTo(v);

                        q1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    //jadwalkey[0] = childSnapshot.getKey();

                                    final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("jadwalU").child(o.getKodeMK().toString()).child("ruangan");
                                    keyUAS[0] = childSnapshot.getKey();
                                    keynextUAS = keyUAS[0];
                                    Log.d("Key Ruangan :", keyUAS[0]);
                                    //Toast.makeText(importdata.this, keynext, Toast.LENGTH_LONG).show();

                                    Map<String, Object> r1 = new HashMap<>();
                                    r1.put(keynextUAS,true);

                                    db2.updateChildren(r1);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                //
            }
            catch(IOException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(IllegalAccessException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InstantiationException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InvocationTargetException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }



            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pd.isShowing())
                pd.dismiss();

        }


    }

    private class GetFile3  extends AsyncTask<Void, Void, Void> {

        private static final String LOG_TAG = "t";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = new ProgressDialog(importdata.this);
            pd.setMessage("Tunggu...");
            //pd.setCancelable(false);
            pd.show();



        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                //progress.show(importdata.this, "Loading", "Tunggu...");

                //ArrayList<Jadwal> jad = new ArrayList<Jadwal>();
                //final List<User> users = CSVReaderAkun.readFile(importdata.this, yourFile2, User.class);

                FileInputStream fis = new FileInputStream(yourFile2);

                //Workbook workbook = new XSSFWorkbook(fis);

                ReadXLS3(yourFile2);

                //List<Jadwal>)(Object) users;
                for (final User o: studentList) {
                    Log.d(DEBUG_TAG, o.toString());


                    final String email = ("dbc"+o.getNim().toString()+ "@lbsupr.com");
                    final String password = o.getPassword().toString();


                    final FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(importdata.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        //BUAT AKUN BARU
                                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(importdata.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {


                                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                                    //Toast.makeText(importdata.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                } else
                                                if (task.isSuccessful())
                                                {

                                                    Log.w("", "Berhasil dibuat", task.getException());
                                                    final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



                                                    final DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);


                                                    final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwalU");

                                                    final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("jadwal");


                                                    String str = o.getJadwalU().toString() ;

                                                    Map<String, Object> jadwal = new HashMap<>();
                                                    jadwal.put("nama", o.getNama().toString());
                                                    jadwal.put("nim", "dbc"+o.getNim().toString());
                                                    jadwal.put("password", o.getPassword().toString());
                                                    //jadwal.put("lokasi",o.getlokasi().toString());
                                                    refnama1.updateChildren(jadwal);

                                                    if (!str.isEmpty()) {

                                                    Query q1  =  myDatabaseReferenceruangan.orderByChild("matakuliah").equalTo(str);

                                                    q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                                    //jadwalkey[0] = childSnapshot.getKey();


                                                                    final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");
                                                                    final DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");
                                                                    keyAkun[0] = childSnapshot.getKey();
                                                                    keynextAkun = keyAkun[0];


                                                                    Log.d("Key Matakuliah :", keyAkun[0]);
                                                                    //Toast.makeText(importdata.this, keynextAkun, Toast.LENGTH_LONG).show();



                                                                    myDatabaseReferenceruangan.child(keynextAkun).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                Map<String, Object> r1 = new HashMap<>();
                                                                                r1.put(keynextAkun, true);

                                                                                DatabaseReference dbcekJU = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                                                                                dbcekJU.child(keynextAkun).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.exists()){
                                                                                            Map<String, Object> r3 = new HashMap<>();
                                                                                            r3.put(keynextAkun, true);
                                                                                            db3.updateChildren(r3);
                                                                                        } else
                                                                                        {
                                                                                            //Toast.makeText(importdata.this, "Data jadwal UAS mahasiswa tidak ditambahkan karena tidak ada dalam Master daftar jadwal kuliah", Toast.LENGTH_LONG).show();

                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                    }
                                                                                });

                                                                                db2.updateChildren(r1);
                                                                            }
                                                                            else
                                                                            {
                                                                                //Toast.makeText(importdata.this, "Data jadwal kuliah mahasiswa tidak ditambahkan karena tidak ada dalam Master daftar jadwal kuliah", Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                }

                                                                /**Map<String, Object> jadwal = new HashMap<>();
                                                                 jadwal.put("nama", o.getNama().toString());
                                                                 jadwal.put("nim", "dbc"+o.getNim().toString());
                                                                 jadwal.put("password", o.getPassword().toString());
                                                                 //jadwal.put("lokasi",o.getlokasi().toString());
                                                                 refnama1.updateChildren(jadwal);*/
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                                //}
                                                }



                                            }
                                        });


                                        //}
                                    } else if (task.isSuccessful()) {


                                        //Log.d("Berhasil masuk", keyAkun[0]);
                                        Log.w("", "Berhasil masuk", task.getException());
                                        // BILA ADA AKUN BUAT TABEL
                                        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



                                        final DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);


                                        final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwalU");

                                        final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("jadwal");


                                        String str = o.getJadwalU().toString() ;

                                        Map<String, Object> jadwal = new HashMap<>();
                                        jadwal.put("nama", o.getNama().toString());
                                        jadwal.put("nim", "dbc"+o.getNim().toString());
                                        jadwal.put("password", o.getPassword().toString());
                                        //jadwal.put("lokasi",o.getlokasi().toString());
                                        refnama1.updateChildren(jadwal);


                                        Query q1  =  myDatabaseReferenceruangan.orderByChild("matakuliah").equalTo(str);

                                        q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                        //jadwalkey[0] = childSnapshot.getKey();


                                                        final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");
                                                        final DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwalU");
                                                        keyAkun[0] = childSnapshot.getKey();
                                                        keynextAkun = keyAkun[0];


                                                        Log.d("Key Matakuliah :", keyAkun[0]);
                                                        //Toast.makeText(importdata.this, keynextAkun, Toast.LENGTH_LONG).show();



                                                        myDatabaseReferenceruangan.child(keynextAkun).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    Map<String, Object> r1 = new HashMap<>();
                                                                    r1.put(keynextAkun, true);

                                                                    DatabaseReference dbcekJU = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                                                                    dbcekJU.child(keynextAkun).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                Map<String, Object> r3 = new HashMap<>();
                                                                                r3.put(keynextAkun, true);
                                                                                db3.updateChildren(r3);
                                                                            } else
                                                                            {
                                                                                //Toast.makeText(importdata.this, "Data jadwal UAS mahasiswa tidak ditambahkan karena tidak ada dalam Master daftar jadwal kuliah", Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    db2.updateChildren(r1);
                                                                }
                                                                else
                                                                {
                                                                    //Toast.makeText(importdata.this, "Data jadwal kuliah mahasiswa tidak ditambahkan karena tidak ada dalam Master daftar jadwal kuliah", Toast.LENGTH_LONG).show();

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                    }


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });





                                    }
                                }
                            });



                }

            }

            catch(IOException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            /**catch(IllegalAccessException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InstantiationException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }
            catch(InvocationTargetException e) {
                Log.e(DEBUG_TAG, e.getMessage());
            }*/


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pd.isShowing())
                pd.dismiss();

        }


    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( pd!=null && pd.isShowing() ){
            pd.cancel();
        }
    }

}
