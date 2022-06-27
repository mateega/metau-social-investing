package com.example.project.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradePaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradePaymentFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String TAG = "TRADE PAYMENT FRAGMENT";
    private FirebaseFirestore db;
    String userId;

    String name;
    String ticker;
    String price;
    String priceChange;
    String rank;
    String imageUrl;

    ImageButton ibBack;

    TextView tvCurrentAccountAssets;
    TextView tvBuyAmount;

    TextView tvBuy;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn0;

    Button btnDot;
    ImageButton btnDelete;

    Button btnCancel;
    Button btnBuy;

    Double buyAmount;
    String buyAmountStr;
    int numLocation;
    String currentAssets;
    Double assets;

    Bundle bundle;

    public TradePaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradePaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradePaymentFragment newInstance(String param1, String param2) {
        TradePaymentFragment fragment = new TradePaymentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name = getArguments().getString("name");
        ticker = getArguments().getString("ticker");
        price = getArguments().getString("price");
        priceChange = getArguments().getString("priceChange");
        rank = getArguments().getString("rank");
        imageUrl = getArguments().getString("imageUrl");

        bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("ticker", ticker);
        bundle.putString("price", price);
        bundle.putString("priceChange", price);
        bundle.putString("rank", rank);
        bundle.putString("imageUrl", imageUrl);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentAssets = "";

        ibBack = view.findViewById(R.id.ibBack);

        tvCurrentAccountAssets = view.findViewById(R.id.tvCurrentAccountAssets);
        getCurrentAssets();

        tvBuyAmount = view.findViewById(R.id.tvBuyAmount);
        tvBuyAmount.setText(buyAmountStr);

        buyAmount = 0.00;
        setBuyAmount(buyAmount);
        numLocation = 0;

        tvBuy = view.findViewById(R.id.tvBuy);
        tvBuy.setText("Buy " + name);

        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        btn7 = view.findViewById(R.id.btn7);
        btn8 = view.findViewById(R.id.btn8);
        btn9 = view.findViewById(R.id.btn9);
        btn0 = view.findViewById(R.id.btn0);

        btnDot = view.findViewById(R.id.btnDot);
        btnDelete = view.findViewById(R.id.btnDelete);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnBuy = view.findViewById(R.id.btnBuy);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(3);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(6);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(7);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(8);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(9);
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNumber(0);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDelete();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAmount = 0.00;
                setBuyAmount(buyAmount);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment coinDetailsFragment = new CoinDetailsFragment();
                coinDetailsFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, coinDetailsFragment).addToBackStack(null).commit();
            }
        });

    }

    private void buy() {
        numLocation = 0;

        if (buyAmount > assets) {
            Toast.makeText(getActivity().getApplicationContext(), "Account has insufficient funds", Toast.LENGTH_SHORT).show();
        } else {
            String groupId = getActivity().getIntent().getStringExtra("groupName");
            DocumentReference docRef = db.collection("groups").document(groupId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            Log.d(TAG, "DocumentSnapshot data: " + data);

                            ArrayList<Object> trades = (ArrayList) data.get("trades");
                            HashMap<String, Object> trade = new HashMap<>();

                            String tradeDirection = "buy";
                            NumberFormat format = NumberFormat.getCurrencyInstance();
                            Number priceNumber = null;
                            try {
                                priceNumber = format.parse(price);
                                System.out.println(priceNumber);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Double tradeLot = buyAmount / ((Double) priceNumber);
                            Timestamp tradeTimestamp = Timestamp.now();

                            trade.put("direction", tradeDirection);
                            trade.put ("lot", tradeLot);
                            trade.put("price", price);
                            trade.put("ticker", ticker);
                            trade.put("time", tradeTimestamp);
                            trade.put("trader", userId);

                            trades.add(trade);
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("trades", trades);
                            db.collection("groups").document(groupId).set(updatedData, SetOptions.merge());
                            updateUserBalance(buyAmount);
                            buyAmount = 0.00;
                            setBuyAmount(buyAmount);
                            Toast.makeText(getActivity().getApplicationContext(), "Trade confirmed", Toast.LENGTH_SHORT).show();
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

    private void updateUserBalance(Double buyAmount) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);

                        Double assets = (Double) data.get("assets");
                        assets = assets - buyAmount;

                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("assets", assets);
                        db.collection("users").document(userId).set(updatedData, SetOptions.merge());

                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        currentAssets = "You have $" + formatter.format(assets) + " available";
                        tvCurrentAccountAssets.setText(currentAssets);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void clickDelete() {
        numLocation--;
        buyAmount = buyAmount*0.1;
        if (buyAmount < 0.01) {
            buyAmount = 0.00;
        }
        setBuyAmount(buyAmount);
    }

    private void clickNumber(int i) {
        numLocation++;
        if (numLocation > 11) {
            return;
        }
        buyAmount = buyAmount*10 + (i * 0.01);
        setBuyAmount(buyAmount);
    }

    private void getCurrentAssets() {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);
                        assets = (Double) data.get("assets");
                        if (assets == 0.00) {
                            currentAssets = "You have $0.00 available";
                            tvCurrentAccountAssets.setText(currentAssets);
                        } else {
                            DecimalFormat formatter = new DecimalFormat("#,###.00");
                            currentAssets = "You have $" + formatter.format(assets) + " available";
                            tvCurrentAccountAssets.setText(currentAssets);
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

    private void setBuyAmount(Double amount) {
        if (amount < 1) {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            buyAmountStr = "$0" + formatter.format(amount);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            buyAmountStr = "$" + formatter.format(amount);
        }
        tvBuyAmount.setText(buyAmountStr);
    }
}