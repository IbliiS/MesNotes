package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
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
public class AnneeBdd implements IObjetBdd {

    private List<IObjet> annees;
    private Utilitaire utilitaire;

    /** TABLE ANNEE */
    private static final String TABLE_ANNEE = "table_annees";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "Nom";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_MOYENNE = "Moyenne";
    private static final int NUM_COL_MOYENNE = 2;

    private int nbElements = 0;
    private RunBDD runBDD;

    public AnneeBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        utilitaire = new Utilitaire();
        //getAll();
    }

    @Override
    public long insert(IObjet objet) {
        IAnnee a = null;
        IAnnee annee = (IAnnee) objet;
        int i = annees.indexOf(annee);
        if (i != -1){
            a = (IAnnee) annees.get(i);
        }
//        int j = runBDD.getObjets().indexOf(objet);
//        if(j != -1){
//            a = (IAnnee) runBDD.getObjets().get(j);
//        }
        Boolean estVide = annees.size() == 0;
        if( estVide || a == null) {
            ContentValues values = new ContentValues();
            values.put(COL_NOM, annee.getNomAnnee());
            values.put(COL_MOYENNE, annee.getMoyenne());
            int l = (int) runBDD.getBdd().insert(TABLE_ANNEE, null, values);
            annee.setId(l);
            annees.add(annee);
            return l;
        }
        else return a.getId();

    }

    @Override
    public int update(int id, IObjet objet) {
        IAnnee anneeADelete = new Annee();
        IAnnee annee = (IAnnee) objet;
        IAnnee a;
        for(IObjet o : annees){
            a = (IAnnee) o;
            if(a.getId() == annee.getId()){
                anneeADelete = a;
            }
        }
        annees.remove(anneeADelete);
        ContentValues values = new ContentValues();
        values.put(COL_NOM, annee.getNomAnnee());
        values.put(COL_MOYENNE, annee.getMoyenne());
        annee.setId(id);
        annees.add(annee);
        return runBDD.getBdd().update(TABLE_ANNEE, values, COL_ID + " = " + id, null);
    }

    @Override
    public int removeWithID(int id) {
        IAnnee anneeADelete = new Annee();
        boolean b = false;
        IAnnee a;
        for(IObjet o : annees){
            a = (IAnnee) o;
            if(a.getId() == id){
                anneeADelete = a;
                b = true;
            }
        }
        if(b) {
            annees.remove(anneeADelete);
        }
        return runBDD.getBdd().delete(TABLE_ANNEE, COL_ID + " = " + id, null);
    }

    @Override
    public int removeWithName(String s) {
        int i =-1;
        IAnnee anneeADelete = new Annee();
        IAnnee a;
        boolean b = false;
        for(IObjet o : annees){
            a = (IAnnee) o;
            if(a.getNomAnnee().equals(s)){
                anneeADelete = a;
                b = true;
            }
        }
        if(b) {
            i = anneeADelete.getId();
            annees.remove(anneeADelete);
        }
        return runBDD.getBdd().delete(TABLE_ANNEE, COL_NOM + " = '" + s +"'", null);
    }

    @Override
    public IObjet getWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_ANNEE + " WHERE ID=" + i, null);
        IAnnee a = (IAnnee) cursorToObject(c);
        a.setMoyennes(runBDD.getAnneeMoyenneBdd().getListObjetWithId(a.getId()));
        return a;
    }

    @Override
    public IObjet getWithName(String nom) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_ANNEE  + " where "+ COL_NOM +" = '" + nom + "'", null);
        IAnnee a = (IAnnee) cursorToObject(c);
        a.setMoyennes(runBDD.getAnneeMoyenneBdd().getListObjetWithId(a.getId()));
        return a;
    }

    @Override
    public List<IObjet> getAll() {
        open();
        int nbElem = getNbElements();
        close();
        if(annees == null){
            annees = new ArrayList<>();
        }
        else if(annees.size() == 0 || nbElem != annees.size()){
            annees.clear();
            int cpt = 0;
            int j = nbElem;
            for(int i = 1; i<=j;i++){
                IAnnee annee = new Annee();
                open();
                annee = (IAnnee) getWithId(i);
                if(! (annee.getNomAnnee().equals(""))){
                    cpt++;
                    annee.setMoyennes(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
                    annees.add(annee);
                }
                if(cpt != nbElem){
                    j++;
                }
            }

        }
        close();
        return utilitaire.copyList(annees);
    }

    @Override
    public IObjet cursorToObject(Cursor c) {
        if (c.getCount() == 0) {
            c.close();
            return new Annee();
        }
        c.moveToFirst();
        IAnnee annee  = new Annee();
        annee.setId(c.getInt(NUM_COL_ID));
        annee.setNomAnnee(c.getString(NUM_COL_NOM));
        annee.setMoyenne(c.getDouble(NUM_COL_MOYENNE));
        c.close();
        return annee;
    }

    @Override
    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    @Override
    public int taille() {
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_ANNEE);
        return (int) s.simpleQueryForLong();
    }

    @Override
    public void open() {
        runBDD.open();
    }

    @Override
    public void close() {
        runBDD.close();
    }

    @Override
    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_ANNEE, null, null);
        close();
    }
}
