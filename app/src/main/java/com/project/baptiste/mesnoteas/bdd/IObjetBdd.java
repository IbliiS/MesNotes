package com.project.baptiste.mesnoteas.bdd;

import android.database.Cursor;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.List;

/**
 * Created by Baptiste on 16/06/2015.
 */
public interface IObjetBdd extends ITableBdd {
    long insert(IObjet objet);

    int update(int id, IObjet objet);

    int removeWithID(int id);

    int removeWithName(String s);

    IObjet getWithId(int i);

    IObjet getWithName(String nom);

    List<IObjet> getAll();

    IObjet cursorToObject(Cursor c);

    int getNbElements();

    int taille();

    void open();

    void close();

    void dropTable();
}
