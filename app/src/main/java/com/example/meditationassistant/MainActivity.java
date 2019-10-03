package com.example.meditationassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView counter, timer;
    EditText counter_number;
    ImageButton play;
    Button record, stop, stopPlaying;
    String pathsave;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = findViewById(R.id.counter);
        counter_number = findViewById(R.id.numberOfTimes);
        play = findViewById(R.id.play);
        record = findViewById(R.id.recodButton);
        stop = findViewById(R.id.stopButton);
        timer = findViewById(R.id.recordTimer);
        stopPlaying = findViewById(R.id.stopPlaying);


        pathsave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/M_Recordings" + UUID.randomUUID().toString() + "_meditation_record.3gp";

        micpermission();
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReadStoragePermissionGranted();
                setupmediarecorder();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                play.setEnabled(false);
                stopPlaying.setEnabled(false);
                Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder=null;
                stop.setEnabled(false);
                play.setEnabled(true);
                record.setEnabled(true);
                stopPlaying.setEnabled(false);
                Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*try
                {
                    if (pathsave.isEmpty()== true)
                        Toast.makeText(MainActivity.this, "Create file first", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }*/



                int i = Integer.parseInt(counter_number.getText().toString());
                counter.setText(""+i);
                mediaPlayer = new MediaPlayer();
                for (int a = 0; a < i; a++) {
                    try {

                        mediaPlayer.setDataSource(pathsave);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Playing..."+i, Toast.LENGTH_SHORT).show();
                }

                stopPlaying.setEnabled(true);

            }
        });

        stopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                Toast.makeText(MainActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
               /* setupmediarecorder();*/
            }
        });

    }

    private void setupmediarecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }

    public void isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted1");

            } else {
                Log.v("TAG", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted1");
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted2");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted2");
            return true;
        }
    }

    public void micpermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 2);
        }
    }
}

