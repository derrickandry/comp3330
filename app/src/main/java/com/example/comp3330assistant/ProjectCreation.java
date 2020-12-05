package com.example.comp3330assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class ProjectCreation extends AppCompatActivity {
    public static final int LOGO_REQUEST = 1;
    public static final int APK_REQUEST = 2;
    private ImageView logo;
    private Button upload, cancel, submit;
    private StorageReference mRef;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private UploadTask uploadLogo, uploadAPK;
    private String logoName, apkName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_creation);
        mRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        logo = (ImageView) findViewById(R.id.logo);
        upload = (Button) findViewById(R.id.upload);
        cancel = (Button) findViewById(R.id.cancel);
        submit = (Button) findViewById(R.id.submitProject);
        progressBar = (ProgressBar) findViewById(R.id.uploadBar);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"Select file or dir"), LOGO_REQUEST);
                setResult(Activity.RESULT_OK);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"Select file or dir"), APK_REQUEST);
                setResult(Activity.RESULT_OK);
                upload.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAPK.cancel();
                cancel.setVisibility(View.INVISIBLE);
                upload.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               TextInputLayout appNameLayout = findViewById(R.id.appNameInput);
               TextInputLayout demoLayout = findViewById(R.id.demoInput);
               TextInputLayout detailsLayout = findViewById(R.id.detailsInput);
               TextInputLayout groupNumLayout = findViewById(R.id.groupNumInput);
               TextInputLayout membersLayout = findViewById(R.id.membersInput);
               TextInputLayout shortDescLayout = findViewById(R.id.shortDescInput);
               TextInputLayout sourceLayout = findViewById(R.id.sourceInput);

               String appName = appNameLayout.getEditText().getText().toString();
               String demo = demoLayout.getEditText().getText().toString();
               String details = detailsLayout.getEditText().getText().toString();
               String groupNum = groupNumLayout.getEditText().getText().toString();
               String members = membersLayout.getEditText().getText().toString();
               String shortDesc = shortDescLayout.getEditText().getText().toString();
               String source = sourceLayout.getEditText().getText().toString();

               Group newGroup = new Group(apkName, appName, shortDesc, logoName, demo, details, source, members);
               databaseReference.child("Groups").child("Group " + groupNum).setValue(newGroup);
               Toast.makeText(ProjectCreation.this, "Group Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String Fpath = data.getDataString();

            GlideApp.with(this)
                    .load(Fpath)
                    .into(logo);

            Uri myUri = Uri.parse(Fpath);
            logoName = getFileName(myUri);
            uploadLogo = mRef.child("logo/" + getFileName(myUri)).putFile(myUri);
            uploadLogo.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProjectCreation.this, "Upload failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProjectCreation.this, "Logo uploaded!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            upload.setVisibility(View.INVISIBLE);
            String Fpath = data.getDataString();
            Uri myUri = Uri.parse(Fpath);
            apkName = getFileName(myUri);
            uploadAPK = mRef.child("apk/" + getFileName(myUri)).putFile(myUri);
            uploadAPK.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProjectCreation.this, "Upload failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    TextView upload = (TextView) findViewById(R.id.test);
                    upload.setText("APK Uploaded!");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                    int currentProgress = (int) progress;
                    progressBar.setProgress(currentProgress);
                }
            });
        }
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}