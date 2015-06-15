package com.project.baptiste.mesnoteas.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.baptiste.mesnoteas.R;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 13/06/2015.
 */
public class NoteListViewAdapter extends BaseAdapter {
    List<INote> notes;
    LayoutInflater inflater;
    Context context;
    List<IMatiere> matieres;
    List<IMatiere> listeMatiere;
    boolean pasDeNote;


    public NoteListViewAdapter(Context context, List<INote> notes, List<IMatiere> matieres) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.notes = notes;
        this.matieres = matieres;
        initListMatiere();
    }

    public void initListMatiere(){
        int nbMatiere = 0;
        IMatiere mat;
        listeMatiere = new ArrayList<>();
        List<INote> listNote;
        for(IMatiere m : matieres){
            listNote = (List<INote>) m.getNotes();
            if( !(listNote.isEmpty()) ){
                for(INote n : listNote ){
                    mat = new Matiere();
                    mat.copyMatiere(m);
                    mat.getNotes().add(n);
                    listeMatiere.add(mat);
                    nbMatiere++;
                }
            }
        }
        pasDeNote = nbMatiere==0;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView matiere;
        TextView note;
        TextView coeff;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.note, null);
            holder.matiere = (TextView) view.findViewById(R.id.nom_matiere);
            holder.note = (TextView) view.findViewById(R.id.note);
            holder.coeff = (TextView) view.findViewById(R.id.coef);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(pasDeNote){
            holder.matiere.setText("Il n'y a pas encore de note");
            holder.note.setText("  ");
            holder.coeff.setText(" ");
        }
        else {
            holder.matiere.setText(listeMatiere.get(position).getNomMatiere());
            holder.note.setText(String.valueOf(listeMatiere.get(position).getNotes().get(0).getNote()));
            holder.coeff.setText(String.valueOf(listeMatiere.get(position).getNotes().get(0).getCoef()));
        }
        return view;
    }
}