package com.project.baptiste.mesnoteas.fragment;

import android.widget.Toast;

import com.project.baptiste.mesnoteas.AjouterMoyenneActivity;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

/**
 * Created by Baptiste on 22/07/2015.
 */
public class DialogModificationMoyenne extends DialogModification {
    private IMoyenne moyenne;
    private String nomMoyenne;
    private AjouterMoyenneActivity.Refresh refresh;
    @Override
    public void supprimer() {
        runBDD.open();
        runBDD.getMoyenneMatiereBdd().removeWithID(moyenne.getId());
        runBDD.close();
        refresh.refresh();
        Toast.makeText(this.getActivity().getApplication().getBaseContext(), "1 période supprimée", Toast.LENGTH_LONG).show();

    }

    @Override
    public void modifier() {
        refresh.modifier();
    }

    @Override
    public void initText() {
        Utilitaire ut = new Utilitaire();
        moyenne = (IMoyenne) data;
        nomMoyenne = moyenne.getNomMoyenne();
        runBDD.open();
        String nomAnnee = ((IAnnee) runBDD.getAnneeMoyenneBdd().getOtherObjetWithId(moyenne.getId())).getNomAnnee();
        int nbMatieres = runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId()).size();
        runBDD.close();
        builder.setMessage("Moyenne  " + nomMoyenne + " est dans " + nomAnnee + "\n"
                        + "Contient " + nbMatieres + " matière(s)\n"
                        + "Moyenne = " + ut.coupeMoyenne(moyenne.getMoyenne())
        );
    }

    @Override
    public void initTitle() {
        builder.setTitle("Période");

    }

    public void setRefresh(Object refresh) {
        this.refresh = (AjouterMoyenneActivity.Refresh) refresh;
    }
}
