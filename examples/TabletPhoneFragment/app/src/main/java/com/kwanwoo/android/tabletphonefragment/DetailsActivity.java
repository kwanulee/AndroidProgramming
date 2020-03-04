package com.kwanwoo.android.tabletphonefragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        DetailsFragment details = new DetailsFragment();
        details.setSelection(getIntent().getIntExtra("index",-1));
        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
    }
}
