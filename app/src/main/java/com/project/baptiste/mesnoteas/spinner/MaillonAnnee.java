package com.project.baptiste.mesnoteas.spinner;

import com.project.baptiste.mesnoteas.general.Annee;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 02/07/2015.
 */
public class MaillonAnnee extends Maillon {

    public MaillonAnnee(Maillon maillon) {
        this.suivant = maillon;
    }

    @Override
    protected List<String> doOperation(List<IObjet> list) {
        List<String> ls = new ArrayList<>();
        if(list.get(0) instanceof IAnnee){
            IAnnee a;
            for(IObjet o : list){
                a = (IAnnee) o;
                ls.add(a.getNomAnnee());
            }
            return ls;
        }
        if(suivant != null) {
            return suivant.remplirListe(list);
        }
        return ls;
    }
}
