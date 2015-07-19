package com.project.baptiste.mesnoteas.graphique.composant;

import android.view.View;

import com.echo.holographlibrary.Line;

/**
 * Created by Baptiste on 30/06/2015.
 */
public interface ILineGraph {
    //Enlever
    Object getLine();

    //Enlever
    int getNbMoyennes();

    //Enlever
    double getNoteMax();

    //Enlever
    double getOrdX();

    //Enlever
    Line getLineOrd();

    View getGraph();
}
