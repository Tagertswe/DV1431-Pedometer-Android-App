package com.pedometer_android_app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.bagi.levente.pedometer.Pedometer;
import name.bagi.levente.pedometer.PedometerSettings;
import name.bagi.levente.pedometer.StepDisplayer;
import name.bagi.levente.pedometer.StepListener;
import name.bagi.levente.pedometer.Utils;

/**
 * Created by martin on 11/26/15.
 */
public class TabFragmentMain extends Fragment {

    private static final String TAG = "TabFragmentMain";
    private static final int STEPS_MSG = 1;

    private View view;
    private int mStepValue;

    private TextView countedSteps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_view, container, false);



        countedSteps = ((TextView)view.findViewById(R.id.mainView_stepcount));
        countedSteps.setText("steps: " + mStepValue);


//        // Inflate the layout for this fragment

        return view;
//        return inflater.inflate(R.layout.main_view, container, false);
    }



}

