package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ViewPhotoArrayAdapter extends Adapter<ViewPhotoArrayAdapter.ViewHolder> {

    private Context context;
    private List<Photo> photoList;

    public ViewPhotoArrayAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photoList.get(photoList.size() - position - 1);


        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        holder.currentTimestamp = photo.getTimeStamp();

        holder.userName.setText(photo.getUsername());
        holder.time.setText(sdf.format(photo.getTimeStamp()));
        Picasso.get().load(photo.getImageURL()).placeholder(R.drawable.common_google_signin_btn_icon_light).into(holder.imageView);
        holder.likes.setText(String.valueOf(photo.getLikes()));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, time, likes;
        public ImageView imageView, like, comment;
        public int cnt = 0;
        public Long currentTimestamp, likes_count;

        private FirebaseAuth auth = FirebaseAuth.getInstance();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private CollectionReference collectionReference = db.collection("photos");
        private FirebaseUser currentUser;

        private String username, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.postUsername);
            time = itemView.findViewById(R.id.postTime);
            imageView = itemView.findViewById(R.id.postImage);
            like = itemView.findViewById(R.id.postLike);
            likes = itemView.findViewById(R.id.postLikes);
            comment = itemView.findViewById(R.id.postComment);

            currentUser = auth.getCurrentUser();
            email = currentUser.getEmail();
            username = email.substring(0, email.length() - 10);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cnt % 2 == 1) {
                        like.setImageResource(R.drawable.heart_filled);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user", currentUser.getEmail());
                        collectionReference.document(String.valueOf(currentTimestamp)).collection("likes").document(username).set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        collectionReference.document(String.valueOf(currentTimestamp)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                likes_count = (Long) documentSnapshot.get("likes");
                                                likes_count += 1;
                                                collectionReference.document(String.valueOf(currentTimestamp)).update("likes", likes_count);
                                            }
                                        });
                                    }
                                });
                    } else {
                        like.setImageResource(R.drawable.heart_empty);
                        collectionReference.document(String.valueOf(currentTimestamp)).collection("likes").document(username).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("rheo", "onSuccess: " + "disliked");
                                        collectionReference.document(String.valueOf(currentTimestamp)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                likes_count = (Long) documentSnapshot.get("likes");
                                                likes_count -= 1;
                                                collectionReference.document(String.valueOf(currentTimestamp)).update("likes", likes_count);
                                            }
                                        });
                                    }
                                });
                    }
                    cnt++;
                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewComments.class);
                    intent.putExtra("timeStamp", currentTimestamp.toString());
                    context.startActivity(intent);
                }
            });

        }

    }
}
