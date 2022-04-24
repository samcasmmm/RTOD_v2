package com.example.rtod_v2.facial;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.rtod_v2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class facialExpressionRecognition {
    private Interpreter interpreter;
    private int INPUT_SIZE;
    private int height=0;
    private int width=0;
    private GpuDelegate gpuDelegate=null;
    private CascadeClassifier cascadeClassifier;
    String tempObj = "";
    facialExpressionRecognition(AssetManager assetManager, Context context, String modelPath, int inputSize) throws IOException {
        INPUT_SIZE=inputSize;
        Interpreter.Options options=new Interpreter.Options();
        gpuDelegate=new GpuDelegate();
        options.addDelegate(gpuDelegate);
        options.setNumThreads(4);
        interpreter=new Interpreter(loadModelFile(assetManager,modelPath),options);
        Log.d("facial_Expression","Model is loaded");
        try {
            InputStream is=context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir=context.getDir("cascade",Context.MODE_PRIVATE);
            File mCascadeFile=new File(cascadeDir,"haarcascade_frontalface_alt");
            FileOutputStream os=new FileOutputStream(mCascadeFile);

            byte[] buffer=new byte[4096];
            int byteRead;
            //-1 that means no data to read
            while ((byteRead=is.read(buffer)) !=-1){

                os.write(buffer,0,byteRead);
            }
            is.close();
            os.close();
            cascadeClassifier=new CascadeClassifier(mCascadeFile.getAbsolutePath());
            Log.d("facial_Expression","Classifier is loaded");

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public Mat recognizeImage(Mat mat_image){

        Core.flip(mat_image.t(),mat_image,1);
        Mat grayscaleImage=new Mat();
        Imgproc.cvtColor(mat_image,grayscaleImage,Imgproc.COLOR_RGBA2GRAY);

        // set height and width
        height=grayscaleImage.height();
        width=grayscaleImage.width();


        int absoluteFaceSize=(int)(height*0.1);
        MatOfRect faces=new MatOfRect();
        if(cascadeClassifier !=null){
            //                                input         output
            cascadeClassifier.detectMultiScale(grayscaleImage,faces,1.1,2,2,
                    new Size(absoluteFaceSize,absoluteFaceSize),new Size());
        }

        Rect[] faceArray=faces.toArray();

        for (int i=0;i<faceArray.length;i++){

            // if you want to draw rectangle around face
            //                input/output starting point ending point        color   R  G  B  alpha    thickness
            Imgproc.rectangle(mat_image,faceArray[i].tl(),faceArray[i].br(),new Scalar(255,0,132,255),2);

            // now crop face from original frame and grayscaleImage
            // starting x coordinate       starting y coordinate
            Rect roi=new Rect((int)faceArray[i].tl().x,(int)faceArray[i].tl().y,
                    ((int)faceArray[i].br().x)-(int)(faceArray[i].tl().x),
                    ((int)faceArray[i].br().y)-(int)(faceArray[i].tl().y));

            // it's very important check one more time
            Mat cropped_rgba=new Mat(mat_image,roi);//

            Bitmap bitmap=null;
            bitmap=Bitmap.createBitmap(cropped_rgba.cols(),cropped_rgba.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped_rgba,bitmap);

            Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,48,48,false);

            ByteBuffer byteBuffer=convertBitmapToByteBuffer(scaledBitmap);

            float[][] emotion=new float[1][1];

            interpreter.run(byteBuffer,emotion);


            // define float value of emotion
            float emotion_v=(float)Array.get(Array.get(emotion,0),0);
            Log.d("facial_expression","Output:  "+ emotion_v);

            String emotion_s=get_emotion_text(emotion_v);

            //             input/output    text: Angry (2.934234)
            Imgproc.putText(mat_image,emotion_s+" ("+emotion_v+")",
                    new Point((int)faceArray[i].tl().x+10,(int)faceArray[i].tl().y+20),
                    1,1.5,new Scalar(0,0,255,150),2);

            tempObj = emotion_s;
            saveLog();
        }

        Core.flip(mat_image.t(),mat_image,0);
        return mat_image;
    }

    public void saveLog(){

        LocalDate dateObj = LocalDate.now();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Face-Expression-LOG").child("Date : "+dateObj.toString());

        HashMap<String, String> objMap = new HashMap<>();
        LocalTime myObj = LocalTime.now();
        String emoName = tempObj;
        objMap.put("Name",emoName);
        objMap.put("Time",myObj.toString());

        myRef.push().setValue(objMap);


    }

    private String get_emotion_text(float emotion_v) {
        String val="";


        if(emotion_v>=0 & emotion_v<0.5){
            val="Surprise";
        }
        else if(emotion_v>=0.5 & emotion_v <1.5){
            val="Fear";
        }
        else if(emotion_v>=1.5 & emotion_v <2.5){
            val="Angry";
        }
        else if(emotion_v>=2.5 & emotion_v <3.5){
            val="Neutral";
        }
        else if(emotion_v>=3.5 & emotion_v <4.5){
            val="Sad";
        }
        else if(emotion_v>=4.5 & emotion_v <5.5){
            val="Disgust";
        }
        else {
            val="Happy";
        }
        return val;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap scaledBitmap) {
        ByteBuffer byteBuffer;
        int size_image=INPUT_SIZE;//48

        byteBuffer=ByteBuffer.allocateDirect(4*1*size_image*size_image*3);

        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues=new int[size_image*size_image];
        scaledBitmap.getPixels(intValues,0,scaledBitmap.getWidth(),0,0,scaledBitmap.getWidth(),scaledBitmap.getHeight());
        int pixel=0;
        for(int i =0;i<size_image;++i){
            for(int j=0;j<size_image;++j){
                final int val=intValues[pixel++];
                byteBuffer.putFloat((((val>>16)&0xFF))/255.0f);
                byteBuffer.putFloat((((val>>8)&0xFF))/255.0f);
                byteBuffer.putFloat(((val & 0xFF))/255.0f);

            }
        }
        return byteBuffer;
    }


    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException{
        // this will give description of file
        AssetFileDescriptor assetFileDescriptor=assetManager.openFd(modelPath);
        // create a inputsteam to read file
        FileInputStream inputStream=new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();

        long startOffset=assetFileDescriptor.getStartOffset();
        long declaredLength=assetFileDescriptor.getDeclaredLength();
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);

    }


}
