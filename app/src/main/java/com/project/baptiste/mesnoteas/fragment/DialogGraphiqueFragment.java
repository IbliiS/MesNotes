package com.project.baptiste.mesnoteas.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Baptiste on 29/06/2015.
 */
public class DialogGraphiqueFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Vous n'avez pas de notes dans cette année, les graphiques seront vides.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle("Attention");
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
