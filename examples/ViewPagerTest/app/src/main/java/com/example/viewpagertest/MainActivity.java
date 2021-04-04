package com.example.viewpagertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 vpPager = findViewById(R.id.vpPager);
        final FragmentStateAdapter adapter = new PagerAdapter(this);
        vpPager.setAdapter(adapter);



        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);  //scrollable tab indicator

        // create a TabLayoutMediator to link the TabLayout to the ViewPager2, and attach it
        new TabLayoutMediator(tabLayout, vpPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(((PagerAdapter)adapter).getPageTitle(position));
                    }
                }
        ).attach();


        vpPager.setCurrentItem(1);  // SecondFragment를 첫 화면에 표시되도록 설정

        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
