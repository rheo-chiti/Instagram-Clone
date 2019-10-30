package com.example.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPhoto extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("photos");

    private List<Photo> photoList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ViewPhotoArrayAdapter arrayAdapter;

    private String userName, imageURL, timeStamp, likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        recyclerView = findViewById(R.id.recycler);
        createList();
    }

    private void createList() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    userName = document.get("username").toString();
                    imageURL = document.get("imageURL").toString();
                    timeStamp = document.get("timeStamp").toString();
                    try {
                        likes = document.get("likes").toString();
                    } catch (NullPointerException e) {
                        Log.d("rheo", "onSuccess: " + likes);
                    }
                    photoList.add(new Photo(userName, imageURL, Long.parseLong(timeStamp), Long.parseLong(likes)));
                }
                if (photoList.size() != 0) {
                    Log.d("rheo", "onSuccess: " + photoList.size());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewPhoto.this));
                    arrayAdapter = new ViewPhotoArrayAdapter(ViewPhoto.this, photoList);
                    recyclerView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(ViewPhoto.this, MainActivity.class));
                finish();
                break;
            case R.id.addPhoto:
                startActivity(new Intent(ViewPhoto.this, AddPhoto.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
