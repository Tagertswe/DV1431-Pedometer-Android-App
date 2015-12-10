package com.pedometer_android_app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import name.bagi.levente.pedometer.PedometerSettings;
import name.bagi.levente.pedometer.StepService;
import name.bagi.levente.pedometer.Utils;

/**
 * Created by pol on 11/26/15.
 */
public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private EditText mUsername;
    private EditText mPassword;

    private StepService mService;
    private int mStepValue;
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private Utils mUtils;
    private Boolean mIsRunning;
    private TextView countedSteps;
    private View view;
    private boolean mIsMetric;

    private static final String TAG = "TabFragmentMain";
    private static final int STEPS_MSG = 1;


    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
//        public void paceChanged(int value) {
//            mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
//        }
//        public void distanceChanged(float value) {
//            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
//        }
//        public void speedChanged(float value) {
//            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int)(value*1000), 0));
//        }
//        public void caloriesChanged(float value) {
//            mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG, (int)(value), 0));
//        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(MainActivity.this,
                    StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(MainActivity.this,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(MainActivity.this,
                    StepService.class));
        }
        mIsRunning = false;
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        android.os.Debug.waitForDebugger();

//        mStepValue = 1;

//        countedSteps = ((TextView)view.findViewById(R.id.mainView_stepcount));
//        countedSteps = ((TextView)findViewById(R.id.steps_test));
//        countedSteps.setText("steps: " + mStepValue);



        mUtils = Utils.getInstance();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        mUtils.setSpeak(mSettings.getBoolean("speak", false));

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        startService(new Intent(MainActivity.this, StepService.class));
        bindStepService();

        mPedometerSettings.clearServiceRunning();


        mIsMetric = mPedometerSettings.isMetric();



        mButton = (Button) findViewById(R.id.login_button);
        mUsername = (EditText)findViewById(R.id.loginUsername);
        mPassword = (EditText)findViewById(R.id.loginPassword);

        //listener for password field, so you can press enter on the virtual
        // keyboard and act as if you were pressing the login button
        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            checkLogin();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();

            }
        });

    }

//
//    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
//        // Retrieve all services that can match the given intent
//        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
//
//        // Make sure only one match was found
//        if (resolveInfo == null || resolveInfo.size() != 1) {
//            return null;
//        }
//
//        // Get component info and create ComponentName
//        ResolveInfo serviceInfo = resolveInfo.get(0);
//        String packageName = serviceInfo.serviceInfo.packageName;
//        String className = serviceInfo.serviceInfo.name;
//        ComponentName component = new ComponentName(packageName, className);
//
//        // Create a new intent. Use the old one for extras and such reuse
//        Intent explicitIntent = new Intent(implicitIntent);
//
//        // Set the component to be explicit
//        explicitIntent.setComponent(component);
//
//        return explicitIntent;
//    }

    public void checkLogin(){
        if( mUsername.getText().toString().contentEquals("pol") && mPassword.getText().toString().contentEquals("pol") ){
            //Intent mainView = new Intent(MainActivity.this, MainView.class );
            MainActivity.this.startActivity(new Intent(MainActivity.this, MainView.class));
        }
    }



    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    Log.d(TAG, "handlemessage is working");
                    System.out.println("handlemessage is working sout");
                    mStepValue = (int)msg.arg1;
//                    countedSteps.setText("" + mStepValue);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };
}
