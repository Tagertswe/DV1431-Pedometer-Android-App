package com.pedometer_android_app;



import android.support.v4.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import name.bagi.levente.pedometer.PedometerSettings;
import name.bagi.levente.pedometer.StepService;
import name.bagi.levente.pedometer.Utils;

public class MainView extends AppCompatActivity implements TabFragmentMain.passDataInterface {
    private StepService mService;
    private int mStepValue;
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private Utils mUtils;
    private Boolean mIsRunning;
    private boolean mIsMetric;
    private static final String TAG = "TabFragmentMain";
    private ViewPager viewPager;

    private TabFragmentMain mFragment;
    private Bundle bundle;

    private static final int STEPS_MSG = 1;

    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    Log.d(TAG, "handlemessage is working");
//                    System.out.println("handlemessage is working sout");
                    mStepValue = (int)msg.arg1;
                    passData(mStepValue);

//                    bundle.putInt(TAG,mStepValue);
//                    countedSteps.setText("" + mStepValue);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };

    //    private OnDataPass dataPasser;
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
            startService(new Intent(MainView.this,
                    StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(MainView.this,
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
            stopService(new Intent(MainView.this,
                    StepService.class));
        }
        mIsRunning = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main_view_activity);

        mStepValue = 0;
        bundle = new Bundle();
//        bundle.putInt("TabFragmentMain",mStepValue);

        mUtils = Utils.getInstance();
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);

        mUtils.setSpeak(mSettings.getBoolean("speak", false));

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        startService(new Intent(MainView.this, StepService.class));
        bindStepService();
        mPedometerSettings.clearServiceRunning();
        mIsMetric = mPedometerSettings.isMetric();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.addTab(tabLayout.newTab().setText("Pedometer"));
        tabLayout.addTab(tabLayout.newTab().setText("High Score"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              if(tab.getPosition() == 0){
                    passData(mStepValue);
                    Log.d(TAG, "this is executed in tab selected for main view!");
//                    TabFragmentMain mFragment = (TabFragmentMain)getSupportFragmentManager().findFragmentById(R.id.TabFragmentMain_id);
//                    if(mFragment != null){
//                        mFragment.setStepsView(mStepValue);
//                        Log.d(TAG, "passdata in mainview is called in select tabs! step value: "+mStepValue);
//                    }
//                    else {
//                        // Otherwise, we're in the one-pane layout and must swap frags...
//                        Log.d(TAG, "passdata in mainview is called in select tabs! 2");
//                        // Create fragment and give it an argument for the selected article
//                        TabFragmentMain newFragment = new TabFragmentMain();
//                        Bundle args = new Bundle();
//                        args.putInt(TAG, mStepValue);
//                        newFragment.setArguments(args);
//
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                        // Replace whatever is in the fragment_container view with this fragment,
//                        // and add the transaction to the back stack so the user can navigate back
//                        transaction.replace(R.id.TabFragmentMain_id, newFragment);
//                        transaction.addToBackStack(null);
//
//                        // Commit the transaction
//                        transaction.commit();
//                    }
//                    viewPager.notify();
//                      viewPager.geti
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.detach(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);

        mUtils.setSpeak(mSettings.getBoolean("speak", false));

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        // Start the service if this is considered to be an application start (last onPause was long ago)
        if (!mIsRunning && mPedometerSettings.isNewStart()) {
            startStepService();
            bindStepService();
        } else if (mIsRunning) {
            bindStepService();
        }

        mPedometerSettings.clearServiceRunning();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }
        if (id == R.id.action_exit_application) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void passData(int data) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        TabFragmentMain mFragment = (TabFragmentMain)getSupportFragmentManager().findFragmentById(R.id.TabFragmentMain_id);
        mFragment = (TabFragmentMain)getSupportFragmentManager().findFragmentById(R.id.TabFragmentMain_id);
        if(mFragment != null){
//            FragmentManager fm = getFragmentManager();
            mFragment.setStepsView(mStepValue);
//            transaction.remove(mFragment);
            Log.d(TAG, "passdata in mainview is called! step value: "+mStepValue);
        }
        else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            Log.d(TAG, "passdata in mainview is called! 2");
            // Create fragment and give it an argument for the selected article
//            mFragment = new TabFragmentMain();
              mFragment = TabFragmentMain.newInstance(mStepValue);
//            TabFragmentMain newFragment = (TabFragmentMain)getSupportFragmentManager().findFragmentById(R.id.TabFragmentMain_id);
//            Bundle args = new Bundle();
//            bundle.putInt(TAG, mStepValue);
//            mFragment.setArguments(bundle);

//        viewPager.notify
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.TabFragmentMain_id, mFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        viewPager.getAdapter().notifyDataSetChanged();
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        private int mNumOfTabs;
        private String TAG = "";

        public PagerAdapter(android.support.v4.app.FragmentManager fm, int NumOfTabs) {
            super(fm);


            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "this prints in pageradapter");
            switch (position) {
                case 0:
//                    TabFragmentMain tab1 = new TabFragmentMain();
//                return TabFragmentMain.instantiate(,"TabFragmentMain");
//                tab1.update();
                   Fragment tab1 = TabFragmentMain.newInstance(mStepValue);
//                    bundle.putInt(TAG, mStepValue);
//                    tab1.setArguments(bundle);
//                    tab1.setStepsView(mStepValue);
                    return tab1;
//                    return  TabFragmentMain.newInstance(mStepValue);
                case 1:
                    TabFragmentSettings tab2 = new TabFragmentSettings();
                    return tab2;
                case 2:
                    TabFragmentPedometer tab3 = new TabFragmentPedometer();
                    return tab3;
                case 3:
                    TabFragmentHighScore tab4 = new TabFragmentHighScore();
                    return tab4;
                default:
                    return null;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
