package com.unaj.gabbo.picmoments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//AIzaSyAVrbNqY_7ayF69iY7cs74bYjL2eJcRodY maps key


/**
 * Created by Gabbo on 8/11/2017.
 */

public class MapActivity extends  FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();
        String coordenadas = intent.getStringExtra("location");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            LatLng sydney = new LatLng(0, 0);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
