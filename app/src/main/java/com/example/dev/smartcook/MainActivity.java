package com.example.dev.smartcook;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle myToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDrawerLayout=(DrawerLayout) findViewById(R.id.main);
        myToggle=new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
        myDrawerLayout.addDrawerListener(myToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        myToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myToggle.onOptionsItemSelected(item)){
            Log.d("YO","Klik");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.examine){

        }
        else if (id == R.id.create_session){

        }
        else if (id == R.id.show_session){


        }
        return false;
    }
}
