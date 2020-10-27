package com.example.hown.lbsftupr.reminderr.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.model.Reminder;
import com.example.hown.lbsftupr.reminderr.database.DatabaseHelper;
import com.example.hown.lbsftupr.reminderr.dialogs.AdvancedRepeatSelector;
import com.example.hown.lbsftupr.reminderr.dialogs.DaysOfWeekSelector;
import com.example.hown.lbsftupr.reminderr.dialogs.IconPicker;
import com.example.hown.lbsftupr.reminderr.dialogs.RepeatSelector;
import com.example.hown.lbsftupr.reminderr.receivers.AlarmReceiver;
import com.example.hown.lbsftupr.reminderr.utils.AlarmUtil;
import com.example.hown.lbsftupr.reminderr.utils.AnimationUtil;
import com.example.hown.lbsftupr.reminderr.utils.DateAndTimeUtil;
import com.example.hown.lbsftupr.reminderr.utils.TextFormatUtil;
import com.example.hown.lbsftupr.schedule.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateEditActivity extends AppCompatActivity implements
        IconPicker.IconSelectionListener, AdvancedRepeatSelector.AdvancedRepeatSelectionListener,
        DaysOfWeekSelector.DaysOfWeekSelectionListener, RepeatSelector.RepeatSelectionListener {

    @BindView(R.id.create_coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.notification_title) EditText titleEditText;
    //@BindView(R.id.notification_content) EditText contentEditText;
    @BindView(R.id.spinner_content) Spinner sc;

    @BindView(R.id.time) TextView timeText;
    @BindView(R.id.date) TextView dateText;
    @BindView(R.id.repeat_day) TextView repeatText;
    @BindView(R.id.switch_toggle) SwitchCompat foreverSwitch;
    @BindView(R.id.show_times_number) EditText timesEditText;
    @BindView(R.id.forever_row) LinearLayout foreverRow;
    @BindView(R.id.bottom_row) LinearLayout bottomRow;
    @BindView(R.id.bottom_view) View bottomView;
    @BindView(R.id.show) TextView showText;
    @BindView(R.id.times) TextView timesText;
    @BindView(R.id.select_icon_text) TextView iconText;

    @BindView(R.id.selected_icon) ImageView imageIconSelect;
    @BindView(R.id.error_time) ImageView imageWarningTime;
    @BindView(R.id.error_date) ImageView imageWarningDate;
    @BindView(R.id.error_show) ImageView imageWarningShow;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private String icon;
    //private String colour;
    private Calendar calendar;
    private boolean[] daysOfWeek = new boolean[7];
    private int timesShown = 0;
    private int timesToShow = 1;
    private int repeatType;
    private int id;
    private int interval = 1;
    ArrayList<Item> items = new ArrayList<Item>();
    ArrayList<String> worldlist;
    JSONObject jsonobject;
    JSONArray jsonarray;


    //private static String url = "https://lbsuprtest.000webhostapp.com/json.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(null);

        calendar = Calendar.getInstance();
        icon = getString(R.string.default_icon_value);
        //colour = getString(R.string.default_colour_value);
        repeatType = Reminder.DOES_NOT_REPEAT;
        id = getIntent().getIntExtra("NOTIFICATION_ID", 0);







        // Check whether to edit or create a new notification
        if (id == 0) {
            DatabaseHelper database = DatabaseHelper.getInstance(this);
            id = database.getLastNotificationId() + 1;
            database.close();
        } else {
            assignReminderValues();
        }

       new DownloadJSON().execute();


    }

    public void assignReminderValues() {
        // Prevent keyboard from opening automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DatabaseHelper database = DatabaseHelper.getInstance(this);
        final Reminder reminder = database.getNotification(id);
        database.close();

        timesShown = reminder.getNumberShown();
        repeatType = reminder.getRepeatType();
        interval = reminder.getInterval();
        icon = reminder.getIcon();
       // colour = reminder.getColour();

        calendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());

        showText.setText(getString(R.string.times_shown_edit, reminder.getNumberShown()));
        titleEditText.setText(reminder.getTitle());


        sc.setPrompt(reminder.getContent());
        dateText.setText(DateAndTimeUtil.toStringReadableDate(calendar));
        timeText.setText(DateAndTimeUtil.toStringReadableTime(calendar, this));
        timesEditText.setText(String.valueOf(reminder.getNumberToShow()));
        //colourText.setText(colour);
       // imageColourSelect.setColorFilter(Color.parseColor(colour));
        timesText.setVisibility(View.VISIBLE);

        if (!getString(R.string.default_icon).equals(icon)) {
            imageIconSelect.setImageResource(getResources().getIdentifier(reminder.getIcon(), "drawable", getPackageName()));
            iconText.setText(R.string.custom_icon);
        }

        if (reminder.getRepeatType() != Reminder.DOES_NOT_REPEAT) {
            if (reminder.getInterval() > 1) {
                repeatText.setText(TextFormatUtil.formatAdvancedRepeatText(this, repeatType, interval));
            } else {
                repeatText.setText(getResources().getStringArray(R.array.repeat_array)[reminder.getRepeatType()]);
            }
            showFrequency(true);
        }

        if (reminder.getRepeatType() == Reminder.SPECIFIC_DAYS) {
            daysOfWeek = reminder.getDaysOfWeek();
            repeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, daysOfWeek));
        }

        if (Boolean.parseBoolean(reminder.getForeverState())) {
            foreverSwitch.setChecked(true);
            bottomRow.setVisibility(View.GONE);
        }
    }


    public void showFrequency(boolean show) {
        if (show) {
            foreverRow.setVisibility(View.VISIBLE);
            bottomRow.setVisibility(View.VISIBLE);
            bottomView.setVisibility(View.VISIBLE);
        } else {
            foreverSwitch.setChecked(false);
            foreverRow.setVisibility(View.GONE);
            bottomRow.setVisibility(View.GONE);
            bottomView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.time_row)
    public void timePicker() {
        TimePickerDialog TimePicker = new TimePickerDialog(CreateEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                timeText.setText(DateAndTimeUtil.toStringReadableTime(calendar, getApplicationContext()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        TimePicker.show();
    }

    @OnClick(R.id.date_row)
    public void datePicker(View view) {
        DatePickerDialog DatePicker = new DatePickerDialog(CreateEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker DatePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateText.setText(DateAndTimeUtil.toStringReadableDate(calendar));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker.show();
    }

    @OnClick(R.id.icon_select)
    public void iconSelector() {
        DialogFragment dialog = new IconPicker();
        dialog.show(getSupportFragmentManager(), "IconPicker");
    }

    @Override
    public void onIconSelection(DialogFragment dialog, String iconName, String iconType, int iconResId) {
        icon = iconName;
        iconText.setText(iconType);
        imageIconSelect.setImageResource(iconResId);
        dialog.dismiss();
    }

    /**@OnClick(R.id.colour_select)
    public void colourSelector() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        int[] colours = database.getColoursArray();
        database.close();

        new ColorChooserDialog.Builder(this, R.string.select_colour)
                .allowUserColorInputAlpha(false)
                .customColors(colours, null)
                .preselect(Color.parseColor(colour))
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColour) {
        colour = String.format("#%06X", (0xFFFFFF & selectedColour));
        imageColourSelect.setColorFilter(selectedColour);
        colourText.setText(colour);
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        database.addColour(new Colour(selectedColour, DateAndTimeUtil.toStringDateTimeWithSeconds(Calendar.getInstance())));
        database.close();
    }*/

    @OnClick(R.id.repeat_row)
    public void repeatSelector() {
        DialogFragment dialog = new RepeatSelector();
        dialog.show(getSupportFragmentManager(), "RepeatSelector");
    }

    @Override
    public void onRepeatSelection(DialogFragment dialog, int which, String repeatText) {
        interval = 1;
        repeatType = which;
        this.repeatText.setText(repeatText);
        if (which == Reminder.DOES_NOT_REPEAT) {
            showFrequency(false);
        } else {
            showFrequency(true);
        }
    }

    @Override
    public void onDaysOfWeekSelected(boolean[] days) {
        repeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, days));
        daysOfWeek = days;
        repeatType = Reminder.SPECIFIC_DAYS;
        showFrequency(true);
    }

    @Override
    public void onAdvancedRepeatSelection(int type, int interval, String repeatText) {
        repeatType = type;
        this.interval = interval;
        this.repeatText.setText(repeatText);
        showFrequency(true);
    }

    public void saveNotification() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);

        Spinner s1 = (Spinner) findViewById(R.id.spinner_content);
        int pos = s1.getSelectedItemPosition();

        String text = s1.getSelectedItem().toString();

        //if (titleEditText.getText().toString().equals(null))
        //{
           // Toast.makeText(CreateEditActivity.this, "Judul Reminder tidak boleh kosong",
                    //Toast.LENGTH_SHORT).show();
        //} else {

            Reminder reminder = new Reminder()
                    .setId(id)
                    .setTitle(titleEditText.getText().toString())
                    //.setContent(contentEditText.getText().toString())
                    //.setContent(sc.getItemAtPosition(pos).toString())
                    .setContent(sc.getItemAtPosition(pos).toString())
                    .setDateAndTime(DateAndTimeUtil.toStringDateAndTime(calendar))
                    .setRepeatType(repeatType)
                    .setForeverState(Boolean.toString(foreverSwitch.isChecked()))
                    .setNumberToShow(timesToShow)
                    .setNumberShown(timesShown)
                    .setIcon(icon)
                    //.setColour(colour)
                    .setInterval(interval);

            database.addNotification(reminder);

            if (repeatType == Reminder.SPECIFIC_DAYS) {
                reminder.setDaysOfWeek(daysOfWeek);
                database.addDaysOfWeek(reminder);
            }

            database.close();
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            calendar.set(Calendar.SECOND, 0);
            AlarmUtil.setAlarm(this, alarmIntent, reminder.getId(), calendar);
            finish();
        //}
    }

    @OnClick(R.id.forever_row)
    public void toggleSwitch() {
        foreverSwitch.toggle();
        if (foreverSwitch.isChecked()) {
            bottomRow.setVisibility(View.GONE);
        } else {
            bottomRow.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.switch_toggle)
    public void switchClicked() {
        if (foreverSwitch.isChecked()) {
            bottomRow.setVisibility(View.GONE);
        } else {
            bottomRow.setVisibility(View.VISIBLE);
        }
    }

    public void validateInput() {
        imageWarningShow.setVisibility(View.GONE);
        imageWarningTime.setVisibility(View.GONE);
        imageWarningDate.setVisibility(View.GONE);

        int selectedItemOfMySpinner = sc.getSelectedItemPosition();

        String actualPositionOfMySpinner = (String) sc.getItemAtPosition(selectedItemOfMySpinner);
        Calendar nowCalendar = Calendar.getInstance();

        if (timeText.getText().equals(getString(R.string.time_now))) {
            calendar.set(Calendar.HOUR_OF_DAY, nowCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE));
        }

        if (dateText.getText().equals(getString(R.string.date_today))) {
            calendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, nowCalendar.get(Calendar.DAY_OF_MONTH));
        }

        // Check if the number of times to show notification is empty
        if (timesEditText.getText().toString().isEmpty()) {
            timesEditText.setText("1");
        }

        timesToShow = Integer.parseInt(timesEditText.getText().toString());
        if (repeatType == Reminder.DOES_NOT_REPEAT) {
            timesToShow = timesShown + 1;
        }

        // Check if selected date is before today's date
        if (DateAndTimeUtil.toLongDateAndTime(calendar) < DateAndTimeUtil.toLongDateAndTime(nowCalendar)) {
            Snackbar.make(coordinatorLayout, R.string.toast_past_date, Snackbar.LENGTH_SHORT).show();
            imageWarningTime.setVisibility(View.VISIBLE);
            imageWarningDate.setVisibility(View.VISIBLE);

            // Check if title is empty
        } else if (titleEditText.getText().toString().trim().isEmpty()) {
            Snackbar.make(coordinatorLayout, R.string.toast_title_empty, Snackbar.LENGTH_SHORT).show();
            AnimationUtil.shakeView(titleEditText, this);
        } else if (actualPositionOfMySpinner.isEmpty()) {
            setSpinnerError(sc,getString(R.string.spinner_eror));

            // Check if times to show notification is too low
        } else if (timesToShow <= timesShown && !foreverSwitch.isChecked()) {
            Snackbar.make(coordinatorLayout, R.string.toast_higher_number, Snackbar.LENGTH_SHORT).show();
            imageWarningShow.setVisibility(View.VISIBLE);
        } else {
            saveNotification();
            Toast.makeText(CreateEditActivity.this, "Berhasil disimpan",
                    Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                validateInput();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            items = new ArrayList<Item>();
            // Create an array to populate the spinner
            worldlist = new ArrayList<String>();

            final DatabaseReference databaseItem,  databaseUserJadwal, databaseJadwal;

            //DatabaseReference databaseItem;

            final ArrayList<String> mKeys1 = new ArrayList<String>();

            databaseItem = FirebaseDatabase.getInstance().getReference().child("jadwal");

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseUserJadwal = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId).child("jadwal");

            databaseJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal");
            final String TAG = "createedit";

            /**databaseUserJadwal.addValueEventListener(new ValueEventListener() {
                public static final String TAG = "createedit";
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String key = postSnapshot.getKey();

                    mKeys1.add(key);**/



                    //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                       // for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                        databaseJadwal.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                                //for (DataSnapshot postSnapshot2 : postSnapshot1.getChildren()) {
                                    final Item item = postSnapshot1.getValue(Item.class);


                                    String matakuliah = item.getmatakuliah().toString();
                                    //.add(item);


                                    worldlist.add(matakuliah);
                                    //items.add(item);
                                    Log.d(TAG, "" + item);


                                    Spinner mySpinner = (Spinner) findViewById(R.id.spinner_content);

                                    // Spinner adapter
                                    mySpinner
                                            .setAdapter(new ArrayAdapter<String>(CreateEditActivity.this,
                                                    android.R.layout.simple_spinner_dropdown_item,
                                                    worldlist));

                                    // Spinner on item click listener
                                    mySpinner
                                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                @Override
                                                public void onItemSelected(AdapterView<?> arg0,
                                                                           View arg1, int position, long arg3) {
                                                    // TODO Auto-generated method stub
                                                    // Locate the textviews in activity_main.xml
                                                    /**TextView txtrank = (TextView) findViewById(R.id.rank);
                                                     TextView txtcountry = (TextView) findViewById(R.id.country);
                                                     TextView txtpopulation = (TextView) findViewById(R.id.population);

                                                     // Set the text followed by the position
                                                     txtrank.setText("Rank : "
                                                     + world.get(position).getRank());
                                                     txtcountry.setText("Country : "
                                                     + world.get(position).getCountry());
                                                     txtpopulation.setText("Population : "
                                                     + world.get(position).getPopulation());*/
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> arg0) {
                                                    // TODO Auto-generated method stub
                                                }
                                            });
                                }
                            }

                            //}



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //}

                    //}



               // @Override
              //  public void onCancelled(DatabaseError databaseError) {

              //  }
           // });




            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

        }
    }


}