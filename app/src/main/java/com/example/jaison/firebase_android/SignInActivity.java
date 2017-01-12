package com.example.jaison.firebase_android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.password)
    EditText password;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    handleSignedInUser(user);
                } else {
                    handleSignedOutUser();
                }
            }
        };
    }

    private void handleSignedInUser(FirebaseUser user) {
        ToDoActivity.startActivity(this);
    }

    private void handleSignedOutUser() {

    }


    @OnClick({R.id.signInButton, R.id.signUpButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInButton:
                auth.signInWithEmailAndPassword(emailId.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    handleSignedInUser(auth.getCurrentUser());
                                } else {
                                    Log.i("tag",task.toString());
                                    showErrorAlert(task.getException().getLocalizedMessage(), null);
                                }
                            }
                        });
                break;
            case R.id.signUpButton:
                auth.createUserWithEmailAndPassword(emailId.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    handleSignedInUser(auth.getCurrentUser());
                                } else {
                                    Log.i("tag",task.toString());
                                    showErrorAlert(task.getException().getLocalizedMessage(), null);
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            auth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
