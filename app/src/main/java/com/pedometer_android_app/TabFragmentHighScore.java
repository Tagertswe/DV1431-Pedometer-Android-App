package com.pedometer_android_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.highscore_view, container, false);
    }

    public void setDB(Db dba){
        this.db = dba;

        this.db.addUser("86", "Martin", "Olsson", "martin", "blaha");
        DateFormat df = new SimpleDateFormat("MMM d, ''yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        this.db.addWalk("5000",date, "86");
        this.db.addWalk("4000",date, "86");
        this.db.addWalk("7000",date, "86");
        this.db.addWalk("3000", date, "86");

        ArrayList<Walk> walkList = db.getWalkData("86");

        for(int i=0; i< walkList.size(); i++){
            System.out.println(walkList.get(i).toString()+"\n");
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
