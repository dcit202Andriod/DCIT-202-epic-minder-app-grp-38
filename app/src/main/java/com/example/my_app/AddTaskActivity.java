package com.example.my_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.my_app.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class   AddTaskActivity extends AppCompatActivity {
    EditText etTaskInput;
    Button saveBtn;
    FirebaseFirestore db;

    String TAG = "myAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
       db = FirebaseFirestore.getInstance();

        saveBtn= findViewById(R.id.taskSaveBtn);
        etTaskInput = findViewById(R.id.inputTaskName);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = etTaskInput.getText().toString().trim();
                if(taskName!=null){
//                    Toast.makeText(AddTaskActivity.this, taskName, Toast.LENGTH_SHORT).show();
                    String taskId;
                    TaskModel TaskModel = new TaskModel(  "" ,taskName,  "PENDING", FirebaseAuth.getInstance().getUid()  );
                    db.collection("tasks").add(TaskModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                findViewById(R.id.successLayout).setVisibility(View.VISIBLE);
                                    findViewById(R.id.addTaskLayout).setVisibility(View.GONE);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            }
        });


        ImageView leftIcon = findViewById(R.id.left_icon);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddTaskActivity.this, HomeActivity.class));
            };
    });
}};