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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignInOrUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText emailEditTex, passwordEditText, nameEditText;
    Button signInButton, signUpButton, submitNameButton;
    TextView errorTextView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_up);

        mContext = this;
        firebaseAuth = FirebaseAuth.getInstance();

        emailEditTex = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        nameEditText = findViewById(R.id.nameEditText);
        submitNameButton = findViewById(R.id.submitNameButton);
        submitNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheName();
            }
        });
        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.GONE);
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
                        firebaseUser = authResult.getUser();
                        showNameField();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to sign up");
            }
        });

    }

    private void showNameField() {
        ////////////////////////////////////////
        emailEditTex.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        ///////////////////////////////////////
        nameEditText.setVisibility(View.VISIBLE);
        submitNameButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    private void setTheName() {
        if (firebaseUser != null) {
            errorTextView.setVisibility(View.GONE);
            String name = nameEditText.getText().toString();
            if (name.length() > 3) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();
                firebaseUser.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent goToMainActivity = new Intent(mContext, MainActivity.class);
                        startActivity(goToMainActivity);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("MyFirebase", "Failed to sign up");
                    }
                });
            } else {
                errorTextView.setText(R.string.invalid_name);
                errorTextView.setVisibility(View.VISIBLE);
            }

        }


    }
}