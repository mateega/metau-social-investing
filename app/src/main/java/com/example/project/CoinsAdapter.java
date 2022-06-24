package com.example.project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.ViewHolder> {

    private Context context;
    private List<Coin> coins;
    private String TAG = "COINS ADAPTER";

    public CoinsAdapter(Context context, List<Coin> coins) {
        this.context = context;
        this.coins = coins;
    }

    public void clear() {
        coins.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Coin> list) {
        coins.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinsAdapter.ViewHolder holder, int position) {
        Coin coin = coins.get(position);
        holder.bind(coin);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCoinImage;
        private TextView tvRank;
        private TextView tvCoinName;
        private TextView tvCoinTicker;
        private TextView tvPrice;
        private TextView tvPriceChange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCoinImage = itemView.findViewById(R.id.ivCoinImage);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvCoinName = itemView.findViewById(R.id.tvCoinName);
            tvCoinTicker = itemView.findViewById(R.id.tvCoinTicker);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPriceChange = itemView.findViewById(R.id.tvPriceChange);
        }

        public void bind(Coin coin) {
            tvRank.setText(coin.getRank());
            tvCoinName.setText(coin.getName());
            tvCoinTicker.setText(coin.getTicker());
            tvPrice.setText(coin.getPrice());
            tvPriceChange.setText(coin.getPriceChange());

            int color = Color.parseColor("#47cd54");
            if (coin.getPriceChange().charAt(0) == '-') {
                color = Color.parseColor("#fe5857");
            }
            tvPriceChange.setTextColor(color);

            String imageUrl = coin.getImageUrl();
            if (imageUrl != null) {
                Glide.with(context).load(imageUrl).into(ivCoinImage);
            }
        }
    }
}
