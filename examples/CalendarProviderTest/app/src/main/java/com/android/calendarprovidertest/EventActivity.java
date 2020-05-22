package com.android.calendarprovidertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class EventActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_QUERY_EVENT = 0;

    private final static int REQUEST_CODE_UPDATE_EVENT = 2;
    private final static int REQUEST_CODE_DELETE_EVENT = 3;

    private String mCalendarId;
    private EditText mId;
    private EditText mTitle;
    private EditText mDtstart;
    private EditText mDtend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mCalendarId = getIntent().getStringExtra("calendar_id");
        Log.i("EventTEST", "mCalendarId="+mCalendarId);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Events 테이블 (Calendar_Id = "+mCalendarId+")");

        mId = findViewById(R.id._id);
        mTitle = findViewById(R.id.edit_title);
        mDtstart = findViewById(R.id.edit_dtstart);
        mDtend = findViewById(R.id.edit_dtend);

        findViewById(R.id.queryAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryEventByCalendar_ID();
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
                queryEventByCalendar_ID();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent();
                queryEventByCalendar_ID();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
                queryEventByCalendar_ID();
            }
        });

        queryEventByCalendar_ID();
    }

    private void queryEventByCalendar_ID() {
        // 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_QUERY_EVENT);
            return;
        }

        // 이벤트 테이블의 프로젝션
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };

        ContentResolver cr = getContentResolver();

        String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?) ";
        String[] selectionArgs = new String[]{mCalendarId}; // mCalendarId는 이벤트가 속한 캘린더 ID

        Cursor cursor = cr.query(
                CalendarContract.Events.CONTENT_URI,
                EVENT_PROJECTION,
                selection,
                selectionArgs,
                null);

        // ConvertSimpleCursorAdapter 설정 및 생성
        // ConvertSimpleCusrorAdapter - epoch 시간 값을 YYYY-MM-DD HH:mm 형식으로 변
        ConvertSimpleCursorAdapter adapter = new ConvertSimpleCursorAdapter(
                getApplicationContext(),
                R.layout.event_item,
                cursor,
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND},
                new int[]{
                        R.id.tv_title,
                        R.id.tv_dtstart,
                        R.id.tv_dtend},
                0);

        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();

                mId.setText(((Cursor) adapter.getItem(i)).getString(0));
                mTitle.setText(((Cursor) adapter.getItem(i)).getString(1));
                mDtstart.setText(convertTimeToDate(((Cursor) adapter.getItem(i)).getString(2)));
                mDtend.setText(convertTimeToDate(((Cursor) adapter.getItem(i)).getString(3)));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_QUERY_EVENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    queryEventByCalendar_ID();
                } else {
                    Toast.makeText(getApplicationContext(), "CALENDAR_READ 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_ADD_EVENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addEvent();
                } else {
                    Toast.makeText(getApplicationContext(), "CALENDAR_WRITE 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_EVENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateEvent();
                } else {
                    Toast.makeText(getApplicationContext(), "CALENDAR_WRITE 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_DELETE_EVENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deleteEvent();
                } else {
                    Toast.makeText(getApplicationContext(), "CALENDAR_WRITE 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private final static int REQUEST_CODE_ADD_EVENT = 1;

    private void addEvent() {
        // WRITE_CALENDAR 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_ADD_EVENT);
            return;
        }

        String title = ((EditText)findViewById(R.id.edit_title)).getText().toString();
        String dtstart_timeFormat = ((EditText)findViewById(R.id.edit_dtstart)).getText().toString();
        String dtend_timeFormat = ((EditText)findViewById(R.id.edit_dtend)).getText().toString();

        long startMillis = convertDateToTime(dtstart_timeFormat);
        long endMillis = convertDateToTime(dtend_timeFormat);

        ContentResolver cr = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, mCalendarId);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Seoul");
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        Log.i("EventTEST", "Event added: eventID = " + eventID);

        if (eventID > 0) {
            Toast.makeText(this, "Event Addition Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEvent() {
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_UPDATE_EVENT);
            return;
        }

        String idString = ((EditText) findViewById(R.id._id)).getText().toString();
        String title = ((EditText) findViewById(R.id.edit_title)).getText().toString();
        String dtstart = ((EditText) findViewById(R.id.edit_dtstart)).getText().toString();
        String dtend = ((EditText) findViewById(R.id.edit_dtend)).getText().toString();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DTSTART, convertDateToTime(dtstart));
        values.put(CalendarContract.Events.DTEND, convertDateToTime(dtend));

//        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(idString));
//        int rows = cr.update(updateUri, values, null, null);

        String where = "(" + CalendarContract.Events._ID + " = ?) ";
        String[] selectionArgs = new String[]{idString};
        int rows = cr.update(CalendarContract.Events.CONTENT_URI, values, where, selectionArgs);

        Log.i("EventTEST", "Rows updated: " + rows);
    }

    private void deleteEvent() {
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_DELETE_EVENT);
            return;
        }

        String idString = ((EditText) findViewById(R.id._id)).getText().toString();

        ContentResolver cr = getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(idString));
        int rows = cr.delete(deleteUri, null, null);
        Log.i("EventTEST", "Event table Rows deleted: " + rows);
        if (rows > 0) {
            Toast.makeText(this, "Event Deletion Success", Toast.LENGTH_SHORT).show();
        }
    }


    private void eventQuery(String id) {
        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    EventActivity.this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_QUERY_EVENT);
            return;
        }

        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };

        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "(" + CalendarContract.Events._ID + " = ?) ";
        String[] selectionArgs = new String[]{id};


        if (id != null) {
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            ConvertSimpleCursorAdapter adapter = new ConvertSimpleCursorAdapter(
                    getApplicationContext(),
                    R.layout.event_item,
                    cur,
                    new String[]{
                            CalendarContract.Events.TITLE,
                            CalendarContract.Events.DTSTART,
                            CalendarContract.Events.DTEND},
                    new int[]{
                            R.id.tv_title,
                            R.id.tv_dtstart,
                            R.id.tv_dtend},
                    0);

            ListView lv = findViewById(R.id.listview);
            lv.setAdapter(adapter);
        }

    }

    private long convertDateToTime(String dateFormat) {
        if (dateFormat != null && !dateFormat.equals("")) {

            long epoch = 0;
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                epoch = sdf.parse(dateFormat).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return epoch;
        }
        return 0;
    }

    private String convertTimeToDate(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        String dateformat = sdf.format(new java.util.Date (Long.parseLong(timeString)));

        return dateformat;
    }
}
