package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.HistoryAdapter;
import com.example.myapplication.HistoryItem;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name, mail;
    Button navigatebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.username);
        mail = findViewById(R.id.usermail);
        navigatebt = findViewById(R.id.navigatebt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter();
        historyRecyclerView.setAdapter(historyAdapter);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String Name = account.getDisplayName();
            String Mail = account.getEmail();

            name.setText(Name);
            mail.setText(Mail);

            // Fetch and display history data
            fetchHistoryData(Mail);
        }

        navigatebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Imgpage.class);
                startActivity(intent);
            }
        });
    }

    private void fetchHistoryData(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference historyRef = db.collection("users")
                .document(userEmail)
                .collection("history");

        historyRef.addSnapshotListener(this, (value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            // A new document was added
                            HistoryItem historyItem = dc.getDocument().toObject(HistoryItem.class);
                            historyAdapter.addHistoryItem(historyItem);
                            break;
                        // Handle other cases if needed (modified, removed)
                    }
                }
            }
        });
    }
}
