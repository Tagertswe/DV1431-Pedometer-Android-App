package com.pedometer_android_app;

/**
 * Created by martin on 11/26/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabFragmentPedometer extends Fragment {

    private static View view;
    private Button startButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.pedometer_view, container, false);

        startButton = ((Button) view.findViewById(R.id.start_button));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {

                System.out.println("start!!!");

            }
        });

        return view;
    }


}