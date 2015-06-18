package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Baptiste on 14/06/2015.
 */
public class MatiereNoteBdd implements IObjetAssoBdd {

    private List<IObjet> matieresNotes;

    /** TABLE ASSOCIATIVE MATIERENOTE */
    private static final String TABLE_MATIERENOTE = "table_matierenote";
    private static final String COL_REFNOTE = "RefNote";
    private static final int NUM_COL_REFNOTE = 1;
    private static final String COL_REFMATIERE = "RefMatiere";
    private static final int NUM_COL_REFMATIERE = 0;

    private int nbElements = 0;

    private RunBDD runBDD;
    private IObjetBdd noteBdd;
    private IObjetBdd matiereBdd;

    public MatiereNoteBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        noteBdd = runBDD.getNoteBdd();
        matiereBdd = runBDD.getMatiereBdd();
        matieresNotes = new ArrayList<>();
        getAll();
    }

    @Override
    public void open(){
        runBDD.open();
    }

    @Override
    public void close(){
        runBDD.close();
    }

    /**
     * @param objet1 la note
     * @param objet2 la matiere
     * @return l'id
     */
    @Override
    public long insert(IObjet objet1, IObjet objet2){
        INote note = (INote) objet1;
        IMatiere matiere = (IMatiere) objet2;
        if( ! (matieresNotes.contains(matiere)) ){
            matieresNotes.add(matiere);
        }
        ContentValues values = new ContentValues();
        values.put(COL_REFMATIERE, matiere.getId());
        values.put(COL_REFNOTE, note.getId());
        return runBDD.getBdd().insert(TABLE_MATIERENOTE, null, values);
    }

    /**
     * Retourne la liste de note d'une matiere
     * @param i
     * @return liste de note
     */
    @Override
    public List<IObjet> getListObjetWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIERENOTE + " WHERE "+ COL_REFMATIERE + "=" + i, null);
        return cursorToObject(c);
    }

    @Override
    public List<IObjet> cursorToObject(Cursor c) {
        List<IObjet> notes = new ArrayList<>();
        if(c.getCount() == 0){
            return notes;
        }
        if(c.moveToFirst()){
            INote note;
            int idMatiere;
            int idNote;
            while(!c.isAfterLast()){
                note = new Note();
                idMatiere = c.getInt(NUM_COL_REFMATIERE);
                idNote = c.getInt(NUM_COL_REFNOTE);
                note = (INote) noteBdd.getWithId(idNote);
                notes.add(note);
                c.moveToNext();
            }
        }
        return  notes;
    }


    @Override
    public List<IObjet> getAll(){
        open();
        if(matieresNotes.size() == 0 || getNbElements() != matieresNotes.size() ){
            matieresNotes.clear();
            IMatiere matiere;
            int cpt = 0;
            int nbMatieres = matiereBdd.getNbElements();
            int j = nbMatieres;
            for(int i = 1; i <= j; i++){
                matiere = new Matiere();
                matiere = (IMatiere) matiereBdd.getWithId(i);
                if( ! (matiere.getNomMatiere().equals("")) ){
                    cpt++;
                    matiere.setNotes(getListObjetWithId(i));
                    matieresNotes.add(matiere);
                }
                if(cpt != nbMatieres ){
                    j++;
                }
            }
        }
        close();
        return matieresNotes;
    }

    @Override
    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    @Override
    public int taille(){
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_MATIERENOTE);
        return (int) s.simpleQueryForLong();
    }

    @Override
    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_MATIERENOTE,null,null);
        close();
    }

    @Override
    public int removeWithID(int id){
        IMatiere matiereADelete = new Matiere();
        boolean b = false;
        IMatiere m;
        for(IObjet o : matieresNotes){
            m = (IMatiere) o;
            if(m.getId() == id){
                matiereADelete = m;
                b = true;
            }
        }
        if(b){
            /** ON SUPPRIME TOUTES LES NOTES DE LA MATIERE **/
            INote n;
            for(IObjet o : matiereADelete.getNotes()){
                n = (INote) o;
                runBDD.getNoteBdd().removeWithID(n.getId());
            }
            matieresNotes.remove(matiereADelete);
        }
        //runBDD.getMatiereBdd().removeWithID(id);
        return runBDD.getBdd().delete(TABLE_MATIERENOTE, COL_REFMATIERE + " = " + id, null);
    }

}
