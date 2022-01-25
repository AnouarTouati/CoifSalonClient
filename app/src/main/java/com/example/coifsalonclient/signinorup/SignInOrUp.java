package com.example.coifsalonclient.signinorup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coifsalonclient.MainActivity;
import com.example.coifsalonclient.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInOrUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    EditText emailEditTex, passwordEditText, nameEditText;
    Button signInButton, signUpButton, submitNameButton,retryButton;
    TextView errorTextView;
    ConstraintLayout errorLayout,layout1,layout2;

    Context mContext;

    enum GoBackTo {
        SUBMIT_USER_NAME,
        SIGN_IN_UP
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_up);

        mContext = this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        setUpViews();
        showLayout1();
    }
    private void setUpViews(){
        getViewsReferences();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


        submitNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheName();
            }
        });

    }
    private void getViewsReferences(){
        layout1=findViewById(R.id.signInOrUpSubLayout1);
        layout2=findViewById(R.id.signInOrUpSubLayout2);
        errorLayout=findViewById(R.id.signInOrUpSubLayout3);

        emailEditTex = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        signInButton = findViewById(R.id.signInButton);

        signUpButton = findViewById(R.id.signUp);
        nameEditText = findViewById(R.id.nameEditText);
        submitNameButton = findViewById(R.id.submitNameButton);
        errorTextView = findViewById(R.id.errorTextView);
        retryButton=findViewById(R.id.retryButtonSignInUpActivity);
    }
    private void showLayout1(){
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }
    private void signIn() {
        firebaseAuth.signInWithEmailAndPassword(emailEditTex.getText().toString(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                       checkIfIsNotBusinessAccount(authResult.getUser());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to sign in");
                showErrorLayout(R.string.invalid_email_or_password,GoBackTo.SIGN_IN_UP);
            }
        });
    }
    private void showErrorLayout(int resourceString, final GoBackTo goBackTo){
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorTextView.setText(resourceString);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(goBackTo){
                    case SUBMIT_USER_NAME: showLayout2();break;
                    case SIGN_IN_UP: showLayout1();break;
                }
            }
        });
    }
    private void checkIfIsNotBusinessAccount(FirebaseUser firebaseUser){
        firebaseFirestore.collection("Shops").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if(!snapshot.contains("ShopUid")){
                            goToMainActivity();
                        }else{
                            showErrorLayout(R.string.invalid_email_or_password,GoBackTo.SIGN_IN_UP);
                            firebaseAuth.signOut();
                            Log.v("MyFirebase", "is business account not allowed");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to check if account is client");
                showErrorLayout(R.string.something_went_wrong_we_could_not_sign_you_in,GoBackTo.SIGN_IN_UP);
                firebaseAuth.signOut();
            }
        });

    }
    private void goToMainActivity(){
        Intent goToMainActivity = new Intent(mContext, MainActivity.class);
        startActivity(goToMainActivity);
        finish();
    }
    private void signUp() {
        firebaseAuth.createUserWithEmailAndPassword(emailEditTex.getText().toString(), passwordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseUser = authResult.getUser();
                        showLayout2();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to sign up");
            }
        });
    }

    private void showLayout2(){
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }
/*
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
*/
    private void setTheName() {
        if (firebaseUser != null) {
            String name = nameEditText.getText().toString();
            if (name.length() > 3) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();
                firebaseUser.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       goToMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("MyFirebase", "Failed to sign up");
                    }
                });
            } else {
                showErrorLayout(R.string.invalid_name,GoBackTo.SUBMIT_USER_NAME);
            }
        }
    }
}