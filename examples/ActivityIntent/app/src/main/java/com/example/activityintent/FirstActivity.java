package com.example.activityintent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    private static final int FIRST_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, getLocalClassName() + ".onCreate");

        setContentView(R.layout.activity_first);

        Button btn = findViewById(R.id.buttonFirstActivity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.buttonThirdActivity);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);

                EditText edit = findViewById(R.id.edit_data);
                intent.putExtra("dataFromFirstActivity", edit.getText().toString());

                startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * 버튼 클릭 이벤트 처리
     * @param view : 클릭된 버튼 객체
     *
     * 클릭된 버튼 객체가 무엇인지를 id를 통해 인지하여, 두 가지 다른 인텐트 객체를 생성
     */
    public void doOnBtnClick(View view) {
        Intent implicit_intent = null;
        switch (view.getId()) {
            case R.id.buttonDialActivity:
                // 114 전화번호로 다이얼 작업을 수행할 수 있도록 인텐트 설정
                implicit_intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:114"));
                break;
            case R.id.buttonMapActivity:
                // 주어진 위도,경도 위치로 지도를 보여줄 수 있도록 인텐트 설정
                implicit_intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:37.5817599,127.0081608"));
                break;
        }
        if (implicit_intent != null)
            startActivity(implicit_intent);
    }

    @Override
    /**
     * FirstActivity에서 시작시킨 액티비티가 종료되면 호출되는 메소드로서,
     * 인텐트 객체인 data의 Extra에 저장된 결과를 "ResultString" 키 값으로 얻어와서
     * 화면 레이아웃에 포함된 EditText 객체에 출력한다.
     * @param requestCode: 액티비티를 호출할 때 전달한 요청코드
     * @param resultCode: 액티비티의 실행결과
     * @param data: 호출된 액티비티의 수행결과가 Extras를 통해 전달된 인텐트 객체.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결과를 반환하는 액티비티가 FIRST_ACTIVITY_REQUEST_CODE 요청코드로 시작된 경우가 아니거나
        // 결과 데이터가 빈 경우라면, 메소드 수행을 바로 반환함.
        if (requestCode != FIRST_ACTIVITY_REQUEST_CODE || data == null)
            return;
        String msg = data.getStringExtra("ResultString");

        EditText edit = findViewById(R.id.edit_data);
        edit.setText(msg);
    }

    private static final String TAG = "Lifecycle";

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, getLocalClassName() + ".onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, getLocalClassName() + ".onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, getLocalClassName() + ".onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, getLocalClassName() + ".onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, getLocalClassName() + ".onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, getLocalClassName() + ".onDestroy");
    }
}
