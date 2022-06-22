package com.example.project.fragments;

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
import android.widget.TextView;

import com.example.project.BuildConfig;
import com.example.project.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


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

    TextView tvCoinName;
    TextView tvCoinTicker;
    TextView tvPrice;
    TextView tvPriceChange;

    String name;
    String ticker;
    String price;
    String priceChange;

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

        tvCoinName = view.findViewById(R.id.tvCoinName);
        tvCoinTicker = view.findViewById(R.id.tvCoinTicker);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvPriceChange = view.findViewById(R.id.tvPriceChange);


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
                // Log.i(TAG, "Search: " + searchText);
                if (searchText.isEmpty() || searchText.equals("")) {
                    Log.i(TAG, "Search is empty");
                } else {
                    try {
                        Log.i(TAG, "Text change");
                        getAssetAPI(searchText);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void getAssetAPI(String searchText) throws IOException {
        name = "updated name";
        setViews();

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
                    // json response headers
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
                    String jsonData = responseBody.string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        System.out.println("JSONARRAY SIZE: " + jsonArray.length());
                        JSONObject object = jsonArray.getJSONObject(0);
                        name = object.getString("name");
                        ticker = object.getString("asset_id");
                        price = object.getString("price_usd");
                        priceChange = "N/A";
                        setViews();

                        Log.i(TAG, name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setViews() {
        tvCoinName.setText(name);
        tvCoinTicker.setText(ticker);
        tvPrice.setText(price);
        tvPriceChange.setText(priceChange);
    }

}