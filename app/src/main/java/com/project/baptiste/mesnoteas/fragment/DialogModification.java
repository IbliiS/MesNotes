package com.project.baptiste.mesnoteas.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.project.baptiste.mesnoteas.AccueilActivity;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

/**
 * Created by Baptiste on 19/07/2015.
 */
public abstract class DialogModification extends DialogFragment {
    protected AlertDialog.Builder builder;
    protected IObjet data;
    protected RunBDD runBDD;
    protected AccueilActivity.Refresh refresh;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        initText();
        builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                supprimer();
                refresh.refresh();
            }
        });
        builder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modifier();
            }
        });
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        initTitle();
        AlertDialog dialog = builder.create();

        return dialog;
    }


    public void setData(IObjet d){
        data = d;
    }

    public void setRunBDD(RunBDD runBDD) {
        this.runBDD = runBDD;
    }

    public void setRefresh(AccueilActivity.Refresh refresh) {
        this.refresh = refresh;
    }

    public abstract void supprimer();

    public abstract void modifier();

    public abstract void initText();

    public abstract void initTitle();
}
