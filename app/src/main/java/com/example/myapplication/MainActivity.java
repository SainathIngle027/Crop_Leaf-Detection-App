package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Button navigateButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Login Button
        navigateButton = findViewById(R.id.googlebt);

        // Initialize Firebase Authentication
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        //Init Python Backend Services
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImgpage();
            }
        });
    }

    private void openImgpage() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent , 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Check if the user's email exists in Firestore
                checkIfUserExists(account.getEmail());
            } catch (ApiException e) {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkIfUserExists(String email) {
        // Assuming 'db' is your Firestore instance
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // User does not exist, create a new document
                            createUserDocument(email);
                        } else {
                            // User already exists, navigate to home page
                            HomePage();
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(this, "Error checking user existence", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserDocument(String email) {
        // Assuming 'db' is your Firestore instance
        db.collection("users")
                .document(email)
                .set(new HashMap<>())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // New user document created, navigate to home page
                        HomePage();
                    } else {
                        // Handle errors
                        Toast.makeText(this, "Error creating user document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void HomePage() {
        finish();
        Intent intent =new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

}