package com.project.baptiste.mesnoteas.graphique.fabrique;

import android.content.Context;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.graphique.composant.IBarGraph;
import com.project.baptiste.mesnoteas.graphique.composant.ILineGraph;

import java.util.List;

/**
 * Created by Baptiste on 30/06/2015.
 */
public interface FabriqueGraphAbs {
    ILineGraph creerLine(List<IObjet> moyennes, Context c);
    IBarGraph creerBar(List<IObjet> matieres,Context c);

}
