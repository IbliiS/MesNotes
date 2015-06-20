package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 16/06/2015.
 */
public class MoyenneBdd implements IObjetBdd {

    private List<IObjet> moyennes;
    private Utilitaire utilitaire;

    /** TABLE MOYENNE */
    private static final String TABLE_MOYENNE = "table_moyennes";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "Nom";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_MOYENNE = "Moyenne";
    private static final int NUM_COL_MOYENNE = 2;

    private int nbElements = 0;
    private RunBDD runBDD;

    public MoyenneBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        utilitaire = new Utilitaire();
        getAll();

    }

    @Override
    public long insert(IObjet objet){
        IMoyenne m = null;
        IMoyenne moyenne = (IMoyenne) objet;
        int i = moyennes.indexOf(moyenne);
        if (i != -1){
            m = (IMoyenne) moyennes.get(i);
        }
        Boolean estVide = moyennes.size() == 0;
        if( estVide || m == null) {
            ContentValues values = new ContentValues();
            values.put(COL_NOM, moyenne.getNomMoyenne());
            values.put(COL_MOYENNE, moyenne.getMoyenne());
            int l = (int) runBDD.getBdd().insert(TABLE_MOYENNE, null, values);
            moyenne.setId(l);
            moyennes.add(moyenne);
            return l;
        }
        else return m.getId();
    }

    @Override
    public int update(int id, IObjet objet){
        IMoyenne moyenneADelete = new Moyenne();
        IMoyenne moyenne = (IMoyenne) objet;
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            if(m.getId() == moyenne.getId()){
                moyenneADelete = m;
            }
        }
        moyennes.remove(moyenneADelete);
        ContentValues values = new ContentValues();
        values.put(COL_NOM, moyenne.getNomMoyenne());
        values.put(COL_MOYENNE, moyenne.getMoyenne());
        moyenne.setId(id);
        moyennes.add(moyenne);
        return runBDD.getBdd().update(TABLE_MOYENNE, values, COL_ID + " = " + id, null);
    }

    @Override
    public int removeWithID(int id){
        IMoyenne moyenneADelete = new Moyenne();
        boolean b = false;
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            if(m.getId() == id){
                moyenneADelete = m;
                b = true;
            }
        }
        if(b) {
            moyennes.remove(moyenneADelete);
        }
        return runBDD.getBdd().delete(TABLE_MOYENNE, COL_ID + " = " + id, null);
    }

    @Override
    public int removeWithName(String s){
        int i =-1;
        IMoyenne moyenneADelete = new Moyenne();
        IMoyenne m;
        boolean b = false;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            if(m.getNomMoyenne().equals(s)){
                moyenneADelete = m;
                b = true;
            }
        }
        if(b) {
            i = moyenneADelete.getId();
            moyennes.remove(moyenneADelete);
        }
        return runBDD.getBdd().delete(TABLE_MOYENNE, COL_ID + " = " + i, null);
    }

    @Override
    public IObjet getWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MOYENNE + " WHERE ID=" + i, null);
        return cursorToObject(c);
    }

    @Override
    public IObjet getWithName(String nom) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MOYENNE  + " where "+ COL_NOM +" = '" + nom + "'", null);
        return cursorToObject(c);
        /*IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            if(m.getNomMoyenne().equals(nom)){
                return m;
            }
        }
        return new Moyenne(); */
    }

    @Override
    public List<IObjet> getAll(){
        open();
        int nbElem = getNbElements();
        close();
        if(moyennes == null){
            moyennes = new ArrayList<>();
        }
        else if(moyennes.size() == 0 || nbElem != moyennes.size()){
            moyennes.clear();
            moyennes = runBDD.getMoyenneMatiereBdd().getAll();
        }
        return utilitaire.copyList(moyennes);
    }


    @Override
    public IObjet cursorToObject(Cursor c){
        if (c.getCount() == 0) {
            return new Moyenne();
        }
        c.moveToFirst();
        IMoyenne moyenne  = new Moyenne();
        moyenne.setId(c.getInt(NUM_COL_ID));
        moyenne.setNomMoyenne(c.getString(NUM_COL_NOM));
        moyenne.setMoyenne(c.getDouble(NUM_COL_MOYENNE));
        c.close();
        return moyenne;
    }


    @Override
    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    @Override
    public int taille(){
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_MOYENNE);
        return (int) s.simpleQueryForLong();
    }

    @Override
    public void open(){
        runBDD.open();
    }

    @Override
    public void close(){
        runBDD.close();
    }

    @Override
    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_MOYENNE, null, null);
        close();
    }

}
