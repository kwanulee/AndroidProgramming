package com.kwanwoo.android.tabletphonefragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                        implements TitlesFragment.OnTitleSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTitleSelected(int i) {
        //Toast.makeText(getApplicationContext(),"position="+i,Toast.LENGTH_SHORT).show();
        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setSelection(i);
            getSupportFragmentManager().beginTransaction().replace(R.id.details, detailsFragment).commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("index", i);
            startActivity(intent);
        }
    }
}
