package com.kwanwoo.android.listview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //어댑터 생성 - createAapter1(), createAdapter2(), createAdapter3() 등으로 바꾸어 가면서 실행해 보세요
        ListAdapter adapt = createAdapter();

        //어댑터 연결
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapt);

        // ListView 기타 설정
        list.setDivider(new ColorDrawable(Color.GRAY));
        list.setDividerHeight(5);

    }

    private ListAdapter createAdapter() {
        // 데이터 원본 준비
        String[] items = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"};

        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    items);
        return adapt;
    }

    private ListAdapter createAdapter2() {

        //어댑터 준비 (리소스 배열 객체 이용, simple_list_item_1 리소스 사용
        ArrayAdapter<CharSequence> adapt
                = ArrayAdapter.createFromResource(
                    this,
                    R.array.items,
                    android.R.layout.simple_list_item_1);
        return adapt;
    }

    private ListAdapter createAdapter3() {

        // 데이터 원본 준비
        String[] items = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"};

        //어댑터 준비 (배열 객체 이용, R.layout.item(사용자 정의) 리소스 사용
        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                    this,
                    R.layout.item,
                    items);

        return adapt;
    }

    private ListAdapter createAdapter4() {
        // 데이터 원본 준비
        ArrayList<HashMap<String, String>> mlist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "Kim");
        map.put("age", "22");
        mlist.add(map);

        map = new HashMap<String, String>();
        map.put("name", "Lee");
        map.put("age", "21");

        mlist.add(map);

        map = new HashMap<String, String>();
        map.put("name", "Park");
        map.put("age", "23");

        mlist.add(map);

        //어댑터 준비 (SimpleAdapter 객체 이용, simple_list_item_2 리소스 사용
        SimpleAdapter adapt = new SimpleAdapter(
                this,
                mlist,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "age"},
                new int[]{android.R.id.text1, android.R.id.text2});

        return adapt;
    }
}
