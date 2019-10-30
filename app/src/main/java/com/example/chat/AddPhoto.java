package com.example.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPhoto extends AppCompatActivity {

    private ImageButton imageButton;
    private ImageView imageView;
    private Button button;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("photos");

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        imageButton = findViewById(R.id.AddPhotoImageButton);
        imageView = findViewById(R.id.AddPhotoImage);
        button = findViewById(R.id.AddPhotoUpload);

        currentUser = firebaseAuth.getCurrentUser();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Photo photo = new Photo();
                String email = currentUser.getEmail();
                String userName = email.substring(0, email.length() - 10);
                photo.setUsername(userName);
                photo.setTimeStamp(System.currentTimeMillis());
                storageReference.child(String.valueOf(photo.getTimeStamp())).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child(String.valueOf(photo.getTimeStamp())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                photo.setImageURL(uri.toString());
                                collectionReference.document(String.valueOf(photo.getTimeStamp())).set(photo);
                                startActivity(new Intent(AddPhoto.this, ViewPhoto.class));
                                finish();
                            }
                        });
                    }
                });

            }
        });

        if (ContextCompat.checkSelfPermission(AddPhoto.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(AddPhoto.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedImage = getResizedBitmap(selectedImage, 800);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), selectedImage, "Title", null);
            filepath = Uri.parse(path);
            imageView.setImageURI(filepath);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
