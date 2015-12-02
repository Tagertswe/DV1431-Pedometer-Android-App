package com.pedometer_android_app;

/**
 * Created by martin on 11/26/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentMain tab1 = new TabFragmentMain();
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

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}