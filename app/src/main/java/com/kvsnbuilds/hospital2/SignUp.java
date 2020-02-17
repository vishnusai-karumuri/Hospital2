package com.kvsnbuilds.hospital2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity
{

    TextInputEditText et_name, et_uname, et_pwd, et_cnfpwd, et_mail, et_address;
    ImageView image;
    String name, mail, pwd, cnfpwd, uname, address, bloodgroup;
    Spinner sp_bloodgroup;
    FirebaseAuth auth;
    validation validation;
    FirebaseDatabase db;
    DatabaseReference dbref, userref;
    boolean f1, f2, f3, f4, f5, f6, f7;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Create Account");
        initialize();
    }

    void initialize()
    {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();

        validation = new validation();

        et_name = findViewById(R.id.signup_name);
        et_mail = findViewById(R.id.signup_mail);
        et_pwd = findViewById(R.id.signup_pwd);
        et_cnfpwd = findViewById(R.id.signup_confpwd);
        et_uname = findViewById(R.id.signup_uname);
        et_address = findViewById(R.id.signup_address);
        sp_bloodgroup = findViewById(R.id.signup_spinner_blood_group);
        image = findViewById(R.id.signup_logo);

    }

    void getdetails()
    {
        name = et_name.getText().toString().trim();
        uname = et_uname.getText().toString().trim();
        pwd = et_pwd.getText().toString().trim();
        cnfpwd = et_cnfpwd.getText().toString().trim();
        mail = et_mail.getText().toString().trim();
        address = et_address.getText().toString().trim();
        bloodgroup = sp_bloodgroup.getSelectedItem().toString();
        f7 = true;
        f6 = true;
        f5 = true;
        f4 = true;
        f3 = true;
        f2 = true;
        f1 = true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //Toast.makeText(this, "Clear From Recents To Exit App", Toast.LENGTH_SHORT).show();
    }

    void createuser()
    {
        auth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    writetodb();//to write details to database
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SignUp.this, image, "trans1");
                    Intent in = new Intent(SignUp.this, Home.class);
                    startActivity(in, activityOptionsCompat.toBundle());
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    //Toast.makeText(SignUp.this, "User Created, Data Pushed to DB", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignUp.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void validate()
    {
        int val = validation.evalidation(mail);
        if(val == 0)
        {
            f1 = false;
            et_mail.setError("Username Empty");
            et_mail.requestFocus();
        }
        else if(val == 1)
        {
            f1 = false;
            et_mail.setError("Not A Valid Email Address");
            et_mail.requestFocus();
        }
        int val1 = validation.pvalidation(pwd);
        if(val1 == 0)
        {
            f2 = false;
            et_pwd.setError("Password Empty");
            et_pwd.requestFocus();
        }
        else if(val1 == 1)
        {
            f2 = false;
            et_pwd.setError("Password Must Have alphabet, number, @,#,$,% and must be between 6 to 20 digits");
            et_pwd.requestFocus();
        }
        else if(val1 == 2)
        {
            //f2 = false;
            //Toast.makeText(this, "Valid Password", Toast.LENGTH_SHORT).show();
        }
        if(!pwd.equals(cnfpwd))
        {
            f3 = false;
            et_pwd.setError("Passwords DONOT Match");
            et_cnfpwd.setError("Passwords DONOT Match");
            et_pwd.requestFocus();
        }
        if(name.isEmpty())
        {
            f4 = false;
            et_name.setError("Name Empty");
            et_name.requestFocus();
        }
        else if(uname.isEmpty())
        {
            f5 = false;
            et_uname.setError("Username Empty");
            et_uname.requestFocus();
        }
        else if(address.isEmpty())
        {
            f6 = false;
            et_address.setError("Address Is Empty");
            et_address.requestFocus();
        }
        else if(cnfpwd.isEmpty())
        {
            f7 = false;
            et_cnfpwd.setError("Password Empty");
            et_cnfpwd.requestFocus();
        }
    }

    void writetodb()
    {
        userref = dbref.child("Users").child(uname);
        userref.child("Name").setValue(name);
        userref.child("Email").setValue(mail);
        userref.child("Address").setValue((address));
        userref.child("BloodGroup").setValue(bloodgroup);
    }

    public void signup(View v)
    {
        getdetails();//to fetch data from all the fields
        validate();//to check the regex patterns for mail,pwd and to makesure that no field is empty
        Toast.makeText(this, "" + f1, Toast.LENGTH_SHORT).show();
        if(f1 && f2 && f3 && f4 && f5 && f6 && f7)
        {
            createuser();//to create user
        }

    }
}
