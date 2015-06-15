package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 13/06/2015.
 */
public class MatiereBdd {

    private List<IMatiere> matieres;

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

    private int nbMatieres = 0;
    private RunBDD runBDD;

    public MatiereBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        matieres = new ArrayList<>();
        getAllMatiere();
    }

    public long insertMatiere(IMatiere matiere){
        IMatiere m = null;
        int i = matieres.indexOf(matiere);
        if (i != -1){
            m = matieres.get(i);
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

    public int updateMatiere(int id, IMatiere matiere){
        IMatiere matiereADelete = new Matiere();
        for(IMatiere m : matieres){
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

    public int removeMatiereWithID(int id){
        IMatiere matiereADelete = new Matiere();
        boolean b = false;
        for(IMatiere m : matieres){
            if(m.getId() == id){
                matiereADelete = m;
                b = true;
            }
        }
        if(b) {
            matieres.remove(matiereADelete);
        }
        return runBDD.getBdd().delete(TABLE_MATIERE, COL_ID + " = " + id, null);
    }

    public int removeMatiereWithName(String s){
        int i =-1;
        IMatiere matiereADelete = new Matiere();
        boolean b = false;
        for(IMatiere m : matieres){
            if(m.getNomMatiere().equals(s)){
                matiereADelete = m;
                b = true;
            }
        }
        if(b) {
            i = matiereADelete.getId();
            matieres.remove(matiereADelete);
        }
        return runBDD.getBdd().delete(TABLE_MATIERE, COL_ID + " = " + i, null);
    }


    public IMatiere getMatiereWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIERE + " WHERE ID=" + i, null);
        return cursorToMatiere(c);
    }

    public IMatiere getMatiereWithName(String nom) {
        //Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIERE + " WHERE " +COL_NOM+ " = " + nom, null);
        //return cursorToMatiere(c);
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " +TABLE_MATIERE  + " where "+ COL_NOM +" = '" + nom + "'", null);
        /**for(IMatiere m : matieres){
            if(m.getNomMatiere().equals(nom)){
                return m;
            }
        }*/
        return cursorToMatiere(c);

    }


    public List<IMatiere> getAllMatiere(){
        open();
        if (matieres.size() == 0 || getNbMatieres() != matieres.size()){
            matieres.clear();
            IMatiere matiere;
            int cpt = 0;
            int nbElement = getNbMatieres();
            int j = nbElement;
            for(int i = 1; i <= j; i++){
                matiere = getMatiereWithId(i);
               if(matiere != null && ! (matiere.getNomMatiere().equals("")) ) {
                    matieres.add(matiere);
                   cpt ++;
                }
                if(cpt != nbElement){
                    j++;
                }
            }
        }
        close();
        return matieres;
    }

    private IMatiere cursorToMatiere(Cursor c){
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

    public int getNbMatieres() {
        nbMatieres = taille();
        return nbMatieres;
    }

    private int taille(){
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
