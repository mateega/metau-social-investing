package com.example.project.fragments;

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
import android.widget.TextView;

import com.example.project.HoldingAdapter;
import com.example.project.MemberAdapter;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private RecyclerView rvHoldings;
    protected List<ArrayList<String>> holdings;
    protected HoldingAdapter holdingAdapter;

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

                            setUsername(data);
                            setPersonalAssets(data);

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

        rvHoldings = view.findViewById(R.id.rvHoldings);
        holdings = new ArrayList<>();
        holdingAdapter = new HoldingAdapter(getContext(), holdings, groupId);
        rvHoldings.setAdapter(holdingAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvHoldings.setLayoutManager(linearLayoutManager);

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

                            setGroupName(data);
                            setMemberCount(data);
                            setGroupAssets(data);
                            setRecentTrade(data);
                            setHoldings(data);

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

    private void setHoldings(Map<String, Object> data) {
        ArrayList<Map<String, Object>> trades = (ArrayList) data.get("trades");
        HashMap<String, ArrayList<String>> tradesMap = new HashMap<String, ArrayList<String>>();
        // trade array list format: [ticker, qnt. in crypto, qnt. in USD]

        for (int i = 0; i < trades.size(); i++) {
            Map<String, Object> trade = trades.get(i);
            String tradeDirection = trade.get("direction").toString();
            Double tradeLot = Double.valueOf(trade.get("lot").toString());
            Double tradePrice = Double.valueOf(trade.get("price").toString());
            String tradeTicker = trade.get("ticker").toString();

            if (tradesMap.containsKey(tradeTicker)) {
                ArrayList<String> oldTrade = tradesMap.get(tradeTicker);
                String oldLot = oldTrade.get(1);
                String oldSizeUSD = oldTrade.get(2);

                if (tradeDirection.equals("buy")) {
                    String newTradeLot = String.valueOf(Double.parseDouble(oldLot) + tradeLot);
                    String newTradeSizeUSD = String.valueOf(Double.parseDouble(oldSizeUSD) + (tradeLot * tradePrice));

                    ArrayList<String> tradeList = new ArrayList<String>();
                    tradeList.add(tradeTicker);
                    tradeList.add(newTradeLot);
                    tradeList.add(newTradeSizeUSD);

                    tradesMap.replace(tradeTicker, tradeList);
                }
            } else {
                if (tradeDirection.equals("buy")) {
                    ArrayList<String> tradeList = new ArrayList<String>();
                    String tradeSizeUSD = String.valueOf(tradeLot * tradePrice);
                    tradeList.add(tradeTicker);
                    tradeList.add(String.valueOf(tradeLot));
                    tradeList.add(tradeSizeUSD);
                    tradesMap.put(tradeTicker, tradeList);
                }
            }
        }
        holdings.addAll(tradesMap.values());
        holdingAdapter.notifyDataSetChanged();

    }

    private void setGroupName(Map<String, Object> data) {
        groupName = data.get("name").toString();
        tvWelcomeBack.setText("Welcome Back to " + groupName + "!");
    }

    private void setRecentTrade(Map<String, Object> data) {
        ArrayList<Map<String, Object>> trades = (ArrayList) data.get("trades");
        int numTrades = trades.size();
        Map<String, Object> trade = trades.get(numTrades - 1);
        Timestamp time = (Timestamp) trade.get("time");
        Date javaDate = time.toDate();
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        recentTradeDate = dateFormat.format(javaDate);
        tvRecentTradeDate.setText(recentTradeDate);
    }

    private void setGroupAssets(Map<String, Object> data) {
        Long groupAssets = (Long) data.get("assets");
        groupAssetCount = String.valueOf(groupAssets);
        double amount = Double.parseDouble(groupAssetCount);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        groupAssetCount = "$" + formatter.format(amount);
        tvGroupAssetsCount.setText(groupAssetCount);
    }

    private void setMemberCount(Map<String, Object> data) {
        ArrayList<Reference> list = (ArrayList) data.get("members");
        int groupSize = list.size();
        memberCount = String.valueOf(groupSize);
        tvMembersCount.setText(memberCount);
    }

    private void setUsername(Map<String, Object> data) {
        userName = data.get("name").toString();
        tvHello.setText("Hello, " + userName);
    }

    private void setPersonalAssets(Map<String, Object> data) {
        personalAssetsCount = String.valueOf((Double) data.get("assets"));
        if (personalAssetsCount.equals("0")) {
            personalAssetsCount = "$0.00";
            tvPersonalAssetsCount.setText(personalAssetsCount);
        } else {
            double amount = Double.parseDouble(personalAssetsCount);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            personalAssetsCount = "$" + formatter.format(amount);
            tvPersonalAssetsCount.setText(personalAssetsCount);
        }
    }

}