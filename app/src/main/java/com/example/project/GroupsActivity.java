package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupsActivity extends AppCompatActivity {

    private static final String TAG = "GROUPS ACTIVITY";
    private final String GROUP_1_ID = "penn_crypto";
    private final String GROUP_2_ID = "columbia_blockchain";
    private final String GROUP_3_ID = "tom's_alt_coin_fund";

    LinearLayout layGroup1;
    LinearLayout layGroup2;
    LinearLayout layGroup3;
    TextView tvGroupAssets1;
    TextView tvGroupAssets2;
    TextView tvGroupAssets3;
    ImageView ivCheck1;
    ImageView ivCheck2;
    ImageView ivCheck3;

    FirebaseFirestore db;
    FirebaseUser user;
    String userId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_groups);
        this.getSupportActionBar().hide();

        layGroup1 = findViewById(R.id.layGroup1);
        layGroup2 = findViewById(R.id.layGroup2);
        layGroup3 = findViewById(R.id.layGroup3);

        tvGroupAssets1 = findViewById(R.id.tvGroupAssets1);
        tvGroupAssets2 = findViewById(R.id.tvGroupAssets2);
        tvGroupAssets3 = findViewById(R.id.tvGroupAssets3);

        ivCheck1 = findViewById(R.id.ivCheck1);
        ivCheck2 = findViewById(R.id.ivCheck2);
        ivCheck3 = findViewById(R.id.ivCheck3);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getEmail();

        updateGroupAssetCount(tvGroupAssets1, GROUP_1_ID, 1);
        updateGroupAssetCount(tvGroupAssets2, GROUP_2_ID, 2);
        updateGroupAssetCount(tvGroupAssets3, GROUP_3_ID, 3);


        layGroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group = GROUP_1_ID;
                String groupName = GROUP_1_NAME;
                if (!(Boolean)map.get(group).get("inGroup")){
                    addUserToGroup(group);
                }
                pullGroupData(group, groupName);
            }
        });
        layGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group = GROUP_2_ID;
                addUserToGroup(group);
                goToGroup(group);
            }
        });
        layGroup3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group = GROUP_3_ID;
                addUserToGroup(group);
                goToGroup(group);
            }
        });

        DocumentReference docRef = db.collection("users").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();

                        userName = data.get("name").toString();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateGroupAssetCount(TextView tvGroupAssets, String groupId, int group) {
        DocumentReference docRef = db.collection("groups").document(groupId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getMetadata().isFromCache()) {
                    Log.i(TAG, "CALLED DATA FROM CACHE");
                } else {
                    Log.i(TAG, "CALLED FIREBASE DATABASE -- GROUPS");
                }
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);

                        Long groupAssets = (Long) data.get("assets");
                        String groupAssetCount = String.valueOf(groupAssets);
                        double amount = Double.parseDouble(groupAssetCount);
                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        groupAssetCount = "$" + formatter.format(amount);
                        tvGroupAssets.setText(groupAssetCount);
                        Log.i("ASSETS: ", groupAssetCount);

                        // add check by group name if user is in group
                        Boolean memberInGroup = false;
                        ArrayList<String> members = (ArrayList) data.get("members");
                        for (String member:members){
                            if (userId.equals(member)){
                                memberInGroup = true;
                            }
                        }
                        if (!memberInGroup) {
                            switch(group) {
                                case 1:
                                    ivCheck1.setVisibility(View.GONE);
                                    return;
                                case 2:
                                    ivCheck2.setVisibility(View.GONE);
                                    return;
                                case 3:
                                    ivCheck3.setVisibility(View.GONE);
                                    return;
                                default:
                                    Log.i(TAG, "invalid group");
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void addUserToGroup(String groupId) {
        DocumentReference docRef = db.collection("groups").document(groupId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getMetadata().isFromCache()) {
                    Log.i(TAG, "CALLED DATA FROM CACHE");
                } else {
                    Log.i(TAG, "CALLED FIREBASE DATABASE -- GROUPS");
                }
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);

                        Boolean memberInGroup = false;
                        ArrayList<String> members = (ArrayList) data.get("members");
                        for (String member:members){
                           if (userId.equals(member)){
                               memberInGroup = true;
                           }
                        }
                        // member is not in group
                        if (!memberInGroup) {
                            members.add(userId);
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("members", members);
                            db.collection("groups").document(groupId).set(updatedData, SetOptions.merge());
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void goToGroup(String groupName) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("groupName", groupName);
        i.putExtra("userName", userName);
        startActivity(i);
    }



}