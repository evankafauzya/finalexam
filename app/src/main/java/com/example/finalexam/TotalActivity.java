package com.example.finalexam;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class TotalActivity extends AppCompatActivity  {
    private FirebaseFirestore db;
    private FirebaseAuth auth;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_total);

    db = FirebaseFirestore.getInstance();
    auth = FirebaseAuth.getInstance();

    TextView subjectList = findViewById(R.id.subjectList);
    TextView totalCredits = findViewById(R.id.totalCredits);

    db.collection("students").document(auth.getCurrentUser().getUid()).get()
            //show total subject selected & credits
            .addOnSuccessListener(document -> {
                List<Map<String, Object>> subjects = (List<Map<String, Object>>) document.get("enrolledSubjects");
                int credits = document.getLong("totalCredits").intValue();

                StringBuilder subjectsText = new StringBuilder();
                for (Map<String, Object> subject : subjects) {
                    subjectsText.append(subject.get("subjectName")).append("\n");
                }

                subjectList.setText(subjectsText.toString());
                totalCredits.setText("Total Credits: " + credits);
            });
}
}