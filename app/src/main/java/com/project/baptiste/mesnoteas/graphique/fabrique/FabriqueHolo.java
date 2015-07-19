package com.project.baptiste.mesnoteas.graphique.fabrique;

import android.content.Context;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.graphique.composant.HoloBar;
import com.project.baptiste.mesnoteas.graphique.composant.HoloLine;
import com.project.baptiste.mesnoteas.graphique.composant.IBarGraph;
import com.project.baptiste.mesnoteas.graphique.composant.ILineGraph;

import java.util.List;

/**
 * Created by Baptiste on 30/06/2015.
 */
public class FabriqueHolo implements FabriqueGraphAbs {
    @Override
    public ILineGraph creerLine(List<IObjet> moyennes, Context c) {
        return new HoloLine(moyennes, c);
    }

    @Override
    public IBarGraph creerBar(List<IObjet> matieres, Context c) {
        return new HoloBar(matieres, c);
    }
}
