package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.general.Annee;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 19/06/2015.
 */
public class AnneeMoyenneBdd implements IObjetAssoBdd {

    private RunBDD runBDD;

    private int nbElements;
    private List<IObjet> anneesMoyennes;
    private Utilitaire utilitaire;

    /** TABLE ASSOCIATIVE ANNEEMOYENNE */
    private static final String TABLE_ANNEEMOYENNE = "table_anneemoyenne";
    private static final String COL_REFANNEE = "RefAnnee";
    private static final int NUM_COL_REFANNEE = 0;
    private static final String COL_REFMOYENNE = "RefMoyenne";
    private static final int NUM_COL_REFMOYENNE = 1;

    public AnneeMoyenneBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        anneesMoyennes = new ArrayList<>();
        utilitaire = new Utilitaire();
        //getAll();

    }

    @Override
    public void open() {
        runBDD.open();
    }

    @Override
    public void close() {
        runBDD.close();
    }

    /**
     *
     * @param objet1 la moyenne
     * @param objet2 l'année
     * @return l'id
     */
    @Override
    public long insert(IObjet objet1, IObjet objet2) {
        IAnnee annee = (IAnnee) objet2;
        IMoyenne moyenne = (IMoyenne) objet1;
        if( ! (anneesMoyennes.contains(annee))){
            anneesMoyennes.add(annee);
        }
        ContentValues values = new ContentValues();
        values.put(COL_REFANNEE, annee.getId());
        values.put(COL_REFMOYENNE, moyenne.getId());
        return runBDD.getBdd().insert(TABLE_ANNEEMOYENNE, null, values);
    }


    /**
     * Retourne la liste de moyenne d'une année
     * @param i id de l'année
     * @return liste de moyenne
     */
    @Override
    public List<IObjet> getListObjetWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_ANNEEMOYENNE + " WHERE "+ COL_REFANNEE + "=" + i, null);
        return cursorToObject(c);
    }

    @Override
    public List<IObjet> cursorToObject(Cursor c) {
        List<IObjet> moyennes = new ArrayList<>();
        if(c.getCount()==0){
            c.close();
            return moyennes;
        }
        if(c.moveToFirst()){
            IMoyenne moyenne;
            int idMoyenne;
            while(!c.isAfterLast()){
                moyenne = new Moyenne();
                idMoyenne = c.getInt(NUM_COL_REFMOYENNE);
                moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithId(idMoyenne);
                moyenne.setMatieres(runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId()));
                moyennes.add(moyenne);
                c.moveToNext();
            }
        }
        c.close();
        return moyennes;
    }

    @Override
    public IObjet getOtherObjetWithId(int id) {
        return null;
    }

    @Override
    public IObjet cursorToOtherObject(Cursor c) {
        return null;
    }

    @Override
    public List<IObjet> getAll() {
        open();
        if(anneesMoyennes.size() == 0 || getNbElements() != anneesMoyennes.size()){
            anneesMoyennes.clear();
            IAnnee annee;
            int cpt = 0;
            int nbAnnees = runBDD.getAnneeBdd().getNbElements();
            int j = nbAnnees;
            for(int i = 0; i <= j; i++){
                annee = new Annee();
                annee = (IAnnee) runBDD.getAnneeBdd().getWithId(i);
                if( ! (annee.getNomAnnee().equals(""))){
                    cpt ++;
                    annee.setMoyennes(getListObjetWithId(i));
                    anneesMoyennes.add(annee);
                }
            }
        }
        close();
        return utilitaire.copyList(anneesMoyennes);
    }

    @Override
    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    @Override
    public int taille() {
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_ANNEEMOYENNE);
        return (int) s.simpleQueryForLong();
    }

    @Override
    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_ANNEEMOYENNE,null,null);
        close();
    }

    @Override
    public int removeWithID(int id) {
        IAnnee anneeADelete = new Annee();
        boolean b = false;
        IAnnee a;
        for(IObjet o : anneesMoyennes){
            a = (IAnnee) o;
            if(a.getId() == id){
                anneeADelete = a;
                b = true;
            }
        }
        if(b){
            /** ON SUPPRIME TOUTES LES MOYENNE DE L'ANNEE **/
            IMoyenne moy;
            for(IObjet o : anneeADelete.getMoyennes()){
                moy = (IMoyenne) o;
                runBDD.getMoyenneBdd().removeWithID(moy.getId());
            }
            anneesMoyennes.remove(anneeADelete);
        }
        runBDD.getAnneeBdd().removeWithID(id);
        return runBDD.getBdd().delete(TABLE_ANNEEMOYENNE, COL_REFANNEE + " = " + id, null);

    }

    @Override
    public int removeOtherObjectWithID(int id) {
        return 0;
    }
}
