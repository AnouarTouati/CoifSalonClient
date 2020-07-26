package com.example.coifsalonclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInOrUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailEditTex, passwordEditText;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_up);

        mContext = this;
        firebaseAuth = FirebaseAuth.getInstance();
         emailEditTex=findViewById(R.id.editTextTextEmailAddress);
         passwordEditText=findViewById(R.id.editTextTextPassword);
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    Button  signUpButton=findViewById(R.id.signUp);
    signUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signUp();
        }
    });
    }


    private void signIn() {
        firebaseAuth.signInWithEmailAndPassword(emailEditTex.getText().toString(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent goToMainActivity = new Intent(mContext, MainActivity.class);
                        startActivity(goToMainActivity);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to sign in");
            }
        });
    }

    private void signUp() {
        firebaseAuth.createUserWithEmailAndPassword(emailEditTex.getText().toString(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent goToMainActivity = new Intent(mContext, MainActivity.class);
                        startActivity(goToMainActivity);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to sign up");
            }
        });
    }
}