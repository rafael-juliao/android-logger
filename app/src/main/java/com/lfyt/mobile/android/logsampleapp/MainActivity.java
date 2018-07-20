package com.lfyt.mobile.android.logsampleapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    MyAwesomeClass myAwesomeClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.D(MainActivity.this, "Click in FAB");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                L.E(MainActivity.this, "Click in Action RANDOM ERROR");
                            }
                        }).show();
            }
        });

        myAwesomeClass = new MyAwesomeClass();

        L.I(this, " Hey Just Created This Activity");
        L.D(myAwesomeClass, " Awesome Class onActivityCreated");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        L.D(this, "The Menu Was Created");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        L.E(this, "Error When Menu Item Selected");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        L.I(this, "Activity Started");
        L.D(myAwesomeClass, "Awesome Class Handling onActivityStarted");

        try{
           int[] array = new int[1];
           int a = array[2];
        }catch (ArrayIndexOutOfBoundsException e){
            L.EXP(this, e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.I(this, "Activity Stopped");
        L.D(myAwesomeClass, "Awesome Class Handling onActivityStopped");

        try{
            String array = null;
            int a = array.length();
        }catch (NullPointerException e){
            L.EXP(myAwesomeClass, e);
        }
        L.E(myAwesomeClass, "HAVE TO STOP DUE TO EXECPETION");

    }
}
