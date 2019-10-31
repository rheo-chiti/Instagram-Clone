package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewComments extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("photos");
    private FirebaseUser currentUser;

    private EditText commentEditText;
    private Button commentPostButton;
    private RecyclerView recyclerView;
    private ViewCommentAdapter viewCommentAdapter;

    private String timeStamp, currentTimeStamp, username, email, commentText;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        Intent intent = getIntent();
        timeStamp = intent.getStringExtra("timeStamp");

        commentEditText = findViewById(R.id.viewCommentText);
        commentPostButton = findViewById(R.id.postCommentButton);
        recyclerView = findViewById(R.id.recyclerView);

        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimeStamp = String.valueOf(System.currentTimeMillis());
                currentUser = auth.getCurrentUser();
                email = currentUser.getEmail();
                username = email.substring(0, email.length() - 10);
                commentText = commentEditText.getText().toString();

                if (commentText.length() != 0) {
                    final Comment comment = new Comment();
                    comment.setTimeStamp(currentTimeStamp);
                    comment.setUserName(username);
                    comment.setCommentText(commentText);
                    collectionReference.document(timeStamp).collection("comment").document(currentTimeStamp).set(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                commentList.add(comment);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ViewComments.this));
                                viewCommentAdapter = new ViewCommentAdapter(ViewComments.this, commentList);
                                recyclerView.setAdapter(viewCommentAdapter);
                                viewCommentAdapter.notifyDataSetChanged();

                            }
                        }
                    });
                } else
                    Snackbar.make(view, "Fields can't be empty", Snackbar.LENGTH_SHORT).show();
            }
        });

        createList();

    }

    private void createList() {
        commentList = new ArrayList<>();
        collectionReference.document(timeStamp).collection("comment").
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Comment comment = new Comment();
                    comment.setCommentText(queryDocumentSnapshot.get("commentText").toString());
                    comment.setUserName(queryDocumentSnapshot.get("userName").toString());
                    comment.setTimeStamp(queryDocumentSnapshot.get("timeStamp").toString());
                    commentList.add(comment);
                }

                try {
                    if (commentList.size() != 0) {
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewComments.this));
                        viewCommentAdapter = new ViewCommentAdapter(ViewComments.this, commentList);
                        recyclerView.setAdapter(viewCommentAdapter);
                        viewCommentAdapter.notifyDataSetChanged();
                    }
                } catch (NullPointerException e) {

                }
            }
        });
    }
}
