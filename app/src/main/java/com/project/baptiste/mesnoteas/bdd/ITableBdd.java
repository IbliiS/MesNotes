package com.project.baptiste.mesnoteas.bdd;

/**
 * Created by Baptiste on 16/06/2015.
 */
public interface ITableBdd {

    void open();
    void close();
    void dropTable();
}
