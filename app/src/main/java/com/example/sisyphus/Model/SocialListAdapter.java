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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sisyphus.R;
import com.example.sisyphus.View.SocialView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SocialListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<String> expandableListTitle;//List of following User's name
    private HashMap<String, ArrayList<Habit>> expandableListDetail;//Storing a HabitList(value) for each following User(Key)
    private HashMap<String, ArrayList<String>> expandableListPercents; //Storing the percent completion of each followed user habit
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public SocialListAdapter(Context context, List<String> expandableListTitle, HashMap<String, ArrayList<Habit>> expandableListDetail, HashMap<String, ArrayList<String>> expandableListPercents) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.expandableListPercents = expandableListPercents;
    }

    /**
     * function to get group count
     * @return
     *  group size
     */
    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    /**
     * function to get child count
     * @param listPosition
     * @return
     *  child size
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    /**
     * function to get group's item
     * @param listPosition
     * @return
     *  group's item'
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    /**
     * function to get child's item
     * @param listPosition
     * @param expandedListPosition
     * @return
     *  child's item
     */
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    /**
     * function to get group's ID
     * @param listPosition
     * @return
     *  group's id
     */
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    /**
     * function to get child's ID
     * @param listPosition
     * @param expandedListPosition
     * @return
     *  child's id
     */
    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public Object getProgress(int listPosition, int expandedListPosition) {
        return this.expandableListPercents.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    /**
     * A function called when populating SocialView
     * @param listPosition
     *  position in data list of current following User
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     *  populated following user content view
     */
    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.social_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.socialListTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);

        //Set First and Last name
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
        return convertView;
    }

    /**
     * A function called when populating SocialView
     * @param listPosition
     *  position in data list of current following User's habit
     * @param expandedListPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     *  populated following user's habit content view
     */
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Habit followHabit = (Habit) getChild(listPosition, expandedListPosition);
        final String habitPercent = (String) getProgress(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.social_list_item, null);
        }

        //Set Habit's name
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.socialExpandedListItem);
        expandedListTextView.setText(followHabit.getHabitName());

        //Set Progress bar
        ProgressBar indicator = (ProgressBar) convertView.findViewById(R.id.progressBar);
        indicator.setProgress(Integer.valueOf(habitPercent));


        return convertView;
    }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
}
