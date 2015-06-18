package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 12/06/2015.
 */
public class NoteBdd implements IObjetBdd {

    private List<IObjet> notes;
    /** TABLE NOTES **/
    private static final String TABLE_NOTES = "table_notes";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOTE = "Note";
    private static final int NUM_COL_NOTE = 1;
    private static final String  COL_COEF = "Coef";
    private static final int NUM_COL_COEF = 2;

    private int nbElements = 0;

    private RunBDD runBDD;

    public NoteBdd(RunBDD runBDD){
        this.runBDD = runBDD;
        notes = new ArrayList<>();
        getAll();
    }

    public void open(){
        runBDD.open();
    }

    public void close(){
        runBDD.close();
    }



    public long insert(IObjet objet){
        INote note = (INote) objet;
        INote n =null;
        int i = notes.indexOf(note);
        if(i != -1){
            n = (INote) notes.get(i);
        }
        boolean estVide = notes.size() == 0;
        if(estVide || n == null) {
            ContentValues values = new ContentValues();
            values.put(COL_NOTE, note.getNote());
            values.put(COL_COEF, note.getCoef());
            int l = (int) runBDD.getBdd().insert(TABLE_NOTES, null, values);
            note.setId(l);
            notes.add(note);
            return l;
        }
        else return n.getId();
    }

    public int update(int id, IObjet objet){
        INote note = (INote) objet;
        INote noteADelete = new Note();
        INote n;
        for(IObjet o : notes){
            n = (INote) o;
            if(n.getId() == note.getId()){
                noteADelete = n;
            }
        }
        notes.remove(noteADelete);
        ContentValues values = new ContentValues();
        values.put(COL_NOTE, note.getNote());
        values.put(COL_COEF, note.getCoef());
        note.setId(id);
        notes.add(note);
        return runBDD.getBdd().update(TABLE_NOTES, values, COL_ID + " = " + id, null);
    }

    public int removeWithID(int id){
        INote noteADelete = new Note();
        boolean b = false;
        INote n;
        for(IObjet o : notes){
            n = (INote) o;
            if(n.getId() == id){
                noteADelete = n;
                b = true;
            }
        }
        if(b){
            notes.remove(noteADelete);
        }
        return runBDD.getBdd().delete(TABLE_NOTES, COL_ID + " = " + id, null);
    }

    @Override
    public int removeWithName(String s) {
        return 0;
    }

    public IObjet getWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE ID=" + i, null);
        return cursorToObject(c);
    }

    @Override
    public IObjet getWithName(String nom) {
        return null;
    }

    public List<IObjet> getAll(){
        open();
        if (notes.size() == 0 || getNbElements() != notes.size()){
            notes.clear();
            int cpt = 0;
            INote note;
            int nbElement = getNbElements();
            int j = nbElement;
            for(int i = 1; i <= j; i++){
                note = (INote) getWithId(i);
                if(note != null && ! (Double.toString(note.getNote())).equals("-1.0") ) {
                    cpt ++;
                    notes.add(note);
                }
                if(cpt != nbElement){
                    j++;
                }
            }

            close();
        }
        return notes;
    }


    public IObjet cursorToObject(Cursor c){
        if (c.getCount() == 0) {
            return new Note();
        }
        c.moveToFirst();
        INote note = new Note();
        note.setId(c.getInt(NUM_COL_ID));
        note.setNote(c.getDouble(NUM_COL_NOTE));
        note.setCoef(c.getInt(NUM_COL_COEF));
        c.close();
        return note;
    }

    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    public int taille(){
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_NOTES+";");
        return (int) s.simpleQueryForLong();
    }

    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_NOTES,null,null);
        close();
    }

}
