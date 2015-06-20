package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 13/06/2015.
 */
public class MatiereBdd implements IObjetBdd {

    private List<IObjet> matieres;
    private Utilitaire utilitaire;

    /** TABLE MATIERE */
    private static final String TABLE_MATIERE = "table_matieres";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "Nom";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_COEF = "Coef";
    private static final int NUM_COL_COEF = 2;
    private static final String COL_MOYENNE = "Moyenne";
    private static final int NUM_COL_MOYENNE = 3;

    private int nbElements = 0;
    private RunBDD runBDD;

    public MatiereBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        utilitaire = new Utilitaire();
        getAll();
    }

    public long insert(IObjet objet){
        IMatiere m = null;
        IMatiere matiere = (IMatiere) objet;
        int i = matieres.indexOf(matiere);
        if (i != -1){
            m = (IMatiere) matieres.get(i);
        }
        Boolean estVide = matieres.size() == 0;
        if( estVide || m == null) {
            ContentValues values = new ContentValues();
            values.put(COL_NOM, matiere.getNomMatiere());
            values.put(COL_COEF, matiere.getCoef());
            values.put(COL_MOYENNE, matiere.getMoyenne());
            int l = (int) runBDD.getBdd().insert(TABLE_MATIERE, null, values);
            matiere.setId(l);
            matieres.add(matiere);
            return l;
        }
        else return m.getId();
    }

    public int update(int id, IObjet objet){
        IMatiere matiereADelete = new Matiere();
        IMatiere matiere = (IMatiere) objet;
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            if(m.getId() == matiere.getId()){
                matiereADelete = m;
            }
        }
        matieres.remove(matiereADelete);
        ContentValues values = new ContentValues();
        values.put(COL_NOM, matiere.getNomMatiere());
        values.put(COL_COEF, matiere.getCoef());
        values.put(COL_MOYENNE, matiere.getMoyenne());
        matiere.setId(id);
        matieres.add(matiere);
        return runBDD.getBdd().update(TABLE_MATIERE, values, COL_ID + " = " + id, null);
    }

    public int removeWithID(int id){
        /*
        IMatiere matiereADelete = new Matiere();
        boolean b = false;
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            if(m.getId() == id){
                matiereADelete = m;
                b = true;
            }
        }
        if(b) {
            matieres.remove(matiereADelete);
        }
        return runBDD.getBdd().delete(TABLE_MATIERE, COL_ID + " = " + id, null);
    */
        IMatiere matiereADelete = (IMatiere) getWithId(id);
        matieres.remove(matiereADelete);
        runBDD.getMatiereNoteBdd().removeWithID(matiereADelete.getId());
        return runBDD.getBdd().delete(TABLE_MATIERE, COL_ID + " = " + id, null);
    }

    public int removeWithName(String s){
        IMatiere matiereADelete = (IMatiere) getWithName(s);
        matieres.remove(matiereADelete);
        runBDD.getMatiereNoteBdd().removeWithID(matiereADelete.getId());
        return runBDD.getBdd().delete(TABLE_MATIERE, COL_NOM + " = '" + s + "'", null);
    }


    public IObjet getWithId(int i) {
         Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIERE + " WHERE ID=" + i, null);
        return cursorToObject(c);
    }

    public IObjet getWithName(String nom) {

        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " +TABLE_MATIERE  + " where "+ COL_NOM +" = '" + nom + "'", null);
        return cursorToObject(c);
    }


    public List<IObjet> getAll(){
        open();
        int nbElem = getNbElements();
        close();
        if(matieres == null){
            matieres = new ArrayList<>();
        }
        else if(matieres.size() == 0 || nbElem != matieres.size()){
            matieres.clear();
            matieres = runBDD.getMatiereNoteBdd().getAll();
        }
        return utilitaire.copyList(matieres);

    }

    public IObjet cursorToObject(Cursor c){
        if (c.getCount() == 0) {
            return new Matiere();
        }
        c.moveToFirst();
        IMatiere matiere = new Matiere();
        matiere.setId(c.getInt(NUM_COL_ID));
        matiere.setNomMatiere(c.getString(NUM_COL_NOM));
        matiere.setCoef(c.getInt(NUM_COL_COEF));
        matiere.setMoyenne(c.getDouble(NUM_COL_MOYENNE));
        c.close();
        return matiere;
    }

    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    public int taille(){
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_MATIERE);
        return (int) s.simpleQueryForLong();
    }

    public void open(){
        runBDD.open();
    }

    public void close(){
        runBDD.close();
    }

    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_MATIERE,null,null);
        close();
    }
}
