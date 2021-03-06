/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sisyphus.R;

/**
 * Fragment that serves as a general-use popup to tell users when they have made a mistake
 */
public class errorFragment extends DialogFragment {

    private String message;
    public errorFragment(String message){
        this.message = message;
    }


    /**
     * function to create a view to display an error message
     * @param savedInstanceState
     *  previous view
     * @return
     *  AlertDialog to display error message
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.error_fragment, null);

        //no actions necessary at this step, so dialog box has no input
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setView(view)
                .setTitle("Oops! An error occurred!")
                .setMessage(message)
                .setNegativeButton("Ok", null)
                .create();


    }


}
