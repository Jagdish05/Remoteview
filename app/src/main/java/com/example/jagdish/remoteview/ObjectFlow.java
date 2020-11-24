package com.example.jagdish.remoteview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class ObjectFlow extends AppCompatActivity {

    private View mDecorView;
    private ImageButton back;
    private ImageButton captureobject, flowobject;
    /*AVLoadingIndicatorView loader;*/

    private boolean objectAvailable = false;

    FrameLayout.LayoutParams ll;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_flow);

        //------------------------------------------------------------------Screen Rendering section
        /*loader = (AVLoadingIndicatorView) findViewById(R.id.laoder);
        loader.smoothToShow();*/
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
        back = (ImageButton)findViewById(R.id.back);
        captureobject = (ImageButton)findViewById(R.id.captureObject);
        flowobject = (ImageButton)findViewById(R.id.flowObject);
        /*stopflow = (ImageButton)findViewById(R.id.stopflow);
        if(stopflow != null) {
            stopflow.setVisibility(View.GONE);
        } *//*stopflow != null*/
        //--------------------------------------------------------------initialization section ended

        //-------------------------------------------------------------------Button listener section
        /*Back to main activity*/
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setting = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(setting);
                }
            });
        } /*back != null*/

        /*To capture object
        * Called when captureObject button is clicked*/
        if(captureobject != null) {
            captureobject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Capture Object", Toast.LENGTH_SHORT).show();
                    objectAvailable = true;
                }
            });
        } /*captureobject != null*/

        /*To start flowing object
        * Called when flowobject button is clicked
        * and to stop flowing object*/
        if(flowobject != null) {
            flowobject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(!objectAvailable) {
                    Toast.makeText(getApplicationContext(),"Object is not Available",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flowobject.getTag().toString().equals(new String("start"))) {
                    Toast.makeText(getApplicationContext(),"Start flowing object",Toast.LENGTH_SHORT).show();
                    /*if(stopflow != null) {
                        flowobject.setVisibility(View.GONE);
                        stopflow.setVisibility(View.VISIBLE);
                    } *//*stopflow != null*/
                    flowobject.setImageResource(R.drawable.videostop);
                    flowobject.setTag(new String("stop"));

                    if(captureobject != null) {
                        captureobject.setVisibility(View.GONE);
                    } /*captureobject != null*/
                } else if(flowobject.getTag().toString().equals(new String("stop"))) {
                    Toast.makeText(getApplicationContext(),"Stop flowing object",Toast.LENGTH_SHORT).show();
                    /*if(flowobject != null) {
                        stopflow.setVisibility(View.GONE);
                        flowobject.setVisibility(View.VISIBLE);
                    } *//**//*flowobject != null*//**//**/
                    flowobject.setImageResource(R.drawable.flowobject);
                    flowobject.setTag(new String("start"));

                    if(captureobject != null) {
                        captureobject.setVisibility(View.VISIBLE);
                    } /*captureobject != null*/
                }

                }
            });
        }/*flowobject != null*/

        /*To stop capturing of video
        * Called when videostop buttton is clicked*/
        /*if(stopflow != null) {
            stopflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Stop flowing object",Toast.LENGTH_SHORT).show();
                    if(flowobject != null) {
                        stopflow.setVisibility(View.GONE);
                        flowobject.setVisibility(View.VISIBLE);
                    } *//*flowobject != null*//*

                    if(captureobject != null) {
                        captureobject.setVisibility(View.VISIBLE);
                    } *//*captureobject != null*//*
                }
            });
        } *//*stopflow != null*/
        //-------------------------------------------------------------Button listener section ended

        //===========================// Video Streaming area //============================================//
        String video = "<body style=\"margin:0px; padding:0px; width:100%; height:100%;\">" +
                "<div  style=\" width:inherit; height:inherit;\"> " +
                "<img  style=\"-webkit-user-select: none; width:inherit; height:inherit;\"  src=\"http://192.168.1.4:8081/\"/>"+
                "</div>"+
                "</body>";

        final FrameLayout frame = (FrameLayout)findViewById(R.id.frame1);
        ll = new FrameLayout
                .LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);
        web = new WebView(getApplicationContext());
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.setBackgroundColor(Color.BLACK);
        web.setLayoutParams(ll);
        //web.getSettings().setLoadWithOverviewMode(true);
        //web.getSettings().setUseWideViewPort(true);
        // web.getSettings().setJavaScriptEnabled(true);
        web.loadData(video,"text/html","UTF-8");
        frame.addView(web); // <--- Key line
        //===========================// Video Streaming area ended //============================================//

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
        }
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
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setting = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(setting);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
