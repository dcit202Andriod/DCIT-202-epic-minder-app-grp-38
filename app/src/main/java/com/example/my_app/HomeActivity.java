package com.example.my_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_app.model.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    RecyclerView taskRv;
    ArrayList<TaskModel> dataList = new ArrayList<>();
    TaskListAdapter  taskListAdapter;

    FirebaseFirestore db;
    TextView userNameTv;
    CircleImageView userImageIv;

    String TAG = "Homepage query docs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db=FirebaseFirestore.getInstance();
        taskRv = findViewById(R.id.taskListRv);
        userNameTv=findViewById(R.id.userNameTv);
        userImageIv=findViewById(R.id.userProfileIv);


        userImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImageIv);
//        userNameTv.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        FirebaseUser currentUserUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUserUser != null && currentUserUser.getDisplayName() != null) {
            String displayName = currentUserUser.getDisplayName();
            userNameTv.setText(displayName);
        }
        String taskId;
        String taskName;
        String taskStatus;
        String userId;


//        dataList.add(new TaskModel("testId" ,"Study Comp. Science","completed"));
        taskListAdapter = new TaskListAdapter(dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false
        );
        taskRv.setLayoutManager(layoutManager);
        taskRv.setAdapter(taskListAdapter);

        findViewById(R.id.addTaskFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddTaskActivity.class));
            };




        });
        findViewById(R.id.leftSecond_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            };

        });

        db.collection( "tasks")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                TaskModel taskModel= document.toObject(TaskModel.class);
                                taskModel.setTaskId(document.getId());
                                dataList.add(taskModel);
                                taskListAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }};
