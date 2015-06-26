package com.project.baptiste.mesnoteas.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.baptiste.mesnoteas.R;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 26/06/2015.
 */
public class AnneeListViewAdapter extends BaseAdapter {
    List<IObjet> annees = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    boolean pasAnnee = true;
    RunBDD runBDD;

    public AnneeListViewAdapter(List<IObjet> annees, Context context) {
        this.annees = annees;
        this.context = context;
        inflater = LayoutInflater.from(context);
        pasAnnee = annees.size() == 0;
        runBDD = RunBDD.getInstance(context);
    }

    @Override
    public int getCount() {
        return annees.size();
    }

    @Override
    public Object getItem(int position) {
        if(pasAnnee) {
            return null;
        }
        return annees.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView annee;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.annee,null);
            holder.annee = (TextView) view.findViewById(R.id.anneeNomAnnee);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        if(pasAnnee){

        }
        else{
            IAnnee a = (IAnnee) annees.get(position);
            holder.annee.setText(a.getNomAnnee());
        }
        return view;
    }
}
