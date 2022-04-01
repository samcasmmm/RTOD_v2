package com.example.rtod_v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.rtod_v2.Auth.AuthMainActivity;
import com.example.rtod_v2.facial.CameraActivity;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity ","OpenCV is Loaded");
        }
        else {
            Log.d("MainActivity ","OpenCV is Not Loaded");
        }
    }

    ImageView profileEdit;
//    Button RTODetectionBtn,HandSignBtn,EditProfileBtn,scan_lenBTN;
    CardView cv_objectDetection, cv_signLang,cv_chatBot, cv_scanLen, cv_feedback,cv_faceRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RTODetectionBtn = findViewById(R.id.ObjectDetectionBTN);
//        HandSignBtn = findViewById(R.id.handSignBTN);
//        EditProfileBtn = findViewById(R.id.EditProfileBTN);
//        chatBotImg = findViewById(R.id.id_chatBot);
//        scan_lenBTN = findViewById(R.id.scan_lenBTN);

        cv_objectDetection = findViewById(R.id.cv_objectDetection);
        cv_signLang = findViewById(R.id.cv_sign_lang);
        cv_scanLen = findViewById(R.id.cv_scan_len);
        cv_chatBot = findViewById(R.id.cv_chatBOT);
        profileEdit = findViewById(R.id.profile_edit);
        cv_feedback = findViewById(R.id.cv_feedback);
        cv_faceRec = findViewById(R.id.cv_faceRec);


        cv_chatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.example.rtod_v2.chatBot.MainActivity.class));
            }
        });





//        profileEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,AuthMainActivity.class));
//            }
//        });

        cv_objectDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,com.example.rtod_v2.ObjDetect.CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        cv_faceRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });



//        cv_scanLen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, com.example.objectdetect_v1.RTOD_Len.MainRLenActivity.class));
//            }
//        });

//        cv_feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, com.example.objectdetect_v1.feedBack.feedback_main.class));
//            }
//        });
    }
}