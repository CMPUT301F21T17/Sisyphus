
//
//    /**
//     * Stores a user to firebase
//     * @param userID
//     * the userID to store the data under
//     * @param user
//     * The user data to be stored
//     */
//    public void storeUser(String userID, User user){
//        collectionReference
//                .document(userID).set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // These are a method which gets executed when the task is succeeded
//                        Log.d(TAG, "Data has been added successfully!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // These are a method which gets executed if there’s any problem
//                        Log.d(TAG, "Data could not be added!" + e.toString());
//                    }
//
//                });
//    }

    /**
     * Stores a habit to firebase
     * @param userID
     * The userID of the user to store data under
     * @param habit
     * The data of the habit to be stored
     */
    public void storeHabit(String userID, Habit habit){
        collectionReference
                //habit given a database name the same as the title given to it by a user
                .document(userID).collection("Habits").document(habit.getName()).set(habit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }

                });
    }

//    /**
//     * Stores a habit event to firebase
//     * @param userID
//     * The userID of the user to store data under
//     * @param habitName
//     * The name of the habit to which the event belongs
//     * @param habitEvent
//     * The habit event to be stored
//     */
//    public void storeHabitEvent(String userID, String habitName, HabitEvent habitEvent){
//        // creating a unique name for the habit event based on current date and time
//        Date today = new Date();
//        String eventName = habitName + " event " + today;
//        collectionReference
//                .document(userID).collection("Habits").document(habitName).collection("HabitEvent").document(eventName).set(habitEvent)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // These are a method which gets executed when the task is succeeded
//                        Log.d(TAG, "Data has been added successfully!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // These are a method which gets executed if there’s any problem
//                        Log.d(TAG, "Data could not be added!" + e.toString());
//                    }
//
//                });
//   }
    public void deleteHabit(String userId,String habitName){
        collectionReference
                //habit given a database name the same as the title given to it by a user
                .document(userId).collection("Habits").document(habitName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }




}