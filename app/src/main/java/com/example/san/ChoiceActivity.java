package com.example.san;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.function.beans.headmotion.LocateAbsoluteAngleHeadMotion;
import com.sanbot.opensdk.function.beans.headmotion.RelativeAngleHeadMotion;
import com.sanbot.opensdk.function.unit.HDCameraManager;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.HeadMotionManager;
import com.sanbot.opensdk.function.unit.ModularMotionManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.WingMotionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceActivity extends TopBaseActivity {
    private final static String TAG = "DIL-BAS";
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;

    @BindView(R.id.btn_motion)
    Button button4;

    @BindView(R.id.button5)
    Button btn_chat;

    @BindView(R.id.btnback)
    Button btnback;
    public static boolean busy = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            register(ChoiceActivity.class);
            //screen always on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            ButterKnife.bind(this);
            hdCameraManager = (HDCameraManager) getUnitManager(FuncConstant.HDCAMERA_MANAGER);
            systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
            hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
            wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);

            //float button of the system
            systemManager.switchFloatBar(true, getClass().getName());


        //LOAD handshakes stats
        MySettings.initializeXML();
        MySettings.loadHandshakes();

        //initialize speak
        MySettings.initializeSpeak();

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button2)
                public void onClick(View view) {
                    Intent intent = new Intent(ChoiceActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button3)
                public void onClick(View view) {
                    Intent intent = new Intent(ChoiceActivity.this, inquiries.class);
                    startActivity(intent);
                    finish();
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button3)
                public void onClick(View view) {
                    Intent intent = new Intent(ChoiceActivity.this, MotionActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.button5)
                public void onClick(View view) {
                    Intent intent = new Intent(ChoiceActivity.this, MotionActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btnback.setOnClickListener(new View.OnClickListener() {
                @Override
                @OnClick(R.id.btnback)
                public void onClick(View view) {

                    finish();
                    System.exit(0);
                }
            });


        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onMainServiceConnected() {


    }


}