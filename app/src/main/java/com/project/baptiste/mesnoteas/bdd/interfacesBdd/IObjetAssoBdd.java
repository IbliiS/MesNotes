package com.project.baptiste.mesnoteas.bdd.interfacesBdd;

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

    IObjet getOtherObjetWithId(int id);

    IObjet cursorToOtherObject(Cursor c);

    List<IObjet> getAll();

    int getNbElements();

    int taille();

    void dropTable();

    int removeWithID(int id);

    int removeOtherObjectWithID(int id);

    void updateOtherObject(int id, IObjet objet, IObjet intoObject);

    void updateObject(int id, IObjet objet);
}
