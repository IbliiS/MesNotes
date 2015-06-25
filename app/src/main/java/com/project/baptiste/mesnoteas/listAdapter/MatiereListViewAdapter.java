package com.project.baptiste.mesnoteas.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.baptiste.mesnoteas.R;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 25/06/2015.
 */
public class MatiereListViewAdapter extends BaseAdapter{
    List<IObjet> matieres = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    boolean pasDeMatieres = true;
    Utilitaire utilitaire = new Utilitaire();
    RunBDD runBDD;

    public MatiereListViewAdapter(List<IObjet> matieres, Context context) {
        inflater = LayoutInflater.from(context);
        this.matieres = matieres;
        this.context = context;
        pasDeMatieres = matieres.size() == 0;
        runBDD = RunBDD.getInstance(context);
    }

    @Override
    public int getCount() {
        return matieres.size();
    }

    @Override
    public Object getItem(int position) {
        if(pasDeMatieres){
            return null;
        }
        return matieres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView moyenne;
        TextView nomMatiere;
        TextView coeffMatiere;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.matiere,null);
            holder.moyenne = (TextView) view.findViewById(R.id.matiereNomPeriode);
            holder.nomMatiere = (TextView) view.findViewById(R.id.matiereNom_matiere);
            holder.coeffMatiere = (TextView) view.findViewById(R.id.matiereCoef_matiere);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        if(pasDeMatieres){

        }
        else{
            IMatiere m = (IMatiere) matieres.get(position);
            runBDD.open();
            IMoyenne moy = (IMoyenne) runBDD.getMoyenneMatiereBdd().getOtherObjetWithId(m.getId());
            runBDD.close();
            holder.moyenne.setText(moy.getNomMoyenne());
            holder.nomMatiere.setText(m.getNomMatiere());
            holder.coeffMatiere.setText(String.valueOf(m.getCoef()));
        }
        return view;
    }
}
