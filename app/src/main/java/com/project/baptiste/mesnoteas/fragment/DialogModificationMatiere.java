package com.project.baptiste.mesnoteas.fragment;

import android.widget.Toast;

import com.project.baptiste.mesnoteas.AjouterMatiereActivity;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.List;

/**
 * Created by Baptiste on 23/07/2015.
 */
public class DialogModificationMatiere extends DialogModification {
    private IMatiere matiere;
    private String nomMatiere;
    private AjouterMatiereActivity.Refresh refresh;

    @Override
    public void supprimer() {
        runBDD.open();
        runBDD.getMoyenneMatiereBdd().removeWithID(matiere.getId());
        runBDD.close();
        refresh.refresh();
        Toast.makeText(this.getActivity().getApplication().getBaseContext(), "1 matière supprimée", Toast.LENGTH_LONG).show();
    }

    @Override
    public void modifier() {
        refresh.modifier();
    }

    @Override
    public void initText() {
        Utilitaire ut = new Utilitaire();
        matiere = (IMatiere) data;
        nomMatiere = matiere.getNomMatiere();
        runBDD.open();
        IMoyenne moy = (IMoyenne) runBDD.getMoyenneMatiereBdd().getOtherObjetWithId(matiere.getId());
        String nomMoyenne = moy.getNomMoyenne();
        String nomAnnee = ((IAnnee) runBDD.getAnneeMoyenneBdd().getOtherObjetWithId(moy.getId())).getNomAnnee();
        matiere.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(matiere.getId()));
        int nbNotes = matiere.getNotes().size();
        runBDD.close();
        builder.setMessage("Matière  " + nomMatiere + "\n"
                        + "Contenue dans l'année " + nomAnnee + ", période " + nomMoyenne + "\n"
                        + "Contient " + nbNotes + " note(s)\n"
                        + "Moyenne = " + ut.coupeMoyenne(matiere.getMoyenne())
        );
    }

    @Override
    public void initTitle() {
        builder.setTitle("Matière");
    }

    public void setRefresh(Object refresh) {
        this.refresh = (AjouterMatiereActivity.Refresh) refresh;
    }
}
