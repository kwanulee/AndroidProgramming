package com.example.kwanwoo.fragmentexample;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        int index = getIntent().getIntExtra("index",-1);
        DetailsFragment details = DetailsFragment.newInstance(index);
        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
    }
}
