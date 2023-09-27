package com.example.san;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.san.MyUtils.rotateAtRelativeAngle;
import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.LED;
import com.sanbot.opensdk.function.beans.StreamOption;
import com.sanbot.opensdk.function.beans.headmotion.LocateAbsoluteAngleHeadMotion;
import com.sanbot.opensdk.function.beans.headmotion.RelativeAngleHeadMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.sanbot.opensdk.function.beans.wing.AbsoluteAngleWingMotion;
import com.sanbot.opensdk.function.unit.HDCameraManager;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.HeadMotionManager;
import com.sanbot.opensdk.function.unit.ModularMotionManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.WingMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.hardware.PIRListener;


import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends TopBaseActivity  {
    private final static String TAG = "DIL-BAS";

    @BindView(R.id.buttonarro)
    Button buttonarro;
    @BindView(R.id.buttonsurp)
    Button buttonsurp;
    @BindView(R.id.buttonwhis)
    Button buttonwhis;
    @BindView(R.id.buttonlau)
    Button buttonlau;
    @BindView(R.id.buttongb)
    Button buttongb;
    @BindView(R.id.buttonshy)
    Button buttonshy;
    @BindView(R.id.buttonsweat)
    Button buttonsweat;
    @BindView(R.id.buttonsnicker)
    Button buttonsnicker;
    @BindView(R.id.buttonpick)
    Button buttonpick;
    @BindView(R.id.buttoncry)
    Button buttoncry;
    @BindView(R.id.buttonab)
    Button buttonab;
    @BindView(R.id.buttonang)
    Button buttonang;
    @BindView(R.id.buttonki)
    Button buttonki;
    @BindView(R.id.buttonsleep)
    Button buttonsleep;
    @BindView(R.id.buttonsmile)
    Button buttonsmile;
    @BindView(R.id.buttongri)
    Button buttongri;
    @BindView(R.id.buttonques)
    Button buttonques;
    @BindView(R.id.buttonfaint)
    Button buttonfaint;
    @BindView(R.id.buttonprise)
    Button buttonprise;
    @BindView(R.id.buttonnormal)
    Button buttonnormal;

    @BindView(R.id.btnback)
    Button btnback;

    //robot managers

    private HDCameraManager hdCameraManager; //video, faceRec
    private HeadMotionManager headMotionManager;    //head movements
    private WingMotionManager wingMotionManager;    //hands movements
    private SystemManager systemManager; //emotions
    private HardWareManager hardWareManager; //leds //touch sensors //voice locate //gyroscope
    private ModularMotionManager modularMotionManager; //wander
    private WheelMotionManager wheelMotionManager;
    //head motion
    LocateAbsoluteAngleHeadMotion locateAbsoluteAngleHeadMotion = new LocateAbsoluteAngleHeadMotion(
            LocateAbsoluteAngleHeadMotion.ACTION_VERTICAL_LOCK,90,30
    );
    RelativeAngleHeadMotion relativeHeadMotionDOWN = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_DOWN, 30);

    private List<Integer> handleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            register(BaseActivity.class);
            //screen always on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base);
            ButterKnife.bind(this);
            hdCameraManager = (HDCameraManager) getUnitManager(FuncConstant.HDCAMERA_MANAGER);
            systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
            hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
            wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);

            //float button of the system
            systemManager.switchFloatBar(true, getClass().getName());







        }catch(Exception e){
            Log.e(TAG, "Error in onCreate: " + e.getMessage());

        }
        //turnOnLights();
        //LOAD handshakes stats
        MySettings.initializeXML();
        MySettings.loadHandshakes();

        //initialize speak
        MySettings.initializeSpeak();
        buttonarro.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonarro)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.ARROGANCE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_GREEN));

            }
        });
        buttonsurp.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonsurp)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SURPRISE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));

            }
        });
        buttonwhis.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonwhis)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.WHISTLE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_BLUE));
            }
        });

        buttonlau.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonlau)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.LAUGHTER);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PINK));
            }
        });

        buttongb.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttongb)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.GOODBYE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));
            }
        });

        buttonshy.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonshy)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SHY);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_RED));
            }
        });

        buttonsweat.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonsweat)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SWEAT);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));
            }
        });

        buttonsnicker.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonsnicker)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SNICKER);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PURPLE));
            }
        });

        buttonpick.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonpick)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.PICKNOSE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_GREEN));
            }
        });

        buttoncry.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttoncry)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.CRY);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));
            }
        });

        buttonab.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonab)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.ABUSE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_GREEN));
            }
        });

        buttonang.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonang)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.ANGRY);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_RED));
            }
        });

        buttonki.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonki)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.KISS);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_RED));
            }
        });

        buttonsleep.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonsleep)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SLEEP);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));
            }
        });

        buttonsmile.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonsmile)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.SMILE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PURPLE));
            }
        });

        buttongri.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttongri)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.GRIEVANCE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.btnback)
            public void onClick(View view) {

                Intent intent = new Intent(BaseActivity.this, ChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonques.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonques)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.QUESTION);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_YELLOW));
            }
        });

        buttonfaint.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonfaint)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.FAINT);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_YELLOW));
            }
        });

        buttonprise.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonprise)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.PRISE);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PURPLE));
            }
        });

        buttonnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonnormal)
            public void onClick(View view) {
                systemManager.showEmotion(EmotionsType.NORMAL);
                hardWareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PINK));
            }
        });






    }

    @Override
    protected void onMainServiceConnected() {

    }







}