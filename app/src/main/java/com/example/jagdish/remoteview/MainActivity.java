package com.example.jagdish.remoteview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;

import static com.example.jagdish.remoteview.R.string.throttle;


public class MainActivity extends AppCompatActivity {

    /*
    private  MediaPlayer mediaPlayer;
    private  SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private ImageView video;
    String vidurl="http://cdn-fms.rbs.com.br/vod/hls_sample1_manifest.m3u8";
    */
    /*SegmentedGroup segmented2;*/
    /*RadioButton radioButton1,radioButton2,radioButton3;*/
    private View mDecorView;
    HttpClient httpClient;
    String url,arm,disarm,thrott,yaw,rp;
    int mythrottle = 1100;
    Joystick joy;
    /*SeekBar sb;*/
    /*TextView tv,angleView;*/
    /*SeekBar seekbar_Yaw;*/
    TextView status;

    ImageButton headcount;
    /*AVLoadingIndicatorView loader;*/

    Boolean arm_status = false;
    Boolean radio1_throttle_min=true;
    Boolean radio2_throttle_min=false;
    Boolean radio3_throttle_min=false;
    Boolean radio1_throttle_max=false;
    Boolean radio2_throttle_max=false;
    Boolean radio3_throttle_max=false;

    GifDrawable gifDrawable;
    String mydebug="MainActivity";

    private String meunItem[] = {"Path Flow","Object Flow","Photography","Profile"};
    BoomMenuButton bmb;

    //server myserver = new server();
    server myserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*loader = (AVLoadingIndicatorView) findViewById(R.id.laoder);
        loader.smoothToShow();*/
        /*
        video = (ImageView)findViewById(R.id.videoLink);
        surfaceView =(SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        */
        headcount = (ImageButton) findViewById(R.id.headcount);

