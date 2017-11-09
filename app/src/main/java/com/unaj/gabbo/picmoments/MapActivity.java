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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            Intent intent = getIntent();
            String coordenadas = intent.getStringExtra("location");
            String latitudeString = getStringLatitude (coordenadas);
            String longString = getStringLong (coordenadas);

            double lat = Double.parseDouble(latitudeString);
            double lon = Double.parseDouble(longString);

            LatLng position = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(position).title("Moment"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 6.0f));
    }

    private String getStringLong(String coordenadas) {
        int index = coordenadas.indexOf(",");
        String toReturn = coordenadas.substring(index+1, coordenadas.length());
        return toReturn;
    }

    private String getStringLatitude(String coordenadas) {
        int index = coordenadas.indexOf(",");
        String toReturn = coordenadas.substring(0, index);
        return toReturn;
    }
}
