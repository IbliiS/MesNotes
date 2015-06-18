package com.project.baptiste.mesnoteas.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 12/06/2015.
 */
public class RunBDD {

    private static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "notes.db";
    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    private List<ITableBdd> objetsBdd;
    private ITableBdd noteBdd;
    private ITableBdd matiereBdd;
    private ITableBdd matiereNoteBdd;
    private ITableBdd moyenneBdd;
    private ITableBdd moyenneMatiereBdd;

    private static RunBDD instance = null;


    public static RunBDD getInstance(Context context){
        if(instance == null){
            instance = new RunBDD(context);
        }
        return instance;
    }

    private RunBDD(Context context) {
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
        objetsBdd = new ArrayList<>();
        noteBdd = new NoteBdd(this);
        matiereBdd = new MatiereBdd(this);
        moyenneBdd = new MoyenneBdd(this);
        matiereNoteBdd = new MatiereNoteBdd(this);
        moyenneMatiereBdd = new MatiereMoyenneBdd(this);

        objetsBdd.add(noteBdd);
        objetsBdd.add(matiereBdd);
        objetsBdd.add(moyenneBdd);
        objetsBdd.add(matiereNoteBdd);
        objetsBdd.add(moyenneMatiereBdd);

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

    public IObjetBdd getNoteBdd() {
        return (IObjetBdd) noteBdd;
    }

    public IObjetBdd getMatiereBdd() {
        return (IObjetBdd) matiereBdd;
    }

    public IObjetAssoBdd getMatiereNoteBdd() {
        return (IObjetAssoBdd) matiereNoteBdd;
    }

    public IObjetBdd getMoyenneBdd() {
        return (IObjetBdd) moyenneBdd;
    }

    public IObjetAssoBdd getMoyenneMatiereBdd() {
        return (IObjetAssoBdd) moyenneMatiereBdd;
    }

    public void viderBdd(){
        for(ITableBdd t : objetsBdd){
            t.dropTable();
        }
    }
}
