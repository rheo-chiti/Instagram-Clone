package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewComments extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("photos");
    private FirebaseUser currentUser;

    private EditText commentEditText;
    private Button commentPostButton;

    private String timeStamp, currentTimeStamp, username, email, commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        Intent intent = getIntent();
        timeStamp = intent.getStringExtra("timeStamp");

        commentEditText = findViewById(R.id.viewCommentText);
        commentPostButton = findViewById(R.id.postCommentButton);

        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimeStamp = String.valueOf(System.currentTimeMillis());
                currentUser = auth.getCurrentUser();
                email = currentUser.getEmail();
                username = email.substring(0, email.length() - 10);
                commentText = commentEditText.getText().toString();

                if (commentText.length() != 0) {
                    Comment comment = new Comment();
                    comment.setTimeStamp(currentTimeStamp);
                    comment.setUserName(username);
                    comment.setCommentText(commentText);
                    collectionReference.document(timeStamp).collection("comment").document(currentTimeStamp).set(comment);
                } else
                    Snackbar.make(view, "Fielda can't be empty", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
