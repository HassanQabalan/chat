package com.example.hasan.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ProductViewHolder> {


    private Context mCtx;
    int mycolor;
    private List<Chats> chats_list;

    public AdapterChat(Context mCtx, List<Chats> chats_list) {
        this.mCtx = mCtx;

        this.chats_list = chats_list;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_item_chat_row, null);
        final ProductViewHolder viewHolder=new ProductViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Chats chats = chats_list.get(position);
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("user_info", Context.MODE_PRIVATE);

        mycolor = sharedPreferences.getInt("color", 0);

        holder.name.setTextColor(mycolor);
        SharedPreferences sharedPreferencesCompany = mCtx.getSharedPreferences("loginInfo", 0);


        holder.name.setText(chats.getName());
        holder.chatText.setText(chats.getChat_text());



}

    @Override
    public int getItemCount() {
        return chats_list.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name,chatText;
        LinearLayout linearLayout;
        RelativeLayout RelativeLayoutChat;
        FloatingActionButton floatingActionButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            chatText = itemView.findViewById(R.id.tv_chat_text);
            RelativeLayoutChat=itemView.findViewById(R.id.RelativeLayoutChat);

        }

    }
        }
