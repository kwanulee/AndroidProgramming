package com.example.jyheo.navigationui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_action1:
                Toast.makeText(getApplicationContext(), R.string.action_quick, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), R.string.action_settings, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_subactivity:
                startActivity(new Intent(this, SubActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
