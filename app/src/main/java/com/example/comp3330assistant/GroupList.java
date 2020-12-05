package com.example.comp3330assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        listView = findViewById(R.id.listView);
        final ArrayList<Group> Groups = new ArrayList<>();
        final GroupAdapter groupAdapter = new GroupAdapter(Groups, this, R.layout.group_row);
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(GroupList.this, ProjectShowcase.class);
                intent.putExtra("apk", Groups.get(position).getAPK());
                intent.putExtra("videoLink", Groups.get(position).getVideo());
                intent.putExtra("members", Groups.get(position).AllMembers());
                intent.putExtra("description", Groups.get(position).getDescription());
                intent.putExtra("logo", Groups.get(position).getLogo());
                intent.putExtra("appName", Groups.get(position).getAppName());
                intent.putExtra("source", Groups.get(position).getSource());
                intent.putExtra("details", Groups.get(position).getDetails());
                startActivity(intent);
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Groups.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Group group = ds.getValue(Group.class);
                    group.setGroupNumber(ds.getKey());
                    Groups.add(group);
                    Log.d("asdf", group.getAppName());
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}