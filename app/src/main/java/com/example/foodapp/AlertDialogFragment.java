package com.example.foodapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {
    private String message;

    public AlertDialogFragment(String message){
        this.message = message;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Builder is a static nested class of AltertDialog class
        builder.setTitle(R.string.Error_title).setMessage(message).setPositiveButton(R.string.Error_button_text, null);
        return builder.create();
    }

}
