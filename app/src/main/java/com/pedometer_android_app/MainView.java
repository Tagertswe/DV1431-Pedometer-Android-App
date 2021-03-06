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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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

    private int mCounter;
    private TabFragmentMain mFragment;
    private Bundle bundle;

    private String mCurrentUserSSN;
    private static final int STEPS_MSG = 1;
    //initializes database connection.
    public Db db = new Db(this);

    Button stopButton;
    Button startButton;

    // Handles incoming message from the StepService
    // (accelerometer stepcounter background service handler)
    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    Log.d(TAG, "handlemessage is working");
//                    System.out.println("handlemessage is working sout");
                    mStepValue = (int)msg.arg1;
                    //the counter is checked here to slow down the update rate of fragments, so less battery drain will occur

                    //pass step data to TabFragmentMain fragment
                    passData(mStepValue);
                    if(mCounter == 5){
                        //updates current step counter value for current logged in user to the database
                        updateCurrentWalk();
                        //passes the ssn of the current logged in user to TabFragmentHighScore so
                        //the top 10 can be populated.
//                        passCurrentUser(mCurrentUserSSN);
                        mCounter = 0;
                    }
                    mCounter++;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };

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

    public void resetStep()
    {
        mService.resetStepCounter();
        mStepValue = 0;
    }

    public void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(MainView.this,
                    StepService.class));
        }
    }

    public void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(MainView.this,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    public void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    public void stopStepService() {
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
        mCounter = 0;
        setContentView(R.layout.main_view_activity);

        mStepValue = 0;
        bundle = new Bundle();

        // initializes the StepService for continuous step counter tracking.
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


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mCurrentUserSSN = extras.getString("CurrentUser");
        }


        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // if TabFragmentMain tab is selected, then it contacts the passData() method.
                if (tab.getPosition() == 0) {
                    passData(mStepValue);
                    Log.d(TAG, "this is executed in tab selected for main view!");
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    // adds the ongoing progress of the walk to the database for high score top 10 purposes.
    public void updateCurrentWalk(){
        DateFormat df = new SimpleDateFormat("MMM d, ''yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        if(mCurrentUserSSN != ""){
            String currSteps = "";
            try{
                currSteps = Integer.toString(mStepValue);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

            //checks if a walk exists, returns 1 if it does, else it returns 0.
            long check2 = db.walkExist(mCurrentUserSSN,date);
            System.out.println("value from walkExist is: "+check2);
            //updates a current walk if it exists
            if(check2 != 0){
                System.out.println("a walk already exists in database!");
                long check = db.updateWalk(mCurrentUserSSN,date,currSteps);
                System.out.println("return value of updatewalk is: "+check);
            }
            //if it doesn't exist, then it adds a walk to the database.
            else{
                System.out.println("a new walk has been inserted in database!");
                db.addWalk(currSteps,date,mCurrentUserSSN);
            }
        }
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
            mCurrentUserSSN = "";
            BackToLogin();
            return true;
        }
        if (id == R.id.action_exit_application) {
            stopStepService();
            unbindStepService();
            AppExit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This method is called for every stepcounter update.
    @Override
    public void passData(int data) {
        // assigns mFragment to the existing Fragment for the layout of TabFragmentMain,
        // it's null if there isn't one running.
        mFragment = (TabFragmentMain)getSupportFragmentManager().findFragmentById(R.id.TabFragmentMain_id);
        if(mFragment != null){
            // If it's null, then it stes the textview in TabFragmentMain to an updated value.
            mFragment.setStepsView(mStepValue);
            Log.d(TAG, "passdata in mainview is called! step value: "+mStepValue);
        }
        else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            Log.d(TAG, "passdata in mainview is called! 2");
            // Create fragment and give it an argument for the selected article
              mFragment = TabFragmentMain.newInstance(mStepValue);
            // Creates a new transaction for TabFragmentMain fragment, and replaces the current one.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.TabFragmentMain_id, mFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }

        //Notifies the viewpager that the fragments have changed.
        viewPager.getAdapter().notifyDataSetChanged();
    }

    //returns the current user that is logged in for the high score fragment which in turns calls this
    //method
    @Override
    public String getCurrentUser() {
        return mCurrentUserSSN;
    }

    // The below class is for the handling of tabs
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
                   Fragment tab1 = TabFragmentMain.newInstance(mStepValue);
                    return tab1;
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

        //This is an override for the viewpager so it will always recreate the fragments upon
        // switching between them. This is to ensure the main view is updated dynamically and updates
        //the steps continuously
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }



    public void AppExit(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

    }//close method

    public void BackToLogin(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }//close method


}
