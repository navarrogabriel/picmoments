package com.unaj.gabbo.picmoments.services;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.unaj.gabbo.picmoments.MomentListActivity;

/**
 * Created by Gabbo on 17/2/2018.
 */

public class CheckLocationService extends AppCompatActivity {

    public boolean locationIsOkay (){
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {
            return false;
        }

        return true;
    }


}
