package com.project.baptiste.mesnoteas.spinner;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 02/07/2015.
 */
public abstract class Maillon {
    protected Maillon suivant;

    public List<String> remplirListe(List<IObjet> list){
        if(list.isEmpty()){
            return new ArrayList<>();
        }
        return doOperation(list);
    }

    protected abstract List<String> doOperation(List<IObjet> list);


    public void setSuivant(Maillon suivant) {
        this.suivant = suivant;
    }
}
