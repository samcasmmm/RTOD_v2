package com.example.rtod_v2.chatBot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtod_v2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private EditText userMsgEdit;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<com.example.rtod_v2.chatBot.ChatsModels>chatsModelsArrayList;
    private ChatRV_Adapter chatRV_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_main);

        chatsRV = findViewById(R.id.idRVChat);
        userMsgEdit = findViewById(R.id.EditMSG);
        sendMsgFAB = findViewById(R.id.idFABSend);
        chatsModelsArrayList = new ArrayList<>();
        chatRV_adapter = new ChatRV_Adapter(chatsModelsArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRV_adapter);


        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdit.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Your Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdit.getText().toString());
                userMsgEdit.setText("");
                
            }
        });

    }
    private void getResponse(String message){

        chatsModelsArrayList.add(new ChatsModels(message,USER_KEY));
        chatRV_adapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=164334&key=RhXuzHnkDj2vdQrz&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(@NonNull Call<MsgModel> call, @NonNull Response<MsgModel> response) {
                if (response.isSuccessful()){
                    MsgModel model = response.body();
                    chatsModelsArrayList.add(new ChatsModels(model.getCnt(),BOT_KEY));
                    chatRV_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MsgModel> call, @NonNull Throwable t) {
                chatsModelsArrayList.add(new ChatsModels("Please Revert Your Question",BOT_KEY));
                chatRV_adapter.notifyDataSetChanged();
            }
        });


    }
}