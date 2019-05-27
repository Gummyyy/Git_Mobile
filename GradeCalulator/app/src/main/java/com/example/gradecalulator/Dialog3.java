package com.example.gradecalulator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatDialogFragment;
//dialog class
// code reference :https://youtu.be/Jho1GC6cuVU
public class Dialog3 extends AppCompatDialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle("Username Error")
                .setMessage("Username already exists, please choose another username.")
                .setPositiveButton("Ok", new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
        return builder.create();
    }
}