        //Menu Control
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setBackgroundColor(Color.TRANSPARENT);
        bmb.setNormalColor(Color.TRANSPARENT);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        bmb.setDotRadius(Util.dp2px(6));
        bmb.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.Bottom);
        bmb.setBoomEnum(BoomEnum.HORIZONTAL_THROW_2);
        bmb.setShowRotateEaseEnum(EaseEnum.EaseOutBack);
        bmb.setBackPressListened(false);
        bmb.setAlpha((float)0.7);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.GRAY);
            bmb.setButtonBottomMargin(305);
        }
        if(Build.VERSION.SDK_INT < 19) {
            bmb.setButtonTopMargin(305);
            Log.e("Build version", String.valueOf((Build.VERSION.SDK_INT)));
        }
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.drone)
                    .normalText(meunItem[i])
                    .rotateImage(true)
                    .rippleEffect(true)
                    .highlightedColor(Color.argb(230,255,195,184))
                    .normalColor(Color.argb(230,255,120,105))
                    .pieceColor(Color.argb(230,255,105,97));
            bmb.addBuilder(builder);
        }

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                // If you have implement listeners for boom-buttons in builders,
                // then you shouldn't add any listener here for duplicate callbacks.
                if(index == 0) {
                    Intent act = new Intent(getApplicationContext(), PathFlow.class);
                    startActivity(act);
                }

                if(index == 1) {
                    Intent act = new Intent(getApplicationContext(), ObjectFlow.class);
                    startActivity(act);
                }

                if(index == 2) {
                    Intent act = new Intent(getApplicationContext(), Photography.class);
                    startActivity(act);
                }

                if(index == 3) {
                    /*Intent act = new Intent(getApplicationContext(), PathFlow.class);
                    startActivity(act);*/
                    Toast.makeText(getApplication(),"Activity is not set yet",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBackgroundClick() {
                Toast.makeText(getApplicationContext(),"Click background!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBoomWillHide() {
                //Toast.makeText(getApplicationContext(),"Will RE-BOOM!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBoomDidHide() {
                //Toast.makeText(getApplicationContext(),"Did RE-BOOM!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBoomWillShow() {
                //Toast.makeText(getApplicationContext(),"Will BOOM!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBoomDidShow() {
                //Toast.makeText(getApplicationContext(),"Did BOOM!!!",Toast.LENGTH_SHORT).show();
            }
        });
        //end of menu control

//===========================// mixed area //=====================================================//
        httpClient = new DefaultHttpClient();

        url = getResources().getString(R.string.url);
        thrott = getResources().getString(throttle);
        arm = getResources().getString(R.string.arm);
        disarm = getResources().getString(R.string.disarm);
        rp = getResources().getString(R.string.rp);
        yaw = getResources().getString(R.string.yaw);
        /*angleView = (TextView)findViewById(R.id.text2);*/
        /*tv= (TextView)findViewById(R.id.text1);*/
        joy = (Joystick)findViewById(R.id.joystick);
        status=(TextView)findViewById(R.id.flighttime);

//===========================// Video Streaming area //============================================//

        String video = "<body style=\"margin:0px; padding:0px; width:100%; height:100%;\">" +
                "<div  style=\" width:inherit; height:inherit;\"> " +
                "<img  style=\"-webkit-user-select: none; width:inherit; height:inherit;\"  src=\"http://192.168.1.4:8081/\"/>"+
                "</div>"+
                "</body>";

        final FrameLayout frame=(FrameLayout)findViewById(R.id.frame1);
        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);
        WebView web = new WebView(getApplicationContext());
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        web.setBackgroundColor(Color.BLACK);

        web.setLayoutParams(ll);

        //web.getSettings().setLoadWithOverviewMode(true);
        //web.getSettings().setUseWideViewPort(true);
        // web.getSettings().setJavaScriptEnabled(true);
        web.loadData(video,"text/html","UTF-8");
        frame.addView(web); // <--- Key line


       /* WebView w = (WebView) findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
//w.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient());
//w.loadUrl(vidAddress);
        w.loadData("<img style=\"-webkit-user-select: none; display: block; margin:auto;\" src=\"http://192.168.1.4:8081/\">","text/html","UTF-8");
*/

//=================================// Throttle Area now//===========================================//

        /*segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);*/

        /*radioButton1 = (RadioButton) findViewById(R.id.button21);

        radioButton2 = (RadioButton) findViewById(R.id.button22);

        radioButton3 = (RadioButton) findViewById(R.id.button23);*/

        /*radioButton2.setEnabled(true);
        radioButton2.setEnabled(false);
        radioButton3.setEnabled(false);*/
        /*sb =(SeekBar) findViewById(R.id.seekBar2);
        sb.setEnabled(false);*/


        /*Throttle stick control*/
        /*sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {*/
                // Toast.makeText(getApplicationContext(),i + " ",Toast.LENGTH_SHORT).show();


