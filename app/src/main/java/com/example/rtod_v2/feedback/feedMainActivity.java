package com.example.rtod_v2.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rtod_v2.MainActivity;
import com.example.rtod_v2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class feedMainActivity extends AppCompatActivity {

    private EditText et_name, et_problem, et_message;
    private Button submitBTN;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_main);

        et_name=findViewById(R.id.et_name);
        et_problem=findViewById(R.id.et_problem);
        et_message=findViewById(R.id.et_message);
        submitBTN=findViewById(R.id.submitBtn);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String problem = et_problem.getText().toString();
                String message = et_message.getText().toString();

                HashMap<String, String> userMap = new HashMap<>();
                if (TextUtils.isEmpty(name)){
                    et_name.setError("Enter Name");
                }
                if (TextUtils.isEmpty(problem)){
                    et_problem.setError("Enter Name");
                }
                if (TextUtils.isEmpty(message)){
                    et_message.setError("Enter Name");
                }
                else {

                    userMap.put("Name", name);
                    userMap.put("Problem", problem);
                    userMap.put("Message", message);
                    myRef.push().setValue(userMap);
                    Toast.makeText(feedMainActivity.this, "Thanks for FeedBacK !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });


    }

}