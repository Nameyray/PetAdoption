package com.moringaschool.petadoption.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moringaschool.petadoption.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    public static  final  String TAG = RegisterActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.name)
    TextView userName;
    @BindView(R.id.email)
    TextView emailAddress;
    @BindView(R.id.mobile)
    TextView phone;
    @BindView(R.id.password)
    TextView passCode;
    @BindView(R.id.confirm)
    TextView confirmPassword;
    @BindView(R.id.signup)
    TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                createNewUser();
            }
        });
        //getInstance of firebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        createAuthStateListener();

    }
    private void createNewUser(){
        final String name = userName.getText().toString().trim();
        final String mobile = phone.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = passCode.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();

        boolean validName = isValidUserName(name);
        boolean validEmail = isValidEmail(email);
        boolean validPassword = isValidPassword(password);
        if (!validEmail || !validName || !validPassword ) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "Authentication is a success!");
                    }else{
                        Toast.makeText(RegisterActivity.this, "ooops! Authentication Failed", Toast.LENGTH_LONG).show();

                    }
                });
    }
    //    listening for user authentication
    private void createAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    //validate a name is entered
    private boolean isValidUserName(String name){
        if(name.equals("")){
            userName.setError("Please enter your name");
            return false;
        }
        return true;
    }

    //check the email address if it is valid
    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail){
            emailAddress.setError("Please enter a valid email address");
            return false;
        }
        return true;
    }

    //check the password
    private boolean isValidPassword(String password){
        if(password.equals("") || password.length() < 6){
            passCode.setError("field required, Please create a strong password");
            return false;
        }
        return true;
    }

}