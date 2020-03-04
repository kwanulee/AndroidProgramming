package com.kwanwoo.android.contentresolvertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 확인
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) { // 권한이 없으므로, 사용자에게 권한 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else // 권한 있음! 해당 데이터나 장치에 접근!
            getContacts();
    }


    private void getContacts() {
        String [] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // 연락처 전화번호 타입에 따른 행 선택을 위한 선택 절
        String selectionClause = ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";

        // 전화번호 타입이 'MOBILE'인 것을 지정
        String[] selectionArgs = {""+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE};

        Cursor c = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  // 조회할 데이터 URI
                projection,         // 조회할 컬럼 들
                selectionClause,    // 선택될 행들에 대한선택될 행들에 대한 조건절
                selectionArgs,      // 조건절에 필요한 파라미터
                null);              // 정렬 안

        String[] contactsColumns = { // 쿼리결과인 Cursor 객체로부터 출력할 열들
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        int[] contactsListItems = { // 열의 값을 출력할 뷰 ID (layout/item.xml 내)
                R.id.name,
                R.id.phone
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item,
                c,
                contactsColumns,
                contactsListItems,
                0);

        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
