package com.example.rtod_v2.chatBot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtod_v2.R;

import java.util.ArrayList;

public class ChatRV_Adapter extends RecyclerView.Adapter {

    private ArrayList<ChatsModal> chatsModalArrayList;
    private Context context;

    public ChatRV_Adapter(ArrayList<ChatsModal> chatsModelsArrayList, Context context) {
        this.chatsModalArrayList = chatsModelsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item,parent,false);
                return new user_ViewHolder(view);

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item,parent,false);
                return new bot_ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModal chatsModal = chatsModalArrayList.get(position);
        switch (chatsModal.getSender()){
            case "user":
                ((user_ViewHolder)holder).userTV.setText(chatsModal.getMessage());
                break;
            case "bot":
                ((bot_ViewHolder)holder).botTV.setText(chatsModal.getMessage());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsModalArrayList.get(position).getSender()){
            case "user":
              return 0;

            case "bot":
                return 1;

            default:
                return -1;

        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    public static class user_ViewHolder extends RecyclerView.ViewHolder{

        TextView userTV;

        public user_ViewHolder(@NonNull View itemView) {
            super(itemView);

            userTV = itemView.findViewById(R.id.idTVUser);

        }
    }

    public static class bot_ViewHolder extends RecyclerView.ViewHolder{

        TextView botTV;

        public bot_ViewHolder(@NonNull View itemView) {
            super(itemView);

            botTV = itemView.findViewById(R.id.idTVBot);

        }
    }

}
