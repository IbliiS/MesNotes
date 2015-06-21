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

    /**
     * Ajuste le string de la moyenne pour qu'il n'y ait que 2 chiffre après la virgule
     * @param d la moyenne
     * @return le string coupé
     */
    public String coupeMoyenne(Double d){
        String moy;
        String nombre =String.valueOf(d);
        moy = nombre.concat("000");
        if(d>10){
            moy = moy.substring(0,5);
        }
        else {
            moy = moy.substring(0,4);

        }

        return moy;
    }
}
