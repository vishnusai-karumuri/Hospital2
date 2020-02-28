package com.kvsnbuilds.hospital2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;


// TODO: 2/17/2020 get data from firebase on login and display on successsful login

public class Home extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        Toast.makeText(this, "Clear App From Recents", Toast.LENGTH_SHORT).show();
    }
}