//                Log.e(mydebug," isCancelled3:" +myserver.isCancelled());
//                myserver.cancel(true);
//                Log.e(mydebug," isCancelled4:" +myserver.isCancelled());


                /*Radio button functionlity*/
                /*if(radioButton1.isChecked())
                {
                    Log.e(mydebug,"Throttle 1 :" +mythrottle );
                    radioButton2.setEnabled(false);
                    radioButton3.setEnabled(false);
                    mythrottle = 1100 + i * 5;
                    tv.setText("pitch:" + mythrottle);

                    if (mythrottle > 1580 && mythrottle <=1600 ) {
                        radioButton2.setEnabled(true);
                        Log.e("radio2_throttle_min" ,""+radio2_throttle_min);

                    }
                }


                if(radioButton2.isChecked()) {

                    Log.e(mydebug,"Throttle 2 :" +mythrottle );


                    radioButton1.setEnabled(false);

                    radioButton3.setEnabled(false);

                    mythrottle = 1600 + i * 2;
                    tv.setText("pitch:" + mythrottle);

                    //  For Step 3 enable
                    if (mythrottle > 1790 && mythrottle <= 1800){
                        radioButton3.setEnabled(true);
                        Log.e("radio3_throttle_min" ,""+radio3_throttle_min);

                    }// For Step 1  disable
                    if (mythrottle >= 1600 && mythrottle <=1620 ) {
                        // sb.setProgress(1599);
                        radioButton1.setEnabled(true);
                        Log.e("radio1_throttle_max" ,""+radio1_throttle_max);

                    }
                }



                if(radioButton3.isChecked()) {
                    Log.e(mydebug,"Throttle 3 :" +mythrottle );
                    radioButton2.setEnabled(false);
                    radioButton1.setEnabled(false);
                    mythrottle = 1800 + i;
                    tv.setText("pitch:" + mythrottle);
                    // For Step 2  enable
                    if (mythrottle >= 1800 && mythrottle <=1810 ) {
                        // sb.setProgress(1799);
                        radioButton2.setEnabled(true);
                    }
                }*/
                //Log.e(mydebug," isCancelled1:" +myserver.isCancelled());
//                if (myserver.isCancelled()){
//                    Log.e(mydebug," isCancelled2:" +myserver.isCancelled());
//                    myserver.execute(url + thrott + mythrottle);
//                }
//                if (myserver.isCancelled()){
//                myserver.execute(url + thrott + mythrottle);
//                }
//                else{
//
//                    myserver.execute(url + thrott + mythrottle);
//                }


            /*}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });*/
        //==========================//Yaw area now//====================================//

        /*Yaw stick control*/
        /*seekbar_Yaw =(SeekBar)findViewById(R.id.customSeekBar);
        seekbar_Yaw.setProgress(50);
        seekbar_Yaw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                tv.setText("pitch:" + (i-50) +"");
                int yaw_value = (int)(i-50)/2;
                Log.e(mydebug,url+yaw+yaw_value);
                new server_armed_roll_yaw_pitch().execute(url+yaw+yaw_value);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //seekbar_Yaw.setProgress(50);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                seekbar_Yaw.setProgress(50);
                Log.e(mydebug,url+yaw+0);
                new server_armed_roll_yaw_pitch().execute(url+yaw+0);
            }
        });*/

//=================================================================================//
        //Joystick movepad = (Joystick)findViewById(R.id.movepad);

        final String angleNoneString = getString(R.string.angle_value_none);
        final String angleValueString = getString(R.string.angle_value);
        final String offsetNoneString = getString(R.string.offset_value_none);
        final String offsetValueString = getString(R.string.offset_value);

        final ImageView drone = (ImageView)findViewById(R.id.orientor);
        ProgressBar pb = (ProgressBar)findViewById(R.id.powerstatus);
        pb.setProgress(70);

        final GifImageButton props = (GifImageButton) findViewById(R.id.props);
        gifDrawable = (GifDrawable) props.getDrawable();
        gifDrawable.stop();
