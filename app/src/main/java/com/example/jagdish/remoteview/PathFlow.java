package com.example.jagdish.remoteview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.ListIterator;

public class PathFlow extends AppCompatActivity implements OnMapReadyCallback {

    private View mDecorView;
    private ImageButton back;
    AVLoadingIndicatorView loader;

    WebView web;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    FusedTracker ft;

    private ImageButton tracking, clear, /*current_loc,*/ headcount;
    private CardView circularMap, circularVideo;
    private CardView.LayoutParams largeCadLayout, smallCadLayout;
    private float radius;

    PolylineOptions polyLO;
    List latlngarray;
    List pathDetail;
    Polyline pathFused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_flow);

        //------------------------------------------------------------------Screen Rendering section
        loader = (AVLoadingIndicatorView) findViewById(R.id.laoder);
        /*loader.smoothToShow();*/
        //------------------------------------------------------------Screen Rendering section ended

        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,true);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        //--------------------------------------------------------------------initialization section
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        back = (ImageButton)findViewById(R.id.back);
        tracking = (ImageButton) findViewById(R.id.startTracking);
        clear = (ImageButton) findViewById(R.id.clearMap);
        /*current_loc = (ImageButton) findViewById(R.id.currentloc);*/
        headcount = (ImageButton) findViewById(R.id.headcount);
        circularMap = (CardView) findViewById(R.id.circularmap);
        circularVideo = (CardView) findViewById(R.id.circularvideo);
        smallCadLayout = (CardView.LayoutParams) circularVideo.getLayoutParams();
        largeCadLayout = (CardView.LayoutParams) circularMap.getLayoutParams();
        web = new WebView(getApplicationContext());
        radius = circularVideo.getRadius();
        circularVideo.bringToFront();
        circularVideo.setPreventCornerOverlap(false);
        final FrameLayout frame = (FrameLayout)findViewById(R.id.frame1);

        ft = new FusedTracker(this);
        //--------------------------------------------------------------initialization section ended
        Log.e("tracker","tracker is start");
        //===========================// Video Streaming area //============================================//
        String video = "<body style=\"margin:0px; padding:0px; width:100%; height:100%;\">" +
                "<div  style=\" width:inherit; height:inherit;\"> " +
                "<img  style=\"-webkit-user-select: none; width:inherit; height:inherit;\"  src=\"http://www.hdwallpapers.in/walls/pink_cosmos_flowers-wide.jpg\"/>"+
                "</div>"+
                "</body>";

        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.setBackgroundColor(Color.TRANSPARENT);
        //web.getSettings().setLoadWithOverviewMode(true);
        //web.getSettings().setUseWideViewPort(true);
        // web.getSettings().setJavaScriptEnabled(true);
        web.loadData(video,"text/html","UTF-8");
        frame.addView(web); // <--- Key line
        //===========================//End Video Streaming area //============================================//

        //-------------------------------------------------------------------Button listener started
        //Clear Button listener
        if(clear != null) {
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (mMap != null) {
                    mMap.clear();
                }
                pathDetail = null;
                pathFused = null;
                latlngarray = null;

                if(ft != null) {
                    ft.setLatlngarray();
                    ft.setPathDetail();
                }
                }
            });
        } /*clear != null*/

        /*if(current_loc != null) {
            current_loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ft != null) {
                        if (ft.getServicesStart()) {
                            Location loc = mMap.getMyLocation();
                            if(loc != null) {
                                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            } else {
                                Toast.makeText(getApplicationContext(),"Waiting for loaction update",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ft.startLocationUpdates();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Tracker is not initialize",Toast.LENGTH_SHORT).show();
                        ft = new FusedTracker(getApplicationContext());
                    }
                }
            });
        }*/ /*current_loc != null*/

        /*Start tracking button listener
        * To stop tracking*/
        if(tracking != null) {
            tracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (ft.getServicesStart()) {
                    if (ft.getCapturePath()) {
                        Toast.makeText(getApplicationContext(), "Tracking stopped", Toast.LENGTH_SHORT).show();
                        ft.capturePath(false, null);
                        createPathFused();
                        pathDetail = ft.getPathDetail();

                        tracking.setImageResource(R.drawable.path_flow_start);
                        /*stopTracking.setVisibility(View.GONE);
                        startTracking.setVisibility(View.VISIBLE);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Starting Tracking", Toast.LENGTH_SHORT).show();
                        ft.capturePath(true, mMap);

                        tracking.setImageResource(R.drawable.path_flow_stop);
                        /*stopTracking.setVisibility(View.VISIBLE);
                        startTracking.setVisibility(View.GONE);*/
                    } /*ft.getCapturePath()*/
                } else {
                    ft.startLocationUpdates();
                    Toast.makeText(getApplicationContext(), "Services is not Started", Toast.LENGTH_SHORT).show();
                } /*ft.getServicesStart()*/
                }
            });
        } /*startTracking != null*/

        /*//Stop tracking button listener
        if(stopTracking != null) {
            stopTracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (ft.getServicesStart()) {
                    if (ft.getCapturePath()) {
                        Toast.makeText(getApplicationContext(), "Tracking stopped", Toast.LENGTH_SHORT).show();
                        ft.capturePath(false, null);
                        createPathFused();
                        pathDetail = ft.getPathDetail();
                        stopTracking.setVisibility(View.GONE);
                        startTracking.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Tracking already stopped", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ft.stopLocationUpdates();
                    Toast.makeText(getApplicationContext(), "Services is not Started", Toast.LENGTH_SHORT).show();
                }
                }
            });
        } *//*stopTracking != null*/

        if(headcount != null) {
            headcount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(headcount.getTag().toString().equals("start")) {
                    headcount.setImageResource(R.drawable.stopheadcount);
                    headcount.setTag(new String("stop"));
                } else if(headcount.getTag().toString().equals("stop")) {
                    headcount.setImageResource(R.drawable.headcount);
                    headcount.setTag(new String("start"));
                }
                }
            });
        } /*headcount != null*/

        //---------------------------------------------------------------Back to Main Activity start
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setting = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(setting);
                    finish();
                }
            });
        } /*back != null*/
        //-----------------------------------------------------------------back to main activity end
        //---------------------------------------------------------------------Button listener ended
        Log.e("listener","Listener are set");

        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideSystemUI();
                            }
                        }, 1500);
                    }
                });
        mDecorView.setKeepScreenOn(true);

        hideSystemUI();
        /*loader.smoothToHide();*/
        Log.e("Act","Activity is loaded");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        loader.smoothToShow();
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Location loc = location;
                if(loc != null) {
                    LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    /*Log.e("locationchange","loo != null");*/
                } else {
                    Toast.makeText(getApplicationContext(),"Waiting for loaction update",Toast.LENGTH_SHORT).show();
                    /*Log.e("locationchange","loo == null");*/
                } /*loc != null*/
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(circularMap != null) {
                    /*Log.e("Cardview", "CardView listener is called");*/
                    if(circularMap.getTag().toString().equals("small")) {
                        //Map View
                        circularMap.setLayoutParams(largeCadLayout);
                        circularMap.setRadius(0);
                        circularMap.setTag(new String("large"));

                        //video View
                        circularVideo.setLayoutParams(smallCadLayout);
                        circularVideo.setRadius(radius);
                        /*web.setLayoutParams(smallCadLayout);*/
                        circularVideo.setTag(new String("small"));
                        circularVideo.bringToFront();
                    } else if(circularMap.getTag().toString().equals("large")) {
                        //Map view
                        circularMap.setLayoutParams(smallCadLayout);
                        circularMap.setRadius(radius);
                        circularMap.setTag(new String("small"));
                        circularMap.bringToFront();

                        //Video view
                        /*web.setLayoutParams(largeCadLayout);*/
                        circularVideo.setLayoutParams(largeCadLayout);
                        circularVideo.setRadius(0);
                        circularVideo.setTag(new String("large"));
                    }
                }
            }
        });

        // Add a marker in Sydney and move the camera
        Location loc = mMap.getMyLocation();
        if(loc != null) {
            LatLng cur_loc = new LatLng(loc.getLatitude(), loc.getLongitude());
            /*mMap.addMarker(new MarkerOptions().position(vgec).title("Marker in vgec"));*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur_loc, 16));
        } /*loc != null*/
        loader.smoothToHide();
    }

    public void createPathFused() {
        mMap.clear();
        latlngarray = ft.getLatlngarray();
        if(latlngarray.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Path is not recorded",Toast.LENGTH_SHORT).show();
            return;
        }

        ListIterator lla = latlngarray.listIterator();
        while(lla.hasNext()) {
            mMap.addMarker(new MarkerOptions().position((LatLng) lla.next()).title("ifused:"+lla.nextIndex())).setAlpha((float) 0.3);
        }

        polyLO = new PolylineOptions()
                .addAll((Iterable<LatLng>) latlngarray)
                .width(5)
                .color(Color.BLUE);
        pathFused = mMap.addPolyline(polyLO);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            hideSystemUI();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
            );
        } /*Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB*/
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ft.stopLocationUpdates();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ft.stopLocationUpdates();
        Intent setting = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(setting);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ft.stopLocationUpdates();
        finish();
    }
}
