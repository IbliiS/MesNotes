package com.project.baptiste.mesnoteas.spinner;

import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 04/07/2015.
 */
public class MaillonMatiere extends Maillon {

    public MaillonMatiere(Maillon maillon){
        this.suivant = maillon;
    }

    @Override
    protected List<String> doOperation(List<IObjet> list) {
        List<String> ls = new ArrayList<>();
        if(list.get(0) instanceof IMatiere){
            IMatiere a;
            for(IObjet o : list){
                a = (IMatiere) o;
                ls.add(a.getNomMatiere());
            }
            return ls;
        }
        if(suivant != null) {
            return suivant.remplirListe(list);
        }
        return ls;
    }
}
