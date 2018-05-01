package com.android.mechanico.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.mechanico.FirebaseTablesPojo.UserRegistrationFirebaseTable;
import com.android.mechanico.R;
import com.android.mechanico.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by akhil on 26/4/18.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText etUsername, etEmail, etMobileNum, etPassword, etConfPassword;
    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        viewsIntialization();
    }


    private void viewsIntialization() {
        etUsername = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etMobileNum = findViewById(R.id.etMobileNum);
        etPassword = findViewById(R.id.etPassword);
        etConfPassword = findViewById(R.id.etConfPassword);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        //toolbar.setNavigationIcon(R.drawable.a);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setEmpty() {
        etUsername.setText("");
        etMobileNum.setText("");
        etPassword.setText("");
        etEmail.setText("");
    }

    private void registerByValidation() {

        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etMobileNum.getText().toString().trim())) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (etMobileNum.getText().toString().trim().length() < 10) {
            Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etConfPassword.getText().toString().trim())) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (etPassword.getText().toString().length() < 7) {
            Toast.makeText(this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().trim().equals(etConfPassword.getText().toString().trim())) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            // new DatabaseSync().execute();
            registerUser();

        }
    }

    private void registerUser() {
        dialog.show();
        auth.createUserWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.cancel();
                    dialog.dismiss();
                    registerUserDb(task.getResult().getUser().getUid());
                } else {
                    dialog.cancel();
                    dialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "User Registered Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUserDb(String uid) {
        dialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        UserRegistrationFirebaseTable user = new UserRegistrationFirebaseTable();
        user.setUsername(etUsername.getText().toString().trim());
        user.setEmail(etEmail.getText().toString().trim());
        user.setMobileNumber(etMobileNum.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());

        databaseReference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    dialog.cancel();
                    setEmpty();
                    Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    setEmpty();
                    Toast.makeText(RegistrationActivity.this, "User Registered Failed", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        registerByValidation();
    }
}
