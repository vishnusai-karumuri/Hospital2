package com.kvsnbuilds.hospital2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    RelativeLayout rellay1, rellay2;
    TextInputEditText et_uname, et_pwd;
    ImageView iv_logo;
    String uname, pwd,mail;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbref;

    validation validation;
    Handler handler = new Handler();
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        et_uname = findViewById(R.id.user_uid);
        et_pwd = findViewById(R.id.user_pwd);
        iv_logo = findViewById(R.id.imgView_logo);

        db = FirebaseDatabase.getInstance();
        dbref = db.getReference();
        validation = new validation();

        auth = FirebaseAuth.getInstance();


        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }

    void getdetails()
    {
        uname = et_uname.getText().toString().trim();
        pwd = et_pwd.getText().toString();
    }

    void validate()
    {
        int val = validation.evalidation(uname);
        if(val == 0)
        {
            et_uname.setError("Username Empty");
            et_uname.requestFocus();
        }
        else if(val == 1)
        {
            et_uname.setError("Not A Valid Email Address");
            et_uname.requestFocus();
        }
        else if(val == 2)
        {
            Toast.makeText(this, "Valid Email Address", Toast.LENGTH_SHORT).show();
        }
        int val1 = validation.pvalidation(pwd);
        if(val1 == 0)
        {
            et_pwd.setError("Password Empty");
            et_pwd.requestFocus();
        }
        else if(val1 == 1)
        {
            et_pwd.setError("Password Must Have alphabet, number, @,#,$,% and must be between 6 to 20 digits");
            et_pwd.requestFocus();
        }
        else if(val1 == 2)
        {
            Toast.makeText(this, "Valid Password", Toast.LENGTH_SHORT).show();
        }
    }

    void checkdb()
    {

        dbref.child("Users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(uname))
                {
                    mail = dataSnapshot.child(uname).child("Email").getValue().toString();
                    auth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Toast.makeText(MainActivity.this, ""+mail, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(MainActivity.this, "No Such User Exist", Toast.LENGTH_SHORT).show();
                    et_uname.setError("Invalid Username");
                    et_uname.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    void firebaselogin()
    {
        //Toast.makeText(this, ""+mail, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+pwd, Toast.LENGTH_SHORT).show();
    }
    public void login(View v)
    {
        getdetails();
        //validate();
        checkdb();
        firebaselogin();
    }

    public void signup(View v)
    {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,iv_logo,"trans1");
        Intent in = new Intent(this,SignUp.class);
        startActivity(in,activityOptionsCompat.toBundle());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Clear From Recents", Toast.LENGTH_SHORT).show();
    }
}
