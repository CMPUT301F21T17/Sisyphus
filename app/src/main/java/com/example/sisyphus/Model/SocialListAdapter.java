/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.sisyphus.R;
import com.example.sisyphus.View.SocialView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SocialListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, ArrayList<Habit>> expandableListDetail;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public SocialListAdapter(Context context, List<String> expandableListTitle, HashMap<String, ArrayList<Habit>> expandableListDetail) {
        Log.d("sociallistconstructor","success");
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        //Log.d("adapter'ssettitle:",expandableListTitle.get(0));
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.social_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.socialListTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);

        DocumentReference docRef = db.collection("Users").document(listTitle);
        final User[] searchedUser = {new User()};
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                searchedUser[0] = documentSnapshot.toObject(User.class);
                listTitleTextView.setText(searchedUser[0].getFirst()+" "+ searchedUser[0].getLast());
                Log.d(TAG,"Get user name:"+searchedUser[0].getFirst());
            }
        });

        //listTitleTextView.setText(listTitle);
        Log.d("setListttle",listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Habit followHabit = (Habit) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.social_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.socialExpandedListItem);
        expandedListTextView.setText(followHabit.getHabitName());
        Log.d("expandListtext",followHabit.getHabitName());
        return convertView;
    }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
}
