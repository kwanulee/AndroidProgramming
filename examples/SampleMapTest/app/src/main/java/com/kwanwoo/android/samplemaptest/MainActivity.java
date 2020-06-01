package com.kwanwoo.android.samplemaptest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mGoogleMap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btn1 = (Button)findViewById(R.id.subway1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleMap != null) {
                    LatLng location = new LatLng(37.5882827, 127.006390);
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title("한성대입구역").
                                    alpha(0.8f).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).
                                    snippet("4호선")
                    );
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                }
            }
        });

        Button btn2 = (Button)findViewById(R.id.subway2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleMap != null) {
                    LatLng location2 = new LatLng(37.5793652, 127.015292);
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location2).
                                    title("창신역").
                                    alpha(0.8f).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).
                                    snippet("6호선")
                    );
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2,15));
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng hansung = new LatLng(37.5817891, 127.009854);
        googleMap.addMarker(
                new MarkerOptions().
                        position(hansung).
                        title("한성대학교"));

        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung,15));

        mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getTitle().equals("한성대입구역")) {
                Toast.makeText(getApplicationContext(),"한성대입구역을 선택하셨습니다", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
