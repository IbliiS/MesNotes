package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 16/06/2015.
 */
public class MatiereMoyenneBdd implements IObjetAssoBdd {

    private List<IObjet> moyennesMatieres;
    private Utilitaire utilitaire;

    /** TABLE ASSOCIATIVE MATIEREMOYENNE */
    private static final String TABLE_MATIEREMOYENNE = "table_matieremoyenne";
    private static final String COL_REFMATIERE = "RefMatiere";
    private static final int NUM_COL_REFMATIERE = 0;
    private static final String COL_REFMOYENNE = "RefMoyenne";
    private static final int NUM_COL_REFMOYENNE = 1;

    private int nbElements = 0;
    private RunBDD runBDD;

    public MatiereMoyenneBdd(RunBDD runBDD) {
        this.runBDD = runBDD;
        moyennesMatieres = new ArrayList<>();
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
     * @param objet1 la matiere
     * @param objet2 la moyenne
     * @return l'id
     */
    @Override
    public long insert(IObjet objet1, IObjet objet2) {
        IMatiere matiere = (IMatiere) objet1;
        IMoyenne moyenne = (IMoyenne) objet2;
        if( ! (moyennesMatieres.contains(moyenne))){
            moyennesMatieres.add(moyenne);
        }
        ContentValues values = new ContentValues();
        values.put(COL_REFMOYENNE, moyenne.getId());
        values.put(COL_REFMATIERE, matiere.getId());
        return runBDD.getBdd().insert(TABLE_MATIEREMOYENNE, null, values);
    }

    /**
     * Retourne la liste de matiere d'une moyenne
     * @param i id de la moyenne
     * @return liste de matiere
     */
    @Override
    public List<IObjet> getListObjetWithId(int i) {
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIEREMOYENNE + " WHERE "+ COL_REFMOYENNE + "=" + i, null);
        return cursorToObject(c);
    }

    @Override
    public List<IObjet> cursorToObject(Cursor c) {
        List<IObjet> matieres = new ArrayList<>();
        if(c.getCount()==0){
            c.close();
            return matieres;
        }
        if(c.moveToFirst()){
            IMatiere matiere;
            int idMatiere;
            while(!c.isAfterLast()){
                matiere = new Matiere();
                idMatiere = c.getInt(NUM_COL_REFMATIERE);
                matiere = (IMatiere) runBDD.getMatiereBdd().getWithId(idMatiere);
                matiere.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(matiere.getId()));
                matieres.add(matiere);
                c.moveToNext();
            }
        }
        c.close();
        return matieres;
    }

    /**
     * Récupérer une moyenne depuis une matière
     * @param id l'id de la matière
     * @return la moyenne
     */
    @Override
    public IObjet getOtherObjetWithId(int id){
        Cursor c = runBDD.getBdd().rawQuery("SELECT * FROM " + TABLE_MATIEREMOYENNE + " WHERE "+ COL_REFMATIERE + "=" + id, null);
        return cursorToOtherObject(c);
    }


    @Override
    public IObjet cursorToOtherObject(Cursor c) {
        IMoyenne moyenne = new Moyenne();
        if(c.getCount()==0){
            c.close();
            return moyenne;
        }
        c.moveToFirst();
        int idMoyenne;
        idMoyenne = c.getInt(NUM_COL_REFMOYENNE);
        moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithId(idMoyenne);
        moyenne.setMatieres(getListObjetWithId(moyenne.getId()));
        c.close();
        return moyenne;
    }

    @Override
    public List<IObjet> getAll() {
        open();
        if(moyennesMatieres.size() == 0 || getNbElements() != moyennesMatieres.size()){
            moyennesMatieres.clear();
            IMoyenne moyenne;
            int cpt = 0;
            int nbMoyennes = runBDD.getMoyenneBdd().getNbElements();
            int j = nbMoyennes;
            for(int i = 1; i <= j; i++){
                moyenne = new Moyenne();
                moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithId(i);
                if( ! (moyenne.getNomMoyenne().equals(""))){
                    cpt ++;
                    moyenne.setMatieres(getListObjetWithId(i));
                    moyennesMatieres.add(moyenne);
                }
                if(cpt != nbMoyennes ){
                    j++;
                }

            }
        }
        close();
        return utilitaire.copyList(moyennesMatieres);
    }

    @Override
    public int getNbElements() {
        nbElements = taille();
        return nbElements;
    }

    @Override
    public int taille() {
        SQLiteStatement s = runBDD.getBdd().compileStatement("SELECT COUNT (*) FROM " + TABLE_MATIEREMOYENNE);
        return (int) s.simpleQueryForLong();
    }

    @Override
    public void dropTable() {
        open();
        runBDD.getBdd().delete(TABLE_MATIEREMOYENNE,null,null);
        close();
    }

    @Override
    public int removeWithID(int id) {
        IMoyenne moyenneADelete = new Moyenne();
        boolean b = false;
        IMoyenne m;
        for(IObjet o : moyennesMatieres){
            m = (IMoyenne) o;
            if(m.getId() == id){
                moyenneADelete = m;
                b = true;
            }
        }
        if(b){
            /** ON SUPPRIME TOUTES LES MATIERES DE LA MOYENNE **/
            IMatiere mat;
            for(IObjet o : getListObjetWithId(moyenneADelete.getId())){
                mat = (IMatiere) o;
                runBDD.getMatiereNoteBdd().removeWithID(mat.getId());
            }
            moyennesMatieres.remove(moyenneADelete);
        }
        runBDD.getAnneeMoyenneBdd().removeOtherObjectWithID(id);
        runBDD.getMoyenneBdd().removeWithID(id);
        return runBDD.getBdd().delete(TABLE_MATIEREMOYENNE, COL_REFMOYENNE + " = " + id, null);

    }

    /**
     * Suppression de la matiere avec son id
     * @param id de la Matiere
     * @return id de la matiere supprimée
     */
    @Override
    public int removeOtherObjectWithID(int id) {
        runBDD.open();
        IMatiere matiereADelete = (IMatiere) runBDD.getMatiereBdd().getWithId(id);
        IMoyenne moyenne = (IMoyenne) getOtherObjetWithId(matiereADelete.getId());
        IMoyenne m = (IMoyenne) moyennesMatieres.get(moyennesMatieres.indexOf(moyenne));
        m.getMatieres().remove(matiereADelete);
        return runBDD.getBdd().delete(TABLE_MATIEREMOYENNE, COL_REFMATIERE + " = " + id, null);
    }

}
