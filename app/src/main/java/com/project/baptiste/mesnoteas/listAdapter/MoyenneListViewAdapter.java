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
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 26/06/2015.
 */
public class MoyenneListViewAdapter extends BaseAdapter{
    List<IObjet> moyennes = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    boolean pasDeMoyenne = true;
    RunBDD runBDD;

    public MoyenneListViewAdapter(List<IObjet> moyennes, Context context) {
        this.moyennes = moyennes;
        this.context = context;
        inflater = LayoutInflater.from(context);
        pasDeMoyenne = moyennes.size() == 0;
        runBDD = RunBDD.getInstance(context);

    }

    @Override
    public int getCount() {
        return moyennes.size();
    }

    @Override
    public Object getItem(int position) {
        if(pasDeMoyenne){
            return null;

        }
        return moyennes.get(position);
    }

    private class ViewHolder{
        TextView annee;
        TextView moyenne;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.moyenne,null);
            holder.annee = (TextView) view.findViewById(R.id.moyenneNomAnnee);
            holder.moyenne = (TextView) view.findViewById(R.id.moyenneNomMoyenne);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        if(pasDeMoyenne){

        }
        else{
            IMoyenne m = (IMoyenne) moyennes.get(position);
            runBDD.open();
            IAnnee a = (IAnnee) runBDD.getAnneeMoyenneBdd().getOtherObjetWithId(m.getId());
            runBDD.close();
            holder.annee.setText(a.getNomAnnee());
            holder.moyenne.setText(m.getNomMoyenne());
        }
        return view;
    }

}
