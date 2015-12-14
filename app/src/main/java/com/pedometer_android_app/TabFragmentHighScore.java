package com.pedometer_android_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by martin on 02/12/2015.
 */
public class TabFragmentHighScore extends Fragment {
    private Db db;
    private static String TAG = "TabFragmentHighScore";
    private TextView date_steps;
    private TextView steps;
    private View view;

    private static String PACKAGE_NAME;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.highscore_view, container, false);
        db = new Db(getActivity());

        PACKAGE_NAME = getActivity().getPackageName();


        printFromDB();
            return view;
    }

    public void printFromDB(){
        Log.d("TabFragmentHighScore", "This is executed in setDB!");
        db.resetDB();
        this.db.addUser("86", "Martin", "Olsson", "martin", "blaha");
        DateFormat df = new SimpleDateFormat("MMM d, ''yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        this.db.addWalk("5000",date, "86");
        this.db.addWalk("4000",date, "86");
        this.db.addWalk("7000",date, "86");
        this.db.addWalk("3000", date, "86");

        ArrayList<Walk> walkList = db.getWalkData("86");

        for(int i=0; i< walkList.size(); i++) {
            //fetches current id number for steps textview
            int resID = getResources().getIdentifier("no_" + i + "_steps", "id", PACKAGE_NAME);
            steps = (TextView) view.findViewById(resID);
            steps.setText(walkList.get(i).getSteps());

//            //fetches current id number for steps date textview
            int resID_date = getResources().getIdentifier("no_"+i+"_date", "id", PACKAGE_NAME);
            date_steps = (TextView) view.findViewById(resID_date);
            date_steps.setText(walkList.get(i).getDate());

        }
    }






    //this method is contact in order to forward updated values of the stepcounter in other classes.
//    public static TabFragmentHighScore newInstance(int data) {
//        Log.d(TAG, "newInstance(steps:" + data + ")");
////        Bundle args = new Bundle();
////        args.putInt(TAG, data);
//        TabFragmentHighScore fragment = new TabFragmentHighScore();
////        fragment.setArguments(args);
//        return fragment;
//    }


}
