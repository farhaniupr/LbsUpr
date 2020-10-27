package com.example.hown.lbsftupr.reminderr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.example.hown.lbsftupr.R;
import com.example.hown.lbsftupr.reminderr.adapters.ReminderAdapter;
import com.example.hown.lbsftupr.reminderr.adapters.ViewPageAdapter;
import com.example.hown.lbsftupr.schedule.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderActivity extends AppCompatActivity implements ReminderAdapter.RecyclerListener {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.fab_button)
    FloatingActionButton floatingActionButton;

    private boolean fabIsHidden = false;


    //test
    public static final String[] DAYS = {"Senin", "Selasa", "Rabu",
            "Kamis", "Jumat"};
    ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        pagerSlidingTabStrip.setViewPager(viewPager);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

        //getSupportLoaderManager().initLoader(0, null, ReminderActivity.this);
    }

    @OnClick(R.id.fab_button)
    public void fabClicked() {
        Intent intent = new Intent(this, CreateEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideFab() {
        floatingActionButton.hide();
        fabIsHidden = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fabIsHidden) {
            floatingActionButton.show();
            fabIsHidden = false;
        }
    }

    /**@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**switch (item.getItemId()) {
            case R.id.action_settings:
                Intent preferenceIntent = new Intent(this, PreferenceActivity.class);
                startActivity(preferenceIntent);
                return true;
            /** case R.id.action_about:
             Intent aboutIntent = new Intent(this, AboutActivity.class);
             startActivity(aboutIntent);
             return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    //test


}