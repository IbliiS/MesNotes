package com.project.baptiste.mesnoteas.general;


import com.project.baptiste.mesnoteas.fabrique.matiere.FabriqueMatiere;
import com.project.baptiste.mesnoteas.fabrique.matiere.IFabriqueMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Baptiste on 01/06/2015.
 */
public class Moyenne implements IMoyenne {

    private int id;
    private String nomMoyenne;
    private IFabriqueMatiere fabriqueMatiere;
    private double moyenne;
    private List<IObjet> matieres;

    public Moyenne() {
        matieres = new ArrayList<>();
        fabriqueMatiere = new FabriqueMatiere();
        moyenne = 0;
        nomMoyenne = "";
    }

    @Override
    public IMatiere creerMatiere(){
        IMatiere m = fabriqueMatiere.createMatiere();
        matieres.add(m);
        return m;
    }

    @Override
    public IObjet supprimerMatiere(int i){
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
            IMatiere m;
            for ( IObjet o : matieres) {
                m = (IMatiere) o;
                r += m.resultatMatiere() * m.getCoef();
                diviseur += m.getCoef();
            }
            moyenne = r / diviseur;
            return moyenne;
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
    public List<IObjet> getMatieres() {
        return matieres;
    }

    @Override
    public void setMatieres(List<IObjet> matieres) {
        this.matieres = matieres;
    }

    @Override
    public String toString(){
        String s =
                "   Période "+nomMoyenne+" : \n";
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
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

    @Override
    public double getMoyenne() {
        return resultatMoyenne();
    }
    @Override
    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IMoyenne){
            IMoyenne m = (IMoyenne) o;
            return m.getId() == this.id;
        }
        return false;
    }
}
