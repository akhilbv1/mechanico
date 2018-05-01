package com.android.mechanico.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mechanico.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etEmail, etPassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewsIntialization();
    }


    private void viewsIntialization() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btnLogin);
        TextView newUser = findViewById(R.id.txtNewUser);
        TextView newMech = findViewById(R.id.txtnewMech);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("LOGIN");
        setSupportActionBar(toolbar);
        newUser.setOnClickListener(this);
        newMech.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtNewUser:
                launchUserRegistrationActivity();
                break;

            case R.id.txtnewMech:
                launchMechRegistrationActivity();
                break;

            case R.id.btnLogin:
                validateLogin();
                break;
        }
    }

    private void launchUserRegistrationActivity(){
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    private void launchMechRegistrationActivity(){
        Intent intent = new Intent(this,MechanicRegistrationActivity.class);
        startActivity(intent);
    }

    private void validateLogin()
    {
        if(TextUtils.isEmpty(etEmail.getText().toString().trim()))
        {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches())
        {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(etPassword.getText().toString().trim()))
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();

        }
        else {
            loginFirebaseAuth();
        }

    }
    private void loginFirebaseAuth(){
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(etEmail.getText().toString().trim(),etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    dialog.cancel();
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    loginSuccess(task.getResult().getUser().getUid());
                }
                else
                {

                    dialog.cancel();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loginSuccess(String currentUser){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("currentuser",currentUser);
        editor.commit();

        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("currentUser",currentUser);
        startActivity(intent);
    }
}
