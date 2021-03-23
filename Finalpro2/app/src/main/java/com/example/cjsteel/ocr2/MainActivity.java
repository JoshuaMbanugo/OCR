package com.example.cjsteel.ocr2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Button reset;
    private DrewView DrewVw;
    private Button cam;
    private Button resetBtn;
    private Bitmap imageBitmap;
    private TextView txtView;
    private ImageView imageView;
    private Button detect;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //private static int SPLASH_TIME_OUT = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        cam = (Button) findViewById(R.id.Cam);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        detect = (Button) findViewById(R.id.detect);

        txtView = findViewById(R.id.txtView);
        DrewVw = findViewById(R.id.DrewVw);
        imageView = findViewById(R.id.imageView);

        final MediaPlayer sound1 = MediaPlayer.create(this, R.raw.sound1);
        final MediaPlayer sound2 = MediaPlayer.create(this, R.raw.sound2);
        final MediaPlayer sound3 = MediaPlayer.create(this, R.raw.sound3);
        final MediaPlayer sound4 = MediaPlayer.create(this, R.raw.sound4);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sound2.start();
                resetCanvas(DrewVw);

            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamView();
                sound3.start();
            }

        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect();
                sound1.start();
            }
        });
    }

    // Capture and confirmes the text by bring the drawview and the firebase recogniser
    private void detect() {

        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle(" (►﹏◄) Detecting (►﹏◄) ");
        saveDialog.setPositiveButton("Lets Go", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // calls the image from the DrawView class
                imageBitmap = getBitmapFromView(DrewVw);
                //calls the method that gets the result of the text.
                detectTxt();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();

    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

        }
    }

    public void detectTxt() {

        // the bitmap image gets passed through the imageBitmap
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        //detects the text
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        //processes the images the the listener
        detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        // Task completed successfully
                        processTxt(firebaseVisionText);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                // Task failed with an exception
                                System.err.println(e.getMessage());
                            }
                        });
    }

    //processes the text into blocks and sets a value of each block to be called
    private void processTxt(FirebaseVisionText text) {
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(MainActivity.this, "Can't find text (◣_◢)", Toast.LENGTH_LONG).show();

            // plays sound when function is passed
            final MediaPlayer sound4 = MediaPlayer.create(this, R.raw.sound4);
            sound4.start();

            return;
        }

        // this puts the text into the text view
        for (FirebaseVisionText.TextBlock block : text.getTextBlocks()) {
            String txt = block.getText();
            txtView.setTextSize(24);
            txtView.setText(txt);
        }
    }


    // MLKIT PROCESS_TEXT_BLOCK
    public void openCamView() {
        Intent intent = new Intent(this, CamView.class);
        startActivity(intent);
    }

    public void resetCanvas(View v) {
        DrewVw.clearCanvas();

    }
}