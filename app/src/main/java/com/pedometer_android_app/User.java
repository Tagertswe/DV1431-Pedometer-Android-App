package com.pedometer_android_app;

/**
 * Created by Pol on 2015-12-11.
 */
public class User
{



    private String SSN;
    private String name;
    private String surname;
    private String username;
    private String password;

    public User(String SSN,String name,String surname,String username, String password)
    {
        this.SSN = SSN;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public User()
    {
        this.SSN = "";
        this.name = "";
        this.surname = "";
        this.username = "";
        this.password = "";
    }

    public String getSSN()
    {
        return SSN;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }


}
