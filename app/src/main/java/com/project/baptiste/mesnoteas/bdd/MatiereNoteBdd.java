package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Baptiste on 14/06/2015.
 */
public class MatiereNoteBdd {

    private HashMap<IMatiere,List<INote>> matieresNotesMap;
    private List<IMatiere> matieresNotes;

    /** TABLE ASSOCIATIVE MATIERENOTE */
    private static final String TABLE_MATIERENOTE = "table_matierenote";
    private static final String COL_REFNOTE = "RefNote";
    private static final int NUM_COL_REFNOTE = 1;
    private static final String COL_REFMATIERE = "RefMatiere";
    private static final int NUM_COL_REFMATIERE = 0;

    private int nbMatieresNotes;

    private RunBDD runBDD;

    public MatiereNoteBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        matieresNotes = new ArrayList<>();
        matieresNotesMap = new HashMap<>();
        getAllMatiereNote();
    }

    public void open(){
        runBDD.open();
    }

    public void close(){
        runBDD.close();
    }

    public long insertMatiereNote(INote note, IMatiere matiere){
        if( ! (matieresNotes.contains(matiere)) ){
            matieresNotes.add(matiere);
        }
        ContentValues values = new ContentValues();
        values.put(COL_REFMATIERE, matiere.getId());
        values.put(COL_REFNOTE, note.getId());
        return runBDD.getBdd().insert(TABLE_MATIERENOTE, null, values);

    }

    public List<INote> getListNoteWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIERENOTE + " WHERE "+ COL_REFMATIERE + "=" + i, null);
        return cursorToNote(c);
    }

    private List<INote> cursorToNote(Cursor c) {
        List<INote> notes = new ArrayList<>();
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
                note = runBDD.getNoteBdd().getNoteWithId(idNote);
                notes.add(note);
                c.moveToNext();
            }
        }
        return  notes;
    }


    public List<IMatiere> getAllMatiereNote(){
        open();
        if(matieresNotes.size() == 0 || getNbMatieresNotes() != matieresNotes.size() ){
            matieresNotes.clear();
            IMatiere matiere;
            List<INote> notes;
            int cpt = 0;
            int nbMatieres = runBDD.getMatiereBdd().getNbMatieres();
            int j = nbMatieres;
            for(int i = 1; i <= j; i++){
                matiere = new Matiere();
                matiere = runBDD.getMatiereBdd().getMatiereWithId(i);
                  if( ! (matiere.getNomMatiere().equals("")) ){
                      cpt++;
                      matiere.setNotes(getListNoteWithId(i));
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

    public int getNbMatieresNotes() {
        nbMatieresNotes = taille();
        return nbMatieresNotes;
    }

    private int taille(){
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_MATIERENOTE);
        return (int) s.simpleQueryForLong();
    }

    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_MATIERENOTE,null,null);
        close();
    }

    public int removeMatiereWithID(int id){
        IMatiere matiereADelete = new Matiere();
        boolean b = false;
        for(IMatiere m : matieresNotes){
            if(m.getId() == id){
                matiereADelete = m;
                b = true;
            }
        }
        if(b){
            matieresNotes.remove(matiereADelete);
        }
        return runBDD.getBdd().delete(TABLE_MATIERENOTE, COL_REFMATIERE + " = " + id, null);
    }

}
