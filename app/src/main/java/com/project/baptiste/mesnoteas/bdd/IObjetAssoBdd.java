package com.project.baptiste.mesnoteas.bdd;

import android.database.Cursor;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.List;

/**
 * Created by Baptiste on 16/06/2015.
 */
public interface IObjetAssoBdd extends ITableBdd{
    void open();

    void close();

    long insert(IObjet objet1, IObjet objet2);

    List<IObjet> getListObjetWithId(int i);

    List<IObjet> cursorToObject(Cursor c);

    List<IObjet> getAll();

    int getNbElements();

    int taille();

    void dropTable();

    int removeWithID(int id);
}
