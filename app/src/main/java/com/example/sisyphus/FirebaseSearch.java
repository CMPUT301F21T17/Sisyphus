package com.example.sisyphus;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// a class that is designed to be an instantiable search object for the firestore database.
// ideally, all future search methods will live in this class and be universally accesssible
// with minimal effort.
public class FirebaseSearch {

    //all (most) meaningful searches will require a userID of the user to begin with
    private String userID;
    private FirebaseFirestore db;

    public FirebaseSearch(){
        //Default constructor
    }

    public FirebaseSearch(String userID, FirebaseFirestore db){
        this.userID = userID;
        this.db = db;
    }

    /**
     * An example method for data retrieval.  Can be expanded in scope and complexity for
     * sake of reusability.
     * @param ID
     * @return
     */
    public User searchUser(String ID){
        DocumentReference docRef = db.collection("Users").document(ID);

        //note that user will be a dummy user (no data) on failure!
        //furthermore, this was actually a solution recommended to me by android studios to
        //evade the need for "final" or making this global.
        final User[] searchedUser = {new User()};
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                searchedUser[0] = documentSnapshot.toObject(User.class);
            }
        });


        return searchedUser[0];
    }



}
