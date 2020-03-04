package com.kwanwoo.android.customadapterview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터 원본 준비
        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.sample_0, "Bella", "1"));
        data.add(new MyItem(R.drawable.sample_1, "Charlie", "2"));
        data.add(new MyItem(R.drawable.sample_2, "Daisy", "1.5"));
        data.add(new MyItem(R.drawable.sample_3, "Duke", "1"));
        data.add(new MyItem(R.drawable.sample_4, "Max", "2"));
        data.add(new MyItem(R.drawable.sample_5, "Happy", "4"));
        data.add(new MyItem(R.drawable.sample_6, "Luna", "3"));
        data.add(new MyItem(R.drawable.sample_7, "Bob", "2"));

        //어댑터 생성
        adapter = new MyAdapter(this, R.layout.item, data);

        //어댑터 연결
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem)adapter.getItem(position)).nName;
                Toast.makeText(MainActivity.this, name + " selected",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
}
