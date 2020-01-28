package com.kvsnbuilds.hospital2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
    String name, mail, pwd, cnfpwd, uname, address;
    Spinner bloodgroup;
    FirebaseAuth auth;
    validation validation;
    FirebaseDatabase db;
    DatabaseReference dbref,userref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        bloodgroup = findViewById(R.id.signup_spinner_blood_group);
    }

    void getdetails()
    {
        name = et_name.getText().toString().trim();
        uname = et_uname.getText().toString().trim();
        pwd = et_pwd.getText().toString().trim();
        cnfpwd = et_cnfpwd.getText().toString().trim();
        mail = et_mail.getText().toString().trim();
        address = et_address.getText().toString().trim();
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
                if (task.isSuccessful())
                {
                    Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                } else
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
            et_mail.setError("Username Empty");
            et_mail.requestFocus();
        }
        else if(val == 1)
        {
            et_mail.setError("Not A Valid Email Address");
            et_mail.requestFocus();
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
        if(!pwd.equals(cnfpwd))
        {
            et_pwd.setError("Passwords DONOT Match");
            et_cnfpwd.setError("Passwords DONOT Match");
            et_pwd.requestFocus();
        }
        if(name.isEmpty())
        {
            et_name.setError("Name Empty");
            et_name.requestFocus();
        }
        else if(uname.isEmpty())
        {
            et_uname.setError("Username Empty");
            et_uname.requestFocus();
        }
        else if(address.isEmpty())
        {
            et_address.setError("Address Is Empty");
            et_address.requestFocus();
        }
        else if(cnfpwd.isEmpty())
        {
            et_cnfpwd.setError("Password Empty");
            et_cnfpwd.requestFocus();
        }
    }

    void writetodb()
    {
        //Toast.makeText(this, ""+uname, Toast.LENGTH_SHORT).show();
        dbref.child("Users").child(uname).child("Email").setValue(mail);
        //userref.child(uname).child("EmailID").setValue(mail);
    }
    public void signup(View v)
    {
        getdetails();
        //validate();
        writetodb();
        //Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();
        //createuser();
    }
}
