package com.kwanwoo.android.uibasic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_views);

        Button btn = findViewById(R.id.submit_button);
        btn.setOnClickListener(new ClickListener());
    }

    public void doAction(View v) {
        Toast.makeText(getApplicationContext(), R.string.button_clicked_msg,
                Toast.LENGTH_SHORT).show();
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), R.string.button_clicked_msg,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
