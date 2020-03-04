package com.example.activityintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("dataFromFirstActivity");
        EditText et = (EditText)findViewById(R.id.editText);
        et.setText(msg);

        Button btn = findViewById(R.id.buttonThirdActivity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultIntent = new Intent();

                EditText et = findViewById(R.id.editText);
                resultIntent.putExtra("ResultString", et.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
