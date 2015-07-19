package com.project.baptiste.mesnoteas.spinner;

import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 04/07/2015.
 */
public class MaillonPeriode extends Maillon {

    public MaillonPeriode(Maillon maillon) {
        this.suivant = maillon;

    }

    @Override
    protected List<String> doOperation(List<IObjet> list) {
        List<String> ls = new ArrayList<>();
        if(list.get(0) instanceof IMoyenne){
            IMoyenne a;
            for(IObjet o : list){
                a = (IMoyenne) o;
                ls.add(a.getNomMoyenne());
            }
            return ls;
        }
        if(suivant != null) {
            return suivant.remplirListe(list);
        }
        return ls;
    }
}
