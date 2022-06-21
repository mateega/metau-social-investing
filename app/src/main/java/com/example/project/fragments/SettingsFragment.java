package com.example.project.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project.InvestorAdapter;
import com.example.project.LoginActivity;
import com.example.project.MemberAdapter;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SETTINGS FRAGMENT";

    private Button btnSignout;
    private FirebaseFirestore db;
    private RecyclerView rvInvestors;
    private RecyclerView rvMembers;
    protected MemberAdapter memberAdapter;
    protected InvestorAdapter investorAdapter;
    protected List<String> members;
    protected List<String> investors;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSignout = view.findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        rvMembers = view.findViewById(R.id.rvMembers);
        rvInvestors = view.findViewById(R.id.rvInvestors);
        members = new ArrayList<>();
        investors = new ArrayList<>();
        memberAdapter = new MemberAdapter(getContext(), members);
        investorAdapter = new InvestorAdapter(getContext(), investors);
        rvMembers.setAdapter(memberAdapter);
        rvInvestors.setAdapter(investorAdapter);
        LinearLayoutManager memberLinearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager investorLinearLayoutManager = new LinearLayoutManager(getContext());
        rvMembers.setLayoutManager(memberLinearLayoutManager);
        rvInvestors.setLayoutManager(investorLinearLayoutManager);


        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String groupId = getActivity().getIntent().getStringExtra("groupName");
        if (groupId != null) {
            DocumentReference docRef = db.collection("groups").document(groupId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            Log.d(TAG, "DocumentSnapshot data: " + data);
                            ArrayList<String> dbMembers = (ArrayList) data.get("members");
                            ArrayList<String> dbTraders = (ArrayList) data.get("traders");

                            Log.d(TAG, "Members: " + dbMembers);
                            Log.d(TAG, "Traders: " + dbTraders);

                            if (dbMembers != null || dbMembers.get(0).equals("")) {
                                members.addAll(dbMembers);
                                memberAdapter.notifyDataSetChanged();
                            }
                            if (dbTraders != null || dbTraders.get(0).equals("")) {
                                investors.addAll(dbTraders);
                                investorAdapter.notifyDataSetChanged();
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
    }

}