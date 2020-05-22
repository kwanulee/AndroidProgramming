package com.android.calendarprovidertest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;

public class CalendarQueryActivity extends AppCompatActivity {
    private String mAccountName;

    private final static int REQUEST_CODE_QUERY_CALENDARS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_query);

        Button button = findViewById(R.id.query);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryCalendars();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_QUERY_CALENDARS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    queryCalendars();
                } else {
                    Toast.makeText(getApplicationContext(), "READ_CALENDAR 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void queryCalendars() {
        // 권한 검사
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_QUERY_CALENDARS);
            return;
        }

        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        final String[] CALENDAR_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        ContentResolver contentResolver = getContentResolver();

        // 화면의 EditText 창으로부터 사용자가 입력한 문자열을 읽어와 mAccountName 변수에 저장
        mAccountName = ((EditText) findViewById(R.id.account_name)).getText().toString();

        Cursor cursor = null;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        if (mAccountName == null || mAccountName.equals(""))
            cursor = contentResolver.query(uri, CALENDAR_PROJECTION, null, null, null);
        else { // ACCOUNT_NAME 속성 값이 mAccountName의 값과 일치하는 캘린더를 추출함
            String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?)";
            String[] selectionArgs = new String[]{mAccountName};
            cursor = contentResolver.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, null);
        }

        // SimpleCursorAdapter 설정 및 생성
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.calendar_item,
                cursor,
                CALENDAR_PROJECTION,
                new int[] {R.id._id, R.id.accountName, R.id.displayName},
                0);

        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_calendarID = view.findViewById(R.id._id);
                Intent intent = new Intent(CalendarQueryActivity.this, EventActivity.class);
                String idStr = tv_calendarID.getText().toString();
                intent.putExtra("calendar_id", idStr);
                startActivity(intent);
            }
        });

    }
}
