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

public class errorFragment extends DialogFragment {

    private String message;
    public errorFragment(String message){
        this.message = message;
    }



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
