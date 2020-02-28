package com.kvsnbuilds.hospital2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    String uname, pwd, mail;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbref;
    boolean flag1,flag2;
    ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);

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
        flag1 = true;
        flag2 = true;
    }

    void validate()
    {
        if(uname.isEmpty())
        {
            et_uname.setError("User Name Field Empty");
            et_uname.requestFocus();
            flag1 = false;
        }
        int val1 = validation.pvalidation(pwd);
        if (val1 == 0)
        {
            et_pwd.setError("Password Empty");
            et_pwd.requestFocus();
            flag2 = false;
        } else if (val1 == 1)
        {
            et_pwd.setError("Password Must Have alphabet, number, @,#,$,% and must be between 6 to 20 digits");
            et_pwd.requestFocus();
            flag2 = false;
        } else if (val1 == 2)
        {
            //Toast.makeText(this, "Valid Password", Toast.LENGTH_SHORT).show();
        }
    }

    void checkdb_login()
    {

        dbref.child("Users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild(uname))
                {
                    mail = dataSnapshot.child(uname).child("Email").getValue().toString();
                    auth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, Home.class));
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                            } else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //Toast.makeText(MainActivity.this, ""+mail, Toast.LENGTH_SHORT).show();
                } else
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

    public void login(View v) throws InterruptedException
    {
        getdetails();
        validate();
        if(flag1  && flag2)
        {
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            //Toast.makeText(this, "Flags Checked", Toast.LENGTH_SHORT).show();
            checkdb_login();
        }
    }

    public void signup(View v)
    {
        //ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, iv_logo, "trans1");
        //Intent in = new Intent(this, SignUp.class);
        //startActivity(in, activityOptionsCompat.toBundle());
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Clear From Recents", Toast.LENGTH_SHORT).show();
    }
}
