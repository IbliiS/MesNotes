package com.project.baptiste.mesnoteas.graphique.fabrique;

import android.content.Context;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.graphique.composant.AChartBar;
import com.project.baptiste.mesnoteas.graphique.composant.AChartLine;
import com.project.baptiste.mesnoteas.graphique.composant.IBarGraph;
import com.project.baptiste.mesnoteas.graphique.composant.ILineGraph;

import java.util.List;

/**
 * Created by Baptiste on 30/06/2015.
 */
public class FabriqueAChart implements FabriqueGraphAbs{
    @Override
    public ILineGraph creerLine(List<IObjet> moyennes, Context c) {
        return new AChartLine(moyennes, c);
    }

    @Override
    public IBarGraph creerBar(List<IObjet> matieres, Context c) {
        return new AChartBar(matieres, c);
    }
}
