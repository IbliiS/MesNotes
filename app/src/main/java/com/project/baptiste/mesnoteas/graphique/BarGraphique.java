package com.project.baptiste.mesnoteas.graphique;

import android.graphics.Color;

import com.echo.holographlibrary.Bar;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class BarGraphique {
    private ArrayList<Bar> bars = new ArrayList<Bar>();
    private List<IObjet> matieres = new ArrayList<>();
    private Utilitaire utilitaire = new Utilitaire();
    private final String ORANGE = "#FF8F00";
    private final String VIOLET = "#E040FB";
    private boolean couleur = false;


    public BarGraphique(List<IObjet> matieres) {
        this.matieres = matieres;
        initBarGraph();
    }

    public void initBarGraph(){
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
    }

    public ArrayList<Bar> getBars() {
        return bars;
    }
}
