package com.yh.simvest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    List<Share> listShare;
    Context context;

    public MyAdapter(Context ct, List<Share> list) {
        context = ct;
        listShare = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_share.setText(listShare.get(position).code + " " + listShare.get(position).lots);
    }

    @Override
    public int getItemCount() {
        return listShare.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_share = itemView.findViewById(R.id.tv_share);
        }
    }

}
