package com.project.baptiste.mesnoteas.general;


import com.project.baptiste.mesnoteas.fabrique.matiere.FabriqueMatiere;
import com.project.baptiste.mesnoteas.fabrique.matiere.IFabriqueMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by Baptiste on 01/06/2015.
 */
public class Moyenne implements IMoyenne {

    private int id;
    private String nomMoyenne;
    private IFabriqueMatiere fabriqueMatiere;
    private Collection<IMatiere> matieres;

    public Moyenne() {
        matieres = new ArrayList<>();
        fabriqueMatiere = new FabriqueMatiere();
    }

    @Override
    public IMatiere creerMatiere(){
        IMatiere m = fabriqueMatiere.createMatiere();
        matieres.add(m);
        return m;
    }

    @Override
    public boolean supprimerMatiere(int i){
        return matieres.remove(i);
    }

    @Override
    public double resultatMoyenne(){
        double r = 0.0;
        int diviseur = 0;
        if(matieres.isEmpty()){
            return r;
        }
        else {
            for (IMatiere m : matieres) {
                r += m.resultatMatiere() * m.getCoef();
                diviseur += m.getCoef();
            }
            return r / diviseur;
        }
    }

    @Override
    public String getNomMoyenne() {
        return nomMoyenne;
    }

    @Override
    public void setNomMoyenne(String nomMoyenne) {
        this.nomMoyenne = nomMoyenne;
    }

    @Override
    public IFabriqueMatiere getFabriqueMatiere() {
        return fabriqueMatiere;
    }

    @Override
    public void setFabriqueMatiere(IFabriqueMatiere fabriqueMatiere) {
        this.fabriqueMatiere = fabriqueMatiere;
    }

    @Override
    public Collection<IMatiere> getMatieres() {
        return matieres;
    }

    @Override
    public void setMatieres(Collection<IMatiere> matieres) {
        this.matieres = matieres;
    }

    @Override
    public String toString(){
        String s =
                "   Période "+nomMoyenne+" : \n";
        for(IMatiere m : matieres){
            s = s+m.toString();
        }
        s = s+ "\n"+
                "   Moyenne période : "+resultatMoyenne()+"\n";
        return s;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
