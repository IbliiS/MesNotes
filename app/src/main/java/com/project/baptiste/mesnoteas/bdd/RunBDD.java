package com.project.baptiste.mesnoteas.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.ITableBdd;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 12/06/2015.
 */
public class RunBDD {

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "notes.db";
    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    private List<ITableBdd> objetsBdd;
    private ITableBdd noteBdd;
    private ITableBdd matiereBdd;
    private ITableBdd matiereNoteBdd;
    private ITableBdd moyenneBdd;
    private ITableBdd anneeBdd;
    private ITableBdd moyenneMatiereBdd;
    private ITableBdd anneeMoyenneBdd;

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
        anneeBdd = new AnneeBdd(this);
        matiereNoteBdd = new MatiereNoteBdd(this);
        moyenneMatiereBdd = new MatiereMoyenneBdd(this);
        anneeMoyenneBdd = new AnneeMoyenneBdd(this);
        objetsBdd.add(noteBdd);
        objetsBdd.add(matiereBdd);
        objetsBdd.add(moyenneBdd);
        objetsBdd.add(anneeBdd);
        objetsBdd.add(matiereNoteBdd);
        objetsBdd.add(moyenneMatiereBdd);
        objetsBdd.add(anneeMoyenneBdd);
        //viderBdd();
        for(ITableBdd o : objetsBdd){
            o.getAll();
        }

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

    public IObjetAssoBdd getAnneeMoyenneBdd() {
        return (IObjetAssoBdd) anneeMoyenneBdd;
    }

    public IObjetBdd getAnneeBdd() {
        return (IObjetBdd) anneeBdd;
    }

    public void viderBdd(){
        for(ITableBdd t : objetsBdd){
            t.dropTable();
        }
    }
}
