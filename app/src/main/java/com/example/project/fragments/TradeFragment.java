package com.example.project.fragments;

import static com.google.common.collect.ComparisonChain.start;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.BuildConfig;
import com.example.project.MainActivity;
import com.example.project.R;
import com.google.common.net.HttpHeaders;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TRADE FRAGMENT";

    EditText etSearch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String searchText;

    TextView tvCoinName1;
    TextView tvCoinTicker1;
    TextView tvPrice1;
    TextView tvPriceChange1;
    TextView tvCoinName2;
    TextView tvCoinTicker2;
    TextView tvPrice2;
    TextView tvPriceChange2;
    TextView tvCoinName3;
    TextView tvCoinTicker3;
    TextView tvPrice3;
    TextView tvPriceChange3;
    TextView tvCoinName4;
    TextView tvCoinTicker4;
    TextView tvPrice4;
    TextView tvPriceChange4;
    TextView tvCoinName5;
    TextView tvCoinTicker5;
    TextView tvPrice5;
    TextView tvPriceChange5;

    TextView tvCoinNameSearch;
    TextView tvCoinTickerSearch;
    TextView tvPriceSearch;
    TextView tvPriceChangeSearch;
    ImageView ivCoinImageSearch;

    TextView tvTopCoins;

    ImageView ivCoinImage1;
    ImageView ivCoinImage2;
    ImageView ivCoinImage3;
    ImageView ivCoinImage4;
    ImageView ivCoinImage5;

    RelativeLayout layCoin1;
    RelativeLayout layCoin2;
    RelativeLayout layCoin3;
    RelativeLayout layCoin4;
    RelativeLayout layCoin5;
    RelativeLayout layCoinSearch;

    public TradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradeFragment newInstance(String param1, String param2) {
        TradeFragment fragment = new TradeFragment();
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
        return inflater.inflate(R.layout.fragment_trade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCoinName1 = view.findViewById(R.id.tvCoinName1);
        tvCoinTicker1 = view.findViewById(R.id.tvCoinTicker1);
        tvPrice1 = view.findViewById(R.id.tvPrice1);
        tvPriceChange1 = view.findViewById(R.id.tvPriceChange1);
        tvCoinName2 = view.findViewById(R.id.tvCoinName2);
        tvCoinTicker2 = view.findViewById(R.id.tvCoinTicker2);
        tvPrice2 = view.findViewById(R.id.tvPrice2);
        tvPriceChange2 = view.findViewById(R.id.tvPriceChange2);
        tvCoinName3 = view.findViewById(R.id.tvCoinName3);
        tvCoinTicker3 = view.findViewById(R.id.tvCoinTicker3);
        tvPrice3 = view.findViewById(R.id.tvPrice3);
        tvPriceChange3 = view.findViewById(R.id.tvPriceChange3);
        tvCoinName4 = view.findViewById(R.id.tvCoinName4);
        tvCoinTicker4 = view.findViewById(R.id.tvCoinTicker4);
        tvPrice4 = view.findViewById(R.id.tvPrice4);
        tvPriceChange4 = view.findViewById(R.id.tvPriceChange4);
        tvCoinName5 = view.findViewById(R.id.tvCoinName5);
        tvCoinTicker5 = view.findViewById(R.id.tvCoinTicker5);
        tvPrice5 = view.findViewById(R.id.tvPrice5);
        tvPriceChange5 = view.findViewById(R.id.tvPriceChange5);

        tvCoinNameSearch = view.findViewById(R.id.tvCoinNameSearch);
        tvCoinTickerSearch = view.findViewById(R.id.tvCoinTickerSearch);
        tvPriceSearch = view.findViewById(R.id.tvPriceSearch);
        tvPriceChangeSearch = view.findViewById(R.id.tvPriceChangeSearch);
        ivCoinImageSearch = view.findViewById(R.id.ivCoinImageSearch);

        tvTopCoins = view.findViewById(R.id.tvTopCoins);

        ivCoinImage1 = view.findViewById(R.id.ivCoinImage1);
        ivCoinImage2 = view.findViewById(R.id.ivCoinImage2);
        ivCoinImage3 = view.findViewById(R.id.ivCoinImage3);
        ivCoinImage4 = view.findViewById(R.id.ivCoinImage4);
        ivCoinImage5 = view.findViewById(R.id.ivCoinImage5);

        layCoin1 = view.findViewById(R.id.layCoin1);
        layCoin2 = view.findViewById(R.id.layCoin2);
        layCoin3 = view.findViewById(R.id.layCoin3);
        layCoin4 = view.findViewById(R.id.layCoin4);
        layCoin5 = view.findViewById(R.id.layCoin5);
        layCoinSearch = view.findViewById(R.id.layCoinSearch);

        layCoinSearch.setVisibility(View.GONE);

        // list today's top cryptocurrencies
        ArrayList<String> coins = new ArrayList<>();
        coins.add("BTC");
        coins.add("ETH");
        coins.add("USDT");
        coins.add("USDC");
        coins.add("BNB");
        callCoinMarketCap(coins, false);

        etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
                if (searchText.isEmpty() || searchText.equals("")) {
                    Log.i(TAG, "Search is empty");
                    toggleCoinViews(false);
                } else {
                    // commented out for now while API key is at request limit
                    try {
                        Log.i(TAG, "Text change");
                        searchForCoin(searchText);
                        toggleCoinViews(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void toggleCoinViews(boolean b) {
        if (b == true) {
            tvTopCoins.setVisibility(View.GONE);
            layCoin1.setVisibility(View.GONE);
            layCoin2.setVisibility(View.GONE);
            layCoin3.setVisibility(View.GONE);
            layCoin4.setVisibility(View.GONE);
            layCoin5.setVisibility(View.GONE);
            layCoinSearch.setVisibility(View.VISIBLE);
        } else {
            tvTopCoins.setVisibility(View.VISIBLE);
            layCoin1.setVisibility(View.VISIBLE);
            layCoin2.setVisibility(View.VISIBLE);
            layCoin3.setVisibility(View.VISIBLE);
            layCoin4.setVisibility(View.VISIBLE);
            layCoin5.setVisibility(View.VISIBLE);
            layCoinSearch.setVisibility(View.GONE);
        }
    }

    private void searchForCoin(String searchText) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://rest.coinapi.io/v1/assets?filter_asset_id=" + searchText)
                .get()
                .addHeader(BuildConfig.COINAPI_AUTH_HEADER, BuildConfig.COINAPI_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String jsonData = responseBody.string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        System.out.println("JSONARRAY SIZE: " + jsonArray.length());
                        JSONObject object = jsonArray.getJSONObject(0);
                        String ticker = object.getString("asset_id");
                        ArrayList<String> coins = new ArrayList<>();
                        coins.add(ticker);
                        callCoinMarketCap(coins, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void callCoinMarketCap(ArrayList<String> coins, boolean search) {
        String coinsStr = "";
        for (int i = 0; i < coins.size(); i++) {
            String coin = coins.get(i);
            coinsStr = coinsStr + coin;
            if (i != coins.size()-1) {
                coinsStr = coinsStr + ",";
            }
        }

        // CoinMarketCap call for top cryptocurrency text
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest").newBuilder();
        urlBuilder.addQueryParameter("symbol",coinsStr);
        urlBuilder.addQueryParameter("convert","USD");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(HttpHeaders.ACCEPT, "application/json")
                .addHeader("X-CMC_PRO_API_KEY", BuildConfig.COINMARKETCAP_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String jsonData = responseBody.string();
                    System.out.println(jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONObject data = jsonObject.getJSONObject("data");
                        for (int i = 1; i < data.length() + 1; i++) {
                            JSONObject cryptocurrency = data.getJSONObject(coins.get(i-1));
                            String name = cryptocurrency.getString("name");
                            String ticker = cryptocurrency.getString("symbol");
                            JSONObject quote = cryptocurrency.getJSONObject("quote");
                            JSONObject usd = quote.getJSONObject("USD");

                            String priceStr = usd.getString("price");
                            Double priceDbl = Double.parseDouble(priceStr);
                            DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                            if (priceStr.charAt(0) == '0') {
                                priceStr = "$0" + formatter1.format(priceDbl);
                            } else {
                                priceStr = "$" + formatter1.format(priceDbl);
                            }

                            String changeStr = usd.getString("percent_change_24h");
                            Double changeDbl = Double.parseDouble(changeStr);
                            DecimalFormat formatter2 = new DecimalFormat("##.##");
                            changeStr = formatter2.format(changeDbl) + "%";

                            int finalI = i;
                            String finalPriceStr = priceStr;
                            String finalChangeStr = changeStr;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (search) {
                                        setCoinMarketCapTextViews(0, name, ticker, finalPriceStr, finalChangeStr);
                                        System.out.println("SEARCH: " + name + ", " + ticker + ", " + finalPriceStr + ", " + finalChangeStr);
                                    } else {
                                        setCoinMarketCapTextViews(finalI, name, ticker, finalPriceStr, finalChangeStr);
                                        System.out.println("NOT SEARCHING");
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // CoinMarketCap call for top cryptocurrency images
        urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info").newBuilder();
        urlBuilder.addQueryParameter("symbol",coinsStr);
        url = urlBuilder.build().toString();

        Request imageRequest = new Request.Builder()
                .url(url)
                .get()
                .addHeader(HttpHeaders.ACCEPT, "application/json")
                .addHeader("X-CMC_PRO_API_KEY", BuildConfig.COINMARKETCAP_KEY)
                .build();

        client.newCall(imageRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String jsonData = responseBody.string();
                    System.out.println(jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONObject data = jsonObject.getJSONObject("data");

                        if (search) {
                            JSONObject cryptocurrency = data.getJSONObject(coins.get(0));
                            String logo = cryptocurrency.getString("logo");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setCoinMarketCapImageViews(0, logo);
                                }
                            });
                        } else {
                            for (int i = 1; i < data.length() + 1; i++) {
                                JSONObject cryptocurrency = data.getJSONObject(coins.get(i-1));
                                String logo = cryptocurrency.getString("logo");

                                int finalI = i;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setCoinMarketCapImageViews(finalI, logo);
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setCoinMarketCapTextViews(int i, String name, String ticker, String price, String priceChange) {
        int color = Color.parseColor("#47cd54");
        if (priceChange.charAt(0) == '-') {
            color = Color.parseColor("#fe5857");
        }

        switch(i) {
            case 0:
                tvCoinNameSearch.setText(name);
                tvCoinTickerSearch.setText(ticker);
                tvPriceSearch.setText(price);
                tvPriceChangeSearch.setText(priceChange);
                tvPriceChangeSearch.setTextColor(color);
                break;
            case 1:
                tvCoinName1.setText(name);
                tvCoinTicker1.setText(ticker);
                tvPrice1.setText(price);
                tvPriceChange1.setText(priceChange);
                tvPriceChange1.setTextColor(color);
                break;
            case 2:
                tvCoinName2.setText(name);
                tvCoinTicker2.setText(ticker);
                tvPrice2.setText(price);
                tvPriceChange2.setText(priceChange);
                tvPriceChange2.setTextColor(color);
                break;
            case 3:
                tvCoinName3.setText(name);
                tvCoinTicker3.setText(ticker);
                tvPrice3.setText(price);
                tvPriceChange3.setText(priceChange);
                tvPriceChange3.setTextColor(color);
                break;
            case 4:
                tvCoinName4.setText(name);
                tvCoinTicker4.setText(ticker);
                tvPrice4.setText(price);
                tvPriceChange4.setText(priceChange);
                tvPriceChange4.setTextColor(color);
                break;
            case 5:
                tvCoinName5.setText(name);
                tvCoinTicker5.setText(ticker);
                tvPrice5.setText(price);
                tvPriceChange5.setText(priceChange);
                tvPriceChange5.setTextColor(color);
                break;
            default:
                System.out.println("invalid view");
        }
    }

    private void setCoinMarketCapImageViews(int i, String profilePictureUrl) {
        switch(i) {
            case 0:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImageSearch);
                }
                break;
            case 1:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImage1);
                }
                break;
            case 2:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImage2);
                }
                break;
            case 3:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImage3);
                }
                break;
            case 4:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImage4);
                }
                break;
            case 5:
                if (profilePictureUrl != null) {
                    Glide.with(getActivity().getApplicationContext()).load(profilePictureUrl).into(ivCoinImage5);
                }
                break;
            default:
                System.out.println("invalid view");
        }
    }


}