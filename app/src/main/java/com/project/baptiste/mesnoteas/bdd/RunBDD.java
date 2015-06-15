package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Baptiste on 12/06/2015.
 */
public class RunBDD {

    private static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "notes.db";
    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    private NoteBdd noteBdd;
    private MatiereBdd matiereBdd;
    private MatiereNoteBdd matiereNoteBdd;

    private static RunBDD instance = null;


    public static RunBDD getInstance(Context context){
        if(instance == null){
            instance = new RunBDD(context);
        }
        return instance;
    }

    private RunBDD(Context context) {
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
        noteBdd = new NoteBdd(this);
        matiereBdd = new MatiereBdd(this);
        matiereNoteBdd = new MatiereNoteBdd(this);

    }

    public void open() {
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBdd() {
        return bdd;
    }

    public void setBdd(SQLiteDatabase bdd) {
        this.bdd = bdd;
    }

    public MaBaseSQLite getMaBaseSQLite() {
        return maBaseSQLite;
    }

    public void setMaBaseSQLite(MaBaseSQLite maBaseSQLite) {
        this.maBaseSQLite = maBaseSQLite;
    }

    public NoteBdd getNoteBdd() {
        return noteBdd;
    }

    public MatiereBdd getMatiereBdd() {
        return matiereBdd;
    }

    public MatiereNoteBdd getMatiereNoteBdd() {
        return matiereNoteBdd;
    }

    public void viderBdd(){
        matiereNoteBdd.dropTable();
        noteBdd.dropTable();
        matiereBdd.dropTable();
    }
}
