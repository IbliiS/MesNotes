package com.project.baptiste.mesnoteas.graphique.composant;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.echo.holographlibrary.Line;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 30/06/2015.
 */
public class AChartLine implements ILineGraph {
    private List<IObjet> moyennes;
    private LineChart graph;

    public AChartLine(List<IObjet> moyennes, Context c) {
        this.moyennes = moyennes;
        this.graph = new LineChart(c);
        initLine();
    }



    private void initLine() {
        LineData lineData = new LineData();
        graph.setNoDataText("Il n'y a pas de périodes");
        graph.setDescription("");
        graph.setTouchEnabled(true);
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);
        graph.setDrawGridBackground(false);
        graph.setPinchZoom(true);
//        graph.setBackgroundColor(Color.LTGRAY);
        graph.getAxisLeft().setAxisMaxValue(22);
        graph.getAxisRight().setEnabled(false);
        graph.getXAxis().setTextColor(Color.BLUE);
        graph.getXAxis().setAvoidFirstLastClipping(true);
        graph.getLegend().setForm(Legend.LegendForm.LINE);
        graph.animateY(3000);
//        graph.getXAxis().setSpaceBetweenLabels(2);
        graph.setData(lineData);
        if(lineData != null) {
            LineDataSet set = lineData.getDataSetByIndex(0);
            if(set == null){
                set = createSet();
                lineData.addDataSet(set);
            }
            set.getEntryCount();
            ArrayList<String> labels = new ArrayList<String>();
            IMoyenne m;
            int i = 0;
            for (IObjet o : moyennes) {
                m = (IMoyenne) o;
                lineData.addXValue(m.getNomMoyenne());
                lineData.addEntry( new Entry((float) m.getMoyenne(),set.getEntryCount() ) , 0);
                labels.add(m.getNomMoyenne());
                i++;
            }
        }

    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Moyenne période");
        set.setDrawCubic(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setValueTextColor(Color.GREEN);
        set.setValueTextSize(12f);
        set.setCubicIntensity(0.2f);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(12);
        set.setFillColor(ColorTemplate.getHoloBlue());
        return set;
    }

    @Override
    public Object getLine() {
        return null;
    }

    @Override
    public int getNbMoyennes() {
        return 0;
    }

    @Override
    public double getNoteMax() {
        return 0;
    }

    @Override
    public double getOrdX() {
        return 0;
    }

    @Override
    public Line getLineOrd() {
        return null;
    }

    @Override
    public View getGraph() {
        return graph;
    }
}
