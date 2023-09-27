package com.example.san;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.san.MyUtils.rotateAtRelativeAngle;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.Toast;

import static com.example.san.MyUtils.concludeSpeak;
import static com.example.san.MyUtils.sleepy;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.beans.Order;
import com.sanbot.opensdk.beans.UserInfo;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.LED;
import com.sanbot.opensdk.function.beans.headmotion.LocateAbsoluteAngleHeadMotion;
import com.sanbot.opensdk.function.beans.headmotion.RelativeAngleHeadMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.beans.wing.AbsoluteAngleWingMotion;
import com.sanbot.opensdk.function.beans.wing.NoAngleWingMotion;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.HeadMotionManager;
import com.sanbot.opensdk.function.unit.ModularMotionManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.WingMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.hardware.GyroscopeListener;
import com.sanbot.opensdk.function.unit.interfaces.hardware.PIRListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  MotionActivity extends TopBaseActivity {
    private final static String TAG = "DIL-SPLASH";

    public static boolean busy = false;

    @BindView(R.id.button1)
    Button btn_headup;

    @BindView(R.id.button)
    Button btn_handdown;
    @BindView(R.id.button3)
    Button btn_handsup;

    @BindView(R.id.button2)
    Button btn_wanderOn;

    @BindView(R.id.button4)
    Button btn_wanderOff;

    @BindView(R.id.button6)
    Button btn_handshake;

    @BindView(R.id.btnforward)
    Button btn_forward;

    @BindView(R.id.btnfstop)
    Button btn_fstop;

    @BindView(R.id.btnleft)
    Button btnleft;

    @BindView(R.id.btnright)
    Button btnright;

    @BindView(R.id.btndforward)
    Button btndforward;

    @BindView(R.id.btnback)
    Button btnback;
    private ModularMotionManager modularMotionManager; //wander
    private HardWareManager hardWareManager;
    private WheelMotionManager wheelMotionManager;
    private SpeechManager speechManager; //voice, speechRec
    private SystemManager systemManager; //emotions
    private HeadMotionManager headMotionManager;    //head movements
    private WingMotionManager wingMotionManager;

    //head motion
    LocateAbsoluteAngleHeadMotion locateAbsoluteAngleHeadMotion = new LocateAbsoluteAngleHeadMotion(
            LocateAbsoluteAngleHeadMotion.ACTION_VERTICAL_LOCK,90,30
    );
    RelativeAngleHeadMotion relativeHeadMotionDOWN = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_DOWN, 30);

    NoAngleWingMotion noAngleWingMotionDOWN = new NoAngleWingMotion(NoAngleWingMotion.PART_RIGHT, 5, NoAngleWingMotion.ACTION_DOWN);

    NoAngleWingMotion noAngleWingMotionUP = new NoAngleWingMotion(NoAngleWingMotion.PART_RIGHT, 5, NoAngleWingMotion.ACTION_UP);

    NoAngleWheelMotion noAngleWheelMotion= new NoAngleWheelMotion(NoAngleWheelMotion.ACTION_FORWARD, 5);

    NoAngleWheelMotion noAngleWheelMotionleft=new NoAngleWheelMotion(NoAngleWheelMotion.ACTION_LEFT_FORWARD, 5 );
    NoAngleWheelMotion noAngleWheelMotionRight=new NoAngleWheelMotion(NoAngleWheelMotion.ACTION_RIGHT_FORWARD, 5);

    RelativeAngleWheelMotion relativeAngleWheelMotionforward= new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT,5,300);

    DistanceWheelMotion distanceWheelMotionforward= new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN,5,600);
    NoAngleWheelMotion noAngleWheelMotionstop=new NoAngleWheelMotion(NoAngleWheelMotion.ACTION_STOP, 5,300);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            register(ChoiceActivity.class);
            //screen always on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_motion);
            ButterKnife.bind(this);
            speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
            systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
            modularMotionManager = (ModularMotionManager) getUnitManager(FuncConstant.MODULARMOTION_MANAGER);
            hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
            wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
            headMotionManager = (HeadMotionManager) getUnitManager(FuncConstant.HEADMOTION_MANAGER);
            wingMotionManager = (WingMotionManager) getUnitManager(FuncConstant.WINGMOTION_MANAGER);
            
            //float button of the system
            systemManager.switchFloatBar(true, getClass().getName());

            //check app permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{INTERNET}, 12);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 12);

            }
            //LOAD handshakes stats
            MySettings.initializeXML();
            MySettings.loadHandshakes();

            speechManager.startSpeak("welcome I am sanbot", MySettings.getSpeakDefaultOption());
            concludeSpeak(speechManager);

            btn_handsup.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button3)
                public void onClick(View view) {

                    handsup();
                }
            });

            //initialize body
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //hands down
                    AbsoluteAngleWingMotion absoluteAngleWingMotion = new AbsoluteAngleWingMotion(AbsoluteAngleWingMotion.PART_BOTH, 8, 180);
                    wingMotionManager.doAbsoluteAngleMotion(absoluteAngleWingMotion);
                    //head up
                    headMotionManager.doAbsoluteLocateMotion(locateAbsoluteAngleHeadMotion);
                    //initially sets the wander to on
                }
            }, 1000);


            btn_wanderOn.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button2)
                public void onClick(View view) {

                    Intent intent = new Intent(MotionActivity.this, ChoiceActivity.class);
                    startActivity(intent);
                    finish();

                }
            });


            btn_wanderOn.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button2)
                public void onClick(View view) {

                   wanderOnNow();
                }
            });


            btn_wanderOff.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button4)
                public void onClick(View view) {

                    wanderOffNow();
                }
            });


            btn_handdown.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button1)
                public void onClick(View view) {

                   handsdown();
                }
            });

            btn_handshake.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button6)
                public void onClick(View view) {

                    Intent intent = new Intent(MotionActivity.this, HandShake.class);
                    startActivity(intent);
                    finish();
                }
            });


            btnback.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button6)
                public void onClick(View view) {

                    Intent intent = new Intent(MotionActivity.this, ChoiceActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btn_forward.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btnforward)
                public void onClick(View view) {
                    goforward();
                }
            });

            btn_fstop.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btnfstop)
                public void onClick(View view) {
                    fstop();
                }
            });

            btnleft.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btnfstop)
                public void onClick(View view) {
                    left();
                }
            });

            btnright.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btnfstop)
                public void onClick(View view) {
                    right();
                }
            });

            btndforward.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btndforward)
                public void onClick(View view) {
                    pirsensor();
                }
            });


        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onMainServiceConnected() {

    }
    public void wanderOnNow() {
        if (!busy) {
            MySettings.setWanderAllowed(true);
            Toast.makeText(MotionActivity.this, "Wander " + MySettings.isWanderAllowed()+" now", Toast.LENGTH_SHORT).show();
            modularMotionManager.switchWander(MySettings.isWanderAllowed());
            Log.i(TAG, "Wander " + MySettings.isWanderAllowed() + " now");
        }
    }
    public void wanderOffNow() {
        MySettings.setWanderAllowed(false);
        Toast.makeText(MotionActivity.this, "Wander off now", Toast.LENGTH_SHORT).show();
        modularMotionManager.switchWander(false);
        Log.i(TAG, "Wander forced off now");
    }


    public void handsup(){
        speechManager.startSpeak("Hands Up", MySettings.getSpeakDefaultOption());
        concludeSpeak(speechManager);
        //hands up
        wingMotionManager.doNoAngleMotion(noAngleWingMotionUP);
        sleepy(0.5);
    }


    public void handsdown(){
        speechManager.startSpeak("hands Down", MySettings.getSpeakDefaultOption());
        concludeSpeak(speechManager);
        wingMotionManager.doNoAngleMotion(noAngleWingMotionDOWN);
        sleepy(0.5);
        //initially sets the wander to on
    }

    public void goforward(){
        wheelMotionManager.doNoAngleMotion(noAngleWheelMotion);
        sleepy(0.5);
    }

    public void fstop(){
        wheelMotionManager.doNoAngleMotion(noAngleWheelMotionstop);
        sleepy(0.5);
    }

    public void distanceforward(){
        wheelMotionManager.doDistanceMotion(distanceWheelMotionforward);
        sleepy(0.5);
    }


    public void right(){
        wheelMotionManager.doNoAngleMotion(noAngleWheelMotionRight);
        sleepy(0.5);
    }

    public void left(){
        wheelMotionManager.doNoAngleMotion(noAngleWheelMotionleft);
        sleepy(0.5);
    }
    public void wheelforward(){
        wheelMotionManager.toString();
    }

public void gyroscopeSensor() {
    hardWareManager.setOnHareWareListener(new GyroscopeListener() {
        @Override
        public void gyroscopeCheckResult(boolean b, boolean b1) {

        }

        @Override
        public void gyroscopeData(float v, float v1, float v2) {

        }
    });

}

    public void pirsensor() {
        hardWareManager.setOnHareWareListener(new PIRListener() {
            @Override
            public void onPIRCheckResult(boolean b, int i) {
                systemManager.showEmotion(EmotionsType.QUESTION);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_YELLOW));
                speechManager.startSpeak("You are front of me, I am following you", MySettings.getSpeakDefaultOption());
                concludeSpeak(speechManager);
                if(i<=50){

                    goforward();

                    }
                }

        });

    }


}