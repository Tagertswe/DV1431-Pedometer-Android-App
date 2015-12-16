package com.pedometer_android_app;

/**
 * Created by martin on 11/26/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import name.bagi.levente.pedometer.PedometerSettings;
import name.bagi.levente.pedometer.StepService;
import name.bagi.levente.pedometer.Utils;

public class TabFragmentPedometer extends Fragment {

    private static View view;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;



    private static final int STEPS_MSG = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.pedometer_view, container, false);

        startButton = ((Button) view.findViewById(R.id.start_button));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {

                ((MainView)getActivity()).startStepService();
                ((MainView)getActivity()).bindStepService();

            }
        });

        stopButton = ((Button) view.findViewById(R.id.stop_button));
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {

                ((MainView)getActivity()).stopStepService();
                ((MainView)getActivity()).unbindStepService();

            }
        });


        resetButton = ((Button) view.findViewById(R.id.reset_button));
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {

                ((MainView)getActivity()).resetStep();
            }
        });

        return view;
    }

    // Container Activity must implement this interface
    public interface pedometerDataPassing
    {
        public void dataCom (int data);
    }

}