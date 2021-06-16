package com.afrobiz.afrobizfind.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;

public class StreetViewActivity extends AppCompatActivity {

//Define the LatLng value we’ll be using for the paranorma’s initial camera position//
double lat;
    double longi;
    private  LatLng LONDON = new LatLng(51.503324, -0.119543);
    private StreetViewPanoramaView mStreetViewPanoramaView;
    private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            longi = (double) b.get("long");
            lat = (double) b.get("lat");
            Toast.makeText(getApplicationContext(), "lat"+lat +"  long "+longi, Toast.LENGTH_LONG).show();

          LONDON = new LatLng(lat, longi);
        }
//Configure the panorama by passing in a StreetViewPanoramaOptions object//

        StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        if (savedInstanceState == null) {

//Set the panorama’s location//

            options.position(LONDON);
        }

        mStreetViewPanoramaView = new StreetViewPanoramaView(this, options);
        addContentView(mStreetViewPanoramaView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Bundle mStreetViewBundle = null;
        if (savedInstanceState != null) {
            mStreetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
        }
        mStreetViewPanoramaView.onCreate(mStreetViewBundle);
    }

}
