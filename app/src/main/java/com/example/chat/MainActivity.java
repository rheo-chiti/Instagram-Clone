package com.example.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword;
    private String userName, password;
    private Button login, getStarted;

    private Boolean flag = true;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("users");
    private FirebaseUser currentUser;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, ViewPhoto.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        editTextUserName = findViewById(R.id.loginUsernameEditText);
        editTextPassword = findViewById(R.id.loginUsernamePassword);
        login = findViewById(R.id.loginLoginButton);
        getStarted = findViewById(R.id.loginSignUpButton);

        dialog = new ProgressDialog(this);
        dialog.setMessage("By the orders of Peaky Fookin Blinders");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.setTitle("Setting u up");
                dialog.show();
                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                if (userName.length() == 0 || password.length() == 0) {
                    dialog.dismiss();
                    Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Snackbar.make(view, "Password size should be more than 8", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            flag = true;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if (document.get("users").equals(userName)) {
                                    flag = false;
                                    Log.d("rheo", "onSuccess: " + flag + document.get("users"));
                                    break;
                                }
                            }
                            if (flag) {
                                dialog.dismiss();
                                String email = userName + "@gmail.com";
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("users", userName);
                                        collectionReference.document(userName).set(map);
                                        Snackbar.make(view, "Registered Sucessfully", Snackbar.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, ViewPhoto.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(view, "Some Error Occured", Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                dialog.dismiss();
                                Snackbar.make(view, "User already exists", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.setTitle("Logging in");
                dialog.show();
                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                if (userName.length() == 0 || password.length() == 0) {
                    Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String email = userName + "@gmail.com";
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(view, "logged in successfully", Snackbar.LENGTH_SHORT).show();
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, ViewPhoto.class));
                                finish();
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(view, "wrong username or password", Snackbar.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

    }
}
