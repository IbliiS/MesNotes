package com.project.baptiste.mesnoteas.fabrique.matiere;


import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class FabriqueMatiere implements IFabriqueMatiere {

    private IMatiere matiere;

    public FabriqueMatiere() {
    }

    @Override
    public IMatiere createMatiere(){
        matiere = new Matiere();
        return matiere;
    }

}
