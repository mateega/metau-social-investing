package com.example.project.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OVERVIEW FRAGMENT";
    private String userName = "";
    private String groupName = "";
    private String memberCount;
    private String groupAssetCount;
    private String personalAssetsCount;
    private String recentTradeDate;
    FirebaseFirestore db;

    TextView tvHello;
    TextView tvWelcomeBack;
    TextView tvMembersCount;
    TextView tvGroupAssetsCount;
    TextView tvPersonalAssetsCount;
    TextView tvRecentTradeDate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
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
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHello = view.findViewById(R.id.tvHello);
        tvWelcomeBack = view.findViewById(R.id.tvWelcomeBack);
        tvMembersCount = view.findViewById(R.id.tvMembersCount);
        tvGroupAssetsCount = view.findViewById(R.id.tvGroupAssetsCount);
        tvPersonalAssetsCount = view.findViewById(R.id.tvPersonalAssetsCount);
        tvRecentTradeDate = view.findViewById(R.id.tvRecentTradeDate);

        tvHello.setText("Hello");
        tvWelcomeBack.setText("Welcome Back!");
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference docRef = db.collection("users").document(user.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            Log.d(TAG, "DocumentSnapshot data: " + data);

                            userName = data.get("name").toString();
                            tvHello.setText("Hello, " + userName);

                            personalAssetsCount = String.valueOf((Long) data.get("assets"));
                            if (personalAssetsCount.equals("0")) {
                                personalAssetsCount = "$0.00";
                                tvPersonalAssetsCount.setText(personalAssetsCount);
                            } else {
                                double amount = Double.parseDouble(personalAssetsCount);
                                DecimalFormat formatter = new DecimalFormat("#,###.00");
                                personalAssetsCount = "$" + formatter.format(amount);
                                tvPersonalAssetsCount.setText(personalAssetsCount);
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

                            groupName = data.get("name").toString();
                            tvWelcomeBack.setText("Welcome Back to " + groupName + "!");

                            ArrayList<Reference> list = (ArrayList) data.get("members");
                            int groupSize = list.size();
                            memberCount = String.valueOf(groupSize);
                            tvMembersCount.setText(memberCount);

                            Long groupAssets = (Long) data.get("assets");
                            groupAssetCount = String.valueOf(groupAssets);
                            double amount = Double.parseDouble(groupAssetCount);
                            DecimalFormat formatter = new DecimalFormat("#,###.00");
                            groupAssetCount = "$" + formatter.format(amount);
                            tvGroupAssetsCount.setText(groupAssetCount);

                            ArrayList<Map<String, Object>> trades = (ArrayList) data.get("trades");
                            int numTrades = trades.size();
                            Map<String, Object> trade = trades.get(numTrades - 1);
                            Timestamp time = (Timestamp) trade.get("time");

                            Date javaDate = time.toDate();
                            DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                            recentTradeDate = dateFormat.format(javaDate);
                            tvRecentTradeDate.setText(recentTradeDate);

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