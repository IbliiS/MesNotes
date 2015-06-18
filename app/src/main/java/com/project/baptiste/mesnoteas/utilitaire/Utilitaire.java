package com.project.baptiste.mesnoteas.utilitaire;

import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 18/06/2015.
 */
public class Utilitaire {

    public List<IObjet> copyList(List<IObjet> list){
        List<IObjet> objetList = new ArrayList<>();
        for(IObjet o : list){
            objetList.add(o);
        }
        return objetList;
    }

    public IObjet copyObjet(IObjet objet){
        IObjet o = objet;
        return o;
    }
}
