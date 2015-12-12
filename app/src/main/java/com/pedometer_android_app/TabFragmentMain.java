package com.pedometer_android_app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by martin on 11/26/15.
 */
public class TabFragmentMain extends Fragment{

    private static final String TAG = "TabFragmentMain";
    private static final int STEPS_MSG = 1;
    private static View view;
    private int mStepValue;
    private TextView  countedSteps;


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
//        super.onCreate(savedInstanceState);
//
//        Bundle args = getArguments();
//        if (args == null) {
//            Log.d(TAG, "bundle is null in oncreate!");
//        } else {
//            Log.d(TAG, "bundle received in oncreate! ");
//            mStepValue = args.getInt("TabFragmentMain");
//            setStepsView(mStepValue);
//        }
//    }

    public static TabFragmentMain newInstance(int data) {
        Log.d(TAG, "newInstance(steps:" + data + ")");
        Bundle args = new Bundle();
        args.putInt(TAG, data);
        TabFragmentMain fragment = new TabFragmentMain();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.main_view, container, false);
        countedSteps = ((TextView) view.findViewById(R.id.mainView_stepcount2));

        mStepValue = 0;


        Bundle args = getArguments();
        if (args == null) {
            Log.d(TAG, "bundle is null!");
        } else {
            Log.d(TAG, "bundle received! ");
            mStepValue = args.getInt("TabFragmentMain");
        }


        //fetches current step value from the Activity class where the StepService background
        //service is deployed.

        setStepsView(mStepValue);
//        countedSteps.setText(String.valueOf(mStepValue));

        return view;
    }


//    public void update(){
////        countedSteps = ((TextView)view.findViewById(R.id.mainView_stepcount));
//        countedSteps.setText(String.valueOf(mStepValue));
//    }
    public void setStepsView(int data){
//        countedSteps = ((TextView)view.findViewById(R.id.mainView_stepcount));
        mStepValue = data;
        countedSteps.setText(String.valueOf(mStepValue));
    }



    public interface passDataInterface{
        void passData(int data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}