//        ImageButton down = (ImageButton)findViewById(R.id.downarrow);
//        ImageButton up = (ImageButton)findViewById(R.id.uparrow);
//        ImageButton left = (ImageButton)findViewById(R.id.leftarrow);
//        ImageButton right = (ImageButton)findViewById(R.id.rightarrow);
        /*ImageButton set = (ImageButton)findViewById(R.id.setting);*/

        //SeekBar throttle=(SeekBar)findViewById(R.id.seekBar2);

        props.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (gifDrawable.isRunning()){
                    gifDrawable.stop();
                    Log.e("disArm",url+disarm);
                   // myserver.execute(url+disarm);
                     myserver.cancel(true);
                    new server_armed_roll_yaw_pitch().execute(url+disarm);

                }else {
                    gifDrawable.start();
                    Log.e("Arm",url+arm);
                    new server_armed_roll_yaw_pitch().execute(url+arm);
                //    myserver.execute(url+arm);

//                    myserver.cancel(false);
                    myserver = (server) new server().execute();
                }
                return true;
            }
        });

        /*if (set != null) {
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setting = new Intent(getApplicationContext(),Setting.class);
                    startActivity(setting);
                }
            });
        }*/
        Log.e(mydebug,joy.getMotionConstraint().toString());

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
        
        if (joy != null) {

            joy.setJoystickListener(new JoystickListener() {

                @Override
                public void onDown() {
                    Log.e(mydebug,"down");
                }

              //  int roll = 100;
               // int oldPitch=100;
                //Boolean change = true;

                @Override
                public void onDrag(float degrees, float offset) {

                    joy.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent me) {
                            switch (me.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    status.setText("Start");
                                    Log.e(mydebug, "true");
                                    break;
                                case MotionEvent.ACTION_UP:
                                    status.setText("false");
                                    Log.e(mydebug, "Ended");
                                    break;
                            }
                            return false;
                        }
                    });
                    float pitch = 0;
                    float rot = 0;


                    if(offset >= 0) {

                        if (degrees >= 45 && degrees <= 135) {
                            pitch = (offset * (-10));
                            // drone.setRotation(pitch);

                        } else if (degrees >= -135 && degrees <= -45) {
                            pitch = (offset * (10));
                            //drone.setRotation(pitch);

                        } else if (degrees > -45 && degrees < 45) {
                            rot = (offset * 10);
                            drone.setRotation(rot);

                        } else if (degrees < -135 || degrees > 135 ) {
                            rot = (offset * (-10));
                            drone.setRotation(rot);
                        }
                    }

//                    Log.e(mydebug,"Rot " +(int)rot);
//                    Log.e(mydebug, "Roll "+ roll);
//                    Log.e(mydebug, " ");

//                    int i=1;
//                    if (rot != 0){
////                        while(true) {
////                            Log.e(mydebug,"Data Roll:" + (int)rot   +"Old Roll" +roll);
////                            if ((roll == (int)rot)){
////                                Log.e(mydebug, "change "+i);
////                                status.setText("Equal:" + rot);
////                                i++;
////                            }else {
////                                Log.e(mydebug, "Break ");
////                                break;
////                            }
////
////                        }
//                    }
//                    if (pitch != 0){
////                        while(true) {
////                            Log.e(mydebug,"Data Pitch:" + (int)pitch   +"Old Pitch" +oldPitch);
////                            if ((roll == (int)rot) || (oldPitch == (int)pitch)){
////                                Log.e(mydebug, "change "+i);
////                                status.setText("Equal:" + rot);
////                                i++;
////                            }else {
////                                Log.e(mydebug, "Break ");
////
////                                break;
////                            }
////
////                        }
//                    }

//                    if (roll < rot) {
//                        //  Log.e(mydebug, "change ");
//                        status.setText("Greater Then");
//
//                    }
//
//                    if (roll > rot){
//                       // Log.e(mydebug, " not change");
//                        status.setText("Less Then:");
//                    }
                    /*tv.setText("pitch:" + Float.toString(pitch));
                    angleView.setText("row:" + Float.toString(rot));*/
                    //  Log.e(mydebug,url+rp+(int)rot+"/"+(int)pitch+"/");
                    new server_armed_roll_yaw_pitch().execute(url+rp+(int)rot+"/"+(int)pitch+"/");
                  //  roll = (int) rot;
                   // oldPitch=(int)pitch;
                    //drone.setRotation(degrees);
                }

                @Override
                public void onUp() {
                    Log.e(mydebug,"onup");
                    /*angleView.setText(angleNoneString);
                    tv.setText(offsetNoneString);*/
                    drone.setRotation(0);
                    //  Log.e(mydebug,url+rp+0+"/"+0+"/");
                    new server_armed_roll_yaw_pitch().execute(url+rp+0+"/"+0+"/");
                }
            });
        }

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
                        }, 2500);
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
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class server extends AsyncTask<String,String,String> {
        String return_message;
        HttpResponse response;
        @Override
        protected String doInBackground(String... strings) {

            try {
                // httpClient.wait();
                while (!isCancelled()) {
                  // response = httpClient.execute(new HttpGet(strings[0]));
                    response = httpClient.execute(new HttpGet(url + thrott + mythrottle));
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    return_message = handler.handleResponse(response).trim();
                    Log.e(mydebug, "return_message :" + return_message);
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                if (return_message.contains("Arm") || return_message.contains("Disarm")) {
                    Toast.makeText(MainActivity.this, return_message.toString(), Toast.LENGTH_LONG).show();
                    if (return_message.contains("Arm")){
                        arm_status = true;
                        gifDrawable.start();
                    }else if (return_message.contains("Disarm")){
                        arm_status = false;
                        gifDrawable.stop();
                    }
                }
            }
            catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"Server Not Found",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    class server_armed_roll_yaw_pitch extends AsyncTask<String,String,String> {
        String return_message;
        HttpResponse response;
        @Override
        protected String doInBackground(String... strings) {

            // httpClient.wait();
            try {
                response = httpClient.execute(new HttpGet(strings[0]));
                // response = httpClient.execute(new HttpGet(url + thrott + arm));
                ResponseHandler<String> handler = new BasicResponseHandler();
                return_message = handler.handleResponse(response).trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(mydebug, "return_message :" + return_message);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                if (return_message.contains("Arm") || return_message.contains("Disarm")) {
                    Toast.makeText(MainActivity.this, return_message.toString(), Toast.LENGTH_LONG).show();
                    if (return_message.contains("Arm")){
                        arm_status = true;
                        gifDrawable.start();
                    }else if (return_message.contains("Disarm")){
                        arm_status = false;
                        gifDrawable.stop();
                    }
                }
            }
            catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"Server Not Found",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*Radio button control*/
    /*public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.button21:
                if (checked) {
                    sb.setEnabled(true);
                    if (radio1_throttle_min) {
                        sb.setProgress(0);
                        radio1_throttle_min = false;
                        Log.e("radio1_throttle_min", "" + radio1_throttle_min);
                    }
                    if (radio1_throttle_max) {
                        radio1_throttle_max = false;
                        Log.e("radio1_throttle_max", "" + radio1_throttle_max);

                        sb.setProgress(1600);
                    }
                }
                radio2_throttle_min = true;
                radio2_throttle_max = false;
                makeText(this, "button1", Toast.LENGTH_SHORT).show();

                break;
            case R.id.button22:
                if (checked)
                {
                    if(radio2_throttle_min){
                        sb.setProgress(0);
                        radio2_throttle_min = false;
                        Log.e("radio2_throttle_min" ,""+radio2_throttle_min);

                    }
                    if(radio2_throttle_max){
                        sb.setProgress(1800);
                        radio2_throttle_max = false;
                        Log.e("radio2_throttle_max" ,""+radio2_throttle_max);

                    }
                }
                radio3_throttle_min = true;
                radio1_throttle_max = true;
                radio3_throttle_max = false;
                //radioButton2.setEnabled(true);
                //radioButton2.setChecked(true);
                // sb.setProgress(0);
                makeText(this, "button2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button23:
                if (checked)
                {
                    if(radio3_throttle_min){
                        sb.setProgress(0);
                        radio3_throttle_min = false;
                        Log.e("radio3_throttle_min" ,""+radio3_throttle_min);

                    }

                    if(radio3_throttle_max){
                        sb.setProgress(1900);
                        radio3_throttle_max = false;
                        Log.e("radio3_throttle_max" ,""+radio3_throttle_max);


                    }
                }
                radio1_throttle_min = false;
                radio2_throttle_max = true;
                makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
        }
    }*/
}
