/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */
/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class FollowRequestListAdapter extends ArrayAdapter<String>{
    private ArrayList<String> followRequestList;
    private Context context;
    followProtocol requestHandler = new followProtocol();
    //FirebaseSearch userSearcher = new FirebaseSearch();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();;

    /**
     * Constructor for FollowRequestListAdapter
     * @param context
     *  current context
     * @param followRequestList
     *  list of habit events to add
     */
    public FollowRequestListAdapter( Context context, ArrayList<String> followRequestList) {
        super(context,0, followRequestList);
        this.followRequestList= followRequestList;
        this.context = context;
        Log.d(TAG,"ADAPTER SUCCESS");
    }


    /**
     * A function called when populating ListHabitEvent
     * @param position
     *  position in data list of current HabitEvent
     * @param convertView
     * @param parent
     * @return
     *  populated habit event content view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_follow_request_list, parent,false);
        }
        //gets habit event from list and formats listview box with habit information
        String requestUser = followRequestList.get(position);
        Log.d(TAG,"get view SUCCESS");
        TextView requestUserName = view.findViewById(R.id.follower_name_text);
        TextView requestUserId = view.findViewById(R.id.follower_id_text);
        Button confirmButton = view.findViewById(R.id.confirm_Follow_button);
        Button rejectButton = view.findViewById(R.id.reject_Follow_button);

        Log.d(TAG,"idmessage"+requestUser);
        DocumentReference docRef = db.collection("Users").document(requestUser);
        final User[] searchedUser = {new User()};
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                searchedUser[0] = documentSnapshot.toObject(User.class);
                requestUserName.setText(searchedUser[0].getFirst()+" "+ searchedUser[0].getLast());
                Log.d(TAG,"Get user name:"+searchedUser[0].getFirst());
            }
        });
        Log.d(TAG,"Usernamemessage"+ searchedUser[0].getFirst());
        //requestUserName.setText(user.getFirst()+" "+ user.getLast());
        requestUserId.setText(requestUser);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestHandler.commitFollow(requestUser);
                requestHandler.handleRequest(requestUser);
                followRequestList.remove(position);
                FollowRequestListAdapter.this.notifyDataSetChanged();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestHandler.handleRequest(requestUser);
                followRequestList.remove(position);
                FollowRequestListAdapter.this.notifyDataSetChanged();
            }
        });


        return view;
    }

    /**
     * An example method for data retrieval.  Can be expanded in scope and complexity for
     * sake of reusability.
     * @param ID
     * @return
     */
    public User searchUser(String ID){
        DocumentReference docRef = db.collection("Users").document(ID);
        //retrieves given user from database
        //note that user will be a dummy user (no data) on failure!
        //furthermore, this was actually a solution recommended to me by android studios to
        //evade the need for "final" or making searchedUser global.
        final User[] searchedUser = {new User()};
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                searchedUser[0] = documentSnapshot.toObject(User.class);
                Log.d(TAG,"Get user name:"+searchedUser[0].getFirst());
            }
        });
        return searchedUser[0];
    }


}
