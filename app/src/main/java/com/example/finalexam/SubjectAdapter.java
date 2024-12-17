package com.example.finalexam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private final List<Subject> subjectList;
    private final OnSubjectClickListener listener;
    private final List<Subject> selectedSubjects;

    public SubjectAdapter(List<Subject> subjectList, OnSubjectClickListener listener, List<Subject> selectedSubjects) {
        this.subjectList = subjectList;
        this.listener = listener;
        this.selectedSubjects = selectedSubjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

        holder.subjectName.setText(subject.getSubjectName());
        holder.subjectCredits.setText("Credits: " + subject.getCredits());

        // Update button text (remove / select)
        if (selectedSubjects.contains(subject)) {
            holder.selectButton.setText("Remove");
        } else {
            holder.selectButton.setText("Select");
        }

        // Handle button click
        holder.selectButton.setOnClickListener(v -> listener.onSubjectClick(subject));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public interface OnSubjectClickListener {
        void onSubjectClick(Subject selectedSubject);
    }

    // ViewHolder class for adapter
    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectCredits;
        Button selectButton;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectCredits = itemView.findViewById(R.id.subjectCredits);
            selectButton = itemView.findViewById(R.id.selectButton);
        }
    }
}
