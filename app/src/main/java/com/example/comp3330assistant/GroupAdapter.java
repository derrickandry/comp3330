package com.example.comp3330assistant;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class GroupAdapter extends ArrayAdapter<Group> implements View.OnClickListener {
    private ArrayList<Group> Groups;
    Context mContext;
    private int resourceLayout;
    private StorageReference mStorageRef;


    public GroupAdapter(ArrayList<Group> groups, Context context, int resource) {
        super(context, resource, groups);
        this.mContext=context;
        this.resourceLayout = resource;

    }
    @Override
    public void onClick(View v) {

    }
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Group groupItem = getItem(position);

        if (groupItem != null) {
            TextView head = (TextView) v.findViewById(R.id.head);
            TextView subtitle = (TextView) v.findViewById(R.id.subtitle);
            TextView description = (TextView) v.findViewById(R.id.description);
            ImageView logo = (ImageView) v.findViewById(R.id.img);

            String headMessage = groupItem.getGroupNumber() + " - " + groupItem.getAppName();
            head.setText(headMessage);

            String subtitleMessage = "By: " + groupItem.AllMembers();
            subtitle.setText(subtitleMessage);

            description.setText(groupItem.getDescription());

            mStorageRef = FirebaseStorage.getInstance().getReference().child("logo/" + groupItem.getLogo());
            Log.d(null, mStorageRef.toString());
            GlideApp.with(v)
                    .load(mStorageRef)
                    .into(logo);
        }

        return v;
    }

}

