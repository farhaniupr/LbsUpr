package com.example.hown.lbsftupr.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.JadwalCSV;
import com.example.hown.lbsftupr.model.JadwalUCSV;
import com.example.hown.lbsftupr.model.User;
import com.example.hown.lbsftupr.model.ju;
import com.example.hown.lbsftupr.model.juCSV;
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

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportExcel extends AppCompatActivity {

    private static final String TAG = "ImportExcel";

    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;

    ProgressBar progressBar;

    Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ProgressDialog pd;

    RadioButton r1, r2, r3;

    ArrayList<User> uploadData = new ArrayList<User>();
    ArrayList<JadwalCSV> uploadDataKuliah = new ArrayList<JadwalCSV>();
    ArrayList<JadwalUCSV> uploadDataUAS = new ArrayList<JadwalUCSV>();

    ListView lvInternalStorage;

    String  keynext, keynextUAS, keynextAkun;

    final String[] key = new String[1];
    final String[] keyUAS = new String[1];
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    final String[] keyAkun = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_excel);

        lvInternalStorage = (ListView) findViewById(R.id.lvInternalStorage);

        btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        btnSDCard = (Button) findViewById(R.id.btnViewSDCard);

        r3 = (RadioButton) findViewById(R.id.radioButton5); //akun
        r1 = (RadioButton) findViewById(R.id.radioButton3); // kuliah
       // r2 = (RadioButton) findViewById(R.id.radioButton4); //uas

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                //r2.setChecked(false);
                r3.setChecked(true);
            }
        });

        /**r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r3.setChecked(false);
                r2.setChecked(true);
            }
        });**/

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r3.setChecked(false);
                //r2.setChecked(false);
                r1.setChecked(true);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        checkFilePermissions();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.
                    if (r3.isChecked()) {
                        /**pd = new ProgressDialog(ImportExcel.this);
                        pd.setMessage("Tunggu...");
                        pd.setCancelable(false);
                        pd.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {*/
                                readExcelData(lastDirectory);
                        Toast.makeText(ImportExcel.this, "Berhasil Diimport", Toast.LENGTH_SHORT).show();

                                /**runOnUiThread(new Runnable() {
                                    @Override
                                    public void run () {
                                        pd.dismiss();
                                        pd.setCancelable(true);
                                        Toast.makeText(ImportExcel.this, "Berhasil Diimport", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();*/
                    } else
                        if (r1.isChecked()) {
                            readExcelDataKuliah(lastDirectory);
                            Toast.makeText(ImportExcel.this, "Berhasil Diimport", Toast.LENGTH_SHORT).show();
                        }
                        //else if (r2.isChecked())
                       // {
                          //  readExcelDataUAS(lastDirectory);
                          //  Toast.makeText(ImportExcel.this, "Berhasil Diimport", Toast.LENGTH_SHORT).show();
                        //}
                        else {
                            Toast.makeText(ImportExcel.this, "Pilih Salah Satu Import", Toast.LENGTH_SHORT).show();
                        }
                }else
                {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }
        });

        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                }else{
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });

        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();

            }
        });


    }

    public void showProgressDialog() {
        if (pd == null) {
            pd= new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
        }
        pd.show();
    }

    public void hideProgressDialog() {
        if (pd!= null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            for (int i = 0; i < listFile.length; i++)
            {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
            //Toast.makeText(ImportExcel.this,"Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *reads the excel file columns then rows. Stores data as ExcelUploadData object
     * @return
     */
    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            final StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>4){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ",");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            //pd.show(ImportExcel.this, "Proses Import", "Tunggu", true);
            //you usually don't want the user to stop the current process, and this will make sure of that
            //pd.setCancelable(false);


                    parseStringBuilder(sb);

            //parseStringBuilder(sb);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    private void readExcelDataKuliah(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>6){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ",");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilderKuliah(sb);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    public void parseStringBuilderKuliah(StringBuilder mStringBuilder){
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        uploadDataKuliah.clear();

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try{
                String KodeMk = columns[0];
                String hari = columns[1];
                String mk = columns[2];
                String jam = columns[3];
                String dosen = columns[4];
                String lokasi = columns[5];

                String cellInfo = "(KodeMk, hari, mk, jam, dosen, lokasi): (" + KodeMk+ "," + hari + "," + mk + "," + jam + "," + dosen + "," + lokasi +")";
                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadDataKuliah.add(new JadwalCSV(KodeMk, hari, dosen, jam,  lokasi,mk));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }
        //printDataToLog();
        new GetFile1().execute();


    }

    private void readExcelDataUAS(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if(c>7){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    }else{
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ",");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilderUAS(sb);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    public void parseStringBuilderUAS(StringBuilder mStringBuilder){
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        uploadDataUAS.clear();

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try{
                String KodeMk = columns[0];
                String hari = columns[1];
                String mk = columns[2];
                String jam = columns[3];
                String dosen = columns[4];
                String lokasi = columns[5];
                String tanggal = columns[6];

                String cellInfo = "(KodeMk, hari, mk, jam, dosen, lokasi, tanggal): (" + KodeMk+ "," + hari + "," + mk + "," + jam + "," + dosen + "," + lokasi + "," + tanggal +")";
                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadDataUAS.add(new JadwalUCSV(KodeMk, hari, dosen, jam,  lokasi,mk, tanggal));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }
        new GetFile2().execute();
    }

    /**
     * Method for parsing imported data and storing in ArrayList<XYValue>
     */
    public void parseStringBuilder(StringBuilder mStringBuilder){
        Log.d(TAG, "parseStringBuilder: Started parsing.");
        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        uploadData.clear();

        //Add to the ArrayList<XYValue> row by row
        for(int i=0; i<rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try{
                String nim = columns[0];
                String nama = columns[1];
                String kd = columns[2];
                String NamaMk = columns[3];
                //String NamaMk2 = columns[4];

                String cellInfo = "(nama, nim, NamaMk): (" + nama+ "," + nim + "," + NamaMk +")";
                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new User(nama, nim, nim, kd, NamaMk));

            }catch (NumberFormatException e){

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();

        GetFile3 g1 = (GetFile3) new GetFile3().execute();

        try {
            Void temp = g1.get();
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            auth.signInWithEmailAndPassword("admin@lbsupr.com", "123456").addOnCompleteListener(ImportExcel.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                                                    //progress[0].dismiss();
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(ImportExcel.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                    } else
                    if (task.isSuccessful()) {

                        Intent i = new Intent(ImportExcel.this, BerandaAdmin.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void printDataToLog() {
        Log.d(TAG, "printDataToLog: Printing data to log...");

        for(int i = 0; i< uploadData.size(); i++){
            String nim = uploadData.get(i).getNim();
            String nama = uploadData.get(i).getNama();
            String kd = uploadData.get(i).getJadwal();
            String NamaMk = uploadData.get(i).getJadwalU();
            Log.d(TAG, "printDataToLog: (x,y): (" + nama+ "," + nim + "," + NamaMk +")");
        }
    }

    /**
     * Returns the cell as a string from the excel file
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = null;
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    //value = ""+cellValue.getBooleanValue();
                    value = String.valueOf(cellValue.getBooleanValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    int numericValue = (int) cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        java.text.SimpleDateFormat formatter =
                                //new SimpleDateFormat("MM/dd/yy");
                                new SimpleDateFormat("dd MMM yyyy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        //value = ""+numericValue;
                        value = String.valueOf((numericValue));
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = String.valueOf(cellValue.getStringValue());
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            /**int permissionCheck = this.checkSelfPermission("android.Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("android.Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }*/
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private class GetFile3  extends AsyncTask<Void, Void, Void> {

        private static final String LOG_TAG = "t";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //progressBar.setIndeterminate(true);
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.setMax(uploadData.size());
            //progressBar.setProgress(0);
            pd = new ProgressDialog(ImportExcel.this);
            pd.setMessage("Tunggu...");
            //pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //pd.setMax(uploadData.size());
            //pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            final int[] count = {0};
            final int[] allitem = {uploadData.size()};
            for (final User o: uploadData) {

                int Bar = uploadData.size();

                Log.d(TAG, o.toString());

                //

                final String email = (o.getNim().toString()+ "@lbsupr.com");
                final String password = o.getPassword().toString();

                final List<String> l1 = new ArrayList<String>();
                l1.add(o.getNim().toString());


                final FirebaseAuth auth = FirebaseAuth.getInstance();



                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ImportExcel.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    //BUAT AKUN BARU
                                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(ImportExcel.this, new OnCompleteListener<AuthResult>() {
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
                                                jadwal.put("nim", o.getNim().toString());
                                                jadwal.put("password", o.getPassword().toString());
                                                jadwal.put("username", o.getNim().toString());
                                                //jadwal.put("lokasi",o.getlokasi().toString());
                                                refnama1.updateChildren(jadwal);

                                                if (!str.isEmpty()) {

                                                    Query q1  =  myDatabaseReferenceruangan.orderByChild("matakuliah").equalTo(str);

                                                    q1.addValueEventListener(new ValueEventListener() {
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



                                                                    myDatabaseReferenceruangan.child(keynextAkun).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                Map<String, Object> r1 = new HashMap<>();
                                                                                r1.put(keynextAkun, true);

                                                                                DatabaseReference dbcekJU = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                                                                                dbcekJU.child(keynextAkun).addValueEventListener(new ValueEventListener() {
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

                                } else if (task.isSuccessful()) {



                                    //Log.d("Berhasil masuk", keyAkun[0]);
                                    Log.w("", "Berhasil masuk", task.getException());
                                    // BILA ADA AKUN BUAT TABEL

                                    final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    final DatabaseReference refnama1 = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);

                                    final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwalU");

                                    final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("jadwal");


                                    Map<String, Object> jadwal = new HashMap<>();
                                    jadwal.put("nama", o.getNama().toString());
                                    jadwal.put("nim", o.getNim().toString());
                                    jadwal.put("password", o.getPassword().toString());
                                    jadwal.put("username", o.getNim().toString());
                                    //jadwal.put("lokasi",o.getlokasi().toString());
                                    refnama1.updateChildren(jadwal);

                                    count[0]++;

                                    String str = o.getJadwalU().toString() ;

                                    Query q1  =  myDatabaseReferenceruangan.orderByChild("matakuliah").equalTo(str);

                                            //final int count = 0;
                                    q1.addValueEventListener(new ValueEventListener() {
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

                                                    myDatabaseReferenceruangan.child(keynextAkun).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()){
                                                                        Map<String, Object> r1 = new HashMap<>();
                                                                        r1.put(keynextAkun, true);

                                                                        DatabaseReference dbcekJU = FirebaseDatabase.getInstance().getReference().child("jadwalU");
                                                                        dbcekJU.child(keynextAkun).addValueEventListener(new ValueEventListener() {
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
                                                                    //pd.dismiss();
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
                                                    //hideProgressDialog();
                                                }
                                        });

                                    //E e = list.get(list.size() - 1);

                                    //final int finalCount = count;

                                    /**final int finalCount = count[0];
                                    Log.d("Nilai Final Count:", String.valueOf(finalCount));
                                    Log.d("Nilai Count:", String.valueOf(count[0]));
                                    final DatabaseReference refnama2 = FirebaseDatabase.getInstance().getReference().child("user");
                                    refnama2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                                //  ]  for (DataSnapshot postSnapshot3 : postSnapshot2.getChildren()) {
                                                if (finalCount > postSnapshot2.getChildrenCount()) {
                                                    pd.dismiss();
                                                }
                                            }
                                            //}
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });*/


                                    //Bekerja tapi ada BUG
                                    /**Log.d("Nilai Final Count:", uploadData.get(uploadData.size()-1).toString());
                                    String lastString = uploadData.get(uploadData.size()-1).toString();
                                    String[] split = lastString.split("\n");
                                    String firstLine = split[0];
                                    Log.d("On String :", lastString);
                                    Log.d("Nilai after split :", firstLine);
                                    //if (l1 != null ) {
                                        //uploadData.get(uploadData.size()-1).toString();
                                        final DatabaseReference refnama2 = FirebaseDatabase.getInstance().getReference().child("user");
                                        Query q = refnama2.orderByChild("nama").equalTo(firstLine);
                                        q.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists())
                                                {
                                                    pd.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });*/

                                    //coba

                                    //allitem[0] = uploadData.size();
                                    Log.d("Count:", String.valueOf(allitem[0]));
                                    final int finalCount = count[0];
                                    //progressBar.setProgress(finalCount);
                                    //pd.incrementProgressBy(finalCount);
                                    Log.d("Nilai Final Count:", String.valueOf(finalCount));
                                    if (finalCount >= allitem[0]) {
                                        pd.dismiss();
                                        //progressBar.setVisibility(View.GONE);
                                    }

                                }
                            }
                        });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //auth.
        }

    }

    private class GetFile2  extends AsyncTask<Void, Void, Void> {

        private static final String LOG_TAG = "t";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pd = new ProgressDialog(ImportExcel.this);
            //pd.setMessage("Tunggu...");
            //pd.setCancelable(false);
            //pd.show();

            pd = new ProgressDialog(ImportExcel.this);
            pd.setMessage("Tunggu...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(uploadData.size());
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final int[] countUAS = {0};
            final int[] allitemUAS = {uploadDataUAS.size()};

            for (final JadwalUCSV o: uploadDataUAS) {
                Log.d(TAG, o.toString());

                final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwalU");

                final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("ruangan");

                String jam = o.getjam().replace(".",":");

                Map<String, Object> jadwal = new HashMap<>();
                jadwal.put("matakuliah",o.getmatakuliah().toString());
                jadwal.put("hari",o.gethari().toString());
                jadwal.put("jam",jam);
                jadwal.put("dosen_koordinator",o.getDosen_koordinator().toString());
                //jadwal.put("lokasi",o.getlokasi().toString());
                jadwal.put("tanggal", o.getTanggal());
                //Log.d(TAG, o.getKodeMK());
                myDatabaseReference.child(o.getKodeMK().toString()).updateChildren(jadwal);

                countUAS[0]++;


                String str = o.getlokasi().toString() ;
                Matcher m = Pattern.compile("<(.+?)>").matcher(str);
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

                                //pd.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                allitemUAS[0] = uploadDataUAS.size();
                Log.d("Count:", String.valueOf(allitemUAS[0]));
                final int finalCountUAS = countUAS[0];
                Log.d("Nilai Final Count:", String.valueOf(finalCountUAS));
                if (finalCountUAS >= allitemUAS[0]) {
                    pd.dismiss();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
        }
    }

    private class GetFile1  extends AsyncTask<Void, Void, Void> {

       private static final String LOG_TAG = "t";

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
            pd = new ProgressDialog(ImportExcel.this);
            pd.setMessage("Tunggu...");
            pd.setCancelable(false);
            pd.show();
       }

       @Override
       protected Void doInBackground(Void... arg0) {
           final int[] countKuliah = {0};
           final int[] allitemKuliah = {uploadDataKuliah.size()};

           for (final JadwalCSV o: uploadDataKuliah) {
               Log.d(TAG, o.toString());

               final DatabaseReference myDatabaseReference= FirebaseDatabase.getInstance().getReference().child("jadwal");

               final DatabaseReference myDatabaseReferenceruangan= FirebaseDatabase.getInstance().getReference().child("ruangan");


               String jam = o.getjam().replace(".",":");
               //jam.replace(".",":");
               Log.d("nilai jam before :", o.getjam());
               Log.d("nilai jam after :", jam);

               Map<String, Object> jadwal = new HashMap<>();
               jadwal.put("matakuliah",o.getmatakuliah().toString());
               jadwal.put("hari",o.gethari().toString());
               jadwal.put("jam",jam);
               jadwal.put("dosen_koordinator",o.getDosen_koordinator().toString());
               //jadwal.put("kodeMk",o.getKodeMk().toString());
               //jadwal.put("lokasi",o.getlokasi().toString());
               myDatabaseReference.child(o.getKodeMk().toString()).updateChildren(jadwal);

               countKuliah[0]++;
               String str = o.getlokasi().toString() ;
               Log.d("nilai String lokasi :", str);
               Matcher m = Pattern.compile("<(.+?)>").matcher(str);
               while(m.find()) {
                   String v = m.group(1);
                   Log.d("nilai lokasi :", v);
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

               allitemKuliah[0] = uploadDataKuliah.size();
               Log.d("Count:", String.valueOf(allitemKuliah[0]));
               final int finalCountKuliah = countKuliah[0];
               Log.d("Nilai Final Count:", String.valueOf(finalCountKuliah));
               if (finalCountKuliah >= allitemKuliah[0]) {
                   pd.dismiss();
               }

           }

           return null;
       }

       @Override
       protected void onPostExecute(Void result) {
           super.onPostExecute(result);
           // Dismiss the progress dialog
           /**if (pd.isShowing())
               pd.dismiss();*/

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
