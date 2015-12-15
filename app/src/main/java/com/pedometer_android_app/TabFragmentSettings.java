package com.pedometer_android_app;

/**
 * Created by martin on 11/26/15.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabFragmentSettings extends Fragment {

    private static View view;
    private Button aboutButton;
    String aboutTest = "This Pedometer application has been created by the BTH students:\nMartin Olsson\nPol Haghverdian.\n\n" +
            "We utilized accelerometer open source code for step tracking, which was taken from https://github.com/bagilevi/android-pedometer";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.settings_view, container, false);

        aboutButton = ((Button) view.findViewById(R.id.about_button));

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {
                TextView k = (TextView) view.findViewById(R.id.settings_view);
                k.setText(aboutTest);
            }
        });

        return view;
    }
}