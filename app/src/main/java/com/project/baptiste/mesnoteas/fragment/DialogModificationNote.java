package com.project.baptiste.mesnoteas.fragment;

import android.content.Intent;
import android.widget.Toast;

import com.project.baptiste.mesnoteas.AccueilActivity;
import com.project.baptiste.mesnoteas.AjouterNoteActivity;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

/**
 * Created by Baptiste on 19/07/2015.
 */
public class DialogModificationNote extends DialogModification {
    private INote note;
    private String nomMatiere;
    private AccueilActivity.Refresh refresh;


    @Override
    public void supprimer() {
        runBDD.open();
        runBDD.getMatiereNoteBdd().removeOtherObjectWithID(note.getId());
        runBDD.getNoteBdd().removeWithID(note.getId());
        runBDD.close();
        Toast.makeText(this.getActivity().getApplication().getBaseContext(), "1 note supprimée", Toast.LENGTH_LONG).show();
        refresh.refresh();
    }

    @Override
    public void modifier() {
        refresh.modifier(note);
        Toast.makeText(this.getActivity().getApplication().getBaseContext(), "Modification "+note.getNote()+" dans "+nomMatiere, Toast.LENGTH_LONG).show();

    }

    @Override
    public void initText() {
        note = (INote) data;
        runBDD.open();
        IMatiere mat =(IMatiere) runBDD.getMatiereNoteBdd().getOtherObjetWithId(note.getId());
        IMoyenne moy = (IMoyenne) runBDD.getMoyenneMatiereBdd().getOtherObjetWithId(mat.getId());
        IAnnee ann = (IAnnee) runBDD.getAnneeMoyenneBdd().getOtherObjetWithId(moy.getId());
        nomMatiere =  mat.getNomMatiere();
        runBDD.close();
        builder.setMessage("Note  " + note.getNote() + " Coef  " + note.getCoef() + " \n\n"
                        + "Année "+ann.getNomAnnee() + " \n"
                        + "Période " +moy.getNomMoyenne() + " \n"
                        + "Matière  " + nomMatiere + ", Moyenne = " + mat.getMoyenne() + " \n"
        );
    }

    @Override
    public void initTitle() {
        builder.setTitle("Note");
    }

    public void setRefresh(Object refresh) {
        this.refresh = (AccueilActivity.Refresh) refresh;
    }
}
