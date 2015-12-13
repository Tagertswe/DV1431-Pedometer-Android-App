package com.pedometer_android_app;

/**
 * Created by Pol on 2015-12-11.
 */
public class Walk
{
    private String steps;
    private String date;
    private String SSN;

    public Walk(String steps,String date,String SSN)
    {
        this.steps = steps;
        this.date = date;
        this.SSN = SSN;
    }

    public String getSteps()
    {
        return steps;
    }

    public String getDate()
    {
        return date;
    }

    public String getSSN()
    {
        return SSN;
    }
}
