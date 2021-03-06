package com.pedometer_android_app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by pol on 12/9/15.
 */

public class Db extends SQLiteOpenHelper
{
    // Database Name
    private static final String DATABASE_NAME = "PMmeter";
    // Table name
    private static String TABLE_USER = "User";
    private static final String TABLE_WALK = "Walk";
    // Columns User
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    // Columns Walk
    private static final String COLUMN_STEPS = "steps";
    private static final String COLUMN_DATE = "date";
    // For both tables
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USER_ID = "USRID";

    private static String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY,"
        + COLUMN_NAME + " TEXT,"
        + COLUMN_SURNAME + " TEXT,"
        + COLUMN_USERNAME + " TEXT,"
        + COLUMN_PASSWORD + " TEXT"
        + ")";

    private static String CREATE_WALK_TABLE = "CREATE TABLE " + TABLE_WALK + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_STEPS + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_USER_ID + " INTEGER,"
            + " FOREIGN KEY ("+COLUMN_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_ID+"));";

    public Db(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALK);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        //Create tables again
        //onCreate(db);
    }

    public long addUser(String SSN,String name,String surname,String username,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_ID, SSN);
        userValues.put(COLUMN_NAME, name);
        userValues.put(COLUMN_SURNAME, surname);
        userValues.put(COLUMN_USERNAME, username);
        String  hashedPass = md5(password);
        userValues.put(COLUMN_PASSWORD, hashedPass);
        long returnValue = db.insert(TABLE_USER, null, userValues);

        System.out.println("Inserting into table user - check nr: " + returnValue);
        db.close();
        return returnValue;
    }

    public long addWalk(String steps,String date,String SSN)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues walkValues = new ContentValues();
        walkValues.put(COLUMN_STEPS, steps);
        walkValues.put(COLUMN_DATE, date);
        walkValues.put(COLUMN_USER_ID, SSN);
        long returnValue = db.insert(TABLE_WALK, null, walkValues);

        System.out.println("Inserting into table walk - check nr: " + returnValue);
        db.close();
        return returnValue;
    }

    // returns number of rows affected.
    // returns -1 om inte uppdaterat
    //returns row number that has been updated if works
    //TODO something fishy here, it returns 0 upon trying to update an existing row.
    public long updateWalk(String SSN,String date,String steps)
    {
//        String clause ="\""+date+"\"" + " = " + COLUMN_DATE + " AND " + SSN + " = " + COLUMN_USER_ID;
        String clause ="\""+date+"\"" + " = " + COLUMN_DATE + " AND " + SSN + " = " + COLUMN_USER_ID;
        ContentValues walkUpdateValues = new ContentValues();
        walkUpdateValues.put(COLUMN_STEPS, steps);
        walkUpdateValues.put(COLUMN_DATE, date);
        walkUpdateValues.put(COLUMN_USER_ID,SSN);

        SQLiteDatabase db = this.getWritableDatabase();
        long returnValue = db.update(TABLE_WALK, walkUpdateValues, clause, null);
        System.out.println("Updating table walk - check nr: " + returnValue);
        db.close();
        return returnValue;
    }
    // this method keeps the saved walk data,only deletes user profile.
    public long deleteUser(String SSN)
    {
        String clause =SSN+"="+COLUMN_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        long returnValue = db.delete(TABLE_USER, clause, null);
        System.out.println("Deleting user - check nr: " + returnValue);
        db.close();
        return returnValue;
    }

    public void resetDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("reseting DB");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
        db.close();
    }

    public int walkExist(String SSN, String date){
        String clause ="SELECT * FROM "+ TABLE_WALK +" WHERE "+ COLUMN_USER_ID +"="+ SSN+" AND "+"\""+COLUMN_DATE+"\""+" = "+"\""+date+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(clause,null);
        if(c.moveToFirst())
        {
            db.close();
            return 1;
        }
        db.close();
        return 0;
    }

    public User getUser(String SSN)
    {
        String clause ="SELECT * FROM "+ TABLE_USER +" WHERE "+ COLUMN_ID +"="+ SSN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(clause,null);
        if(c.moveToFirst())
        {
            User user = new User(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4));
            db.close();
            return user;
        }
        db.close();
        return null;
    }

    //fetches a maximum of 10 elements from the database, for the highscore view
    public ArrayList<Walk> getWalkData(String SSN)
    {
        String clause ="SELECT * FROM "+ TABLE_WALK +" WHERE "+ COLUMN_USER_ID +"="+ SSN+" ORDER BY "+"\""+COLUMN_STEPS+"\""+" DESC"+" LIMIT 10";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(clause, null);
        ArrayList<Walk> walkList = new ArrayList<Walk>();
        if(c != null && c.getCount() > 0)
        {


            while(c.moveToNext())
            {
               Walk walk = new Walk(c.getString(1),c.getString(2),c.getString(3));
               walkList.add(walk);
            }
            db.close();
            return walkList;
        }
        db.close();
        return walkList;
    }

    public User loginCheck(String username,String password)
    {
        String  hashedPass = md5(password);
        String clause ="SELECT * FROM "+ TABLE_USER +" WHERE "+ COLUMN_USERNAME +"="+ "\""+username+"\"" + " AND " + COLUMN_PASSWORD + "=" + "\""+hashedPass+"\"" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(clause, null);
        if(c.moveToFirst())
        {
            User user = new User(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4));
            db.close();
            return user;
        }

        db.close();
        return null;

    }


    private static String md5(String s) { try {

        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
        return "";

    }

}
