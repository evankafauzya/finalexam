package com.example.finalexam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<Subject> subjectList = new ArrayList<>();
    private List<Subject> selectedSubjects = new ArrayList<>();
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button summaryButton = findViewById(R.id.summaryButton);

        adapter = new SubjectAdapter(subjectList, new SubjectAdapter.OnSubjectClickListener() {
            @Override
            public void onSubjectClick(Subject selectedSubject) {
                db.collection("students").document(auth.getCurrentUser().getUid()).get()
                        .addOnSuccessListener(document -> {
                            int totalCredits = document.getLong("totalCredits").intValue();

                            if (selectedSubjects.contains(selectedSubject)) {
                                // remove logic handle
                                selectedSubjects.remove(selectedSubject);
                                db.collection("students").document(auth.getCurrentUser().getUid())
                                        .update(
                                                "enrolledSubjects", FieldValue.arrayRemove(selectedSubject),
                                                "totalCredits", totalCredits - 3
                                        ).addOnSuccessListener(aVoid -> {
                                            Toast.makeText(EnrollmentActivity.this, "Subject removed!", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // credits > 24 handle
                                if (totalCredits + 3 > 24) {
                                    Toast.makeText(EnrollmentActivity.this, "Credit limit exceeded!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // select logic handle
                                    selectedSubjects.add(selectedSubject);
                                    db.collection("students").document(auth.getCurrentUser().getUid())
                                            .update(
                                                    "enrolledSubjects", FieldValue.arrayUnion(selectedSubject),
                                                    "totalCredits", totalCredits + 3
                                            ).addOnSuccessListener(aVoid -> {
                                                Toast.makeText(EnrollmentActivity.this, "Subject added!", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }
                            adapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EnrollmentActivity.this, "Failed to update selection: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }, selectedSubjects);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db.collection("subjects").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    subjectList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Subject subject = doc.toObject(Subject.class);
                        subjectList.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EnrollmentActivity.this, "Failed to fetch subjects: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        summaryButton.setOnClickListener(v -> {
            startActivity(new Intent(EnrollmentActivity.this, TotalActivity.class));
        });
    }
}
