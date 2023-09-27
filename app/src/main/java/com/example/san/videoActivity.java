package com.example.san;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;

import android.widget.VideoView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class videoActivity extends AppCompatActivity {
    @BindView(R.id.videoView)
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_video);
            ButterKnife.bind(this);


            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
            Uri uri = Uri.parse(videoPath);
            videoView.setVideoURI(uri);

            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);

            // Set an OnCompletionListener to start ChoiceActivity after video completion
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent(videoActivity.this, ChoiceActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Start the video playback
            videoView.start();

        }
}