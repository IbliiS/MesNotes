package com.project.baptiste.mesnoteas.graphique.composant;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 30/06/2015.
 */
public class AChartBar implements IBarGraph {
    private List<IObjet> matieres;
    private BarChart graph;

    public AChartBar(List<IObjet> matieres, Context c) {
        this.matieres = matieres;
        this.graph = new BarChart(c);
        initBar();
    }

    private void initBar(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        IMatiere m;
        int i = 0;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            entries.add(new BarEntry((float) m.getMoyenne(),i));
            labels.add(m.getNomMatiere());
            i++;
        }
        BarDataSet dataset = new BarDataSet(entries, "Moyenne matiere");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(12);
        dataset.setValueTextColor(Color.GREEN);
        BarData barData = new BarData(labels, dataset);
        graph.setData(barData);
        graph.animateY(3000);
        graph.setNoDataText("il n'y a pas de matières");

        graph.getAxisLeft().setAxisMaxValue(22);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisLeft().setStartAtZero(true);
        graph.getXAxis().setSpaceBetweenLabels(2);
        graph.getXAxis().setTextColor(Color.BLUE);
        graph.setDescription("");

    }

    @Override
    public View getGraph() {
        return graph;
    }

}
