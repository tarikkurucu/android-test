package com.example.tarik.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

//            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,age INT(3))");
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS newUsers (name VARCHAR,age INT(3), id INTEGER PRIMARY KEY)");

           /* myDatabase.execSQL("INSERT INTO newUsers (name,age) VALUES ('Bugs',10)");

            myDatabase.execSQL("INSERT INTO newUsers (name,age) VALUES ('Yosemite',40)");*/

            Cursor c = myDatabase.rawQuery("SELECT * FROM newUsers", null);

            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            int idIndex = c.getColumnIndex("id");

            c.moveToFirst();
            while (c!=null){
                Log.i("name",c.getString(nameIndex));
                Log.i("age",Integer.toString(c.getInt(ageIndex)));
                Log.i("id",Integer.toString(c.getInt(idIndex)));

                c.moveToNext();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*try{
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Events",MODE_PRIVATE,null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR,year INT(5))");
            myDatabase.execSQL("INSERT INTO events (name,year) VALUES ('aaaa',1250)");
            myDatabase.execSQL("INSERT INTO events (name,year) VALUES ('bbbb',1554)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM events",null);

            int nameIndex = c.getColumnIndex("name");
            int yearIndex = c.getColumnIndex("year");

            c.moveToFirst();
            while (c!=null){
                Log.i("name",c.getString(nameIndex));
                Log.i("age",Integer.toString(c.getInt(yearIndex)));
                c.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
