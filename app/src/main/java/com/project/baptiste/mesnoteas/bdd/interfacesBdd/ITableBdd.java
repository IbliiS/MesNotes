package com.project.baptiste.mesnoteas.bdd.interfacesBdd;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.List;

/**
 * Created by Baptiste on 16/06/2015.
 */
public interface ITableBdd {

    void open();
    void close();
    void dropTable();
    List<IObjet> getAll();
}
