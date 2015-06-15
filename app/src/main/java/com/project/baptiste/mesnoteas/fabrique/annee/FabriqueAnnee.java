package com.project.baptiste.mesnoteas.fabrique.annee;


import com.project.baptiste.mesnoteas.general.Annee;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;

/**
 * Created by Baptiste on 02/06/2015.
 */
public class FabriqueAnnee implements IFabriqueAnnee {
    private IAnnee annee;

    public FabriqueAnnee() {
    }

    @Override
    public IAnnee createAnnee(){
        annee = new Annee();
        return annee;
    }
}
