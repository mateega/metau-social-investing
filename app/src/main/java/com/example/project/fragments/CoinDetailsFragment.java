package com.example.project.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.BuildConfig;
import com.example.project.Coin;
import com.example.project.MainActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.HttpHeaders;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.tradingview.lightweightcharts.api.options.models.ChartOptions;
import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions;
import com.tradingview.lightweightcharts.api.options.models.LayoutOptions;
import com.tradingview.lightweightcharts.view.ChartsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "COIN DETAILS FRAGMENT";

    private String mParam1;
    private String mParam2;

    TextView tvCoinName;
    TextView tvCoinName2;
    TextView tvCoinTicker;
    TextView tvPrice;
    TextView tvPriceChange;
    ImageView ivCoinImage;

    ImageButton ibBack;

    String name;
    String ticker;
    String price;
    String priceChange;
    String rank;
    String imageUrl;

    Button btnSell;
    Button btnBuy;

    Bundle bundle;


    public CoinDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoinDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoinDetailsFragment newInstance(String param1, String param2) {
        CoinDetailsFragment fragment = new CoinDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_coin_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ibBack = view.findViewById(R.id.ibBack);
        tvCoinName = view.findViewById(R.id.tvCoinName);
        tvCoinName2 = view.findViewById(R.id.tvCoinName2);
        tvCoinTicker = view.findViewById(R.id.tvCoinTicker);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvPriceChange = view.findViewById(R.id.tvPriceChange);
        ivCoinImage = view.findViewById(R.id.ivCoinImage);
        btnSell = view.findViewById(R.id.btnSell);
        btnBuy = view.findViewById(R.id.btnBuy);

        // Data points below chart
        // Commented out until I decide which data points should be displayed (e.g. high, low, open, volume)
//        tvHigh = view.findViewById(R.id.tvHigh);
//        tvLow = view.findViewById(R.id.tvLow);
//        tvOpen = view.findViewById(R.id.tvOpen);
//        tvVolume = view.findViewById(R.id.tvVolume);

        tvCoinName.setText(name);
        tvCoinName2.setText(name);
        tvCoinTicker.setText(ticker);
        tvPrice.setText(price);
        tvPriceChange.setText(priceChange);

        int color = Color.parseColor("#47cd54");
        if (priceChange.charAt(0) == '-') {
            color = Color.parseColor("#fe5857");
        }
        tvPriceChange.setTextColor(color);

        if (imageUrl != null) {
            Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(ivCoinImage);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment tradeFragment = new TradeFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.flContainer, tradeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("direction", "sell");
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment tradePaymentFragment = new TradePaymentFragment();
                tradePaymentFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.flContainer, tradePaymentFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("direction", "buy");
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment tradePaymentFragment = new TradePaymentFragment();
                tradePaymentFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.flContainer, tradePaymentFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}