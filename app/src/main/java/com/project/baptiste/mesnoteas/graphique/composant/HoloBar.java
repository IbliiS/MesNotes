package com.project.baptiste.mesnoteas.graphique.composant;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class HoloBar implements IBarGraph {
    private List<IObjet> matieres = new ArrayList<>();
    private Utilitaire utilitaire = new Utilitaire();
    private final String ORANGE = "#FF8F00";
    private final String VIOLET = "#E040FB";
    private boolean couleur = false;
    private BarGraph graph;


    public HoloBar(List<IObjet> matieres, Context c) {
        this.graph = new BarGraph(c);
        this.matieres = matieres;
        initBarGraph();
    }

    public void initBarGraph(){
        ArrayList<Bar> bars = new ArrayList<Bar>();
        double moyenne;
        String moyenecut;
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            Bar d = new Bar();
            moyenecut = utilitaire.coupeMoyenne(m.getMoyenne());
            moyenne = Double.parseDouble(moyenecut);
            d.setName(m.getNomMatiere());
            d.setValue((float) moyenne);
            if(couleur){
                d.setColor(Color.parseColor(VIOLET));
            }
            else{
                d.setColor(Color.parseColor(ORANGE));
            }
            couleur = !(couleur);
            bars.add(d);
        }

        graph.setBackgroundColor(Color.parseColor("#FFFFFF"));
        graph.setBars(bars);
    }

    @Override
    public View getGraph() {
        return graph;
    }

}
