package com.project.baptiste.mesnoteas.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.project.baptiste.mesnoteas.AccueilActivity;
import com.project.baptiste.mesnoteas.bdd.RunBDD;

/**
 * Created by Baptiste on 23/07/2015.
 */
public class DialogResetBdd extends DialogFragment {
    private RunBDD runBDD;
    private AccueilActivity.Refresh refresh;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Vous êtes sur le point de vider la base de données, vous perdrez toutes vos données.")
                .setPositiveButton("Vider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runBDD.viderBdd();
                        refresh.reset();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setTitle("Attention");
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void setRunBDD(RunBDD runBDD) {
        this.runBDD = runBDD;
    }

    public void setRefresh(AccueilActivity.Refresh refresh) {
        this.refresh = refresh;
    }
}
