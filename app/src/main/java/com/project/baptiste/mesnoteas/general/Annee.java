package com.project.baptiste.mesnoteas.general;


import com.project.baptiste.mesnoteas.fabrique.moyenne.FabriqueMoyenne;
import com.project.baptiste.mesnoteas.fabrique.moyenne.IFabriqueMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class Annee implements IAnnee {
    private int id;
    private String nomAnnee;
    private Collection<IMoyenne> moyennes;
    private IFabriqueMoyenne fabriqueMoyenne;

    public Annee() {
        fabriqueMoyenne = new FabriqueMoyenne();
        moyennes = new ArrayList<>();
    }

    @Override
    public double moyenneAnnee(){
        double r = 0.0;
        int diviseur = 0;
        if(moyennes.isEmpty()){
            return r;
        }
        else{
            for(IMoyenne m : moyennes){
                r += m.resultatMoyenne();
                diviseur += 1;
            }
            return r/diviseur;
        }
    }

    @Override
    public boolean supprimerMoyenne(int i){
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
    public Collection<IMoyenne> getMoyennes() {
        return moyennes;
    }

    @Override
    public void setMoyennes(Collection<IMoyenne> moyennes) {
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
        for (IMoyenne m : moyennes){
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
}
