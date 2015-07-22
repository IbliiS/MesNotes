package com.project.baptiste.mesnoteas.fragment;

import android.widget.Toast;

import com.project.baptiste.mesnoteas.AjouterAnneeActivity;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

/**
 * Created by Baptiste on 21/07/2015.
 */
public class DialogModificationAnnee extends DialogModification {
    private IAnnee annee;
    private String nomAnnee;
    private AjouterAnneeActivity.Refresh refresh;

    @Override
    public void supprimer() {
        runBDD.open();
        runBDD.getAnneeMoyenneBdd().removeWithID(annee.getId());
        runBDD.close();
        refresh.refresh();
        Toast.makeText(this.getActivity().getApplication().getBaseContext(), "1 année supprimée", Toast.LENGTH_LONG).show();
    }

    @Override
    public void modifier() {
        refresh.modifier();
    }

    @Override
    public void initText() {
        Utilitaire ut = new Utilitaire();
        annee = (IAnnee) data;
        nomAnnee = annee.getNomAnnee();
        runBDD.open();
        int nbMoyenne = runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()).size();
        runBDD.close();
        builder.setMessage("Année  " + nomAnnee + " \n"
                + "Contient " + nbMoyenne + " période(s)\n"
                + "Moyenne = " + ut.coupeMoyenne(annee.getMoyenne())
        );

    }

    @Override
    public void initTitle() {
        builder.setTitle("Année");
    }

    public void setRefresh(Object refresh) {
        this.refresh = (AjouterAnneeActivity.Refresh) refresh;
    }
}
