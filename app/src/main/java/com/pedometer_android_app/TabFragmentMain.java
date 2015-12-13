package com.pedometer_android_app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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


    //this method is contact in order to forward updated values of the stepcounter in other classes.
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
        // initializes the counterSteps textView to the textview xml.
        countedSteps = ((TextView) view.findViewById(R.id.mainView_stepcount2));

        mStepValue = 0;


        //gets the arguments for the update of the step counter
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

        return view;
    }



    public void setStepsView(int data){
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

