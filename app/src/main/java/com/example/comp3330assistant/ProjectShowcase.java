package com.example.comp3330assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ProjectShowcase extends AppCompatActivity {

    Button download;
    StorageReference ref;
    YouTubePlayerView youTubePlayerView;
    Intent intent;
    private static final String TAG_Video = "Getting video link";
    private static final String TAG_APK = "Getting apk link";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        Log.d(TAG_Video, intent.getStringExtra("videoLink"));
        Log.d(TAG_APK, intent.getStringExtra("apk"));
        setContentView(R.layout.activity_showcase);
        download = findViewById(R.id.download);
        setAllTexts();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = intent.getStringExtra("videoLink");
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        youTubePlayerView.release();
    }
    private void setAllTexts() {
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        final TextView appName = (TextView) findViewById(R.id.appName);
        final TextView description = (TextView) findViewById(R.id.description);
        final TextView detailsContent = (TextView) findViewById(R.id.detailsContent);
        final TextView membersContent = (TextView) findViewById(R.id.membersContent);
        final TextView sourceContent = (TextView) findViewById(R.id.sourceContent);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("logo/" + intent.getStringExtra("logo"));
        GlideApp.with(this)
                .load(mStorageRef)
                .into(logo);
        appName.setText(intent.getStringExtra("appName"));
        description.setText(intent.getStringExtra("description"));
        detailsContent.setText(intent.getStringExtra("details"));
        membersContent.setText(intent.getStringExtra("members"));
        sourceContent.setText(intent.getStringExtra("source"));
    }

    public void download(){

        ref = FirebaseStorage.getInstance().getReference().child("apk/" + intent.getStringExtra("apk"));
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFile(ProjectShowcase.this, intent.getStringExtra("appName"), "apk", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+ fileExtension);

        downloadManager.enqueue(request);
    }
}