package com.project.baptiste.mesnoteas.general;


import com.project.baptiste.mesnoteas.fabrique.moyenne.FabriqueMoyenne;
import com.project.baptiste.mesnoteas.fabrique.moyenne.IFabriqueMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class Annee implements IAnnee {
    private int id;
    private String nomAnnee;
    private List<IObjet> moyennes;
    private IFabriqueMoyenne fabriqueMoyenne;
    private double moyenne;

    public Annee() {
        fabriqueMoyenne = new FabriqueMoyenne();
        moyennes = new ArrayList<>();
        moyenne = 0;
        nomAnnee = "";
    }

    @Override
    public double moyenneAnnee(){
        double r = 0.0;
        int diviseur = 0;
        if(moyennes.isEmpty()){
            moyenne = -1;
            return moyenne;
        }
        else{
            IMoyenne m;
            for(IObjet o : moyennes){
                m = (IMoyenne) o;
                if( m.getMatieres().size() != 0 ) {
                    if(m.resultatMoyenne() != -1){
                        r += m.resultatMoyenne();
                        diviseur += 1;
                    }
                }
            }
            if(diviseur == 0){
                moyenne = -1;
            }
            else{
                moyenne = r/diviseur;
            }
            return moyenne;
        }
    }

    @Override
    public IObjet supprimerMoyenne(int i){
        return moyennes.remove(i);
    }

    @Override
    public IMoyenne creerMoyenne(){
        IMoyenne m = fabriqueMoyenne.createMoyenne();
        moyennes.add(m);
        return m;
    }

    @Override
    public String getNomAnnee() {
        return nomAnnee;
    }

    @Override
    public void setNomAnnee(String nomAnnee) {
        this.nomAnnee = nomAnnee;
    }

    @Override
    public List<IObjet> getMoyennes() {
        return moyennes;
    }

    @Override
    public void setMoyennes(List<IObjet> moyennes) {
        this.moyennes = moyennes;
    }

    @Override
    public IFabriqueMoyenne getFabriqueMoyenne() {
        return fabriqueMoyenne;
    }

    @Override
    public void setFabriqueMoyenne(IFabriqueMoyenne fabriqueMoyenne) {
        this.fabriqueMoyenne = fabriqueMoyenne;
    }

    @Override
    public String toString() {
        String s;
        s = "Annee "+nomAnnee+ " :\n";
        IMoyenne m;
        for (IObjet o : moyennes){
            m = (IMoyenne) o;
            s = s+m.toString()+"\n";
        }
        return s+"\nMoyenne année : "+moyenneAnnee()+"\n";
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
        return moyenneAnnee();
    }

    @Override
    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IAnnee){
            IAnnee a = (IAnnee) o;
            return a.getId() == this.id;
        }
        return false;
    }
}
