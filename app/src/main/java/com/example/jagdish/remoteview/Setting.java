package com.example.jagdish.remoteview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.Animation.OrderEnum;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import static android.R.color.transparent;


public class Setting extends AppCompatActivity {

    private View mDecorView;
    private ProgressBar p1, p2;
    private String meunItem[] = {"Path Flow","Object Flow","Photography","Profile"};
    /*private View v1, v2;*/
    BoomMenuButton bmb;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /*v1 = (View)findViewById(R.id.v1);
        v2 = (View)findViewById(R.id.v2);*/

        /*ArcShape shape = new ArcShape(135,90);
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        Paint shape2paint = shapeDrawable.getPaint();
        shape2paint.setColor(getResources().getColor(R.color.baseColor));
        shape2paint.setFilterBitmap(true);
        shape2paint.setAntiAlias(true);
        shape2paint.setStrokeWidth(3);
        shape2paint.setStyle(Paint.Style.STROKE);
        v1.setBackground(shapeDrawable);*/

        /*shape = new ArcShape(315,90);
        shapeDrawable = new ShapeDrawable(shape);
        shape2paint = shapeDrawable.getPaint();
        shape2paint.setColor(getResources().getColor(R.color.baseColor));
        shape2paint.setFilterBitmap(true);
        shape2paint.setAntiAlias(true);
        shape2paint.setStrokeWidth(3);
        shape2paint.setStyle(Paint.Style.STROKE);
        v2.setBackground(shapeDrawable);*/

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

        ImageButton back = (ImageButton)findViewById(R.id.back);
        p1 = (ProgressBar)findViewById(R.id.pb1);
        p2 = (ProgressBar)findViewById(R.id.pb2);
        p1.setProgress(100);
        p2.setProgress(50);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        /*android:bmb_buttonEnum="textInsideCircle"
        android:bmb_piecePlaceEnum="piecePlace_dot_9_1"
        android:bmb_buttonPlaceEnum="buttonPlace_sc_9_1"*/
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setBackgroundColor(Color.TRANSPARENT);
        bmb.setNormalColor(Color.TRANSPARENT);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        bmb.setDotRadius(Util.dp2px(6));
        /*bmb.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.BR);
        bmb.setButtonBottomMargin(60);
        bmb.setButtonRightMargin(60);*/
        bmb.setBoomEnum(BoomEnum.PARABOLA_3);
        bmb.setShowRotateEaseEnum(EaseEnum.EaseOutBack);
        bmb.setBackPressListened(false);
        bmb.setShowDuration(1000);
        bmb.setAlpha((float)0.6);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bmb.setTransitionName("Menu");
        }
        /*for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder()
                    .normalImageRes(R.drawable.cogwheel));
        }*/
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

        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent setting = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(setting);
                }
            });
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
