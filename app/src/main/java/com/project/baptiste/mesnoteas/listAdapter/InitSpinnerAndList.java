package com.project.baptiste.mesnoteas.listAdapter;
import android.widget.Spinner;

import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 20/06/2015.
 */
public class InitSpinnerAndList{
    private RunBDD runBDD;
    private List<IObjet> notes;
    private List<IObjet> moyennes;
    private List<IObjet> annees;
    private List<IObjet> matieres;
    private List<String> anneeString;
    private List<String> moyenneString;
    private List<String> matiereString;
    private Spinner moyenneSpinner;
    private Spinner anneeSpinner;
    private Spinner matiereSpinner;
    private Utilitaire utilitaire;



    public InitSpinnerAndList(RunBDD runBDD, Spinner moyenneSpinner, Spinner anneeSpinner, Spinner matiereSpinner) {
        this.runBDD = runBDD;
        moyennes = new ArrayList<>();
        annees = new ArrayList<>();
        matieres = new ArrayList<>();
        notes = new ArrayList<>();
        anneeString = new ArrayList<>();
        moyenneString = new ArrayList<>();
        matiereString = new ArrayList<>();
        this.moyenneSpinner = moyenneSpinner;
        this.anneeSpinner = anneeSpinner;
        this.matiereSpinner = matiereSpinner;
        utilitaire = new Utilitaire();
        annees = runBDD.getAnneeBdd().getAll();
        initAnneeSpinner();
    }


    private void initAnneeSpinner(){
        final String selectionner = "-- Selectionner --";
        List<String> exemple = new ArrayList<String>();
        exemple.add(selectionner);
        IAnnee a;
        for(IObjet o : annees){
            a = (IAnnee) o;
            exemple.add(a.getNomAnnee());
        }
        anneeString = exemple;
    }

    private void initMoyenneSpinner(){
        final String toutes = "-- Toutes --";
        List<String> listeMoyenne = new ArrayList<String>();
        listeMoyenne.add(toutes);
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            listeMoyenne.add(m.getNomMoyenne());
        }
        moyenneString = listeMoyenne;
    }

    private void initMatiereSpinner(){
        final String toutes = "-- Toutes --";
        List<String> listeMatiere = new ArrayList<String>();
        listeMatiere.add(toutes);
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            listeMatiere.add(m.getNomMatiere());
        }
        matiereString = listeMatiere;
    }

    public List<IObjet> initMoyenne(String item_selected){
        runBDD.open();
        IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
        moyennes.clear();
        moyennes = utilitaire.copyList(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
        runBDD.close();
        return utilitaire.copyList(moyennes);
    }


    public List<IObjet> initMatiereParMatiere(String item_selected) {
        matieres.clear();
        runBDD.open();
        IMatiere m = (IMatiere) runBDD.getMatiereBdd().getWithName(item_selected);
        m.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
        matieres.add(m);
        runBDD.close();
        return utilitaire.copyList(matieres);
    }


    public List<IObjet> initMatieresParAnnee(String item_selected){
        matieres.clear();
        runBDD.open();
        IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
        List<IObjet> listMoyennes = utilitaire.copyList(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
        List<IObjet> listMatieres = new ArrayList<>();
        IMoyenne m;
        for(IObjet o : listMoyennes){
            m = (IMoyenne) o;
            listMatieres = utilitaire.copyList(runBDD.getMoyenneMatiereBdd().getListObjetWithId(m.getId()));
            IMatiere mat;
            for(IObjet ob : listMatieres){
                mat = (IMatiere) ob;
                matieres.add(mat);
            }
        }
        runBDD.close();
        return utilitaire.copyList(matieres);
    }

    public List<IObjet> initMatieresParMoyenne(String item_selected) {
        matieres.clear();
        runBDD.open();
        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
        List<IObjet> l = new ArrayList<>();
        l = utilitaire.copyList(runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId()));
        IMatiere m;
        for(IObjet o : l){
            m = (IMatiere) o;
            m.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
            matieres.add(m);
        }
        runBDD.close();
        return utilitaire.copyList(matieres);
    }

    private void initNotes(){
        notes.clear();
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            INote n;
            runBDD.open();
            List<IObjet> l = utilitaire.copyList(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
            for(IObjet on : l){
                n = (INote) on;
                notes.add(n);
            }
            runBDD.close();
        }
    }

    public void supprimerNotes(List<IObjet> notes){
        runBDD.open();
        INote n;
        for(IObjet o : notes){
            n = (INote) o;
            runBDD.getMatiereNoteBdd().removeOtherObjectWithID(n.getId());
            runBDD.getNoteBdd().removeWithID(n.getId());
        }
    }

    public List<IObjet> getNotes() {
        initNotes();
        return utilitaire.copyList(notes);
    }

    public List<IObjet> getAnnees() {
        return annees;
    }

    public Spinner getMoyenneSpinner() {
        initMoyenneSpinner();
        return moyenneSpinner;
    }

    public Spinner getAnneeSpinner() {
        initAnneeSpinner();
        return anneeSpinner;
    }

    public Spinner getMatiereSpinner() {
        initMatiereSpinner();
        return matiereSpinner;
    }


    public List<String> getAnneeString() {
        return anneeString;
    }

    public List<String> getMoyenneString() {
        return moyenneString;
    }

    public List<String> getMatiereString() {
        return matiereString;
    }
}
