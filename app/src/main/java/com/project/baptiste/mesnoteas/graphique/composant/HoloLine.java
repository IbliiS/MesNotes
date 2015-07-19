package com.project.baptiste.mesnoteas.graphique.composant;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class HoloLine implements ILineGraph {
    private final String VERT = "#64DD17";
    private final String GRIS = "#BDBDBD";
    private final String ROUGE = "#FF5252";
    private final String ORANGE = "#FF8F00";
    private final String VIOLET = "#E040FB";
    private List<IObjet> moyennes;
    private Line line = new Line();
    private Line lineOrd = new Line();
    private int nbMoyennes;
    private double ordX = 0.01;
    private double noteMax = 0;
    private LineGraph graph;

    public HoloLine(List<IObjet> moyennes, Context c) {
        this.graph = new LineGraph(c);
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

        if(nbMoyennes > 0){
            graph.addLine(line);
            graph.addLine(lineOrd);
            graph.setRangeY(0, (float) noteMax);
            graph.setRangeX(0, (float) (ordX - 1.95));
            //graph.setLineToFill(0);
        }
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

    @Override
    public Line getLine() {
        return line;
    }

    @Override
    public int getNbMoyennes() {
        return nbMoyennes;
    }

    @Override
    public double getNoteMax() {
        return noteMax;
    }

    @Override
    public double getOrdX() {
        return ordX;
    }

    @Override
    public Line getLineOrd() {
        return lineOrd;
    }

    @Override
    public View getGraph() {
        return graph;
    }
}
