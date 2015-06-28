package com.project.baptiste.mesnoteas.graphique;

import android.graphics.Color;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LinePoint;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class LineGraphique {
    private final String VERT = "#64DD17";
    private final String GRIS = "#E0E0E0";
    private final String ROUGE = "#FF5252";
    private final String ORANGE = "#FF8F00";
    private final String VIOLET = "#E040FB";
    private List<IObjet> moyennes;
    private Line line = new Line();
    private Line lineOrd = new Line();
    private int nbMoyennes;
    private double ordX = 0.01;
    private double noteMax = 0;

    public LineGraphique(List<IObjet> moyennes) {
        this.moyennes = moyennes;
        nbMoyennes = moyennes.size();
        initGraph();
        initNoteMax();
        initLineOrd();
    }

    public void initGraph(){
        LinePoint p ;
        IMoyenne m;
        double moyenne;
        for(IObjet o : moyennes){
            p = new LinePoint();
            m = (IMoyenne) o;
            moyenne = m.getMoyenne();
            p.setX(ordX);
            p.setY(moyenne);
            p.setColor(Color.parseColor(VIOLET));
            line.addPoint(p);
            ordX += 2;
            if(moyenne>noteMax){
                noteMax = moyenne;
            }
        }
        line.setColor(Color.parseColor(ORANGE));
    }

    public void initNoteMax(){
        if(noteMax < 20){
            noteMax = 20;
        }
        else noteMax = 100;
    }

    public void initLineOrd(){
        LinePoint lp1 = new LinePoint(0,0);
        lp1.setColor(Color.parseColor(ROUGE));
        LinePoint lp2 = new LinePoint(0,noteMax);
        lineOrd.addPoint(lp1);
        lineOrd.addPoint(lp2);
        lineOrd.setColor(Color.parseColor(GRIS));
    }

    public Line getLine() {
        return line;
    }

    public int getNbMoyennes() {
        return nbMoyennes;
    }

    public double getNoteMax() {
        return noteMax;
    }

    public double getOrdX() {
        return ordX;
    }

    public Line getLineOrd() {
        return lineOrd;
    }
}
