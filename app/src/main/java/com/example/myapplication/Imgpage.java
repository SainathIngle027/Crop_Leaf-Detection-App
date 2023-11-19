package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


public class Imgpage extends AppCompatActivity {

    ImageView preimg;
    private Uri imguri;

    String imageUrl = null;

    CardView ImgPicker,resultcard;
    private ProgressBar progressBar;
    TextView result,cardname;

    Button predict , knowinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgpage);
        FirebaseApp.initializeApp(this);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);

        getPremission();
        resultcard = findViewById(R.id.resultcard);
        preimg =findViewById(R.id.preimg);
        ImgPicker = findViewById(R.id.ImgPicker);
        result=findViewById(R.id.result);
        knowinfo=findViewById(R.id.knowinfo);
        cardname=findViewById(R.id.cardname);
        knowinfo.setVisibility(View.INVISIBLE);

        cardname.setText("Select Image");

        ImgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardname.setText("");

                ImagePicker.with(Imgpage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(720)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imguri = data.getData();
        preimg.setImageURI(imguri);
        result.setText("Wait For A While");
        uploadImageToFirebase();

    }

    private void uploadImageToFirebase() {
        if (imguri != null) {
            progressBar.setVisibility(View.VISIBLE);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString()); // Define a unique path for the image.

            imageRef.putFile(imguri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully.
                        // You can retrieve the download URL here.
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUrl = uri.toString();
                            cardname.setText("Image Uploaed");
                            callpython();

                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure.
                        cardname.setText("Image Not Uploaded");
                    });
        } else {
            // Handle the case when imguri is null (no image selected).
            cardname.setText("Image Not Selected");
        }
    }

    private void callpython() {

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("AzureVision");
        PyObject obj = pyobj.callAttr("main", imageUrl);
        cardname.setText("Disease Detected As");
        progressBar.setVisibility(View.GONE);
        result.setText(obj.toString().toUpperCase());


    }

    private void getPremission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(Imgpage.this,new String[] {Manifest.permission.CAMERA},11);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11)
        {
            if(grantResults.length>0)
            {
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    this.getPremission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}