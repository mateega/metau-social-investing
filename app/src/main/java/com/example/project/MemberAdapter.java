package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;
    private List<String> members;
    private String TAG = "MEMBER ADAPTER";

    public MemberAdapter(Context context, List<String> members) {
        this.context = context;
        this.members = members;
    }

    public void clear(){
        members.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<String> members) {
        members.addAll(members);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String member = members.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        }

        public void bind(String member) {
            tvName.setText(member);



//            ParseFile profilePicture = (ParseFile) post.getUser().get("profilePic");
//            if (profilePicture != null) {
//                Glide.with(context).load(profilePicture.getUrl()).into(ivProfileImage);
//            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.i(TAG, String.valueOf(position));
        }
    }

}
