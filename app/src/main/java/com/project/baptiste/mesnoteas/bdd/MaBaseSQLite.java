package com.project.baptiste.mesnoteas.bdd;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


/**
 * Created by Baptiste on 12/06/2015.
 */
public class MaBaseSQLite extends SQLiteOpenHelper {
    /** TABLE NOTE */
    private static final String TABLE_NOTES = "table_notes";
    private static final String COL_ID = "ID";
    private static final String COL_NOTE = "Note";
    private static final String COL_COEF = "Coef";
    /** TABLE ANNEE */
    private static final String TABLE_ANNEE = "table_annees";
    private static final String COL_NOM = "Nom";
    /** TABLE ASSOCIATIVE ANNEEMOYENNE */
    private static final String TABLE_ANNEEMOYENNE = "table_anneemoyenne";
    private static final String COL_REFANNEE = "RefAnnee";
    private static final String COL_REFMOYENNE = "RefMoyenne";
    /** TABLE ETUDIANT */
    private static final String TABLE_ETUDIANT = "table_etudiants";
    /** TABLE MATIERE */
    private static final String TABLE_MATIERE = "table_matieres";
    /** TABLE MOYENNE */
    private static final String TABLE_MOYENNE = "table_moyennes";
    /** TABLE ASSOCIATIVE MATIEREMOYENNE */
    private static final String TABLE_MATIEREMOYENNE = "table_matieremoyenne";
    private static final String COL_REFMATIERE = "RefMatiere";
    /** TABLE ASSOCIATIVE MATIERENOTE */
    private static final String TABLE_MATIERENOTE = "table_matierenote";
    private static final String COL_REFNOTE = "RefNote";
    private static final String COL_MOYENNE = "Moyenne";


    private static final String CREATE_NOTE = "CREATE TABLE "
            + TABLE_NOTES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOTE + " REAL, " + COL_COEF + " INTEGER);";

    private static final String CREATE_MATIERE = "CREATE TABLE "
            + TABLE_MATIERE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, " + COL_COEF + " INTEGER, " + COL_MOYENNE + " REAL);";

    private static final String CREATE_MOYENNE = "CREATE TABLE "
            + TABLE_MOYENNE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, " + COL_MOYENNE + " REAL);";

    private static final String CREATE_ANNEE = "CREATE TABLE "
            + TABLE_ANNEE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, " + COL_MOYENNE + " REAL);";

    private static final String CREATE_MATIERENOTE = "CREATE TABLE "
            + TABLE_MATIERENOTE + " ("
            + COL_REFMATIERE + " INTEGER NOT NULL, " + COL_REFNOTE + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + COL_REFMATIERE + "," + COL_REFNOTE + "));";

    private static final String CREATE_MATIEREMOYENNE = "CREATE TABLE "
            + TABLE_MATIEREMOYENNE + " ("
            + COL_REFMATIERE + " INTEGER NOT NULL, " + COL_REFMOYENNE + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + COL_REFMATIERE + "," + COL_REFMOYENNE + "));";

    private static final String CREATE_ANNEEMOYENNE = "CREATE TABLE "
            + TABLE_ANNEEMOYENNE + " ("
            + COL_REFANNEE + " INTEGER NOT NULL, " + COL_REFMOYENNE + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + COL_REFANNEE + "," + COL_REFMOYENNE + "));";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_NOTE);
        db.execSQL(CREATE_MATIERE);
        db.execSQL(CREATE_MOYENNE);
        db.execSQL(CREATE_ANNEE);
        db.execSQL(CREATE_MATIERENOTE);
        db.execSQL(CREATE_MATIEREMOYENNE);
        db.execSQL(CREATE_ANNEEMOYENNE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_MATIERENOTE + ";");
        db.execSQL("DROP TABLE " + TABLE_MATIEREMOYENNE + ";");
        db.execSQL("DROP TABLE " + TABLE_ANNEEMOYENNE + ";");
        db.execSQL("DROP TABLE " + TABLE_NOTES + ";");
        db.execSQL("DROP TABLE " + TABLE_MATIERE + ";");
        db.execSQL("DROP TABLE " + TABLE_MOYENNE + ";");
        db.execSQL("DROP TABLE " + TABLE_ANNEE + ";");

        onCreate(db);
    }

}
